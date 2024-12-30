package net.quackiemackie.pathoscraft.gui.parts.quest;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.network.PacketDistributor;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.gui.screen.QuestScreen;
import net.quackiemackie.pathoscraft.util.handlers.client.ToolTipHandler;
import net.quackiemackie.pathoscraft.util.handlers.quest.QuestHandler;
import net.quackiemackie.pathoscraft.network.payload.quest.active.AddActiveQuest;
import net.quackiemackie.pathoscraft.network.payload.quest.active.RemoveActiveQuest;
import net.quackiemackie.pathoscraft.util.quest.Quest;
import net.quackiemackie.pathoscraft.registers.PathosAttachments;

import java.util.ArrayList;
import java.util.List;

public class QuestSlotButton extends Button {

    private final ItemStack itemStack;
    private final List<Component> hoverInfo;
    private final Quest quest;

    /**
     * Constructs a new QuestSlotButton.
     *
     * @param x         The x-coordinate of the button.
     * @param y         The y-coordinate of the button.
     * @param message   The text to display on the button.
     * @param itemStack The ItemStack to render on the button.
     * @param quest     The quest associated with this button.
     * @param onPress   The action to perform when the button is pressed.
     */
    public QuestSlotButton(int x, int y, Component message, ItemStack itemStack, Quest quest, OnPress onPress) {
        super(x, y, 16, 16, message, onPress, DEFAULT_NARRATION);
        this.itemStack = itemStack;
        this.hoverInfo = new ArrayList<>();
        this.quest = quest;
    }

    /**
     * Gets the quest associated with this button.
     *
     * @return The quest.
     */
    public Quest getQuest() {
        return quest;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Checks to see if the button is visible or mouse if hovering over it.
        if (!this.visible || !this.isMouseOver(mouseX, mouseY)) {
            return false;
        }

        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        List<Quest> activeQuests = new ArrayList<>(((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get()));

        return switch (button) {
            case 0 -> {
                handleLeftClick(activeQuests, player, minecraft);
                refreshUIAfterQuestChange(activeQuests);
                yield true;
            }
            case 1 -> {
                handleRightClick(activeQuests, player, minecraft);
                refreshUIAfterQuestChange(activeQuests);
                yield true;
            }
            default -> false;
        };
    }

    /**
     * Handles removing a quest from the active list upon right-click.
     *
     * @brief When a quest is right-clicked, it is removed from the player's
     * active quest list, and both the client and server are updated
     * to reflect this change.
     * The UI is refreshed to display the
     * updated list of active quests.
     */
    private void handleRightClick(List<Quest> activeQuests, Player player, Minecraft minecraft) {
        Quest quest = this.getQuest();
        if (minecraft.screen instanceof QuestScreen questScreen) {
            if (activeQuests.contains(quest)) {
                activeQuests.remove(quest);
                player.setData(PathosAttachments.ACTIVE_QUESTS.get(), activeQuests);
                questScreen.refreshQuestsUI(activeQuests, questScreen.activeButton.getQuestType());

                PacketDistributor.sendToServer(new RemoveActiveQuest(quest));
            }
        }
    }

    /**
     * Handles accepting rewards from completed quests upon left-click.
     *
     * @brief This method processes the action of accepting rewards for quests that
     * have been completed. It updates the completed quests data attachment and
     * sends the necessary payload to the server to execute the associated logic.
     */
    private void handleLeftClick(List<Quest> activeQuests, Player player, Minecraft minecraft) {
        Quest quest = this.getQuest();
        if (minecraft.screen instanceof QuestScreen questScreen) {
            if (!QuestHandler.isQuestCompleted(player, quest) && !QuestHandler.isActiveQuest(player, quest)) {
                boolean slotAssigned = false;

                for (int i = 0; i < Quest.MAX_ACTIVE_QUESTS; i++) {
                    final int currentSlot = i;
                    boolean isSlotInUse = activeQuests.stream()
                            .anyMatch(activeQuest -> activeQuest.getQuestActiveSlot() == currentSlot);

                    if (!isSlotInUse) {
                        quest.setQuestActiveSlot(currentSlot);
                        slotAssigned = true;
                        break;
                    }
                }

                if (slotAssigned) {
                    activeQuests.add(quest);
                    player.setData(PathosAttachments.ACTIVE_QUESTS.get(), activeQuests);
                    questScreen.refreshQuestsUI(activeQuests, questScreen.activeButton.getQuestType());

                    PacketDistributor.sendToServer(new AddActiveQuest(quest));

                    PathosCraft.LOGGER.info("Quest {} assigned to slot {}", quest.getQuestId(), quest.getQuestActiveSlot());
                } else {
                    PathosCraft.LOGGER.warn("No available slots for Quest {}", quest.getQuestId());
                }
            }
        }

    }

    void refreshUIAfterQuestChange(List<Quest> activeQuests) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen instanceof QuestScreen questScreen) {
            if (questScreen.activeButton == questScreen.activeQuestsButton) {
                questScreen.refreshActiveQuestsUI(activeQuests);
            } else {
                questScreen.refreshQuestsUI(activeQuests, questScreen.activeButton.getQuestType());
            }
        }
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        List<Quest> activeQuests = new ArrayList<>(((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get()));
        Quest quest = this.getQuest();

        boolean isQuestActive = activeQuests.contains(quest);
        boolean isQuestObjectiveCompleted = QuestHandler.isQuestObjectiveCompleted(quest);
        boolean isQuestCompleted = QuestHandler.isQuestCompleted(player, quest);

        int itemX = this.getX() + (this.width / 2);
        int itemY = this.getY() + (this.height / 2);
        int backgroundColor;

        if (isQuestObjectiveCompleted) {
            backgroundColor = 0x5000FF00; // Green for objective completed
            renderBackground(guiGraphics, itemX, itemY, backgroundColor);
        } else if (isQuestActive) {
            backgroundColor = 0x50FFA500; // Orange for active
            renderBackground(guiGraphics, itemX, itemY, backgroundColor);
        } else if (isQuestCompleted) {
            backgroundColor = 0x90000000; // Grey for completed
            renderBackground(guiGraphics, itemX, itemY, backgroundColor);
        }

        renderItem(itemStack, itemX, itemY, guiGraphics);

        if (this.isHovered() && !hoverInfo.isEmpty()) {
            List<ClientTooltipComponent> tooltipComponents = hoverInfo.stream()
                    .map(component -> ClientTooltipComponent.create(component.getVisualOrderText()))
                    .toList();

            ClientTooltipPositioner positioner = (screenWidth, screenHeight, tooltipMouseX, tooltipMouseY, tooltipWidth, tooltipHeight) -> {
                int x = Math.min(screenWidth - tooltipWidth - 10, tooltipMouseX + 10);
                int y = Math.min(screenHeight - tooltipHeight - 10, tooltipMouseY - 20);
                return new org.joml.Vector2i(Math.max(10, x), Math.max(10, y));
            };

            ToolTipHandler.renderCustomTooltip(
                    minecraft.font,           // Font used for rendering text
                    tooltipComponents,        // Converted tooltip components
                    guiGraphics,              // Graphics context
                    positioner,               // Tooltip positioner
                    mouseX,                   // Current mouse X position (method parameter)
                    mouseY                    // Current mouse Y position (method parameter)
            );
        }
    }

