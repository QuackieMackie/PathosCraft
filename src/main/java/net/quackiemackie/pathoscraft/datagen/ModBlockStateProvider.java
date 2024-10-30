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
import net.quackiemackie.pathoscraft.block.ModBlocks;
import net.quackiemackie.pathoscraft.block.advanced.RepairBlock;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, PathosCraft.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.SADNESS_BLOCK);
        blockWithItem(ModBlocks.DEEPSLATE_SADNESS_ORE);
        blockWithItem(ModBlocks.SADNESS_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_SUNNY_ORE);
        blockWithItem(ModBlocks.SUNNY_ORE);
        booleanBlockModelChange(ModBlocks.REPAIR_BLOCK.get(), RepairBlock.CLICKED, "repair_block_active", "repair_block_inactive");

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
