package io.github.quackiemackie.pathoscraft.gui.screen.worker;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.gui.menu.WorkerMainMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.Map;

public class WorkerMainScreen extends AbstractContainerScreen<WorkerMainMenu> {
    private final WorkerMainMenu menu;
    private Map<Integer, Integer> slotMapData;

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "textures/screen/worker_main_menu.png");

    public WorkerMainScreen(WorkerMainMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.menu = menu;

        this.imageWidth = 176;
        this.imageHeight = 256;
    }

    @Override
    protected void init() {
        super.init();

        int buttonX = (this.width - this.imageWidth) / 2 + 10;
        int buttonY = (this.height - this.imageHeight) / 2 + 220;

        Button mapButton = Button.builder(Component.literal("Open Map"), button -> {
            this.slotMapData = menu.getSlotMapData();
            this.minecraft.setScreen(new WorkerMapScreen(this, slotMapData));
        }).bounds(buttonX, buttonY, 90, 20).build();

        this.addRenderableWidget(mapButton);
    }


    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        int inventoryLabelY = this.inventoryLabelY + 56;
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, inventoryLabelY, 4210752, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}