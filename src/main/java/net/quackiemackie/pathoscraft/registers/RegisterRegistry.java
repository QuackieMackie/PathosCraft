package net.quackiemackie.pathoscraft.registers;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.quest.Quest;

public class RegisterRegistry {
    public static final ResourceKey<Registry<Quest>> QUEST_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "quest"));

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
                QUEST_REGISTRY_KEY,
                Quest.CODEC,
                Quest.CODEC
        );
    }
}
