package io.github.quackiemackie.pathoscraft.network.payload.quest.active;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.util.quest.Quest;

public record RemoveActiveQuest(Quest quest) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<RemoveActiveQuest> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "remove_active_quest"));

    public static final StreamCodec<ByteBuf, RemoveActiveQuest> STREAM_CODEC = ByteBufCodecs.fromCodec(Quest.CODEC).map(
            RemoveActiveQuest::new,
            RemoveActiveQuest::quest
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}