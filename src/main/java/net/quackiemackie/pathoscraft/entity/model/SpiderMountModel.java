package net.quackiemackie.pathoscraft.entity.model;

import net.minecraft.resources.ResourceLocation;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.entity.entitys.SpiderMountEntity;
import software.bernie.geckolib.model.GeoModel;

public class SpiderMountModel extends GeoModel<SpiderMountEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "geo/entity/spider_mount.geo.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "textures/entity/spider_mount.png");
    private static final ResourceLocation ANIMATIONS = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "animations/entity/spider_mount.animation.json");

    @Override
    public ResourceLocation getModelResource(SpiderMountEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(SpiderMountEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(SpiderMountEntity animatable) {
        return ANIMATIONS;
    }
}
