package io.github.quackiemackie.pathoscraft.item.model;

import net.minecraft.resources.ResourceLocation;
import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.item.items.tools.AnimatedItem;
import software.bernie.geckolib.model.GeoModel;

public class AnimatedItemModel extends GeoModel<AnimatedItem> {

    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "geo/item/animated_item.geo.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "textures/item/animated_item.png");
    private static final ResourceLocation ANIMATIONS = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "animations/item/animated_item.animation.json");

    @Override
    public ResourceLocation getModelResource(AnimatedItem animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(AnimatedItem animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(AnimatedItem animatable) {
        return ANIMATIONS;
    }
}
