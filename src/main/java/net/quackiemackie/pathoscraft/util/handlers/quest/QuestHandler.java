package net.quackiemackie.pathoscraft.util.handlers.quest;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.network.PacketDistributor;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.network.payload.quest.active.UpdateProgressActiveQuest;
import net.quackiemackie.pathoscraft.util.quest.Quest;
import net.quackiemackie.pathoscraft.util.quest.QuestObjective;
import net.quackiemackie.pathoscraft.util.quest.QuestReward;
import net.quackiemackie.pathoscraft.registers.PathosAttachments;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

//TODO:
// Small error with the a duplicate value when the progress isn't at the quantity yet.
// I think it's to do with active buttons or regular slot buttons being rendered
// depending on if it's on the active data attachment or from the files.
// -
// The error I'm receiving is to do with duplicated ids being rendered.

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

        Map<Integer, Set<Integer>> usedSlotsByType = new HashMap<>();

        for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            Resource resource = entry.getValue();
            try (InputStreamReader reader = new InputStreamReader(resource.open())) {
                JsonElement jsonElement = JsonParser.parseReader(reader);
                Quest quest = Quest.CODEC.parse(JsonOps.INSTANCE, jsonElement)
                        .result()
                        .orElseThrow(() -> new IllegalStateException("Failed to parse quest: " + jsonElement));

                // Check for duplicate slot
                usedSlotsByType.putIfAbsent(quest.type(), new HashSet<>());
                if (!usedSlotsByType.get(quest.type()).add(quest.slot())) {
                    throw new IllegalStateException(String.format("Duplicate slot %d found for quest type '%s' in file '%s'", quest.slot(), quest.type(), resourceLocation));
                }

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
     *
     * @type Helper
     */
    public static Quest getQuestById(int questId) {
        return quests.stream()
                .filter(quest -> quest.id() == questId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds quests by their type.
     *
     * @param questType the type of the quests to find.
     * @return the list of quests with the specified type.
     *
     * @type Helper
     */
    public static List<Quest> getQuestsByType(int questType) {
        return quests.stream()
                .filter(quest -> quest.type() == questType)
                .collect(Collectors.toList());
    }

    /**
     * Checks if a quest is completed based on its objectives.
     *
     * @param quest the quest to be checked
     * @return true if all objectives are completed, false otherwise
     *
     * @type Helper
     */
    public static boolean isQuestObjectiveCompleted(Quest quest) {
        return quest.objectives().stream()
                .allMatch(objective -> objective.progress() >= objective.quantity());
    }

    /**
     * Checks if the quest is already on the completed list.
     *
     * @param player the player whose completed quests are being checked
     * @param quest the quest to be checked
     * @return true if the quest is on the completed quest list, false otherwise
     *
     * @type Helper
     */
    public static boolean isQuestCompleted(Player player, Quest quest) {
        List<Quest> completedQuests = player.getData(PathosAttachments.COMPLETED_QUESTS.get());
        return completedQuests.contains(quest);
    }

    /**
     * Checks if the quest is part of the active list.
     *
     * @param player the player whose active quests are being checked
     * @param quest the quest to be checked
     * @return true if the quest is on the active quest list, false otherwise
     *
     * @type Helper
     */
    public static boolean isActiveQuest(Player player, Quest quest) {
        List<Quest> activeQuests = ((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get());
        return activeQuests.contains(quest);
    }

    /**
     * Converts a list of active quests into a map for quick lookup.
     *
     * This method creates a mapping of each quest's unique ID to the corresponding
     * Quest object, allowing efficient retrieval of quests based on their ID.
     * This is useful for scenarios where frequent lookups by quest ID are required,
     * enhancing performance by avoiding repeated list searches.
     *
     * @param activeQuests the list of active Quest objects to be mapped
     * @return a Map where each key is the quest ID and the value is the Quest object
     *
     * @type Helper
     */
    public static Map<Integer, Quest> getActiveQuestMap(List<Quest> activeQuests) {
        return activeQuests.stream()
                .collect(Collectors.toMap(Quest::id, Function.identity()));
    }

    /**
     * Checks if the picked-up item is relevant to any of the player's active quests.
     *
     * @param player        the player who picked up the item
     * @param itemRegistry  the registry name of the picked-up item
     * @return true if the item is relevant to an active quest, false otherwise
     *
     * @type Event Helper
     */
    public static boolean isRelevantQuestItem(Player player, String itemRegistry) {
        // Retrieve the player's active quests
        List<Quest> activeQuests = ((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get());

        // Loop through all objectives of active quests to see if the item is a target
        for (Quest quest : activeQuests) {
            for (QuestObjective objective : quest.objectives()) {
                // Check if the objective action is "collect" and the target matches the item
                if ("collect".equals(objective.action()) && itemRegistry.equals(objective.target())) {
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
     * @param updatedObjective    the specific quest objective that's being updated
     * @param quest        the quest to which the objective belongs
     *
     * @type Helper
     */
    public static void updateProgress(Player player, QuestObjective updatedObjective, Quest quest) {
        PathosCraft.LOGGER.info("Player: {}, Quest Objective: {}, Quest: {}", player, updatedObjective, quest);

        // Replace and re-map the updated objective in the quest
        Quest updatedQuest = new Quest(
                quest.id(),
                quest.name(),
                quest.description(),
                quest.icon(),
                quest.type(),
                quest.preceding(),
                quest.slot(),
                quest.activeSlot(),
                Collections.singletonList(new QuestObjective(updatedObjective.action(), updatedObjective.target(), updatedObjective.quantity(), updatedObjective.progress())),
                quest.rewards()
        );

        // Update the player's active quests
        List<Quest> activeQuests = new ArrayList<>(((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get()));
        activeQuests.replaceAll(q -> q.id() == updatedQuest.id() ? updatedQuest : q);
        ((IAttachmentHolder) player).setData(PathosAttachments.ACTIVE_QUESTS.get(), activeQuests);

        PathosCraft.LOGGER.debug("Updated quest in active quests on server: {}", activeQuests.stream()
                .filter(q -> q.id() == updatedQuest.id())
                .findFirst()
                .orElse(null));

        // Send the updated quest to the client
        if (player instanceof ServerPlayer serverPlayer) {
            PathosCraft.LOGGER.debug("Sending updated quest data to client: {}", updatedQuest);
            PacketDistributor.sendToPlayer(serverPlayer, new UpdateProgressActiveQuest(updatedQuest));
        }
    }

    /**
     * Updates the player's quest progress when a target is killed.
     *
     * @param player       the player who killed the target
     * @param killedTarget the identifier of the target that was killed
     *
     * @type Event Helper
     */
    public static void updateQuestKillProgress(Player player, String killedTarget) {
        List<Quest> activeQuests = ((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get());

        for (Quest quest : activeQuests) {
            for (QuestObjective objective : quest.objectives()) {
                // Check if this is a "kill" objective and matches the killed target
                if ("kill".equals(objective.action()) && killedTarget.equals(objective.target())) {
                    int currentProgress = objective.progress();

                    // Only update progress if it's less than the quantity required
                    if (currentProgress < objective.quantity()) {
                        // Create the updated objective
                        QuestObjective updatedObjective = objective.withProgress(currentProgress + 1);

                        // Pass the updated objective to updateProgress for replacing
                        updateProgress(player, updatedObjective, quest);

                        // Debugging info
                        PathosCraft.LOGGER.debug("Updated kill progress: QuestID={}, Target={}, Progress={}/{}",
                                quest.id(), updatedObjective.target(), updatedObjective.progress(), updatedObjective.quantity());
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
     *
     * @type Event Helper
     */
    public static void updateQuestPickupProgress(Player player, String pickedUpItem, int quantity) {
        List<Quest> activeQuests = ((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get());
        for (Quest quest : activeQuests) {
            for (QuestObjective objective : quest.objectives()) {
                // Match "collect" objectives against the picked-up item
                if ("collect".equals(objective.action()) && pickedUpItem.equals(objective.target()) && quantity > 0) {
                    int currentProgress = objective.progress();
                    int requiredQuantity = objective.quantity();

                    if (currentProgress < requiredQuantity) {
                        // Calculate the progress increment
                        int progressIncrement = Math.min(quantity, requiredQuantity - currentProgress);
                        PathosCraft.LOGGER.debug("Progress increment: {}", progressIncrement);

                        // Create the updated objective
                        QuestObjective updatedObjective = objective.withProgress(currentProgress + progressIncrement);
                        PathosCraft.LOGGER.debug("Updated objective: {}", updatedObjective);

                        // Decrease the remaining quantity by the progress increment
                        quantity -= progressIncrement;

                        // Pass the updated objective to updateProgress
                        updateProgress(player, updatedObjective, quest);

                        // Debugging info
                        PathosCraft.LOGGER.debug("Updated collect progress: QuestID={}, Item={}, Progress={}/{}",
                                quest.id(), updatedObjective.target(), updatedObjective.progress(), updatedObjective.quantity());

                        // Exit if there's no remaining quantity
                        if (quantity <= 0) {
                            return;
                        }
                    }
                }
            }
        }
    }

    /**
     * Determines if collecting an item will update any active quest objectives for a player.
     *
     * @param player       the player who is performing the action
     * @param itemRegistry the registry name of the item being evaluated
     * @return true if collecting the item will progress any of the player's active quests, false otherwise
     *
     * @type Event Helper
     */
    public static boolean willUpdateQuestProgress(Player player, String itemRegistry) {
        List<Quest> activeQuests = ((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get());

        for (Quest quest : activeQuests) {
            for (QuestObjective objective : quest.objectives()) {
                if ("collect".equals(objective.action()) && itemRegistry.equals(objective.target())
                        && objective.progress() < objective.quantity()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Calculates the total number of a specific item needed to complete all relevant quest objectives for a player.
     *
     * @param player       the player whose quests are being checked
     * @param itemRegistry the registry name of the item to check
     * @return the total quantity of the item still needed to fulfill relevant quest objectives
     *
     * @type Event Helper
     */
    public static int getAmountForQuest(Player player, String itemRegistry) {
        List<Quest> activeQuests = ((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get());
        int totalRequiredQuantity = 0;

        for (Quest quest : activeQuests) {
            for (QuestObjective objective : quest.objectives()) {
                if ("collect".equals(objective.action()) && itemRegistry.equals(objective.target())) {
                    int currentProgress = objective.progress();
                    int requiredQuantity = objective.quantity();
                    if (currentProgress < requiredQuantity) {
                        totalRequiredQuantity += (requiredQuantity - currentProgress);
                    }
                }
            }
        }

        return totalRequiredQuantity;
    }

    /**
     * Returns items to the player if the quest has half-completed collect objectives.
     *
     * @param player the player to receive the items
     * @param quest the quest being removed
     *
     * @type Payload
     */
    public static void returnItems(Player player, Quest quest) {
        for (QuestObjective objective : quest.objectives()) {
            if ("collect".equals(objective.action())) {
                int itemsToReturn = objective.progress();
                if (itemsToReturn > 0) {
                    giveItemsToPlayer(player, objective.target(), itemsToReturn);
                }
            }
        }
    }

    /**
     * Gives the reward items to the player.
     *
     * @param player the player to receive the items
     * @param quest the quest being claimed
     *
     * @type Payload
     */
    public static void giveRewardsToPlayer(Player player, Quest quest) {
        for (QuestReward reward : quest.rewards()) {
            giveItemsToPlayer(player, reward.item(), reward.quantity());
        }
    }

    /**
     * Gives the specific quantity of items to the player.
     *
     * @param player   the player receiving the items.
     * @param itemName the name of the item.
     * @param quantity the quantity of items to give.
     */
    public static void giveItemsToPlayer(Player player, String itemName, int quantity) {
        try {
            ResourceLocation itemRegistryName = ResourceLocation.parse(itemName);
            Item item = BuiltInRegistries.ITEM.get(itemRegistryName);
            ItemStack itemStack = new ItemStack(item, quantity);
            player.getInventory().add(itemStack);
        } catch (ResourceLocationException e) {
            PathosCraft.LOGGER.error("Failed to parse item name {}: {}", itemName, e.getMessage());
        }
    }
}