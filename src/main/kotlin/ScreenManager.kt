import javax.swing.JFrame
import javax.swing.JPanel


enum class GameScreen {
    MAIN_MENU,
    GAME_SELECT,
    IN_GAME,
    GAME_OVER,
    SETTINGS
}

class ScreenManager(private val frame: JFrame) {
    private var currentScreen: GameScreen = GameScreen.MAIN_MENU
    private val screens: MutableMap<GameScreen, JPanel> = mutableMapOf()

    fun registerScreen(screenType: GameScreen, panel: JPanel) {
        screens[screenType] = panel
    }

    fun switchTo(screenType: GameScreen) {
        frame.contentPane.removeAll()
        screens[screenType]?.let {
            frame.contentPane.add(it)
        }
        frame.revalidate()
        frame.repaint()
        currentScreen = screenType
    }

    fun getCurrentScreen(): GameScreen = currentScreen
}