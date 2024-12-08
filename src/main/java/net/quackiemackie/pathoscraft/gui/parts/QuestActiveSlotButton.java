package net.quackiemackie.pathoscraft.gui.parts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.network.PacketDistributor;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.gui.screen.QuestScreen;
import net.quackiemackie.pathoscraft.handlers.QuestHandler;
import net.quackiemackie.pathoscraft.network.payload.QuestMenuActiveQuestsPayload;
import net.quackiemackie.pathoscraft.quest.Quest;
import net.quackiemackie.pathoscraft.registers.PathosAttachments;

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

        // Check to make sure it only works on the active tab
        if (!(Minecraft.getInstance().screen instanceof QuestScreen questScreen) ||
                questScreen.activeButton != questScreen.activeQuestsButton) {
            return false;
        }

        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        List<Quest> activeQuests = new ArrayList<>(((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get()));
        Quest quest = this.getQuest();

        PathosCraft.LOGGER.info("Clicked Quest ID: {}", quest.getQuestId());
        return switch (button) {
            case 0 -> {
                handleLeftClick();
                yield true;
            }
            case 1 -> {
                handleRightClick(activeQuests, player, minecraft);
                yield true;
            }
            case 2 -> {
                handleMiddleClick(activeQuests, player, minecraft, quest, activeQuests.indexOf(quest));
                yield true;
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
    private void handleMiddleClick(List<Quest> activeQuests, Player player, Minecraft minecraft, Quest clickedQuest, int clickedQuestIndex) {
        if (heldQuest == null) {
            heldQuest = clickedQuest;
            heldQuestIndex = clickedQuestIndex;
        } else if (heldQuestIndex != clickedQuestIndex) {
            PathosCraft.LOGGER.info("Swapping {Quest ID: {}, Index: {}} with {Quest ID: {}, Index: {}}", clickedQuest.getQuestId(), clickedQuestIndex, heldQuest.getQuestId(), heldQuestIndex);

            Collections.swap(activeQuests, heldQuestIndex, clickedQuestIndex);
            player.setData(PathosAttachments.ACTIVE_QUESTS.get(), activeQuests);
            PacketDistributor.sendToServer(new QuestMenuActiveQuestsPayload(activeQuests));

            if (minecraft.screen instanceof QuestScreen questScreen) {
                questScreen.refreshActiveQuestsUI(activeQuests);
            }
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
    private void handleRightClick(List<Quest> activeQuests, Player player, Minecraft minecraft) {
        if (activeQuests.contains(quest)) {
            activeQuests.remove(quest);
            player.setData(PathosAttachments.ACTIVE_QUESTS.get(), activeQuests);
        }

        PacketDistributor.sendToServer(new QuestMenuActiveQuestsPayload(activeQuests));

        if (minecraft.screen instanceof QuestScreen questScreen) {
            questScreen.refreshActiveQuestsUI(activeQuests);
        }
    }

    /**
     * Handles accepting rewards from completed quests upon left-click.
     *
     * @brief This method processes the action of accepting rewards for quests that
     *        have been completed. It updates the completed quests data attachment and
     *        sends the necessary payload to the server to execute the associated logic.
     */
    private void handleLeftClick() {
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        Quest quest = this.getQuest();
        boolean isQuestCompleted = QuestHandler.isQuestCompleted(quest);

        int itemX = this.getX() + (this.width / 2);
        int itemY = this.getY() + (this.height / 2);
        int glowColorBackground;
        int glowColorBorder;

        if (isQuestCompleted) {
            glowColorBackground = 0x4000FF00; // Green for completed
            glowColorBorder = 0x8000FF00;
            renderGlow(guiGraphics, itemX, itemY, glowColorBackground, glowColorBorder);
        }
        renderItem(itemStack, itemX, itemY, guiGraphics);

        if (this.isHovered()) {
            if (!hoverInfo.isEmpty()) {
                guiGraphics.renderTooltip(Minecraft.getInstance().font, hoverInfo, ItemStack.EMPTY.getTooltipImage(), mouseX, mouseY);
            }
        }
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