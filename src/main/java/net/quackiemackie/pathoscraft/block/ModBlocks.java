package net.quackiemackie.pathoscraft.block;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
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

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(PathosCraft.MOD_ID);

    public static final DeferredBlock<Block> SADNESS_BLOCK = registerBlock("sadness_block", () ->
            new Block(BlockBehaviour.Properties.of()
                    .strength(1f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> SADNESS_ORE = registerBlock("sadness_ore", () ->
            new DropExperienceBlock(UniformInt.of(2, 4), BlockBehaviour.Properties.of()
                    .strength(1f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> DEEPSLATE_SADNESS_ORE = registerBlock("deepslate_sadness_ore", () ->
            new DropExperienceBlock(UniformInt.of(3, 4), BlockBehaviour.Properties.of()
                    .strength(1f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> SUNNY_ORE = registerBlock("sunny_ore", () ->
            new DropExperienceBlock(UniformInt.of(2, 4), BlockBehaviour.Properties.of()
                    .strength(1f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> DEEPSLATE_SUNNY_ORE = registerBlock("deepslate_sunny_ore", () ->
            new DropExperienceBlock(UniformInt.of(3, 4), BlockBehaviour.Properties.of()
                    .strength(1f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> REPAIR_BLOCK = registerBlock("repair_block", () ->
            new RepairBlock(BlockBehaviour.Properties.of()
                    .strength(2f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.AMETHYST_CLUSTER)));

    //Tests
//    public static final DeferredBlock<Block> SEED_TEST_BLOCK = registerBlock("seed_test_block", () ->
//            new CropBlock(BlockBehaviour.Properties.of()
//                    .mapColor(MapColor.PLANT)
//                    .noCollission()
//                    .randomTicks()
//                    .instabreak()
//                    .sound(SoundType.CROP)
//                    .pushReaction(PushReaction.DESTROY)));

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
