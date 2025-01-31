package io.github.quackiemackie.pathoscraft.gui.screen.parts.worker;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import java.util.Map;

public class WorkerDraggableWidget {
    private int x, y, width, height;
    private boolean isDragging = false;
    private int dragOffsetX, dragOffsetY;
    private final int originalWidth;
    private final int originalHeight;

    private final static float DEFAULT_ZOOM = 1.0F;
    private float currentScale = 1.0F;
    private float targetScale = 1.0F;
    private final static float ZOOM_SPEED = 0.1F;

    private final Map<Integer, Integer> slotMapData;
    private final WorkerMapRenderer workerMapRenderer;

    public WorkerDraggableWidget(WorkerDraggableArea draggableArea, int width, int height, Map<Integer, Integer> slotMapData) {
        this.originalWidth = width;
        this.originalHeight = height;
        this.width = width;
        this.height = height;
        this.x = draggableArea.getX() + (draggableArea.getWidth() - this.width) / 2;
        this.y = draggableArea.getY() + (draggableArea.getHeight() - this.height) / 2;
        this.slotMapData = slotMapData;

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
        /* For testing purposes. */
        //renderBorder(guiGraphics);

        updateZoom(draggableArea);

        guiGraphics.pose().pushPose();

        guiGraphics.enableScissor(
                draggableArea.getX(),
                draggableArea.getY(),
                draggableArea.getX() + draggableArea.getWidth(),
                draggableArea.getY() + draggableArea.getHeight()
        );

        if (this.slotMapData != null) {
            renderMapInWidget(guiGraphics, this.x, this.y, this.width, this.height);
        }

        int textWidth = font.width("+");
        guiGraphics.pose().translate(0, 0, 50);
        guiGraphics.drawString(font, "+", x + (width - textWidth) / 2, y + (height / 2) - 4, 0xFFFFFFFF);

        guiGraphics.disableScissor();
        guiGraphics.pose().popPose();
    }

