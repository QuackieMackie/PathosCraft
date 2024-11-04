package net.quackiemackie.pathoscraft.network;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.quackiemackie.pathoscraft.network.payload.AstralFormStatus;
import net.quackiemackie.pathoscraft.registers.ModAttachments;

public class ClientPayloadHandler {
    /**
     * A basic example of a server -> client payload.
     */
    public static void handleAstralFormStatus(AstralFormStatus status, IPayloadContext context) {
        context.enqueueWork(() -> {
                    Player player = context.player();
                    ((IAttachmentHolder) player).setData(ModAttachments.IN_ASTRAL_FORM.get(), status.status());
                })
                .exceptionally(e -> {
                    context.disconnect(Component.translatable("networking.pathoscraft.client.failed", e.getMessage()));
                    return null;
                });
    }
}