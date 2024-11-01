package net.quackiemackie.pathoscraft.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.attachments.ModAttachments;
import net.quackiemackie.pathoscraft.handlers.AstralFormHandler;
import net.quackiemackie.pathoscraft.util.Keybinding;

import java.util.logging.Logger;

@EventBusSubscriber(modid = PathosCraft.MOD_ID)
public class ClientEvents {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (Keybinding.ASTRAL_FORM_EXIT.consumeClick()) {

            Logger.getLogger("PathosCraft").info("in_astral_form for player " + player.getData(ModAttachments.IN_ASTRAL_FORM.get()));

            if (player.getData(ModAttachments.IN_ASTRAL_FORM.get())) {
                AstralFormHandler.leaveAstralForm(player);
            }
        }
    }
}