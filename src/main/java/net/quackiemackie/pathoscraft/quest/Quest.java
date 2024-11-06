package net.quackiemackie.pathoscraft.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

/**
 * Represents a quest in the game.
 */
public class Quest {

    public static final Codec<Quest> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("id").forGetter(Quest::getId),
            Codec.STRING.fieldOf("quest_name").forGetter(Quest::getQuestName),
            Codec.STRING.fieldOf("quest_description").forGetter(Quest::getQuestDescription),
            Codec.INT.fieldOf("quest_type").forGetter(Quest::getQuestType),
            QuestObjective.CODEC.listOf().fieldOf("quest_objectives").forGetter(Quest::getQuestObjectives),
            QuestReward.CODEC.listOf().fieldOf("quest_rewards").forGetter(Quest::getQuestRewards)
    ).apply(instance, Quest::new));

    private final int id;
    private final String questName;
    private final String questDescription;
    private final int questType;
    private final List<QuestObjective> questObjectives;
    private final List<QuestReward> questRewards;

    /**
     * Creates a Quest.
     *
     * @param id the unique identifier of the quest.
     * @param questName the name of the quest.
     * @param questDescription a description of the quest.
     * @param questType the type of the quest.
     * @param questObjectives a list of objectives required to complete the quest.
     * @param questRewards a list of rewards given upon completing the quest.
     */
    public Quest(int id, String questName, String questDescription, int questType, List<QuestObjective> questObjectives, List<QuestReward> questRewards){
        this.id = id;
        this.questName = questName;
        this.questDescription = questDescription;
        this.questType = questType;
        this.questObjectives = questObjectives;
        this.questRewards = questRewards;
    }

    public int getId(){
        return this.id;
    }

    public String getQuestName(){
        return this.questName;
    }

    public String getQuestDescription(){
        return this.questDescription;
    }

    public int getQuestType(){
        return this.questType;
    }

    public List<QuestObjective> getQuestObjectives(){
        return this.questObjectives;
    }

    public List<QuestReward> getQuestRewards(){
        return this.questRewards;
    }
}