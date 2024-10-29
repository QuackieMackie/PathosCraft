package net.quackiemackie.pathoscraft.item.advanced;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class ManaPotion extends Item {

    public ManaPotion(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        super.finishUsingItem(stack, level, entity);

        if (entity instanceof ServerPlayer player) {
            ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE);
            if (!player.getInventory().add(emptyBottle)) {
                player.drop(emptyBottle, false);
            }
        }
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("item.pathoscraft.mana_potion.tooltip"));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
