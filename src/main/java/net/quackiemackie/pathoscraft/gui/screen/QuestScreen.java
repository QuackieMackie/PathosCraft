package net.quackiemackie.pathoscraft.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.quackiemackie.pathoscraft.gui.menu.QuestMenu;

public class QuestScreen extends AbstractContainerScreen<QuestMenu> {

    public QuestScreen(QuestMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("quest_menu", "textures/screen/quest_menu.png");

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        // Render the background texture of your GUI
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // Render labels
        guiGraphics.drawString(this.font, this.title.getString(), 8, 6, 4210752);
    }
}
