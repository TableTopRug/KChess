package ui

import game.Game
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import javax.swing.*

/**
 * Base class for game UI management.
 * Handles display of move history and game information.
 *
 * @property game The game instance being managed
 * @property movesPanel The panel for displaying move history
 * @property infoPanel The panel for displaying game information
 * @property movesListModel The model for the moves list
 * @property movesList The JList component displaying moves
 * @author TableTopRug
 * @version 1.0
 */
open class GameUIManager(private val game: Game, private val movesPanel: JPanel, private val infoPanel: JPanel) {
    /** Data model for the move history list */
    internal val movesListModel = DefaultListModel<String>()
    /** JList component that displays move history */
    internal val movesList = JList(movesListModel)


    /** Left panel for displaying game information */
    val lpanel = JPanel()
    /** Center panel for displaying the game board */
    val mpanel = JPanel()
    /** Right panel for displaying additional game information */
    val rpanel = JPanel()


    init {
        movesPanel.layout = BorderLayout()
        movesPanel.minimumSize = Dimension(128, 256)
        movesPanel.add(JLabel("Move History"), BorderLayout.NORTH)

        val scrollPane = JScrollPane(movesList)
        movesPanel.add(scrollPane, BorderLayout.CENTER)
    }

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
     * Updates the displayed move history from the game.
     * Retrieves formatted moves and displays them in pairs (White, Black).
     * Auto-scrolls to the most recent move.
     */
    open fun updateMoves() {
        movesListModel.clear()
        val moves = game.getFormattedMoveHistory()

        // Display moves in pairs (White, Black)
        for (i in moves.indices step 2) {
            val whitMove = moves[i]
            val blackMove = if (i + 1 < moves.size) moves[i + 1] else ""
            movesListModel.addElement("$whitMove    $blackMove")
        }

        // Auto-scroll to bottom
        movesList.ensureIndexIsVisible(movesListModel.size() - 1)
    }
}