package io.github.quackiemackie.pathoscraft.util.handlers.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import org.joml.Vector2ic;

import java.util.List;

public class ToolTipHandler {

    /**
     * Renders a customizable tooltip at a specified position on the screen.
     *
     * @param font the font used to render the tooltip text
     * @param components a list of {@code ClientTooltipComponent} objects representing each line of the tooltip content
     * @param graphics the {@code GuiGraphics} object used for rendering the tooltip
     * @param positioner an implementation of {@code ClientTooltipPositioner} used to determine the tooltip's position
     * @param mouseX the x-coordinate of the mouse position in the GUI
     * @param mouseY the y-coordinate of the mouse position in the GUI
     */
    public static void renderCustomTooltip(Font font, List<ClientTooltipComponent> components, GuiGraphics graphics, ClientTooltipPositioner positioner, int mouseX, int mouseY) {
        if (components.isEmpty()) return;

        // Tool tip width and height padding values
        int topPadding = 4, bottomPadding = 6;
        int sidePadding = 4, lineSpacing = 2;

        // Calculate tooltip dimensions
        int tooltipWidth = components.stream().mapToInt(c -> c.getWidth(font)).max().orElse(0);
        tooltipWidth += sidePadding * 2;

        int baseHeight = components.stream().mapToInt(ClientTooltipComponent::getHeight).sum();
        int tooltipHeight = baseHeight + topPadding + bottomPadding + (components.size() - 1) * lineSpacing;

        // Determine tooltip positioning
        Vector2ic position = positioner.positionTooltip(graphics.guiWidth(), graphics.guiHeight(), mouseX, mouseY, tooltipWidth, tooltipHeight);
        int x = position.x(), y = position.y();

        // Render the tooltip background
        TooltipRenderUtil.renderTooltipBackground(graphics, x, y, tooltipWidth, tooltipHeight, 400, 0xF0100010, 0xF0100010, 0xFF800080, 0xFF000000);

        boolean hasTitle = components.size() > 1;

        // Render tooltip components and the gradient
        renderComponentsWithGradient(font, components, x + sidePadding, y + topPadding, tooltipWidth, graphics, lineSpacing, hasTitle);
    }

    /**
     * Renders a list of tooltip components with the first component centered and underlined
     * by a gradient, and the rest of the components left-aligned.
     *
     * @param font the font used to render the components
     * @param components a list of {@code ClientTooltipComponent} objects to render
     * @param x the x-coordinate where the rendering starts
     * @param y the y-coordinate where the rendering starts
     * @param tooltipWidth the total width of the tooltip
     * @param graphics the graphics object used for rendering
     * @param lineSpacing the spacing between consecutive lines of components
     */
    private static void renderComponentsWithGradient(Font font, List<ClientTooltipComponent> components, int x, int y, int tooltipWidth, GuiGraphics graphics, int lineSpacing, boolean hasTitle) {
        int textY = y;
        int titleLineHeight = 1;

        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, 400.0F);

        if (!components.isEmpty()) {
            ClientTooltipComponent firstComponent = components.getFirst();

            // If the tooltip has a "title," center and underline the first component
            if (hasTitle) {
                int titleWidth = firstComponent.getWidth(font);
                int centeredX = x + (tooltipWidth / 2) - (titleWidth / 2);
                firstComponent.renderText(font, centeredX, textY, graphics.pose().last().pose(), graphics.bufferSource());

                // Draw an underline if there are more lines
                if (components.size() > 1) {
                    int underlineY = textY + firstComponent.getHeight() + 2;
                    drawGradientLine(graphics, x, underlineY, tooltipWidth, titleLineHeight);
                    textY += firstComponent.getHeight() + titleLineHeight + 6; // Add padding for the next lines
                } else {
                    textY += firstComponent.getHeight() + 4; // Add basic padding for single-line tooltips
                }
            } else {
                // If there's no title, render the first component as a standard left-aligned line
                firstComponent.renderText(font, x, textY, graphics.pose().last().pose(), graphics.bufferSource());
                textY += firstComponent.getHeight() + 4;
            }
        }

        // Render remaining components (all left-aligned consistently)
        for (int i = 1; i < components.size(); i++) {
            ClientTooltipComponent component = components.get(i);
            component.renderText(font, x, textY, graphics.pose().last().pose(), graphics.bufferSource());
            textY += component.getHeight() + lineSpacing;
        }

        graphics.pose().popPose();
    }

    /**
     * Draws a horizontal gradient line at the specified position and dimensions.
     * The gradient fades towards transparency from the center of the line to both edges.
     *
     * @param graphics the graphics object used to render the gradient line
     * @param x the starting x-coordinate of the gradient line
     * @param y the y-coordinate of the gradient line
     * @param width the width of the gradient line
     * @param height the height of the gradient line
     */
    private static void drawGradientLine(GuiGraphics graphics, int x, int y, int width, int height) {
        int midPoint = x + (width / 2);

        // Left side gradient
        for (int i = 0; i < width / 2; i++) {
            int alpha = (int) (255 * (1.0 - (i / (float) (width / 2)))); // Gradual fade to transparent
            int color = (alpha << 24) | 0x800080; // Purple with dynamic alpha
            graphics.fill(midPoint - i, y, midPoint - i - 1, y + height, color);
        }

        // Right side gradient
        for (int i = 0; i < width / 2; i++) {
            int alpha = (int) (255 * (1.0 - (i / (float) (width / 2)))); // Gradual fade to transparent
            int color = (alpha << 24) | 0x800080; // Purple with dynamic alpha
            graphics.fill(midPoint + i, y, midPoint + i + 1, y + height, color);
        }
    }
}
