package net.quackiemackie.pathoscraft.registers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.quackiemackie.pathoscraft.PathosCraft;

/**
 * Handles the registration and management of mod tags for the PathosCraft mod.
 */
public class PathosTags {
    public static class Blocks {
        public static final TagKey<Block> EXCAVATION_MINIGAME_ORES = createTag("excavation_minigame_ores");
        public static final TagKey<Block> LUMBERING_MINIGAME_BLOCKS = createTag("lumbering_minigame_blocks");

        public static final TagKey<Block> SADNESS_ORE = createTag("sadness_ore");
        public static final TagKey<Block> SUNNY_ORE = createTag("sunny_ore");

        public static final TagKey<Block> NEEDS_SADNESS_TOOL = createTag("needs_sadness_tool");
        public static final TagKey<Block> INCORRECT_FOR_SADNESS_TOOL = createTag("incorrect_for_sadness_tool");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> REPAIRABLE_BLOCK_ITEMS = createTag("repairable_block_items");
        public static final TagKey<Item> MINIGAME_ENCHANT_ITEMS = createTag("minigame_enchant_items");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, name));
        }
    }

    public static class EntityFood {
        public static final TagKey<Item> SPIDER_MOUNT_FOOD = createTag("spider_mount_food");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, name));
        }
    }
}
