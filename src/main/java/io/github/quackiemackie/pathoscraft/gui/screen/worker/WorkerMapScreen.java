package io.github.quackiemackie.pathoscraft.gui.screen.worker;

import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.gui.parts.worker.screen.WorkerDraggableArea;
import io.github.quackiemackie.pathoscraft.gui.parts.worker.screen.WorkerDraggableWidget;
import io.github.quackiemackie.pathoscraft.util.handlers.workers.WorkerPayloadHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class WorkerMapScreen extends Screen {
    private WorkerDraggableArea draggableArea;
    private WorkerDraggableWidget draggableWidget;

    private final WorkerMainScreen parentScreen;

    public WorkerMapScreen(WorkerMainScreen parentScreen) {
        super(Component.literal("Worker Map"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        super.init();
        int screenWidth = this.width;
        int screenHeight = this.height;

        int areaWidth = (int) (screenWidth * 0.7);
        int areaHeight = (int) (screenHeight * 0.7);
        int startX = (screenWidth - areaWidth) / 2;
        int startY = (screenHeight - areaHeight) / 2;
        int maxX = startX + areaWidth;
        int maxY = startY + areaHeight;

        draggableArea = new WorkerDraggableArea(startX, startY, maxX, maxY, 5, 0xFF000000, 0x77000000);
        draggableWidget = new WorkerDraggableWidget(draggableArea, 750, 750, WorkerPayloadHandler.getWorkerMapData());

        PathosCraft.LOGGER.info(WorkerPayloadHandler.getWorkerMapData().toString());
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (draggableWidget != null) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
                draggableWidget.resetZoom(draggableArea);
                return true;
            }

            if (draggableWidget.mousePressed(mouseX, mouseY, button)) {
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (draggableWidget != null) {
            draggableWidget.mouseReleased(button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (draggableWidget != null) {
            if (draggableWidget.mouseDragged(mouseX, mouseY, button, draggableArea)) {
                return true;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (draggableWidget != null) {
            if (draggableWidget.mouseScrolled(scrollY, draggableArea)) {
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parentScreen);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        if (draggableArea == null || draggableWidget == null) {
            guiGraphics.drawCenteredString(this.font, "Loading...", this.width / 2, this.height / 2, 0xFFFFFF);
            return;
        }

        draggableArea.render(guiGraphics);
        draggableWidget.render(guiGraphics, this.font, draggableArea);
    }

}