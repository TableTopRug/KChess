import game.Chess
import game.Game
import game.GameType
import ui.GameUIManager
import ui.UIManager
import ui.SceneManager
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JFrame

/**
 * Main application class managing the game lifecycle and UI.
 * Handles game initialization, UI management, and scene navigation.
 *
 * @property frame The main application window
 * @property currentGame The currently active game instance
 * @property uiManager The UI manager for the current game
 * @property sceneManager The scene/screen manager for navigation
 * @author TableTopRug
 * @version 1.0
 */
class GameApplication {
    /** Main application frame */
    val frame = JFrame("Games App")

    /** The currently active game instance */
    private var currentGame: Game? = null
    /** UI manager for the current game */
    private var uiManager: UIManager? = null
    /** Scene manager for screen navigation */
    private var sceneManager: SceneManager? = null

    /** Callbacks to be invoked when a game type is selected */
    private val gameSelectCallbacks = mutableListOf<(GameType) -> Unit>()

    init {
        initializeFrame()
        initializeUISceneManagers()
    }

    /**
     * Initializes the main application frame with default settings.
     * Sets window properties, layout, and positioning.
     * @internal
     */
    private fun initializeFrame() {
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.minimumSize = Dimension(800, 600)
        frame.setLocationRelativeTo(null)
        frame.layout = BorderLayout()
    }

    /**
     * Initializes the UI and scene managers.
     * Sets up the UI management system for game displays.
     * @internal
     */
    private fun initializeUISceneManagers() {
        uiManager = GameUIManager(frame)
        sceneManager = SceneManager(uiManager!!)
    }

    /**
     * Starts the application and displays the main window.
     * Initializes the game loop if a game is active.
     */
    fun start() {
        frame.isVisible = true
        startGameLoop()
    }

    /**
     * Starts the game loop thread for the current game.
     * Runs continuously while the game is active.
     * @internal
     */
    private fun startGameLoop() {
        Thread {
            while (currentGame?.gameOver == false) {
                // Game loop logic
            }
        }.start()
    }
}