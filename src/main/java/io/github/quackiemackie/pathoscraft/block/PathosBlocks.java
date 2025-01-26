package io.github.quackiemackie.pathoscraft.block;

import io.github.quackiemackie.pathoscraft.block.block.WorkerStationBlock;
import io.github.quackiemackie.pathoscraft.block.entity.WorkerStationEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.block.block.RepairBlock;
import io.github.quackiemackie.pathoscraft.item.PathosItems;

import java.util.function.Supplier;

public class PathosBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(PathosCraft.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, PathosCraft.MOD_ID);

    public static final DeferredBlock<Block> SADNESS_BLOCK = registerBlock("sadness_block", () -> new Block(BlockBehaviour.Properties.of().strength(1f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> SADNESS_ORE = registerBlock("sadness_ore", () -> new DropExperienceBlock(UniformInt.of(2, 4), BlockBehaviour.Properties.of().strength(1f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> DEEPSLATE_SADNESS_ORE = registerBlock("deepslate_sadness_ore", () -> new DropExperienceBlock(UniformInt.of(3, 4), BlockBehaviour.Properties.of().strength(1f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> SUNNY_ORE = registerBlock("sunny_ore", () -> new DropExperienceBlock(UniformInt.of(2, 4), BlockBehaviour.Properties.of().strength(1f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> DEEPSLATE_SUNNY_ORE = registerBlock("deepslate_sunny_ore", () -> new DropExperienceBlock(UniformInt.of(3, 4), BlockBehaviour.Properties.of().strength(1f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> REPAIR_BLOCK = registerBlock("repair_block", () -> new RepairBlock(BlockBehaviour.Properties.of().strength(2f).requiresCorrectToolForDrops().sound(SoundType.AMETHYST_CLUSTER)));
    public static final DeferredBlock<Block> WORKER_STATION_BLOCK = registerBlock("worker_station_block", () -> new WorkerStationBlock(BlockBehaviour.Properties.of()));

    public static final Supplier<BlockEntityType<WorkerStationEntity>> WORKER_STATION_ENTITY = BLOCK_ENTITY.register("worker_station_entity", () -> BlockEntityType.Builder.of(WorkerStationEntity::new, PathosBlocks.WORKER_STATION_BLOCK.get()).build(null));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        PathosItems.ITEMS.register(name, () -> new BlockItem(block.get(), new BlockItem.Properties()));
    }
}
