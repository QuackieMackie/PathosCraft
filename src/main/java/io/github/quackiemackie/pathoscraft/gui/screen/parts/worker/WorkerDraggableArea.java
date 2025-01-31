package io.github.quackiemackie.pathoscraft.gui.screen.parts.worker;

import net.minecraft.client.gui.GuiGraphics;

public class WorkerDraggableArea {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int borderSize;
    private final int borderColor;
    private final int backgroundColor;

    /**
     * Creates a draggable area based on four coordinates:
     * - (startX, startY): the top-left corner of the draggable area.
     * - (maxX, maxY): the bottom-right corner of the draggable area.
     *
     * @param startX the x-coordinate of the top-left corner
     * @param startY the y-coordinate of the top-left corner
     * @param maxX   the x-coordinate of the bottom-right corner
     * @param maxY   the y-coordinate of the bottom-right corner
     */
    public WorkerDraggableArea(int startX, int startY, int maxX, int maxY, int borderSize, int borderColor, int backgroundColor) {
        this.x = startX;
        this.y = startY;
        this.width = Math.max(0, maxX - startX);
        this.height = Math.max(0, maxY - startY);
        this.borderSize = Math.max(0, borderSize);
        this.borderColor = borderColor;
        this.backgroundColor = backgroundColor;
    }

    /**
     * Renders the draggable area by drawing its background and border.
     *
     * @param guiGraphics the graphics context used for rendering
     */
    public void render(GuiGraphics guiGraphics) {
        drawBackground(guiGraphics);
        drawBorder(guiGraphics);
    }

    /**
     * Draws the semi-transparent background of the draggable area.
     *
     * @param guiGraphics the graphics context used for rendering the background
     */
    private void drawBackground(GuiGraphics guiGraphics) {
        guiGraphics.fill(getInnerX(), getInnerY(), getInnerX() + getInnerWidth(), getInnerY() + getInnerHeight(), backgroundColor);
    }

    /**
     * Draws a solid rectangular border around the draggable area with a thickness of `borderSize`.
     *
     * @param guiGraphics the graphics context used for rendering the border
     */
    private void drawBorder(GuiGraphics guiGraphics) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 100);

        // Top border
        guiGraphics.fill(x, y, x + width, y + borderSize, borderColor);
        // Bottom border
        guiGraphics.fill(x, y + height - borderSize, x + width, y + height, borderColor);
        // Left border
        guiGraphics.fill(x, y, x + borderSize, y + height, borderColor);
        // Right border
        guiGraphics.fill(x + width - borderSize, y, x + width, y + height, borderColor);

        guiGraphics.pose().popPose();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private int getInnerX() {
        return x + borderSize;
    }

    private int getInnerY() {
        return y + borderSize;
    }

    private int getInnerWidth() {
        return width - 2 * borderSize;
    }

    private int getInnerHeight() {
        return height - 2 * borderSize;
    }
}