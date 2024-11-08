package net.quackiemackie.pathoscraft.event;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.handlers.AstralFormHandler;
import net.quackiemackie.pathoscraft.network.payload.AstralFormKeyPressPayload;
import net.quackiemackie.pathoscraft.registers.ModKeybinding;
import net.quackiemackie.pathoscraft.registers.ModAttachments;
import net.quackiemackie.pathoscraft.util.ModClientRenders;

@EventBusSubscriber(modid = PathosCraft.MOD_ID)
public class ClientEvents {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        // Astral Form Keybind logic and action bar
        if (player instanceof IAttachmentHolder holder && holder.getData(ModAttachments.IN_ASTRAL_FORM.get())) {
            String exitKeyMessage = ModKeybinding.ASTRAL_FORM_EXIT.getTranslatedKeyMessage().getString();
            Component actionBarMessage = Component.translatable("abilities.pathoscraft.astral_form_exit", exitKeyMessage);
            player.displayClientMessage(actionBarMessage, true);

            if (ModKeybinding.ASTRAL_FORM_EXIT.consumeClick()) {
                PacketDistributor.sendToServer(new AstralFormKeyPressPayload());
            }
        }
    }

    @SubscribeEvent
    public static void renderOverlay(RenderGuiLayerEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (AstralFormHandler.shouldShowAstralWarningOverlay() && player instanceof IAttachmentHolder holder && holder.getData(ModAttachments.IN_ASTRAL_FORM.get())) {
            DeltaTracker deltaTracker = event.getPartialTick();
            float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(Minecraft.getInstance().isPaused());
            ModClientRenders.renderAstralWarningOverlay(event.getGuiGraphics(), partialTicks);
        } else if (!AstralFormHandler.shouldShowAstralWarningOverlay() && player instanceof IAttachmentHolder holder && !holder.getData(ModAttachments.IN_ASTRAL_FORM.get()) && ModClientRenders.astralFormRenderStartTime == 0){
            ModClientRenders.resetStartTime();
        }
    }
}