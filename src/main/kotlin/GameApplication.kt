import game.Chess
import game.Game
import game.GameType
import ui.GameUIManager
import ui.UIManager
import ui.SceneManager
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JFrame

class GameApplication {
    /** Main application frame */
    val frame = JFrame("Games App")

    private var currentGame: Game? = null
    private var uiManager: UIManager? = null
    private var sceneManager: SceneManager? = null

    private val gameSelectCallbacks = mutableListOf<(GameType) -> Unit>()

    init {
        initializeFrame()
        initializeUISceneManagers()
    }


    private fun initializeFrame() {
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.minimumSize = Dimension(800, 600)
        frame.setLocationRelativeTo(null)
        frame.layout = BorderLayout()
    }

    private fun initializeUISceneManagers() {
        uiManager = GameUIManager(frame)
        sceneManager = SceneManager(uiManager!!)
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