package io.github.quackiemackie.pathoscraft.event.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.util.handlers.abilities.AstralFormHandler;
import io.github.quackiemackie.pathoscraft.network.payload.keybinds.AstralFormKeyPress;
import io.github.quackiemackie.pathoscraft.registers.PathosKeybinding;
import io.github.quackiemackie.pathoscraft.registers.PathosAttachments;
import io.github.quackiemackie.pathoscraft.util.abilities.astralForm.AstralFormOverlay;
import io.github.quackiemackie.pathoscraft.util.handlers.client.ToolTipHandler;

@EventBusSubscriber(modid = PathosCraft.MOD_ID)
public class ClientEvents {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        // Astral Form Keybind logic and action bar
        if (player instanceof IAttachmentHolder holder && holder.getData(PathosAttachments.IN_ASTRAL_FORM.get())) {
            String exitKeyMessage = PathosKeybinding.ASTRAL_FORM_EXIT.getTranslatedKeyMessage().getString();
            Component actionBarMessage = Component.translatable("abilities.pathoscraft.astral_form_exit", exitKeyMessage);
            player.displayClientMessage(actionBarMessage, true);

            if (PathosKeybinding.ASTRAL_FORM_EXIT.consumeClick()) {
                PacketDistributor.sendToServer(new AstralFormKeyPress());
            }
        }
    }

    @SubscribeEvent
    public static void renderOverlay(RenderGuiLayerEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (AstralFormHandler.shouldShowAstralWarningOverlay() && player instanceof IAttachmentHolder holder && holder.getData(PathosAttachments.IN_ASTRAL_FORM.get())) {
            DeltaTracker deltaTracker = event.getPartialTick();
            float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(Minecraft.getInstance().isPaused());
            AstralFormOverlay.renderAstralWarningOverlay(event.getGuiGraphics(), partialTicks);
        } else if (!AstralFormHandler.shouldShowAstralWarningOverlay() && player instanceof IAttachmentHolder holder && !holder.getData(PathosAttachments.IN_ASTRAL_FORM.get()) && AstralFormOverlay.astralFormRenderStartTime == 0){
            AstralFormOverlay.resetStartTime();
        }
    }

    @SubscribeEvent
    public static void onItemTooltip(RenderTooltipEvent.Pre event) {
        if (BuiltInRegistries.ITEM.getKey(event.getItemStack().getItem()).getNamespace().equals(PathosCraft.MOD_ID)) {
            event.setCanceled(true);
            ToolTipHandler.renderCustomTooltip(event.getFont(), event.getComponents(), event.getGraphics(), event.getTooltipPositioner(), event.getX(), event.getY());
        }
    }
}