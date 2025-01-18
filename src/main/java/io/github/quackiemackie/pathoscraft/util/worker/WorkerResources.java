package io.github.quackiemackie.pathoscraft.util.worker;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public record WorkerResources(ItemStack item) {

    public static final Codec<WorkerResources> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.fieldOf("item").forGetter(WorkerResources::item)
    ).apply(instance, WorkerResources::new));

    @Override
    public String toString() {
        return "Resources: {" +
                "item='" + item + '\'' +
                '}';
    }
}
