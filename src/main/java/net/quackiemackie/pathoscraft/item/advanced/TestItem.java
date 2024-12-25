package net.quackiemackie.pathoscraft.item.advanced;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.network.PacketDistributor;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.item.PathosItems;
import net.quackiemackie.pathoscraft.network.payload.quest.completed.ClearCompletedQuests;
import net.quackiemackie.pathoscraft.registers.PathosAttachments;
import net.quackiemackie.pathoscraft.util.handlers.minigames.FishingHandler;
import net.quackiemackie.pathoscraft.util.quest.Quest;

import java.util.ArrayList;
import java.util.List;

public class TestItem extends Item {
    public TestItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = PathosItems.RAW_SADNESS.get().getDefaultInstance();
        FishingHandler.startMiniGame(player);
        //ExcavationHandler.startMiniGame(player, stack);
        if (!level.isClientSide && player != null) {
            clearPlayerDataAttachment(player);
        }

        return super.use(level, player, hand);
    }

    private void clearPlayerDataAttachment(Player player) {
        ((IAttachmentHolder) player).setData(PathosAttachments.COMPLETED_QUESTS.get(), new ArrayList<>());
        PathosCraft.LOGGER.info("Cleared data attachment for player {}", player.getName().getString());
        List<Quest> completedQuests = ((IAttachmentHolder) player).getData(PathosAttachments.COMPLETED_QUESTS.get());
        PacketDistributor.sendToPlayer((ServerPlayer) player, new ClearCompletedQuests(completedQuests));
    }
}
