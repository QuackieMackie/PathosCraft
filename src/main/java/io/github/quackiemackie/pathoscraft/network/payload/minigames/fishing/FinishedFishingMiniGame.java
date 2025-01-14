package io.github.quackiemackie.pathoscraft.network.payload.minigames.fishing;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import io.github.quackiemackie.pathoscraft.PathosCraft;

public record FinishedFishingMiniGame(int score) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<FinishedFishingMiniGame> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "finished_fishing_mini_game"));

    public static final StreamCodec<ByteBuf, FinishedFishingMiniGame> STREAM_CODEC = StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    FinishedFishingMiniGame::score,
                    FinishedFishingMiniGame::new
            );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
