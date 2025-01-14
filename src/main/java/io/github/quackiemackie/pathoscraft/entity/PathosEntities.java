package io.github.quackiemackie.pathoscraft.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.entity.entity.AstralFormEntity;
import io.github.quackiemackie.pathoscraft.entity.entity.MushroomEntity;
import io.github.quackiemackie.pathoscraft.entity.entity.SpiderMountEntity;

import java.util.function.Supplier;

public class PathosEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, PathosCraft.MOD_ID);

    public static final Supplier<EntityType<AstralFormEntity>> ASTRAL_FORM = ENTITY_TYPES.register("astral_form", () -> EntityType.Builder.of(AstralFormEntity::new, MobCategory.CREATURE).sized(1f, 2.0f).build("astral_form"));
    public static final Supplier<EntityType<SpiderMountEntity>> SPIDER_MOUNT = ENTITY_TYPES.register("spider_mount", () -> EntityType.Builder.of(SpiderMountEntity::new, MobCategory.CREATURE).sized(1f, 2.0f).build("spider_mount"));
    public static final Supplier<EntityType<MushroomEntity>> MUSHROOM = ENTITY_TYPES.register("mushroom", () -> EntityType.Builder.of(MushroomEntity::new, MobCategory.CREATURE).sized(0.6f, 1.2f).build("mushroom"));
}
