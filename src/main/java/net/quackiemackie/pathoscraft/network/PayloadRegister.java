package net.quackiemackie.pathoscraft.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.quackiemackie.pathoscraft.network.payload.IPayloadHandler;
import net.quackiemackie.pathoscraft.network.payload.keybinds.AstralFormKeyPress;
import net.quackiemackie.pathoscraft.network.payload.abilities.astralForm.AstralFormStatus;
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

        registrar.playBidirectional(AstralFormStatus.TYPE, AstralFormStatus.STREAM_CODEC,
                new DirectionalPayloadHandler<>(ClientPayloadHandler::handleAstralFormStatus, IPayloadHandler.noOperation()));

        registrar.playBidirectional(AstralFormKeyPress.TYPE, AstralFormKeyPress.STREAM_CODEC,
                new DirectionalPayloadHandler<>(IPayloadHandler.noOperation(), ServerPayloadHandler::handleAstralFormKeyPress));

        registrar.playBidirectional(AddActiveQuest.TYPE, AddActiveQuest.STREAM_CODEC,
                new DirectionalPayloadHandler<>(IPayloadHandler.noOperation(), ServerPayloadHandler::handleAddActiveQuest));

        registrar.playBidirectional(RemoveActiveQuest.TYPE, RemoveActiveQuest.STREAM_CODEC,
                new DirectionalPayloadHandler<>(IPayloadHandler.noOperation(), ServerPayloadHandler::handleRemoveActiveQuest));

        registrar.playBidirectional(SwapActiveQuests.TYPE, SwapActiveQuests.STREAM_CODEC,
                new DirectionalPayloadHandler<>(IPayloadHandler.noOperation(), ServerPayloadHandler::handleSwapActiveQuest));

        registrar.playBidirectional(UpdateProgressActiveQuest.TYPE, UpdateProgressActiveQuest.STREAM_CODEC,
                new DirectionalPayloadHandler<>(ClientPayloadHandler::handleUpdateProgressActiveQuest, IPayloadHandler.noOperation()));

        registrar.playBidirectional(SyncActiveQuests.TYPE, SyncActiveQuests.STREAM_CODEC,
                new DirectionalPayloadHandler<>(ClientPayloadHandler::handleSyncActiveQuests, IPayloadHandler.noOperation()));

        registrar.playBidirectional(AddCompletedQuest.TYPE, AddCompletedQuest.STREAM_CODEC,
                new DirectionalPayloadHandler<>(IPayloadHandler.noOperation(), ServerPayloadHandler::handleAddCompletedQuest));

        registrar.playBidirectional(ClearCompletedQuests.TYPE, ClearCompletedQuests.STREAM_CODEC,
                new DirectionalPayloadHandler<>(ClientPayloadHandler::handleSyncCompletedQuests, IPayloadHandler.noOperation()));

        registrar.playBidirectional(SyncCompletedQuests.TYPE, SyncCompletedQuests.STREAM_CODEC,
                new DirectionalPayloadHandler<>(ClientPayloadHandler::handleClearCompletedQuests, IPayloadHandler.noOperation()));
    }
}
