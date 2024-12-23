package net.quackiemackie.pathoscraft.enchantment.effect;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.quackiemackie.pathoscraft.network.payload.minigames.fishing.StartFishingMiniGame;

public class FishingMiniGameEffect {
    private final float activateEffectChance;

    /**
     * Constructs a FishingMiniGameEffect instance with a specified chance
     * of activating the fishing mini-game effect.
     *
     * @param activateEffectChance A float value between 0.0 (inclusive) and 1.0 (inclusive)
     *                             representing the probability of the effect being activated.
     *                             If the value is out of this range, an
     *                             {@code IllegalArgumentException} is thrown.
     */
    public FishingMiniGameEffect(float activateEffectChance) {
        if (activateEffectChance < 0.0f || activateEffectChance > 1.0f) {
            throw new IllegalArgumentException("Effect chance must be between 0.0 and 1.0");
        }
        this.activateEffectChance = activateEffectChance;
    }

    /**
     * Determines whether the effect is successfully activated based on the configured
     * activation chance.
     *
     * A random value between 0.0 (inclusive) and 1.0 (exclusive) is generated, and the effect
     * is considered activated if this value is less than or equal to the specified
     * `activateEffectChance`.
     *
     * @return true if the effect is activated, false otherwise
     */
    public boolean isEffectActivated() {
        float randomValue = (float) Math.random();
        return randomValue <= this.activateEffectChance;
    }

    /**
     * Plays a special effect for the fishing mini-game when the activation conditions are met.
     * This will send a packet to the specified player to initiate the fishing mini-game
     * and cancel the ongoing fishing event.
     *
     * @param serverPlayer The {@code ServerPlayer} who initiated the fishing action and for
     *                     whom the mini-game will be activated.
     * @param event        The {@code ItemFishedEvent} that triggered the effect. This event
     *                     will be canceled upon activation of the mini-game.
     */
    public void playEffect(ServerPlayer serverPlayer, ItemFishedEvent event) {
        PacketDistributor.sendToPlayer(serverPlayer, StartFishingMiniGame.INSTANCE);
        event.setCanceled(true);
    }
}