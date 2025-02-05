package io.github.quackiemackie.pathoscraft.gui.screen.worker;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.gui.menu.WorkerMainMenu;
import io.github.quackiemackie.pathoscraft.gui.screen.parts.worker.WorkerTabButton;
import io.github.quackiemackie.pathoscraft.util.worker.WorkerNodeList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.Map;
import java.util.Objects;

public class WorkerMainScreen extends AbstractContainerScreen<WorkerMainMenu> {
    private final WorkerMainMenu menu;
    private Map<Integer, Integer> slotMapData;
    private WorkerNodeList workerNodes;

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

        int buttonX = (this.width - this.imageWidth) / 2 - 29;
        int buttonY = (this.height - this.imageHeight) / 2 + 10;
        WorkerTabButton mapTab = new WorkerTabButton(buttonX, buttonY, button -> {
            this.slotMapData = menu.getSlotMapData();
            this.workerNodes = menu.getNodeList();
            Objects.requireNonNull(this.minecraft).setScreen(new WorkerMapScreen(this, slotMapData, workerNodes));
        });
        this.addRenderableWidget(mapTab);
    }


    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 5);
        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
        guiGraphics.pose().popPose();
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