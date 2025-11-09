package tabletoprg


import AIPlayer
import Board
import Chess
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

    lpanel.minimumSize = Dimension(256, 64)
    rpanel.minimumSize = Dimension(256, 64)

    lpanel.background = Color.RED
    mpanel.background = Color.GREEN
    rpanel.background = Color.BLUE

    val whiteCaps: JPanel = JPanel()
    whiteCaps.add(JLabel("White Captured Pieces: "))

    val blackCaps: JPanel = JPanel()
    blackCaps.add(JLabel("Black Captured Pieces: "))

    val splitPanel = JSplitPane(JSplitPane.VERTICAL_SPLIT, whiteCaps, blackCaps)

    lpanel.add(splitPanel)
    rpanel.add(JLabel("Moves: "))

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

    frame.pack()

    frame.isVisible = true

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