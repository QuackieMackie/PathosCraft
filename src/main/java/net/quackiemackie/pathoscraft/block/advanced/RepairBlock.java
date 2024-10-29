package net.quackiemackie.pathoscraft.block.advanced;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.phys.BlockHitResult;
import net.quackiemackie.pathoscraft.item.ModItems;

import java.util.List;

public class RepairBlock extends Block {

    public RepairBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        level.playSound(player, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1f, 1f);

        if (!level.isClientSide) {
            if (player.getMainHandItem().getItem() == ModItems.JUMP_WAND.get() && player.getMainHandItem().getDamageValue() > 0) {
                ItemStack itemStack = player.getMainHandItem();
                itemStack.setDamageValue(0);

                level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1f, 1f);
                ((ServerLevel) level).sendParticles(ParticleTypes.HEART, pos.getX(), pos.getY() + 1, pos.getZ(), 10, 0.5, 0.5, 0.5, 0.2);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {

        if (entity instanceof Player) {
            level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1f, 1f);
        }

        if (!level.isClientSide) {
            if (entity instanceof ItemEntity itemEntity) {
                if (itemEntity.getItem().getItem() == ModItems.JUMP_WAND.get() && itemEntity.getItem().getDamageValue() > 0) {
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

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("block.pathoscraft.repair_block.tooltip"));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
