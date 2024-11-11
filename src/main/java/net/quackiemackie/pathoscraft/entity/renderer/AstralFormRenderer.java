package net.quackiemackie.pathoscraft.entity.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.quackiemackie.pathoscraft.entity.entitys.AstralFormEntity;
import net.quackiemackie.pathoscraft.entity.model.AstralFormModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AstralFormRenderer extends GeoEntityRenderer<AstralFormEntity> {

    public AstralFormRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AstralFormModel());
    }

}
