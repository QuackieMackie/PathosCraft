package net.quackiemackie.pathoscraft.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class QuestReward {

    public static final Codec<QuestReward> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("action").forGetter(QuestReward::getAction),
            Codec.STRING.fieldOf("item").forGetter(QuestReward::getItem),
            Codec.INT.fieldOf("quantity").forGetter(QuestReward::getQuantity)
    ).apply(instance, QuestReward::new));

    private final String action;
    private final String item;
    private final int quantity;

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
}