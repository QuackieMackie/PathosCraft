package net.quackiemackie.pathoscraft.block.advanced;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.quackiemackie.pathoscraft.util.ModTags;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RepairBlock extends Block {

    public static final BooleanProperty CLICKED = BooleanProperty.create("clicked");

    public RepairBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(CLICKED, false));
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        level.playSound(player, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1f, 1f);
        boolean currentState = state.getValue(CLICKED);
        if (!level.isClientSide()) {
            if (player.isShiftKeyDown()) {
                level.setBlockAndUpdate(pos, state.setValue(CLICKED, !currentState));

                if (player instanceof ServerPlayer serverPlayer) {
                    if (currentState) {
                        serverPlayer.sendSystemMessage(Component.literal("Repair Block has been set to active!"));
                    } else {
                        serverPlayer.sendSystemMessage(Component.literal("Repair Block has been set to inactive!"));
                    }
                }
            } else if (!currentState && isValidItem(player.getMainHandItem()) && player.getMainHandItem().getDamageValue() > 0) {
                ItemStack itemStack = player.getMainHandItem();
                itemStack.setDamageValue(0);

                level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1f, 1f);
                ((ServerLevel) level).sendParticles(ParticleTypes.HEART, pos.getX(), pos.getY() + 1, pos.getZ(), 10, 0.5, 0.5, 0.5, 0.2);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void stepOn(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Entity entity) {

        if (entity instanceof Player) {
            level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1f, 1f);
        }

        if (!level.isClientSide) {
            boolean currentState = state.getValue(CLICKED);
            if (entity instanceof ItemEntity itemEntity) {
                if (!currentState && isValidItem(itemEntity.getItem()) && itemEntity.getItem().getDamageValue() > 0) {
                    ItemStack itemStack = itemEntity.getItem();
                    itemStack.setDamageValue(0);

                    level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1f, 1f);

                    ((ServerLevel) level).sendParticles(ParticleTypes.HEART, pos.getX(), pos.getY() + 1, pos.getZ(), 10, 0.5, 0.5, 0.5, 0.2);

                    itemEntity.setItem(itemStack);
                }
            }

            super.stepOn(level, pos, state, entity);
        }
    }

    private boolean isValidItem(ItemStack itemStack){
        return itemStack.is(ModTags.Items.REPAIRABLE_BLOCK_ITEMS);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CLICKED);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("block.pathoscraft.repair_block.tooltip"));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
