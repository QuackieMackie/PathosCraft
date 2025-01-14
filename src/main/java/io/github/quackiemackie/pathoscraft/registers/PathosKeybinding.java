package io.github.quackiemackie.pathoscraft.registers;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

import java.util.function.Supplier;

/**
 * Handles the registration and management of custom keybindings for the PathosCraft mod.
 */
public class PathosKeybinding {
    public static final KeyMapping ASTRAL_FORM_EXIT = new KeyMapping("keybinding.pathoscraft.astral_form_exit", InputConstants.Type.KEYSYM, InputConstants.KEY_H, "keybinding.pathoscraft.category");
    public static final Supplier<KeyMapping> PATHOSCRAFT_KEYMAPPING = () -> ASTRAL_FORM_EXIT;
}