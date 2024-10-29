package net.quackiemackie.pathoscraft.block;

import net.minecraft.network.chat.Component;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.block.advanced.RepairBlock;
import net.quackiemackie.pathoscraft.item.ModItems;

import java.util.List;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(PathosCraft.MOD_ID);

    public static final DeferredBlock<Block> SADNESS_BLOCK = registerBlock("sadness_block", () ->
            new Block(BlockBehaviour.Properties.of()
                    .strength(1f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)) {
                @Override
                public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("block.pathoscraft.sadness_block.tooltip"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });

    public static final DeferredBlock<Block> SADNESS_ORE = registerBlock("sadness_ore", () ->
            new DropExperienceBlock(UniformInt.of(2, 4), BlockBehaviour.Properties.of()
                    .strength(1f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)) {
                @Override
                public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("block.pathoscraft.sadness_ore.tooltip"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });

    public static final DeferredBlock<Block> DEEPSLATE_SADNESS_ORE = registerBlock("deepslate_sadness_ore", () ->
            new DropExperienceBlock(UniformInt.of(3, 4), BlockBehaviour.Properties.of()
                    .strength(1f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)) {
                @Override
                public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("block.pathoscraft.deepslate_sadness_ore.tooltip"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });

    public static final DeferredBlock<Block> SUNNY_ORE = registerBlock("sunny_ore", () ->
            new DropExperienceBlock(UniformInt.of(2, 4), BlockBehaviour.Properties.of()
                    .strength(1f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)) {
                @Override
                public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("block.pathoscraft.sunny_ore.tooltip"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });

    public static final DeferredBlock<Block> DEEPSLATE_SUNNY_ORE = registerBlock("deepslate_sunny_ore", () ->
            new DropExperienceBlock(UniformInt.of(3, 4), BlockBehaviour.Properties.of()
                    .strength(1f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)) {
                @Override
                public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("block.pathoscraft.deepslate_sunny_ore.tooltip"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });

    public static final DeferredBlock<Block> REPAIR_BLOCK = registerBlock("repair_block", () ->
            new RepairBlock(BlockBehaviour.Properties.of()
                    .strength(2f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.AMETHYST_CLUSTER)));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new BlockItem.Properties()));
    }

    public static void register(IEventBus modEventBus){
        BLOCKS.register(modEventBus);
    }
}
