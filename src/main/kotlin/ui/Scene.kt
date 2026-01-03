package ui

import game.Game
import java.awt.GridBagConstraints
import java.util.Observable
import javax.swing.JPanel

/**
 * Enumeration of all possible game screens.
 *
 * @property MAIN_MENU The main menu screen
 * @property GAME_SELECT Screen for selecting which game to play
 * @property IN_GAME The active game screen
 * @property GAME_OVER The game over/results screen
 * @property SETTINGS The settings/options screen
 * @author TableTopRug
 * @version 1.0
 */
enum class GameScene {
    MAIN_MENU,
    GAME_SELECT,
    IN_GAME,
    GAME_OVER,
    SETTINGS
}

/**
 * Manages screen navigation and display in the application.
 * Handles switching between different game screens and registering UI panels.
 *
 * @property frame The main application JFrame
 * @property currentScreen The currently displayed screen type
 * @property screens Map of registered screens
 * @author TableTopRug
 * @version 1.0
 */
open class SceneManager(open val uIManager: UIManager) {
    private var currentScreen: GameScene = GameScene.MAIN_MENU

    // Map of registered screens: [left, center, right]
    private val screens: MutableMap<GameScene, Array<JPanel?>> = mutableMapOf()

    internal var gbc = GridBagConstraints()

    /** Flag indicating if the game over screen has been prepared with a winner */
    var isGameOverPrepared: Boolean = false
    /** The game over screen panel */
    var gameOverScreen: JPanel = JPanel()


    /**
     * Prepares a screen before displaying it.
     * Can be overridden by subclasses to perform initialization tasks.
     *
     * @param screenType The type of screen to prepare
     * @param args Optional arguments for screen initialization
     */
    open fun prepareScene(screenType: GameScene, vararg args: Any?) {
        when (screenType) {
            GameScene.GAME_OVER -> {
                throw NotImplementedError("prepareScreen for GAME_OVER must be implemented in subclass.")
            }
            else -> {
                // Default implementation does nothing
            }
        }
    }

    /**
     * Switches to the specified screen, removing all other screens.
     *
     * @param sceneType The type of screen to switch to
     */
    open fun switchTo(sceneType: GameScene) {
        prepareScene(sceneType)

        uIManager.clearCenter()

        screens[sceneType]?.let {
            if (it[0] != null) {
                uIManager.setLeftPanelContent(it[0]!!)
                uIManager.showLeftPanel()
            } else {
                uIManager.hideLeftPanel()
            }

            if (it[1] != null) {
                uIManager.setCenterContent(it[1]!!)
            } else {
                throw IllegalStateException("Center panel cannot be null for screen: $sceneType")
            }

            if (it[2] != null) {
                uIManager.setRightPanelContent(it[2]!!)
                uIManager.showRightPanel()
            } else {
                uIManager.hideRightPanel()
            }
        }

        currentScreen = sceneType
    }

    /**
     * Resets the screen manager to the initial state.
     * Clears all displayed content and returns to the main menu.
     */
    open fun reset() {
        uIManager.clearCenter()
        this.switchTo(GameScene.MAIN_MENU)

        currentScreen = GameScene.MAIN_MENU
    }

    /**
     * Registers a screen panel with the manager.
     *
     * @param screenType The type of screen to register
     * @param panel The JPanel to associate with this screen
     */
    fun registerScene(screenType: GameScene, panel: JPanel) {
        screens[screenType] = arrayOf(null, panel, null);
    }

    /**
     * Registers a screen panel with the manager.
     *
     * @param screenType The type of screen to register
     * @param panel The JPanel to associate with this screen
     */
    fun registerScene(screenType: GameScene, panels: Array<JPanel?>) {
        screens[screenType] = panels
    }

    /**
     * Gets the currently displayed scene.
     *
     * @return The current [GameScene] type
     */
    fun getCurrentScene(): GameScene = currentScreen
}