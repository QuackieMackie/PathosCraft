package net.quackiemackie.pathoscraft.gui.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.quackiemackie.pathoscraft.gui.PathosMenu;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

//MCreator generated the base code for this, and I've gone through and used it as a jumping off point.

public class QuestMenu extends AbstractContainerMenu implements Supplier<Map<Integer, Slot>> {
    public final Level world;
    public final Player entity;
    public int x, y, z;
    private ContainerLevelAccess access = ContainerLevelAccess.NULL;
    private IItemHandler internal;
    private final Map<Integer, Slot> customSlots = new HashMap<>();
    private boolean bound = false;
    private Supplier<Boolean> boundItemMatcher = null;
    private Entity boundEntity = null;
    private BlockEntity boundBlockEntity = null;

    public QuestMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        super(PathosMenu.QUEST_MENU.get(), id);
        this.entity = inventory.player;
        this.world = inventory.player.level();
        this.internal = new ItemStackHandler(32);
        BlockPos pos = null;
        if (extraData != null) {
            pos = extraData.readBlockPos();
            this.x = pos.getX();
            this.y = pos.getY();
            this.z = pos.getZ();
            access = ContainerLevelAccess.create(world, pos);

            if (extraData.readableBytes() == 1) { // bound to item
                byte hand = extraData.readByte();
                ItemStack itemStack = hand == 0 ? this.entity.getMainHandItem() : this.entity.getOffhandItem();
                this.boundItemMatcher = () -> itemStack == (hand == 0 ? this.entity.getMainHandItem() : this.entity.getOffhandItem());
                IItemHandler cap = itemStack.getCapability(Capabilities.ItemHandler.ITEM);
                if (cap != null) {
                    this.internal = cap;
                    this.bound = true;
                }
            } else if (extraData.readableBytes() > 1) { // bound to entity
                extraData.readByte(); // drop padding
                boundEntity = world.getEntity(extraData.readVarInt());
                if (boundEntity != null) {
                    IItemHandler cap = boundEntity.getCapability(Capabilities.ItemHandler.ENTITY);
                    if (cap != null) {
                        this.internal = cap;
                        this.bound = true;
                    }
                }
            } else { // might be bound to block
                boundBlockEntity = this.world.getBlockEntity(pos);
                if (boundBlockEntity instanceof BaseContainerBlockEntity baseContainerBlockEntity) {
                    this.internal = new InvWrapper(baseContainerBlockEntity);
                    this.bound = true;
                }
            }
        }

        // Quest Slots
        for (int slotIndexY = 0; slotIndexY < 11; slotIndexY++) {
            for (int slotIndexX = 0; slotIndexX < 9; slotIndexX++) {
                this.addSlot(new Slot(inventory, slotIndexX + (slotIndexY + 1) * 9, 8 + slotIndexX * 18, 18 + slotIndexY * 18));
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.bound) {
            if (this.boundItemMatcher != null) {
                return this.boundItemMatcher.get();
            } else if (this.boundBlockEntity != null) {
                return AbstractContainerMenu.stillValid(this.access, player, this.boundBlockEntity.getBlockState().getBlock());
            } else if (this.boundEntity != null) {
                return this.boundEntity.isAlive();
            }
        }
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotItemStack = slot.getItem();
            itemStack = slotItemStack.copy();

            int inventorySize = this.slots.size();
            int internalSize = internal.getSlots();

            if (index < internalSize) {
                if (!this.moveItemStackTo(slotItemStack, internalSize, inventorySize, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotItemStack, itemStack);
            } else if (!this.moveItemStackTo(slotItemStack, 0, internalSize, false)) {
                int playerInventoryStart = internalSize + 27;
                int playerInventoryEnd = playerInventoryStart + 9;

                if (index < playerInventoryStart) {
                    if (!this.moveItemStackTo(slotItemStack, playerInventoryStart, inventorySize, true)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(slotItemStack, internalSize, playerInventoryStart, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (slotItemStack.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (slotItemStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, slotItemStack);
        }
        return itemStack;
    }

    @Override
    protected boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean success = false;
        int currentIndex = startIndex;

        if (reverseDirection) {
            currentIndex = endIndex - 1;
        }

        if (stack.isStackable()) {
            while (!stack.isEmpty() && (reverseDirection ? currentIndex >= startIndex : currentIndex < endIndex)) {
                Slot slot = this.slots.get(currentIndex);
                ItemStack currentStack = slot.getItem();

                if (slot.mayPlace(currentStack) && !currentStack.isEmpty() && ItemStack.isSameItemSameComponents(stack, currentStack)) {
                    int combinedCount = currentStack.getCount() + stack.getCount();
                    int maxStackSize = slot.getMaxStackSize(currentStack);

                    if (combinedCount <= maxStackSize) {
                        stack.setCount(0);
                        currentStack.setCount(combinedCount);
                        slot.set(currentStack);
                        success = true;
                    } else if (currentStack.getCount() < maxStackSize) {
                        stack.shrink(maxStackSize - currentStack.getCount());
                        currentStack.setCount(maxStackSize);
                        slot.set(currentStack);
                        success = true;
                    }
                }

                if (reverseDirection) {
                    currentIndex--;
                } else {
                    currentIndex++;
                }
            }
        }

        if (!stack.isEmpty()) {
            if (reverseDirection) {
                currentIndex = endIndex - 1;
            } else {
                currentIndex = startIndex;
            }

            while (reverseDirection ? currentIndex >= startIndex : currentIndex < endIndex) {
                Slot emptySlot = this.slots.get(currentIndex);
                ItemStack emptyStack = emptySlot.getItem();

                if (emptyStack.isEmpty() && emptySlot.mayPlace(stack)) {
                    int maxStackSize = emptySlot.getMaxStackSize(stack);
                    emptySlot.setByPlayer(stack.split(Math.min(stack.getCount(), maxStackSize)));
                    emptySlot.setChanged();
                    success = true;
                    break;
                }

                if (reverseDirection) {
                    currentIndex--;
                } else {
                    currentIndex++;
                }
            }
        }
        return success;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!bound && player instanceof ServerPlayer serverPlayer) {
            if (!serverPlayer.isAlive() || serverPlayer.hasDisconnected()) {
                for (int slotIndex = 0; slotIndex < internal.getSlots(); slotIndex++) {
                    player.drop(internal.getStackInSlot(slotIndex), false);
                    if (internal instanceof IItemHandlerModifiable modifiableHandler) {
                        modifiableHandler.setStackInSlot(slotIndex, ItemStack.EMPTY);
                    }
                }
            } else {
                for (int slotIndex = 0; slotIndex < internal.getSlots(); slotIndex++) {
                    player.getInventory().placeItemBackInInventory(internal.getStackInSlot(slotIndex));
                    if (internal instanceof IItemHandlerModifiable modifiableHandler) {
                        modifiableHandler.setStackInSlot(slotIndex, ItemStack.EMPTY);
                    }
                }
            }
        }
    }

    @Override
    public Map<Integer, Slot> get() {
        return customSlots;
    }
}