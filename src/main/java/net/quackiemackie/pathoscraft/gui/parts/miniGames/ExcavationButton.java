package net.quackiemackie.pathoscraft.gui.parts.miniGames;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.quackiemackie.pathoscraft.PathosCraft;

import java.util.function.Consumer;

public class ExcavationButton extends AbstractButton {

    private static final ResourceLocation CRACK_ATLAS_TEXTURE = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "textures/minigame/stone_cracks.png");
    private static final ResourceLocation HIDDEN_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/stone.png");
    private static final ResourceLocation REVEALED_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/block/deepslate.png");

    private static final int FRAME_COUNT = 4;
    private static final int FRAME_HEIGHT = 16;
    private static final int ATLAS_WIDTH = 16;
    private static final int FRAME_DELAY_THRESHOLD = 4;
    private int frameDelay = 0;

    private final int row;
    private final int col;
    private final String type;
    private boolean isRevealed = false;
    private boolean isAnimating = false;
    private int frameCounter = 0;
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

        // Check if animating, and render textures accordingly
        if (isAnimating) {
            int sourceY = frameCounter * 16; // Vertical position in the atlas for the current frame
            int sourceWidth = 16;
            int sourceHeight = 16;

            // Render the current frame, dynamically scaling to the button size
            guiGraphics.blit(CRACK_ATLAS_TEXTURE, getX(), getY(), this.width, this.height,
                    0, sourceY,    // Source top-left corner in the atlas (u, v)
                    sourceWidth, sourceHeight, // Source size (fixed dimensions of each frame)
                    ATLAS_WIDTH, FRAME_HEIGHT * FRAME_COUNT // Total atlas dimensions
            );

            // Handle animation frame transitions
            frameDelay++;
            if (frameDelay >= FRAME_DELAY_THRESHOLD) {
                frameDelay = 0;
                frameCounter++;

                if (frameCounter >= FRAME_COUNT) {
                    isAnimating = false;
                    isRevealed = true;
                }
            }
        } else if (isRevealed) {
            guiGraphics.blit(REVEALED_TEXTURE, getX(), getY(), 0, 0, this.width, this.height, this.width, this.height);
        } else {
            guiGraphics.blit(HIDDEN_TEXTURE, getX(), getY(), 0, 0, this.width, this.height, this.width, this.height);
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
        if (isRevealed || isAnimating) return;
        isAnimating = true;
        frameCounter = 0;
        frameDelay = 0;
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

    /**
     * Checks whether the animation for the current button has finished.
     *
     * @return true if the animation is completed, false otherwise.
     */
    public boolean isAnimationCompleted() {
        return !isAnimating && isRevealed;
    }
}
