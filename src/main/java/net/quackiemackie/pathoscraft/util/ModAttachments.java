package net.quackiemackie.pathoscraft.util;

import com.mojang.serialization.Codec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.quackiemackie.pathoscraft.PathosCraft;

import java.util.function.Supplier;

/**
 * Handles the registration and management of mod attachments for the PathosCraft mod.
 */
public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(
            NeoForgeRegistries.ATTACHMENT_TYPES, PathosCraft.MOD_ID);

    public static final Supplier<AttachmentType<Boolean>> IN_ASTRAL_FORM = ATTACHMENT_TYPES.register(
            "in_astral_form", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build()
    );

    public static void register(IEventBus modEventBus) {
        ATTACHMENT_TYPES.register(modEventBus);
    }
}
