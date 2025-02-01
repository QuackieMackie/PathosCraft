package io.github.quackiemackie.pathoscraft.block.block;

import com.mojang.serialization.MapCodec;
import io.github.quackiemackie.pathoscraft.block.entity.WorkerStationBE;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class WorkerStation extends BaseEntityBlock {
    public static final MapCodec<WorkerStation> CODEC = simpleCodec(WorkerStation::new);

    public WorkerStation(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new WorkerStationBE(blockPos, blockState);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(state.getBlock() != newState.getBlock()) {
            if(level.getBlockEntity(pos) instanceof WorkerStationBE workerStationBlockEntity) {
                workerStationBlockEntity.drops();
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(level.getBlockEntity(pos) instanceof WorkerStationBE workerStationBlockEntity) {
            if (!level.isClientSide()) {
                player.openMenu(new SimpleMenuProvider(workerStationBlockEntity, Component.literal("worker_main_menu")), pos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.SUCCESS;
    }
}
