package io.github.quackiemackie.pathoscraft.gui.menu;

import io.github.quackiemackie.pathoscraft.gui.PathosMenu;
import io.github.quackiemackie.pathoscraft.gui.parts.worker.WorkerHireSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class WorkerHireMenu extends AbstractContainerMenu implements Supplier<Map<Integer, Slot>> {
    private final Map<Integer, Slot> customSlots = new HashMap<>();

    public WorkerHireMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        super(PathosMenu.WORKER_HIRE_MENU.get(), id);
        IItemHandler internal = new ItemStackHandler(5);

        addWorkerSlots(internal, 80, 18);
        addPlayerInventory(inventory, 8, 50);
    }

    private void addWorkerSlots(IItemHandler inventory, int x, int y) {
        int slotSpacing = 18;
        inventory.insertItem(1, new ItemStack(Items.ACACIA_FENCE), false);

        this.customSlots.put(0, this.addSlot(new WorkerHireSlot(inventory, 0, x - 2 * slotSpacing, y)));
        this.customSlots.put(1, this.addSlot(new WorkerHireSlot(inventory, 1, x - slotSpacing, y)));
        this.customSlots.put(2, this.addSlot(new WorkerHireSlot(inventory, 2, x, y)));
        this.customSlots.put(3, this.addSlot(new WorkerHireSlot(inventory, 3, x + slotSpacing, y)));
        this.customSlots.put(4, this.addSlot(new WorkerHireSlot(inventory, 4, x + 2 * slotSpacing, y)));
    }

    private void addPlayerInventory(Inventory playerInventory, int x, int y) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, x + col * 18, y + row * 18));
            }
        }

        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, x + col * 18, y + 58));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    protected boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        return false;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
    }

    @Override
    public Map<Integer, Slot> get() {
        return customSlots;
    }
}
