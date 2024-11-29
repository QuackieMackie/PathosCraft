package net.quackiemackie.pathoscraft.network;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.handlers.AstralFormHandler;
import net.quackiemackie.pathoscraft.network.payload.AstralFormKeyPressPayload;
import net.quackiemackie.pathoscraft.network.payload.QuestMenuActiveQuestsPayload;
import net.quackiemackie.pathoscraft.registers.PathosAttachments;

public class ServerPayloadHandler {

    /**
     * A basic example of a client -> server payload.
     */
    public static void handleAstralFormKeyPress(AstralFormKeyPressPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
                    Player player = context.player();
                    if (((IAttachmentHolder) player).getData(PathosAttachments.IN_ASTRAL_FORM.get())) {
                        AstralFormHandler.leaveAstralForm(player);
                    }
                }).exceptionally(e -> {
                    context.disconnect(Component.translatable("networking.pathoscraft.server.failed", e.getMessage()));
                    return null;
                });
    }

    public static void handleQuestMenuActiveQuests(final QuestMenuActiveQuestsPayload data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            player.setData(PathosAttachments.ACTIVE_QUESTS.get(), data.quests());
            PathosCraft.LOGGER.info("Updated the servers active quests for player {}: {}", player.getName().getString(), data.quests());
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("networking.pathoscraft.server.failed", e.getMessage()));
            return null;
        });
    }
}
