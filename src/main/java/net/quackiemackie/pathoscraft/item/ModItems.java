package net.quackiemackie.pathoscraft.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.quackiemackie.pathoscraft.PathosCraft;

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

    public static void register(IEventBus modEventBus){
        ITEMS.register(modEventBus);
    }
}
