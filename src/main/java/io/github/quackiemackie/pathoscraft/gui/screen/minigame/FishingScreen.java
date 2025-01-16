package io.github.quackiemackie.pathoscraft.gui.screen.minigame;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;
import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.gui.parts.miniGames.InformationButton;
import io.github.quackiemackie.pathoscraft.gui.parts.miniGames.FishingSequenceButton;
import io.github.quackiemackie.pathoscraft.network.payload.minigames.fishing.FinishedFishingMiniGame;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FishingScreen extends Screen {

    private static final int MAX_LIVES = 3;
    private static final long TOTAL_TIME = 10_000;
    private static final int MAX_ROUNDS = 6;
    private static final int MAX_VISIBLE_BUTTONS = 5;
    private static final int INITIAL_SEQUENCE_SIZE = 5;
    private static final int SEQUENCE_SIZE_INCREMENT = 2;
    private static final String[] BUTTONS = {"W", "A", "S", "D", "Space"};

    private boolean waitingToStart = true;
    private boolean timerExpired = false;
    private boolean completed = false;
    private long startTime = 0;

    private int currentRound = 1;
    private int currentIndex = 0;
    private int remainingLives = MAX_LIVES;
    private int score = 0;

    private final List<String> sequence = new ArrayList<>();
    private final List<FishingSequenceButton> buttonWidgets = new ArrayList<>();

    public FishingScreen() {
        super(Component.translatable("screen.title.pathoscraft.fishing_mini_game"));
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

        int buttonWidth = this.width / 8;
        int buttonHeight = this.height / 20;
        int buttonSpacing = buttonHeight + 5;
        int centerX = (this.width / 2) - (buttonWidth / 2);

        int topButtonY = this.height / 2 - (buttonHeight / 2);

        for (int i = 0; i < visibleSequence.size(); i++) {
            int buttonY = topButtonY + (i * buttonSpacing);

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

        score -= 5;

        // If no lives remain, end the game
        if (remainingLives <= 0) {
            finishGame(false, false);
        }
    }

    /**
     * Completes the current round and advances to the next.
     */
    private void completeRound() {
        if (currentRound >= MAX_ROUNDS) {
            finishGame(true, false);
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
    private void finishGame(boolean success, boolean earlyQuit) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.setScreen(null);

        if (earlyQuit) {
            // Logic for quitting early with the current score
            PathosCraft.LOGGER.info("You quit early! Final score: {}", score);
            minecraft.player.sendSystemMessage(Component.translatable("screen.widget.pathoscraft.fishing_mini_game.quit_early", score));
        } else if (success) {
            // Logic for successful completion
            PathosCraft.LOGGER.info("You've won! Total score: {}", score);
            minecraft.player.sendSystemMessage(Component.translatable("screen.widget.pathoscraft.fishing_mini_game.success", score));
        } else {
            // Logic for failure
            PathosCraft.LOGGER.info("Game over! Total score: {}", score);
            minecraft.player.sendSystemMessage(Component.translatable("screen.widget.pathoscraft.fishing_mini_game.failure", score));
        }

        PacketDistributor.sendToServer(new FinishedFishingMiniGame(score));
    }

    /**
     * Renders the screen with the widgets, score, lives, and progress bar.
     */
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        long elapsedTime = waitingToStart ? 0 : System.currentTimeMillis() - startTime;
        long remainingTime = TOTAL_TIME - elapsedTime;

        // Render the screen elements
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        if (waitingToStart) {
            if (!buttonWidgets.isEmpty()) {
                FishingSequenceButton topButton = buttonWidgets.getFirst();
                Component startMessage = Component.translatable("screen.widget.pathoscraft.fishing_mini_game.start_message");
                int messageX = this.width / 2 - this.font.width(startMessage) / 2;
                int messageY = topButton.getY() - this.font.lineHeight - 5;
                guiGraphics.drawString(this.font, startMessage, messageX, messageY, 0xFFFFFF);
            }

            InformationButton infoButton = new InformationButton(10, 10);
            String[] instructions = Component.translatable("screen.widget.pathoscraft.fishing_mini_game.instructions").getString().split("\n");
            infoButton.setHoverInfo(instructions);
            this.addRenderableWidget(infoButton);
        }

        //guiGraphics.fill(this.width / 2 - 1, 0, this.width / 2 + 1, this.height, 0xFFFF0000); // Vertical red line
        //guiGraphics.fill(0, this.height / 2 - 1, this.width, this.height / 2 + 1, 0xFFFF0000); // Horizontal red line

        // Render the progress bar only (but time doesn't progress while waiting)
        renderProgressBar(guiGraphics, remainingTime);

        // Render lives as hearts
        renderLives(guiGraphics);

        // Display score and round at all times
        int progressBarHeight = progressBarHeight();
        int progressBarY = this.height - progressBarHeight;

        int scoreY = progressBarY - this.font.lineHeight - 5;
        int roundY = scoreY - this.font.lineHeight - 5;

        guiGraphics.drawString(this.font, Component.translatable("screen.widget.pathoscraft.fishing_mini_game.score", score).getString(), 10, scoreY, 0xFFFFFF);
        guiGraphics.drawString(this.font, Component.translatable("screen.widget.pathoscraft.fishing_mini_game.round", currentRound).getString(), 10, roundY, 0xFFFFFF);
        if (!buttonWidgets.isEmpty()) {
            FishingSequenceButton topButton = buttonWidgets.getFirst();
            String matchMessage = Component.translatable("screen.widget.pathoscraft.fishing_mini_game.match_key").getString();
            int messageX = topButton.getX() - this.font.width(matchMessage) - 5;
            int messageY = topButton.getY() + (topButton.getHeight() / 2) - (this.font.lineHeight / 2);
            guiGraphics.drawString(this.font, matchMessage, messageX, messageY, 0xFFFFFF00);
        }

        // Handle timeout (once waitingToStart is false)
        if (!waitingToStart && remainingTime <= 0) {
            finishGame(false, false);
        }
    }

    /**
     * Renders the progress bar based on the remaining time.
     */
    private void renderProgressBar(GuiGraphics guiGraphics, long remainingTime) {
        int progressBarWidth = (int) (this.width * (remainingTime / (float) TOTAL_TIME));
        int progressBarHeight = progressBarHeight();
        int progressBarX = 0;
        int progressBarY = this.height - progressBarHeight;
        int red = (int) ((1 - remainingTime / (float) TOTAL_TIME) * 255);
        int green = (int) ((remainingTime / (float) TOTAL_TIME) * 255);
        int progressColor = (0xFF << 24) | (red << 16) | (green << 8); // Color gradient
        guiGraphics.fill(progressBarX, progressBarY, progressBarX + progressBarWidth, progressBarY + progressBarHeight, progressColor);
    }

    private int progressBarHeight() {
        int progressBarHeight;
        return progressBarHeight = (int) (this.height * 0.04);
    }

    /**
     * Renders the player's remaining lives as hearts on the screen.
     */
    private void renderLives(GuiGraphics guiGraphics) {
        int heartSize = this.width / 20;
        int spacing = heartSize / 5;
        int totalWidth = (heartSize + spacing) * MAX_LIVES - spacing;

        int progressBarHeight = progressBarHeight();
        int progressBarY = this.height - progressBarHeight;

        int startY = progressBarY - heartSize - 10;
        int startX = (this.width - totalWidth) / 2;

        for (int i = 0; i < MAX_LIVES; i++) {
            int x = startX + i * (heartSize + spacing);
            int heartColor = (i < remainingLives) ? 0xFFFF0000 : 0xFF555555; // Full heart: red, Empty heart: gray
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(x, startY, 0);
            guiGraphics.pose().scale(heartSize / 8.0f, heartSize / 8.0f, 1.0f);
            guiGraphics.drawString(minecraft.font, "❤", 0, 0, heartColor);
            guiGraphics.pose().popPose();
        }
    }

    /**
     * Handles key presses and determines if they match the expected input.
     */
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        String pressed = getKeyName(keyCode);
        int inventoryKeyCode = minecraft.options.keyInventory.getKey().getValue();

        if (keyCode == inventoryKeyCode || keyCode == GLFW.GLFW_KEY_ESCAPE) {
            finishGame(false, true);
            return true;
        }

        if (waitingToStart) {
            if (pressed.isEmpty()) {
                return false;
            }
            waitingToStart = false;
            this.startTime = System.currentTimeMillis();

            String expected = sequence.get(currentIndex);
            if (pressed.equals(expected)) {
                processSuccessfulKeyPress();
            } else {
                processFailedAttempt();
            }

            return true;
        }

        if (timerExpired || completed) {
            return false;
        }

        if (pressed.isEmpty()) {
            return false;
        }

        String expected = sequence.get(currentIndex);
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