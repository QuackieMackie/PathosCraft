package io.github.quackiemackie.pathoscraft.gui.menu;

import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.block.entity.WorkerStationEntity;
import io.github.quackiemackie.pathoscraft.gui.PathosMenu;
import io.github.quackiemackie.pathoscraft.gui.parts.worker.menu.WorkerMapSlots;
import io.github.quackiemackie.pathoscraft.network.payload.worker.UpdateWorkerStationMapData;
import io.github.quackiemackie.pathoscraft.util.handlers.workers.WorkerPayloadHandler;
import io.github.quackiemackie.pathoscraft.util.worker.WorkerStationMaps;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class WorkerMainMenu extends AbstractContainerMenu implements Supplier<Map<Integer, Slot>> {
    private final Map<Integer, Slot> mapSlots = new HashMap<>();

    public WorkerMainMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        super(PathosMenu.WORKER_MAIN_MENU.get(), id);
        IItemHandler internal = new ItemStackHandler(9);

        if (extraData != null) {
            BlockPos blockPos = extraData.readBlockPos();
            BlockEntity blockEntity = inventory.player.level().getBlockEntity(blockPos);
            if (blockEntity instanceof WorkerStationEntity workerStationEntity) {
                internal = workerStationEntity.getItemHandler();
            }
        }

        addMapSlots(internal, inventory.player);
        addPlayerInventory(inventory, 8, 140);

        if (inventory.player instanceof ServerPlayer serverPlayer) {
            WorkerStationMaps currentMapData = WorkerPayloadHandler.getWorkerMapData();
            if (currentMapData != null) {
                PacketDistributor.sendToPlayer(serverPlayer, new UpdateWorkerStationMapData(currentMapData));
                PathosCraft.LOGGER.info("Sent full Worker Station Map data to client on menu close.");
            }
        }
    }

    private void addMapSlots(IItemHandler inventory, Player player) {
        this.mapSlots.put(0, this.addSlot(new WorkerMapSlots(inventory, 0, 62, 36, player)));
        this.mapSlots.put(1, this.addSlot(new WorkerMapSlots(inventory, 1, 80, 36, player)));
        this.mapSlots.put(2, this.addSlot(new WorkerMapSlots(inventory, 2, 98, 36, player)));

        this.mapSlots.put(3, this.addSlot(new WorkerMapSlots(inventory, 3, 62, 54, player)));
        this.mapSlots.put(4, this.addSlot(new WorkerMapSlots(inventory, 4, 80, 54, player)));
        this.mapSlots.put(5, this.addSlot(new WorkerMapSlots(inventory, 5, 98, 54, player)));

        this.mapSlots.put(6, this.addSlot(new WorkerMapSlots(inventory, 6, 62, 72, player)));
        this.mapSlots.put(7, this.addSlot(new WorkerMapSlots(inventory, 7, 80, 72, player)));
        this.mapSlots.put(8, this.addSlot(new WorkerMapSlots(inventory, 8, 98, 72, player)));
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
        return mapSlots;
    }


}