package net.quackiemackie.pathoscraft.util.handlers.quest;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.util.quest.Quest;
import net.quackiemackie.pathoscraft.registers.PathosAttachments;

import java.util.ArrayList;
import java.util.List;

public class QuestPayloadHandler {

    /**
     * Adds a quest to the player's active quest list if it meets the necessary conditions:
     * the quest is not already completed, is not already an active quest, and the maximum
     * active quest limit has not been reached. If the quest is successfully added, it updates
     * the player's data and logs the operation. If the addition is skipped due to reaching
     * the maximum limit, it logs that as well.
     *
     * @param activeQuests the list of quests currently active for the player
     * @param quest the quest to be added to the active list
     * @param player the player for whom the quest is being added
     */
    public static void addActiveQuest(List<Quest> activeQuests, Quest quest, Player player) {
        if (!QuestHandler.isQuestCompleted(player, quest) && !QuestHandler.isActiveQuest(player, quest)) {
            if (activeQuests.size() < Quest.MAX_ACTIVE_QUESTS) {
                activeQuests.add(quest);
                player.setData(PathosAttachments.ACTIVE_QUESTS.get(), activeQuests);
                PathosCraft.LOGGER.info("Server: Added Quest {} to Player {} Active Quests", quest.id(), player.getName().getString());
            } else {
                PathosCraft.LOGGER.info("Server: Max quest count reached skipped adding {}, for player {}.", quest.id(), player.getName().getString());
            }
        }
    }

    /**
     * Removes a quest from the player's active quest list if the quest is not completed
     * and it is currently active. Updates the player's active quest data and logs the
     * removal operation.
     *
     * @param activeQuests the list of quests currently active for the player
     * @param quest the quest to be removed from the active list
     * @param player the player from whom the quest is being removed
     */
    public static void removeActiveQuest(List<Quest> activeQuests, Quest quest, Player player) {
        if (!QuestHandler.isQuestCompleted(player, quest) && QuestHandler.isActiveQuest(player, quest)) {
            activeQuests.remove(quest);

            for (int i = 0; i < activeQuests.size(); i++) {
                activeQuests.get(i).withActiveSlot(i);
            }

            player.setData(PathosAttachments.ACTIVE_QUESTS.get(), activeQuests);

            QuestHandler.returnItems(player, quest);

            PathosCraft.LOGGER.info("Server: Removed Quest {ID: {}, Slot: {}} from Player {} Active Quests and reassigned slots.",
                    quest.id(), quest.activeSlot(), player.getName().getString());

            for (Quest activeQuest : activeQuests) {
                PathosCraft.LOGGER.info("Quest {ID: {}, Assigned Slot: {}}", activeQuest.id(), activeQuest.activeSlot());
            }
        } else {
            PathosCraft.LOGGER.warn("Server: Quest {ID: {}} could not be removed. Either it is already completed or not active for Player {}.",
                    quest.id(), player.getName().getString());
        }
    }

