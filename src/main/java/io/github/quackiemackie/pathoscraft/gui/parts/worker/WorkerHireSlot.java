package io.github.quackiemackie.pathoscraft.gui.parts.worker;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class WorkerHireSlot extends SlotItemHandler {
    public WorkerHireSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return super.mayPickup(playerIn);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }
}
