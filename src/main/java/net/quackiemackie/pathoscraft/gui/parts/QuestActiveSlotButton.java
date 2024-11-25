package net.quackiemackie.pathoscraft.gui.parts;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class QuestActiveSlotButton extends QuestSlotButton {

    private final ItemStack itemStack;
    private final List<Component> hoverInfo;
    private final int questId;

    /**
     * Constructs a new QuestSlotButton.
     *
     * @param x         The x-coordinate of the button.
     * @param y         The y-coordinate of the button.
     * @param message   The text to display on the button.
     * @param itemStack The ItemStack to render on the button.
     * @param questId   The ID of the quest associated with this button.
     * @param onPress   The action to perform when the button is pressed.
     */
    public QuestActiveSlotButton(int x, int y, Component message, ItemStack itemStack, int questId, OnPress onPress) {
        super(x, y, message, itemStack, questId, onPress);
        this.itemStack = itemStack;
        this.hoverInfo = new ArrayList<>();
        this.questId = questId;
    }

    /**
     * Gets the quest ID associated with this button.
     *
     * @return The quest ID.
     */
    public int getQuestId() {
        return questId;
    }

    //TODO:
    // Adding logic for reloading the active quests buttons, and removing from active quests if it's clicked in this screens tab.
    @Override
    public void onPress() {
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        int itemX = this.getX() + (this.width / 2);
        int itemY = this.getY() + (this.height / 2);

        renderItem(itemStack, itemX, itemY, guiGraphics);
        //renderBorder(guiGraphics);

        if (this.isHovered()) {
            if (!hoverInfo.isEmpty()) {
                guiGraphics.renderTooltip(Minecraft.getInstance().font, hoverInfo, ItemStack.EMPTY.getTooltipImage(), mouseX, mouseY);
            }
        }
    }

    public void addHoverInfo(Component info) {
        this.hoverInfo.add(info);
    }

    protected void renderBorder(GuiGraphics guiGraphics) {
        super.renderBorder(guiGraphics);
    }

    /**
     * Renders the ItemStack on the button.
     *
     * @param itemStack   The ItemStack to render.
     * @param x           The x-coordinate where the item should be rendered.
     * @param y           The y-coordinate where the item should be rendered.
     * @param guiGraphics The graphics context used for rendering.
     */
    protected void renderItem(ItemStack itemStack, int x, int y, GuiGraphics guiGraphics) {
        super.renderItem(itemStack, x, y, guiGraphics);
    }

    /**
     * Renders a BlockItem on the button.
     *
     * @param itemStack   The BlockItem stack to render.
     * @param poseStack   The pose stack for transformations.
     * @param guiGraphics The graphics context used for rendering.
     */
    protected void renderBlockItem(ItemStack itemStack, PoseStack poseStack, GuiGraphics guiGraphics) {
        super.renderBlockItem(itemStack, poseStack, guiGraphics);
    }
}
