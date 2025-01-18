package io.github.quackiemackie.pathoscraft.gui.parts.worker;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class DraggableWidget {
    private int x, y, width, height;
    private boolean isDragging = false;
    private int dragOffsetX, dragOffsetY;

    /**
     * Creates a new draggable widget with the specified initial position and size.
     *
     * @param startX the initial x-coordinate of the widget
     * @param startY the initial y-coordinate of the widget
     * @param width the width of the widget
     * @param height the height of the widget
     */
    public DraggableWidget(int startX, int startY, int width, int height) {
        this.x = startX;
        this.y = startY;
        this.width = width;
        this.height = height;
    }

    /**
     * Renders the draggable widget by drawing its background and centered text.
     *
     * @param guiGraphics the graphics context used for rendering the widget
     * @param font the font used for rendering the text inside the widget
     */
    public void render(GuiGraphics guiGraphics, Font font) {
        guiGraphics.fillGradient(x, y, x + width, y + height, 0xFFCCCCCC, 0xFF666666);
        int textWidth = font.width("Test");
        guiGraphics.drawString(font, "Test", x + (width - textWidth) / 2, y + (height / 2) - 4, 0xFFFFFF);
    }

    /**
     * Handles the mouse press event for the widget. If the left mouse button is pressed
     * while the mouse cursor is over the widget, this method enables dragging mode
     * and calculates the offset position for the drag operation.
     *
     * @param mouseX the x-coordinate of the mouse cursor
     * @param mouseY the y-coordinate of the mouse cursor
     * @param button the mouse button being pressed, where 0 typically represents the left button
     * @return true if the widget enters dragging mode, false otherwise
     */
    public boolean mousePressed(double mouseX, double mouseY, int button) {
        if (button == 0 && isMouseOver(mouseX, mouseY)) {
            isDragging = true;
            dragOffsetX = (int) (mouseX - x);
            dragOffsetY = (int) (mouseY - y);
            return true;
        }
        return false;
    }

    /**
     * Handles the dragging functionality of the widget within the specified draggable area.
     * Updates the widget position while ensuring it stays within the boundaries of the draggable area.
     *
     * @param mouseX the current x-coordinate of the mouse
     * @param mouseY the current y-coordinate of the mouse
     * @param button the mouse button being pressed, where 0 typically represents the left button
     * @param draggableArea the area in which the widget is allowed to be dragged
     * @return true if the dragging operation updates the widget's position, false otherwise
     */
    public boolean mouseDragged(double mouseX, double mouseY, int button, DraggableArea draggableArea) {
        if (isDragging && button == 0) {
            int newWidgetX = (int) (mouseX - dragOffsetX);
            int newWidgetY = (int) (mouseY - dragOffsetY);

            int draggableX = draggableArea.getX();
            int draggableY = draggableArea.getY();
            int draggableWidth = draggableArea.getWidth();
            int draggableHeight = draggableArea.getHeight();

            int minX, maxX, minY, maxY;

            if (width > draggableWidth) {
                // Adjust for widget larger than area, with 10-pixel padding
                minX = draggableX - width + (draggableWidth - 10);
                maxX = draggableX + draggableWidth - (draggableWidth - 10);

                minY = draggableY - height + (draggableHeight - 10);
                maxY = draggableY + draggableHeight - (draggableHeight - 10);
            } else {
                // The widget is smaller than or fits within the area
                minX = draggableX;
                maxX = draggableX + draggableWidth - width;

                minY = draggableY;
                maxY = draggableY + draggableHeight - height;
            }

            x = Math.max(minX, Math.min(newWidgetX, maxX));
            y = Math.max(minY, Math.min(newWidgetY, maxY));

            return true;
        }
        return false;
    }

    /**
     * Handles the mouse release event for the widget. Disables dragging mode if
     * the left mouse button (button 0) is released.
     *
     * @param button the mouse button being released, where 0 typically represents the left button
     * @return true if the left mouse button was released and dragging mode was disabled, false otherwise
     */
    public boolean mouseReleased(int button) {
        if (button == 0) {
            isDragging = false;
            return true;
        }
        return false;
    }

    /**
     * Determines if the mouse cursor is currently positioned over the widget.
     *
     * @param mouseX the x-coordinate of the mouse cursor
     * @param mouseY the y-coordinate of the mouse cursor
     * @return true if the mouse cursor is over the widget's boundaries, false otherwise
     */
    private boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

}
