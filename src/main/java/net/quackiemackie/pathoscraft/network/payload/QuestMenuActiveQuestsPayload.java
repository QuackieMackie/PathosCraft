package net.quackiemackie.pathoscraft.network.payload;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.quackiemackie.pathoscraft.PathosCraft;

import java.util.List;

public record QuestMenuActiveQuestsPayload(List<Integer> questIds) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<QuestMenuActiveQuestsPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "quest_menu_select_quest"));

    public static final StreamCodec<ByteBuf, QuestMenuActiveQuestsPayload> STREAM_CODEC = ByteBufCodecs.VAR_INT.apply(ByteBufCodecs.list()).map(
            QuestMenuActiveQuestsPayload::new,
            QuestMenuActiveQuestsPayload::questIds
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
