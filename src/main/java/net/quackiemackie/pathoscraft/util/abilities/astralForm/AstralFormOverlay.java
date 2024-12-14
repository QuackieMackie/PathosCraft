package net.quackiemackie.pathoscraft.util.abilities.astralForm;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.quackiemackie.pathoscraft.PathosCraft;

public class AstralFormOverlay {
    private static final ResourceLocation WARNING_OVERLAY_TEXTURE = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "textures/gui/astral_warning_overlay.png");
    public static long astralFormRenderStartTime = 0;

    public static void renderAstralWarningOverlay(GuiGraphics guiGraphics, float partialTicks) {
        long elapsedTime = System.currentTimeMillis() - astralFormRenderStartTime;
        float secondsElapsed = elapsedTime / 1000.0f;
        float opacity = Math.min(1.0f, secondsElapsed * 0.05f);

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, WARNING_OVERLAY_TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, opacity);

        Minecraft mc = Minecraft.getInstance();
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        guiGraphics.blit(WARNING_OVERLAY_TEXTURE, 0, 0, width, height, 0.0F, 0.0F, 256, 256, 256, 256);

        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }

    public static void resetStartTime() {
        astralFormRenderStartTime = System.currentTimeMillis();
    }
}
