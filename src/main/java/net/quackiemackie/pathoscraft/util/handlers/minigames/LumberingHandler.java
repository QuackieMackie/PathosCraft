package net.quackiemackie.pathoscraft.util.handlers.minigames;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.quackiemackie.pathoscraft.gui.screen.LumberingMiniGame;

public class LumberingHandler {
    public static void startMiniGame(Player player) {
        if (player.level().isClientSide && player == Minecraft.getInstance().player) {
            Minecraft.getInstance().setScreen(new LumberingMiniGame());
        }
    }

    public static void rewardCompletedMiniGame() {
    }
}
