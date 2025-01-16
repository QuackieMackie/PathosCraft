package io.github.quackiemackie.pathoscraft.util.handlers.minigames;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.gui.screen.minigame.ExcavationScreen;

public class ExcavationHandler {
    public static void startMiniGame(Player player, ItemStack blockDrop) {
        if (player.level().isClientSide && player == Minecraft.getInstance().player) {
            Minecraft.getInstance().setScreen(new ExcavationScreen(blockDrop));
        }
    }

    public static void rewardCompletedMiniGame(int quantity, ItemStack rewardItem, Player player) {
        rewardItem.setCount(quantity);
        player.drop(rewardItem, false);
        PathosCraft.LOGGER.info("Rewarding: {}, for finding {}x {} ore.", player, quantity, rewardItem);
    }
}
