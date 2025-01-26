package io.github.quackiemackie.pathoscraft.item.items;

import io.github.quackiemackie.pathoscraft.util.handlers.minigames.ExcavationHandler;
import io.github.quackiemackie.pathoscraft.util.handlers.minigames.FishingHandler;
import io.github.quackiemackie.pathoscraft.util.handlers.minigames.LumberingHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.network.PacketDistributor;
import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.item.PathosItems;
import io.github.quackiemackie.pathoscraft.network.payload.quest.completed.ClearCompletedQuests;
import io.github.quackiemackie.pathoscraft.registers.PathosAttachments;
import io.github.quackiemackie.pathoscraft.util.quest.Quest;

import java.util.ArrayList;
import java.util.List;

public class TestItem extends Item {
    public TestItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        //startFishingMinigame(player);
        //startExcavationMinigame(player);
        //startLumberingMinigame(player);
        //clearPlayerDataAttachment(player, level);
        return super.use(level, player, hand);
    }

    private void startFishingMinigame(Player player) {
        FishingHandler.startMiniGame(player);
    }

    private void startExcavationMinigame(Player player) {
        ItemStack excavationStack = PathosItems.RAW_SADNESS.get().getDefaultInstance();
        ExcavationHandler.startMiniGame(player, excavationStack);
    }

    private void startLumberingMinigame(Player player) {
        ItemStack lumberingStack = Blocks.OAK_LOG.asItem().getDefaultInstance();
        LumberingHandler.startMiniGame(player, lumberingStack);
    }

    private void clearPlayerDataAttachment(Player player, Level level) {
        if (!level.isClientSide && player != null) {
            player.setData(PathosAttachments.COMPLETED_QUESTS.get(), new ArrayList<>());
            PathosCraft.LOGGER.info("Cleared data attachment for player {}", player.getName().getString());
            List<Quest> completedQuests = player.getData(PathosAttachments.COMPLETED_QUESTS.get());
            PacketDistributor.sendToPlayer((ServerPlayer) player, new ClearCompletedQuests(completedQuests));
        }
    }
}
