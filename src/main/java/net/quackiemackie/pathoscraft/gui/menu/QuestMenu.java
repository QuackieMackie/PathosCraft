package net.quackiemackie.pathoscraft.gui.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.quackiemackie.pathoscraft.gui.PathosMenu;
import net.quackiemackie.pathoscraft.gui.parts.QuestSlot;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class QuestMenu extends AbstractContainerMenu implements Supplier<Map<Integer, Slot>> {
    private final IItemHandler internal;
    private final Map<Integer, Slot> customSlots = new HashMap<>();

    public QuestMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        super(PathosMenu.QUEST_MENU.get(), id);
        this.internal = new ItemStackHandler(99);

        initQuestSlots();
    }

    private void initQuestSlots() {
        for (int slotIndex = 0; slotIndex < internal.getSlots(); slotIndex++) {
            int x = 8 + (slotIndex % 9) * 18;
            int y = 18 + (slotIndex / 9) * 18;
            boolean canPickup = false;
            Slot slot = this.addSlot(new QuestSlot(internal, slotIndex, x, y, canPickup));
           customSlots.put(slotIndex, slot);
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