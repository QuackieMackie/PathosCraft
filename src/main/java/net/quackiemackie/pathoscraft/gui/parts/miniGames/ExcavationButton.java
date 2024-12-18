package net.quackiemackie.pathoscraft.gui.parts.miniGames;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class ExcavationButton extends AbstractButton {

    private final int row;
    private final int col;
    private final String type;
    private boolean isRevealed = false;
    private final Consumer<ExcavationButton> onPressCallback;

    public ExcavationButton(int x, int y, int width, int height, String type, int row, int col, Consumer<ExcavationButton> onPressCallback) {
        super(x, y, width, height, Component.literal("?"));
        this.type = type;
        this.row = row;
        this.col = col;
        this.onPressCallback = onPressCallback;
    }

    @Override
    public void onPress() {
        onPressCallback.accept(this);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        int borderColor = 0xFF000000;
        int borderThickness = 2;

        guiGraphics.fill(getX() - borderThickness, getY() - borderThickness, getX() + this.width + borderThickness, getY(), borderColor);
        guiGraphics.fill(getX() - borderThickness, getY() + this.height, getX() + this.width + borderThickness, getY() + this.height + borderThickness, borderColor);
        guiGraphics.fill(getX() - borderThickness, getY(), getX(), getY() + this.height, borderColor);
        guiGraphics.fill(getX() + this.width, getY(), getX() + this.width + borderThickness, getY() + this.height, borderColor);

        if (isRevealed) {
            guiGraphics.blit(ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/deepslate.png"), getX(), getY(), 0, 0, this.width, this.height, 16, 16);
        } else {
            guiGraphics.blit(ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/stone.png"), getX(), getY(), 0, 0, this.width, this.height, 16, 16);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    /**
     * Reveals the current button by changing its state and updating its display message.
     *
     * Specifically, this method marks the button as revealed by setting its internal
     * state to true and updates its message to reflect its type. If the button's type
     * is "ore," the message will display "Ore"; otherwise, it will display "Empty."
     */
    public void reveal() {
        isRevealed = true;
        setMessage(Component.literal(type.equals("ore") ? "Ore" : "Empty"));
    }

    /**
     * Checks whether the current button has been revealed.
     *
     * @return true if the button is revealed, false otherwise.
     */
    public boolean isRevealed() {
        return isRevealed;
    }

    /**
     * Retrieves the row index of the button within the grid.
     *
     * @return the row index of this button as an integer.
     */
    public int getRow() {
        return row;
    }

    /**
     * Retrieves the column index of the button within the grid.
     *
     * @return the column index of this button as an integer.
     */
    public int getCol() {
        return col;
    }

    /**
     * Retrieves the type of the button, which indicates its associated state or content.
     *
     * @return a string representing the type of this button, such as "ore" or "empty".
     */
    public String getType() {
        return type;
    }
}
