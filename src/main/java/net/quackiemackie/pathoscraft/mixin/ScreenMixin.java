package net.quackiemackie.pathoscraft.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void customRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        String fpsText = String.valueOf(minecraft.getFps());

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int textWidth = minecraft.font.width(fpsText);
        int centeredX = (screenWidth - textWidth) / 2;

        guiGraphics.drawString(minecraft.font, fpsText, centeredX, 10, 0xFFFFFF);
    }
}
