package io.github.quackiemackie.pathoscraft.gui.screen.worker;

import io.github.quackiemackie.pathoscraft.gui.parts.DraggableArea;
import io.github.quackiemackie.pathoscraft.gui.parts.DraggableWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class WorkerMapScreen extends Screen {
    private DraggableArea draggableArea;
    private DraggableWidget draggableWidget;

    public WorkerMapScreen() {
        super(Component.literal("title"));
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

        // Initialize draggable area and widget
        draggableArea = new DraggableArea(startX, startY, maxX, maxY, 5, 0xFF000000, 0x77000000);
        draggableWidget = new DraggableWidget(draggableArea, 750, 750);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
            draggableWidget.resetZoom(draggableArea);
            return true;
        }

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
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (draggableWidget.mouseScrolled(scrollY, draggableArea)) {
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        draggableArea.render(guiGraphics);
        draggableWidget.render(guiGraphics, this.font, draggableArea);
    }
}