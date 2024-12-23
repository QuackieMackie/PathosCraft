package net.quackiemackie.pathoscraft.event.server;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.network.payload.quest.active.SyncActiveQuests;
import net.quackiemackie.pathoscraft.network.payload.quest.completed.SyncCompletedQuests;
import net.quackiemackie.pathoscraft.util.quest.Quest;
import net.quackiemackie.pathoscraft.registers.PathosAttachments;
import net.quackiemackie.pathoscraft.util.handlers.abilities.AstralFormHandler;

import java.util.List;

@EventBusSubscriber(modid = PathosCraft.MOD_ID)
public class PlayerEvents {

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onPlayerTick(PlayerTickEvent.Post event){
        Player player = event.getEntity();

        if (!player.level().isClientSide() && player.getData(PathosAttachments.IN_ASTRAL_FORM.get())) {
            AstralFormHandler.checkDistanceAndSnapback(player, 20.0, 30.0);
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        player.getData(PathosAttachments.IN_ASTRAL_FORM.get());
        PathosCraft.LOGGER.info("Astral Form: {}", player.getData(PathosAttachments.IN_ASTRAL_FORM.get()));
        if (player.getData(PathosAttachments.IN_ASTRAL_FORM.get())) {
            AstralFormHandler.leaveAstralForm(player);
        }


        if (player instanceof ServerPlayer serverPlayer) {
            if (serverPlayer.hasData(PathosAttachments.ACTIVE_QUESTS.get())) {
                List<Quest> activeQuests = ((IAttachmentHolder) serverPlayer).getData(PathosAttachments.ACTIVE_QUESTS.get());
                PacketDistributor.sendToPlayer(serverPlayer, new SyncActiveQuests(activeQuests));
            } else {
                PathosCraft.LOGGER.error("Issue with active quests, it is empty.");
            }

            if (serverPlayer.hasData(PathosAttachments.COMPLETED_QUESTS.get())) {
                List<Quest> completedQuests = ((IAttachmentHolder) serverPlayer).getData(PathosAttachments.COMPLETED_QUESTS.get());
                PacketDistributor.sendToPlayer(serverPlayer, new SyncCompletedQuests(completedQuests));
            } else {
                PathosCraft.LOGGER.error("Issue with completed quests, it is empty.");
            }
        }
    }
}
