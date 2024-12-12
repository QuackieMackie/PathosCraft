package net.quackiemackie.pathoscraft.network.payload.quest.completed;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.quest.Quest;

public record AddCompletedQuest(Quest quest) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AddCompletedQuest> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "add_completed_quest"));

    public static final StreamCodec<ByteBuf, AddCompletedQuest> STREAM_CODEC = ByteBufCodecs.fromCodec(Quest.CODEC).map(
            AddCompletedQuest::new,
            AddCompletedQuest::quest
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
