package net.quackiemackie.pathoscraft.event;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.network.payload.AstralFormKeyPressPayload;
import net.quackiemackie.pathoscraft.util.Keybinding;
import net.quackiemackie.pathoscraft.util.ModAttachments;

import static net.minecraft.network.chat.TextColor.parseColor;

@EventBusSubscriber(modid = PathosCraft.MOD_ID)
public class ClientEvents {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        // Astral Form Keybind logic and action bar
        if (player instanceof IAttachmentHolder holder && holder.getData(ModAttachments.IN_ASTRAL_FORM.get())) {
            String exitKeyMessage = Keybinding.ASTRAL_FORM_EXIT.getTranslatedKeyMessage().getString();
            Component actionBarMessage = Component.translatable("abilities.pathoscraft.astral_form_exit", exitKeyMessage);
            player.displayClientMessage(actionBarMessage, true);

            if (Keybinding.ASTRAL_FORM_EXIT.consumeClick()) {
                PacketDistributor.sendToServer(new AstralFormKeyPressPayload());
            }
        }
    }
}