package io.github.quackiemackie.pathoscraft.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.quackiemackie.pathoscraft.block.entity.WorkerStationBE;
import io.github.quackiemackie.pathoscraft.gui.screen.parts.worker.WorkerMapRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class WorkerStationBER implements BlockEntityRenderer<WorkerStationBE> {
    private final WorkerMapRenderer workerMapRenderer;

    public WorkerStationBER(BlockEntityRendererProvider.Context context) {
        Minecraft minecraft = Minecraft.getInstance();
        this.workerMapRenderer = new WorkerMapRenderer(
                minecraft.getTextureManager(),
                minecraft.getMapDecorationTextures()
        );
    }

    @Override
    public void render(WorkerStationBE blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        WorkerMapRenderer.renderMapOnBE(workerMapRenderer, poseStack, bufferSource, blockEntity, 0.0025f, 0xF000F0);
    }
}
