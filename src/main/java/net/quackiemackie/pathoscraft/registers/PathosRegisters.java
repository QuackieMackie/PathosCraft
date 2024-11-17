package net.quackiemackie.pathoscraft.registers;

import net.neoforged.bus.api.IEventBus;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.block.PathosBlocks;
import net.quackiemackie.pathoscraft.entity.PathosEntities;
import net.quackiemackie.pathoscraft.gui.PathosMenu;
import net.quackiemackie.pathoscraft.item.PathosItems;

import java.util.function.Consumer;

public class PathosRegisters {

    public static void register(IEventBus modEventBus) {
        PathosCraft.LOGGER.info("Starting registration process.");

        registerSafe(modEventBus, "items", PathosItems.ITEMS::register);
        registerSafe(modEventBus, "blocks", PathosBlocks.BLOCKS::register);
        registerSafe(modEventBus, "creative mode tabs", bus -> {
            PathosCreativeModeTabs.CREATIVE_MODE_TAB.register(bus);
            bus.addListener(PathosCreativeModeTabs::addCreative);
        });
        registerSafe(modEventBus, "entities", PathosEntities.ENTITY_TYPES::register);
        registerSafe(modEventBus, "data components", PathosDataComponents.DATA_COMPONENT_TYPE::register);
        registerSafe(modEventBus, "attachments", PathosAttachments.ATTACHMENT_TYPES::register);
        registerSafe(modEventBus, "menus", PathosMenu.REGISTRY::register);

        PathosCraft.LOGGER.info("Registration process completed.");
    }

    private static void registerSafe(IEventBus modEventBus, String name, Consumer<IEventBus> registrationAction) {
        try {
            registrationAction.accept(modEventBus);
            PathosCraft.LOGGER.info("Successfully registered {}", name);
        } catch (Exception e) {
            PathosCraft.LOGGER.error("Failed to register {}", name, e);
        }
    }
}