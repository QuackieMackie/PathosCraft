package net.quackiemackie.pathoscraft.gui.parts;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class QuestSlotButton extends Button {

    private final ItemStack itemStack;
    private final List<Component> hoverInfo;

    public QuestSlotButton(int x, int y, Component message, ItemStack itemStack, OnPress onPress) {
        super(x, y, 16, 16, message, onPress, DEFAULT_NARRATION);
        this.itemStack = itemStack;
        this.hoverInfo = new ArrayList<>();
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        int itemX = this.getX() + (this.width / 2);
        int itemY = this.getY() + (this.height / 2);

        renderItem(itemStack, itemX, itemY, guiGraphics);
        //renderBorder(guiGraphics);

        if (this.isHovered()) {
            if (!hoverInfo.isEmpty()) {
                guiGraphics.renderTooltip(Minecraft.getInstance().font, hoverInfo, ItemStack.EMPTY.getTooltipImage(), mouseX, mouseY);
            }
        }
    }

    public void addHoverInfo(Component info) {
        this.hoverInfo.add(info);
    }

    private void renderBorder(GuiGraphics guiGraphics) {
        int borderColor = 0xFFFFFFFF;
        int left = this.getX();
        int top = this.getY();
        int right = this.getX() + this.width;
        int bottom = this.getY() + this.height;

        guiGraphics.fill(left, top, right, top + 1, borderColor);
        guiGraphics.fill(left, bottom - 1, right, bottom, borderColor);
        guiGraphics.fill(left, top, left + 1, bottom, borderColor);
        guiGraphics.fill(right - 1, top, right, bottom, borderColor);
    }

    private void renderItem(ItemStack itemStack, int x, int y, GuiGraphics guiGraphics) {
        PoseStack poseStack = guiGraphics.pose();

        poseStack.pushPose();
        poseStack.translate(x, y, 100.0F);

        boolean isBlock = itemStack.getItem() instanceof BlockItem;

        if (isBlock) {
            poseStack.scale(16.0F, -16.0F, 16.0F);
            renderBlockItem(itemStack, poseStack, guiGraphics);
        } else {
            guiGraphics.renderItem(itemStack, -8, -8);
        }

        poseStack.popPose();
    }

    private void renderBlockItem(ItemStack itemStack, PoseStack poseStack, GuiGraphics guiGraphics) {
        Minecraft minecraft = Minecraft.getInstance();
        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
        BakedModel bakedModel = minecraft.getItemRenderer().getModel(itemStack, null, minecraft.player, 0);

        minecraft.getItemRenderer().render(itemStack, ItemDisplayContext.GUI, false, poseStack, bufferSource, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, bakedModel);
        bufferSource.endBatch();
    }
}