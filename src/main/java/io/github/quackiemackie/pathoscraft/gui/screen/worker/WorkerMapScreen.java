package io.github.quackiemackie.pathoscraft.gui.screen.worker;

import io.github.quackiemackie.pathoscraft.gui.parts.worker.DraggableArea;
import io.github.quackiemackie.pathoscraft.gui.parts.worker.DraggableWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class WorkerMapScreen extends Screen {
    private DraggableArea draggableArea;
    private DraggableWidget draggableWidget;

    public WorkerMapScreen() {
        super(Component.literal("title"));
    }

    @Override
    protected void init() {
        super.init();

        // Initialize draggable area and widget
        draggableArea = new DraggableArea(this.width, this.height);
        draggableWidget = new DraggableWidget(draggableArea.getX() + 10, draggableArea.getY() + 10, 50, 50);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (draggableWidget.mousePressed(mouseX, mouseY, button)) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        draggableWidget.mouseReleased(button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (draggableWidget.mouseDragged(mouseX, mouseY, button, draggableArea)) {
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        draggableArea.render(guiGraphics);
        guiGraphics.pose().pushPose();
        guiGraphics.enableScissor(
                draggableArea.getX(),
                draggableArea.getY(),
                draggableArea.getX() + draggableArea.getWidth(),
                draggableArea.getY() + draggableArea.getHeight()
        );
        draggableWidget.render(guiGraphics, this.font);
        guiGraphics.disableScissor();
        guiGraphics.pose().popPose();
    }
}