    /**
     * Adds information to be displayed when hovering over the button.
     *
     * @param info The hover information to add.
     */
    public void addHoverInfo(Component info) {
        this.hoverInfo.add(info);
    }

    /**
     * Renders the border around the button.
     *
     * @param guiGraphics The graphics context used for rendering.
     */
    protected void renderBorder(GuiGraphics guiGraphics) {
        int borderColor = 0xFFFFFFFF;
        int left = this.getX();
        int top = this.getY();
        int right = this.getX() + this.width;
        int bottom = this.getY() + this.height;

        guiGraphics.fill(left, top, right, top + 1, borderColor);
        guiGraphics.fill(left, bottom - 1, right, bottom, borderColor);
        guiGraphics.fill(left, top, left + 1, bottom, borderColor);
        guiGraphics.fill(right - 1, top, right, bottom, borderColor);
    }

    /**
     * Renders the ItemStack on the button.
     *
     * @param itemStack   The ItemStack to render.
     * @param x           The x-coordinate where the item should be rendered.
     * @param y           The y-coordinate where the item should be rendered.
     * @param guiGraphics The graphics context used for rendering.
     */
    protected void renderItem(ItemStack itemStack, int x, int y, GuiGraphics guiGraphics) {
        PoseStack poseStack = guiGraphics.pose();

        poseStack.pushPose();
        poseStack.translate(x, y, 100.0F);

        if (itemStack.getItem() instanceof BlockItem) {
            renderBlockItem(itemStack, poseStack, guiGraphics);
        } else {
            guiGraphics.renderItem(itemStack, -8, -8);
        }

        poseStack.popPose();
    }

    /**
     * Renders a BlockItem on the button.
     *
     * @param itemStack   The BlockItem stack to render.
     * @param poseStack   The pose stack for transformations.
     * @param guiGraphics The graphics context used for rendering.
     */
    protected void renderBlockItem(ItemStack itemStack, PoseStack poseStack, GuiGraphics guiGraphics) {
        Minecraft minecraft = Minecraft.getInstance();
        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
        BakedModel bakedModel = minecraft.getItemRenderer().getModel(itemStack, null, minecraft.player, 0);

        poseStack.scale(16.0F, -16.0F, 16.0F);
        minecraft.getItemRenderer().render(itemStack, ItemDisplayContext.GUI, false, poseStack, bufferSource, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, bakedModel);
        bufferSource.endBatch();
    }

    protected void renderBackground(GuiGraphics guiGraphics, int x, int y, int color) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        int halfWidth = width / 2;
        int halfHeight = height / 2;

        poseStack.translate(0, 0, 50.0F);

        guiGraphics.fill(x - halfWidth, y - halfHeight, x + halfWidth, y + halfHeight, color);
        poseStack.popPose();
    }
}