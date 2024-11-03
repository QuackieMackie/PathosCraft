package net.quackiemackie.pathoscraft.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.entity.advanced.AstralFormEntity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, PathosCraft.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<AstralFormEntity>> ASTRAL_FORM = ENTITY_TYPES.register("astral_form", () ->
            EntityType.Builder.of(AstralFormEntity::new, MobCategory.AMBIENT)
                .sized(1f, 2.0f)
                .build("astral_form")
    );

    public static void register(IEventBus modEventBus) {
        ENTITY_TYPES.register(modEventBus);
    }
}