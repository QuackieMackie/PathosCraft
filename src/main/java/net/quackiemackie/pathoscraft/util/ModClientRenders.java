package net.quackiemackie.pathoscraft.util;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.resources.ResourceLocation;
import net.quackiemackie.pathoscraft.PathosCraft;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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


    public static void renderPlayerSkin(GuiGraphics guiGraphics, UUID playerUUID, int x, int y, int z, int width, int height) {
        SkinManager skinManager = Minecraft.getInstance().getSkinManager();
        GameProfile profile = new GameProfile(playerUUID, null);
        CompletableFuture<PlayerSkin> skinFuture = skinManager.getOrLoad(profile);

        skinFuture.thenAccept(playerSkin -> {
            ResourceLocation texture = playerSkin.texture() != null ? playerSkin.texture() : DefaultPlayerSkin.getDefaultTexture();

            RenderSystem.setShaderTexture(0, texture);
            // Rendering the skin on the specified location
            guiGraphics.blit(texture, x, y, z, 8, 8, width, height, 64, 64);
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }
}
