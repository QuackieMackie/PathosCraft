package io.github.quackiemackie.pathoscraft.gui.screen.parts.worker;

import io.github.quackiemackie.pathoscraft.PathosCraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class WorkerNodeButton {
    private float localX, localY;
    private final WorkerDraggableWidget parent;

    private final static ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "textures/screen/worker_map_node.png");

    public WorkerNodeButton(WorkerDraggableWidget parent, float x, float y) {
        this.parent = parent;
        this.localX = x;
        this.localY = y;
    }

    public void render(GuiGraphics guiGraphics) {
        float scale = parent.getCurrentScale();

        float globalX = (parent.getX() + localX * scale);
        float globalY = (parent.getY() + localY * scale);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(globalX, globalY, 0);
        guiGraphics.pose().scale(scale, scale, 1.0F);
        guiGraphics.blit(TEXTURE, 0, 0, 0, 0, 16, 16, 16, 16);
        guiGraphics.pose().popPose();
    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        float scale = parent.getCurrentScale();

        float globalX = parent.getX() + localX * scale;
        float globalY = parent.getY() + localY * scale;
        float size = 16 * scale;

        return mouseX >= globalX && mouseX <= globalX + size &&
                mouseY >= globalY && mouseY <= globalY + size;
    }

    public void setPosition(float x, float y) {
        this.localX = x;
        this.localY = y;
    }
}