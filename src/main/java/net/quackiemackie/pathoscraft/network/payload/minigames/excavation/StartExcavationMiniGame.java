package net.quackiemackie.pathoscraft.network.payload.minigames.excavation;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.quackiemackie.pathoscraft.PathosCraft;

public class StartExcavationMiniGame implements CustomPacketPayload {
    public static final StartExcavationMiniGame INSTANCE = new StartExcavationMiniGame();

    public static final CustomPacketPayload.Type<StartExcavationMiniGame> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "start_excavation_mini_game"));

    public static final StreamCodec<ByteBuf, StartExcavationMiniGame> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    public StartExcavationMiniGame() {}

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
