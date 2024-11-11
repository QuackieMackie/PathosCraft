package net.quackiemackie.pathoscraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.item.PathosItems;
import net.quackiemackie.pathoscraft.registers.PathosTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class PathosItemTagProvider extends ItemTagsProvider {

    public PathosItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, PathosCraft.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(PathosTags.Items.REPAIRABLE_BLOCK_ITEMS)
                .add(PathosItems.JUMP_WAND.get())
                .add(PathosItems.ASTRAL_LANTERN.get());

        tag(ItemTags.SWORDS)
                .add(PathosItems.SADNESS_SWORD.get());
        tag(ItemTags.PICKAXES)
                .add(PathosItems.SADNESS_PICKAXE.get());
        tag(ItemTags.AXES)
                .add(PathosItems.SADNESS_AXE.get());
        tag(ItemTags.SHOVELS)
                .add(PathosItems.SADNESS_SHOVEL.get());
        tag(ItemTags.HOES)
                .add(PathosItems.SADNESS_HOE.get());

        tag(PathosTags.EntityFood.SPIDER_MOUNT_FOOD)
                .add(Items.CHICKEN, Items.PORKCHOP, Items.BEEF, Items.MUTTON, Items.RABBIT)
                .add(Items.COOKED_CHICKEN, Items.COOKED_PORKCHOP, Items.COOKED_BEEF, Items.COOKED_MUTTON, Items.COOKED_RABBIT);
    }
}
