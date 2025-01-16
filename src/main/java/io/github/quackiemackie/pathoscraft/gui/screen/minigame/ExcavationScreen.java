package io.github.quackiemackie.pathoscraft.gui.screen.minigame;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.gui.parts.miniGames.ExcavationButton;
import io.github.quackiemackie.pathoscraft.gui.parts.miniGames.InformationButton;
import io.github.quackiemackie.pathoscraft.network.payload.minigames.excavation.FinishedExcavationMiniGame;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ExcavationScreen extends Screen {

    private static final int GRID_SIZE = 5; // Size of the grid (5x5)
    private static final int MAX_LIVES = 6; // Maximum lives for the player
    private static final int MAX_ORES = 5; // Maximum number of ores on the board

    private static final ResourceLocation STONE_PICKAXE_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/item/stone_pickaxe.png");
    private static final ResourceLocation BROKEN_STONE_PICKAXE_TEXTURE = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "textures/minigame/broken_stone_pickaxe.png");

    private final String[][] board = new String[GRID_SIZE][GRID_SIZE];
    private final boolean[][] revealedStates = new boolean[GRID_SIZE][GRID_SIZE];
    private final List<ExcavationButton> buttons = new ArrayList<>();

    private final ItemStack rewardItem;
    private int remainingLives = MAX_LIVES;
    private int remainingOres;
    private boolean gameOver = false;
    private boolean pauseState = false;

    public ExcavationScreen(ItemStack rewardItem) {
        super(Component.translatable("screen.title.pathoscraft.excavation_mini_game"));
        this.rewardItem = rewardItem;
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
                    button.setRevealed(true);
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

        if (earlyQuit) {
            PathosCraft.LOGGER.info("You quit early!");
            Minecraft.getInstance().player.sendSystemMessage(
                    Component.translatable("screen.widget.pathoscraft.excavation_mini_game.quit_early", foundOres));
        } else if (success) {
            PathosCraft.LOGGER.info("Congratulations, you won!");
            Minecraft.getInstance().player.sendSystemMessage(
                    Component.translatable("screen.widget.pathoscraft.excavation_mini_game.congratulations", foundOres));
        } else {
            PathosCraft.LOGGER.info("Better luck next time!");
            Minecraft.getInstance().player.sendSystemMessage(
                    Component.translatable("screen.widget.pathoscraft.excavation_mini_game.better_luck", foundOres));
        }

        //Only send payload if found ores = over 0
        if (foundOres > 0) {
            PacketDistributor.sendToServer(new FinishedExcavationMiniGame(foundOres, rewardItem));
        }
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
        renderLives(guiGraphics);

        InformationButton infoButton = new InformationButton(10, 10);
        String[] instructions = Component.translatable("screen.widget.pathoscraft.excavation_mini_game.instructions").getString().split("\n");
        infoButton.setHoverInfo(instructions);
        this.addRenderableWidget(infoButton);

        // Display pause or game-over messages
        if (pauseState) {
            int centerX = width / 2;
            int centerY = height / 2;

            String resultMessage;
            int resultColor;

            int foundOres = getFoundOres();
            if (gameOver) {
                if (remainingLives > 0 || remainingOres == 0) {
                    resultMessage = Component.translatable("screen.widget.pathoscraft.excavation_mini_game.amazing", foundOres).getString();
                    resultColor = Color.GREEN.getRGB();
                } else if (foundOres >= 1) {
                    resultMessage = Component.translatable("screen.widget.pathoscraft.excavation_mini_game.not_bad", foundOres).getString();
                    resultColor = Color.ORANGE.getRGB();
                } else {
                    resultMessage = Component.translatable("screen.widget.pathoscraft.excavation_mini_game.better_luck_next_time").getString();
                    resultColor = Color.RED.getRGB();
                }

                String exitPrompt = Component.translatable("screen.widget.pathoscraft.excavation_mini_game.exit_prompt",
                minecraft.options.keyInventory.getKey().getDisplayName().getString()).getString();

                // Render result message
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(centerX, centerY - 20, 300.0f);
                guiGraphics.pose().scale(1.8f, 1.8f, 1.0f);
                guiGraphics.drawString(minecraft.font, resultMessage, -(minecraft.font.width(resultMessage) / 2), 0, resultColor);
                guiGraphics.pose().popPose();

                // Render the "exit" prompt
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(centerX, centerY + 20, 300.0f);
                guiGraphics.pose().scale(1.4f, 1.4f, 1.0f);
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
     * Renders textures for "ore" tiles that have completed their animation in the Excavation Mini-Game.
     *
     * This method iterates through the list of buttons and checks for buttons that
     * represent "ore" and have completed their animation. For such buttons, it calculates
     * the position and scaling for drawing the corresponding "ore" textures, and renders
     * the textures using the provided graphics context.
     *
     * @param guiGraphics The graphics context used to render "ore" textures onto the screen.
     */
    private void renderOreTextures(GuiGraphics guiGraphics) {
        int buttonSize = Math.min(width, height) / (GRID_SIZE + 2);
        int textureWidth = (int) (buttonSize * 0.8);
        int textureHeight = (int) (buttonSize * 0.8);
        int startX = (this.width - (GRID_SIZE * buttonSize)) / 2;
        int startY = (this.height - (GRID_SIZE * buttonSize)) / 2;

        for (ExcavationButton button : buttons) {
            if (button.isAnimationCompleted() && "ore".equals(button.getType())) {
                int x = startX + button.getCol() * buttonSize;
                int y = startY + button.getRow() * buttonSize;
                int centerX = x + buttonSize / 2;
                int centerY = y + buttonSize / 2;

                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(centerX, centerY, 0);

                float scaleX = (float) textureWidth / 16.0F;
                float scaleY = (float) textureHeight / 16.0F;
                guiGraphics.pose().scale(scaleX, scaleY, 1.0F);
                guiGraphics.pose().translate(-8, -8, 0);

                guiGraphics.renderItem(rewardItem, 0, 0);
                guiGraphics.pose().popPose();
            }
        }
    }

    /**
     * Renders the player's remaining lives as stone pickaxes along the left side of the screen.
     * When a life is lost, the corresponding pickaxe is replaced with a broken pickaxe texture.
     *
     * Lives are displayed vertically, with the first life starting from the top left of the screen.
     *
     * @param guiGraphics The graphics context used to render the pickaxes.
     */
    private void renderLives(GuiGraphics guiGraphics) {
        int pickaxeSize = height / 15;
        int margin = pickaxeSize / 4;
        int startX = 10;
        int startY = 30;

        for (int i = 0; i < MAX_LIVES; i++) {
            ResourceLocation texture = (i < remainingLives) ? STONE_PICKAXE_TEXTURE : BROKEN_STONE_PICKAXE_TEXTURE;
            int yOffset = startY + i * (pickaxeSize + margin);
            guiGraphics.blit(texture, startX, yOffset, 0, 0, pickaxeSize, pickaxeSize, pickaxeSize, pickaxeSize);
        }
    }

    /**
     * Handles key press events for the Excavation Mini-Game screen.
     * Depending on the game state, this method allows the user to exit
     * the screen or finish the game early when certain keys are pressed.
     *
     * @param keyCode   The key code of the pressed key.
     * @param scanCode  The scan code of the pressed key.
     * @param modifiers The bitmask of modifier keys pressed (e.g., Shift, Ctrl).
     * @return true if the key press was handled; false otherwise.
     */
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        int inventoryKeyCode = minecraft.options.keyInventory.getKey().getValue();

        if (pauseState && (keyCode == inventoryKeyCode || keyCode == GLFW.GLFW_KEY_ESCAPE)) {
            minecraft.setScreen(null); // Exit the screen when paused
            return true;
        }

        if (!pauseState && (keyCode == inventoryKeyCode || keyCode == GLFW.GLFW_KEY_ESCAPE)) {
            finishGame(false, true); // Mark as early quit
            minecraft.setScreen(null);
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}