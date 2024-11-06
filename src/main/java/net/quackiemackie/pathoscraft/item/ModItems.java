package net.quackiemackie.pathoscraft.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.block.ModBlocks;
import net.quackiemackie.pathoscraft.item.advanced.AnimatedItem;
import net.quackiemackie.pathoscraft.item.advanced.AstralLantern;
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

    public static final DeferredItem<SwordItem> SADNESS_SWORD = ITEMS.register("sadness_sword", () ->
            new SwordItem(ModToolTiers.SADNESS, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModToolTiers.SADNESS, 5, -1.4F))));

    public static final DeferredItem<PickaxeItem> SADNESS_PICKAXE = ITEMS.register("sadness_pickaxe", () ->
            new PickaxeItem(ModToolTiers.SADNESS, new Item.Properties()
                    .attributes(PickaxeItem.createAttributes(ModToolTiers.SADNESS, 5, -1.4F))));

    public static final DeferredItem<AxeItem> SADNESS_AXE = ITEMS.register("sadness_axe", () ->
            new AxeItem(ModToolTiers.SADNESS, new Item.Properties()
                    .attributes(AxeItem.createAttributes(ModToolTiers.SADNESS, 5, -1.4F))));

    public static final DeferredItem<ShovelItem> SADNESS_SHOVEL = ITEMS.register("sadness_shovel", () ->
            new ShovelItem(ModToolTiers.SADNESS, new Item.Properties()
                    .attributes(ShovelItem.createAttributes(ModToolTiers.SADNESS, 5, -1.4F))));

    public static final DeferredItem<HoeItem> SADNESS_HOE = ITEMS.register("sadness_hoe", () ->
            new HoeItem(ModToolTiers.SADNESS, new Item.Properties()
                    .attributes(HoeItem.createAttributes(ModToolTiers.SADNESS, 5, -1.4F))));

    public static final DeferredItem<Item> ASTRAL_LANTERN = ITEMS.register("astral_lantern", () ->
            new AstralLantern(new Item.Properties().durability(25).stacksTo(1)));


    //Edible / Drinkable
    public static final DeferredItem<Item> MANA_HERB = ITEMS.register("mana_herb", () ->
            new Item(new Item.Properties().food(ModFoodProperties.MANA_HERB)) {
                @Override
                public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("item.pathoscraft.mana_herb.tooltip"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });

    //Tests
    public static final DeferredItem<ArrowItem> ARROW_TEST = ITEMS.register("arrow_test", () ->
            new ArrowItem(new Item.Properties().stacksTo(16)));

//    public static final DeferredItem<Item> SEED_TEST = ITEMS.register("seed_test", () ->
//            new ItemNameBlockItem(ModBlocks.SEED_TEST_BLOCK.get(), new Item.Properties().stacksTo(5)));

    public static final DeferredItem<Item> MANA_POTION = ITEMS.register("mana_potion", () ->
            new ManaPotion(new Item.Properties().food(ModFoodProperties.MANA_POTION)) {
                @Override
                public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("item.pathoscraft.mana_potion.tooltip"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });

    public static final DeferredItem<Item> ANIMATED_ITEM = ITEMS.register("animated_item", () ->
            new AnimatedItem(new Item.Properties()));

    public static void register(IEventBus modEventBus){
        ITEMS.register(modEventBus);
    }
}
