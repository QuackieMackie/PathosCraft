package net.quackiemackie.pathoscraft.network;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.network.payload.AstralFormStatusPayload;
import net.quackiemackie.pathoscraft.network.payload.QuestMenuActiveQuestsPayload;
import net.quackiemackie.pathoscraft.registers.PathosAttachments;

public class ClientPayloadHandler {
    /**
     * A basic example of a server -> client payload.
     */
    public static void handleAstralFormStatus(AstralFormStatusPayload status, IPayloadContext context) {
        context.enqueueWork(() -> {
                    Player player = context.player();
                    ((IAttachmentHolder) player).setData(PathosAttachments.IN_ASTRAL_FORM.get(), status.status());
                }).exceptionally(e -> {
                    context.disconnect(Component.translatable("networking.pathoscraft.client.failed", e.getMessage()));
                    return null;
                });
    }

    public static void handleQuestMenuActiveQuests(QuestMenuActiveQuestsPayload data, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            player.setData(PathosAttachments.ACTIVE_QUESTS.get(), data.questIds());
            PathosCraft.LOGGER.info("Updated the clients active quests for player {}: {}", player.getName().getString(), data.questIds());
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("networking.pathoscraft.client.failed", e.getMessage()));
            return null;
        });
    }
}