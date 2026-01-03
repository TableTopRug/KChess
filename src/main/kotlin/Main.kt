import ui.GameScreen
import ui.ScreenManager
import java.awt.*
import javax.swing.*



/**
 * Entry point for the Chess application.
 * Initializes the game, UI, and starts the game loop.
 *
 * @param args Command line arguments (not used)
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
 * (Currently not fully implemented)
 *
 * @param screenManager The screen manager for handling navigation
 * @return JPanel for the main menu
 */
fun createMainMenu(screenManager: ScreenManager): JPanel {
    val panel = JPanel()
    val startButton = JButton("Start Game")

    startButton.addActionListener {
        screenManager.switchTo(GameScreen.IN_GAME)
    }
    panel.add(startButton)

    return panel
}
