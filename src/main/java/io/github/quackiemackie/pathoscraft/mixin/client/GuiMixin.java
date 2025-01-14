package io.github.quackiemackie.pathoscraft.mixin.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void customRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        String fpsText = "FPS: " + minecraft.getFps();

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int textWidth = minecraft.font.width(fpsText);
        int centeredX = (screenWidth - textWidth) / 2;

        guiGraphics.drawString(minecraft.font, fpsText, centeredX, 10, 0xFFFFFF);
    }
}