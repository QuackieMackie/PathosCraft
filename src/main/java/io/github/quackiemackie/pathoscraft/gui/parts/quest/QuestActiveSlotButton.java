package io.github.quackiemackie.pathoscraft.gui.parts.quest;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.network.PacketDistributor;
import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.gui.screen.quest.QuestScreen;
import io.github.quackiemackie.pathoscraft.util.handlers.quest.QuestHandler;
import io.github.quackiemackie.pathoscraft.network.payload.quest.active.RemoveActiveQuest;
import io.github.quackiemackie.pathoscraft.network.payload.quest.active.SwapActiveQuests;
import io.github.quackiemackie.pathoscraft.network.payload.quest.completed.AddCompletedQuest;
import io.github.quackiemackie.pathoscraft.util.quest.Quest;
import io.github.quackiemackie.pathoscraft.registers.PathosAttachments;

import java.util.*;

public class QuestActiveSlotButton extends QuestSlotButton {

    private final ItemStack itemStack;
    private final List<Component> hoverInfo;
    private final Quest quest;

    private static Quest heldQuest = null;
    private static int heldQuestIndex = -1;

    /**
     * Constructs a new QuestActiveSlotButton.
     *
     * @param x         The x-coordinate of the button.
     * @param y         The y-coordinate of the button.
     * @param message   The text to display on the button.
     * @param itemStack The ItemStack to render on the button.
     * @param quest     The quest associated with this button.
     * @param onPress   The action to perform when the button is pressed.
     */
    public QuestActiveSlotButton(int x, int y, Component message, ItemStack itemStack, Quest quest, OnPress onPress) {
        super(x, y, message, itemStack, quest, onPress);
        this.itemStack = itemStack;
        this.hoverInfo = new ArrayList<>();
        this.quest = quest;
    }

