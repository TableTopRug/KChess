package tabletoprg

import AIPlayer
import Board
import Chess
import ChessGameUIManager
import GameUIManager
import HumanPlayer
import java.awt.*
import javax.swing.*
import java.awt.event.ActionListener


val frame = JFrame("Games App")

val lpanel = JPanel()
val mpanel = JPanel()
val rpanel = JPanel()


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

fun main(args: Array<String>) {
    initUI()

    val game = Chess(
        listOf(
            HumanPlayer(COLOR.WHITE),
            HumanPlayer(COLOR.BLACK)
//            AIPlayer(COLOR.BLACK)
        )
    )

    // Set game reference in board
    game.board.game = game

    mpanel.add(game.board)

    val uiManager = ChessGameUIManager(game, rpanel, lpanel)

    frame.pack()

    frame.isVisible = true

    game.addMoveListener {
        SwingUtilities.invokeLater {
            uiManager.updateMoves()
        }
    }

    // Start AI turn loop (in a separate thread so UI doesn't freeze)
    Thread {
        while (true) {
            Thread.sleep(1000)  // Wait 1 second between moves
            val currentPlayer = game.players.find { it.color == game.getCurrentTurn() }
            if (currentPlayer is AIPlayer) {
                SwingUtilities.invokeLater {
                    currentPlayer.takeTurn(game)
                }
            }
        }
    }.start()
}