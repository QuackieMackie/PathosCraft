package net.quackiemackie.pathoscraft.gui.parts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class QuestTabButton extends AbstractButton {

    private boolean active;
    private final int questType;
    private final OnPress onPress;

    public interface OnPress {
        void onPress(QuestTabButton button);
    }

    public QuestTabButton(int x, int y, int width, int height, Component message, boolean initialState, int questType, OnPress onPress) {
        super(x, y, width, height, message);
        this.active = initialState;
        this.questType = questType;
        this.onPress = onPress;
    }

    public int getQuestType() {
        return questType;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void onPress() {
        if (this.onPress != null) {
            this.onPress.onPress(this);
        }
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int buttonColor = this.active ? 0xFF404040 : 0xC6C6C6C6;
        int cornerRadius = 10;

        drawRoundedRect(graphics, buttonColor, cornerRadius);
        drawRightAlignedText(graphics, mouseX, mouseY);
    }

    private void drawRoundedRect(GuiGraphics graphics, int color, int cornerRadius) {
        graphics.fill(this.getX() + cornerRadius, this.getY(), this.getX() + this.width, this.getY() + this.height, color);
        for (int i = 0; i < cornerRadius; i++) {
            graphics.fill(this.getX() + i, this.getY() + i, this.getX() + cornerRadius - i, this.getY() + i + 1, color);
        }
        for (int i = 0; i < cornerRadius; i++) {
            graphics.fill(this.getX() + i, this.getY() + this.height - i - 1, this.getX() + cornerRadius - i, this.getY() + this.height - i, color);
        }
        graphics.fill(this.getX(), this.getY() + cornerRadius, this.getX() + cornerRadius, this.getY() + this.height - cornerRadius, color);
    }

    private void drawRightAlignedText(GuiGraphics graphics, int mouseX, int mouseY) {
        int textColor = this.isHovered() ? 0xFFFFFFA0 : 0xFFFFFFFF;
        int textWidth = Minecraft.getInstance().font.width(this.getMessage().getString());
        int xPos = this.getX() + this.width - textWidth - 5;
        graphics.drawString(Minecraft.getInstance().font, this.getMessage(), xPos, this.getY() + 5, textColor);
    }
}