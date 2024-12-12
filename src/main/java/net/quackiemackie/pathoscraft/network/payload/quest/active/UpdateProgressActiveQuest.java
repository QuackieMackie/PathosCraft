package net.quackiemackie.pathoscraft.network.payload.quest.active;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.quest.Quest;

public record UpdateProgressActiveQuest(Quest quest) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UpdateProgressActiveQuest> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "update_progress_active_quest"));

    public static final StreamCodec<ByteBuf, UpdateProgressActiveQuest> STREAM_CODEC = ByteBufCodecs.fromCodec(Quest.CODEC).map(
            UpdateProgressActiveQuest::new,
            UpdateProgressActiveQuest::quest
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
