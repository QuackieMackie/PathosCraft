package net.quackiemackie.pathoscraft.enchantment;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.quackiemackie.pathoscraft.PathosCraft;

public class PathosEnchantment {
    public static final ResourceKey<Enchantment> EXCAVATION_MINIGAME = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "excavation_minigame"));
    public static final ResourceKey<Enchantment> FISHING_MINIGAME = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "fishing_minigame"));

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<DamageType> damageTypes = context.lookup(Registries.DAMAGE_TYPE);
        HolderGetter<Enchantment> enchantments = context.lookup(Registries.ENCHANTMENT);
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);

        register(context, EXCAVATION_MINIGAME, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.PICKAXES),
                        5,
                        1,
                        Enchantment.dynamicCost(5, 7),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.MAINHAND)));

        register(context, FISHING_MINIGAME, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.FISHING_ENCHANTABLE),
                        5,
                        1,
                        Enchantment.dynamicCost(5, 7),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.MAINHAND)));
    }

    private static void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key,
                                 Enchantment.Builder builder) {
        registry.register(key, builder.build(key.location()));
    }
}
