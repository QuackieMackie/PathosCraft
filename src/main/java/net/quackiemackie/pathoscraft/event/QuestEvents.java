package net.quackiemackie.pathoscraft.event;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.handlers.QuestHandler;

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
    public static void onItemPickup(ItemEntityPickupEvent.Post event) {
        Player player = event.getPlayer();
        ItemStack pickedUpItem = event.getOriginalStack();

        ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(pickedUpItem.getItem());

        if (QuestHandler.isRelevantQuestItem(player, itemRegistryName.toString())) {
            PathosCraft.LOGGER.info("Player {} picked up quest-related item: {}, Quantity: {}", player.getName().getString(), itemRegistryName, pickedUpItem.getCount());
            QuestHandler.updateQuestPickupProgress(player, itemRegistryName.toString(), pickedUpItem.getCount());
        }
    }
}
