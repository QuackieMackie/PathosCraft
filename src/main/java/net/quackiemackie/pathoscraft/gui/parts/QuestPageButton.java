package net.quackiemackie.pathoscraft.gui.parts;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.quackiemackie.pathoscraft.gui.screen.QuestScreen;

public class QuestPageButton extends AbstractButton {
    private final boolean flipped;
    private final QuestScreen questScreen;

    public QuestPageButton(int x, int y, int width, int height, Component message, boolean flipped, QuestScreen questScreen) {
        super(x, y, width, height, message);
        this.flipped = flipped;
        this.questScreen = questScreen;
    }

    @Override
    public void onPress() {
        if (flipped) {
            if (questScreen.currentPage > 1) {
                questScreen.currentPage--;
                questScreen.initQuestSlotButtons();
            }
        } else {
            if (questScreen.currentPage < questScreen.maxPages) {
                questScreen.currentPage++;
                questScreen.initQuestSlotButtons();
            }
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int buttonColor = 0xC6C6C6C6;
        if (this.isHovered()) {
            buttonColor = 0xFFFFFFFF;
        }

        drawTriangle(graphics, buttonColor);
    }

    private void drawTriangle(GuiGraphics graphics, int color) {
        int midY = this.getY() + this.height / 2;

        int lineWidth = 2;
        int halfHeight = this.height / 2;

        if (flipped) {
            // Draw a left-facing arrow: "<"
            for (int i = 0; i < halfHeight; i++) {
                graphics.fill(this.getX() + i, midY - i, this.getX() + i + lineWidth, midY + i + lineWidth, color);
                graphics.fill(this.getX() + i, midY + i, this.getX() + i + lineWidth, midY - i + lineWidth, color);
            }
        } else {
            // Draw a right-facing arrow: ">"
            for (int i = 0; i < halfHeight; i++) {
                graphics.fill(this.getX() + this.width - i - lineWidth, midY - i, this.getX() + this.width - i, midY + i + lineWidth, color);
                graphics.fill(this.getX() + this.width - i - lineWidth, midY + i, this.getX() + this.width - i, midY - i + lineWidth, color);
            }
        }
    }
}