package io.github.quackiemackie.pathoscraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.enchantment.PathosEnchantment;
import io.github.quackiemackie.pathoscraft.registers.PathosQuests;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class PathosDataPackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.ENCHANTMENT, PathosEnchantment::bootstrap)
            .add(PathosQuests.QUEST_REGISTRY_KEY, PathosQuests::bootstrap);

    public PathosDataPackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(PathosCraft.MOD_ID));
    }
}