    /**
     * Renders a 3x3 grid of maps within the specified widget region.
     * Each map is scaled and positioned
     * inside the widget bounds using the provided dimensions and current currentScale level.
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
        double cellWidth = (widgetWidth / (cols * currentScale));
        double cellHeight = (widgetHeight / (rows * currentScale));

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int mapId = mapIds[row][col];

                double cellX = (widgetX + col * cellWidth * currentScale);
                double cellY = (widgetY + row * cellHeight * currentScale);

                MapItemSavedData mapState = minecraft.level.getMapData(new MapId(mapId));
                if (mapState == null) {
                    continue;
                }

                float scale = (float) (Math.min(cellWidth / 128, cellHeight / 128) * currentScale);

                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(cellX, cellY, 25);
                guiGraphics.pose().scale(scale, scale, 1.0F);
                workerMapRenderer.render(guiGraphics.pose(), guiGraphics.bufferSource(), new MapId(mapId), mapState, false, 0xf000f0);
                guiGraphics.pose().popPose();
            }
        }
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
     * Adjusts the widget's position to ensure it remains within the boundaries of the specified draggable area.
     * If the widget's dimensions exceed the draggable area's bounds, it is shifted accordingly to maintain visibility.
     *
     * @param draggableArea The draggable area used to constrain the widget's position and dimensions.
     */
    private void constrainWithinBounds(WorkerDraggableArea draggableArea) {
        int draggableX = draggableArea.getX();
        int draggableY = draggableArea.getY();
        int draggableWidth = draggableArea.getWidth();
        int draggableHeight = draggableArea.getHeight();

        if (width > draggableWidth) {
            int targetX = Math.max(draggableX - width + draggableWidth, Math.min(x, draggableX));
            x = (int) lerp(x, targetX, ZOOM_SPEED);
        } else {
            int targetX = Math.max(draggableX, Math.min(x, draggableX + draggableWidth - width));
            x = (int) lerp(x, targetX, ZOOM_SPEED);
        }

        if (height > draggableHeight) {
            int targetY = Math.max(draggableY - height + draggableHeight, Math.min(y, draggableY));
            y = (int) lerp(y, targetY, ZOOM_SPEED);
        } else {
            int targetY = Math.max(draggableY, Math.min(y, draggableY + draggableHeight - height));
            y = (int) lerp(y, targetY, ZOOM_SPEED);
        }
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

            x = newWidgetX;
            y = newWidgetY;

            constrainWithinBounds(draggableArea);

            return true;
        }
        return false;
    }

    /**
     * Handles mouse scroll events for zooming in or out within a draggable area.
     * Adjusts the current zoom level of the widget, ensuring it stays within the
     * defined minimum and maximum zoom limits, and updates the widget's display accordingly.
     *
     * @param scrollY The amount scrolled by the mouse.
     *                Positive values typically indicate scrolling up, and negative values typically
     *                indicate scrolling down.
     * @param draggableArea The draggable area that provides bounds for the widget's zoom
     *                      adjustments and ensures the widget remains constrained within its limits.
     * @return true if the zoom level is successfully updated, false otherwise.
     */
    public boolean mouseScrolled(double scrollY, WorkerDraggableArea draggableArea) {
        targetScale = (float) Math.max(0.2F, Math.min(targetScale + (scrollY * ZOOM_SPEED), 3.0F));

        updateZoom(draggableArea);
        constrainWithinBounds(draggableArea);

        return true;
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

    /**
     * Resets the zoom level of the widget to its default value and reinitializes the widget's dimensions
     * to their original sizes.
     * This method ensures that the widget remains constrained within the bounds
     * of the specified draggable area after the reset.
     *
     * @param draggableArea The draggable area within which the widget is constrained and adjusted
     *                      after resetting the zoom level.
     */
    public void resetZoom(WorkerDraggableArea draggableArea) {
        currentScale = DEFAULT_ZOOM;

        width = originalWidth;
        height = originalHeight;

        constrainWithinBounds(draggableArea);
    }

    /**
     * Updates the zoom level of the widget by interpolating between the current
     * scale and the target scale.
     * Adjusts the widget's dimensions and re-centers
     * it to maintain its position.
     * Ensures the widget's boundaries are constrained
     * within the specified draggable area.
     *
     * @param draggableArea The area within which the widget's position is constrained
     *                      after updating its zoom level.
     */
    public void updateZoom(WorkerDraggableArea draggableArea) {
        int oldWidth = this.width;
        int oldHeight = this.height;
        int centerX = this.x + oldWidth / 2;
        int centerY = this.y + oldHeight / 2;

        currentScale = lerp(currentScale, targetScale, ZOOM_SPEED);

        this.width = (int) (this.originalWidth * currentScale);
        this.height = (int) (this.originalHeight * currentScale);

        this.x = centerX - this.width / 2;
        this.y = centerY - this.height / 2;

        constrainWithinBounds(draggableArea);
    }

    /**
     * Linearly interpolates between two values by the specified factor.
     * The interpolation factor determines the weight of the second value
     * relative to the first value in the resulting mixed value.
     *
     * @param start The starting value of the interpolation.
     * @param end The ending value of the interpolation.
     * @param factoredSpeed The interpolation factor ranging from 0.0 to 1.0,
     *                      where 0.0 corresponds to the starting value and 1.0
     *                      corresponds to the ending value.
     *                      Values outside this range will extrapolate beyond the specified range.
     * @return The interpolated value between the start and end values based on
     *         the given interpolation factor.
     */
    private static float lerp(float start, float end, float factoredSpeed) {
        return start + (end - start) * factoredSpeed;
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

    /**
     * Retrieves the map ID associated with the specified slot.
     * If the slot does not have an associated map ID, it returns -1 by default.
     *
     * @param slot The slot index whose associated map ID is to be retrieved.
     * @return The map ID associated with the slot, or -1 if no map ID is found.
     */
    public int getMapIdForSlot(int slot) {
        return slotMapData.getOrDefault(slot, -1);
    }
}