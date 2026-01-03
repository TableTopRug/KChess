package ui.chess

import game.COLOR
import ui.GameScene
import ui.SceneManager
import ui.UIManager
import java.awt.*
import javax.swing.*

/**
 * Chess-specific screen manager.
 * Handles chess game screens including the game over screen with winner display.
 *
 * @property frame The main application frame
 * @property gameOverScreen The game over overlay panel
 * @property isGameOverPrepared Flag indicating if the game over screen is prepared
 * @author TableTopRug
 * @version 1.0
 */
class ChessSceneManager(override val uIManager: ChessUIManager): SceneManager(uIManager) {
    init {
        gameOverScreen = createGameOverScreen()
    }

    /**
     * Prepares a screen before displaying it.
     * For game over screen, requires a COLOR argument representing the winner.
     *
     * @param screenType The type of screen to prepare
     * @param args For GAME_OVER: [0] must be the winning COLOR
     * @throws IllegalArgumentException if GAME_OVER is prepared without a COLOR argument
     */
    override fun prepareScene(screenType: GameScene, vararg args: Any?) {
        when(screenType) {
            GameScene.GAME_OVER -> {
                if (args.isEmpty() || args[0] !is COLOR) {
                    throw IllegalArgumentException("prepareGameOverScreen requires a COLOR argument representing the winner.")
                }

                val winner = args[0] as COLOR
                prepareGameOverScreen(winner)
            }
            else -> super.prepareScene(screenType, *args)
        }
    }

    /**
     * Switches to the specified screen.
     * For game over screen, displays as an overlay with semi-transparent background.
     *
     * @param screenType The type of screen to switch to
     * @throws IllegalStateException if GAME_OVER screen is not prepared
     */
    override fun switchTo(sceneType: GameScene) {
        when (sceneType) {
            GameScene.GAME_OVER -> {
                if (!isGameOverPrepared) {
                    throw IllegalStateException("Game Over screen not prepared. Call prepareGameOverScreen(winner: COLOR) before switching to GAME_OVER.")
                }

                super.registerScene(GameScene.GAME_OVER, gameOverScreen)
                super.switchTo(sceneType)
            }
            else -> {
                super.switchTo(sceneType)
            }
        }
    }

    /**
     * Creates the game over screen panel with semi-transparent overlay.
     * Includes buttons for navigation and win/loss display.
     *
     * @return JPanel representing the game over screen
     */
    fun createGameOverScreen(): JPanel {
        val overlay = JPanel(GridBagLayout())
        overlay.background = Color(0, 0, 0, 180)  // Semi-transparent black
//        overlay.bounds = frame.bounds

        gbc.gridx = 0
        gbc.insets = Insets(10, 10, 10, 10)

        gbc.gridy = 0
        val newGameBtn = JButton("Go to Main Menu")

        newGameBtn.addActionListener {
            this.switchTo(GameScene.MAIN_MENU)
        }

        gbc.gridy = 1
        overlay.add(newGameBtn, gbc)

        return overlay
    }

    /**
     * Prepares the game over screen by displaying the winner name.
     * Must be called before switching to the GAME_OVER screen.
     *
     * @param winner The color of the winning player
     */
    fun prepareGameOverScreen(winner: COLOR) {
        val label = JLabel("${winner.name} Wins!")
        label.font = Font("Arial", Font.BOLD, 32)
        label.foreground = Color.WHITE

        gbc.gridy = 0
        gameOverScreen.add(label, gbc)

        isGameOverPrepared = true
    }

    /**
     * Resets the screen manager to initial state.
     * Clears the game over screen and resets preparation flag.
     */
    override fun reset() {
        isGameOverPrepared = false
        gameOverScreen.removeAll()
        super.reset()
    }
}