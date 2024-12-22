package net.quackiemackie.pathoscraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.block.PathosBlocks;
import net.quackiemackie.pathoscraft.registers.PathosTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class PathosBlockTagProvider extends BlockTagsProvider {

    public PathosBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, PathosCraft.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(PathosBlocks.REPAIR_BLOCK.get())
                .add(PathosBlocks.SADNESS_BLOCK.get())
                .add(PathosBlocks.SADNESS_ORE.get())
                .add(PathosBlocks.SUNNY_ORE.get())
                .add(PathosBlocks.DEEPSLATE_SADNESS_ORE.get())
                .add(PathosBlocks.DEEPSLATE_SUNNY_ORE.get());

        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(PathosBlocks.REPAIR_BLOCK.get())
                .add(PathosBlocks.SUNNY_ORE.get())
                .add(PathosBlocks.DEEPSLATE_SUNNY_ORE.get())
                .add(PathosBlocks.SADNESS_ORE.get())
                .add(PathosBlocks.DEEPSLATE_SADNESS_ORE.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(PathosBlocks.SADNESS_BLOCK.get());

        tag(PathosTags.Blocks.NEEDS_SADNESS_TOOL)
                .addTag(BlockTags.NEEDS_IRON_TOOL);

        tag(PathosTags.Blocks.INCORRECT_FOR_SADNESS_TOOL)
                .addTag(BlockTags.INCORRECT_FOR_IRON_TOOL)
                .remove(PathosTags.Blocks.NEEDS_SADNESS_TOOL);

        tag(PathosTags.Blocks.SADNESS_ORE)
                .add(PathosBlocks.SADNESS_ORE.get())
                .add(PathosBlocks.DEEPSLATE_SADNESS_ORE.get());
        tag(PathosTags.Blocks.SUNNY_ORE)
                .add(PathosBlocks.SUNNY_ORE.get())
                .add(PathosBlocks.DEEPSLATE_SUNNY_ORE.get());

        tag(PathosTags.Blocks.EXCAVATION_MINIGAME_ORES)
                .addTag(Tags.Blocks.ORES)
                .addTag(PathosTags.Blocks.SADNESS_ORE)
                .addTag(PathosTags.Blocks.SUNNY_ORE);
    }
}