package io.github.quackiemackie.pathoscraft.gui.screen.quest;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.quackiemackie.pathoscraft.gui.screen.parts.InformationButton;
import io.github.quackiemackie.pathoscraft.gui.screen.parts.quest.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.gui.menu.QuestMenu;
import io.github.quackiemackie.pathoscraft.util.handlers.quest.QuestHandler;
import io.github.quackiemackie.pathoscraft.util.quest.Quest;
import io.github.quackiemackie.pathoscraft.util.quest.QuestObjective;
import io.github.quackiemackie.pathoscraft.util.quest.QuestReward;
import io.github.quackiemackie.pathoscraft.registers.PathosAttachments;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestScreen extends AbstractContainerScreen<QuestMenu> {

    QuestTabButton mainQuestButton;
    QuestTabButton sideQuestButton;
    QuestTabButton optionalQuestButton;
    public QuestTabButton activeQuestsButton;
    public QuestTabButton activeButton;

    QuestPageButton previousPageButton;
    QuestPageButton nextPageButton;
    public int currentPage = 1;
    public int maxPages = 1;
    private final int MAX_QUESTS_PER_PAGE = 99;


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
        String pageTranslation = Component.translatable("screen.widget.pathoscraft.quest_menu.page").getString();
        String pageText = String.format("%s %d/%d", pageTranslation, currentPage, maxPages);
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
        guiGraphics.drawString(this.font, Component.translatable("screen.title.pathoscraft.quest_menu"), 8, 6, 0xFFFFFFFF);
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height);
        init(minecraft, width, height);
    }

    @Override
    public void init() {
        calculateDimensions();
        super.init();

        initTabButtons();
        initInfoButtons();
        initPageButtons();

        initQuestSlotButtons();
    }

    /**
     * Initialize and adds the tab buttons to the screen.
     */
    private void initInfoButtons() {
        InformationButton infoButton = new InformationButton(10, 10);
        String[] instructions = Component.translatable("screen.widget.pathoscraft.quest_menu.information_button.tooltip").getString().split("\n");
        infoButton.setHoverInfo(instructions);
        this.addRenderableWidget(infoButton);
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
        int rightAnchorX = this.width / 2 - 87;
        int leftAnchorX = this.width / 2 + 87;
        int mainQuestButtonWidth = font.width(Component.translatable("screen.widget.pathoscraft.quest_menu.main_quest_button")) + 20;
        int sideQuestButtonWidth = font.width(Component.translatable("screen.widget.pathoscraft.quest_menu.side_quest_button")) + 20;
        int optionalQuestButtonWidth = font.width(Component.translatable("screen.widget.pathoscraft.quest_menu.optional_quest_button")) + 20;
        int activeQuestButtonWidth = font.width(Component.translatable("screen.widget.pathoscraft.quest_menu.active_quest_button")) + 20;

        // Initialize and add main quest button
        mainQuestButton = new QuestTabButton(rightAnchorX - mainQuestButtonWidth, this.height / 2 - 105, mainQuestButtonWidth, 20, Component.translatable("screen.widget.pathoscraft.quest_menu.main_quest_button"), true, 0, button -> {
            setActiveButton(mainQuestButton);
        }, false);

        // Initialize and add side quest button
        sideQuestButton = new QuestTabButton(rightAnchorX - sideQuestButtonWidth, this.height / 2 - 75, sideQuestButtonWidth, 20, Component.translatable("screen.widget.pathoscraft.quest_menu.side_quest_button"), false, 1, button -> {
            setActiveButton(sideQuestButton);
        }, false);

        // Initialize and add optional quest button
        optionalQuestButton = new QuestTabButton(rightAnchorX - optionalQuestButtonWidth, this.height / 2 - 45, optionalQuestButtonWidth, 20, Component.translatable("screen.widget.pathoscraft.quest_menu.optional_quest_button"), false, 2, button -> {
            setActiveButton(optionalQuestButton);
        }, false);

        // Initialize and add active quests button
        activeQuestsButton = new QuestTabButton(leftAnchorX, this.height / 2 - 105, activeQuestButtonWidth, 20, Component.translatable("screen.widget.pathoscraft.quest_menu.active_quest_button"), false, 3, button -> {
            setActiveButton(activeQuestsButton);
        }, true);

        activeButton = mainQuestButton;

        // Add buttons to the screen
        this.addRenderableWidget(mainQuestButton);
        this.addRenderableWidget(sideQuestButton);
        this.addRenderableWidget(optionalQuestButton);
        this.addRenderableWidget(activeQuestsButton);
    }

    private int calculateMaxPagesForTab(QuestTabButton button) {
        List<Quest> quests = QuestHandler.getQuestsByType(button.getQuestType());

        int maxSlot = quests.stream()
                .mapToInt(Quest::getQuestSlot)
                .max()
                .orElse(0);

        return (maxSlot == 0) ? 1 : (int) Math.ceil((double) (maxSlot + 1) / MAX_QUESTS_PER_PAGE);
    }

    /**
     * Initialize and adds the page buttons to the screen.
     */
    private void initPageButtons() {
        previousPageButton = new QuestPageButton(this.leftPos + 30, this.topPos + this.imageHeight + 2, 20, 20, Component.literal("<"), true, this);
        nextPageButton = new QuestPageButton(this.leftPos + this.imageWidth - 50, this.topPos + this.imageHeight + 2, 20, 20, Component.literal(">"), false, this);

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
    public void initQuestSlotButtons() {
        Player player = Minecraft.getInstance().player;
        int questType = activeButton.getQuestType();

        // Remove existing QuestSlotButtons
        this.children().removeIf(child -> child instanceof QuestSlotButton);
        this.renderables.removeIf(renderable -> renderable instanceof QuestSlotButton);

        // Retrieve relevant quests for display
        List<Quest> filteredQuests = QuestHandler.getQuestsByType(questType);
        List<Quest> activeQuests = player.getData(PathosAttachments.ACTIVE_QUESTS.get());

        // Recalculate the total number of pages based on the tab type
        maxPages = calculateMaxPagesForTab(activeButton);

        // Adjust page buttons' enabled states
        if (activeButton != activeQuestsButton) {
            previousPageButton.active = currentPage > 1;
            nextPageButton.active = currentPage < maxPages;
        }

        if (questType == 0 || questType == 1 || questType == 2) {
            questTabBuilder(filteredQuests, activeQuests);
        } else if (questType == 3) {
            activeQuestTabBuilder(activeQuests);
        }
    }

    /**
     * Refreshes the active quests UI.
     *
     * @param activeQuests List of quests currently active for the player.
     *
     * @brief This method is responsible for updating the UI to reflect changes in the active quests.
     *        It first removes all current quest buttons from the UI and then rebuilds them based
     *        on the provided list of active quests. This ensures that any additions, removals,
     *        or swaps in quests are accurately displayed to the player.
     */
    public void refreshQuestsUI(List<Quest> activeQuests, int questType) {
        this.children().removeIf(child -> child instanceof QuestSlotButton);
        this.renderables.removeIf(renderable -> renderable instanceof QuestSlotButton);

        List<Quest> filteredQuests = QuestHandler.getQuestsByType(questType);

        questTabBuilder(filteredQuests, activeQuests);
    }

    /**
     * Constructs the quest tabs by adding buttons for quests on the current page.
     *
     * @param filteredQuests the list of filtered quests.
     * @param activeQuests the list of quests that are currently active.
     */
    private void questTabBuilder(List<Quest> filteredQuests, List<Quest> activeQuests) {
        Map<Integer, Quest> activeQuestMap = QuestHandler.getActiveQuestMap(activeQuests);

        int startSlot = (currentPage - 1) * MAX_QUESTS_PER_PAGE;
        int endSlot = startSlot + MAX_QUESTS_PER_PAGE;

        for (Quest quest : filteredQuests) {
            int questSlot = quest.getQuestSlot();

            // Skip quests that do not belong to the current page
            if (questSlot < startSlot || questSlot >= endSlot) {
                continue;
            }

            // Calculate screen coordinates based on questSlot (slot within the page)
            int pageRelativeSlot = questSlot - startSlot; // Slot relative to the current page
            int x = 8 + (pageRelativeSlot % 9) * 18;
            int y = 18 + (pageRelativeSlot / 9) * 18;

            // Get quest from active list or default to existing quest
            Quest activeQuest = activeQuestMap.get(quest.getQuestId());
            QuestSlotButton questButton;

            // Create quest button based on its state (active/completed/inactive)
            ItemStack questItemStack = createQuestIconStack(quest);
            if (activeQuest != null && QuestHandler.isQuestObjectiveCompleted(activeQuest) || activeQuest != null && QuestHandler.isQuestPartiallyCompleted(activeQuest)) {
                questButton = new QuestActiveSlotButton(this.leftPos + x, this.topPos + y, Component.empty(), questItemStack, activeQuest, e -> {});
            } else {
                questButton = new QuestSlotButton(this.leftPos + x, this.topPos + y, Component.empty(), questItemStack, quest, e -> {});
            }

            addHoverInfo(questButton, quest);
            this.addRenderableWidget(questButton);
        }
    }

    /**
     * Refreshes the active quests UI.
     *
     * @param activeQuests List of quests currently active for the player.
     *
     * @brief This method is responsible for updating the UI to reflect changes in the active quests.
     *        It first removes all current quest buttons from the UI and then rebuilds them based
     *        on the provided list of active quests. This ensures that any additions, removals,
     *        or swaps in quests are accurately displayed to the player.
     */
    public void refreshActiveQuestsUI(List<Quest> activeQuests) {
        this.children().removeIf(child -> child instanceof QuestActiveSlotButton);
        this.renderables.removeIf(renderable -> renderable instanceof QuestActiveSlotButton);
        activeQuestTabBuilder(activeQuests);
    }

    /**
     * Initializes and displays active quests on the screen.
     * Sorts and adds buttons for each active quest.
     *
     * @param activeQuests the list of active quests.
     */
    private void activeQuestTabBuilder(List<Quest> activeQuests) {
        for (Quest quest : activeQuests) {
            if (quest == null) {
                continue;
            }

            int questActiveSlot = quest.getQuestActiveSlot();

            if (questActiveSlot < 0 || questActiveSlot >= Quest.MAX_ACTIVE_QUESTS) {
                PathosCraft.LOGGER.error("Invalid quest active slot: " + questActiveSlot + " for quest " + quest.getQuestName());
                continue;
            }

            int x = 8 + (questActiveSlot % 9) * 18;
            int y = 18 + (questActiveSlot / 9) * 18;

            ItemStack questItemStack = createQuestIconStack(quest);

            QuestActiveSlotButton questButton = new QuestActiveSlotButton(this.leftPos + x, this.topPos + y, Component.empty(), questItemStack, quest, e -> {});

            addHoverInfo(questButton, quest);

            addRenderableWidget(questButton);
        }
    }

    /**
     * Creates a quest icon from the quests json entry.
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
     * Adds hover information (lore) to the given quest button, using active quest data if available.
     *
     * @param questButton the quest button to add hover information to.
     * @param quest       the quest for which to add hover information.
     */
    private static void addHoverInfo(QuestSlotButton questButton, Quest quest) {
        Player player = Minecraft.getInstance().player;
        List<Quest> activeQuests = new ArrayList<>(player.getData(PathosAttachments.ACTIVE_QUESTS.get()));

        // Check if the current quest is an active quest
        Quest displayQuest = activeQuests.stream()
                .filter(activeQuest -> activeQuest.getQuestId() == quest.getQuestId())
                .findFirst()
                .orElse(quest);

        String name = Component.translatable("quest.pathoscraft.quest_" + quest.getQuestId() + ".name").getString();
        String description = Component.translatable("quest.pathoscraft.quest_" + quest.getQuestId() + ".description").getString();
        questButton.addHoverInfo(Component.literal(String.format("§7%s", name)));
        questButton.addHoverInfo(Component.literal(String.format("§7%s", description)));
        questButton.addHoverInfo(Component.literal(""));

        questButton.addHoverInfo(Component.literal(String.format("§a%s", Component.translatable("quest.pathoscraft.objective.title").getString())));
        int objectiveIndex = 1;
        for (QuestObjective questObjective : displayQuest.getQuestObjectives()) {
            String action = Component.translatable("quest.pathoscraft.objective.action." + questObjective.getAction()).getString();
            String target = Component.translatable("quest.pathoscraft.quest_" + displayQuest.getQuestId() + ".objective_" + objectiveIndex + ".target").getString();
            String progress = questObjective.getProgress() + "/" +  questObjective.getQuantity();
            String consume = Component.translatable("quest.pathoscraft.return_item.consume").getString();

            if (!questObjective.getReturnItems() && questObjective.getAction().equals("collect")) {
                questButton.addHoverInfo(Component.literal(String.format("§8- §a%s %s %s %s", action, target, progress, consume)));
            } else {
                questButton.addHoverInfo(Component.literal(String.format("§8- §a%s %s %s", action, target, progress)));
            }
            objectiveIndex++;
        }

        questButton.addHoverInfo(Component.literal(String.format("§c%s", Component.translatable("quest.pathoscraft.reward.title").getString())));
        int rewardIndex = 1;
        for (QuestReward questReward : displayQuest.getQuestRewards()) {
            String reward = Component.translatable("quest.pathoscraft.quest_" + displayQuest.getQuestId() + ".reward_" + rewardIndex + ".item").getString();
            questButton.addHoverInfo(Component.literal(String.format("§8- §c%d %s", questReward.getQuantity(), reward)));
            rewardIndex++;
        }
        questButton.addHoverInfo(Component.literal(""));
        questButton.addHoverInfo(Component.literal(String.format("§8%s §7%s", Component.translatable("quest.pathoscraft.type").getString(), (displayQuest.getQuestType() == 0 ? Component.translatable("quest.pathoscraft.type.main").getString() : displayQuest.getQuestType() == 1 ? Component.translatable("quest.pathoscraft.type.side").getString() : displayQuest.getQuestType() == 2 ? Component.translatable("quest.pathoscraft.type.optional").getString() : Component.translatable("quest.pathoscraft.type.unknown").getString()))));
        questButton.addHoverInfo(Component.literal(String.format("§8%s §7%d", Component.translatable("quest.pathoscraft.id").getString(), displayQuest.getQuestId())));

    }
}