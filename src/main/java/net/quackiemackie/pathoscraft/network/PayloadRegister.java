package net.quackiemackie.pathoscraft.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.quackiemackie.pathoscraft.network.payload.keybinds.AstralFormKeyPress;
import net.quackiemackie.pathoscraft.network.payload.abilities.astralForm.AstralFormStatus;
import net.quackiemackie.pathoscraft.network.payload.minigames.fishing.FinishedFishingMiniGame;
import net.quackiemackie.pathoscraft.network.payload.minigames.fishing.StartFishingMiniGame;
import net.quackiemackie.pathoscraft.network.payload.quest.active.*;
import net.quackiemackie.pathoscraft.network.payload.quest.completed.AddCompletedQuest;
import net.quackiemackie.pathoscraft.network.payload.quest.completed.ClearCompletedQuests;
import net.quackiemackie.pathoscraft.network.payload.quest.completed.SyncCompletedQuests;

public class PayloadRegister {
    /**
     * Registers various payload handlers to handle client-to-server and server-to-client communications.
     *
     * @param event The event containing the payload registrar to which handlers are registered.
     */
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1").executesOn(HandlerThread.MAIN);

        // Abilities
        registrar.commonToClient(AstralFormStatus.TYPE, AstralFormStatus.STREAM_CODEC, ClientPayloadHandler::handleAstralFormStatus);

        // Key binds
        registrar.commonToServer(AstralFormKeyPress.TYPE, AstralFormKeyPress.STREAM_CODEC, ServerPayloadHandler::handleAstralFormKeyPress);

        // Quests
        registrar.commonToServer(AddActiveQuest.TYPE, AddActiveQuest.STREAM_CODEC, ServerPayloadHandler::handleAddActiveQuest);
        registrar.commonToServer(RemoveActiveQuest.TYPE, RemoveActiveQuest.STREAM_CODEC, ServerPayloadHandler::handleRemoveActiveQuest);
        registrar.commonToServer(SwapActiveQuests.TYPE, SwapActiveQuests.STREAM_CODEC, ServerPayloadHandler::handleSwapActiveQuest);
        registrar.commonToClient(UpdateProgressActiveQuest.TYPE, UpdateProgressActiveQuest.STREAM_CODEC, ClientPayloadHandler::handleUpdateProgressActiveQuest);
        registrar.commonToClient(SyncActiveQuests.TYPE, SyncActiveQuests.STREAM_CODEC, ClientPayloadHandler::handleSyncActiveQuests);

        registrar.commonToServer(AddCompletedQuest.TYPE, AddCompletedQuest.STREAM_CODEC, ServerPayloadHandler::handleAddCompletedQuest);
        registrar.commonToClient(ClearCompletedQuests.TYPE, ClearCompletedQuests.STREAM_CODEC, ClientPayloadHandler::handleSyncCompletedQuests);
        registrar.commonToClient(SyncCompletedQuests.TYPE, SyncCompletedQuests.STREAM_CODEC, ClientPayloadHandler::handleClearCompletedQuests);

        // Mini Games
        registrar.commonToClient(StartFishingMiniGame.TYPE, StartFishingMiniGame.STREAM_CODEC, ClientPayloadHandler::handleStartMiniGame);
        registrar.commonToServer(FinishedFishingMiniGame.TYPE, FinishedFishingMiniGame.STREAM_CODEC, ServerPayloadHandler::handleFinishedFishingMiniGame);
    }
}
