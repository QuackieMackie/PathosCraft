package net.quackiemackie.pathoscraft.gui.parts.quest;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

public class QuestInformationButton extends Button {

    private final List<Component> hoverInfo;
    private boolean isInfoVisible = false;

    /**
     * Constructs a new QuestSlotButton.
     *
     * @param x The x-coordinate of the button.
     * @param y The y-coordinate of the button.
     */
    public QuestInformationButton(int x, int y) {
        super(x, y, 16, 16, Component.literal("I"), button -> {}, DEFAULT_NARRATION);
        this.hoverInfo = new ArrayList<>();
    }

    @Override
    public void onPress() {
        toggleInfoVisibility();
    }

    /**
     * Adds information to be displayed when hovering over the button.
     *
     * @param info The hover information to add.
     */
    private void addHoverInfo(Component info) {
        this.hoverInfo.add(info);
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

            String[] lines = Component.translatable("menu.widget.pathoscraft.quest_menu.information_button.tooltip").getString().split("\n");

            for (String line : lines) {
                tooltip.add(Component.literal(line).getVisualOrderText());
            }

            int tooltipX = this.getX() + this.getWidth();
            int tooltipY = this.getY() + this.getHeight();
            guiGraphics.renderTooltip(minecraft.font, tooltip, tooltipX, tooltipY);
        }
    }
}