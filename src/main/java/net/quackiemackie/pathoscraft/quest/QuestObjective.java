package net.quackiemackie.pathoscraft.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class QuestObjective {

    public static final Codec<QuestObjective> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("action").forGetter(QuestObjective::getAction),
            Codec.STRING.fieldOf("target").forGetter(QuestObjective::getTarget),
            Codec.INT.fieldOf("quantity").forGetter(QuestObjective::getQuantity)
    ).apply(instance, QuestObjective::new));

    private final String action;
    private final String target;
    private final int quantity;

    public QuestObjective(String action, String target, int quantity) {
        this.action = action;
        this.target = target;
        this.quantity = quantity;
    }

    public String getAction() {
        return action;
    }

    public String getTarget() {
        return target;
    }

    public int getQuantity() {
        return quantity;
    }
}