    /**
     * Retrieves the quest associated with this button.
     *
     * @return The quest.
     */
    public Quest getQuest() {
        return quest;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Checks to see if the button is visible or mouse if hovering over it.
        if (!this.visible || !this.isMouseOver(mouseX, mouseY)) {
            return false;
        }

        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        List<Quest> activeQuests = new ArrayList<>(((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get()));
        List<Quest> completedQuests = new ArrayList<>(((IAttachmentHolder) player).getData(PathosAttachments.COMPLETED_QUESTS.get()));
        Quest quest = this.getQuest();

        return switch (button) {
            case 0 -> {
                clearHeldQuestState();
                handleLeftClick(completedQuests, activeQuests, player);
                refreshUIAfterQuestChange(activeQuests);
                yield true;
            }
            case 1 -> {
                clearHeldQuestState();
                handleRightClick(activeQuests, player);
                refreshUIAfterQuestChange(activeQuests);
                yield true;
            }
            case 2 -> {
                // Check to make sure it only works on the active tab
                if (minecraft.screen instanceof QuestScreen questScreen && questScreen.activeButton == questScreen.activeQuestsButton) {
                    handleMiddleClick(activeQuests, quest, activeQuests.indexOf(quest), player);
                    refreshUIAfterQuestChange(activeQuests);
                    yield true;
                }
                yield false;
            }
            default -> false;
        };
    }

    /**
     * Handles swapping of quests upon middle-click.
     *
     * @brief Initiates or completes a swap action when a quest in the active quest
     *        list is middle-clicked. If no quest is held, it selects the quest
     *        for swapping. If a quest is already held, it swaps the held quest
     *        with the clicked quest, updates the active quest list, refreshes
     *        the UI, and communicates changes to the server.
     */
    private void handleMiddleClick(List<Quest> activeQuests, Quest clickedQuest, int clickedQuestIndex, Player player) {
        if (heldQuest == null) {
            heldQuest = clickedQuest;
            heldQuestIndex = clickedQuestIndex;
        } else if (heldQuestIndex != clickedQuestIndex) {
            PathosCraft.LOGGER.info("Client: Requesting server to swap {Quest ID: {}, Index: {}} with {Quest ID: {}, Index: {}}", clickedQuest.getQuestId(), clickedQuestIndex, heldQuest.getQuestId(), heldQuestIndex);

            Collections.swap(activeQuests, heldQuestIndex, clickedQuestIndex);

            int tempSlot = heldQuest.getQuestActiveSlot();
            heldQuest.setQuestActiveSlot(clickedQuest.getQuestActiveSlot());
            clickedQuest.setQuestActiveSlot(tempSlot);

            player.setData(PathosAttachments.ACTIVE_QUESTS.get(), activeQuests);

            List<Quest> swappableQuests = List.of(heldQuest, clickedQuest);
            PacketDistributor.sendToServer(new SwapActiveQuests(swappableQuests));

            PathosCraft.LOGGER.info("Client {swappableQuests: [{}]}", swappableQuests);

            clearHeldQuestState();
        }
    }

    /**
     * Clears the state of the held quest.
     *
     * @brief This method resets the held quest properties, setting the held quest
     *        and its index to null and -1, respectively. It is typically called
     *        after a swap operation to ensure that no quest remains selected.
     */
    public static void clearHeldQuestState() {
        heldQuest = null;
        heldQuestIndex = -1;
    }

    /**
     * Handles removing a quest from the active list upon right-click.
     *
     * @brief When a quest is right-clicked, it is removed from the player's
     *        active quest list, and both the client and server are updated
     *        to reflect this change.
     *        The UI is refreshed to display the
     *        updated list of active quests.
     */
    private void handleRightClick(List<Quest> activeQuests, Player player) {
        Quest quest = this.getQuest();

        if (QuestHandler.isQuestCompleted(player, quest)) {
            return;
        }

        if (activeQuests.contains(quest)) {
            activeQuests.remove(quest);

            for (int i = 0; i < activeQuests.size(); i++) {
                activeQuests.get(i).setQuestActiveSlot(i);
            }

            player.setData(PathosAttachments.ACTIVE_QUESTS.get(), activeQuests);

            PacketDistributor.sendToServer(new RemoveActiveQuest(quest));
        }
    }

    /**
     * Handles accepting rewards from completed quests upon left-click.
     *
     * @brief This method processes the action of accepting rewards for a quest that
     *        has been completed. It updates the completed quests data attachment and
     *        sends the necessary payload to the server to execute the associated logic.
     */
    private void handleLeftClick(List<Quest> completedQuests, List<Quest> activeQuests,  Player player) {
        if (QuestHandler.isQuestObjectiveCompleted(quest) && !QuestHandler.isQuestCompleted(player, quest)) {
            completedQuests.add(quest);
            activeQuests.remove(quest);

            for (int i = 0; i < activeQuests.size(); i++) {
                activeQuests.get(i).setQuestActiveSlot(i);
            }

            player.setData(PathosAttachments.COMPLETED_QUESTS.get(), completedQuests);
            player.setData(PathosAttachments.ACTIVE_QUESTS.get(), activeQuests);

            PacketDistributor.sendToServer(new AddCompletedQuest(quest));
        }
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        Quest quest = this.getQuest();

        List<Quest> activeQuests = new ArrayList<>(player.getData(PathosAttachments.ACTIVE_QUESTS.get()));

        boolean isQuestActive = activeQuests.contains(quest);
        boolean isQuestObjectiveCompleted = QuestHandler.isQuestObjectiveCompleted(quest);
        boolean isQuestCompleted = QuestHandler.isQuestCompleted(player, quest);

        int itemX = this.getX() + (this.width / 2);
        int itemY = this.getY() + (this.height / 2);
        int backgroundColor;

        if (isQuestObjectiveCompleted) {
            backgroundColor = 0x5000FF00; // Green for objective completed
            renderBackground(guiGraphics, itemX, itemY, backgroundColor);
        } else if (isQuestActive) {
            backgroundColor = 0x50FFA500; // Orange for active
            renderBackground(guiGraphics, itemX, itemY, backgroundColor);
        } else if (isQuestCompleted) {
            backgroundColor = 0x90000000; // Grey for completed
            renderBackground(guiGraphics, itemX, itemY, backgroundColor);
        }

        renderItem(itemStack, itemX, itemY, guiGraphics);

        if (this.isHovered() && heldQuest == null) {
            if (!hoverInfo.isEmpty()) {
                guiGraphics.renderTooltip(Minecraft.getInstance().font, hoverInfo, ItemStack.EMPTY.getTooltipImage(), mouseX, mouseY);
            }
        }

        if (heldQuest != null && heldQuest == quest) {
            renderHeldItem(itemStack, mouseX, mouseY, guiGraphics);
        }
    }

    /**
     * Renders the specified ItemStack at the current mouse cursor position, ensuring it appears above
     * other UI components by adjusting its render layer.
     */
    private void renderHeldItem(ItemStack itemStack, int mouseX, int mouseY, GuiGraphics guiGraphics) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();

        poseStack.translate(mouseX - 8, mouseY - 8, 200.0F);
        guiGraphics.renderItem(itemStack, 0, 0);

        poseStack.popPose();
    }

    /**
     * Adds information to be displayed when hovering over the button.
     *
     * @param info The hover information to add.
     */
    public void addHoverInfo(Component info) {
        this.hoverInfo.add(info);
    }
}