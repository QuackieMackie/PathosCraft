package net.quackiemackie.pathoscraft.network;

import net.minecraft.ResourceLocationException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.handlers.AstralFormHandler;
import net.quackiemackie.pathoscraft.network.payload.AstralFormKeyPressPayload;
import net.quackiemackie.pathoscraft.network.payload.QuestMenuActiveQuestsPayload;
import net.quackiemackie.pathoscraft.quest.Quest;
import net.quackiemackie.pathoscraft.quest.QuestObjective;
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
                    returnItemsForHalfCompletedObjectives(player, quest);
                }
            }

            player.setData(PathosAttachments.ACTIVE_QUESTS.get(), data.quests());
            PathosCraft.LOGGER.info("Updated the server's active quests for player {}: {}", player.getName().getString(), data.quests());
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("networking.pathoscraft.server.failed", e.getMessage()));
            return null;
        });
    }

    /**
     * Returns items to the player if the quest has half-completed collect objectives.
     *
     * @param player the player to receive the items
     * @param quest the quest being removed
     */
    private static void returnItemsForHalfCompletedObjectives(Player player, Quest quest) {
        for (QuestObjective objective : quest.getQuestObjectives()) {
            if ("collect".equals(objective.getAction())) {
                int itemsToReturn = objective.getProgress();
                if (itemsToReturn > 0) {
                    giveItemsToPlayer(player, objective.getTarget(), itemsToReturn);
                }
            }
        }
    }

    /**
     * Gives the specified quantity of the item to the player.
     *
     * @param player the player receiving the items
     * @param itemName the name of the item
     * @param quantity the number of items to be given
     */
    private static void giveItemsToPlayer(Player player, String itemName, int quantity) {
        try {
            ResourceLocation itemRegistryName = ResourceLocation.parse(itemName);
            Item item = BuiltInRegistries.ITEM.get(itemRegistryName);

            ItemStack itemStack = new ItemStack(item, quantity);
            player.getInventory().add(itemStack);
            PathosCraft.LOGGER.info("Returned {} of item: {} to the player.", quantity, itemName);
        } catch (ResourceLocationException e) {
            PathosCraft.LOGGER.error("Failed to parse item name {}: {}", itemName, e.getMessage());
        }
    }
}
