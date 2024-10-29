package net.quackiemackie.pathoscraft.item.advanced;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class JumpWand extends Item {

    public JumpWand(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context){
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (player != null) {
            if (!level.isClientSide) {
                player.teleportTo(context.getClickedPos().getX() + 0.5D, context.getClickedPos().getY() + 1, context.getClickedPos().getZ() + 0.5D);

                context.getItemInHand().hurtAndBreak(1, ((ServerLevel) level), context.getPlayer(), item ->
                        context.getPlayer().onEquippedItemBroken(item, EquipmentSlot.MAINHAND));

                level.playSound(null, context.getClickedPos(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL);
                ((ServerLevel) level).sendParticles(ParticleTypes.PORTAL, player.getX(), player.getY(), player.getZ(), 50, 0.5, 0.5, 0.5, 0.2);
            }
        }
        return super.useOn(context);
    }
}
