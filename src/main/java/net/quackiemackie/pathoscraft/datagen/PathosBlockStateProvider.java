package net.quackiemackie.pathoscraft.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.block.PathosBlocks;
import net.quackiemackie.pathoscraft.block.advanced.RepairBlock;

public class PathosBlockStateProvider extends BlockStateProvider {

    public PathosBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, PathosCraft.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(PathosBlocks.SADNESS_BLOCK);
        blockWithItem(PathosBlocks.DEEPSLATE_SADNESS_ORE);
        blockWithItem(PathosBlocks.SADNESS_ORE);
        blockWithItem(PathosBlocks.DEEPSLATE_SUNNY_ORE);
        blockWithItem(PathosBlocks.SUNNY_ORE);
        booleanBlockModelChange(PathosBlocks.REPAIR_BLOCK.get(), RepairBlock.CLICKED, "repair_block_active", "repair_block_inactive");
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }

    private void booleanBlockModelChange(Block block, BooleanProperty stateProperty, String activeTexture, String inactiveTexture) {
        getVariantBuilder(block).forAllStates(state -> {
            if (state.getValue(stateProperty)) { // true
                return new ConfiguredModel[]{
                        new ConfiguredModel(models().cubeAll(activeTexture, ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "block/" + activeTexture)))
                };
            } else { // false
                return new ConfiguredModel[]{
                        new ConfiguredModel(models().cubeAll(inactiveTexture, ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "block/" + inactiveTexture)))
                };
            }
        });
        simpleBlockItem(block, models().cubeAll(activeTexture, ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "block/" + activeTexture)));
    }
}
