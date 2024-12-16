package net.quackiemackie.pathoscraft.entity.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.quackiemackie.pathoscraft.entity.entity.SpiderMountEntity;
import net.quackiemackie.pathoscraft.entity.model.SpiderMountModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SpiderMountRenderer extends GeoEntityRenderer<SpiderMountEntity> {

    public SpiderMountRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SpiderMountModel());
    }
}
