package io.github.quackiemackie.pathoscraft.gui.menu;

import io.github.quackiemackie.pathoscraft.block.PathosBlocks;
import io.github.quackiemackie.pathoscraft.block.entity.WorkerStationBlockEntity;
import io.github.quackiemackie.pathoscraft.gui.PathosMenu;
import io.github.quackiemackie.pathoscraft.gui.menu.parts.FilledMapSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Map;

public class WorkerMainMenu extends AbstractContainerMenu {
    public final WorkerStationBlockEntity blockEntity;
    private final Level level;

    public WorkerMainMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public WorkerMainMenu(int pContainerId, Inventory inv, BlockEntity blockEntity) {
        super(PathosMenu.WORKER_MAIN_MENU.get(), pContainerId);
        this.blockEntity = ((WorkerStationBlockEntity) blockEntity);
        this.level = inv.player.level();

        addPlayerInventory(inv, 8, 140);

        this.addSlot(new FilledMapSlot(this.blockEntity.inventory, 0, 62, 36));
        this.addSlot(new FilledMapSlot(this.blockEntity.inventory, 1, 80, 36));
        this.addSlot(new FilledMapSlot(this.blockEntity.inventory, 2, 98, 36));

        this.addSlot(new FilledMapSlot(this.blockEntity.inventory, 3, 62, 54));
        this.addSlot(new FilledMapSlot(this.blockEntity.inventory, 4, 80, 54));
        this.addSlot(new FilledMapSlot(this.blockEntity.inventory, 5, 98, 54));

        this.addSlot(new FilledMapSlot(this.blockEntity.inventory, 6, 62, 72));
        this.addSlot(new FilledMapSlot(this.blockEntity.inventory, 7, 80, 72));
        this.addSlot(new FilledMapSlot(this.blockEntity.inventory, 8, 98, 72));
        
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
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = this.slots.get(index);
        if (!sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        int blockInventorySize = this.blockEntity.inventory.getSlots();
        int playerInventoryEnd = blockInventorySize + 36;

        if (index < blockInventorySize) {
            if (!this.moveItemStackTo(sourceStack, blockInventorySize, playerInventoryEnd, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (!this.moveItemStackTo(sourceStack, 0, blockInventorySize, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (sourceStack.isEmpty()) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }

        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer, PathosBlocks.WORKER_STATION_BLOCK.get());
    }

    public Map<Integer, Integer> getSlotMapData() {
        return blockEntity.getSlotMapData();
    }
}