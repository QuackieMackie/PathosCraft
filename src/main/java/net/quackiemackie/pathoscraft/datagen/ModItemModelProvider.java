package net.quackiemackie.pathoscraft.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, PathosCraft.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.RAW_SADNESS.get());
        basicItem(ModItems.RAW_SUNNY.get());
        basicItem(ModItems.SADNESS_INGOT.get());
        basicItem(ModItems.SUNNY_INGOT.get());
        basicItem(ModItems.MANA_HERB.get());
        basicItem(ModItems.MANA_POTION.get());
        basicItem(ModItems.ARROW_TEST.get());

        handheldItem(ModItems.JUMP_WAND.get());
        handheldItem(ModItems.SADNESS_SWORD.get());
        handheldItem(ModItems.SADNESS_PICKAXE.get());
        handheldItem(ModItems.SADNESS_AXE.get());
        handheldItem(ModItems.SADNESS_SHOVEL.get());
        handheldItem(ModItems.SADNESS_HOE.get());

    }
}
