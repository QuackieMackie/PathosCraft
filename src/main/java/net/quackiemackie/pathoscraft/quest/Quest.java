package net.quackiemackie.pathoscraft.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

/**
 * Represents a quest in the game.
 */
public class Quest {

    /**
     * Notes for the quest system.
     * - = + = -
     * quest type will have 2 types for now, 0 and 1
     * 0 would equal a main quest, these quests would be saved to the player data,
     * so the system can cross-reference what has been completed by that player in that world to make these quests not repeatable this means they can only be completed 1 time by that account on that world without modifying anything.
     * - = + = -
     * 1 is a quest that can be done multiple times or repeated this would let
     * the system create methods of using these quests to be triggered and played through multiple times.
     * - = + = -
     * Quests would need to be activated by the player in order for progression in that quest to be made,
     * this would be a limited list for the game to check the progress of periodically.
     * As an example, player x accepts a kill quest for zombies with a secondary objective to collect zombie flesh.
     * This would mean the events have to be checked dynamically by the game.
     * If player x has y, z quests active, it needs to know the objectives in order to know what events to be checked.
     * - = + = -
     * Quest rewards would be directly placed in the quest inventory, this inventory would be a forever expanding box
     * that can store items and give items but not accept items.
     * It would act as a one-way storage for the quest items.
     */

    public static final Codec<Quest> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("id").forGetter(Quest::getId),
            Codec.STRING.fieldOf("quest_name").forGetter(Quest::getQuestName),
            Codec.STRING.fieldOf("quest_description").forGetter(Quest::getQuestDescription),
            Codec.INT.fieldOf("quest_type").forGetter(Quest::getQuestType),
            Codec.INT.optionalFieldOf("quest_preceding", 0).forGetter(Quest::getPrecedingQuest),
            QuestObjective.CODEC.listOf().fieldOf("quest_objectives").forGetter(Quest::getQuestObjectives),
            QuestReward.CODEC.listOf().fieldOf("quest_rewards").forGetter(Quest::getQuestRewards)
    ).apply(instance, Quest::new));

    private final int id;
    private final String questName;
    private final String questDescription;
    private final int questType;
    private final int questPreceding;
    private final List<QuestObjective> questObjectives;
    private final List<QuestReward> questRewards;

    /**
     * Creates a Quest.
     *
     * @param id the unique identifier of the quest.
     * @param questName the name of the quest.
     * @param questDescription a description of the quest.
     * @param questType the type of the quest.
     * @param questPreceding this is the id of the quest that is this quest in the chain if it's the first quest return `0`
     * @param questObjectives a list of objectives required to complete the quest.
     * @param questRewards a list of rewards given upon completing the quest.
     */
    public Quest(int id, String questName, String questDescription, int questType, int questPreceding, List<QuestObjective> questObjectives, List<QuestReward> questRewards){
        this.id = id;
        this.questName = questName;
        this.questDescription = questDescription;
        this.questType = questType;
        this.questPreceding = questPreceding;
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

    public int getPrecedingQuest(){
        return this.questPreceding;
    }

    public List<QuestObjective> getQuestObjectives(){
        return this.questObjectives;
    }

    public List<QuestReward> getQuestRewards(){
        return this.questRewards;
    }
}