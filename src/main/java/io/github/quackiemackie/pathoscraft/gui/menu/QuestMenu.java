package io.github.quackiemackie.pathoscraft.gui.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import io.github.quackiemackie.pathoscraft.gui.PathosMenu;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class QuestMenu extends AbstractContainerMenu implements Supplier<Map<Integer, Slot>> {
    private final IItemHandler internal;
    private final Map<Integer, Slot> customSlots = new HashMap<>();

    public QuestMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        super(PathosMenu.QUEST_MENU.get(), id);
        this.internal = new ItemStackHandler(99);
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