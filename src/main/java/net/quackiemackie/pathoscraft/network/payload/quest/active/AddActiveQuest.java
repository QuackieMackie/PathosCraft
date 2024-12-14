package net.quackiemackie.pathoscraft.network.payload.quest.active;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.util.quest.Quest;

public record AddActiveQuest(Quest quest) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AddActiveQuest> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "add_active_quest"));

    public static final StreamCodec<ByteBuf, AddActiveQuest> STREAM_CODEC = ByteBufCodecs.fromCodec(Quest.CODEC).map(
            AddActiveQuest::new,
            AddActiveQuest::quest
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}