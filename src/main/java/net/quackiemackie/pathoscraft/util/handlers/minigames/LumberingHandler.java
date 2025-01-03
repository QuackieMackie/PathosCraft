package net.quackiemackie.pathoscraft.util.handlers.minigames;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.quackiemackie.pathoscraft.gui.screen.LumberingMiniGame;

public class LumberingHandler {
    public static void startMiniGame(Player player, ItemStack blockBroken) {
        if (player.level().isClientSide && player == Minecraft.getInstance().player) {
            Minecraft.getInstance().setScreen(new LumberingMiniGame(blockBroken));
        }
    }

    public static void rewardCompletedMiniGame() {
    }
}
