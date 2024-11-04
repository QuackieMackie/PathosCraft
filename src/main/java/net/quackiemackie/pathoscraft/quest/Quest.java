package net.quackiemackie.pathoscraft.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public class Quest {

    public static final Codec<Quest> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("id").forGetter(Quest::getId),
            Codec.STRING.fieldOf("questName").forGetter(Quest::getQuestName),
            Codec.STRING.fieldOf("questDescription").forGetter(Quest::getQuestDescription),
            Codec.INT.fieldOf("type").forGetter(Quest::getQuestType),
            QuestObjective.CODEC.listOf().fieldOf("questObjectives").forGetter(Quest::getQuestObjectives),
            QuestReward.CODEC.listOf().fieldOf("questRewards").forGetter(Quest::getQuestRewards)
    ).apply(instance, Quest::new));

    private final int id;
    private final String questName;
    private final String questDescription;
    private final int questType;
    private final List<QuestObjective> questObjectives;
    private final List<QuestReward> questRewards;

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