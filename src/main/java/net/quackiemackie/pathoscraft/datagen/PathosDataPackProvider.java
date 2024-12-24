package net.quackiemackie.pathoscraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.enchantment.PathosEnchantment;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class PathosDataPackProvider extends DatapackBuiltinEntriesProvider implements DataProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.ENCHANTMENT, PathosEnchantment::bootstrap);
            //.add(PathosQuests.QUEST_REGISTRY_KEY, PathosQuests::bootstrap);

    //TODO: Look into if I can create an .add() for the quests in the data pack. Because currently they ugly clash.

    private final PackOutput packOutput;

    public PathosDataPackProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registryProvider) {
        super(packOutput, registryProvider, BUILDER, Set.of(PathosCraft.MOD_ID));
        this.packOutput = packOutput;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        CompletableFuture<?> superRun = super.run(cache);

        // Add the PathosQuestProvider here
        PathosQuestProvider pathosQuestProvider = new PathosQuestProvider(this.packOutput);
        CompletableFuture<?> questProviderRun = pathosQuestProvider.run(cache);

        // Combine all tasks
        return CompletableFuture.allOf(superRun, questProviderRun);
    }

    @Override
    public String getName() {
        return "PathosCraft Data Pack Providers";
    }
}
