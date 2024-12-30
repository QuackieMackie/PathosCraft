package net.quackiemackie.pathoscraft.registers;

import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.util.quest.Quest;
import net.quackiemackie.pathoscraft.util.quest.QuestObjective;
import net.quackiemackie.pathoscraft.util.quest.QuestReward;

import java.util.List;

/**
 * ModQuests class handles the registration of custom quests within the PathosCraft mod.
 *
 * <p>This class is responsible for setting up a custom registry for quests defined as JSON files
 * in the data pack directory. It subscribes to the <code>DataPackRegistryEvent.NewRegistry</code>
 * event to register the custom quest registry, allowing the game to load quest data from the specified path.</p>
 *
 * <p>The quests data is expected to be stored in <code>data/mod_id/quest/</code></p>
 *
 * <p>This class is annotated with <code>@EventBusSubscriber</code> to automatically register event handlers
 * on the mod-specific event bus.</p>
 */
@EventBusSubscriber(modid = PathosCraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class PathosQuests {
    public static final ResourceKey<Registry<Quest>> QUEST_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.parse("quest"));

    private static final int DEFAULT_PROGRESS = 0;
    private static final int ACTIVE_QUEST_SLOT = 0;

    /// A static and immutable list of predefined `Quest` objects representing various quests in the game.
    /// The `QUESTS` variable contains a collection of quests, both main and optional, detailing their objectives,
    /// rewards, icons, and associated metadata. These quests are typically used for gameplay progression and quest tracking.
    /// Key details stored for each quest:
    /// - A unique quest ID to identify it.
    /// - Name, description, and icon for display purposes.
    /// - Type and slot identifiers for categorization.
    /// - Preceding quest information to determine prerequisites.
    /// - Objectives specifying the tasks to complete.
    /// - Rewards granted upon quest completion.
    /// `QUESTS` eliminates the need for dynamic quest generation by providing a predefined set of quests during initialization.
    /// The quests are defined using the [Quest], [QuestObjective], and [QuestReward] records.
    /// This field is immutable and declared as `private static final` to ensure thread safety and prevent external modification.
    private static final List<Quest> QUESTS = List.of(
            new Quest(1, "Main quest 1", "Description for test quest.", "minecraft:dirt", 0, 0, 0, ACTIVE_QUEST_SLOT,
                    List.of(new QuestObjective("collect", "minecraft:dirt", 10, DEFAULT_PROGRESS, true)),
                    List.of(new QuestReward("give", "minecraft:diamond", 1))),

            new Quest(2, "Main quest 2", "Description for test quest 2.", "minecraft:stone", 0, 1, 1, ACTIVE_QUEST_SLOT,
                    List.of(new QuestObjective("kill", "minecraft:zombie", 10, DEFAULT_PROGRESS, false),
                            new QuestObjective("collect", "pathoscraft:raw_sadness", 5, DEFAULT_PROGRESS, true)),
                    List.of(new QuestReward("give", "minecraft:gold_ingot", 1))),

            new Quest(3, "Main quest 3", "Description for test quest 3.", "minecraft:diamond_block", 0, 2, 2, ACTIVE_QUEST_SLOT,
                    List.of(new QuestObjective("kill", "minecraft:skeleton", 10, DEFAULT_PROGRESS, false),
                            new QuestObjective("collect", "pathoscraft:raw_sadness", 5, DEFAULT_PROGRESS, true)),
                    List.of(new QuestReward("give", "minecraft:gold_ingot", 1))),

            new Quest(4, "Main quest 4", "Description for test quest 4.", "minecraft:apple", 0, 3, 3, ACTIVE_QUEST_SLOT,
                    List.of(new QuestObjective("kill", "minecraft:skeleton", 10, DEFAULT_PROGRESS, false),
                            new QuestObjective("collect", "minecraft:apple", 5, DEFAULT_PROGRESS, true)),
                    List.of(new QuestReward("give", "minecraft:gold_ingot", 1))),

            new Quest(5, "Side Quest 1", "Description for test quest 5.", "minecraft:gold_block", 1, 0, 0, ACTIVE_QUEST_SLOT,
                    List.of(new QuestObjective("collect", "minecraft:dirt", 5, DEFAULT_PROGRESS, false)),
                    List.of(new QuestReward("give", "minecraft:diamond", 1))),

            new Quest(6, "Optional Quest 1", "Description for test quest 6.", "pathoscraft:raw_sadness", 2, 0, 0, ACTIVE_QUEST_SLOT,
                    List.of(new QuestObjective("kill", "minecraft:chicken", 10, DEFAULT_PROGRESS, false),
                            new QuestObjective("kill", "minecraft:pig", 5, DEFAULT_PROGRESS, false)),
                    List.of(new QuestReward("give", "minecraft:diamond", 1))),

            new Quest(7, "Optional Quest 2", "Description for test quest 7.", "minecraft:gold_ingot", 2, 0, 1, ACTIVE_QUEST_SLOT,
                    List.of(new QuestObjective("collect", "minecraft:dirt", 5, DEFAULT_PROGRESS, true)),
                    List.of(new QuestReward("give", "minecraft:diamond", 1))),

            new Quest(8, "Main Quest 5", "Test Quest 8", "pathoscraft:animated_item", 0, 4, 4, ACTIVE_QUEST_SLOT,
                    List.of(new QuestObjective("collect", "minecraft:diamond", 5, DEFAULT_PROGRESS, true),
                            new QuestObjective("collect", "minecraft:iron_ingot", 10, DEFAULT_PROGRESS, true),
                            new QuestObjective("collect", "minecraft:gold_ingot", 10, DEFAULT_PROGRESS, false)),
                    List.of(new QuestReward("give", "minecraft:gold_ingot", 1)))
    );

    public static List<Quest> getQuests() {
        return QUESTS;
    }

    /**
     * Bootstrap method dynamically generates quests and uses dynamically generated ResourceKeys.
     *
     * @param context the registry context provided by the bootstrap process.
     */
    public static void bootstrap(BootstrapContext<Quest> context) {
        for (Quest quest : QUESTS) {
            ResourceKey<Quest> questKey = ResourceKey.create(QUEST_REGISTRY_KEY, ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "quest_" + quest.getQuestId()));

            context.register(questKey, quest);

            PathosCraft.LOGGER.info("Registered Quest: ID={} Name={} Key={}", quest.getQuestId(), quest.getQuestName(), questKey.location());
        }
    }

    /**
     * Event handler for the DataPackRegistryEvent.NewRegistry event.
     *
     * <p>This method registers the quest registry and its codecs. It ensures that quest data will be loaded
     * from the specified JSON files within the mod's data pack directory.</p>
     *
     * @param event the event instance representing the new data pack registry.
     */
    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        try {
            event.dataPackRegistry(QUEST_REGISTRY_KEY, Quest.CODEC, Quest.CODEC);
            PathosCraft.LOGGER.info("Quest registry key & data pack registered.");
        } catch (Exception e) {
            PathosCraft.LOGGER.info("Failed to register quest registry key & data pack: {}", e.getMessage());
        }
    }
}
