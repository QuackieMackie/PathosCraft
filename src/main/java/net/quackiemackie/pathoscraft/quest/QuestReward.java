package net.quackiemackie.pathoscraft.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents a reward given upon completing a quest.
 */
public class QuestReward {

    public static final Codec<QuestReward> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("action").forGetter(QuestReward::getAction),
            Codec.STRING.fieldOf("item").forGetter(QuestReward::getItem),
            Codec.INT.fieldOf("quantity").forGetter(QuestReward::getQuantity)
    ).apply(instance, QuestReward::new));

    private final String action;
    private final String item;
    private final int quantity;

    /**
     * Creates a QuestReward.
     *
     * @param action the action associated with the reward.
     * @param item the item given as a reward.
     * @param quantity the quantity of the item given as a reward.
     */
    public QuestReward(String action, String item, int quantity) {
        this.action = action;
        this.item = item;
        this.quantity = quantity;
    }

    public String getAction() {
        return action;
    }

    public String getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Reward: {" +
                "action='" + action + '\'' +
                ", item='" + item + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}