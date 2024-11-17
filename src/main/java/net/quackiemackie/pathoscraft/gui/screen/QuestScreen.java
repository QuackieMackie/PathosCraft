package net.quackiemackie.pathoscraft.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.quackiemackie.pathoscraft.gui.menu.QuestMenu;
import net.quackiemackie.pathoscraft.gui.parts.QuestButton;
import org.jetbrains.annotations.NotNull;

//MCreator generated the base code for this, and I've gone through and used it as a jumping off point.

public class QuestScreen extends AbstractContainerScreen<QuestMenu> {
    private final int x, y, z;

    QuestButton mainQuestButton;
    QuestButton sideQuestButton;
    QuestButton optionalQuestButton;
    QuestButton activeButton;

    public QuestScreen(QuestMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.x = container.x;
        this.y = container.y;
        this.z = container.z;
        this.imageWidth = 176;
        this.imageHeight = 222;
    }

    private static final ResourceLocation texture = ResourceLocation.parse("pathoscraft:textures/screen/quest_menu.png");

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
        RenderSystem.disableBlend();
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256) {
            this.minecraft.player.closeContainer();
            return true;
        }
        return super.keyPressed(key, b, c);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, Component.translatable("menu.title.pathoscraft.quest_menu"), 8, 6, 0xFFFFFFFF);
    }

    @Override
    public void init() {
        super.init();

        mainQuestButton = new QuestButton(this.width / 2 - 182, this.height / 2 - 105, 95, 20, Component.translatable("menu.widget.pathoscraft.quest_menu.main_quest_button"), true) {
            @Override
            public void onPress() {
                setActiveButton(this);
            }
        };

        sideQuestButton = new QuestButton(this.width / 2 - 182, this.height / 2 - 75, 95, 20, Component.translatable("menu.widget.pathoscraft.quest_menu.side_quest_button"), false) {
            @Override
            public void onPress() {
                setActiveButton(this);
            }
        };

        optionalQuestButton = new QuestButton(this.width / 2 - 182, this.height / 2 - 45, 95, 20, Component.translatable("menu.widget.pathoscraft.quest_menu.optional_quest_button"), false) {
            @Override
            public void onPress() {
                setActiveButton(this);
            }
        };

        activeButton = mainQuestButton;

        this.addRenderableWidget(mainQuestButton);
        this.addRenderableWidget(sideQuestButton);
        this.addRenderableWidget(optionalQuestButton);
    }

    private void setActiveButton(QuestButton button) {
        if (activeButton != button) {
            activeButton.setActive(false);
            button.setActive(true);
            activeButton = button;
        }
    }
}
