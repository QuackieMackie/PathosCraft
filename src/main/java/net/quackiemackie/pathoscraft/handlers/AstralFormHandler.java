package net.quackiemackie.pathoscraft.handlers;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.util.ModAttachments;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

import java.util.logging.Logger;

public class AstralFormHandler {

    private static final Logger logger = Logger.getLogger(PathosCraft.MOD_ID);

    /**
     * The player enters Astral Form, switching the gameplay mode and storing the original position.
     *
     * @param player The player entering Astral Form.
     */
    public static void enterAstralForm(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        logger.info("Player " + player.getName().getString() + " entered Astral Form");

        BlockPos originalPos = player.blockPosition();
        player.getPersistentData().putInt("astral_form_original_x", originalPos.getX());
        player.getPersistentData().putInt("astral_form_original_y", originalPos.getY());
        player.getPersistentData().putInt("astral_form_original_z", originalPos.getZ());

        player.sendSystemMessage(Component.literal("Astral Form has been entered.\nLeft body at Position: X: " + originalPos.getX() + " Y: " + originalPos.getY() + " Z: " + originalPos.getZ()));

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

        BlockPos originalPos = new BlockPos(
                player.getPersistentData().getInt("astral_form_original_x"),
                player.getPersistentData().getInt("astral_form_original_y"),
                player.getPersistentData().getInt("astral_form_original_z")
        );
        player.sendSystemMessage(Component.literal("Astral Form has been left.\nReturning to body at Position: X: " + originalPos.getX() + " Y: " + originalPos.getY() + " Z: " + originalPos.getZ()));

        serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.literal("")));

        serverPlayer.setGameMode(GameType.SURVIVAL);

        player.getPersistentData().remove("astral_form_original_x");
        player.getPersistentData().remove("astral_form_original_y");
        player.getPersistentData().remove("astral_form_original_z");

        setInAstralForm(player, false);
    }

    /**
     * Sets the in_astral_form attachment for the player.
     *
     * @param player        The player to modify.
     * @param inAstralForm  The status of astral form to set.
     */
    public static void setInAstralForm(Player player, boolean inAstralForm) {
        ((IAttachmentHolder) player).setData(ModAttachments.IN_ASTRAL_FORM.get(), inAstralForm);
        logger.info("Set in_astral_form for player " + player.getName().getString() + " to " + inAstralForm);
    }

    /**
     * Check the player's distance from the original position and snap back if too far.
     *
     * @param player      The player in astral form.
     * @param maxDistance The maximum allowable distance from the original position.
     */
    public static void checkDistanceAndSnapback(Player player, double maxDistance) {
        ((IAttachmentHolder) player).getExistingData(ModAttachments.IN_ASTRAL_FORM.get()).ifPresent(inAstralForm -> {
            if (inAstralForm) {
                double startX = player.getPersistentData().getInt("astral_form_original_x");
                double startY = player.getPersistentData().getInt("astral_form_original_y");
                double startZ = player.getPersistentData().getInt("astral_form_original_z");

                double currentX = player.getX();
                double currentY = player.getY();
                double currentZ = player.getZ();

                double distance = Math.sqrt(Math.pow(currentX - startX, 2) + Math.pow(currentY - startY, 2) + Math.pow(currentZ - startZ, 2));

                if (distance > maxDistance) {
                    player.teleportTo(startX, startY, startZ);
                    player.sendSystemMessage(Component.literal("You have gotten too far from your body!\nYou have been snapped back to reality leaving the astral plane."));
                    leaveAstralForm(player);
                }
            }
        });
    }
}