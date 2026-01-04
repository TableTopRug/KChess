import ui.GameScene
import ui.SceneManager
import java.awt.*
import javax.swing.*

/**
 * Main entry point for the KChess application.
 * Initializes the game engine, UI, and starts the application.
 *
 * @param args Command line arguments (not used)
 * @author TableTopRug
 * @version 1.0
 */
fun main(args: Array<String>) {
    val app = GameApplication()

    app.awaitPlayerTypeSelect()
    app.awaitGameSelect()

    app.onGameSelect { game ->
        app.setupGame(game)
    }

    app.start()
}

/**
 * Creates and returns the main menu panel.
 * Displays a start button that transitions to game selection.
 * (Currently not fully implemented with all menu options)
 *
 * @param sceneManager The scene manager for handling screen navigation
 * @return JPanel configured for the main menu display
 * @author TableTopRug
 * @version 1.0
 */
fun createMainMenu(sceneManager: SceneManager): JPanel {
    val panel = JPanel()
    val startButton = JButton("Start Game")

    startButton.addActionListener {
        sceneManager.switchTo(GameScene.IN_GAME)
    }
    panel.add(startButton)

    return panel
}
