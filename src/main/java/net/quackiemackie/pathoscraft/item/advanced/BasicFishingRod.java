package net.quackiemackie.pathoscraft.item.advanced;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.gui.screen.FishingMiniGame;

public class BasicFishingRod extends FishingRodItem {

    public BasicFishingRod(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        PathosCraft.LOGGER.info("BasicFishingRod: use method triggered!");

        ItemStack itemStack = player.getItemInHand(hand);

        if (level.isClientSide) {
            startMiniGame(player);
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }

    private void startMiniGame(Player player) {
        // Only act on the client-side instance
        if (player.level().isClientSide && player == Minecraft.getInstance().player) {
            PathosCraft.LOGGER.info("BasicFishingRod: startMiniGame method triggered!");
            Minecraft.getInstance().setScreen(new FishingMiniGame());
        }
    }
}