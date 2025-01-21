package io.github.quackiemackie.pathoscraft.item.model;

import net.minecraft.resources.ResourceLocation;
import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.item.items.misc.AstralLantern;
import software.bernie.geckolib.model.GeoModel;

public class AstralLanternModel extends GeoModel<AstralLantern> {

    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "geo/item/astral_lantern.geo.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "textures/item/astral_lantern.png");
    private static final ResourceLocation ANIMATIONS = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "animations/item/astral_lantern.animation.json");

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
