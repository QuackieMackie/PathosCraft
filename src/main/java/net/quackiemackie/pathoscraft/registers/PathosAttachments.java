package net.quackiemackie.pathoscraft.registers;

import com.mojang.serialization.Codec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.quest.Quest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

// Use the player data attachment to store completed quests to the player.
// SavedData is for offline player information, online players this will be fine.

/**
 * Handles the registration and management of mod attachments for the PathosCraft mod.
 */
public class PathosAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, PathosCraft.MOD_ID);

    public static final Supplier<AttachmentType<Boolean>> IN_ASTRAL_FORM = ATTACHMENT_TYPES.register("in_astral_form", () -> AttachmentType.builder(() -> false)
            .serialize(Codec.BOOL)
            .build());

    private static final Supplier<List<Quest>> DefaultQuestList = ArrayList::new;
    public static final Supplier<AttachmentType<List<Quest>>> ACTIVE_QUESTS = ATTACHMENT_TYPES.register("active_quests", () -> AttachmentType.builder(DefaultQuestList)
            .serialize(Codec.list(Quest.CODEC))
            .copyOnDeath()
            .build());
}
