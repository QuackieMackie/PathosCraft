package net.quackiemackie.pathoscraft.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents an objective required to complete a quest.
 */
public class QuestObjective {

    public static final Codec<QuestObjective> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("action").forGetter(QuestObjective::getAction),
            Codec.STRING.fieldOf("target").forGetter(QuestObjective::getTarget),
            Codec.INT.fieldOf("quantity").forGetter(QuestObjective::getQuantity)
    ).apply(instance, QuestObjective::new));

    private final String action;
    private final String target;
    private final int quantity;

    /**
     * Creates a QuestObjective.
     *
     * @param action the action required to complete the objective.
     * @param target the target of the action.
     * @param quantity the quantity of the target required to complete the objective.
     */
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

    @Override
    public String toString() {
        return "Objective: {" +
                "action='" + action + '\'' +
                ", target='" + target + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}