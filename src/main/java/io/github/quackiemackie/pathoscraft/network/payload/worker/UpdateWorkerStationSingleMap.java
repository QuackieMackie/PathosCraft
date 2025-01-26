package io.github.quackiemackie.pathoscraft.network.payload.worker;

import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.util.worker.FilledMap;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record UpdateWorkerStationSingleMap(FilledMap filledMap) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UpdateWorkerStationSingleMap> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "worker_station_single_map_data"));

    public static final StreamCodec<ByteBuf, UpdateWorkerStationSingleMap> STREAM_CODEC = ByteBufCodecs.fromCodec(FilledMap.CODEC).map(
            UpdateWorkerStationSingleMap::new,
            UpdateWorkerStationSingleMap::filledMap
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
