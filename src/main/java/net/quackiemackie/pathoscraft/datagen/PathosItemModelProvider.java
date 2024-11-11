package net.quackiemackie.pathoscraft.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.item.PathosItems;

public class PathosItemModelProvider extends ItemModelProvider {

    public PathosItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, PathosCraft.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(PathosItems.RAW_SADNESS.get());
        basicItem(PathosItems.RAW_SUNNY.get());
        basicItem(PathosItems.SADNESS_INGOT.get());
        basicItem(PathosItems.SUNNY_INGOT.get());
        basicItem(PathosItems.MANA_HERB.get());
        basicItem(PathosItems.MANA_POTION.get());
        basicItem(PathosItems.ARROW_TEST.get());

        handheldItem(PathosItems.JUMP_WAND.get());
        handheldItem(PathosItems.SADNESS_SWORD.get());
        handheldItem(PathosItems.SADNESS_PICKAXE.get());
        handheldItem(PathosItems.SADNESS_AXE.get());
        handheldItem(PathosItems.SADNESS_SHOVEL.get());
        handheldItem(PathosItems.SADNESS_HOE.get());

        handheldItem(PathosItems.QUEST_BOOK.get());
    }
}
