package net.quackiemackie.pathoscraft.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.quackiemackie.pathoscraft.gui.parts.miniGames.InformationButton;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

public class LumberingMiniGame extends Screen {

    private static final ResourceLocation AXE_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/item/stone_axe.png");
    private final ItemStack logItemStack;

    private static final int TIMER_DURATION = 10; // Total game time in seconds
    private static final int COUNTDOWN_INITIAL_TIME = 3; // Countdown duration in seconds
    private static final int MAX_ROUNDS = 3; // Maximum number of rounds
    private static final float INITIAL_RANDOM_AREA_WIDTH = 0.1f; // Initial target area width (fraction of bar)
    private static final float PROGRESS_DECAY = 0.005f; // Normal progress decay
    private static final float PROGRESS_INCREMENT = 0.01f; // Progress gain on space press
    private static final float PROGRESS_DECAY_EXTRA = 0.01f; // Additional decay on downward press

    private float progress = 0.5f;
    private float lastProgress = progress;
    private float timeLeft = TIMER_DURATION;

    private boolean isPaused = true;
    private boolean gameOver = false;
    private boolean spacePressed = false;
    private boolean decreasePressed = false;

    private boolean isCountingDown = false;
    private int countdownTime = COUNTDOWN_INITIAL_TIME;
    private int countdownTickCounter = 0;

    private int currentRound = 1;
    private boolean gameWon = false;

    private final Random random = new Random();
    private float randomAreaStartRelative = 0f;
    private float randomAreaWidthRelative = INITIAL_RANDOM_AREA_WIDTH;

    private int spacePressDuration = 0;
    private float progressAccelerationFactor = 1.0f;

    public LumberingMiniGame(ItemStack logTexture) {
        super(Component.translatable("screen.title.pathoscraft.minigame.lumbering"));
        this.logItemStack = logTexture;
        generateRandomArea();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        renderTimer(guiGraphics);
        renderProgressBar(guiGraphics, partialTick);
        renderTargetArea(guiGraphics);

        if (isCountingDown) renderCountdown(guiGraphics);
        if (isPaused) {
            renderPausePrompt(guiGraphics);

            InformationButton infoButton = new InformationButton(10, 10);
            String[] instructions = Component.translatable("screen.widget.pathoscraft.minigame.lumbering.instructions").getString().split("\n");
            infoButton.setHoverInfo(instructions);
            this.addRenderableWidget(infoButton);
        }
        if (gameOver) renderGameOverPrompt(guiGraphics);
    }

    @Override
    public void tick() {
        super.tick();

        if (isPaused || gameOver) return;

        lastProgress = progress;

        // Only update the game timer if progress is outside the target area
        if (!isProgressInsideTarget()) {
            timeLeft -= (timeLeft > 0) ? 1.0f / 20.0f : 0;
            if (timeLeft <= 0) {
                gameOver = true;
                gameWon = false;
                return;
            }
        }

        if (spacePressed) {
            spacePressDuration++;
            progressAccelerationFactor = 1.0f + (spacePressDuration / 50.0f);
            progress += (PROGRESS_INCREMENT * progressAccelerationFactor);
        } else {
            spacePressDuration = 0;
            progressAccelerationFactor = 1.0f;
        }

        progress -= PROGRESS_DECAY;
        if (decreasePressed) progress -= PROGRESS_DECAY_EXTRA;

        progress = Math.max(0, Math.min(progress, 1));

        if (!isCountingDown && isProgressInsideTarget()) startCountdown();
        else if (isCountingDown && !isProgressInsideTarget()) resetCountdown();

        if (isCountingDown) handleCountdown();
    }

    /**
     * Renders the countdown timer displaying the remaining time in full seconds.
     * The timer is centered horizontally and positioned at a specified height relative to the screen.
     *
     * @param guiGraphics The graphical context used to render text and graphics on the screen.
     */
    private void renderTimer(GuiGraphics guiGraphics) {
        int timerX = this.width / 2;
        int timerY = (int) (this.height * 0.2f);

        String timeRemaining = Component.translatable("screen.widget.pathoscraft.minigame.lumbering.time_remaining", String.format("%.1f", timeLeft)).getString();
        guiGraphics.drawCenteredString(this.font, timeRemaining, timerX, timerY, 0xFFFFFF);
    }

