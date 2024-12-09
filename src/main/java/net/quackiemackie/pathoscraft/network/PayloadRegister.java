package net.quackiemackie.pathoscraft.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.quackiemackie.pathoscraft.network.payload.AstralFormKeyPressPayload;
import net.quackiemackie.pathoscraft.network.payload.AstralFormStatusPayload;
import net.quackiemackie.pathoscraft.network.payload.QuestMenuActiveQuestsPayload;
import net.quackiemackie.pathoscraft.network.payload.QuestMenuCompletedQuestsPayload;

public class PayloadRegister {
    /**
     * Handles the registration and management of payloads for the PathosCraft mod.
     */
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1").executesOn(HandlerThread.MAIN);

        registrar.playBidirectional(
                AstralFormStatusPayload.TYPE,
                AstralFormStatusPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientPayloadHandler::handleAstralFormStatus,
                        (payload, context) -> {}
                )
        );

        registrar.playBidirectional(
                AstralFormKeyPressPayload.TYPE,
                AstralFormKeyPressPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        (payload, context) -> {},
                        ServerPayloadHandler::handleAstralFormKeyPress
                )
        );

        registrar.playBidirectional(
                QuestMenuActiveQuestsPayload.TYPE,
                QuestMenuActiveQuestsPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientPayloadHandler::handleQuestMenuActiveQuests,
                        ServerPayloadHandler::handleQuestMenuActiveQuests
                )
        );

        registrar.playBidirectional(
                QuestMenuCompletedQuestsPayload.TYPE,
                QuestMenuCompletedQuestsPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientPayloadHandler::handleQuestMenuCompletedQuests,
                        ServerPayloadHandler::handleQuestMenuCompletedQuests
                )
        );
    }
}
