package net.quackiemackie.pathoscraft.network.payload;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.quest.Quest;

import java.util.List;

public record QuestMenuActiveQuestsPayload(List<Quest> quests) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<QuestMenuActiveQuestsPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "quest_menu_select_quest"));

    public static final StreamCodec<ByteBuf, QuestMenuActiveQuestsPayload> STREAM_CODEC = ByteBufCodecs.fromCodec(Quest.CODEC.listOf()).map(
            QuestMenuActiveQuestsPayload::new,
            QuestMenuActiveQuestsPayload::quests
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
