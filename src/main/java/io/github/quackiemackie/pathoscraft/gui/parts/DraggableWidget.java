package io.github.quackiemackie.pathoscraft.gui.parts;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.quackiemackie.pathoscraft.gui.parts.worker.WorkerMapRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class DraggableWidget {
    private int x, y, width, height;
    private boolean isDragging = false;
    private int dragOffsetX, dragOffsetY;

    private static final String displayText = "+";
    private static final int textColor = 0xFFFFFFFF;

    private final WorkerMapRenderer workerMapRenderer;

    /**
     * Creates a draggable widget within the specified draggable area, initializing its size, position,
     * and gradient color properties.
     * The widget is centered within the provided draggable area based on its width and height.
     *
     * @param draggableArea     The area within which the widget can be dragged.
     * @param width             The width of the draggable widget.
     * @param height            The height of the draggable widget.
     */
    public DraggableWidget(DraggableArea draggableArea, int width, int height) {
        this.width = width;
        this.height = height;
        this.x = draggableArea.getX() + (draggableArea.getWidth() - this.width) / 2;
        this.y = draggableArea.getY() + (draggableArea.getHeight() - this.height) / 2;

        Minecraft minecraft = Minecraft.getInstance();
        this.workerMapRenderer = new WorkerMapRenderer(
                minecraft.getTextureManager(),
                minecraft.getMapDecorationTextures()
        );

    }

    /**
     * Renders the draggable widget inside the bounds of a DraggableArea.
     *
     * @param guiGraphics   The graphics context used for rendering the widget.
     * @param font          The font used for rendering the text inside the widget.
     * @param draggableArea The draggable area defining the widget's clipping bounds.
     */
    public void render(GuiGraphics guiGraphics, Font font, DraggableArea draggableArea) {
        guiGraphics.pose().pushPose();

        guiGraphics.enableScissor(
                draggableArea.getX(),
                draggableArea.getY(),
                draggableArea.getX() + draggableArea.getWidth(),
                draggableArea.getY() + draggableArea.getHeight()
        );

        renderMapInWidget(guiGraphics, this.x, this.y, this.width, this.height, 2);

        int textWidth = font.width(displayText);
        guiGraphics.pose().translate(0, 0, 50);
        guiGraphics.drawString(font, displayText, x + (width - textWidth) / 2, y + (height / 2) - 4, textColor);

        guiGraphics.disableScissor();
        guiGraphics.pose().popPose();

    }

    public void renderMapInWidget(GuiGraphics guiGraphics, int widgetX, int widgetY, int widgetWidth, int widgetHeight, int mapId) {
        Minecraft minecraft = Minecraft.getInstance();
        MapItemSavedData mapState = minecraft.level.getMapData(new MapId(mapId));
        if (mapState == null) {
            return;
        }

        float scale = Math.min((float) widgetWidth / 128, (float) widgetHeight / 128);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(widgetX, widgetY, 25);
        guiGraphics.pose().scale(scale, scale, 1.0F);
        workerMapRenderer.render(guiGraphics.pose(), guiGraphics.bufferSource(), new MapId(mapId), mapState, false, 0xf000f0);
        guiGraphics.pose().popPose();
    }

    /**
     * Handles the mouse press event for the widget. Enables dragging mode if the left mouse button is pressed
     * while the mouse cursor is over the widget.
     *
     * @param mouseX The x-coordinate of the mouse cursor.
     * @param mouseY The y-coordinate of the mouse cursor.
     * @param button The mouse button being pressed, where 0 typically represents the left button.
     * @return true if the widget enters dragging mode, false otherwise.
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
     * Handles the drag event for the widget and keeps it within the boundaries of the draggable area.
     *
     * @param mouseX        The current x-coordinate of the mouse.
     * @param mouseY        The current y-coordinate of the mouse.
     * @param button        The mouse button being used, where 0 typically represents the left button.
     * @param draggableArea The area within which the widget can be dragged.
     * @return true if the widget's position was updated, false otherwise.
     */
    public boolean mouseDragged(double mouseX, double mouseY, int button, DraggableArea draggableArea) {
        if (isDragging && button == 0) {
            int newWidgetX = (int) (mouseX - dragOffsetX);
            int newWidgetY = (int) (mouseY - dragOffsetY);

            int draggableX = draggableArea.getX() + draggableArea.getBorderSize();
            int draggableY = draggableArea.getY() + draggableArea.getBorderSize();
            int draggableWidth = draggableArea.getWidth() - 2 * draggableArea.getBorderSize();
            int draggableHeight = draggableArea.getHeight() - 2 * draggableArea.getBorderSize();

            int minX, maxX, minY, maxY;

            if (width > draggableWidth) {
                // Widget is larger
                minX = draggableX - width + (draggableWidth - 10);
                maxX = draggableX + draggableWidth - (draggableWidth - 10);

                minY = draggableY - height + (draggableHeight - 10);
                maxY = draggableY + draggableHeight - (draggableHeight - 10);
            } else {
                // Widget is smaller
                minX = draggableX;
                maxX = draggableX + draggableWidth - width;

                minY = draggableY;
                maxY = draggableY + draggableHeight - height;
            }

            // Ensure the new position stays within the adjusted boundaries
            x = Math.max(minX, Math.min(newWidgetX, maxX));
            y = Math.max(minY, Math.min(newWidgetY, maxY));

            return true;
        }
        return false;
    }

    /**
     * Handles the mouse release event for the widget. Disables dragging mode when the left mouse button is released.
     *
     * @param button The mouse button being released, where 0 typically represents the left button.
     */
    public void mouseReleased(int button) {
        if (button == 0) {
            isDragging = false;
        }
    }

    /**
     * Determines if the mouse cursor is currently positioned over the widget.
     *
     * @param mouseX The x-coordinate of the mouse cursor.
     * @param mouseY The y-coordinate of the mouse cursor.
     * @return true if the mouse cursor is over the widget, false otherwise.
     */
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}