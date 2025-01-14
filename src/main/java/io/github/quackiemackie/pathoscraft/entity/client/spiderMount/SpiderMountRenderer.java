package io.github.quackiemackie.pathoscraft.entity.client.spiderMount;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.entity.entity.SpiderMountEntity;

public class SpiderMountRenderer extends MobRenderer<SpiderMountEntity, SpiderMountModel<SpiderMountEntity>> {
    public SpiderMountRenderer(EntityRendererProvider.Context context) {
        super(context, new SpiderMountModel<>(context.bakeLayer(SpiderMountModel.LAYER_LOCATION)), 0.25f);
    }

    @Override
    public ResourceLocation getTextureLocation(SpiderMountEntity spiderMountEntity) {
        return ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "textures/entity/spider_mount.png");
    }

    @Override
    public void render(SpiderMountEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
