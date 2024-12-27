package net.quackiemackie.pathoscraft.event.server;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.enchantment.PathosEnchantment;
import net.quackiemackie.pathoscraft.enchantment.effect.ExcavationMiniGameEffect;
import net.quackiemackie.pathoscraft.enchantment.effect.FishingMiniGameEffect;
import net.quackiemackie.pathoscraft.registers.PathosDataComponents;
import net.quackiemackie.pathoscraft.registers.PathosTags;

@EventBusSubscriber(modid = PathosCraft.MOD_ID)
public class EnchantEvents {

    @SubscribeEvent
    public static void onPlayerMine(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) return;

        if (event.getState().is(PathosTags.Blocks.EXCAVATION_MINIGAME_ORES) && EnchantmentHelper.getEnchantmentLevel(enchantmentHolder(PathosEnchantment.MINIGAME, player), player) == 1) {

            Boolean activeMinigame = stack.get(PathosDataComponents.ACTIVE_MINIGAME_BOOL);
            if (activeMinigame == null) {
                activeMinigame = true;
                stack.set(PathosDataComponents.ACTIVE_MINIGAME_BOOL, activeMinigame);
            }

            if (activeMinigame) {
                ExcavationMiniGameEffect effect = new ExcavationMiniGameEffect(1.00f);
                effect.handleBlockMine(event);
            }
        }
    }

    @SubscribeEvent
    public static void onItemFished(ItemFishedEvent event) {
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) return;

        if (EnchantmentHelper.getEnchantmentLevel(enchantmentHolder(PathosEnchantment.MINIGAME, player), player) == 1) {

            Boolean activeMinigame = stack.get(PathosDataComponents.ACTIVE_MINIGAME_BOOL);
            if (activeMinigame == null) {
                activeMinigame = true;
                stack.set(PathosDataComponents.ACTIVE_MINIGAME_BOOL, activeMinigame);
            }

            if (activeMinigame) {
                FishingMiniGameEffect effect = new FishingMiniGameEffect(1.00f);
                if (effect.isEffectActivated()) {
                    effect.playEffect((ServerPlayer) player, event);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.RightClickItem event) {
        ItemStack stack = event.getItemStack();
        Player player = event.getEntity();
        if (EnchantmentHelper.getEnchantmentLevel(enchantmentHolder(PathosEnchantment.MINIGAME, player), player) == 1 && player.isShiftKeyDown() && stack.is(PathosTags.Items.MINIGAME_ENCHANT_ITEMS)) {

            Boolean activeMinigame = stack.get(PathosDataComponents.ACTIVE_MINIGAME_BOOL);
            boolean isMinigameActive = activeMinigame != null && activeMinigame;

            stack.set(PathosDataComponents.ACTIVE_MINIGAME_BOOL, !isMinigameActive);

            Component status = Component.translatable(isMinigameActive
                    ? "message.pathoscraft.status.inactive"
                    : "message.pathoscraft.status.active");
            player.displayClientMessage(Component.translatable("message.pathoscraft.minigame_status", status), true);

            event.setCanceled(true);
        }
    }

    /**
     * Retrieves a {@link Holder} for the specified enchantment based on the provided player context.
     *
     * @param enchantment The resource key representing the targeted enchantment.
     * @param player The player whose current level's registry access is used to locate the enchantment holder.
     * @return A holder containing the specified enchantment, fetched from the player's current registry.
     */
    private static Holder<Enchantment> enchantmentHolder(ResourceKey<Enchantment> enchantment, Player player) {
        try {
            RegistryAccess registryAccess = player.level().registryAccess();
            Registry<Enchantment> enchantmentRegistry = registryAccess.registryOrThrow(Registries.ENCHANTMENT);
            return enchantmentRegistry.getHolderOrThrow(enchantment);
        } catch (Exception e) {
            PathosCraft.LOGGER.error("Failed to retrieve enchantment holder for enchantment {}: {}", enchantment, e.getMessage());
            throw e;
        }
    }
}
