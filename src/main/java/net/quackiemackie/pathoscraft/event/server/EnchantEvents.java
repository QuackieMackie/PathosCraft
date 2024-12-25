package net.quackiemackie.pathoscraft.event.server;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.enchantment.PathosEnchantment;
import net.quackiemackie.pathoscraft.enchantment.effect.ExcavationMiniGameEffect;
import net.quackiemackie.pathoscraft.enchantment.effect.FishingMiniGameEffect;
import net.quackiemackie.pathoscraft.registers.PathosTags;

@EventBusSubscriber(modid = PathosCraft.MOD_ID)
public class EnchantEvents {

    @SubscribeEvent
    public static void onPlayerMine(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player.getMainHandItem().isEmpty()) return;

        if (event.getState().is(PathosTags.Blocks.EXCAVATION_MINIGAME_ORES)) {
            int excavationEnchantmentLevel = EnchantmentHelper.getEnchantmentLevel(enchantmentHolder(PathosEnchantment.EXCAVATION_MINIGAME, player), player);

            if (excavationEnchantmentLevel == 1) {
                ExcavationMiniGameEffect effect = new ExcavationMiniGameEffect(0.20f);
                effect.handleBlockMine(event);
            }
        }
    }


    @SubscribeEvent
    public static void onItemFished(ItemFishedEvent event) {
        Player player = event.getEntity();
        if (player.getMainHandItem().isEmpty()) return;

        int fishingEnchantmentLevel = EnchantmentHelper.getEnchantmentLevel(enchantmentHolder(PathosEnchantment.FISHING_MINIGAME, player), player);

        if (fishingEnchantmentLevel == 1) {
            FishingMiniGameEffect effect = new FishingMiniGameEffect(0.20f);

            if (effect.isEffectActivated()) {
                effect.playEffect((ServerPlayer) player, event);
            }
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
        RegistryAccess registryAccess = player.level().registryAccess();
        Registry<Enchantment> enchantmentRegistry = registryAccess.registryOrThrow(Registries.ENCHANTMENT);
        return enchantmentRegistry.getHolderOrThrow(enchantment);
    }
}
