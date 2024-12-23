package net.quackiemackie.pathoscraft.event.server;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemStackedOnOtherEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.util.handlers.quest.QuestHandler;
import net.quackiemackie.pathoscraft.item.advanced.QuestBook;

@EventBusSubscriber(modid = PathosCraft.MOD_ID)
public class QuestEvents {

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            ResourceLocation targetEntityId = BuiltInRegistries.ENTITY_TYPE.getKey(event.getEntity().getType());

            PathosCraft.LOGGER.info("Progress updated: {}", targetEntityId);
            QuestHandler.updateQuestKillProgress(player, targetEntityId.toString());
        }
    }

    @SubscribeEvent
    public static void onItemStackedOnOther(ItemStackedOnOtherEvent event) {
        Player player = event.getPlayer();
        ItemStack carriedItem = event.getCarriedItem();
        ItemStack stackedOnItem = event.getStackedOnItem();

        if (carriedItem.getItem() instanceof QuestBook && player instanceof ServerPlayer serverPlayer) {
            ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(stackedOnItem.getItem());
            if (QuestHandler.isRelevantQuestItem(serverPlayer, itemRegistryName.toString()) && QuestHandler.willUpdateQuestProgress(serverPlayer, itemRegistryName.toString())) {
                int requiredAmountForQuest = QuestHandler.getAmountForQuest(player, itemRegistryName.toString());
                int quantityToRemove = Math.min(stackedOnItem.getCount(), requiredAmountForQuest);

                QuestHandler.updateQuestPickupProgress(serverPlayer, itemRegistryName.toString(), quantityToRemove);
                stackedOnItem.shrink(quantityToRemove);

                event.setCanceled(true);
            }
        }
    }
}
