package net.quackiemackie.pathoscraft.registers;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.util.quest.Quest;

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
    public static final ResourceKey<Registry<Quest>> QUEST_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "quest"));

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
