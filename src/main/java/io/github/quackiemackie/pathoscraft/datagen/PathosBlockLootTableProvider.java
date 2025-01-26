package io.github.quackiemackie.pathoscraft.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import io.github.quackiemackie.pathoscraft.block.PathosBlocks;
import io.github.quackiemackie.pathoscraft.item.PathosItems;

import java.util.Set;

public class PathosBlockLootTableProvider extends BlockLootSubProvider {

    protected PathosBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    // To not drop an item need to add `.noLootTable()` to the block.

    @Override
    protected void generate() {
        dropSelf(PathosBlocks.REPAIR_BLOCK.get());
        dropSelf(PathosBlocks.SADNESS_BLOCK.get());
        add(PathosBlocks.DEEPSLATE_SADNESS_ORE.get(), block -> createOreDrop(PathosBlocks.DEEPSLATE_SADNESS_ORE.get(), PathosItems.RAW_SADNESS.get()));
        add(PathosBlocks.SADNESS_ORE.get(), block -> createOreDrop(PathosBlocks.SADNESS_ORE.get(), PathosItems.RAW_SADNESS.get()));
        add(PathosBlocks.DEEPSLATE_SUNNY_ORE.get(), block -> createOreDrop(PathosBlocks.DEEPSLATE_SUNNY_ORE.get(), PathosItems.RAW_SUNNY.get()));
        add(PathosBlocks.SUNNY_ORE.get(), block -> createOreDrop(PathosBlocks.SUNNY_ORE.get(), PathosItems.RAW_SUNNY.get()));
        dropSelf(PathosBlocks.WORKER_STATION_BLOCK.get());
    }

    protected Iterable<Block> getKnownBlocks() {
        return PathosBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
