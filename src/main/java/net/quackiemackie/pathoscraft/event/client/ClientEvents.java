package net.quackiemackie.pathoscraft.event.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.util.handlers.abilities.AstralFormHandler;
import net.quackiemackie.pathoscraft.network.payload.keybinds.AstralFormKeyPress;
import net.quackiemackie.pathoscraft.registers.PathosKeybinding;
import net.quackiemackie.pathoscraft.registers.PathosAttachments;
import net.quackiemackie.pathoscraft.util.abilities.astralForm.AstralFormOverlay;
import org.joml.Vector2ic;

import java.util.List;

@EventBusSubscriber(modid = PathosCraft.MOD_ID)
public class ClientEvents {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        // Astral Form Keybind logic and action bar
        if (player instanceof IAttachmentHolder holder && holder.getData(PathosAttachments.IN_ASTRAL_FORM.get())) {
            String exitKeyMessage = PathosKeybinding.ASTRAL_FORM_EXIT.getTranslatedKeyMessage().getString();
            Component actionBarMessage = Component.translatable("abilities.pathoscraft.astral_form_exit", exitKeyMessage);
            player.displayClientMessage(actionBarMessage, true);

            if (PathosKeybinding.ASTRAL_FORM_EXIT.consumeClick()) {
                PacketDistributor.sendToServer(new AstralFormKeyPress());
            }
        }
    }

    @SubscribeEvent
    public static void renderOverlay(RenderGuiLayerEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (AstralFormHandler.shouldShowAstralWarningOverlay() && player instanceof IAttachmentHolder holder && holder.getData(PathosAttachments.IN_ASTRAL_FORM.get())) {
            DeltaTracker deltaTracker = event.getPartialTick();
            float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(Minecraft.getInstance().isPaused());
            AstralFormOverlay.renderAstralWarningOverlay(event.getGuiGraphics(), partialTicks);
        } else if (!AstralFormHandler.shouldShowAstralWarningOverlay() && player instanceof IAttachmentHolder holder && !holder.getData(PathosAttachments.IN_ASTRAL_FORM.get()) && AstralFormOverlay.astralFormRenderStartTime == 0){
            AstralFormOverlay.resetStartTime();
        }
    }

    @SubscribeEvent
    public static void onItemTooltip(RenderTooltipEvent.Pre event) {
        if (BuiltInRegistries.ITEM.getKey(event.getItemStack().getItem()).getNamespace().equals(PathosCraft.MOD_ID)) {
            event.setCanceled(true);

            Font font = event.getFont();
            int mouseX = event.getX();
            int mouseY = event.getY();
            List<ClientTooltipComponent> components = event.getComponents();

            if (components.isEmpty()) return;

            int topPadding = 2;
            int bottomPadding = 8;

            int tooltipWidth = components.stream().mapToInt(c -> c.getWidth(font)).max().orElse(0);
            int baseHeight = components.stream().mapToInt(ClientTooltipComponent::getHeight).sum();
            int tooltipHeight = baseHeight + topPadding + bottomPadding + 4 + 2;

            GuiGraphics graphics = event.getGraphics();
            ClientTooltipPositioner positioner = event.getTooltipPositioner();
            Vector2ic position = positioner.positionTooltip(
                    graphics.guiWidth(),
                    graphics.guiHeight(),
                    mouseX,
                    mouseY,
                    tooltipWidth,
                    tooltipHeight
            );
            int x = position.x(), y = position.y();

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

            renderComponents(font, components, x, y + topPadding, tooltipWidth, graphics);
        }
    }

    private static void renderComponents(Font font, List<ClientTooltipComponent> components, int x, int y, int tooltipWidth, GuiGraphics graphics) {
        int textY = y;

        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, 400.0F);

        if (!components.isEmpty()) {
            ClientTooltipComponent titleComponent = components.getFirst();

            int titleWidth = titleComponent.getWidth(font);
            int titleX = x + (tooltipWidth / 2) - (titleWidth / 2);
            titleComponent.renderText(font, titleX, textY, graphics.pose().last().pose(), graphics.bufferSource());

            int underlinePadding = 2;
            int lineWidth = (int) (tooltipWidth * 0.8);
            int lineStartX = x + (tooltipWidth / 2) - (lineWidth / 2);
            int lineEndX = lineStartX + lineWidth;
            int lineY = textY + titleComponent.getHeight() + underlinePadding;

            graphics.fill(lineStartX, lineY, lineEndX, lineY + 1, 0x55800080);

            textY += titleComponent.getHeight() + underlinePadding + 4;
        }

        for (int i = 1; i < components.size(); i++) {
            ClientTooltipComponent component = components.get(i);
            component.renderText(font, x, textY, graphics.pose().last().pose(), graphics.bufferSource());
            textY += component.getHeight() + 2;
        }

        graphics.pose().popPose();
    }
}