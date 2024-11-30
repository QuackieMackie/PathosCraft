package net.quackiemackie.pathoscraft.event;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.network.payload.QuestMenuActiveQuestsPayload;
import net.quackiemackie.pathoscraft.quest.Quest;
import net.quackiemackie.pathoscraft.quest.QuestObjective;
import net.quackiemackie.pathoscraft.registers.PathosAttachments;

import java.util.List;

@EventBusSubscriber(modid = PathosCraft.MOD_ID)
public class QuestEvents {

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            ResourceLocation targetEntityId = BuiltInRegistries.ENTITY_TYPE.getKey(event.getEntity().getType());

            PathosCraft.LOGGER.info("Progress updated: {}", targetEntityId);
            updateQuestProgress(player, targetEntityId.toString());
        }
    }

    private static void updateQuestProgress(Player player, String killedTarget) {
        List<Quest> activeQuests = ((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get());

        for (Quest quest : activeQuests) {
            for (QuestObjective objective : quest.getQuestObjectives()) {
                if ("kill".equals(objective.getAction()) && killedTarget.equals(objective.getTarget())) {
                    int currentProgress = objective.getProgress();
                    if (currentProgress < objective.getQuantity()) {
                        ServerPlayer serverPlayer = (ServerPlayer) player;

                        objective.setProgress(currentProgress + 1);

                        PathosCraft.LOGGER.info("Progress updated: {Quest ID: {}, Progression: {}}", quest.getQuestId(), objective.getProgress());
                        player.setData(PathosAttachments.ACTIVE_QUESTS.get(), activeQuests);
                        PacketDistributor.sendToPlayer(serverPlayer, new QuestMenuActiveQuestsPayload(activeQuests));
                    }
                }
            }
        }
    }
}
