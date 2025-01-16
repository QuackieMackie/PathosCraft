package io.github.quackiemackie.pathoscraft.util.worker;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public class Worker {
    public static final Codec<Worker> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("id").forGetter(Worker::getId),
            Codec.INT.fieldOf("type").forGetter(Worker::getType),
            WorkerResources.CODEC.listOf().fieldOf("resources").forGetter(Worker::getResources)
    ).apply(instance, Worker::new));

    private final int id; // 0 = Naive, 1 = Normal, 2 = Skilled, 3 = Professional, 4 = Artisan
    private int type;
    private List<WorkerResources> resources;

    public Worker(int id, int type, List<WorkerResources> resources) {
        this.id = id;
        this.type = type;
        this.resources = resources;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public List<WorkerResources> getResources() {
        return resources;
    }

    @Override
    public String toString() {
        return "Worker: {" +
                "id=" + id +
                ", type=" + type +
                ", resources=" + resources +
                '}';
    }
}
