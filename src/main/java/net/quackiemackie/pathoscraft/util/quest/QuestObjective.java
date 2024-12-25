package net.quackiemackie.pathoscraft.util.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record QuestObjective(String action, String target, int quantity, int progress) {
    /**
     * Codec for serializing and deserializing the QuestObjective.
     */
    public static final Codec<QuestObjective> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("action").forGetter(QuestObjective::action),
            Codec.STRING.fieldOf("target").forGetter(QuestObjective::target),
            Codec.INT.fieldOf("quantity").forGetter(QuestObjective::quantity),
            Codec.INT.optionalFieldOf("progress", 0).forGetter(QuestObjective::progress)
    ).apply(instance, QuestObjective::new));

    /**
     * Creates a copy of this QuestObjective with updated progress.
     *
     * @param newProgress the new progress value.
     * @return a new QuestObjective with the updated progress.
     */
    public QuestObjective withProgress(int newProgress) {
        return new QuestObjective(this.action, this.target, this.quantity, newProgress);
    }

    /**
     * Overrides the default toString implementation to match the custom format.
     *
     * @return a string representation of the QuestObjective.
     */
    @Override
    public String toString() {
        return "Objective: {" +
                "action='" + action + '\'' +
                ", target='" + target + '\'' +
                ", quantity='" + quantity + '\'' +
                ", progress=" + progress +
                '}';
    }
}