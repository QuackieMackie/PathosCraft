package net.quackiemackie.pathoscraft.gui;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.quackiemackie.pathoscraft.gui.screen.QuestScreen;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PathosScreens {

    @SubscribeEvent
    public static void clientLoad(RegisterMenuScreensEvent event) {
        event.register(PathosMenu.QUEST_MENU.get(), QuestScreen::new);
    }
}