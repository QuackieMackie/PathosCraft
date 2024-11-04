package net.quackiemackie.pathoscraft;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.quackiemackie.pathoscraft.entity.ModEntities;
import net.quackiemackie.pathoscraft.entity.client.AstralFormRenderer;
import net.quackiemackie.pathoscraft.event.GeneralEntityEvents;
import net.quackiemackie.pathoscraft.network.PayloadRegister;
import net.quackiemackie.pathoscraft.quest.ModQuests;
import net.quackiemackie.pathoscraft.registers.ModAttachments;
import net.quackiemackie.pathoscraft.block.ModBlocks;
import net.quackiemackie.pathoscraft.registers.ModDataComponents;
import net.quackiemackie.pathoscraft.item.ModCreativeModeTabs;
import net.quackiemackie.pathoscraft.item.ModItems;
import net.quackiemackie.pathoscraft.registers.ModKeybinding;
import net.quackiemackie.pathoscraft.registers.RegisterRegistry;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(PathosCraft.MOD_ID)
public class PathosCraft {
    public static final String MOD_ID = "pathoscraft";
    private static final Logger LOGGER = LogUtils.getLogger();

    public PathosCraft(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        modEventBus.addListener(PayloadRegister::register);
        NeoForge.EVENT_BUS.register(GeneralEntityEvents.class);
        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);
        ModAttachments.register(modEventBus);
        ModDataComponents.register(modEventBus);
        ModQuests.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }

        @SubscribeEvent
        public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.ASTRAL_FORM.get(), AstralFormRenderer::new);
        }

        @SubscribeEvent
        public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
            ModKeybinding.registerBindings(event);
        }
    }
}
