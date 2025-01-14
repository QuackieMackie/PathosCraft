package io.github.quackiemackie.pathoscraft;

import io.github.quackiemackie.pathoscraft.registers.PathosItemProperties;
import io.github.quackiemackie.pathoscraft.registers.PathosKeybinding;
import io.github.quackiemackie.pathoscraft.registers.PathosRegisters;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import io.github.quackiemackie.pathoscraft.entity.PathosEntities;
import io.github.quackiemackie.pathoscraft.entity.client.astralForm.AstralFormRenderer;
import io.github.quackiemackie.pathoscraft.entity.client.mushroom.MushroomRenderer;
import io.github.quackiemackie.pathoscraft.entity.client.spiderMount.SpiderMountRenderer;
import io.github.quackiemackie.pathoscraft.event.server.GeneralEntityEvents;
import io.github.quackiemackie.pathoscraft.network.PayloadRegister;
import io.github.quackiemackie.pathoscraft.util.handlers.quest.QuestHandler;
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

    //TODO:
    // - I want to add a toggle method for quests, so when the mod launches, I am able to toggle whether the quests from the mod load or only external data packs load.

    public static final String MOD_ID = "pathoscraft";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PathosCraft(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        NeoForge.EVENT_BUS.register(GeneralEntityEvents.class);

        PathosRegisters.register(modEventBus);
        modEventBus.addListener(PayloadRegister::register);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        ResourceManager resourceManager = server.getResourceManager();
        QuestHandler.loadQuests(resourceManager);
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            PathosItemProperties.registerMinecraftItemProperties();

            EntityRenderers.register(PathosEntities.ASTRAL_FORM.get(), AstralFormRenderer::new);
            EntityRenderers.register(PathosEntities.SPIDER_MOUNT.get(), SpiderMountRenderer::new);
            EntityRenderers.register(PathosEntities.MUSHROOM.get(), MushroomRenderer::new);
        }

        @SubscribeEvent
        public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(PathosKeybinding.PATHOSCRAFT_KEYMAPPING.get());
        }

        @SubscribeEvent
        public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        }
    }
}
