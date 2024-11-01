package net.quackiemackie.pathoscraft.event;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.attachments.ModAttachments;
import net.quackiemackie.pathoscraft.handlers.AstralFormHandler;

import java.util.logging.Logger;

@EventBusSubscriber(modid = PathosCraft.MOD_ID)
public class PlayerEvents {

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onPlayerTick(PlayerTickEvent.Post event){
        Player player = event.getEntity();

        if (!player.level().isClientSide() && player.getData(ModAttachments.IN_ASTRAL_FORM.get())) {
            AstralFormHandler.checkDistanceAndSnapback(player, 20.0);
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        player.getData(ModAttachments.IN_ASTRAL_FORM.get());
        Logger.getLogger(PathosCraft.MOD_ID).info("Astral Form: " + player.getData(ModAttachments.IN_ASTRAL_FORM.get()));

        if (player.getData(ModAttachments.IN_ASTRAL_FORM.get())) {
            AstralFormHandler.leaveAstralForm(player);
        }
    }
}
