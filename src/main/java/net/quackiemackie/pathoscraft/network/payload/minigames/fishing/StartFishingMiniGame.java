package net.quackiemackie.pathoscraft.network.payload.minigames.fishing;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.quackiemackie.pathoscraft.PathosCraft;

public class StartFishingMiniGame implements CustomPacketPayload {
    public static final StartFishingMiniGame INSTANCE = new StartFishingMiniGame();

    public static final CustomPacketPayload.Type<StartFishingMiniGame> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "start_fishing_mini_game"));

    public static final StreamCodec<ByteBuf, StartFishingMiniGame> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    public StartFishingMiniGame() {}

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
