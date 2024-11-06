package net.quackiemackie.pathoscraft.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.quackiemackie.pathoscraft.block.ModBlocks;
import net.quackiemackie.pathoscraft.item.ModItems;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {

    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    // To not drop an item need to add `.noLootTable()` to the block.

    @Override
    protected void generate() {
        dropSelf(ModBlocks.REPAIR_BLOCK.get());
        dropSelf(ModBlocks.SADNESS_BLOCK.get());
        add(ModBlocks.DEEPSLATE_SADNESS_ORE.get(), block -> createOreDrop(ModBlocks.DEEPSLATE_SADNESS_ORE.get(), ModItems.RAW_SADNESS.get()));
        add(ModBlocks.SADNESS_ORE.get(), block -> createOreDrop(ModBlocks.SADNESS_ORE.get(), ModItems.RAW_SADNESS.get()));
        add(ModBlocks.DEEPSLATE_SUNNY_ORE.get(), block -> createOreDrop(ModBlocks.DEEPSLATE_SUNNY_ORE.get(), ModItems.RAW_SUNNY.get()));
        add(ModBlocks.SUNNY_ORE.get(), block -> createOreDrop(ModBlocks.SUNNY_ORE.get(), ModItems.RAW_SUNNY.get()));

    }

    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
