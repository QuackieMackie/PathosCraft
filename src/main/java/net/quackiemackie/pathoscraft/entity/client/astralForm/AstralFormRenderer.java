package net.quackiemackie.pathoscraft.entity.client.astralForm;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.entity.entity.AstralFormEntity;

public class AstralFormRenderer extends MobRenderer<AstralFormEntity, AstralFormModel<AstralFormEntity>> {
    public AstralFormRenderer(EntityRendererProvider.Context context) {
        super(context, new AstralFormModel<>(context.bakeLayer(AstralFormModel.LAYER_LOCATION)), 0.25f);
    }

    @Override
    public ResourceLocation getTextureLocation(AstralFormEntity astralFormEntity) {
        return ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "textures/entity/astral_form.png");
    }

    @Override
    public void render(AstralFormEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
