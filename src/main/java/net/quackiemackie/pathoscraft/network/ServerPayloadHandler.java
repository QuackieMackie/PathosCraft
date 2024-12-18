package net.quackiemackie.pathoscraft.network;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.network.payload.minigames.excavation.FinishedExcavationMiniGame;
import net.quackiemackie.pathoscraft.network.payload.minigames.fishing.FinishedFishingMiniGame;
import net.quackiemackie.pathoscraft.util.handlers.abilities.AstralFormHandler;
import net.quackiemackie.pathoscraft.network.payload.keybinds.AstralFormKeyPress;
import net.quackiemackie.pathoscraft.util.handlers.minigames.ExcavationHandler;
import net.quackiemackie.pathoscraft.util.handlers.minigames.FishingHandler;
import net.quackiemackie.pathoscraft.util.handlers.quest.QuestPayloadHandler;
import net.quackiemackie.pathoscraft.network.payload.quest.active.AddActiveQuest;
import net.quackiemackie.pathoscraft.network.payload.quest.active.RemoveActiveQuest;
import net.quackiemackie.pathoscraft.network.payload.quest.active.SwapActiveQuests;
import net.quackiemackie.pathoscraft.network.payload.quest.completed.AddCompletedQuest;
import net.quackiemackie.pathoscraft.util.quest.Quest;
import net.quackiemackie.pathoscraft.registers.PathosAttachments;

import java.util.ArrayList;
import java.util.List;

/**
 * The ServerPayloadHandler class contains static methods to handle various server payloads received from the client in the PathosCraft mod.
 * These methods are responsible for processing and updating server-side state based on the data received.
 * Each method corresponds to a specific type of payload and contains logic to handle and log the appropriate updates.
 */
public class ServerPayloadHandler {
    public static void handleAstralFormKeyPress(AstralFormKeyPress data, IPayloadContext context) {
        Player player = context.player();
        if (((IAttachmentHolder) player).getData(PathosAttachments.IN_ASTRAL_FORM.get())) {
            AstralFormHandler.leaveAstralForm(player);
        }
    }

    /**
     * Handles the addition of an active quest to the player's active quest list.
     *
     * @param data    the payload containing the quest to be added to the active quests.
     * @param context the payload context containing the player and associated data.
     */
    public static void handleAddActiveQuest(AddActiveQuest data, IPayloadContext context) {
        Player player = context.player();
        List<Quest> activeQuests = new ArrayList<>(((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get()));

        QuestPayloadHandler.addActiveQuest(activeQuests, data.quest(), player);

        PathosCraft.LOGGER.info("Active quest added: {Player: {}, Quest: {}}", player.getName().getString(), data.quest());

    }

    /**
     * Handles the removal of an active quest from the player's active quest list.
     *
     * @param data    the payload containing the quest to be removed from the active quests.
     * @param context the payload context containing the player and associated data.
     */
    public static void handleRemoveActiveQuest(RemoveActiveQuest data, IPayloadContext context) {
        Player player = context.player();
        List<Quest> activeQuests = new ArrayList<>(((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get()));

        QuestPayloadHandler.removeActiveQuest(activeQuests, data.quest(), player);

        PathosCraft.LOGGER.info("Active quest removed: {Player: {}, Quest: {}}", player.getName().getString(), data.quest());
    }

    /**
     * Handles the logic for swapping two active quests in the player's quest list.
     * This method logs the details of the swap, including the player and the two quests involved.
     *
     * @param data    the payload containing the details of the two quests to be swapped.
     *                The firstQuest refers to the quest being removed, and the secondQuest refers
     *                to the quest being added as the new active quest.
     * @param context the payload context containing the player initiating the quest swap and additional relevant data.
     */
    public static void handleSwapActiveQuest(SwapActiveQuests data, IPayloadContext context) {
        Player player = context.player();
        List<Quest> activeQuests = new ArrayList<>(((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get()));
        List<Quest> swappableQuests = data.swappableQuests();

        QuestPayloadHandler.swapActiveQuest(activeQuests, swappableQuests, player);

        PathosCraft.LOGGER.info("Active quest swap request processed: {Player: {}, Swappable Quests: {}}", player.getName().getString(), swappableQuests);
    }

    /**
     * Handles the addition of a completed quest to the player's completed quests list.
     * Logs the event with the player's name and the quest being added.
     *
     * @param data    the payload containing the quest to be added to the completed quests.
     * @param context the payload context containing the player and associated data.
     */
    public static void handleAddCompletedQuest(AddCompletedQuest data, IPayloadContext context) {
        Player player = context.player();
        List<Quest> activeQuests = new ArrayList<>(((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get()));
        List<Quest> completedQuests = new ArrayList<>(((IAttachmentHolder) player).getData(PathosAttachments.COMPLETED_QUESTS.get()));

        QuestPayloadHandler.addCompletedQuest(completedQuests, activeQuests, data.quest(), player);

        PathosCraft.LOGGER.info("Completed quest added: {Player: {}, Quest: {}}", player.getName().getString(), data.quest());
    }

    /**
     * Handles the completion of the fishing mini-game by logging the player's score
     * and rewarding the player based on their achievements in the mini-game.
     *
     * @param data    the payload containing the score achieved in the fishing mini-game.
     * @param context the payload context containing the player who finished the mini-game
     *                and associated data.
     */
    public static void handleFinishedFishingMiniGame(FinishedFishingMiniGame data, IPayloadContext context) {
        PathosCraft.LOGGER.info("Score: {}, Player: {}", data.score(), context.player().getName().getString());
        FishingHandler.rewardCompletedMiniGame(data.score(), context.player());
    }

    /**
     * Handles the completion of the excavation mini-game by logging the details
     * of the player and the ore they found. Rewards the player based on the amount
     * of ore discovered during the mini-game.
     *
     * @param data    the payload containing information about the completed excavation mini-game,
     *                including the number of ores found by the player.
     * @param context the payload context containing details about the player who completed
     *                the excavation mini-game and their associated data.
     */
    public static void handleFinishedExcavationMiniGame(FinishedExcavationMiniGame data, IPayloadContext context) {
        PathosCraft.LOGGER.info("Found Ore: {}, Player: {}", data.foundOre(), context.player().getName().getString());
        ExcavationHandler.rewardCompletedMiniGame(data.foundOre(), context.player());
    }
}
