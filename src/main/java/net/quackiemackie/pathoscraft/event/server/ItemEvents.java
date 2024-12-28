package net.quackiemackie.pathoscraft.event.server;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.item.advanced.CreatureCrystal;
import net.quackiemackie.pathoscraft.registers.PathosDataComponents;

@EventBusSubscriber(modid = PathosCraft.MOD_ID)
public class ItemEvents {

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        Level level = player.level();
        ItemStack heldItem = player.getItemInHand(event.getHand());

        if (heldItem.getItem() instanceof CreatureCrystal && heldItem.get(PathosDataComponents.ENTITY_CAPTURED_DATA) == null) {
            Entity target = event.getTarget();
            if (target.isAlive() && !(target instanceof Player) && !(target instanceof ItemEntity)) {
                if (!level.isClientSide) {
                    CreatureCrystal.captureEntity(heldItem, level, player, target);
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = player.level();
        ItemStack heldItem = player.getItemInHand(event.getHand());

        if (heldItem.getItem() instanceof CreatureCrystal && player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                CreatureCrystal crystal = (CreatureCrystal) heldItem.getItem();
                InteractionResult result = crystal.releaseEntityAt(heldItem, level, player, event.getHitVec().getBlockPos().above());

                if (result == InteractionResult.SUCCESS) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
