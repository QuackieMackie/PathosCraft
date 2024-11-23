package net.quackiemackie.pathoscraft.handlers;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.network.PacketDistributor;
import net.quackiemackie.pathoscraft.entity.entitys.AstralFormEntity;
import net.quackiemackie.pathoscraft.network.payload.AstralFormStatusPayload;
import net.quackiemackie.pathoscraft.registers.PathosAttachments;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.quackiemackie.pathoscraft.entity.PathosEntities;

import java.util.UUID;

public class AstralFormHandler {
    public static boolean isInWarningDistance = false;

    /**
     * The player enters Astral Form, switching the gameplay mode and storing the original position.
     *
     * @param player The player entering Astral Form.
     */
    public static void enterAstralForm(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        player.getPersistentData().putDouble("astral_form_original_x", player.getX());
        player.getPersistentData().putDouble("astral_form_original_y", player.getY());
        player.getPersistentData().putDouble("astral_form_original_z", player.getZ());
        player.getPersistentData().putFloat("astral_form_original_yaw", player.getYRot());
        player.getPersistentData().putFloat("astral_form_original_pitch", player.getXRot());

        summonAstralForm(player);

        serverPlayer.setGameMode(GameType.SPECTATOR);

        setInAstralForm(player, true);
    }

    /**
     * The player leaves Astral Form, restoring the gameplay mode and original position.
     *
     * @param player The player leaving Astral Form.
     */
    public static void leaveAstralForm(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        double originalX = player.getPersistentData().getDouble("astral_form_original_x");
        double originalY = player.getPersistentData().getDouble("astral_form_original_y");
        double originalZ = player.getPersistentData().getDouble("astral_form_original_z");
        float originalYaw = player.getPersistentData().getFloat("astral_form_original_yaw");
        float originalPitch = player.getPersistentData().getFloat("astral_form_original_pitch");

        removeAstralFormEntity(player);

        player.teleportTo(originalX, originalY, originalZ);
        player.setYRot(originalYaw);
        player.setXRot(originalPitch);

        serverPlayer.setGameMode(GameType.SURVIVAL);

        player.getPersistentData().remove("astral_form_original_x");
        player.getPersistentData().remove("astral_form_original_y");
        player.getPersistentData().remove("astral_form_original_z");
        player.getPersistentData().remove("astral_form_original_yaw");
        player.getPersistentData().remove("astral_form_original_pitch");
        player.getPersistentData().remove("astral_form_entity_id");

        setInAstralForm(player, false);

        player.displayClientMessage(Component.literal(" "), true);
    }

    /**
     * Sets the in_astral_form attachment for the player.
     *
     * @param player        The player to modify.
     * @param inAstralForm  The status of astral form to set.
     */
    public static void setInAstralForm(Player player, boolean inAstralForm) {
        ((IAttachmentHolder) player).setData(PathosAttachments.IN_ASTRAL_FORM.get(), inAstralForm);
        isInWarningDistance = false;

        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new AstralFormStatusPayload(inAstralForm));
        }
    }

    /**
     * Check the player's distance from the original position and snap back if too far.
     *
     * @param player      The player in astral form.
     * @param maxDistance The maximum allowable distance from the original position.
     */
    public static void checkDistanceAndSnapback(Player player, double warningDistance, double maxDistance) {
        ((IAttachmentHolder) player).getExistingData(PathosAttachments.IN_ASTRAL_FORM.get()).ifPresent(inAstralForm -> {
            if (inAstralForm) {
                double startX = player.getPersistentData().getDouble("astral_form_original_x");
                double startY = player.getPersistentData().getDouble("astral_form_original_y");
                double startZ = player.getPersistentData().getDouble("astral_form_original_z");

                double currentX = player.getX();
                double currentY = player.getY();
                double currentZ = player.getZ();

                double distance = Math.sqrt(Math.pow(currentX - startX, 2) + Math.pow(currentY - startY, 2) + Math.pow(currentZ - startZ, 2));

                isInWarningDistance = (distance > warningDistance && distance <= maxDistance);

                if (distance > maxDistance) {
                    player.teleportTo(startX, startY, startZ);
                    player.sendSystemMessage(Component.literal("You have gotten too far from your body!\nYou have been snapped back to reality leaving the astral plane."));
                    leaveAstralForm(player);
                    isInWarningDistance = false;
                }
            }
        });
    }

    /**
     * Summons an AstralFormEntity near the given player.
     *
     * @param player The player near which the entity is to be summoned.
     */
    private static void summonAstralForm(Player player) {
        if (player.level().isClientSide) return;

        ServerLevel serverLevel = (ServerLevel) player.level();
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();
        float yaw = player.getYRot();
        float pitch = player.getXRot();

        AstralFormEntity entity = PathosEntities.ASTRAL_FORM.get().create(serverLevel);
        if (entity != null) {
            entity.moveTo(x, y, z, yaw, pitch);
            serverLevel.addFreshEntity(entity);

            entity.getPersistentData().putUUID("astral_form_player_id", player.getUUID());
            player.getPersistentData().putUUID("astral_form_entity_id", entity.getUUID());

            Component entityName = Component.literal(player.getDisplayName().getString()).append(Component.translatable("abilities.pathoscraft.astral_form_name"));
            entity.setCustomName(entityName);
            entity.setCustomNameVisible(true);
        }
    }

    /**
     * Handles the death of an AstralFormEntity by triggering a custom message and exiting astral form.
     *
     * @param astralFormEntity The AstralFormEntity that died.
     */
    public static void handleAstralFormEntityDeath(AstralFormEntity astralFormEntity) {
        UUID playerUUID = astralFormEntity.getPersistentData().getUUID("astral_form_player_id");
        Player player = astralFormEntity.level().getPlayerByUUID(playerUUID);

        if (player != null) {
            player.sendSystemMessage(Component.literal("Your astral form has perished! Returning to your physical body."));
            removeAstralFormEntity(player);
            leaveAstralForm(player);
        }
    }

    /**
     * Removes the AstralFormEntity associated with the player.
     *
     * @param player The player associated with the AstralFormEntity.
     */
    private static void removeAstralFormEntity(Player player) {
        if (!(player.level() instanceof ServerLevel serverLevel)) return;

        UUID entityId = player.getPersistentData().getUUID("astral_form_entity_id");

        Entity entity = serverLevel.getEntity(entityId);
        if (entity instanceof AstralFormEntity) {
            entity.remove(Entity.RemovalReason.KILLED);
        }
    }

    public static boolean shouldShowAstralWarningOverlay() {
        return isInWarningDistance;
    }
}