package net.quackiemackie.pathoscraft.gui.parts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.network.PacketDistributor;
import net.quackiemackie.pathoscraft.gui.screen.QuestScreen;
import net.quackiemackie.pathoscraft.handlers.QuestHandler;
import net.quackiemackie.pathoscraft.network.payload.QuestMenuActiveQuestsPayload;
import net.quackiemackie.pathoscraft.quest.Quest;
import net.quackiemackie.pathoscraft.registers.PathosAttachments;

import java.util.ArrayList;
import java.util.List;

public class QuestActiveSlotButton extends QuestSlotButton {

    private final ItemStack itemStack;
    private final List<Component> hoverInfo;
    private final Quest quest;

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
    public void onPress() {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        List<Quest> activeQuests = new ArrayList<>(((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get()));
        Quest quest = this.getQuest();

        if (activeQuests.contains(quest)) {
            activeQuests.remove(quest);
            player.setData(PathosAttachments.ACTIVE_QUESTS.get(), activeQuests);
            itemStack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        }

        PacketDistributor.sendToServer(new QuestMenuActiveQuestsPayload(activeQuests));

        if (minecraft.screen instanceof QuestScreen questScreen) {
            questScreen.removeActiveQuestButtons();
            questScreen.addActiveQuestButton(activeQuests);
        }
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