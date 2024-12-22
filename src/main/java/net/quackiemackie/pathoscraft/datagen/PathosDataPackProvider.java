package net.quackiemackie.pathoscraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.enchantment.PathosEnchantment;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class PathosDataPackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.ENCHANTMENT, PathosEnchantment::bootstrap);

    public PathosDataPackProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registryProvider) {
        super(packOutput, registryProvider, BUILDER, Set.of(PathosCraft.MOD_ID));
    }
}
