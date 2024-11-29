package net.quackiemackie.pathoscraft.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

/**
 * Represents a quest in the game.
 */
public class Quest {

    public static final Codec<Quest> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("quest_id").forGetter(Quest::getQuestId),
            Codec.STRING.fieldOf("quest_name").forGetter(Quest::getQuestName),
            Codec.STRING.fieldOf("quest_description").forGetter(Quest::getQuestDescription),
            Codec.STRING.fieldOf("quest_icon").forGetter(Quest::getQuestIcon),
            Codec.INT.fieldOf("quest_type").forGetter(Quest::getQuestType),
            Codec.INT.optionalFieldOf("quest_preceding", 0).forGetter(Quest::getPrecedingQuest),
            Codec.INT.fieldOf("quest_slot").forGetter(Quest::getQuestSlot),
            QuestObjective.CODEC.listOf().fieldOf("quest_objectives").forGetter(Quest::getQuestObjectives),
            QuestReward.CODEC.listOf().fieldOf("quest_rewards").forGetter(Quest::getQuestRewards)
    ).apply(instance, Quest::new));

    private final int questId;
    private final String questName;
    private final String questDescription;
    private final String questIcon;
    private final int questType;
    private final int questPreceding;
    private final int questSlot;
    private final List<QuestObjective> questObjectives;
    private final List<QuestReward> questRewards;

    /**
     * Creates a Quest.
     *
     * @param questId          the unique identifier of the quest.
     * @param questName        the name of the quest.
     * @param questDescription a description of the quest.
     * @param questIcon        the icon representing the quest.
     * @param questType        the type of the quest.
     * @param questPreceding   this is the id of the preceding quest in the chain if it's the first quest return `0`.
     * @param questSlot        The quest menu slot.
     * @param questObjectives  a list of objectives required to complete the quest.
     * @param questRewards     a list of rewards given upon completing the quest.
     */
    public Quest(int questId, String questName, String questDescription, String questIcon, int questType, int questPreceding, int questSlot, List<QuestObjective> questObjectives, List<QuestReward> questRewards) {
        this.questId = questId;
        this.questName = questName;
        this.questDescription = questDescription;
        this.questIcon = questIcon;
        this.questType = questType;
        this.questPreceding = questPreceding;
        this.questSlot = questSlot;
        this.questObjectives = questObjectives;
        this.questRewards = questRewards;
    }

    public int getQuestId() {
        return this.questId;
    }

    public String getQuestName() {
        return this.questName;
    }

    public String getQuestDescription() {
        return this.questDescription;
    }

    public String getQuestIcon() {
        return this.questIcon;
    }

    public int getQuestType() {
        return this.questType;
    }

    public int getPrecedingQuest() {
        return this.questPreceding;
    }

    public int getQuestSlot() {
        return this.questSlot;
    }

    public List<QuestObjective> getQuestObjectives() {
        return this.questObjectives;
    }

    public List<QuestReward> getQuestRewards() {
        return this.questRewards;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + questId +
                ", name='" + questName + '\'' +
                ", description='" + questDescription + '\'' +
                ", type=" + questType +
                (questObjectives.isEmpty() ? "" : ", objectives=" + questObjectives) +
                (questRewards.isEmpty() ? "" : ", rewards=" + questRewards) +
                '}';
    }
}