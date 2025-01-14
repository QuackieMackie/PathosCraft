package io.github.quackiemackie.pathoscraft.enchantment.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import io.github.quackiemackie.pathoscraft.network.payload.minigames.excavation.StartExcavationMiniGame;

import java.util.List;

public class ExcavationMiniGameEffect {
    private final float activateEffectChance;

    /**
     * Constructs an instance of ExcavationMiniGameEffect with a specified chance of triggering the effect.
     * Throws an exception if the chance is not within the valid range of 0.0 to 1.0.
     *
     * @param activateEffectChance A floating-point value representing the probability of activating the effect.
     *                             Must be between 0.0 (inclusive) and 1.0 (inclusive).
     * @throws IllegalArgumentException if the provided chance is outside the range of 0.0 to 1.0.
     */
    public ExcavationMiniGameEffect(float activateEffectChance) {
        if (activateEffectChance < 0.0f || activateEffectChance > 1.0f) {
            throw new IllegalArgumentException("Effect chance must be between 0.0 and 1.0");
        }
        this.activateEffectChance = activateEffectChance;
    }

    /**
     * Determines if the excavation mini-game effect should be activated based on a randomly
     * generated value and the predefined activation chance.
     *
     * @return true if the effect is activated, false otherwise.
     */
    public boolean isEffectActivated() {
        float randomValue = (float) Math.random();
        return randomValue <= this.activateEffectChance;
    }

    /**
     * Handles the event when a block is mined by the player. This method determines
     * whether a special effect should be activated or if the block should be destroyed
     * naturally with its drops.
     *
     * @param event The block break event, containing information about the player,
     *              the block being broken, its position, and the level it is in.
     */
    public void handleBlockMine(BlockEvent.BreakEvent event) {
        ServerPlayer player = (ServerPlayer) event.getPlayer();
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();

        List<ItemStack> drops = Block.getDrops(state, (ServerLevel) level, pos, null, player, player.getMainHandItem());
        ItemStack firstDrop = drops.isEmpty() ? ItemStack.EMPTY : drops.getFirst();

        if (isEffectActivated()) {
            playEffect(player, event, firstDrop);
        } else {
            level.destroyBlock(pos, true, player); // Breaks the block and allows natural drops
        }
    }

    /**
     * Initiates the excavation mini-game effect for the specified player and block break event.
     * Sends a packet to the player, prompting the start of the mini-game with the item drop,
     * and destroys the specified block without dropping items into the world.
     *
     * @param serverPlayer The server-side player that initiated the block break event and will receive the mini-game packet.
     * @param event The block break event providing details about the block being broken, including its position and level.
     * @param drop The item stack representing the primary drop of the block, to be used in the mini-game.
     */
    public void playEffect(ServerPlayer serverPlayer, BlockEvent.BreakEvent event, ItemStack drop) {
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();

        PacketDistributor.sendToPlayer(serverPlayer, new StartExcavationMiniGame(drop));
        level.destroyBlock(pos, false, serverPlayer);
    }
}