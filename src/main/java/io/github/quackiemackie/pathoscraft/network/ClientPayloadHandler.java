package io.github.quackiemackie.pathoscraft.network;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.network.payload.minigames.excavation.StartExcavationMiniGame;
import io.github.quackiemackie.pathoscraft.network.payload.minigames.fishing.StartFishingMiniGame;
import io.github.quackiemackie.pathoscraft.network.payload.minigames.lumbering.StartLumberingMiniGame;
import io.github.quackiemackie.pathoscraft.util.handlers.minigames.ExcavationHandler;
import io.github.quackiemackie.pathoscraft.util.handlers.minigames.FishingHandler;
import io.github.quackiemackie.pathoscraft.util.handlers.minigames.LumberingHandler;
import io.github.quackiemackie.pathoscraft.util.handlers.quest.QuestPayloadHandler;
import io.github.quackiemackie.pathoscraft.network.payload.abilities.astralForm.AstralFormStatus;
import io.github.quackiemackie.pathoscraft.network.payload.quest.active.SyncActiveQuests;
import io.github.quackiemackie.pathoscraft.network.payload.quest.active.UpdateProgressActiveQuest;
import io.github.quackiemackie.pathoscraft.network.payload.quest.completed.ClearCompletedQuests;
import io.github.quackiemackie.pathoscraft.network.payload.quest.completed.SyncCompletedQuests;
import io.github.quackiemackie.pathoscraft.registers.PathosAttachments;

/**
 * The ClientPayloadHandler class contains static methods to handle various client payloads received from the server
 * in the PathosCraft mod. These methods are responsible for processing and updating client-side state based on the
 * data received. Each method corresponds to a specific type of payload and contains logic to handle and log the
 * appropriate updates.
 */
public class ClientPayloadHandler {
    public static void handleAstralFormStatus(AstralFormStatus data, IPayloadContext context) {
        Player player = context.player();
        player.setData(PathosAttachments.IN_ASTRAL_FORM.get(), data.status());
    }

    /**
     * Handles the update of progress for an active quest on the client side. This method is called
     * when an UpdateProgressActiveQuest payload is received, modifying the active quest progress
     * for the player's client-side state.
     *
     * @param data    The payload containing the details of the active quest progress to update.
     * @param context The context providing access to the player and the environment in which the payload is processed.
     */
    public static void handleUpdateProgressActiveQuest(UpdateProgressActiveQuest data, IPayloadContext context) {
        Player player = context.player();
        QuestPayloadHandler.updateActiveQuestProgress(player, data.quest());
    }

    /**
     * Handles syncing the active quests for the client. This method is invoked when a SyncActiveQuests
     * payload is received, and it updates the player's client-side quest data.
     *
     * @param data    The payload containing the list of active quests to be synced.
     * @param context The context providing access to the player and the environment in which the payload is processed.
     */
    public static void handleSyncActiveQuests(SyncActiveQuests data, IPayloadContext context) {
        Player player = context.player();
        player.setData(PathosAttachments.ACTIVE_QUESTS.get(), data.quests());
        PathosCraft.LOGGER.info("Sync Active quests: {Player: {}, Quests: {}}", player.getName().getString(), data.quests());
    }

    /**
     * Handles syncing the completed quests for the client. This method receives a payload containing
     * the list of completed quests and logs the information for update purposes.
     *
     * @param data    The payload containing the list of completed quests to be synced.
     * @param context The context providing access to the player and the environment in which the payload is processed.
     */
    public static void handleSyncCompletedQuests(ClearCompletedQuests data, IPayloadContext context) {
        Player player = context.player();
        player.setData(PathosAttachments.COMPLETED_QUESTS.get(), data.quests());
        PathosCraft.LOGGER.info("Sync Completed quests: {Player: {}, Quests: {}}", player.getName().getString(), data.quests());
    }

    /**
     * Handles the clearing of completed quests for the client-side player. This method synchronizes
     * the completed quests data received in the payload with the client-side state and logs the update.
     *
     * @param data    The payload containing the updated list of completed quests to clear.
     * @param context The context providing access to the player and the environment in which the payload is processed.
     */
    public static void handleClearCompletedQuests(SyncCompletedQuests data, IPayloadContext context) {
        Player player = context.player();
        player.setData(PathosAttachments.COMPLETED_QUESTS.get(), data.quests());
        PathosCraft.LOGGER.info("Cleared Completed quests: {Player: {}, Quests: {}}", player.getName().getString(), data.quests());
    }

    /**
     * Handles the initiation of the fishing mini-game on the client-side. This method
     * is triggered when a StartFishingMiniGame payload is received, initiating the
     * fishing mini-game interface for the player.
     *
     * @param data    The payload containing the data required to start the fishing mini-game.
     * @param context The context providing access to the player and the environment in which the payload is processed.
     */
    public static void handleStartFishingMiniGame(StartFishingMiniGame data, IPayloadContext context) {
        FishingHandler.startMiniGame(context.player());
    }

    /**
     * Handles the initiation of the excavation mini-game on the client-side. This method
     * is triggered when a StartExcavationMiniGame payload is received and initiates the
     * excavation mini-game interface for the player.
     *
     * @param data    The payload containing the data required to start the excavation mini-game.
     * @param context The context providing access to the player and the environment in which the payload is processed.
     */
    public static void handleStartExcavationMiniGame(StartExcavationMiniGame data, IPayloadContext context) {
        ExcavationHandler.startMiniGame(context.player(), data.blockDrop());
        PathosCraft.LOGGER.info("Item drop: {}", data.blockDrop());
    }

    /**
     * Handles the initiation of the lumbering mini-game on the client-side. This method
     * is called when a StartLumberingMiniGame payload is received, triggering the interface
     * for the lumbering mini-game for the player.
     *
     * @param data    The payload containing the data required to start the lumbering mini-game.
     * @param context The context providing access to the player and the environment in which the payload is processed.
     */
    public static void handleStartLmberingMiniGame(StartLumberingMiniGame data, IPayloadContext context) {
        LumberingHandler.startMiniGame(context.player(), data.blockDrop());
    }
}