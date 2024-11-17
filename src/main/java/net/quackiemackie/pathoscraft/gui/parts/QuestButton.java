package net.quackiemackie.pathoscraft.gui.parts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class QuestButton extends AbstractButton {

    private boolean active;

    public QuestButton(int x, int y, int width, int height, Component message, boolean initialState) {
        super(x, y, width, height, message);
        this.active = initialState;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        // Method placeholder for narration updates
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void onPress() {
        // This will be handled by QuestScreen
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int buttonColor = this.active ? 0xFF404040 : 0xC6C6C6C6; // Color based on active state
        int cornerRadius = 10; // Adjust corner radius as needed

        drawRoundedRect(graphics, buttonColor, cornerRadius);
        drawRightAlignedText(graphics, mouseX, mouseY);
    }

    private void drawRoundedRect(GuiGraphics graphics, int color, int cornerRadius) {
        // Main rectangle
        graphics.fill(this.getX() + cornerRadius, this.getY(), this.getX() + this.width, this.getY() + this.height, color);
        // Top-left arc (approx. a quarter circle)
        for (int i = 0; i < cornerRadius; i++) {
            graphics.fill(this.getX() + i, this.getY() + i, this.getX() + cornerRadius - i, this.getY() + i + 1, color);
        }
        // Bottom-left arc (approx. a quarter circle)
        for (int i = 0; i < cornerRadius; i++) {
            graphics.fill(this.getX() + i, this.getY() + this.height - i - 1, this.getX() + cornerRadius - i, this.getY() + this.height - i, color);
        }
        // Vertical bar connecting top-left and bottom-left arcs
        graphics.fill(this.getX(), this.getY() + cornerRadius, this.getX() + cornerRadius, this.getY() + this.height - cornerRadius, color);
    }

    private void drawRightAlignedText(GuiGraphics graphics, int mouseX, int mouseY) {
        int textColor = this.isHovered() ? 0xFFFFFFA0 : 0xFFFFFFFF; // Highlight on hover
        int textWidth = Minecraft.getInstance().font.width(this.getMessage().getString());
        int xPos = this.getX() + this.width - textWidth - 5; // Right-align with padding of 5 pixels
        graphics.drawString(Minecraft.getInstance().font, this.getMessage(), xPos, this.getY() + 5, textColor);
    }
}