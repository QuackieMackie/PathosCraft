package net.quackiemackie.pathoscraft.gui;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.quackiemackie.pathoscraft.PathosCraft;

public class PathosMenu {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, PathosCraft.MOD_ID);

//    public static final Supplier<MenuType<QuestMenu>> QUEST_MENU = MENUS.register("quest_menu", () -> IMenuTypeExtension.create(QuestMenu::new));
}