    /**
     * Handles the process of updating the progress of a player's active quest.
     * If the quest specified by the updatedQuest parameter is found in the player's
     * active quests, it replaces the existing quest with the updated quest.
     * Updates the player's active quest data and logs the operation. Logs warnings
     * if the active quest list is empty or if the specified quest is not found.
     *
     * @param player       the player whose active quest progress is being updated.
     * @param updatedQuest the quest with updated progress information.
     */
    public static void updateActiveQuestProgress(Player player, Quest updatedQuest) {
        List<Quest> activeQuests = new ArrayList<>(((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get()));

        if (activeQuests.isEmpty()) {
            PathosCraft.LOGGER.warn("Client: Active quests are empty for player {}, cannot handle quest progress update.",
                    player.getName().getString());
            return;
        }

        boolean questUpdated = false;

        for (int i = 0; i < activeQuests.size(); i++) {
            Quest quest = activeQuests.get(i);

            if (quest.id() == updatedQuest.id()) {
                activeQuests.set(i, updatedQuest);
                questUpdated = true;

                PathosCraft.LOGGER.info("Client: Updated quest ID {} for player {}: {}",
                        updatedQuest.id(), player.getName().getString(), updatedQuest);
                break;
            }
        }

        if (!questUpdated) {
            PathosCraft.LOGGER.warn("Client: Quest ID {} not found in active quests for player {}, cannot update progress.",
                    updatedQuest.id(), player.getName().getString());
            return;
        }

        ((IAttachmentHolder) player).setData(PathosAttachments.ACTIVE_QUESTS.get(), activeQuests);
    }

    /**
     * Swaps the active quest slots of two specified quests within the player's active quest list.
     * Logs warnings if the operation fails or if invalid conditions are detected.
     *
     * @param activeQuests    the list of quests currently active for the player.
     * @param swappableQuests a list containing exactly two quests to swap in the active list.
     * @param player          the player for whom the quests are being swapped.
     */
    public static void swapActiveQuest(List<Quest> activeQuests, List<Quest> swappableQuests, Player player) {
        if (swappableQuests.size() != 2) {
            PathosCraft.LOGGER.warn("Server: SwapActiveQuests payload must contain exactly 2 quests. Received size: {}", swappableQuests.size());
            return;
        }

        Quest firstQuest = swappableQuests.get(0);
        Quest secondQuest = swappableQuests.get(1);

        PathosCraft.LOGGER.info("Server {firstQuest: {}, secondQuest: {}}", firstQuest, secondQuest);

        if (firstQuest.id() == secondQuest.id()) {
            PathosCraft.LOGGER.warn("Server: Attempted to swap the same quest {ID: {}} with itself for Player {}",
                    firstQuest.id(), player.getName().getString());
            return;
        }

        Quest questInFirstSlot = activeQuests.stream()
                .filter(quest -> quest.id() == firstQuest.id())
                .findFirst()
                .orElse(null);

        Quest questInSecondSlot = activeQuests.stream()
                .filter(quest -> quest.id() == secondQuest.id())
                .findFirst()
                .orElse(null);

        if (questInFirstSlot != null && questInSecondSlot != null) {
            int firstActiveSlot = questInFirstSlot.activeSlot();
            int secondActiveSlot = questInSecondSlot.activeSlot();

            questInFirstSlot.withActiveSlot(secondActiveSlot);
            questInSecondSlot.withActiveSlot(firstActiveSlot);

            player.setData(PathosAttachments.ACTIVE_QUESTS.get(), activeQuests);

            PathosCraft.LOGGER.info("Server: Swapped Quest {ID: {}, Old Slot: {}, New Slot: {}} with Quest {ID: {}, Old Slot: {}, New Slot: {}} for Player {}", questInFirstSlot.id(), firstActiveSlot, secondActiveSlot, questInSecondSlot.id(), secondActiveSlot, firstActiveSlot, player.getName().getString());
        } else {
            PathosCraft.LOGGER.warn("Server: Failed to swap quests. Quests with IDs {} and {} were not found in the active quest list for Player {}", firstQuest.id(), secondQuest.id(), player.getName().getString());
        }
    }

    /**
     * Adds a completed quest to the player's list of completed quests if all conditions are met.
     * This includes verifying that the quest is marked as completed and is currently active.
     * Once added, the player's completed quest data is updated and the operation is logged.
     *
     * @param completedQuests the list of quests that the player has completed
     * @param quest the quest to be added to the completed list
     * @param player the player whose completed quests are being updated
     */
    public static void addCompletedQuest(List<Quest> completedQuests, List<Quest> activeQuests, Quest quest, Player player) {
        if (QuestHandler.isQuestCompleted(player, quest)) {
            PathosCraft.LOGGER.warn("Server: Cannot add Quest {} to Player {} Completed Quests because it is already on the completed list.", quest.id(), player.getName().getString());
            return;
        }

        if (!QuestHandler.isQuestObjectiveCompleted(quest)) {
            PathosCraft.LOGGER.warn("Server: Cannot add Quest {} to Player {} Completed Quests because it is not completed.", quest.id(), player.getName().getString());
            return;
        }

        QuestHandler.returnItems(player, quest);
        QuestHandler.giveRewardsToPlayer(player, quest);

        activeQuests.remove(quest);

        for (int i = 0; i < activeQuests.size(); i++) {
            activeQuests.get(i).withActiveSlot(i);
        }

        player.setData(PathosAttachments.ACTIVE_QUESTS.get(), activeQuests);

        completedQuests.add(quest);
        player.setData(PathosAttachments.COMPLETED_QUESTS.get(), completedQuests);

        PathosCraft.LOGGER.info("Server: Given rewards for Quest {} to Player {}.", quest.id(), player.getName().getString());
        PathosCraft.LOGGER.info("Server: Added Quest {} to Player {} Completed Quests", quest.id(), player.getName().getString());
    }
}
