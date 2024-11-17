package net.quackiemackie.pathoscraft.item.advanced;

import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.gui.menu.QuestMenu;
import net.quackiemackie.pathoscraft.handlers.QuestHandler;
import net.quackiemackie.pathoscraft.quest.Quest;

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

            List<Quest> questsByType = QuestHandler.getQuestsByType(0);

            StringBuilder questTypeInfoBuilder = new StringBuilder("Quest Type Test");
            for (Quest quest : questsByType) {
                questTypeInfoBuilder.append("\nQuest ID: ").append(quest.getId())
                        .append(", Name: ").append(quest.getQuestName())
                        .append(", Description: ").append(quest.getQuestDescription())
                        .append(", Type: ").append(quest.getQuestType())
                        .append(", Objective: ").append(quest.getQuestObjectives())
                        .append(", Reward: ").append(quest.getQuestRewards());
            }
            String questTypeInfo = questTypeInfoBuilder.toString();
            PathosCraft.LOGGER.info(questTypeInfo);
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide());
    }
}
