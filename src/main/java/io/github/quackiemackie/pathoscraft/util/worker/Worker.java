package io.github.quackiemackie.pathoscraft.util.worker;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

//TODO: Effecency would return the % of a reward.
// Speed would determine how much of a speed boost per node it gathers
// Luck would give a chance to add a 25% bonus to rewards. For example
// ---
// During the day the time reduced would be 25% faster
// Night would have a 25% reduction

public record Worker(int id, int type, int efficiency, int speed, int luck, List<WorkerResources> resources) {
    public static final Codec<Worker> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("id").forGetter(Worker::id),
            Codec.INT.fieldOf("type").forGetter(Worker::type), // 0 = Naive, 1 = Normal, 2 = Skilled, 3 = Professional, 4 = Artisan
            Codec.INT.fieldOf("efficiency").forGetter(Worker::efficiency),
            Codec.INT.fieldOf("speed").forGetter(Worker::speed),
            Codec.INT.fieldOf("luck").forGetter(Worker::luck),
            WorkerResources.CODEC.listOf().fieldOf("resources").forGetter(Worker::resources)
    ).apply(instance, Worker::new));

    public static Worker naiveWorker(int id, List<WorkerResources> resources) {
        return new Worker(id, 0,
                randomBetween(100, 250),   // Efficiency
                randomBetween(100, 250),   // Speed
                randomBetween(0, 50),      // Luck
                resources);
    }

    public static Worker normalWorker(int id, List<WorkerResources> resources) {
        return new Worker(id, 1,
                randomBetween(250, 500),   // Efficiency
                randomBetween(250, 500),   // Speed
                randomBetween(25, 100),    // Luck
                resources);
    }

    public static Worker skilledWorker(int id, List<WorkerResources> resources) {
        return new Worker(id, 2,
                randomBetween(500, 800),   // Efficiency
                randomBetween(500, 800),   // Speed
                randomBetween(75, 125),    // Luck
                resources);
    }

    public static Worker professionalWorker(int id, List<WorkerResources> resources) {
        return new Worker(id, 3,
                randomBetween(800, 1000),  // Efficiency
                randomBetween(800, 1000),  // Speed
                randomBetween(100, 150),   // Luck
                resources);
    }

    public static Worker artisanWorker(int id, List<WorkerResources> resources) {
        return new Worker(id, 4,
                randomBetween(1000, 1200), // Efficiency
                randomBetween(1000, 1200), // Speed
                randomBetween(150, 250),   // Luck
                resources);
    }

    private static int randomBetween(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }


    @Override
    public String toString() {
        return "Worker: {" +
                "id=" + id +
                ", type=" + type +
                ", efficiency=" + efficiency +
                ", speed=" + speed +
                ", luck=" + luck +
                ", resources=" + resources +
                '}';
    }
}
