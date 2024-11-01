package net.quackiemackie.pathoscraft.network.payload;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.quackiemackie.pathoscraft.PathosCraft;

public record AstralFormStatus(boolean status) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<AstralFormStatus> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "astral_form_status"));

    public static final StreamCodec<ByteBuf, AstralFormStatus> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            AstralFormStatus::status,
            AstralFormStatus::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
