package io.github.quackiemackie.pathoscraft.network.payload.worker;

import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.util.worker.WorkerStationMaps;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record UpdateWorkerStationMapData(WorkerStationMaps mapData) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UpdateWorkerStationMapData> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "worker_station_map_data"));

    public static final StreamCodec<ByteBuf, UpdateWorkerStationMapData> STREAM_CODEC = ByteBufCodecs.fromCodec(WorkerStationMaps.CODEC).map(
            UpdateWorkerStationMapData::new,
            UpdateWorkerStationMapData::mapData
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
