package net.quackiemackie.pathoscraft.network.payload;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface IPayloadHandler<T extends CustomPacketPayload> extends net.neoforged.neoforge.network.handling.IPayloadHandler<T> {

    static <T extends CustomPacketPayload> IPayloadHandler<T> noOperation() {
        return (payload, context) -> {};
    }

    void handle(T payload, IPayloadContext context);
}
