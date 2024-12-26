package net.quackiemackie.pathoscraft.registers;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.event.server.EnchantEvents;
import net.quackiemackie.pathoscraft.item.advanced.CreatureCrystal;

import java.util.function.UnaryOperator;

/**
 * Handles the registration and management of mod data components for the PathosCraft mod.
 */
public class PathosDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPE = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, PathosCraft.MOD_ID);
    private static <T>DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPE.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    ///A Boolean value used to determine if the logic for the minigame should be able to run or if it's inactive.
    /// Minigame logic here: [EnchantEvents]
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> ACTIVE_MINIGAME_BOOL = register("active_minigame_bool", builder -> builder.persistent(Codec.BOOL));
    ///A future data component, that will store entity data, so they can be captures in the [CreatureCrystal]
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> ENTITY_CAPTURED_DATA = register("captured_entity_data", builder -> builder.persistent(CompoundTag.CODEC));
}
