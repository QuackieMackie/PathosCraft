package io.github.quackiemackie.pathoscraft.util.handlers.minigames;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import io.github.quackiemackie.pathoscraft.gui.screen.minigame.LumberingScreen;

public class LumberingHandler {
    public static void startMiniGame(Player player, ItemStack blockBroken) {
        if (player.level().isClientSide && player == Minecraft.getInstance().player) {
            Minecraft.getInstance().setScreen(new LumberingScreen(blockBroken));
        }
    }

    public static void rewardCompletedMiniGame() {
    }
}
