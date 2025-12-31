import java.awt.BorderLayout
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


    init {
        movesPanel.layout = BorderLayout()
        movesPanel.minimumSize = Dimension(128, 256)
        movesPanel.add(JLabel("Move History"), BorderLayout.NORTH)

        val scrollPane = JScrollPane(movesList)
        movesPanel.add(scrollPane, BorderLayout.CENTER)
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