    /**
     * Renders the progress bar for the mini-game. The progress bar depicts the player's
     * current progress visually, with textures and animations representing the changes
     * in state. An axe icon and a red progress indicator are included in the bar to
     * signify progress position.
     *
     * @param guiGraphics The graphical context used to render visual elements, such as textures
     *                    and shapes, on the screen.
     * @param partialTick The partial tick value used to interpolate between frames for
     *                    smoother rendering of animations.
     */
    private void renderProgressBar(GuiGraphics guiGraphics, float partialTick) {
        int progressBarWidth = (int) (this.width * 0.8f);
        int progressBarHeight = (int) (this.height * 0.1f);
        int progressBarX = (this.width - progressBarWidth) / 2;
        int progressBarY = this.height / 2;

        BlockItem blockItem = (BlockItem) logItemStack.getItem();
        String rawName = blockItem.getBlock().defaultBlockState().getBlockHolder().getRegisteredName();
        String modId = rawName.contains(":") ? rawName.split(":")[0] : "minecraft";
        String itemName = rawName.contains(":") ? rawName.split(":")[1] : rawName;
        ResourceLocation logTexture = ResourceLocation.fromNamespaceAndPath(modId, "textures/block/" + itemName + ".png");

        for (int xOffset = 0; xOffset < progressBarWidth; xOffset += 16) {
            guiGraphics.blit(logTexture, progressBarX + xOffset, progressBarY, 0, 0, Math.min(16, progressBarWidth - xOffset), progressBarHeight, 16, 16);
        }

        float interpolatedProgress = lastProgress + (progress - lastProgress) * partialTick;
        int axeX = (int) (progressBarX + (progressBarWidth * interpolatedProgress)) - 8;
        int axeY = progressBarY - 16;

        guiGraphics.blit(AXE_TEXTURE, axeX, axeY, 0, 0, 16, 16, 16, 16);
        guiGraphics.fill(axeX + 8, progressBarY, axeX + 9, progressBarY + progressBarHeight, 0xFFFF0000);
    }

    /**
     * Renders the target area on the progress bar. The target area is represented as a
     * specific portion of the progress bar, defined by its relative start position and width.
     * Vertical yellow lines indicate the boundaries of the target region.
     *
     * @param guiGraphics The graphical context used to render visual elements on the screen.
     */
    private void renderTargetArea(GuiGraphics guiGraphics) {
        if (isPaused) return;

        int progressBarWidth = (int) (this.width * 0.8f);
        int progressBarHeight = (int) (this.height * 0.1f);
        int progressBarX = (this.width - progressBarWidth) / 2;
        int progressBarY = this.height / 2;

        int targetStartX = (int) (randomAreaStartRelative * progressBarWidth) + progressBarX;
        int targetEndX = (int) ((randomAreaStartRelative + randomAreaWidthRelative) * progressBarWidth) + progressBarX;

        guiGraphics.fill(targetStartX, progressBarY, targetStartX + 1, progressBarY + progressBarHeight, 0xFFFFFF00);
        guiGraphics.fill(targetEndX - 1, progressBarY, targetEndX, progressBarY + progressBarHeight, 0xFFFFFF00);
    }

    /**
     * Renders the countdown timer to the screen, displaying the remaining time until the next game state.
     * The method calculates the displayed countdown value by taking into account the current time
     * and the elapsed tick count, and formats it to one decimal place. The resulting text is centered
     * on the screen and colored based on the countdown's state.
     *
     * @param guiGraphics The graphical context used to render text and graphics on the screen.
     */
    private void renderCountdown(GuiGraphics guiGraphics) {
        int countdownX = this.width / 2;
        int countdownY = (int) (this.height * 0.4f);

        float displayedCountdown = countdownTime - countdownTickCounter / 20.0f;
        guiGraphics.drawCenteredString(this.font, String.format("%.1f", displayedCountdown), countdownX, countdownY, getCountdownColor());
    }

    /**
     * Determines the appropriate color code based on the current countdown time.
     * The method evaluates the value of `countdownTime` and returns a color.
     *
     * @return The color code as an integer, representing the visual state of the countdown.
     */
    private int getCountdownColor() {
        if (countdownTime > 2) return 0xFFFF0000;
        if (countdownTime > 1) return 0xFFFFA500;
        return 0xFF00FF00;
    }

