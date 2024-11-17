package net.quackiemackie.pathoscraft.gui.menu;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.quackiemackie.pathoscraft.gui.PathosMenu;
import net.quackiemackie.pathoscraft.gui.parts.QuestSlot;
import net.quackiemackie.pathoscraft.handlers.QuestHandler;
import net.quackiemackie.pathoscraft.quest.Quest;
import net.quackiemackie.pathoscraft.quest.QuestObjective;
import net.quackiemackie.pathoscraft.quest.QuestReward;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        List<Quest> questsByType = QuestHandler.getQuestsByType(0);
        for (Quest quest : questsByType) {
            int slotIndex = quest.getQuestSlot();
            ItemStack stack = createQuestItemStack(quest);
            internal.insertItem(slotIndex, stack, false);
        }

        for (int slotIndex = 0; slotIndex < internal.getSlots(); slotIndex++) {
            int x = 8 + (slotIndex % 9) * 18;
            int y = 18 + (slotIndex / 9) * 18;
            boolean canPickup = false;
            Slot slot = this.addSlot(new QuestSlot(internal, slotIndex, x, y, canPickup));
            customSlots.put(slotIndex, slot);
        }
    }

    private ItemStack createQuestItemStack(Quest quest) {
        String[] iconParts = quest.getQuestIcon().split(":");
        String namespace = iconParts.length > 1 ? iconParts[0] : "minecraft";
        String path = iconParts.length > 1 ? iconParts[1] : iconParts[0];

        ResourceLocation questIcon = ResourceLocation.fromNamespaceAndPath(namespace, path);
        Item item = BuiltInRegistries.ITEM.get(questIcon);

        if (item == null) {
            item = Items.MAP;
        }

        ItemStack stack = new ItemStack(item);
        stack.set(DataComponents.CUSTOM_NAME, Component.literal("§a" + quest.getQuestName()));
        stack.set(DataComponents.LORE, createQuestLore(quest));
        stack.setCount(1);
        return stack;
    }

    private ItemLore createQuestLore(Quest quest) {
        List<Component> loreComponents = new ArrayList<>();
        loreComponents.add(Component.literal("§7Description: §f" + quest.getQuestDescription()));
        loreComponents.add(Component.literal("§7Objectives:"));

        for (QuestObjective objective : quest.getQuestObjectives()) {
            String target = objective.getTarget().substring(objective.getTarget().indexOf(':') + 1).replace('_', ' ');
            loreComponents.add(Component.literal("  §8- §f" + objective.getAction() + " " + objective.getQuantity() + " " + target));
        }

        loreComponents.add(Component.literal("§7Rewards:"));

        for (QuestReward reward : quest.getQuestRewards()) {
            loreComponents.add(Component.literal("  §8- §f" + reward.getQuantity() + " " + reward.getItem().substring(reward.getItem().indexOf(':') + 1).replace('_', ' ')));
        }
        loreComponents.add(Component.literal(""));
        loreComponents.add(Component.literal("§7Type: §f" + (quest.getQuestType() == 0 ? "Main Quest" : "Side Quest")));
        loreComponents.add(Component.literal("§7Quest ID: §f" + quest.getId()));

        return new ItemLore(loreComponents);
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