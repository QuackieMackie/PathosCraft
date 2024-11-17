package net.quackiemackie.pathoscraft.item.advanced;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

// check for the entity captured data (need to still figure that out)
//stack.has(PathosDataComponents.ENTITY_CAPTURED_DATA)

//This item is going to be used to pick up certain entities (thinking Monster and Animals)
//and store the data in the item, and place that entity back down.
//Essentially a poke ball but crystal \o/

public class CreatureCrystal extends Item {

    public CreatureCrystal(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (!level.isClientSide && player != null) {
            if (!stack.hasFoil()) {
                stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
            } else if (stack.hasFoil()) {
                stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);
            }
        }

        return super.useOn(context);
    }
}