package io.github.quackiemackie.pathoscraft.gui;

import io.github.quackiemackie.pathoscraft.gui.menu.WorkerHireMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.gui.menu.QuestMenu;

public class PathosMenu {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, PathosCraft.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<QuestMenu>> QUEST_MENU = REGISTRY.register("quest_menu", () -> IMenuTypeExtension.create(QuestMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<WorkerHireMenu>> WORKER_HIRE_MENU = REGISTRY.register("worker_hire_menu", () -> IMenuTypeExtension.create(WorkerHireMenu::new));
}