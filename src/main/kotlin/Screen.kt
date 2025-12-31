import javax.swing.JFrame
import javax.swing.JLayeredPane
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
enum class GameScreen {
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
open class ScreenManager(private val frame: JFrame) {
    private var currentScreen: GameScreen = GameScreen.MAIN_MENU
    //    private val root = frame.contentPane as JLayeredPane
    private val screens: MutableMap<GameScreen, JPanel> = mutableMapOf()

    /**
     * Prepares a screen before displaying it.
     * Can be overridden by subclasses to perform initialization tasks.
     *
     * @param screenType The type of screen to prepare
     * @param args Optional arguments for screen initialization
     */
    open fun prepareScreen(screenType: GameScreen, vararg args: Any?) {
        // Override in subclasses if needed
    }

    /**
     * Registers a screen panel with the manager.
     *
     * @param screenType The type of screen to register
     * @param panel The JPanel to associate with this screen
     */
    fun registerScreen(screenType: GameScreen, panel: JPanel) {
        screens[screenType] = panel
    }

    /**
     * Switches to the specified screen, removing all other screens.
     *
     * @param screenType The type of screen to switch to
     */
    open fun switchTo(screenType: GameScreen) {
        prepareScreen(screenType)

        frame.contentPane.removeAll()

        screens[screenType]?.let {
            frame.contentPane.add(it, java.awt.BorderLayout.CENTER)
        }

        frame.revalidate()
        frame.repaint()

        currentScreen = screenType
    }

    /**
     * Gets the currently displayed screen.
     *
     * @return The current [GameScreen] type
     */
    fun getCurrentScreen(): GameScreen = currentScreen

    /**
     * Resets the screen manager to the initial state.
     * Clears all displayed content and returns to the main menu.
     */
    open fun reset() {
        frame.contentPane.removeAll()
        frame.revalidate()
        frame.repaint()
        currentScreen = GameScreen.MAIN_MENU
    }
}