package net.quackiemackie.pathoscraft.network;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.handlers.AstralFormHandler;
import net.quackiemackie.pathoscraft.handlers.QuestHandler;
import net.quackiemackie.pathoscraft.network.payload.AstralFormKeyPressPayload;
import net.quackiemackie.pathoscraft.network.payload.QuestMenuActiveQuestsPayload;
import net.quackiemackie.pathoscraft.network.payload.QuestMenuCompletedQuestsPayload;
import net.quackiemackie.pathoscraft.quest.Quest;
import net.quackiemackie.pathoscraft.registers.PathosAttachments;

import java.util.List;

public class ServerPayloadHandler {

    /**
     * A basic example of a client -> server payload.
     */
    public static void handleAstralFormKeyPress(AstralFormKeyPressPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
                    Player player = context.player();
                    if (((IAttachmentHolder) player).getData(PathosAttachments.IN_ASTRAL_FORM.get())) {
                        AstralFormHandler.leaveAstralForm(player);
                    }
                }).exceptionally(e -> {
                    context.disconnect(Component.translatable("networking.pathoscraft.server.failed", e.getMessage()));
                    return null;
                });
    }

    public static void handleQuestMenuActiveQuests(final QuestMenuActiveQuestsPayload data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            List<Quest> currentActiveQuests = ((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get());

            for (Quest quest : currentActiveQuests) {
                if (!data.quests().contains(quest)) {
                    QuestHandler.returnItems(player, quest);
                }
            }

            player.setData(PathosAttachments.ACTIVE_QUESTS.get(), data.quests());
            PathosCraft.LOGGER.info("Updated the server's active quests for player {}: {}", player.getName().getString(), data.quests());
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("networking.pathoscraft.server.failed", e.getMessage()));
            return null;
        });
    }

    public static void handleQuestMenuCompletedQuests(QuestMenuCompletedQuestsPayload data, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            List<Quest> previousCompletedQuests = ((IAttachmentHolder) player).getData(PathosAttachments.COMPLETED_QUESTS.get());

            List<Quest> newCompletedQuests = data.quests().stream()
                    .filter(quest -> !previousCompletedQuests.contains(quest))
                    .toList();

            for (Quest quest : newCompletedQuests) {
                QuestHandler.giveRewardsToPlayer(player, quest);
                PathosCraft.LOGGER.info("Given rewards for quest ID: {} to player {}", quest.getQuestId(), player.getName().getString());
            }

            player.setData(PathosAttachments.COMPLETED_QUESTS.get(), data.quests());
            PathosCraft.LOGGER.info("Updated the server's completed quests for player {}: {}", player.getName().getString(), data.quests());
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("networking.pathoscraft.client.failed", e.getMessage()));
            return null;
        });
    }
}
