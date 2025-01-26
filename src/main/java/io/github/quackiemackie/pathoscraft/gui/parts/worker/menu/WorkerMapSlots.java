package io.github.quackiemackie.pathoscraft.gui.parts.worker.menu;

import io.github.quackiemackie.pathoscraft.network.payload.worker.UpdateWorkerStationSingleMap;
import io.github.quackiemackie.pathoscraft.util.worker.FilledMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;

public class WorkerMapSlots extends SlotItemHandler {
    private final Player player;

    public WorkerMapSlots(IItemHandler itemHandler, int index, int xPosition, int yPosition, Player player) {
        super(itemHandler, index, xPosition, yPosition);
        this.player = player;
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return super.mayPickup(playerIn);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return Items.FILLED_MAP.equals(stack.getItem());
    }

    @Override
    public void setChanged() {
        super.setChanged();

        ItemStack stack = this.getItem();
        int mapId = stack.isEmpty() || !stack.has(DataComponents.MAP_ID)
                ? -1  // No map present in the slot
                : stack.get(DataComponents.MAP_ID).id();

        int slotIndex = this.getSlotIndex();

        FilledMap filledMap = new FilledMap(slotIndex, mapId);

        if (this.player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new UpdateWorkerStationSingleMap(filledMap));
        }
    }
}
