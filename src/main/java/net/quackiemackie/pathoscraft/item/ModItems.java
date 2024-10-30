package net.quackiemackie.pathoscraft.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.component.ModDataComponents;
import net.quackiemackie.pathoscraft.item.advanced.JumpWand;
import net.quackiemackie.pathoscraft.item.advanced.ManaPotion;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PathosCraft.MOD_ID);

    //Ingredients
    public static final DeferredItem<Item> RAW_SADNESS = ITEMS.register("raw_sadness", () ->
            new Item(new Item.Properties()));

    public static final DeferredItem<Item> SADNESS_INGOT = ITEMS.register("sadness_ingot", () ->
            new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAW_SUNNY = ITEMS.register("raw_sunny", () ->
            new Item(new Item.Properties()));

    public static final DeferredItem<Item> SUNNY_INGOT = ITEMS.register("sunny_ingot", () ->
            new Item(new Item.Properties()));


    //Tools
    public static final DeferredItem<Item> JUMP_WAND = ITEMS.register("jump_wand", () ->
            new JumpWand(new Item.Properties().durability(100)));

    // This was just to see if I can make it work. I didn't plan on using it yet.
    public static final DeferredItem<ArrowItem> ARROW_TEST = ITEMS.register("arrow_test", () ->
            new ArrowItem(new Item.Properties().stacksTo(16)));


    //Edible / Drinkable
    public static final DeferredItem<Item> MANA_HERB = ITEMS.register("mana_herb", () ->
            new Item(new Item.Properties().food(ModFoodProperties.MANA_HERB)) {
                @Override
                public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("item.pathoscraft.mana_herb.tooltip"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });

    public static final DeferredItem<Item> MANA_POTION = ITEMS.register("mana_potion", () ->
            new ManaPotion(new Item.Properties().food(ModFoodProperties.MANA_POTION)) {
                @Override
                public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("item.pathoscraft.mana_potion.tooltip"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });

    public static void register(IEventBus modEventBus){
        ITEMS.register(modEventBus);
    }
}
