package io.github.quackiemackie.pathoscraft.block.entity;

import io.github.quackiemackie.pathoscraft.block.PathosBlockEntities;
import io.github.quackiemackie.pathoscraft.gui.menu.WorkerMainMenu;
import io.github.quackiemackie.pathoscraft.registers.PathosDataComponents;
import io.github.quackiemackie.pathoscraft.util.worker.WorkerNodeList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class WorkerStationBE extends BlockEntity implements MenuProvider {
    private static final Random RANDOM = new Random();
    private static final int MIN_WORKER_NODES = 3;
    private static final int MAX_WORKER_NODES = 5;

    private final static int SIZE = 9;
    public final ItemStackHandler inventory = new ItemStackHandler(SIZE) {
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    public WorkerStationBE(BlockPos blockPos, BlockState blockState) {
        super(PathosBlockEntities.WORKER_STATION_BE.get(), blockPos, blockState);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        addWorkerNodes();
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for(int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("inventory", inventory.serializeNBT(provider));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        inventory.deserializeNBT(provider, tag.getCompound("inventory"));
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Worker Station");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new WorkerMainMenu(id, playerInventory, this);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveWithoutMetadata(provider);
    }

    public Map<Integer, Integer> getSlotMapData() {
        Map<Integer, Integer> slotData = new HashMap<>();
        for (int slot = 0; slot < inventory.getSlots(); slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (!stack.isEmpty() && stack.getItem() instanceof MapItem) {
                if (stack.has(DataComponents.MAP_ID)) {
                    int mapId = stack.get(DataComponents.MAP_ID).id();
                    slotData.put(slot, mapId);
                }
            }
        }
        return slotData;
    }

    public void addWorkerNodes() {
        if (this.level == null || this.level.isClientSide()) {
            return;
        }

        if (this.components().has(PathosDataComponents.WORKER_NODE_DATA.get())) {
            return;
        }

        int nodeCount = RANDOM.nextInt(MAX_WORKER_NODES - MIN_WORKER_NODES + 1) + MIN_WORKER_NODES;
        List<WorkerNodeList.WorkerNode> generatedNodes = new ArrayList<>(nodeCount);

        for (int i = 0; i < nodeCount; i++) {
            int x;
            int y;
            boolean valid;

            do {
                x = RANDOM.nextInt(740 - 40 + 1) + 40;
                y = RANDOM.nextInt(740 - 40 + 1) + 40;
                valid = true;

                for (WorkerNodeList.WorkerNode node : generatedNodes) {
                    int deltaX = x - node.x();
                    int deltaY = y - node.y();
                    if (Math.sqrt(deltaX * deltaX + deltaY * deltaY) < 50) {
                        valid = false;
                        break;
                    }
                }
            } while (!valid);

            generatedNodes.add(new WorkerNodeList.WorkerNode(x, y));
        }

        WorkerNodeList workerNodeList = new WorkerNodeList(generatedNodes);

        DataComponentMap dataComponents = DataComponentMap.builder().set(PathosDataComponents.WORKER_NODE_DATA.get(), workerNodeList).build();
        this.setComponents(dataComponents);
    }

    public WorkerNodeList getWorkerNodes() {
        if (this.components().has(PathosDataComponents.WORKER_NODE_DATA.get())) {
            return this.components().get(PathosDataComponents.WORKER_NODE_DATA.get());
        } else {
            return new WorkerNodeList(Collections.emptyList());
        }
    }
}