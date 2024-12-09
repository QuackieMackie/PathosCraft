package net.quackiemackie.pathoscraft.item.advanced;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.network.PacketDistributor;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.network.payload.QuestMenuCompletedQuestsPayload;
import net.quackiemackie.pathoscraft.quest.Quest;
import net.quackiemackie.pathoscraft.registers.PathosAttachments;
import net.quackiemackie.pathoscraft.registers.PathosDataComponents;

import java.util.ArrayList;
import java.util.List;

public class JumpWand extends Item {

    public JumpWand(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (!level.isClientSide && player != null) {
            clearPlayerDataAttachment(player);
            sendClearDataPayload(player);
        }

        return super.useOn(context);
    }

    private void clearPlayerDataAttachment(Player player) {
        ((IAttachmentHolder) player).setData(PathosAttachments.COMPLETED_QUESTS.get(), new ArrayList<>());
        PathosCraft.LOGGER.info("Cleared data attachment for player {}", player.getName().getString());
    }

    private void sendClearDataPayload(Player player) {
        List<Quest> completedQuests = ((IAttachmentHolder) player).getData(PathosAttachments.COMPLETED_QUESTS.get());
        PacketDistributor.sendToPlayer((ServerPlayer) player, new QuestMenuCompletedQuestsPayload(completedQuests));
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
