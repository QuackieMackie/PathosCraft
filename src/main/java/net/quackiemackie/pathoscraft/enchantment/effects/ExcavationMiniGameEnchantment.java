package net.quackiemackie.pathoscraft.enchantment.effects;

import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.network.payload.minigames.excavation.StartExcavationMiniGame;

public record ExcavationMiniGameEnchantment() implements EnchantmentEntityEffect {
    public static final MapCodec<ExcavationMiniGameEnchantment> CODEC = MapCodec.unit(ExcavationMiniGameEnchantment::new);

    @Override
    public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 origin) {
        PathosCraft.LOGGER.info("Player: {}", entity.getName().getString());
        PacketDistributor.sendToPlayer((ServerPlayer) entity, StartExcavationMiniGame.INSTANCE);
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
