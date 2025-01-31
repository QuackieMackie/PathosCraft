package io.github.quackiemackie.pathoscraft.gui.menu.parts;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class FilledMapSlot extends SlotItemHandler {

    public FilledMapSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return Items.FILLED_MAP.equals(stack.getItem());
    }
}
