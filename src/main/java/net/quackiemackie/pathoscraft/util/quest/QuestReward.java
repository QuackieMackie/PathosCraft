package net.quackiemackie.pathoscraft.util.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents a reward given upon completing a quest.
 */
public record QuestReward(String action, String item, int quantity) {
    /**
     * Codec for serializing and deserializing the QuestReward.
     */
    public static final Codec<QuestReward> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("action").forGetter(QuestReward::action),
            Codec.STRING.fieldOf("item").forGetter(QuestReward::item),
            Codec.INT.fieldOf("quantity").forGetter(QuestReward::quantity)
    ).apply(instance, QuestReward::new));

    /**
     * Overrides the default toString implementation to match the custom format.
     *
     * @return a string representation of the QuestReward.
     */
    @Override
    public String toString() {
        return "Reward: {" +
                "action='" + action + '\'' +
                ", item='" + item + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}