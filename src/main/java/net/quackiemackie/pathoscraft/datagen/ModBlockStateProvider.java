package net.quackiemackie.pathoscraft.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.block.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, PathosCraft.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.REPAIR_BLOCK);
        blockWithItem(ModBlocks.SADNESS_BLOCK);
        blockWithItem(ModBlocks.DEEPSLATE_SADNESS_ORE);
        blockWithItem(ModBlocks.SADNESS_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_SUNNY_ORE);
        blockWithItem(ModBlocks.SUNNY_ORE);
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }
}
