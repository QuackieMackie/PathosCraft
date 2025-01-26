package io.github.quackiemackie.pathoscraft.util.worker;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record WorkerStationMaps(List<FilledMap> maps) {
    public static final Codec<WorkerStationMaps> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FilledMap.CODEC.listOf().fieldOf("maps").forGetter(WorkerStationMaps::maps)
    ).apply(instance, WorkerStationMaps::new));


}
