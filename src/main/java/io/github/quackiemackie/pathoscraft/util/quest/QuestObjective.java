package io.github.quackiemackie.pathoscraft.util.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Represents an objective required to complete a quest.
 */
public class QuestObjective {

    public static final Codec<QuestObjective> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("action").forGetter(QuestObjective::getAction),
            Codec.STRING.fieldOf("target").forGetter(QuestObjective::getTarget),
            Codec.INT.fieldOf("quantity").forGetter(QuestObjective::getQuantity),
            Codec.INT.optionalFieldOf("progress", 0).forGetter(QuestObjective::getProgress),
            Codec.BOOL.fieldOf("returnItems").forGetter(QuestObjective::getReturnItems)
    ).apply(instance, QuestObjective::new));

    private final String action;
    private final String target;
    private final int quantity;
    private int progress;
    private final boolean returnItems;

    /**
     * Creates a QuestObjective.
     *
     * @param action the action required to complete the objective.
     * @param target the target of the action.
     * @param quantity the quantity of the target required to complete the objective.
     * @param progress initial progress of the objective.
     * @param returnItems should items in this objective be returned after completing the quest?
     */
    public QuestObjective(String action, String target, int quantity, int progress, boolean returnItems) {
        this.action = action;
        this.target = target;
        this.quantity = quantity;
        this.progress = progress;
        this.returnItems = returnItems;
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

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean getReturnItems() {
        return returnItems;
    }

    @Override
    public String toString() {
        return "Objective: {" +
                "action='" + action + '\'' +
                ", target='" + target + '\'' +
                ", quantity=" + quantity +
                ", progress=" + progress +
                ", returnItems=" + returnItems +
                '}';
    }
}