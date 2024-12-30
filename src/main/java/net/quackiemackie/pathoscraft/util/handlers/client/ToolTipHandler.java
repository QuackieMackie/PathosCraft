package net.quackiemackie.pathoscraft.util.handlers.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import org.joml.Vector2ic;

import java.util.List;

public class ToolTipHandler {

    public static void renderCustomTooltip(Font font, List<ClientTooltipComponent> components, GuiGraphics graphics, ClientTooltipPositioner positioner, int mouseX, int mouseY) {
        if (components.isEmpty()) return;

        // Padding values
        int topPadding = 4;
        int bottomPadding = 6;
        int sidePadding = 4;
        int lineSpacing = 2;

        // Calculate tooltip dimensions
        int tooltipWidth = components.stream()
                .mapToInt(c -> c.getWidth(font))
                .max()
                .orElse(0);

        tooltipWidth += sidePadding * 2;

        int baseHeight = components.stream()
                .mapToInt(ClientTooltipComponent::getHeight)
                .sum();

        int tooltipHeight = baseHeight
                + topPadding
                + bottomPadding
                + (components.size() - 1) * lineSpacing;

        // Determine tooltip positioning
        Vector2ic position = positioner.positionTooltip(
                graphics.guiWidth(),
                graphics.guiHeight(),
                mouseX,
                mouseY,
                tooltipWidth,
                tooltipHeight
        );
        int x = position.x(), y = position.y();

        // Render tooltip background
        TooltipRenderUtil.renderTooltipBackground(
                graphics,
                x,
                y,
                tooltipWidth,
                tooltipHeight,
                400,
                0xF0100010,
                0xF0100010,
                0xFF800080,
                0xFF000000
        );

        // Render tooltip components and the gradient
        renderComponentsWithGradient(font, components, x + sidePadding, y + topPadding, tooltipWidth, graphics, lineSpacing);
    }

    private static void renderComponentsWithGradient(Font font, List<ClientTooltipComponent> components, int x, int y, int tooltipWidth, GuiGraphics graphics, int lineSpacing) {
        int textY = y;

        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, 400.0F); // Ensure rendering over the background

        if (!components.isEmpty()) {
            // Render the first line (centered) with an underline
            ClientTooltipComponent titleComponent = components.getFirst();

            int titleWidth = titleComponent.getWidth(font);
            int centeredX = x + (tooltipWidth / 2) - (titleWidth / 2); // Center the title text horizontally
            titleComponent.renderText(font, centeredX, textY, graphics.pose().last().pose(), graphics.bufferSource());

            // Draw the gradient underline below the title
            int underlineY = textY + titleComponent.getHeight() + 2; // Adjust for line padding
            drawGradientLine(graphics, x, underlineY, tooltipWidth, 1);

            textY += titleComponent.getHeight() + 1 + 6; // Space for the underline and padding
        }

        // Render remaining components (not centered)
        for (int i = 1; i < components.size(); i++) {
            ClientTooltipComponent component = components.get(i);
            component.renderText(font, x, textY, graphics.pose().last().pose(), graphics.bufferSource());
            textY += component.getHeight() + lineSpacing;
        }

        graphics.pose().popPose();
    }

    private static void drawGradientLine(GuiGraphics graphics, int x, int y, int width, int height) {
        int midPoint = x + (width / 2);

        // Render the left side gradient
        for (int i = 0; i < width / 2; i++) {
            int alpha = (int) (255 * (1.0 - (i / (float) (width / 2)))); // Gradual fade to transparent
            int color = (alpha << 24) | 0x800080; // Purple with dynamic alpha
            graphics.fill(midPoint - i, y, midPoint - i - 1, y + height, color);
        }

        // Render the right side gradient
        for (int i = 0; i < width / 2; i++) {
            int alpha = (int) (255 * (1.0 - (i / (float) (width / 2)))); // Gradual fade to transparent
            int color = (alpha << 24) | 0x800080; // Purple with dynamic alpha
            graphics.fill(midPoint + i, y, midPoint + i + 1, y + height, color);
        }
    }
}
