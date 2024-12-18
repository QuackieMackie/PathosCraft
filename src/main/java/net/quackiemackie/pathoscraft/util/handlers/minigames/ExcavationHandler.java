package net.quackiemackie.pathoscraft.util.handlers.minigames;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.gui.screen.ExcavationMiniGame;

public class ExcavationHandler {
    public static void startMiniGame(Player player) {
        if (player.level().isClientSide && player == Minecraft.getInstance().player) {
            Minecraft.getInstance().setScreen(new ExcavationMiniGame());
        }
    }

    public static void rewardCompletedMiniGame(int foundOre, Player player) {
        PathosCraft.LOGGER.info("Rewarding: {}, for finding {} ore.", player, foundOre);
    }
}
