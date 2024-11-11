package net.quackiemackie.pathoscraft.entity.model;

import net.minecraft.resources.ResourceLocation;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.entity.entitys.AstralFormEntity;
import software.bernie.geckolib.model.GeoModel;

public class AstralFormModel extends GeoModel<AstralFormEntity> {

    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "geo/entity/player_ghost.geo.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "textures/entity/player_ghost.png");
    private static final ResourceLocation ANIMATIONS = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "animations/entity/player_ghost.animation.json");

    @Override
    public ResourceLocation getModelResource(AstralFormEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(AstralFormEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(AstralFormEntity animatable) {
        return ANIMATIONS;
    }
}
