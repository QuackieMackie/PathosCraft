package net.quackiemackie.pathoscraft.item.advanced;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.quackiemackie.pathoscraft.PathosCraft;
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

//        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
//            // Open the QuestMenu
//            serverPlayer.openMenu(new SimpleMenuProvider(
//                    (containerId, playerInventory, playerEntity) -> new QuestMenu(containerId, playerInventory),
//                    Component.translatable("menu.title.pathoscraft.quest_menu")
//            ));
//        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide());
    }
}
