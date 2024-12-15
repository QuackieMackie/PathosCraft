package net.quackiemackie.pathoscraft.gui.parts.miniGames;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class FishingMiniGameSequence extends AbstractButton {

    private boolean completed = false;
    private boolean failed = false;
    private long failEndTime = 0;

    public FishingMiniGameSequence(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    public void onPress() {
    }

    /**
     * Sets the completion status of the current instance. This status is typically used
     * to indicate that the associated button or sequence in the mini-game is successfully completed.
     *
     * @param completed true to mark the instance as completed, false otherwise
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Marks the current instance as failed and sets a duration for which the failure state will persist.
     *
     * @param durationMillis the duration, in milliseconds, that the failure state should last
     */
    public void setFailed(long durationMillis) {
        this.failed = true;
        this.failEndTime = System.currentTimeMillis() + durationMillis;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (failed && System.currentTimeMillis() > failEndTime) {
            failed = false;
        }

        guiGraphics.fill(getX(), getY(), getX() + this.width, getY() + this.height, 0xAA000000);

        int borderColor = 0xFF444444;
        guiGraphics.hLine(getX(), getX() + this.width - 1, getY(), borderColor);
        guiGraphics.hLine(getX(), getX() + this.width - 1, getY() + this.height - 1, borderColor);
        guiGraphics.vLine(getX(), getY(), getY() + this.height - 1, borderColor);
        guiGraphics.vLine(getX() + this.width - 1, getY(), getY() + this.height - 1, borderColor);

        int textColor;
        if (failed) {
            textColor = 0xFFFF0000;
        } else if (completed) {
            textColor = 0xFF00FF00;
        } else {
            textColor = 0xFFFFFFFF;
        }

        guiGraphics.drawCenteredString(Minecraft.getInstance().font, this.getMessage(), getX() + this.width / 2, getY() + (this.height - 8) / 2, textColor);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }
}