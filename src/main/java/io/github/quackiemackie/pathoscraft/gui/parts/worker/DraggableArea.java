package io.github.quackiemackie.pathoscraft.gui.parts.worker;

import net.minecraft.client.gui.GuiGraphics;

public class DraggableArea {
    private int x, y, width, height;

    /**
     * Creates a draggable area with dimensions and position calculated as a percentage
     * of the provided screen dimensions. The draggable area is centered within the screen.
     *
     * @param screenWidth the width of the screen
     * @param screenHeight the height of the screen
     */
    public DraggableArea(int screenWidth, int screenHeight) {
        this.width = (int) (screenWidth * 0.7);
        this.height = (int) (screenHeight * 0.7);
        this.x = (screenWidth - this.width) / 2;
        this.y = (screenHeight - this.height) / 2;
    }

    /**
     * Renders the draggable area by drawing its background and border.
     *
     * @param guiGraphics the graphics context used for rendering the draggable area
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
        guiGraphics.fill(x, y, x + width, y + height, 0x77000000);
    }

    /**
     * Draws a solid black rectangular border around the draggable area.
     *
     * @param guiGraphics the graphics context used for rendering the border
     */
    private void drawBorder(GuiGraphics guiGraphics) {
        // Top border
        guiGraphics.fill(x, y, x + width, y + 1, 0xFF000000);
        // Bottom border
        guiGraphics.fill(x, y + height - 1, x + width, y + height, 0xFF000000);
        // Left border
        guiGraphics.fill(x, y, x + 1, y + height, 0xFF000000);
        // Right border
        guiGraphics.fill(x + width - 1, y, x + width, y + height, 0xFF000000);
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

}
