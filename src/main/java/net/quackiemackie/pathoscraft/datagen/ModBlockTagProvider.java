package net.quackiemackie.pathoscraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.block.ModBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {

    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, PathosCraft.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.REPAIR_BLOCK.get())
                .add(ModBlocks.SADNESS_BLOCK.get())
                .add(ModBlocks.SADNESS_ORE.get())
                .add(ModBlocks.SUNNY_ORE.get())
                .add(ModBlocks.DEEPSLATE_SADNESS_ORE.get())
                .add(ModBlocks.DEEPSLATE_SUNNY_ORE.get());

        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.REPAIR_BLOCK.get())
                .add(ModBlocks.SUNNY_ORE.get())
                .add(ModBlocks.DEEPSLATE_SUNNY_ORE.get())
                .add(ModBlocks.SADNESS_ORE.get())
                .add(ModBlocks.DEEPSLATE_SADNESS_ORE.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.SADNESS_BLOCK.get());
    }
}