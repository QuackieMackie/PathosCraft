package net.quackiemackie.pathoscraft.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import net.quackiemackie.pathoscraft.util.ModTags;

public class ModToolTiers {
    public static final Tier SADNESS = new SimpleTier(ModTags.Blocks.INCORRECT_FOR_SADNESS_TOOL,
            1200, 4f, 3f, 20, () -> Ingredient.of(ModItems.SADNESS_INGOT.get()));

}
