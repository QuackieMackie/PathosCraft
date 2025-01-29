package io.github.quackiemackie.pathoscraft.block;

import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.block.entity.WorkerStationBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PathosBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, PathosCraft.MOD_ID);

    public static final Supplier<BlockEntityType<WorkerStationBlockEntity>> WORKER_STATION_ENTITY = BLOCK_ENTITY.register("worker_station_entity", () -> BlockEntityType.Builder.of(WorkerStationBlockEntity::new, PathosBlocks.WORKER_STATION_BLOCK.get()).build(null));

}
