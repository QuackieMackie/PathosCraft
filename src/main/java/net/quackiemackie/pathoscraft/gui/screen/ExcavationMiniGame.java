package net.quackiemackie.pathoscraft.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.gui.parts.miniGames.ExcavationButton;
import net.quackiemackie.pathoscraft.network.payload.minigames.excavation.FinishedExcavationMiniGame;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//TODO
// Depending on what ore is used as the texture, or what block is broken, it would render in similar minecraft ores.
// .
// Replace the live counter with something visual, I think stone pickaxes down the side. As they get lost the pickaxes
// would break, and disappear or appear broken.

public class ExcavationMiniGame extends Screen {

    private static final int GRID_SIZE = 5; // Size of the grid (5x5)
    private static final int MAX_LIVES = 6; // Maximum lives for the player
    private static final int MAX_ORES = 5; // Maximum number of ores on the board

    private static final ResourceLocation SADNESS_TEXTURE = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "textures/item/raw_sadness.png");
    private final String[][] board = new String[GRID_SIZE][GRID_SIZE];
    private final boolean[][] revealedStates = new boolean[GRID_SIZE][GRID_SIZE];
    private final List<ExcavationButton> buttons = new ArrayList<>();

    private int remainingLives = MAX_LIVES;
    private int remainingOres;
    private boolean gameOver = false;
    private boolean pauseState = false;

    public ExcavationMiniGame() {
        super(Component.literal("Excavation Mini-Game"));
        generateBoard();
    }

    /**
     * Generates the game board for the Excavation Mini-Game by filling it with either "ore"
     * or "stone" based on a random chance. There is a 20% chance of placing "ore"
     * tiles, limited by the maximum allowed number of ores. The remaining tiles are
     * filled with "stone".
     *
     * The method iterates through each cell in the board grid and assigns a type
     * based on the generated random chance. The total count of "ore" tiles is tracked
     * to ensure it does not exceed the maximum limit.
     */
    private void generateBoard() {
        Random random = new Random();
        remainingOres = 0;

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                double chance = random.nextDouble();

                // Place ore with a 20% chance, up to the maximum ore limit
                if (chance < 0.20 && remainingOres < MAX_ORES) {
                    board[i][j] = "ore";
                    remainingOres++;
                } else {
                    board[i][j] = "stone";
                }
            }
        }
    }

    /**
     * Initializes the Excavation Mini-Game by setting up its graphical and interactive elements.
     *
     * This method performs the following actions:
     * - Clears the current button objects to prepare for new widgets.
     * - Dynamically calculates the size and position of the buttons based on the screen dimensions and grid size.
     * - Iterates through the game board (`board`) to create `ExcavationButton` widgets for each cell.
     * - Configures each button with its position, size, type, and state, and adds it to the renderable widgets.
     * - Restores the revealed state of buttons based on the `revealedStates` array.
     *
     * Additionally, the method centers the buttons within the screen to ensure proper alignment.
     */
    @Override
    protected void init() {
        super.init();
        buttons.clear();

        int buttonSize = Math.min(width, height) / (GRID_SIZE + 2); // Dynamically scale button size
        int startX = (this.width - (GRID_SIZE * buttonSize)) / 2;
        int startY = (this.height - (GRID_SIZE * buttonSize)) / 2;

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                int x = startX + col * buttonSize;
                int y = startY + row * buttonSize;
                String type = board[row][col];

                ExcavationButton button = new ExcavationButton(x, y, buttonSize, buttonSize, type, row, col, this::onButtonClick);

                // Restore revealed state if needed
                if (revealedStates[row][col]) {
                    button.reveal();
                }

                this.addRenderableWidget(button);
                buttons.add(button);
            }
        }
    }

    /**
     * Handles the logic for when a button in the Excavation Mini-Game is clicked.
     * This method checks the current state of the game and the clicked button,
     * reveals the button, updates the game state, and checks for win or loss conditions.
     *
     * @param button The `ExcavationButton` that was clicked, representing a specific grid cell in the game.
     */
    private void onButtonClick(ExcavationButton button) {
        if (gameOver || button.isRevealed()) return;

        button.reveal();
        int row = button.getRow();
        int col = button.getCol();
        revealedStates[row][col] = true;

        if (button.getType().equals("ore")) {
            remainingOres--;
            checkGameWon();
        } else {
            remainingLives--;
            checkGameOver();
        }
    }

    /**
     * Checks if the game has been won by verifying if all ores have been found.
     *
     * If the remaining ores are reduced to zero, the game state is set to over,
     * and the game concludes with a victory state.
     */
    private void checkGameWon() {
        if (remainingOres == 0) {
            gameOver = true;
            finishGame(true, false); // Player won
        }
    }

    /**
     * Checks whether the Excavation Mini-Game should end due to the player losing all remaining lives.
     *
     * If the player has zero or fewer lives (`remainingLives <= 0`), the game is marked as over
     * by setting the `gameOver` flag to `true`. Afterward, the game finishes with a call to
     * {@link #finishGame(boolean, boolean)} using `false` for both parameters, indicating that
     * the player lost and did not quit early.
     */
    private void checkGameOver() {
        if (remainingLives <= 0) {
            gameOver = true;
            finishGame(false, false); // Player lost
        }
    }

    /**
     * Ends the game, pauses it, and displays results or quit messages.
     *
     * @param success   Whether the game was won.
     * @param earlyQuit Whether the player quit early.
     */
    private void finishGame(boolean success, boolean earlyQuit) {
        int foundOres = getFoundOres();
        pauseState = true; // Pause the game

        // Send messages based on game outcome
        if (earlyQuit) {
            PathosCraft.LOGGER.info("You quit early!");
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("You quit early! Ores found: " + foundOres));
        } else if (success) {
            PathosCraft.LOGGER.info("Congratulations, you won!");
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Congratulations, you won! Total ores found: " + foundOres));
        } else {
            PathosCraft.LOGGER.info("Better luck next time!");
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Better luck next time! Total ores found: " + foundOres));
        }

        PacketDistributor.sendToServer(new FinishedExcavationMiniGame(foundOres));
    }

    /**
     * Renders the Excavation Mini-Game's graphical user interface, including the game state,
     * player lives, and victory or failure messages. This method handles drawing the current
     * game board, remaining lives, and any relevant messages such as pause state or game result.
     *
     * @param guiGraphics The graphics context used to render the UI elements.
     * @param mouseX The current x-coordinate of the mouse pointer within the screen.
     * @param mouseY The current y-coordinate of the mouse pointer within the screen.
     * @param partialTicks The time elapsed since the last frame, used for rendering animations or updates.
     */
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        renderOreTextures(guiGraphics);
        // Display lives remaining
        guiGraphics.drawString(minecraft.font, "Lives: " + remainingLives, 10, 10, 0xFFFFFF);

        // Display pause or game-over messages
        if (pauseState) {
            int centerX = width / 2;
            int centerY = height / 2;

            String resultMessage;
            int resultColor;

            int foundOres = getFoundOres();
            if (gameOver) {
                // Display game result based on success or failure
                if (remainingLives > 0 || remainingOres == 0) {
                    resultMessage = "Amazing! You found " + foundOres + " veins of ore!";
                    resultColor = Color.GREEN.getRGB();
                } else if (foundOres >= 1) {
                    resultMessage = "Not bad, you found " + foundOres + " veins of ore!";
                    resultColor = Color.ORANGE.getRGB();
                } else {
                    resultMessage = "Better luck next time!";
                    resultColor = Color.RED.getRGB();
                }

                String exitPrompt = "Press 'E' or 'Escape' to exit and claim your rewards.";

                // Render result message
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(centerX, centerY - 20, 0);
                guiGraphics.pose().scale(1.8f, 1.8f, 1.0f); // Fixed scaling
                guiGraphics.drawString(minecraft.font, resultMessage, -(minecraft.font.width(resultMessage) / 2), 0, resultColor);
                guiGraphics.pose().popPose();

                // Render prompt message
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(centerX, centerY + 20, 0);
                guiGraphics.pose().scale(1.4f, 1.4f, 1.0f); // Fixed scaling
                guiGraphics.drawString(minecraft.font, exitPrompt, -(minecraft.font.width(exitPrompt) / 2), 0, resultColor);
                guiGraphics.pose().popPose();
            }
        }
    }

    /**
     * Calculates the number of "ore" tiles that have been revealed on the game board.
     *
     * The method iterates through the revealed states and board grid to count the
     * total tiles marked as "ore" and revealed by the player.
     *
     * @return The total number of revealed "ore" tiles on the board.
     */
    private int getFoundOres() {
        int foundOres = 0;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (revealedStates[row][col] && "ore".equals(board[row][col])) {
                    foundOres++;
                }
            }
        }
        return foundOres;
    }

    /**
     * Renders the textures for revealed "ore" tiles on the game board.
     *
     * This method uses the grid layout and the revealed states of the game tiles
     * to identify and render "ore" textures at their corresponding positions on
     * the graphical interface. Textures for "ore" tiles are centered within
     * their respective grid cells.
     *
     * @param guiGraphics The graphical context used for rendering the textures.
     */
    private void renderOreTextures(GuiGraphics guiGraphics) {
        int buttonSize = Math.min(width, height) / (GRID_SIZE + 2);
        int textureWidth = (int) (buttonSize * 0.8);
        int textureHeight = (int) (buttonSize * 0.8);
        int startX = (this.width - (GRID_SIZE * buttonSize)) / 2;
        int startY = (this.height - (GRID_SIZE * buttonSize)) / 2;

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (revealedStates[row][col] && board[row][col].equals("ore")) {
                    int x = startX + col * buttonSize;
                    int y = startY + row * buttonSize;
                    int centerX = x + (buttonSize - textureWidth) / 2;
                    int centerY = y + (buttonSize - textureHeight) / 2;

                    guiGraphics.blit(SADNESS_TEXTURE, centerX, centerY, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
                }
            }
        }
    }

    /**
     * Handles key press events during the Excavation Mini-Game. Determines behavior
     * based on whether the game is in a paused state or not, and the specific key
     * pressed (such as 'E' or 'ESCAPE').
     *
     * When the game is paused, pressing 'E' or 'ESCAPE' exits the screen. When the
     * game is not paused, pressing these keys ends the game with an early quit status.
     *
     * @param keyCode   The numerical code of the key that was pressed.
     * @param scanCode  The scan code representing the physical key location on the keyboard.
     * @param modifiers Bitmask indicating any modifier keys (e.g., Shift, Ctrl) held during the key press.
     * @return true if the key press was handled by this method; otherwise, false if passed to the parent class.
     */
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (pauseState && (keyCode == GLFW.GLFW_KEY_E || keyCode == GLFW.GLFW_KEY_ESCAPE)) {
            Minecraft.getInstance().setScreen(null); // Exit the screen when paused
            return true;
        }

        if (!pauseState && (keyCode == GLFW.GLFW_KEY_E || keyCode == GLFW.GLFW_KEY_ESCAPE)) {
            finishGame(false, true); // Mark as early quit
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}