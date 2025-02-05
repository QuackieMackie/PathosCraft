package io.github.quackiemackie.pathoscraft.network.payload.minigames.excavation;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import io.github.quackiemackie.pathoscraft.PathosCraft;

public record FinishedExcavationMiniGame(int foundOre, ItemStack rewardItem) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<FinishedExcavationMiniGame> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "finished_excavation_mini_game"));

        public static final StreamCodec<ByteBuf, FinishedExcavationMiniGame> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT,
                FinishedExcavationMiniGame::foundOre,
                ByteBufCodecs.fromCodec(ItemStack.CODEC),
                FinishedExcavationMiniGame::rewardItem,
                FinishedExcavationMiniGame::new
        );

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }