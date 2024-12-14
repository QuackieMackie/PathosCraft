package net.quackiemackie.pathoscraft.network.payload.quest.completed;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.util.quest.Quest;

import java.util.List;

public record SyncCompletedQuests(List<Quest> quests) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncCompletedQuests> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "sync_completed_quests"));

    public static final StreamCodec<ByteBuf, SyncCompletedQuests> STREAM_CODEC = ByteBufCodecs.fromCodec(Quest.CODEC.listOf()).map(
            SyncCompletedQuests::new,
            SyncCompletedQuests::quests
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
