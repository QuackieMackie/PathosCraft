package net.quackiemackie.pathoscraft.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.quackiemackie.pathoscraft.entity.advanced.SpiderMountEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SpiderMountRenderer extends GeoEntityRenderer<SpiderMountEntity> {

    public SpiderMountRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SpiderMountModel());
    }
}
