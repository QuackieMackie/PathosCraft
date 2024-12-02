package net.quackiemackie.pathoscraft.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.network.PacketDistributor;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.network.payload.QuestMenuActiveQuestsPayload;
import net.quackiemackie.pathoscraft.quest.Quest;
import net.quackiemackie.pathoscraft.quest.QuestObjective;
import net.quackiemackie.pathoscraft.registers.PathosAttachments;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestHandler {
    private static final List<Quest> quests = new ArrayList<>();

    /**
     * Loads quests from the JSON files in the specified directory.
     *
     * @param resourceManager the resource manager to get the quest files.
     */
    public static void loadQuests(ResourceManager resourceManager) {
        ResourceLocation questFolder = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "quest");

        Map<ResourceLocation, Resource> resources = resourceManager.listResources(questFolder.getPath(), path -> path.getPath().endsWith(".json"));

        quests.clear();

        for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            Resource resource = entry.getValue();
            try (InputStreamReader reader = new InputStreamReader(resource.open())) {
                JsonElement jsonElement = JsonParser.parseReader(reader);
                Quest quest = Quest.CODEC.parse(JsonOps.INSTANCE, jsonElement)
                        .result()
                        .orElseThrow(() -> new IllegalStateException("Failed to parse quest: " + jsonElement));
                quests.add(quest);
            } catch (IOException e) {
                PathosCraft.LOGGER.error("Failed to load quest from file: {}{}", resourceLocation, e.getMessage());
            }
        }
        PathosCraft.LOGGER.info("Loaded {} quests.", quests.size());
    }

    /**
     * Gets the list of all loaded quests.
     *
     * @return the list of loaded quests.
     */
    public static List<Quest> getQuests() {
        return quests;
    }

    /**
     * Finds a quest by its ID.
     *
     * @param questId the ID of the quest to find.
     * @return the quest with the specified ID, or null if not found.
     */
    public static Quest getQuestById(int questId) {
        return quests.stream()
                .filter(quest -> quest.getQuestId() == questId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds quests by their type.
     *
     * @param questType the type of the quests to find.
     * @return the list of quests with the specified type.
     */
    public static List<Quest> getQuestsByType(int questType) {
        List<Quest> questsByType = new ArrayList<>();
        for (Quest quest : quests) {
            if (quest.getQuestType() == questType) {
                questsByType.add(quest);
            }
        }
        return questsByType;
    }

    /**
     * Checks if the picked-up item is relevant to any of the player's active quests.
     *
     * @param player        the player who picked up the item
     * @param itemRegistry  the registry name of the picked-up item
     * @return true if the item is relevant to an active quest, false otherwise
     */
    public static boolean isRelevantQuestItem(Player player, String itemRegistry) {
        // Retrieve the player's active quests
        List<Quest> activeQuests = ((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get());

        // Loop through all objectives of active quests to see if the item is a target
        for (Quest quest : activeQuests) {
            for (QuestObjective objective : quest.getQuestObjectives()) {
                // Check if the objective action is "collect" and the target matches the item
                if ("collect".equals(objective.getAction()) && itemRegistry.equals(objective.getTarget())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Updates the player's progress on a specific quest objective and notifies the client.
     *
     * @param player       the player whose progress is being updated
     * @param serverPlayer the server-side representation of the player
     * @param activeQuests the list of active quests of the player
     * @param objective    the specific quest objective that's being updated
     * @param quest        the quest to which the objective belongs
     */
    private static void updateProgress(Player player, ServerPlayer serverPlayer, List<Quest> activeQuests, QuestObjective objective, Quest quest) {
        PathosCraft.LOGGER.info("Progress updated: {Quest ID: {}, Progression: {}}", quest.getQuestId(), objective.getProgress());
        player.setData(PathosAttachments.ACTIVE_QUESTS.get(), activeQuests);
        PacketDistributor.sendToPlayer(serverPlayer, new QuestMenuActiveQuestsPayload(activeQuests));
    }

    /**
     * Updates the player's quest progress when a target is killed.
     *
     * @param player       the player who killed the target
     * @param killedTarget the identifier of the target that was killed
     */
    public static void updateQuestKillProgress(Player player, String killedTarget) {
        List<Quest> activeQuests = ((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get());

        // Iterate through each quest to find matching objectives
        for (Quest quest : activeQuests) {
            for (QuestObjective objective : quest.getQuestObjectives()) {
                // Check if the objective is a "kill" action for the killed target
                if ("kill".equals(objective.getAction()) && killedTarget.equals(objective.getTarget())) {
                    int currentProgress = objective.getProgress();

                    // Only update progress if the current progress is less than the required quantity
                    if (currentProgress < objective.getQuantity()) {
                        ServerPlayer serverPlayer = (ServerPlayer) player;

                        // Increment the progress by one
                        objective.setProgress(currentProgress + 1);

                        // Update progress and notify the client
                        updateProgress(player, serverPlayer, activeQuests, objective, quest);
                    }
                }
            }
        }
    }

    /**
     * Updates the quest progress for a player when they pick up items.
     *
     * @param player       the player whose quest progress will be updated
     * @param pickedUpItem the item that was picked up
     * @param quantity     the quantity of the item that was picked up
     */
    public static void updateQuestPickupProgress(Player player, String pickedUpItem, int quantity) {
        List<Quest> activeQuests = ((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get());
        int remainingQuantity = quantity;

        for (Quest quest : activeQuests) {
            for (QuestObjective objective : quest.getQuestObjectives()) {
                if ("collect".equals(objective.getAction()) && pickedUpItem.equals(objective.getTarget()) && remainingQuantity > 0) {
                    int currentProgress = objective.getProgress();
                    int requiredQuantity = objective.getQuantity();

                    if (currentProgress < requiredQuantity) {
                        int progressIncrement = Math.min(remainingQuantity, requiredQuantity - currentProgress);

                        objective.setProgress(currentProgress + progressIncrement);
                        remainingQuantity -= progressIncrement;

                        updateProgress(player, (ServerPlayer) player, activeQuests, objective, quest);

                        // Exit the loop if there are no remaining items
                        if (remainingQuantity <= 0) {
                            return;
                        }
                    }
                }
            }
        }
    }

    public static boolean willUpdateQuestProgress(Player player, String itemRegistry) {
        List<Quest> activeQuests = ((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get());

        for (Quest quest : activeQuests) {
            for (QuestObjective objective : quest.getQuestObjectives()) {
                if ("collect".equals(objective.getAction()) && itemRegistry.equals(objective.getTarget())
                        && objective.getProgress() < objective.getQuantity()) {
                    return true;
                }
            }
        }

        return false;
    }

    public static int getAmountForQuest(Player player, String itemRegistry) {
        List<Quest> activeQuests = ((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get());
        int totalRequiredQuantity = 0;

        for (Quest quest : activeQuests) {
            for (QuestObjective objective : quest.getQuestObjectives()) {
                if ("collect".equals(objective.getAction()) && itemRegistry.equals(objective.getTarget())) {
                    int currentProgress = objective.getProgress();
                    int requiredQuantity = objective.getQuantity();
                    if (currentProgress < requiredQuantity) {
                        totalRequiredQuantity += (requiredQuantity - currentProgress);
                    }
                }
            }
        }

        return totalRequiredQuantity;
    }
}