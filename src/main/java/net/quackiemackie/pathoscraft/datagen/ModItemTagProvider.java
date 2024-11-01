package net.quackiemackie.pathoscraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.item.ModItems;
import net.quackiemackie.pathoscraft.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {

    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, PathosCraft.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Items.REPAIRABLE_BLOCK_ITEMS)
                .add(ModItems.JUMP_WAND.get())
                .add(ModItems.ASTRAL_LANTERN.get());

        tag(ItemTags.SWORDS)
                .add(ModItems.SADNESS_SWORD.get());
        tag(ItemTags.PICKAXES)
                .add(ModItems.SADNESS_PICKAXE.get());
        tag(ItemTags.AXES)
                .add(ModItems.SADNESS_AXE.get());
        tag(ItemTags.SHOVELS)
                .add(ModItems.SADNESS_SHOVEL.get());
        tag(ItemTags.HOES)
                .add(ModItems.SADNESS_HOE.get());
    }
}
