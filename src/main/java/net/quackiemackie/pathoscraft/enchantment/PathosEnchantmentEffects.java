package net.quackiemackie.pathoscraft.enchantment;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.enchantment.effects.ExcavationMiniGameEnchantment;
import net.quackiemackie.pathoscraft.enchantment.effects.FishingMiniGameEnchantment;

import java.util.function.Supplier;

public class PathosEnchantmentEffects {
    public static final DeferredRegister<MapCodec<? extends EnchantmentEntityEffect>> ENTITY_ENCHANTMENT_EFFECTS = DeferredRegister.create(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, PathosCraft.MOD_ID);

    public static final Supplier<MapCodec<? extends EnchantmentEntityEffect>> EXCAVATION_MINIGAME = ENTITY_ENCHANTMENT_EFFECTS.register("excavation_minigame", () -> ExcavationMiniGameEnchantment.CODEC);
    public static final Supplier<MapCodec<? extends EnchantmentEntityEffect>> FISHING_MINIGAME = ENTITY_ENCHANTMENT_EFFECTS.register("fishing_minigame", () -> FishingMiniGameEnchantment.CODEC);
}
