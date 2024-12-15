package net.quackiemackie.pathoscraft.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.quackiemackie.pathoscraft.gui.parts.miniGames.FishingMiniGameSequence;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FishingMiniGame extends Screen {

    private static final String[] BUTTONS = {"W", "A", "S", "D", "Space"};
    private final List<String> sequence = new ArrayList<>();
    private final List<FishingMiniGameSequence> buttonWidgets = new ArrayList<>();
    private int currentIndex = 0;
    private boolean completed = false;

    private int remainingLives = 3;

    private static final int MAX_LIVES = 3;
    private static final int SEQUENCE_SIZE = 10;

    public FishingMiniGame() {
        super(Component.literal("fishing mini game"));
        generateSequence();
    }

    /**
     * Generates a randomized sequence of button inputs for the FishingMiniGame.
     * This method populates the game's sequence list with a predefined number of
     * random button selections from the available options in the BUTTONS array.
     * The sequence is used as the target input pattern that players must replicate.
     * The size of the sequence generated is fixed at 5 entries.
     */
    private void generateSequence() {
        Random random = new Random();
        for (int i = 0; i < SEQUENCE_SIZE; i++) {
            sequence.add(BUTTONS[random.nextInt(BUTTONS.length)]);
        }
    }

    /**
     * Initializes the FishingMiniGame screen by setting up the buttons based on the generated sequence.
     * This method calculates the positions and dimensions for each button, centers them on the screen,
     * and adds them as renderable widgets. The buttons are displayed at the bottom of the screen
     * to visually represent the sequence to be played.
     *
     * The initialization takes into account the total number of buttons, the spacing between them,
     * and the screen's width to ensure proper alignment.
     */
    @Override
    protected void init() {
        super.init();

        this.clearWidgets(); // Remove any previous buttons/widgets
        this.buttonWidgets.clear();

        int buttonCount = SEQUENCE_SIZE;
        int buttonWidth = 35;
        int buttonHeight = 35;
        int buttonSpacing = 5;

        // Calculate total width needed for all buttons and gaps
        int totalButtonsWidth = (buttonWidth * buttonCount) + (buttonSpacing * (buttonCount - 1));

        // Determine starting position to center the buttons horizontally
        int startX = (this.width - totalButtonsWidth) / 2;
        int posY = this.height - 80;

        // Dynamically create and add buttons based on the sequence and positions
        for (int i = 0; i < buttonCount; i++) {
            String label = sequence.get(i);
            int x = startX + (i * (buttonWidth + buttonSpacing)); // Calculate x position for each button
            int y = posY; // Y position is the same for all buttons

            // Create a button for each sequence step
            FishingMiniGameSequence button = new FishingMiniGameSequence(x, y, buttonWidth, buttonHeight, Component.literal(label));
            this.addRenderableWidget(button);
            this.buttonWidgets.add(button);
        }
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height);

        // Recalculate and reinitialize buttons dynamically
        this.init();
    }

    /**
     * Renders the FishingMiniGame screen, including the visual elements such as GUI components and the
     * hearts (lives) above the buttons. The method dynamically updates the appearance of the hearts
     * to reflect the remaining number of lives.
     *
     * @param guiGraphics the graphical context used to render the GUI elements
     * @param mouseX the current x-coordinate of the mouse pointer
     * @param mouseY the current y-coordinate of the mouse pointer
     * @param partialTicks the partial tick time, used to render smooth animations
     */
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        // Render hearts (lives) above the buttons
        int heartXStart = (this.width - (MAX_LIVES * 20)) / 2;
        int heartYPosition = this.height - 100;

        for (int i = 0; i < MAX_LIVES; i++) {
            int heartColor = (i < remainingLives) ? 0xFFFF0000 : 0xFF555555;

            // Draw hearts above the buttons (as squares for now, replaceable with images)
            int x = heartXStart + (i * 20);
            guiGraphics.fill(x, heartYPosition, x + 16, heartYPosition + 16, heartColor);
        }
    }

    /**
     * Handles the key press event for the FishingMiniGame. Compares the pressed key with the expected
     * key in the sequence and updates the state of the game accordingly. If the pressed key matches
     * the expected key, marks the button as completed and proceeds to the next key in the sequence.
     * If the pressed key does not match, marks the button as failed, reduces lives, and checks if
     * the game should end. Completes the game on a successful sequence or ends it if all lives are lost.
     *
     * @param keyCode    the key code of the pressed key
     * @param scanCode   the scan code of the pressed key (hardware-specific code)
     * @param modifiers  the modifier flags (e.g., SHIFT, CTRL) combined as a bitfield
     * @return true if the key press was successfully processed, false if the game is already completed
     */
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (completed) return false;

        String expected = sequence.get(currentIndex);
        String pressed = getKeyName(keyCode);

        if (expected.equals(pressed)) {
            buttonWidgets.get(currentIndex).setCompleted(true);
            currentIndex++;

            if (currentIndex >= SEQUENCE_SIZE) {
                // The sequence is completed successfully
                completed = true;
                Minecraft.getInstance().player.sendSystemMessage(Component.literal("Mini Game Won."));
                this.onClose();
            }
        } else {
            // Mark the button as failed in ms
            buttonWidgets.get(currentIndex).setFailed(500);

            remainingLives--; // Deduct a life
            if (remainingLives <= 0) {
                // End the game if no lives remain
                Minecraft.getInstance().player.sendSystemMessage(Component.literal("You have lost the game."));
                this.onClose();
            }
        }

        return true;
    }

    /**
     * Retrieves the name of the key corresponding to the provided key code.
     * This method maps specific key codes to their respective names, such as "W", "A", "S", "D", or "Space".
     * If the key code does not match any predefined mappings, an empty string is returned.
     *
     * @param keyCode the integer code of the key being evaluated
     * @return the name of the key if mapped, or an empty string if no match is found
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

    /**
     * Handles the closure of the FishingMiniGame screen.
     * This method is invoked when the mini-game is either completed or forcibly closed,
     * resetting the current screen to null. It ensures that the mini-game's state is properly cleaned up.
     * The method also calls the parent class's onClose implementation for additional cleanup operations.
     */
    @Override
    public void onClose() {
        super.onClose();
        Minecraft.getInstance().setScreen(null);
    }
}