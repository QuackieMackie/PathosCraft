package net.quackiemackie.pathoscraft.item.model;

import net.minecraft.resources.ResourceLocation;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.item.advanced.AstralLantern;
import software.bernie.geckolib.model.GeoModel;

public class AstralLanternModel extends GeoModel<AstralLantern> {

    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "geo/astral_lantern.geo.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "textures/item/astral_lantern.png");
    private static final ResourceLocation ANIMATIONS = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "animations/astral_lantern.animation.json");

    @Override
    public ResourceLocation getModelResource(AstralLantern animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(AstralLantern animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(AstralLantern animatable) {
        return ANIMATIONS;
    }
}
