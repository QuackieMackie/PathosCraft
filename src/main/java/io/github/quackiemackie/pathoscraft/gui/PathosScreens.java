package io.github.quackiemackie.pathoscraft.gui;

import io.github.quackiemackie.pathoscraft.gui.screen.worker.WorkerHireScreen;
import io.github.quackiemackie.pathoscraft.gui.screen.worker.WorkerMainScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import io.github.quackiemackie.pathoscraft.gui.screen.quest.QuestScreen;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PathosScreens {

    @SubscribeEvent
    public static void clientLoad(RegisterMenuScreensEvent event) {
        event.register(PathosMenu.QUEST_MENU.get(), QuestScreen::new);
        event.register(PathosMenu.WORKER_HIRE_MENU.get(), WorkerHireScreen::new);
        event.register(PathosMenu.WORKER_MAIN_MENU.get(), WorkerMainScreen::new);
    }
}