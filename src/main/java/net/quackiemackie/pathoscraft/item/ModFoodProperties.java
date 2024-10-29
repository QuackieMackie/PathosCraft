package net.quackiemackie.pathoscraft.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoodProperties {

    public static final FoodProperties MANA_HERB = new FoodProperties.Builder().nutrition(4).saturationModifier(2).alwaysEdible().effect(() ->
            new MobEffectInstance(MobEffects.REGENERATION, 20*5, 1), 50).build();

    public static final FoodProperties MANA_POTION = new FoodProperties.Builder().nutrition(4).saturationModifier(2).alwaysEdible().effect(() ->
            new MobEffectInstance(MobEffects.REGENERATION, 20*5, 1), 100).fast().build();
}