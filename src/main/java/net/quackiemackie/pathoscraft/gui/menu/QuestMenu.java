package net.quackiemackie.pathoscraft.gui.menu;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;

public class QuestMenu extends AbstractContainerMenu implements Supplier<Map<Integer, Slot>> {
    protected QuestMenu(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    @Override
    public Map<Integer, Slot> get() {
        return Map.of();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }
}