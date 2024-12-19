package net.quackiemackie.pathoscraft.gui.parts.miniGames;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

public class InformationButton extends Button {

    private final List<Component> hoverInfo;
    private boolean isInfoVisible = false;

    /**
     * Constructs a new InformationButton.
     *
     * @param x The x-coordinate of the button.
     * @param y The y-coordinate of the button.
     */
    public InformationButton(int x, int y) {
        super(x, y, 16, 16, Component.literal("I"), button -> {}, DEFAULT_NARRATION);
        this.hoverInfo = new ArrayList<>();
    }

    @Override
    public void onPress() {
        toggleInfoVisibility();
    }

    /**
     * Adds hover information directly from an array of strings.
     *
     * @param lines The hover information provided as strings.
     */
    public void setHoverInfo(String[] lines) {
        this.hoverInfo.clear();
        for (String line : lines) {
            this.hoverInfo.add(Component.literal(line));
        }
    }

    /**
     * Toggles the visibility of the hover information.
     */
    private void toggleInfoVisibility() {
        isInfoVisible = !isInfoVisible;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        Minecraft minecraft = Minecraft.getInstance();
        boolean shouldHighlight = this.isHovered() || isInfoVisible;

        ResourceLocation sprite = shouldHighlight ? SPRITES.get(true, true) : SPRITES.enabled();
        guiGraphics.blitSprite(sprite, this.getX(), this.getY(), this.getWidth(), this.getHeight());

        int textColor = getFGColor() | Mth.ceil(this.alpha * 255.0F) << 24;
        guiGraphics.drawCenteredString(minecraft.font, this.getMessage(), this.getX() + this.getWidth() / 2, this.getY() + (this.getHeight() - 8) / 2, textColor);

        if (shouldHighlight) {
            List<FormattedCharSequence> tooltip = new ArrayList<>();
            for (Component component : hoverInfo) {
                tooltip.add(component.getVisualOrderText());
            }

            int tooltipX = this.getX() + this.getWidth();
            int tooltipY = this.getY() + this.getHeight();
            guiGraphics.renderTooltip(minecraft.font, tooltip, tooltipX, tooltipY);
        }
    }
}