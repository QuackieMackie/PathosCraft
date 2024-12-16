package net.quackiemackie.pathoscraft.util.handlers.minigames;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.gui.screen.FishingMiniGame;

public class FishingHandler {

    /**
     * Starts the fishing mini-game, opening the mini-game screen for the player
     * if the current environment is client-side and the player is the local player.
     * Logs a message indicating the method was triggered.
     *
     * @param player The player instance for whom the mini-game is triggered.
     */
    public static void startMiniGame(Player player) {
        if (player.level().isClientSide && player == Minecraft.getInstance().player) {
            Minecraft.getInstance().setScreen(new FishingMiniGame());
        }
    }

    public static void rewardCompletedMiniGame(Player player) {
        PathosCraft.LOGGER.info("Logic for rewarding {} goes here.", player);
    }
}
