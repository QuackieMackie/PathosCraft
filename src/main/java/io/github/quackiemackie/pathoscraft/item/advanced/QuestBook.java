package io.github.quackiemackie.pathoscraft.item.advanced;

import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import io.github.quackiemackie.pathoscraft.gui.menu.QuestMenu;

import java.util.List;

public class QuestBook extends Item {

    public QuestBook(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeBlockPos(new BlockPos(serverPlayer.blockPosition().getX(), serverPlayer.blockPosition().getY(), serverPlayer.blockPosition().getZ()));
            serverPlayer.openMenu(new SimpleMenuProvider(
                    (containerId, playerInventory, menuPlayer) -> new QuestMenu(containerId, playerInventory, buf),
                    Component.translatable("menu.title.pathoscraft.quest_menu")
            ));
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("item.pathoscraft.quest_book.tooltip"));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}