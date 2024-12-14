package net.quackiemackie.pathoscraft.network.payload;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface IPayloadHandler<T extends CustomPacketPayload> extends net.neoforged.neoforge.network.handling.IPayloadHandler<T> {

    /**
     * Returns an IPayloadHandler that performs no operation when handling payloads.
     * This can be used as a placeholder or default handler in scenarios where no specific handling is required.
     *
     * @param <T> The type of payload extending CustomPacketPayload.
     * @return An IPayloadHandler instance that does nothing when invoked.
     */
    static <T extends CustomPacketPayload> IPayloadHandler<T> noOperation() {
        return (payload, context) -> {};
    }

    void handle(T payload, IPayloadContext context);
}
