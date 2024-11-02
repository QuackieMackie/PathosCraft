package net.quackiemackie.pathoscraft.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.quackiemackie.pathoscraft.entity.advanced.AstralFormEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AstralFormRenderer extends GeoEntityRenderer<AstralFormEntity> {

    public AstralFormRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AstralFormModel());
    }

}
