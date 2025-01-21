package io.github.quackiemackie.pathoscraft.item.items;

import io.github.quackiemackie.pathoscraft.gui.screen.worker.WorkerMapScreen;
import net.minecraft.client.Minecraft;
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
        ItemStack excavationStack = PathosItems.RAW_SADNESS.get().getDefaultInstance();
        ItemStack lumberingStack = Blocks.OAK_LOG.asItem().getDefaultInstance();
//        FishingHandler.startMiniGame(player);
//        ExcavationHandler.startMiniGame(player, excavationStack);
//        LumberingHandler.startMiniGame(player, lumberingStack);
//
//        if (!level.isClientSide && player != null) {
//            clearPlayerDataAttachment(player);
//        }
//
//        excavationStack.setCount(10);
//        WorkerResources stone = new WorkerResources(excavationStack);
//
//        PathosCraft.LOGGER.info(String.valueOf(Worker.artisanWorker(1, List.of(new WorkerResources(excavationStack), stone))));
//
        if (player.level().isClientSide && player == Minecraft.getInstance().player) {
            Minecraft.getInstance().setScreen(new WorkerMapScreen());
        }
//
//        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
//            serverPlayer.openMenu(new SimpleMenuProvider((id, inventory, serverPlayerEntity) ->
//                    new WorkerHireMenu(id, inventory, null),
//                    Component.translatable("screen.title.pathoscraft.worker_hire_menu")
//            ));
//        }
        return super.use(level, player, hand);
    }

    private void clearPlayerDataAttachment(Player player) {
        player.setData(PathosAttachments.COMPLETED_QUESTS.get(), new ArrayList<>());
        PathosCraft.LOGGER.info("Cleared data attachment for player {}", player.getName().getString());
        List<Quest> completedQuests = player.getData(PathosAttachments.COMPLETED_QUESTS.get());
        PacketDistributor.sendToPlayer((ServerPlayer) player, new ClearCompletedQuests(completedQuests));
    }
}
