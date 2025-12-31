import javax.swing.JFrame
import javax.swing.JLayeredPane
import javax.swing.JPanel


enum class GameScreen {
    MAIN_MENU,
    GAME_SELECT,
    IN_GAME,
    GAME_OVER,
    SETTINGS
}

open class ScreenManager(private val frame: JFrame) {
    private var currentScreen: GameScreen = GameScreen.MAIN_MENU
//    private val root = frame.contentPane as JLayeredPane
    private val screens: MutableMap<GameScreen, JPanel> = mutableMapOf()


    open fun prepareScreen(screenType: GameScreen, vararg args: Any?) {
        // Override in subclasses if needed
    }

    fun registerScreen(screenType: GameScreen, panel: JPanel) {
        screens[screenType] = panel
    }

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

    fun getCurrentScreen(): GameScreen = currentScreen

    open fun reset() {
        frame.contentPane.removeAll()
        frame.revalidate()
        frame.repaint()
        currentScreen = GameScreen.MAIN_MENU
    }
}