import chess.Chess
import chess.ChessGameUIManager
import chess.ChessScreenManager
import chess.HumanChessPlayer
import java.awt.*
import javax.swing.*

/** Main application frame */
val frame = JFrame("Games App")

/** Left panel for displaying game information */
val lpanel = JPanel()

/** Center panel for displaying the game board */
val mpanel = JPanel()

/** Right panel for displaying additional game information */
val rpanel = JPanel()

/**
 * Initializes the main UI layout with three-panel border layout.
 * Sets up the frame and panel configuration for the application.
 */
fun initUI() {
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.minimumSize = Dimension(800, 600)
    frame.setLocationRelativeTo(null);
    frame.layout = BorderLayout()

    // Set fixed sizes for side panels
    lpanel.preferredSize = Dimension(200, 600)
    lpanel.minimumSize = Dimension(50, 125)
    lpanel.maximumSize = Dimension(400, 1200)

    rpanel.preferredSize = Dimension(200, 600)
    rpanel.minimumSize = Dimension(50, 125)
    rpanel.maximumSize = Dimension(400, 1200)

    lpanel.background = Color.RED
    mpanel.background = Color.GREEN
    rpanel.background = Color.BLUE

    frame.add(lpanel, BorderLayout.WEST)
    frame.add(mpanel, BorderLayout.CENTER)
    frame.add(rpanel, BorderLayout.EAST)
}

/**
 * Entry point for the Chess application.
 * Initializes the game, UI, and starts the game loop.
 *
 * @param args Command line arguments (not used)
 */
fun main(args: Array<String>) {
    initUI()

    val screenManager = ChessScreenManager(frame)
    val game = Chess(
        listOf(
            HumanChessPlayer(COLOR.WHITE),
            HumanChessPlayer(COLOR.BLACK)
//            AIPlayer(COLOR.BLACK)
        )
    )

    // Set game reference in board
    game.board.game = game


    val mainMenuPanel = createMainMenu(screenManager)
    val gamePanel = game.board
    val gameOverPanel = screenManager.createGameOverScreen()
    
    mpanel.add(gamePanel)
    screenManager.registerScreen(GameScreen.MAIN_MENU, mainMenuPanel)
    screenManager.registerScreen(GameScreen.IN_GAME, gamePanel)
    screenManager.registerScreen(GameScreen.GAME_OVER, gameOverPanel)

    val uiManager = ChessGameUIManager(game, rpanel, lpanel)

    frame.pack()

    frame.isVisible = true

    game.addMoveListener {
        SwingUtilities.invokeLater {
            uiManager.updateMoves()
        }
    }

    game.subscribeAsUIManager(uiManager)

    //TODO: Add check to see if its an AI player or not, and add conditional logic and proper UI registration and button handleing
    // Start AI turn loop (in a separate thread so UI doesn't freeze)
    Thread {
        //TODO: Add ui stuff for game starting
        while (!game.gameOver) {
            Thread.sleep(1000)  // Wait 1 second between moves
            val currentPlayer = game.players.find { it.color == game.getCurrentTurn() }
            if (currentPlayer is AIPlayer) {
                SwingUtilities.invokeLater {
                    currentPlayer.takeTurn(game)
                }
            }
        }
        screenManager.prepareScreen(GameScreen.GAME_OVER, game.winner)
    }.start()
}

/**
 * Creates and returns the game panel.
 * (Currently not fully implemented)
 *
 * @return JPanel for the game view
 */
fun createGamePanel(): JPanel {

}

/**
 * Creates and returns the main menu panel.
 * (Currently not fully implemented)
 *
 * @param screenManager The screen manager for handling navigation
 * @return JPanel for the main menu
 */
fun createMainMenu(screenManager: ScreenManager): JPanel {

}
