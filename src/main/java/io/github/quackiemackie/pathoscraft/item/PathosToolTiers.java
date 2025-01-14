package io.github.quackiemackie.pathoscraft.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import io.github.quackiemackie.pathoscraft.registers.PathosTags;

public class PathosToolTiers {
    public static final Tier SADNESS = new SimpleTier(PathosTags.Blocks.INCORRECT_FOR_SADNESS_TOOL,
            1200, 4f, 3f, 20, () -> Ingredient.of(PathosItems.SADNESS_INGOT.get()));

}
