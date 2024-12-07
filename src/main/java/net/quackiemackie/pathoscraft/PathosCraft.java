package net.quackiemackie.pathoscraft;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.quackiemackie.pathoscraft.entity.PathosEntities;
import net.quackiemackie.pathoscraft.entity.renderer.AstralFormRenderer;
import net.quackiemackie.pathoscraft.entity.renderer.SpiderMountRenderer;
import net.quackiemackie.pathoscraft.event.GeneralEntityEvents;
import net.quackiemackie.pathoscraft.network.PayloadRegister;
import net.quackiemackie.pathoscraft.handlers.QuestHandler;
import net.quackiemackie.pathoscraft.registers.*;
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
    // - When quests are in place, the quest name and description need to be translatable.
    // - I want to add a toggle method for quests, so when the mod launches, I am able to toggle whether the quests from the mod load or only external data packs load.
    // - When quests are completed add an option to keep or remove the items, so quests could take items permanently to complete or they'd return them after completing.
    // - I want to add some kind of organising to active quests, not manually done in the code like before.
    //   The goal is for the player to be able to drag the quests in the active quests tab and swap them around to reorganise the array.

    //TODO:
    // If the quest is on the COMPLETED_QUESTS data attachment then it would render in a green tick, containing the same data instead of the quest_icon
    // .
    // If the quest gets clicked while in this 'ready to complete' state, it would fetch the rewards, and return a message for now.
    // in the future it would drop the items at the players location (possibly deplying fireworks as well, or some kind of happy noise and particles.)

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
        }

        @SubscribeEvent
        public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(PathosKeybinding.PATHOSCRAFT_KEYMAPPING.get());
        }

        @SubscribeEvent
        public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(PathosEntities.ASTRAL_FORM.get(), AstralFormRenderer::new);
            event.registerEntityRenderer(PathosEntities.SPIDER_MOUNT.get(), SpiderMountRenderer::new);
        }
    }
}
