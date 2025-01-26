package io.github.quackiemackie.pathoscraft.util.worker;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FilledMap(int slot, int mapId) {
    public static final Codec<FilledMap> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("slot").forGetter(FilledMap::slot),
            Codec.INT.fieldOf("mapId").forGetter(FilledMap::mapId)
    ).apply(instance, FilledMap::new));
}