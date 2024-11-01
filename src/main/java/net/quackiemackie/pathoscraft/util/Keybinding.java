package net.quackiemackie.pathoscraft.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

import java.util.function.Supplier;

/**
 * Handles the registration and management of custom keybindings for the PathosCraft mod.
 */
public class Keybinding {

    public static final KeyMapping ASTRAL_FORM_EXIT = new KeyMapping(
            "keybinding.pathoscraft.astral_form_exit",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_H,
            "keybinding.pathoscraft.category"
    );

    public static final KeyMapping CLEAR_PERSISTENT_DATA = new KeyMapping(
            "keybinding.pathoscraft.clear_persistent_data",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_J,
            "keybinding.pathoscraft.category"
    );

    public static final Supplier<KeyMapping> PATHOSCRAFT_KEYMAPPING = () -> ASTRAL_FORM_EXIT;
    public static final Supplier<KeyMapping> CLEAR_PERSISTENT_DATA_MAPPING = () -> CLEAR_PERSISTENT_DATA;

    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(PATHOSCRAFT_KEYMAPPING.get());
        event.register(CLEAR_PERSISTENT_DATA_MAPPING.get());
    }
}