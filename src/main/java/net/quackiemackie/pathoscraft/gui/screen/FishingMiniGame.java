package net.quackiemackie.pathoscraft.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.gui.parts.miniGames.FishingSequenceButton;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FishingMiniGame extends Screen {

    private static final int MAX_LIVES = 3;
    private static final long TOTAL_TIME = 10_000;
    private static final int MAX_ROUNDS = 5;
    private static final int MAX_VISIBLE_BUTTONS = 5;
    private static final int INITIAL_SEQUENCE_SIZE = 5;
    private static final int SEQUENCE_SIZE_INCREMENT = 2;
    private static final String[] BUTTONS = {"W", "A", "S", "D", "Space"};

    private final List<String> sequence = new ArrayList<>();
    private final List<FishingSequenceButton> buttonWidgets = new ArrayList<>();
    private int currentIndex = 0;
    private int remainingLives = MAX_LIVES;
    private int score = 0;
    private int currentRound = 1;
    private boolean timerExpired = false;
    private boolean completed = false;
    private long startTime = 0;

    public FishingMiniGame() {
        super(Component.literal("fishing mini game"));
        generateSequence();
    }

    /**
     * Generates a randomized sequence based on the current round.
     * Sequence length starts at 3 and increases by 2 for each round.
     */
    private void generateSequence() {
        sequence.clear();
        Random random = new Random();
        int sequenceSize = INITIAL_SEQUENCE_SIZE + (SEQUENCE_SIZE_INCREMENT * (currentRound - 1)); // Sequence length grows with rounds

        for (int i = 0; i < sequenceSize; i++) {
            sequence.add(BUTTONS[random.nextInt(BUTTONS.length)]);
        }
    }

    /**
     * Gets the current visible portion of the sequence (up to 5 elements).
     * @return a sublist of the sequence.
     */
    private List<String> getCurrentVisibleSequence() {
        int endIndex = Math.min(currentIndex + MAX_VISIBLE_BUTTONS, sequence.size());
        return sequence.subList(currentIndex, endIndex);
    }

    /**
     * Updates the visible `buttonWidgets` using the current sequence window.
     */
    private void updateVisibleWidgets() {
        List<String> visibleSequence = getCurrentVisibleSequence();
        buttonWidgets.clear();
        this.clearWidgets();

        int buttonWidth = 100;
        int buttonHeight = 20;
        int buttonSpacing = buttonHeight + 5;

        // Always center the top button
        int centerY = (this.height / 2) - 10;
        int centerX = (this.width / 2) - (buttonWidth / 2);

        for (int i = 0; i < visibleSequence.size(); i++) {
            int buttonY = centerY + (i * buttonSpacing);

            String button = visibleSequence.get(i);
            FishingSequenceButton widget = new FishingSequenceButton(centerX, buttonY, buttonWidth, buttonHeight, Component.literal(button));

            buttonWidgets.add(widget);
            this.addRenderableWidget(widget);
        }
    }

    /**
     * Initializes the screen and sets up widgets for the current round.
     */
    @Override
    protected void init() {
        super.init();
        if (startTime == 0) {
            this.startTime = System.currentTimeMillis();
        }
        this.updateVisibleWidgets();
    }

    /**
     * Handles resizing of the game screen and reinitializes the buttons.
     */
    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height);
        if (startTime == 0) {
            this.startTime = System.currentTimeMillis();
        }
        this.init();
    }

    /**
     * Handles a successful key press.
     * Moves to the next index and updates the visible portion of the sequence.
     */
    private void processSuccessfulKeyPress() {
        score++;
        currentIndex++;

        if (currentIndex >= sequence.size()) {
            completeRound();
        } else {
            updateVisibleWidgets();
        }
    }

    /**
     * Handles a failed key press attempt.
     */
    private void processFailedAttempt() {
        // Deduct a life
        remainingLives--;

        // If no lives remain, end the game
        if (remainingLives <= 0) {
            finishGame(false);
        }
    }

    /**
     * Completes the current round and advances to the next.
     */
    private void completeRound() {
        if (currentRound >= MAX_ROUNDS) {
            finishGame(true);
        } else {
            currentRound++;
            currentIndex = 0;
            generateSequence();
            updateVisibleWidgets();
            this.startTime = System.currentTimeMillis();
        }
    }

    /**
     * Finishes the game and informs the player of the results.
     */
    private void finishGame(boolean success) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.setScreen(null);

        if (success) {
            PathosCraft.LOGGER.info("You've won! Total score: {}", score);
            minecraft.player.sendSystemMessage(Component.literal("You've won! Total score: " + score));
        } else {
            PathosCraft.LOGGER.info("Game over! Total score: {}", score);
            minecraft.player.sendSystemMessage(Component.literal("Game over! Total score: " + score));
        }
    }

    /**
     * Renders the screen with the widgets, score, lives, and progress bar.
     */
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (startTime == 0) {
            this.startTime = System.currentTimeMillis();
        }

        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        //guiGraphics.fill(this.width / 2 - 1, 0, this.width / 2 + 1, this.height, 0xFFFF0000); // Vertical red line
        //guiGraphics.fill(0, this.height / 2 - 1, this.width, this.height / 2 + 1, 0xFFFF0000); // Horizontal red line

        // Render timer and check for expiration
        long elapsedTime = System.currentTimeMillis() - startTime;
        long remainingTime = TOTAL_TIME - elapsedTime;
        if (remainingTime <= 0) {
            finishGame(false);
            return;
        }
        renderProgressBar(guiGraphics, remainingTime);

        // Render lives as hearts
        renderLives(guiGraphics);

        // Display score and round
        guiGraphics.drawString(this.font, "Score: " + score, 10, 10, 0xFFFFFF);
        guiGraphics.drawString(this.font, "Round: " + currentRound, 10, 25, 0xFFFFFF);
        guiGraphics.drawString(this.font, ">", this.width / 2 - 60, this.height / 2 - 4,0xFFFFFF00);
    }

    /**
     * Renders the progress bar based on the remaining time.
     */
    private void renderProgressBar(GuiGraphics guiGraphics, long remainingTime) {
        int progressBarWidth = (int) (this.width * (remainingTime / (float) TOTAL_TIME));
        int progressBarHeight = 10;
        int progressBarX = (this.width - this.width) / 2;
        int progressBarY = this.height - progressBarHeight;
        int red = (int) ((1 - remainingTime / (float) TOTAL_TIME) * 255);
        int green = (int) ((remainingTime / (float) TOTAL_TIME) * 255);
        int progressColor = (0xFF << 24) | (red << 16) | (green << 8); // Color gradient
        guiGraphics.fill(progressBarX, progressBarY, progressBarX + progressBarWidth, progressBarY + progressBarHeight, progressColor);
    }

    /**
     * Renders the player's remaining lives as hearts on the screen.
     */
    private void renderLives(GuiGraphics guiGraphics) {
        int heartWidth = 8;
        int spacing = 5;
        int totalWidth = (heartWidth + spacing) * MAX_LIVES - spacing;

        int startX = (this.width - totalWidth) / 2;
        int startY = this.height - 40;

        for (int i = 0; i < MAX_LIVES; i++) {
            int x = startX + i * (heartWidth + spacing);
            int heartColor = (i < remainingLives) ? 0xFFFF0000 : 0xFF555555; // Full heart: red, Empty heart: gray
            guiGraphics.drawString(Minecraft.getInstance().font, "❤", x, startY, heartColor);
        }
    }

    /**
     * Handles key presses and determines if they match the expected input.
     */
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (timerExpired || completed) {
            return false;
        }
        String expected = sequence.get(currentIndex);
        String pressed = getKeyName(keyCode);
        if (pressed.equals(expected)) {
            processSuccessfulKeyPress();
        } else {
            processFailedAttempt();
        }
        return true;
    }

    /**
     * Maps GLFW key inputs to their string representations.
     */
    private String getKeyName(int keyCode) {
        return switch (keyCode) {
            case GLFW.GLFW_KEY_W -> "W";
            case GLFW.GLFW_KEY_A -> "A";
            case GLFW.GLFW_KEY_S -> "S";
            case GLFW.GLFW_KEY_D -> "D";
            case GLFW.GLFW_KEY_SPACE -> "Space";
            default -> "";
        };
    }
}