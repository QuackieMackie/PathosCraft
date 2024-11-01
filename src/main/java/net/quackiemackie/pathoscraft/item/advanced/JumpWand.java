package net.quackiemackie.pathoscraft.item.advanced;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.quackiemackie.pathoscraft.attachments.ModAttachments;
import net.quackiemackie.pathoscraft.component.ModDataComponents;
import net.quackiemackie.pathoscraft.handlers.AstralFormHandler;

import java.util.List;
import java.util.logging.Logger;

//TODO:
// I want this class to eventually be changed and improved (when I have more experience)
// The goal is to have the player use the one to enter an astral form.
// The astral form would leave the body behind.
// (It would be possible to still damage (Taking damage would snap the player out of the astral form))
// The player would be limited in the distance it would be able to travel from the players body.
// When the player leaves x distance from the body an overlay would be applied and their vision would look kind of static around the edges.
// If they got to far after being warned they would snap back to their body and be heavily damaged.
// The wand would let them jump to locations their astral body discovers.
// Similar to spectator mode they'd be able to fly and phase through walls.


public class JumpWand extends Item {

    private static final Logger logger = Logger.getLogger("PathosCraft");

    public JumpWand(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context){
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (!level.isClientSide && !player.getData(ModAttachments.IN_ASTRAL_FORM)) {
            AstralFormHandler.enterAstralForm(player);

            context.getItemInHand().hurtAndBreak(1, ((ServerLevel) level), context.getPlayer(), item ->
                    context.getPlayer().onEquippedItemBroken(item, EquipmentSlot.MAINHAND));

            level.playSound(null, context.getClickedPos(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL);
            ((ServerLevel) level).sendParticles(ParticleTypes.PORTAL, player.getX(), player.getY(), player.getZ(), 50, 0.5, 0.5, 0.5, 0.2);

            context.getItemInHand().set(ModDataComponents.COORDINATES, context.getClickedPos());
        }

        return super.useOn(context);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("item.pathoscraft.jump_wand.tooltip"));

        if (stack.get(ModDataComponents.COORDINATES) != null) {
            tooltipComponents.add(Component.literal("Last Jump location: " + stack.get(ModDataComponents.COORDINATES)));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
