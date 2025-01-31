package io.github.quackiemackie.pathoscraft.gui.screen.parts.worker;

import io.github.quackiemackie.pathoscraft.PathosCraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class WorkerTabButton extends AbstractButton {
    private final OnPress onPress;

    private final static ResourceLocation TEXTURE_SELECTED = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "textures/screen/worker_map_tab_selected.png");
    private final static ResourceLocation TEXTURE_UNSELECTED = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "textures/screen/worker_map_tab_unselected.png");

    public interface OnPress {
        void onPress(WorkerTabButton button);
    }

    public WorkerTabButton(int x, int y, OnPress onPress) {
        super(x, y, 32, 26, Component.empty());
        this.onPress = onPress;
    }

    @Override
    public void onPress() {
        if (this.onPress != null) {
            this.onPress.onPress(this);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        ResourceLocation texture = this.isHovered() ? TEXTURE_SELECTED : TEXTURE_UNSELECTED;
        guiGraphics.pose().pushPose();

        if (this.isHovered()) {
            guiGraphics.pose().translate(0, 0, 0);
        } else {
            guiGraphics.pose().translate(0, 0, 10);
        }

        guiGraphics.blit(texture, this.getX(), this.getY(), 0, 0, this.width, this.height, 32, 26);
        guiGraphics.pose().popPose();
    }
}