    /**
     * Renders the pause prompt on the screen, displaying a message instructing the player
     * to press the spacebar to start or resume the game.
     *
     * @param guiGraphics The graphical context used to render text and graphics on the screen.
     */
    private void renderPausePrompt(GuiGraphics guiGraphics) {
        guiGraphics.drawCenteredString(this.font, "Press SPACE to Start", this.width / 2, this.height / 2 - 40, 0xFFFF0000);
    }

    /**
     * Renders the game over prompt to the screen, displaying a message indicating
     * whether the player won or lost, and providing further instructions for exiting the game.
     *
     * @param guiGraphics The graphical context used to render text and graphics on the screen.
     */
    private void renderGameOverPrompt(GuiGraphics guiGraphics) {
        String message = gameWon ? "Congratulations, You Win!" : "You Lose! Better Luck Next Time!";
        int messageColor = gameWon ? 0xFF00FF00 : 0xFFFF0000;
        guiGraphics.drawCenteredString(this.font, message, this.width / 2, this.height / 2 - 60, messageColor);
        guiGraphics.drawCenteredString(this.font, "Press ESC to exit", this.width / 2, this.height / 2 - 40, 0xFFFFFF);
    }

    /**
     * Initiates the countdown mechanic by setting the countdown state to active
     * and resetting the countdown timer to its initial value.
     * This method ensures the countdown process begins from a defined starting point.
     */
    private void startCountdown() {
        isCountingDown = true;
        countdownTime = COUNTDOWN_INITIAL_TIME;
    }

    /**
     * Resets the countdown mechanics by stopping the countdown and
     * reinitializing the countdown tick counter to zero.
     * This method is used to halt any in-progress countdown activity.
     */
    private void resetCountdown() {
        isCountingDown = false;
        countdownTickCounter = 0;
    }

    /**
     * Handles the logic and progression of the countdown mechanic during the game.
     */
    private void handleCountdown() {
        countdownTickCounter++;
        if (countdownTickCounter >= 20) {
            countdownTime--;
            countdownTickCounter = 0;
        }

        if (countdownTime <= 0) {
            advanceToNextRound();
        }
    }

    /**
     * Advances the game to the next round or concludes the game if the final round has been reached.
     */
    private void advanceToNextRound() {
        isCountingDown = false;
        if (currentRound < MAX_ROUNDS) {
            currentRound++;
            randomAreaWidthRelative *= 0.7F;
            generateRandomArea();
        } else {
            gameOver = true;
            gameWon = true;
        }
    }

    /**
     * Determines if the player's current progress is within the defined target area.
     * The target area is defined by a starting position and width, both relative to the
     * progress bar's total length.
     *
     * @return true if the progress is within the target area, false otherwise.
     */
    private boolean isProgressInsideTarget() {
        return progress >= randomAreaStartRelative && progress <= (randomAreaStartRelative + randomAreaWidthRelative);
    }

    /**
     * Generates a new random starting position for the target area within the progress bar,
     * ensuring that the area is fully contained and does not extend beyond the allowed bounds.
     */
    private void generateRandomArea() {
        randomAreaStartRelative = random.nextFloat() * (1.0f - randomAreaWidthRelative);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (gameOver && keyCode == GLFW.GLFW_KEY_ESCAPE) {
            Minecraft.getInstance().setScreen(null);
            return true;
        }

        if (isPaused && keyCode == GLFW.GLFW_KEY_SPACE) {
            isPaused = false;
            return true;
        }

        if (!isPaused && keyCode == GLFW.GLFW_KEY_SPACE) {
            spacePressed = true;
            return true;
        }

        if (!isPaused && (keyCode == GLFW.GLFW_KEY_S || keyCode == GLFW.GLFW_KEY_DOWN)) {
            decreasePressed = true;
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_SPACE) {
            spacePressed = false;
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_S || keyCode == GLFW.GLFW_KEY_DOWN) {
            decreasePressed = false;
            return true;
        }

        return super.keyReleased(keyCode, scanCode, modifiers);
    }
}