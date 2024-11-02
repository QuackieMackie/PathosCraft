package net.quackiemackie.pathoscraft.network.payload;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.quackiemackie.pathoscraft.PathosCraft;

public record AstralFormKeyPressPayload() implements CustomPacketPayload {

    public static final AstralFormKeyPressPayload INSTANCE = new AstralFormKeyPressPayload();

    public static final CustomPacketPayload.Type<AstralFormKeyPressPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "astral_form_key_press"));

    public static final StreamCodec<ByteBuf, AstralFormKeyPressPayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    public AstralFormKeyPressPayload() {
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}