package net.quackiemackie.pathoscraft.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.quackiemackie.pathoscraft.PathosCraft;

public class ModTags {

    public static class Blocks {
        public static final TagKey<Block> NEEDS_SADNESS_TOOL = createTag("needs_sadness_tool");
        public static final TagKey<Block> INCORRECT_FOR_SADNESS_TOOL = createTag("incorrect_for_sadness_tool");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> REPAIRABLE_BLOCK_ITEMS = createTag("repairable_block_items");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, name));
        }
    }
}
