import game.Chess
import game.Game
import game.GameType
import ui.GameUIManager
import ui.ScreenManager
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JFrame

class GameApplication {
    /** Main application frame */
    val frame = JFrame("Games App")

    private var currentGame: Game? = null
    private var uiManager: GameUIManager? = null
    private var screenManager: ScreenManager? = null

    private val gameSelectCallbacks = mutableListOf<(GameType) -> Unit>()

    init {
        initializeFrame()
    }


    private fun initializeFrame() {
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.minimumSize = Dimension(800, 600)
        frame.setLocationRelativeTo(null)
        frame.layout = BorderLayout()
    }

    fun onGameSelect(callback: (GameType) -> Unit) {
        gameSelectCallbacks.add(callback)
    }

    fun setupGame(gameType: GameType) {
        when (gameType) {
            GameType.CHESS -> currentGame = Chess()
            // Add other games here
        }
    }

    fun start() {
        frame.isVisible = true
        startGameLoop()
    }

    private fun startGameLoop() {
        Thread {
            while (currentGame?.gameOver == false) {
                // Game loop logic
            }
        }.start()
    }
}