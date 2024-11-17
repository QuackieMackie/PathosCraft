package net.quackiemackie.pathoscraft.gui.parts;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.Container;
import net.neoforged.neoforge.items.IItemHandler;

public class QuestSlot extends Slot {
    private final IItemHandler itemHandler;
    private final int index;
    private final boolean canPickup;
    private static final Container DUMMY_CONTAINER = new Container() {
        @Override
        public void clearContent() {}

        @Override
        public int getContainerSize() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public ItemStack getItem(int slot) {
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack removeItem(int slot, int amount) {
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack removeItemNoUpdate(int slot) {
            return ItemStack.EMPTY;
        }

        @Override
        public void setItem(int slot, ItemStack stack) {}

        @Override
        public void setChanged() {}

        @Override
        public boolean stillValid(Player player) {
            return false;
        }
    };

    public QuestSlot(IItemHandler itemHandler, int index, int x, int y, boolean canPickup) {
        super(DUMMY_CONTAINER, index, x, y);
        this.itemHandler = itemHandler;
        this.index = index;
        this.canPickup = canPickup;
    }

    @Override
    public ItemStack getItem() {
        return this.itemHandler.getStackInSlot(index);
    }

    @Override
    public void set(ItemStack stack) {
        this.itemHandler.insertItem(index, stack, false);  // Added false for simulate parameter
        this.setChanged();
    }

    @Override
    public ItemStack remove(int amount) {
        ItemStack stack = this.itemHandler.getStackInSlot(index);
        return stack.isEmpty() ? ItemStack.EMPTY : this.itemHandler.extractItem(index, amount, false);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return this.itemHandler.isItemValid(index, stack);
    }

    @Override
    public boolean hasItem() {
        return !this.itemHandler.getStackInSlot(index).isEmpty();
    }

    @Override
    public boolean mayPickup(Player player) {
        return canPickup && !this.itemHandler.extractItem(index, 1, true).isEmpty();
    }

    @Override
    public void setChanged() {
        // No action required here. If needed, you could implement your logic here.
    }
}