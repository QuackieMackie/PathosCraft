package net.quackiemackie.pathoscraft.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.quackiemackie.pathoscraft.PathosCraft;

public class ModClientRenders {
    private static final ResourceLocation WARNING_OVERLAY_TEXTURE = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "textures/gui/astral_warning_overlay.png");

    public static void render(GuiGraphics guiGraphics, float partialTicks) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, WARNING_OVERLAY_TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.5F);

        Minecraft mc = Minecraft.getInstance();
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        guiGraphics.blit(WARNING_OVERLAY_TEXTURE, 0, 0, width, height, 0.0F, 0.0F, 256, 256, 256, 256);

        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }
}
