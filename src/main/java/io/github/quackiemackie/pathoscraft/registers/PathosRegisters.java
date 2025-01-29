package io.github.quackiemackie.pathoscraft.registers;

import io.github.quackiemackie.pathoscraft.block.PathosBlockEntities;
import net.neoforged.bus.api.IEventBus;
import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.block.PathosBlocks;
import io.github.quackiemackie.pathoscraft.enchantment.PathosEnchantmentEffects;
import io.github.quackiemackie.pathoscraft.entity.PathosEntities;
import io.github.quackiemackie.pathoscraft.gui.PathosMenu;
import io.github.quackiemackie.pathoscraft.item.PathosItems;

import java.util.function.Consumer;

public class PathosRegisters {

    public static void register(IEventBus modEventBus) {
        PathosCraft.LOGGER.info("Starting registration process.");

        registerSafe(modEventBus, "items", PathosItems.ITEMS::register);
        registerSafe(modEventBus, "blocks", PathosBlocks.BLOCKS::register);
        registerSafe(modEventBus, "block entities", PathosBlockEntities.BLOCK_ENTITY::register);
        registerSafe(modEventBus, "creative mode tabs", bus -> {
            PathosCreativeModeTabs.CREATIVE_MODE_TAB.register(bus);
            bus.addListener(PathosCreativeModeTabs::addCreative);
        });
        registerSafe(modEventBus, "entities", PathosEntities.ENTITY_TYPES::register);
        registerSafe(modEventBus, "data components", PathosDataComponents.DATA_COMPONENT_TYPE::register);
        registerSafe(modEventBus, "attachments", PathosAttachments.ATTACHMENT_TYPES::register);
        registerSafe(modEventBus, "menus", PathosMenu.REGISTRY::register);
        registerSafe(modEventBus, "enchantment effects", PathosEnchantmentEffects.ENTITY_ENCHANTMENT_EFFECTS::register);

        PathosCraft.LOGGER.info("Registration process completed.");
    }

    private static void registerSafe(IEventBus modEventBus, String name, Consumer<IEventBus> registrationAction) {
        try {
            registrationAction.accept(modEventBus);
            PathosCraft.LOGGER.info("Successfully registered IEventBus: {}", name);
        } catch (Exception e) {
            PathosCraft.LOGGER.error("Failed to register {}", name, e);
        }
    }
}