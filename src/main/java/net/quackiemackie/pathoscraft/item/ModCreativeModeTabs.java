package net.quackiemackie.pathoscraft.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.block.ModBlocks;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PathosCraft.MOD_ID);

    public static final Supplier<CreativeModeTab> PATHOSCRAFT_BLOCKS_TAB = CREATIVE_MODE_TAB.register("pathoscraft_blocks_creative_tab", () ->
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.SADNESS_BLOCK.get()))
                    .title(Component.translatable("creative_tab.pathoscraft.blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.SADNESS_ORE.get());
                        output.accept(ModBlocks.DEEPSLATE_SADNESS_ORE.get());
                        output.accept(ModBlocks.SADNESS_BLOCK.get());
                    })
                    .build());

    public static final Supplier<CreativeModeTab> PATHOSCRAFT_INGREDIENTS_TAB = CREATIVE_MODE_TAB.register("pathoscraft_ingredients_creative_tab", () ->
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.RAW_SADNESS.get()))
                    .title(Component.translatable("creative_tab.pathoscraft.ingredients"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.RAW_SADNESS.get());
                        output.accept(ModItems.SADNESS_INGOT.get());
                    })
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "pathoscraft_blocks_creative_tab"))
                    .build());

    public static void register(IEventBus modEventBus) {
        CREATIVE_MODE_TAB.register(modEventBus);
    }
}
