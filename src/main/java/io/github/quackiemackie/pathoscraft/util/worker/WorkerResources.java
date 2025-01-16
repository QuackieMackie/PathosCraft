package io.github.quackiemackie.pathoscraft.util.worker;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class WorkerResources {

    public static final Codec<WorkerResources> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("item").forGetter(WorkerResources::getItem),
            Codec.INT.fieldOf("quantity").forGetter(WorkerResources::getQuantity)
    ).apply(instance, WorkerResources::new));

    private final String item;
    private final int quantity;

    public WorkerResources(String item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public String getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Resources: {" +
                "item='" + item + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
