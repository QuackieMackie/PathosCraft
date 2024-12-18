package net.quackiemackie.pathoscraft.util.handlers.minigames;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.gui.screen.FishingMiniGame;

//TODO:
// Need to map the loot to some kind of configurable value.

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


    /**
     * Rewards the player based on the score they achieved in a mini-game.
     * Grants experience points and items to the player according to the specified score ranges.
     * If the score does not fall within recognized ranges, logs a warning message.
     *
     * @param score  The score the player obtained in the mini-game.
     * @param player The player to be rewarded for completing the mini-game.
     */
    public static void rewardCompletedMiniGame(int score, Player player) {
        PathosCraft.LOGGER.info("Rewarding: {}, for getting {} points", player, score);

        if (score >= 5 && score <= 40) {
            player.giveExperiencePoints(5);
            player.addItem(new ItemStack(Items.COD, 3));
        }
        else if (score >= 41 && score <= 55) {
            player.giveExperiencePoints(15);
            player.addItem(new ItemStack(Items.GOLD_NUGGET, 5));
        }
        else if (score >= 56 && score <= 60) {
            player.giveExperiencePoints(30);
            player.addItem(new ItemStack(Items.DIAMOND, 1));
        } else {
            PathosCraft.LOGGER.warn("Unrecognized score: {} for player {}", score, player.getName().getString());
        }
    }
}
