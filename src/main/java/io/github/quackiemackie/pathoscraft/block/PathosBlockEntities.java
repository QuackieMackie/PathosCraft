package io.github.quackiemackie.pathoscraft.block;

import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.block.entity.WorkerStationBE;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PathosBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, PathosCraft.MOD_ID);

    public static final Supplier<BlockEntityType<WorkerStationBE>> WORKER_STATION_BE = BLOCK_ENTITY.register("worker_station_be", () -> BlockEntityType.Builder.of(WorkerStationBE::new, PathosBlocks.WORKER_STATION.get()).build(null));

}
