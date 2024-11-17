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

import java.util.logging.Logger;

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

            for (Quest quest : QuestHandler.getQuests()) {
                String questInfo = "Quest ID: " + quest.getId()
                        + ", Name: " + quest.getQuestName()
                        + ", Description: " + quest.getQuestDescription();

                player.sendSystemMessage(Component.literal(questInfo));
                Logger.getLogger(PathosCraft.MOD_ID).info(questInfo);
            }

            Quest questId = QuestHandler.getQuestById(1);
            String questInfo = "Quest ID: " + questId.getId()
                    + ", Name: " + questId.getQuestName()
                    + ", Description: " + questId.getQuestDescription()
                    + ", Objective: " + questId.getQuestObjectives()
                    + ", Reward: " + questId.getQuestRewards();
            Logger.getLogger(PathosCraft.MOD_ID).info(questInfo);
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide());
    }
}
