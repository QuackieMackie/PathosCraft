package net.quackiemackie.pathoscraft.util.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.registers.PathosQuests;

import java.util.List;

/**
 * Represents a quest in the game.
 */
public record Quest(
        int id,
        String name,
        String description,
        String icon,
        int type,
        int preceding,
        int slot,
        int activeSlot,
        List<QuestObjective> objectives,
        List<QuestReward> rewards
) {
    public static final int MAX_ACTIVE_QUESTS = 45;

    /**
     * Codec for serializing and deserializing the Quest.
     */
    public static final Codec<Quest> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("quest_id").forGetter(Quest::id),
            Codec.STRING.fieldOf("quest_name").forGetter(Quest::name),
            Codec.STRING.fieldOf("quest_description").forGetter(Quest::description),
            Codec.STRING.fieldOf("quest_icon").forGetter(Quest::icon),
            Codec.INT.fieldOf("quest_type").forGetter(Quest::type),
            Codec.INT.optionalFieldOf("quest_preceding", 0).forGetter(Quest::preceding),
            Codec.INT.fieldOf("quest_slot").forGetter(Quest::slot),
            Codec.INT.fieldOf("quest_active_slot").orElse(-1).forGetter(Quest::activeSlot),
            QuestObjective.CODEC.listOf().fieldOf("quest_objectives").forGetter(Quest::objectives),
            QuestReward.CODEC.listOf().fieldOf("quest_rewards").forGetter(Quest::rewards)
    ).apply(instance, Quest::new));

    /**
     * Creates a new Quest with an updated active slot.
     *
     * @param newActiveSlot the new active slot value.
     */
    public void withActiveSlot(int newActiveSlot) {
        new Quest(
                this.id,
                this.name,
                this.description,
                this.icon,
                this.type,
                this.preceding,
                this.slot,
                newActiveSlot,
                this.objectives,
                this.rewards
        );
    }

    /**
     * Overrides the default toString implementation with a custom format.
     *
     * @return a string representation of the Quest.
     */
    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", activeSlot=" + activeSlot +
                (objectives.isEmpty() ? "" : ", objectives=" + objectives) +
                (rewards.isEmpty() ? "" : ", rewards=" + rewards) +
                '}';
    }

    /**
     * Registers the quest instance to the custom quest registry.
     *
     * @param registryAccess The registry access object used to obtain the custom quest registry.
     * @param registryName The name of the quest registry entry.
     */
    public void register(RegistryAccess registryAccess, String registryName) {
        Registry<Quest> questRegistry = registryAccess.registryOrThrow(PathosQuests.QUEST_REGISTRY_KEY);

        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, registryName);
        Registry.register(questRegistry, id, this);

        PathosCraft.LOGGER.info("Registered quest: {}", id);
    }
}