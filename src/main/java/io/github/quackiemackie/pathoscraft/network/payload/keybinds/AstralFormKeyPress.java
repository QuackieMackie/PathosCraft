package io.github.quackiemackie.pathoscraft.network.payload.keybinds;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import io.github.quackiemackie.pathoscraft.PathosCraft;

public record AstralFormKeyPress() implements CustomPacketPayload {
    public static final AstralFormKeyPress INSTANCE = new AstralFormKeyPress();

    public static final CustomPacketPayload.Type<AstralFormKeyPress> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "astral_form_key_press"));

    public static final StreamCodec<ByteBuf, AstralFormKeyPress> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    public AstralFormKeyPress() {
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}