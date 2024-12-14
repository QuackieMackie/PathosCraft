package net.quackiemackie.pathoscraft.network.payload.quest.active;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.util.quest.Quest;

import java.util.List;

public record SwapActiveQuests(List<Quest> swappableQuests) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SwapActiveQuests> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "swap_active_quest"));

    public static final StreamCodec<ByteBuf, SwapActiveQuests> STREAM_CODEC = ByteBufCodecs.fromCodec(Quest.CODEC.listOf()).map(
            SwapActiveQuests::new,
            SwapActiveQuests::swappableQuests
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
