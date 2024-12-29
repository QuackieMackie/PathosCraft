package net.quackiemackie.pathoscraft.mixin;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Deprecated
@Mixin(value = GuiGraphics.class, priority = 1001)
public class GuiGraphicsMixin {
    @Inject(method = "renderTooltipInternal", at = @At(value = "INVOKE", target = "size()I", ordinal = 0), cancellable = true)
    private void modifyTooltipBorder(Font font, List<ClientTooltipComponent> components, int mouseX, int mouseY, ClientTooltipPositioner tooltipPositioner, CallbackInfo ci) {
        if (components.isEmpty()) return;

        // Dynamically calculate tooltip dimensions
        int tooltipWidth = components.stream().mapToInt(c -> c.getWidth(font)).max().orElse(0);
        int baseHeight = components.stream().mapToInt(ClientTooltipComponent::getHeight).sum();
        int tooltipHeight = baseHeight + 4 + 2;

        // Tooltip position
        Vector2ic position = tooltipPositioner.positionTooltip(((GuiGraphics) (Object) this).guiWidth(), ((GuiGraphics) (Object) this).guiHeight(), mouseX, mouseY, tooltipWidth, tooltipHeight);
        int x = position.x(), y = position.y();

        // Render the tooltip background with standard colors
        TooltipRenderUtil.renderTooltipBackground((GuiGraphics) (Object) this, x, y, tooltipWidth, tooltipHeight, 400, 0xF0100010, 0xF0100010, 0xFF800080, 0xFF000000);

        // Render the components
        pathosCraft$renderComponents(font, components, x, y, tooltipWidth);

        ci.cancel();
    }

    @Unique
    private void pathosCraft$renderComponents(Font font, List<ClientTooltipComponent> components, int x, int y, int tooltipWidth) {
        GuiGraphics guiGraphics = (GuiGraphics) (Object) this;
        int textY = y;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, 400.0F);

        // Render title (first component) and underline
        if (!components.isEmpty()) {
            ClientTooltipComponent titleComponent = components.getFirst();

            // Center the title text
            int titleWidth = titleComponent.getWidth(font);
            int titleX = x + (tooltipWidth / 2) - (titleWidth / 2);
            titleComponent.renderText(font, titleX, textY, guiGraphics.pose().last().pose(), guiGraphics.bufferSource());

            // Render underline (80% of tooltip width)
            int lineWidth = (int) (tooltipWidth * 0.8);
            int lineStartX = x + (tooltipWidth / 2) - (lineWidth / 2);
            int lineEndX = lineStartX + lineWidth;
            int lineY = textY + titleComponent.getHeight();
            guiGraphics.fill(lineStartX, lineY, lineEndX, lineY + 1, 0x60800080);

            textY += titleComponent.getHeight() + 4;
        }

        // Render remaining text components
        for (int i = 1; i < components.size(); i++) {
            ClientTooltipComponent component = components.get(i);
            component.renderText(font, x, textY, guiGraphics.pose().last().pose(), guiGraphics.bufferSource());
            textY += component.getHeight() + (i == 1 ? 2 : 0);
        }

        // Render associated images
        textY = y;
        for (int i = 0; i < components.size(); i++) {
            ClientTooltipComponent component = components.get(i);
            component.renderImage(font, x, textY, guiGraphics);
            textY += component.getHeight() + (i == 0 ? 2 : 0);
        }

        guiGraphics.pose().popPose();
    }
}