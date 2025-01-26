package io.github.quackiemackie.pathoscraft.gui.parts.worker.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.quackiemackie.pathoscraft.util.worker.FilledMap;
import io.github.quackiemackie.pathoscraft.util.worker.WorkerStationMaps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class WorkerDraggableWidget {
    private int x, y, width, height;
    private boolean isDragging = false;
    private int dragOffsetX, dragOffsetY;
    private final int originalWidth;
    private final int originalHeight;
    private double zoom = 1.0;

    private final WorkerStationMaps mapData;

    private final WorkerMapRenderer workerMapRenderer;

    private static final String displayText = "+";
    private static final int textColor = 0xFFFFFFFF;

    /**
     * Creates a draggable widget within the specified draggable area, initializing its size, position,
     * and gradient color properties.
     * The widget is centered within the provided draggable area based on its width and height.
     *
     * @param draggableArea     The area within which the widget can be dragged.
     * @param width             The width of the draggable widget.
     * @param height            The height of the draggable widget.
     */
    public WorkerDraggableWidget(WorkerDraggableArea draggableArea, int width, int height, WorkerStationMaps mapData) {
        this.originalWidth = width;
        this.originalHeight = height;
        this.width = width;
        this.height = height;
        this.x = draggableArea.getX() + (draggableArea.getWidth() - this.width) / 2;
        this.y = draggableArea.getY() + (draggableArea.getHeight() - this.height) / 2;
        this.mapData = mapData;

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
    public void render(GuiGraphics guiGraphics, Font font, WorkerDraggableArea draggableArea) {
        renderBorder(guiGraphics);

        guiGraphics.pose().pushPose();

        guiGraphics.enableScissor(
                draggableArea.getX(),
                draggableArea.getY(),
                draggableArea.getX() + draggableArea.getWidth(),
                draggableArea.getY() + draggableArea.getHeight()
        );

        if (this.mapData != null) {
            renderMapInWidget(guiGraphics, this.x, this.y, this.width, this.height);
        }

        int textWidth = font.width(displayText);
        guiGraphics.pose().translate(0, 0, 50);
        guiGraphics.drawString(font, displayText, x + (width - textWidth) / 2, y + (height / 2) - 4, textColor);

        guiGraphics.disableScissor();
        guiGraphics.pose().popPose();
    }

    protected void renderBorder(GuiGraphics guiGraphics) {
        int borderColor = 0xFFFFFFFF;
        int left = this.getX();
        int top = this.getY();
        int right = this.getX() + this.width;
        int bottom = this.getY() + this.height;

        guiGraphics.fill(left, top, right, top + 1, borderColor);
        guiGraphics.fill(left, bottom - 1, right, bottom, borderColor);
        guiGraphics.fill(left, top, left + 1, bottom, borderColor);
        guiGraphics.fill(right - 1, top, right, bottom, borderColor);
    }

    /**
     * Renders a 3x3 grid of maps within the specified widget region.
     * Each map is scaled and positioned
     * inside the widget bounds using the provided dimensions and current zoom level.
     *
     * @param guiGraphics The graphics context used for rendering the widget and maps.
     * @param widgetX The x-coordinate of the top-left corner of the widget.
     * @param widgetY The y-coordinate of the top-left corner of the widget.
     * @param widgetWidth The width of the widget.
     * @param widgetHeight The height of the widget.
     */
    public void renderMapInWidget(GuiGraphics guiGraphics, int widgetX, int widgetY, int widgetWidth, int widgetHeight) {
        Minecraft minecraft = Minecraft.getInstance();

        int[][] mapIds = {
                {getMapIdForSlot(0), getMapIdForSlot(1), getMapIdForSlot(2)},
                {getMapIdForSlot(3), getMapIdForSlot(4), getMapIdForSlot(5)},
                {getMapIdForSlot(6), getMapIdForSlot(7), getMapIdForSlot(8)}
        };

        int rows = 3;
        int cols = 3;
        double cellWidth = (widgetWidth / (cols * zoom));
        double cellHeight = (widgetHeight / (rows * zoom));

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int mapId = mapIds[row][col];

                double cellX = (widgetX + col * cellWidth * zoom);
                double cellY = (widgetY + row * cellHeight * zoom);

                MapItemSavedData mapState = minecraft.level.getMapData(new MapId(mapId));
                if (mapState == null) {
                    continue;
                }

                float scale = (float) (Math.min(cellWidth / 128, cellHeight / 128) * zoom);

                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(cellX, cellY, 25);
                guiGraphics.pose().scale(scale, scale, 1.0F);
                workerMapRenderer.render(guiGraphics.pose(), guiGraphics.bufferSource(), new MapId(mapId), mapState, false, 0xf000f0);
                guiGraphics.pose().popPose();
            }
        }
    }

    /**
     * Retrieves the map ID associated with a given slot from the WorkerStationMaps.
     *
     * @param slot The slot number for which the map ID should be retrieved.
     * @return The map ID if a matching slot is found, or -1 if no match exists.
     */
    public int getMapIdForSlot(int slot) {
        return mapData.maps().stream()
                .filter(filledMap -> filledMap.slot() == slot)
                .map(FilledMap::mapId)
                .findFirst()
                .orElse(-1);
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
    public boolean mouseDragged(double mouseX, double mouseY, int button, WorkerDraggableArea draggableArea) {
        if (isDragging && button == 0) {
            int newWidgetX = (int) (mouseX - dragOffsetX);
            int newWidgetY = (int) (mouseY - dragOffsetY);

            int draggableX = draggableArea.getX() + draggableArea.getBorderSize();
            int draggableY = draggableArea.getY() + draggableArea.getBorderSize();
            int draggableWidth = draggableArea.getWidth() - 2 * draggableArea.getBorderSize();
            int draggableHeight = draggableArea.getHeight() - 2 * draggableArea.getBorderSize();

            int minX, maxX, minY, maxY;

            if (width > draggableWidth) {
                minX = draggableX - width + (draggableWidth);
                maxX = draggableX + draggableWidth - (draggableWidth);
            } else {
                minX = draggableX;
                maxX = draggableX + draggableWidth - width;
            }

            if (height > draggableHeight) {
                minY = draggableY - height + (draggableHeight);
                maxY = draggableY + draggableHeight - (draggableHeight);
            } else {
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
     * Handles mouse scroll events to adjust the zoom level of the widget and updates its dimensions.
     * This method ensures the widget's zoom level remains within a specific range, recalculates
     * its width and height based on the zoom level, and constrains its position within the
     * boundaries of the specified draggable area.
     *
     * @param scrollY        The amount of vertical scrolling, where positive values signify upward
     *                       scrolling and negative values signify downward scrolling.
     * @param draggableArea  The area within which the widget is constrained, used to adjust
     *                       the widget's position after resizing.
     * @return true Always returns true as scrolling is a continuous event.
     */
    public boolean mouseScrolled(double scrollY, WorkerDraggableArea draggableArea) {
        double zoomAmount = 0.1;
        zoom += scrollY * zoomAmount;

        zoom = Math.max(0.2, Math.min(zoom, 3.0));

        width = (int) (this.originalWidth * zoom);
        height = (int) (this.originalHeight * zoom);

        constrainWithinBounds(draggableArea);

        return true;
    }

    /**
     * Resets the zoom level of the widget to its default value, restoring its original dimensions,
     * and ensures that the widget remains constrained within the specified draggable area boundaries.
     *
     * @param draggableArea The area within which the widget is constrained and adjusted after resetting its zoom.
     */
    public void resetZoom(WorkerDraggableArea draggableArea) {
        zoom = 1.0;

        width = originalWidth;
        height = originalHeight;

        constrainWithinBounds(draggableArea);
    }

    /**
     * Ensures that the widget's position remains within the bounds of the given draggable area.
     * This method adjusts the widget's x and y coordinates as necessary to prevent it from
     * overlapping or exiting the draggable area's limits while considering its size.
     *
     * @param draggableArea The area within which the widget can be dragged, including its borders.
     */
    private void constrainWithinBounds(WorkerDraggableArea draggableArea) {
        int draggableX = draggableArea.getX() + draggableArea.getBorderSize();
        int draggableY = draggableArea.getY() + draggableArea.getBorderSize();
        int draggableWidth = draggableArea.getWidth() - 2 * draggableArea.getBorderSize();
        int draggableHeight = draggableArea.getHeight() - 2 * draggableArea.getBorderSize();

        if (width > draggableWidth) {
            x = Math.max(draggableX - width + draggableWidth, Math.min(x, draggableX));
        } else {
            x = Math.max(draggableX, Math.min(x, draggableX + draggableWidth - width));
        }

        if (height > draggableHeight) {
            y = Math.max(draggableY - height + draggableHeight, Math.min(y, draggableY));
        } else {
            y = Math.max(draggableY, Math.min(y, draggableY + draggableHeight - height));
        }
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