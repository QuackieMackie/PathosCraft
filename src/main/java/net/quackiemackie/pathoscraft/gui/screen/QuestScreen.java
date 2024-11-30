package net.quackiemackie.pathoscraft.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.quackiemackie.pathoscraft.gui.menu.QuestMenu;
import net.quackiemackie.pathoscraft.gui.parts.QuestActiveSlotButton;
import net.quackiemackie.pathoscraft.gui.parts.QuestPageButton;
import net.quackiemackie.pathoscraft.gui.parts.QuestSlotButton;
import net.quackiemackie.pathoscraft.gui.parts.QuestTabButton;
import net.quackiemackie.pathoscraft.handlers.QuestHandler;
import net.quackiemackie.pathoscraft.quest.Quest;
import net.quackiemackie.pathoscraft.quest.QuestObjective;
import net.quackiemackie.pathoscraft.quest.QuestReward;
import net.quackiemackie.pathoscraft.registers.PathosAttachments;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QuestScreen extends AbstractContainerScreen<QuestMenu> {

    QuestTabButton mainQuestButton;
    QuestTabButton sideQuestButton;
    QuestTabButton optionalQuestButton;
    QuestTabButton activeQuestsButton;
    public QuestTabButton activeButton;
    public static final int maxActiveQuests = 45;

    QuestPageButton previousPageButton;
    QuestPageButton nextPageButton;
    public int currentPage = 1;
    private int maxPages = 1;

    private static final ResourceLocation questTexture = ResourceLocation.parse("pathoscraft:textures/screen/quest_menu.png");
    private static final ResourceLocation activeQuestTexture = ResourceLocation.parse("pathoscraft:textures/screen/active_quest_menu.png");

    public QuestScreen(QuestMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        activeButton = new QuestTabButton(0, 0, 0, 0, Component.empty(), false, 0, null, false);
        calculateDimensions();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        if (activeButton != activeQuestsButton) {
            drawPageNumber(guiGraphics);
        }
    }

    /**
     * Draws the current page number at the bottom of the screen.
     *
     * @param graphics the graphics context used for rendering.
     */
    private void drawPageNumber(GuiGraphics graphics) {
        String pageText = "Page " + currentPage + "/" + maxPages;
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

        // Render background texture depending on the active button
        if (activeButton == activeQuestsButton) {
            guiGraphics.blit(activeQuestTexture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
        } else {
            guiGraphics.blit(questTexture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
        }

        RenderSystem.disableBlend();
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256) { // ESC key
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
    public void resize(Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height);
        init(minecraft, width, height);
    }

    @Override
    public void init() {
        // Dynamically calculate imageWidth and imageHeight based on content
        calculateDimensions();
        super.init();

        // Initialize buttons
        initTabButtons();
        initPageButtons();

        // Initialize quest slot buttons
        initQuestSlotButtons();
    }

    /**
     * Dynamically calculates and sets the image width and height based on content.
     */
    private void calculateDimensions() {
        int baseHeight = (activeButton == activeQuestsButton) ? 96 : 222; // Active quest tab : Quest tabs
        this.imageWidth = 176;
        this.imageHeight = baseHeight;
    }

    /**
     * Initialize and adds the tab buttons to the screen.
     */
    private void initTabButtons() {
        // Initialize and add main quest button
        mainQuestButton = new QuestTabButton(this.width / 2 - 182, this.height / 2 - 105, 95, 20, Component.translatable("menu.widget.pathoscraft.quest_menu.main_quest_button"), true, 0, button -> {
            setActiveButton(mainQuestButton);
        }, false);

        // Initialize and add side quest button
        sideQuestButton = new QuestTabButton(this.width / 2 - 182, this.height / 2 - 75, 95, 20, Component.translatable("menu.widget.pathoscraft.quest_menu.side_quest_button"), false, 1, button -> {
            setActiveButton(sideQuestButton);
        }, false);

        // Initialize and add optional quest button
        optionalQuestButton = new QuestTabButton(this.width / 2 - 182, this.height / 2 - 45, 95, 20, Component.translatable("menu.widget.pathoscraft.quest_menu.optional_quest_button"), false, 2, button -> {
            setActiveButton(optionalQuestButton);
        }, false);

        // Initialize and add active quests button
        activeQuestsButton = new QuestTabButton(this.width / 2 + 87, this.height / 2 - 105, 95, 20, Component.translatable("menu.widget.pathoscraft.quest_menu.active_quest_button"), false, 3, button -> {
            setActiveButton(activeQuestsButton);
        }, true);

        activeButton = mainQuestButton;

        // Add buttons to the screen
        this.addRenderableWidget(mainQuestButton);
        this.addRenderableWidget(sideQuestButton);
        this.addRenderableWidget(optionalQuestButton);
        this.addRenderableWidget(activeQuestsButton);
    }

    /**
     * Initialize and adds the page buttons to the screen.
     */
    private void initPageButtons() {
        // Initialize and add previous page button
        previousPageButton = new QuestPageButton(this.leftPos + 30, this.topPos + this.imageHeight + 2,
                20, 20, Component.literal("<"), true) {
            @Override
            public void onPress() {
                if (currentPage > 1) {
                    currentPage--;
                    initQuestSlotButtons();
                }
            }
        };

        // Initialize and add next page button
        nextPageButton = new QuestPageButton(this.leftPos + this.imageWidth - 50, this.topPos + this.imageHeight + 2,
                20, 20, Component.literal(">"), false) {
            @Override
            public void onPress() {
                if (currentPage < maxPages) {
                    currentPage++;
                    initQuestSlotButtons();
                }
            }
        };

        if (activeButton != activeQuestsButton) {
            this.addRenderableWidget(previousPageButton);
            this.addRenderableWidget(nextPageButton);
        }
    }

    /**
     * Sets the currently active tab button.
     *
     * @param button the button to set as active.
     */
    private void setActiveButton(QuestTabButton button) {
        if (activeButton != button) {
            activeButton.setActive(false);
            button.setActive(true);
            activeButton = button;
            currentPage = 1;

            calculateDimensions();

            this.children().removeIf(child -> child instanceof QuestPageButton);
            this.renderables.removeIf(renderable -> renderable instanceof QuestPageButton);

            if (activeButton != activeQuestsButton) {
                this.addRenderableWidget(previousPageButton);
                this.addRenderableWidget(nextPageButton);
            }

            initQuestSlotButtons();
        }
    }

    /**
     * Initializes and adds quest slot buttons to the screen based on the active quest type.
     * This includes setting up buttons for main, side, and optional quests, as well as active quests.
     */
    private void initQuestSlotButtons() {
        Player player = Minecraft.getInstance().player;
        int questType = activeButton.getQuestType();

        // Remove existing QuestSlotButtons
        this.children().removeIf(child -> child instanceof QuestSlotButton);
        this.renderables.removeIf(renderable -> renderable instanceof QuestSlotButton);

        // Retrieve relevant quests for display
        List<Quest> questsByType = QuestHandler.getQuestsByType(activeButton.getQuestType());
        List<Quest> activeQuests = ((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get());

        maxPages = questsByType.stream()
                .mapToInt(Quest::getQuestSlot)
                .max()
                .orElse(0) / 99 + 1;

        // Adjust page buttons' enabled states
        if (activeButton != activeQuestsButton) {
            previousPageButton.active = currentPage > 1;
            nextPageButton.active = currentPage < maxPages;
        }

        if (questType == 0 || questType == 1 || questType == 2) {
            questTabBuilder(activeQuests, questsByType);
        } else if (questType == 3) {
            activeQuestTabBuilder(activeQuests);
        }
    }

    /**
     * Constructs the quest tabs by adding buttons for quests on the current page.
     *
     * @param activeQuests the list of quests that are currently active.
     * @param questsByType the list of quests filtered by type based on the active tab.
     */
    private void questTabBuilder(List<Quest> activeQuests, List<Quest> questsByType) {
        for (Quest quest : questsByType) {
            int slotIndex = quest.getQuestSlot();
            int slotPage = slotIndex / 99 + 1;

            if (slotPage == currentPage) {
                int pageIndex = slotIndex % 99;
                int x = 8 + (pageIndex % 9) * 18;
                int y = 18 + (pageIndex / 9) * 18;

                ItemStack questItemStack = createQuestIconStack(quest);

                if (activeQuests.contains(quest)) {
                    questItemStack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
                }

                QuestSlotButton questButton = new QuestSlotButton(
                        this.leftPos + x,
                        this.topPos + y,
                        Component.empty(),
                        questItemStack,
                        quest, e -> {}
                );

                addHoverInfo(questButton, quest);

                this.addRenderableWidget(questButton);
            }
        }
    }

    public void addActiveQuestButton(List<Quest> activeQuests) {
        activeQuestTabBuilder(activeQuests);
    }

    public void removeActiveQuestButtons() {
        this.children().removeIf(child -> child instanceof QuestActiveSlotButton);
        this.renderables.removeIf(renderable -> renderable instanceof QuestActiveSlotButton);
    }

    /**
     * Initializes and displays active quests on the screen.
     * Sorts and adds buttons for each active quest.
     *
     * @param activeQuests the list of active quests.
     */
    private void activeQuestTabBuilder(List<Quest> activeQuests) {
        if (activeQuests == null) {
            activeQuests = new ArrayList<>();
        } else {
            activeQuests = new ArrayList<>(activeQuests);
        }
        Collections.sort(activeQuests, Comparator.comparing(Quest::getQuestId));

        for (int i = 0; i < activeQuests.size() && i < maxActiveQuests; i++) {
            Quest quest = activeQuests.get(i);

            if (quest == null) {
                continue;
            }

            int x = 8 + (i % 9) * 18;
            int y = 18 + (i / 9) * 18;

            ItemStack questItemStack = createQuestIconStack(quest);

            QuestActiveSlotButton questButton = new QuestActiveSlotButton(
                    this.leftPos + x,
                    this.topPos + y,
                    Component.empty(),
                    questItemStack,
                    quest, e -> {}
            );

            addHoverInfo(questButton, quest);

            addRenderableWidget(questButton);
        }
    }

    /**
     * Creates an ItemStack representing the quest icon.
     *
     * @param quest the quest for which to create the icon.
     * @return the ItemStack representing the quest icon.
     */
    private ItemStack createQuestIconStack(Quest quest) {
        String[] iconParts = quest.getQuestIcon().split(":");
        String namespace = iconParts.length > 1 ? iconParts[0] : "minecraft";
        String path = iconParts.length > 1 ? iconParts[1] : iconParts[0];

        ResourceLocation questIcon = ResourceLocation.fromNamespaceAndPath(namespace, path);
        Item item = BuiltInRegistries.ITEM.get(questIcon);

        return new ItemStack(item);
    }

    /**
     * Adds hover information (lore) to the given quest button.
     *
     * @param questButton the quest button to add hover information to.
     * @param quest       the quest for which to add hover information.
     */
    private static void addHoverInfo(QuestSlotButton questButton, Quest quest) {
        questButton.addHoverInfo(Component.literal("§7" + quest.getQuestName()));
        questButton.addHoverInfo(Component.literal("§7" + quest.getQuestDescription()));
        questButton.addHoverInfo(Component.literal(""));
        questButton.addHoverInfo(Component.literal("§aObjectives:"));
        for (QuestObjective objective : quest.getQuestObjectives()) {
            String target = objective.getTarget().substring(objective.getTarget().indexOf(':') + 1).replace('_', ' ');
            questButton.addHoverInfo(Component.literal("  §8- §a" + objective.getAction() + " " + objective.getQuantity() + " " + target));
        }
        questButton.addHoverInfo(Component.literal("§cRewards:"));
        for (QuestReward reward : quest.getQuestRewards()) {
            String itemName = reward.getItem().substring(reward.getItem().indexOf(':') + 1).replace('_', ' ');
            questButton.addHoverInfo(Component.literal("  §8- §c" + reward.getQuantity() + " " + itemName));
        }
        questButton.addHoverInfo(Component.literal(""));
        questButton.addHoverInfo(Component.literal("§7Type: §f" + (quest.getQuestType() == 0 ? "Main Quest" : "Side Quest")));
        questButton.addHoverInfo(Component.literal("§7Quest ID: §f" + quest.getQuestId()));
    }
}