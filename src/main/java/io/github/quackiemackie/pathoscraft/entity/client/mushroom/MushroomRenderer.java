package io.github.quackiemackie.pathoscraft.entity.client.mushroom;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.entity.entity.MushroomEntity;

public class MushroomRenderer extends MobRenderer<MushroomEntity, MushroomModel<MushroomEntity>> {
    public MushroomRenderer(EntityRendererProvider.Context context) {
        super(context, new MushroomModel<>(context.bakeLayer(MushroomModel.LAYER_LOCATION)), 0.25f);
    }

    @Override
    public ResourceLocation getTextureLocation(MushroomEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "textures/entity/mushroom.png");
    }

    @Override
    public void render(MushroomEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if(entity.isBaby()) {
            poseStack.scale(0.45f, 0.45f, 0.45f);
        } else {
            poseStack.scale(1f, 1f, 1f);
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
