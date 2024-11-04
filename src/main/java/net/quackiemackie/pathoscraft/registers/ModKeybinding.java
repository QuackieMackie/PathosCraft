package net.quackiemackie.pathoscraft.registers;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

import java.util.function.Supplier;

/**
 * Handles the registration and management of custom keybindings for the PathosCraft mod.
 */
public class ModKeybinding {

    public static final KeyMapping ASTRAL_FORM_EXIT = new KeyMapping(
            "keybinding.pathoscraft.astral_form_exit",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_H,
            "keybinding.pathoscraft.category"
    );

    public static final Supplier<KeyMapping> PATHOSCRAFT_KEYMAPPING = () -> ASTRAL_FORM_EXIT;

    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(PATHOSCRAFT_KEYMAPPING.get());
    }
}