package net.quackiemackie.pathoscraft.network.payload.quest.completed;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.quest.Quest;

import java.util.List;

public record ClearCompletedQuests(List<Quest> quests) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClearCompletedQuests> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "clear_completed_quests"));

    public static final StreamCodec<ByteBuf, ClearCompletedQuests> STREAM_CODEC = ByteBufCodecs.fromCodec(Quest.CODEC.listOf()).map(
            ClearCompletedQuests::new,
            ClearCompletedQuests::quests
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}