package net.quackiemackie.pathoscraft.item.advanced;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.quackiemackie.pathoscraft.registers.PathosDataComponents;

import java.util.List;

public class JumpWand extends Item {

    public JumpWand(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("item.pathoscraft.jump_wand.tooltip"));

        if (stack.get(PathosDataComponents.COORDINATES) != null) {
            tooltipComponents.add(Component.literal("Last Jump location: " + stack.get(PathosDataComponents.COORDINATES)));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
