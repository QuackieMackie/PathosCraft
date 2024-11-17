package net.quackiemackie.pathoscraft.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.quackiemackie.pathoscraft.gui.menu.QuestMenu;
import net.quackiemackie.pathoscraft.gui.parts.QuestButton;
import net.quackiemackie.pathoscraft.gui.parts.QuestPageButton;
import org.jetbrains.annotations.NotNull;

public class QuestScreen extends AbstractContainerScreen<QuestMenu> {

    QuestButton mainQuestButton;
    QuestButton sideQuestButton;
    QuestButton optionalQuestButton;
    QuestButton activeQuestsButton;
    QuestButton activeButton;

    private QuestPageButton previousPageButton;
    private QuestPageButton nextPageButton;
    private int currentPage = 1;

    public QuestScreen(QuestMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.imageWidth = 176;
        this.imageHeight = 222;
    }

    private static final ResourceLocation texture = ResourceLocation.parse("pathoscraft:textures/screen/quest_menu.png");

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        drawPageNumber(guiGraphics);
    }

    private void drawPageNumber(GuiGraphics graphics) {
        String pageText = "Page " + currentPage;
        int textWidth = Minecraft.getInstance().font.width(pageText);
        int xPos = this.leftPos + (this.imageWidth / 2) - (textWidth / 2);
        int yPos = this.topPos + this.imageHeight + 8;
        graphics.drawString(Minecraft.getInstance().font, pageText, xPos, yPos, 0xFFFFFFFF);
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

        activeQuestsButton = new QuestButton(this.width / 2 - 182, this.height / 2 + 75, 95, 20, Component.translatable("menu.widget.pathoscraft.quest_menu.active_quest_button"), false) {
            @Override
            public void onPress() {
                setActiveButton(this);
            }
        };

        activeButton = mainQuestButton;

        previousPageButton = new QuestPageButton(this.leftPos + 30, this.topPos + this.imageHeight + 2, 20, 20, Component.literal("<"), true) {
            @Override
            public void onPress() {
                if (currentPage > 1) {
                    currentPage--;
                }
            }
        };

        nextPageButton = new QuestPageButton(this.leftPos + this.imageWidth - 50, this.topPos + this.imageHeight + 2, 20, 20, Component.literal(">"), false) {
            @Override
            public void onPress() {
                currentPage++;
            }
        };

        this.addRenderableWidget(mainQuestButton);
        this.addRenderableWidget(sideQuestButton);
        this.addRenderableWidget(optionalQuestButton);
        this.addRenderableWidget(activeQuestsButton);
        this.addRenderableWidget(previousPageButton);
        this.addRenderableWidget(nextPageButton);

    }

    private void setActiveButton(QuestButton button) {
        if (activeButton != button) {
            activeButton.setActive(false);
            button.setActive(true);
            activeButton = button;
        }
    }
}
