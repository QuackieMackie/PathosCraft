package io.github.quackiemackie.pathoscraft.network.payload.minigames.lumbering;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import io.github.quackiemackie.pathoscraft.PathosCraft;

public record StartLumberingMiniGame(ItemStack blockDrop) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<StartLumberingMiniGame> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "start_lumbering_mini_game"));

    public static final StreamCodec<ByteBuf, StartLumberingMiniGame> STREAM_CODEC = ByteBufCodecs.fromCodec(ItemStack.CODEC).map(
            StartLumberingMiniGame::new,
            StartLumberingMiniGame::blockDrop
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}