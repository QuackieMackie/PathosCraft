package net.quackiemackie.pathoscraft.gui.parts.miniGames;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class FishingSequenceButton extends AbstractButton {

    public FishingSequenceButton(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    public void onPress() {
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        // Background for the button
        guiGraphics.fill(getX(), getY(), getX() + this.width, getY() + this.height, 0xAA000000);

        int borderColor = 0xFFAAAAAA;

        guiGraphics.hLine(getX(), getX() + this.width - 1, getY(), borderColor);
        guiGraphics.hLine(getX(), getX() + this.width - 1, getY() + this.height - 1, borderColor);
        guiGraphics.vLine(getX(), getY(), getY() + this.height - 1, borderColor);
        guiGraphics.vLine(getX() + this.width - 1, getY(), getY() + this.height - 1, borderColor);

        // Text in the middle of the button
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, this.getMessage(), getX() + this.width / 2, getY() + (this.height - 8) / 2, 0xFFFFFFFF);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }
}