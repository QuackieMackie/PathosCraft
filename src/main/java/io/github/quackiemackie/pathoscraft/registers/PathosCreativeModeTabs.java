package io.github.quackiemackie.pathoscraft.registers;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.block.PathosBlocks;
import io.github.quackiemackie.pathoscraft.item.PathosItems;

import java.util.function.Supplier;

public class PathosCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PathosCraft.MOD_ID);

    public static final Supplier<CreativeModeTab> PATHOSCRAFT_BLOCKS_TAB = CREATIVE_MODE_TAB.register("pathoscraft_blocks_creative_tab", () ->
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(PathosBlocks.SADNESS_BLOCK.get()))
                    .title(Component.translatable("creative_tab.pathoscraft.blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(PathosBlocks.SADNESS_ORE.get());
                        output.accept(PathosBlocks.SUNNY_ORE.get());
                        output.accept(PathosBlocks.DEEPSLATE_SADNESS_ORE.get());
                        output.accept(PathosBlocks.DEEPSLATE_SUNNY_ORE.get());
                        output.accept(PathosBlocks.SADNESS_BLOCK.get());
                        output.accept(PathosBlocks.REPAIR_BLOCK.get());
                        output.accept(PathosBlocks.WORKER_STATION.get());
                    })
                    .build());

    public static final Supplier<CreativeModeTab> PATHOSCRAFT_INGREDIENTS_TAB = CREATIVE_MODE_TAB.register("pathoscraft_ingredients_creative_tab", () ->
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(PathosItems.RAW_SADNESS.get()))
                    .title(Component.translatable("creative_tab.pathoscraft.ingredients"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(PathosItems.RAW_SADNESS.get());
                        output.accept(PathosItems.RAW_SUNNY.get());
                        output.accept(PathosItems.SADNESS_INGOT.get());
                        output.accept(PathosItems.SUNNY_INGOT.get());
                    })
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "pathoscraft_blocks_creative_tab"))
                    .build());

    public static final Supplier<CreativeModeTab> PATHOSCRAFT_TOOLS_TAB = CREATIVE_MODE_TAB.register("pathoscraft_tools_creative_tab", () ->
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(PathosItems.JUMP_WAND.get()))
                    .title(Component.translatable("creative_tab.pathoscraft.tools"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(PathosItems.JUMP_WAND.get());
                        output.accept(PathosItems.ARROW_TEST.get());
                        output.accept(PathosItems.SADNESS_SWORD.get());
                        output.accept(PathosItems.SADNESS_PICKAXE.get());
                        output.accept(PathosItems.SADNESS_AXE.get());
                        output.accept(PathosItems.SADNESS_SHOVEL.get());
                        output.accept(PathosItems.SADNESS_HOE.get());

                        output.accept(PathosItems.BASIC_FISHING_ROD.get());

                        output.accept(PathosItems.ANIMATED_ITEM.get());
                        output.accept(PathosItems.ASTRAL_LANTERN.get());
                        output.accept(PathosItems.CREATURE_CRYSTAL.get());
                    })
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "pathoscraft_ingredients_creative_tab"))
                    .build());

    public static final Supplier<CreativeModeTab> PATHOSCRAFT_FOOD_TAB = CREATIVE_MODE_TAB.register("pathoscraft_food_creative_tab", () ->
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(PathosItems.MANA_HERB.get()))
                    .title(Component.translatable("creative_tab.pathoscraft.food"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(PathosItems.MANA_HERB.get());
                        output.accept(PathosItems.MANA_POTION.get());
                    })
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "pathoscraft_tools_creative_tab"))
                    .build());

    public static final Supplier<CreativeModeTab> PATHOSCRAFT_MISC_TAB = CREATIVE_MODE_TAB.register("pathoscraft_misc_creative_tab", () ->
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(PathosItems.QUEST_BOOK.get()))
                    .title(Component.translatable("creative_tab.pathoscraft.misc"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(PathosItems.QUEST_BOOK.get());
                        output.accept(PathosItems.CREATURE_CRYSTAL.get());
                        output.accept(PathosItems.TEST_ITEM.get());

                        output.accept(PathosItems.NAIVE_WORKER.get());
                        output.accept(PathosItems.NORMAL_WORKER.get());
                        output.accept(PathosItems.SKILLED_WORKER.get());
                        output.accept(PathosItems.PROFESSIONAL_WORKER.get());
                        output.accept(PathosItems.ARTISAN_WORKER.get());
                    })
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "pathoscraft_food_creative_tab"))
                    .build());

    @SubscribeEvent
    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
    }
}
