package net.quackiemackie.pathoscraft.gui.parts;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.network.PacketDistributor;
import net.quackiemackie.pathoscraft.gui.screen.QuestScreen;
import net.quackiemackie.pathoscraft.network.payload.QuestMenuActiveQuestsPayload;
import net.quackiemackie.pathoscraft.registers.PathosAttachments;

import java.util.ArrayList;
import java.util.List;

public class QuestActiveSlotButton extends QuestSlotButton {

    private final ItemStack itemStack;
    private final List<Component> hoverInfo;
    private final int questId;

    /**
     * Constructs a new QuestSlotButton.
     *
     * @param x         The x-coordinate of the button.
     * @param y         The y-coordinate of the button.
     * @param message   The text to display on the button.
     * @param itemStack The ItemStack to render on the button.
     * @param questId   The ID of the quest associated with this button.
     * @param onPress   The action to perform when the button is pressed.
     */
    public QuestActiveSlotButton(int x, int y, Component message, ItemStack itemStack, int questId, OnPress onPress) {
        super(x, y, message, itemStack, questId, onPress);
        this.itemStack = itemStack;
        this.hoverInfo = new ArrayList<>();
        this.questId = questId;
    }

    /**
     * Gets the quest ID associated with this button.
     *
     * @return The quest ID.
     */
    public int getQuestId() {
        return questId;
    }

    @Override
    public void onPress() {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        List<Integer> activeQuests = ((IAttachmentHolder) player).getData(PathosAttachments.ACTIVE_QUESTS.get());
        int questId = this.getQuestId();

        if (activeQuests.contains(questId)) {
            activeQuests.remove(Integer.valueOf(questId));
            player.setData(PathosAttachments.ACTIVE_QUESTS.get(), activeQuests);
        }

        PacketDistributor.sendToServer(new QuestMenuActiveQuestsPayload(activeQuests));

        if (minecraft.screen instanceof QuestScreen) {
            QuestScreen questScreen = (QuestScreen) minecraft.screen;
            questScreen.removeActiveQuestButtons();
            questScreen.addActiveQuestButton(activeQuests);
        }
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        int itemX = this.getX() + (this.width / 2);
        int itemY = this.getY() + (this.height / 2);

        itemStack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        renderItem(itemStack, itemX, itemY, guiGraphics);
        //renderBorder(guiGraphics);

        if (this.isHovered()) {
            if (!hoverInfo.isEmpty()) {
                guiGraphics.renderTooltip(Minecraft.getInstance().font, hoverInfo, ItemStack.EMPTY.getTooltipImage(), mouseX, mouseY);
            }
        }
    }

    public void addHoverInfo(Component info) {
        this.hoverInfo.add(info);
    }

    protected void renderBorder(GuiGraphics guiGraphics) {
        super.renderBorder(guiGraphics);
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
        super.renderItem(itemStack, x, y, guiGraphics);
    }

    /**
     * Renders a BlockItem on the button.
     *
     * @param itemStack   The BlockItem stack to render.
     * @param poseStack   The pose stack for transformations.
     * @param guiGraphics The graphics context used for rendering.
     */
    protected void renderBlockItem(ItemStack itemStack, PoseStack poseStack, GuiGraphics guiGraphics) {
        super.renderBlockItem(itemStack, poseStack, guiGraphics);
    }
}
