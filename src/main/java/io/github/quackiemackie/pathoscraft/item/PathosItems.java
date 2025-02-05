package io.github.quackiemackie.pathoscraft.item;

import io.github.quackiemackie.pathoscraft.item.items.*;
import io.github.quackiemackie.pathoscraft.item.items.consumable.*;
import io.github.quackiemackie.pathoscraft.item.items.misc.*;
import io.github.quackiemackie.pathoscraft.item.items.tools.*;
import io.github.quackiemackie.pathoscraft.item.items.workers.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import io.github.quackiemackie.pathoscraft.PathosCraft;

public class PathosItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PathosCraft.MOD_ID);
    // Ingredients
    public static final DeferredItem<Item> RAW_SADNESS = ITEMS.register("raw_sadness", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SADNESS_INGOT = ITEMS.register("sadness_ingot", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAW_SUNNY = ITEMS.register("raw_sunny", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SUNNY_INGOT = ITEMS.register("sunny_ingot", () -> new Item(new Item.Properties()));
    // Tools
    public static final DeferredItem<Item> JUMP_WAND = ITEMS.register("jump_wand", () -> new JumpWand(new Item.Properties().durability(100)));
    public static final DeferredItem<Item> ASTRAL_LANTERN = ITEMS.register("astral_lantern", () -> new AstralLantern(new Item.Properties().durability(25).stacksTo(1)));
    public static final DeferredItem<SwordItem> SADNESS_SWORD = ITEMS.register("sadness_sword", () -> new SwordItem(PathosToolTiers.SADNESS, new Item.Properties().attributes(SwordItem.createAttributes(PathosToolTiers.SADNESS, 5, -1.4F))));
    public static final DeferredItem<PickaxeItem> SADNESS_PICKAXE = ITEMS.register("sadness_pickaxe", () -> new PickaxeItem(PathosToolTiers.SADNESS, new Item.Properties().attributes(PickaxeItem.createAttributes(PathosToolTiers.SADNESS, 5, -1.4F))));
    public static final DeferredItem<AxeItem> SADNESS_AXE = ITEMS.register("sadness_axe", () -> new AxeItem(PathosToolTiers.SADNESS, new Item.Properties().attributes(AxeItem.createAttributes(PathosToolTiers.SADNESS, 5, -1.4F))));
    public static final DeferredItem<ShovelItem> SADNESS_SHOVEL = ITEMS.register("sadness_shovel", () -> new ShovelItem(PathosToolTiers.SADNESS, new Item.Properties().attributes(ShovelItem.createAttributes(PathosToolTiers.SADNESS, 5, -1.4F))));
    public static final DeferredItem<HoeItem> SADNESS_HOE = ITEMS.register("sadness_hoe", () -> new HoeItem(PathosToolTiers.SADNESS, new Item.Properties().attributes(HoeItem.createAttributes(PathosToolTiers.SADNESS, 5, -1.4F))));
    public static final DeferredItem<BasicFishingRod> BASIC_FISHING_ROD = ITEMS.register("basic_fishing_rod", () -> new BasicFishingRod(new Item.Properties().stacksTo(1).durability(64)));
    // Edible / Drinkable
    public static final DeferredItem<Item> MANA_HERB = ITEMS.register("mana_herb", () -> new Item(new Item.Properties().food(PathosFoodProperties.MANA_HERB)));
    public static final DeferredItem<Item> MANA_POTION = ITEMS.register("mana_potion", () -> new ManaPotion(new Item.Properties().food(PathosFoodProperties.MANA_POTION)));
    // Special
    public static final DeferredItem<Item> QUEST_BOOK = ITEMS.register("quest_book", () -> new QuestBook(new Item.Properties()));
    // Worker
    public static final DeferredItem<Item> NAIVE_WORKER = ITEMS.register("naive_worker", () -> new NaiveWorker(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> NORMAL_WORKER = ITEMS.register("normal_worker", () -> new NormalWorker(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SKILLED_WORKER = ITEMS.register("skilled_worker", () -> new SkilledWorker(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> PROFESSIONAL_WORKER = ITEMS.register("professional_worker", () -> new ProfessionalWorker(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> ARTISAN_WORKER = ITEMS.register("artisan_worker", () -> new ArtisanWorker(new Item.Properties().stacksTo(1)));
    // Tests
    public static final DeferredItem<TestItem> TEST_ITEM = ITEMS.register("test_item", () -> new TestItem(new Item.Properties().stacksTo(1).fireResistant()));
    public static final DeferredItem<ArrowItem> ARROW_TEST = ITEMS.register("arrow_test", () -> new ArrowItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> ANIMATED_ITEM = ITEMS.register("animated_item", () -> new AnimatedItem(new Item.Properties()));
    public static final DeferredItem<Item> CREATURE_CRYSTAL = ITEMS.register("creature_crystal", () -> new CreatureCrystal(new Item.Properties().stacksTo(1)));
}
