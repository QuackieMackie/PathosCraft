package net.quackiemackie.pathoscraft.gui;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.gui.menu.QuestMenu;

public class PathosMenu {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, PathosCraft.MOD_ID);
    public static final DeferredHolder<MenuType<?>, MenuType<QuestMenu>> QUEST_MENU = REGISTRY.register("quest_menu", () -> IMenuTypeExtension.create(QuestMenu::new));
}