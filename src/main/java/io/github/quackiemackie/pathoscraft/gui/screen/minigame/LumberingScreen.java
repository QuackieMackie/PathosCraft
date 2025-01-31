package io.github.quackiemackie.pathoscraft.gui.screen.minigame;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.gui.screen.parts.InformationButton;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

public class LumberingScreen extends Screen {

    private static final int FRAME_COUNT = 6;
    private static final int TIMER_DURATION = 10;
    private static final int COUNTDOWN_INITIAL_TIME = 3;
    private static final int MAX_ROUNDS = 3;
    private static final float INITIAL_RANDOM_AREA_WIDTH = 0.1f;
    private static final float PROGRESS_DECAY = 0.005f;
    private static final float PROGRESS_INCREMENT = 0.01f;
    private static final float PROGRESS_DECAY_EXTRA = 0.01f;

    private static final ResourceLocation AXE_ANIMATION_TEXTURE = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "textures/minigame/chopping_axe_atlas.png");

    private float progress = 0.5f;
    private float timeLeft = TIMER_DURATION;
    private boolean isPaused = true;
    private boolean gameOver = false;
    private boolean spacePressed = false;
    private boolean decreasePressed = false;
    private boolean isCountingDown = false;
    private final Random random = new Random();
    private float lastProgress = progress;
    private int spacePressDuration = 0;
    float progressAccelerationFactor = 1.0f;

    private int countdownTime = COUNTDOWN_INITIAL_TIME;
    private int countdownTickCounter;
    private int currentRound = 1;
    private boolean gameWon = false;

    private int progressBarX, progressBarY, progressBarWidth, progressBarHeight;
    private float randomAreaStartRelative, randomAreaWidthRelative = INITIAL_RANDOM_AREA_WIDTH;

    private final ItemStack logItemStack;
    private ResourceLocation logTexture;

    private int currentFrame = 0;
    private int frameTickCounter = 0;

    public LumberingScreen(ItemStack logItemStack) {
        super(Component.translatable("screen.title.pathoscraft.minigame.lumbering"));
        this.logItemStack = logItemStack;
        generateRandomArea();
    }

    private void initGameComponents() {
        progressBarWidth = (int) (this.width * 0.8f);
        progressBarHeight = (int) (this.height * 0.1f);
        progressBarX = (this.width - progressBarWidth) / 2;
        progressBarY = this.height / 2;

        BlockItem blockItem = (BlockItem) logItemStack.getItem();
        String rawName = blockItem.getBlock().defaultBlockState().getBlockHolder().getRegisteredName();
        String modId = rawName.contains(":") ? rawName.split(":")[0] : "minecraft";
        String itemName = rawName.contains(":") ? rawName.split(":")[1] : rawName;
        logTexture = ResourceLocation.fromNamespaceAndPath(modId, "textures/block/" + itemName + ".png");

        InformationButton infoButton = new InformationButton(10, 10);
        String[] instructions = Component.translatable("screen.widget.pathoscraft.minigame.lumbering.instructions").getString().split("\n");
        infoButton.setHoverInfo(instructions);
        this.addRenderableWidget(infoButton);

        countdownTime = COUNTDOWN_INITIAL_TIME;
        currentRound = 1;
        randomAreaWidthRelative = INITIAL_RANDOM_AREA_WIDTH;
    }

    @Override
    public void init() {
        super.init();
        initGameComponents();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        renderProgressBarBackground(guiGraphics);
        renderTargetArea(guiGraphics);
        renderTimer(guiGraphics);
        renderPosition(guiGraphics, partialTick);

        if (isCountingDown) renderCountdown(guiGraphics);
        if (gameOver) renderGameOverPrompt(guiGraphics);
    }

    @Override
    public void tick() {
        super.tick();

        if (isPaused || gameOver) return; // Skip updates if paused or game over

        lastProgress = progress; // Save last progress for smooth interpolation

        if (isCountingDown) {
            if (isProgressInsideTarget()) {
                handleCountdown(); // Continue countdown if inside target
            } else {
                resetCountdown(); // Reset countdown if outside target
            }
        } else if (isProgressInsideTarget()) {
            startCountdown(); // Start countdown on entering target
        }

        if (!isProgressInsideTarget()) {
            timeLeft -= 1.0f / 20.0f; // Decrement time when outside target
            if (timeLeft <= 0) {
                gameOver = true;
                gameWon = false;
            }
        }

        updateProgress();
        checkGameOverConditions();

        frameTickCounter++;
        if (frameTickCounter >= 10) {
            currentFrame = (currentFrame + 1) % FRAME_COUNT;
            frameTickCounter = 0;
        }
    }

    private void updateProgress() {
        if (spacePressed) {
            spacePressDuration++;
            progressAccelerationFactor = 1.0f + (spacePressDuration / 50.0f); // Progress increases faster with time
            progress += PROGRESS_INCREMENT * progressAccelerationFactor;
        } else {
            spacePressDuration = 0;
            progressAccelerationFactor = 1.0f;
        }

        progress -= PROGRESS_DECAY;
        if (decreasePressed) {
            progress -= PROGRESS_DECAY_EXTRA;
        }

        progress = Math.max(0, Math.min(progress, 1)); // Clamp progress between 0 and 1
    }

    private void startCountdown() {
        isCountingDown = true;
        countdownTime = COUNTDOWN_INITIAL_TIME; // Reset countdown
    }

    private void resetCountdown() {
        isCountingDown = false;
        countdownTickCounter = 0; // Reset countdown tick counter
    }

    private boolean isProgressInsideTarget() {
        return progress >= randomAreaStartRelative && progress <= (randomAreaStartRelative + randomAreaWidthRelative);
    }

    private void renderTimer(GuiGraphics guiGraphics) {
        guiGraphics.drawCenteredString(
                this.font,
                Component.translatable("screen.widget.pathoscraft.minigame.lumbering.time_remaining", String.format("%.1f", timeLeft)).getString(),
                this.width / 2,
                (int) (this.height * 0.2f),
                0xFFFFFF
        );
    }

    private void renderCountdown(GuiGraphics guiGraphics) {
        int countdownX = this.width / 2;
        int countdownY = (int) (this.height * 0.4f);

        String formattedCountdown = String.format("%.1f", countdownTime - (countdownTickCounter / 20.0f));
        int countdownColor = getCountdownColor();
        guiGraphics.drawCenteredString(this.font, formattedCountdown, countdownX, countdownY, countdownColor);
    }

    private int getCountdownColor() {
        if (countdownTime > 2) return 0xFFFF0000; // Red for most of the countdown
        if (countdownTime > 1) return 0xFFFFA500; // Orange
        return 0xFF00FF00; // Green for final seconds
    }

    private void renderTargetArea(GuiGraphics guiGraphics) {
        float areaEnd = Math.min(randomAreaStartRelative + randomAreaWidthRelative, 1.0f);
        int targetStartX = (int) (randomAreaStartRelative * progressBarWidth) + progressBarX;
        int targetEndX = (int) (areaEnd * progressBarWidth) + progressBarX;

        guiGraphics.fill(targetStartX, progressBarY, targetEndX, progressBarY + progressBarHeight, 0x40FFFF00);
    }

    private void renderGameOverPrompt(GuiGraphics guiGraphics) {
        String message = gameWon ? "Congratulations, You Win!" : "You Lose! Better Luck Next Time!";
        int color = gameWon ? 0xFF00FF00 : 0xFFFF0000;
        guiGraphics.drawCenteredString(this.font, message, this.width / 2, this.height / 2 - 60, color);
        guiGraphics.drawCenteredString(this.font, "Press ESC to exit", this.width / 2, this.height / 2 - 40, 0xFFFFFF);
    }

    private void renderProgressBarBackground(GuiGraphics guiGraphics) {
        for (int xOffset = 0; xOffset < progressBarWidth; xOffset += 16) {
            guiGraphics.blit(logTexture, progressBarX + xOffset, progressBarY, 0, 0, Math.min(16, progressBarWidth - xOffset), progressBarHeight, 16, 16);
        }
    }

    private void renderPosition(GuiGraphics guiGraphics, float partialTick) {
        float interpolatedProgress = lastProgress + (progress - lastProgress) * partialTick;
        int axeX = (int) (progressBarX + (progressBarWidth * interpolatedProgress)) - 8;
        int axeY = progressBarY - 16;

        renderChoppingAxe(guiGraphics, axeX, axeY);
        guiGraphics.fill(axeX + 8, progressBarY, axeX + 9, progressBarY + progressBarHeight, 0xFFFF0000);
    }

    private void renderChoppingAxe(GuiGraphics guiGraphics, int axeX, int axeY) {
        int atlasHeight = FRAME_COUNT * 16;
        int v = (currentFrame % FRAME_COUNT) * 16;

        guiGraphics.blit(AXE_ANIMATION_TEXTURE, axeX, axeY, 0, v, 16, 16, 16, atlasHeight);
    }

    private void handleCountdown() {
        countdownTickCounter++;
        if (countdownTickCounter >= 20) {
            countdownTime--; // Decrement countdown every second
            countdownTickCounter = 0;
        }

        if (countdownTime <= 0) {
            advanceToNextRound();
        }
    }

    private void advanceToNextRound() {
        isCountingDown = false;
        if (currentRound < MAX_ROUNDS) {
            currentRound++;
            randomAreaWidthRelative *= 0.7F; // Make the target narrower
            generateRandomArea();
        } else {
            gameOver = true;
            gameWon = true;
        }
    }

    private void checkGameOverConditions() {
        if (timeLeft <= 0) {
            gameOver = true;
            gameWon = false;
        }
    }

    private void generateRandomArea() {
        randomAreaWidthRelative = Math.max(0.05f, Math.min(randomAreaWidthRelative, 0.2f));
        randomAreaStartRelative = random.nextFloat() * (1.0f - randomAreaWidthRelative);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (gameOver && keyCode == GLFW.GLFW_KEY_ESCAPE) {
            onGameExit();
            return true;
        }
        if (isPaused && keyCode == GLFW.GLFW_KEY_SPACE) {
            isPaused = false;
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_SPACE) spacePressed = true;
        if (keyCode == GLFW.GLFW_KEY_S || keyCode == GLFW.GLFW_KEY_DOWN) decreasePressed = true;

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_SPACE) spacePressed = false;
        if (keyCode == GLFW.GLFW_KEY_S || keyCode == GLFW.GLFW_KEY_DOWN) decreasePressed = false;

        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    private void onGameExit() {
        Minecraft.getInstance().setScreen(null);
    }
}