import tabletoprg.lpanel
import tabletoprg.rpanel
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*


class GameUIManager(private val game: Game, private val movesPanel: JPanel, private val capturedPanel: JPanel) {
    private val movesListModel = DefaultListModel<String>()
    private val movesList = JList(movesListModel)


    init {
        movesPanel.layout = BorderLayout()
        movesPanel.minimumSize = Dimension(128, 256)
        movesPanel.add(JLabel("Move History"), BorderLayout.NORTH)

        capturedPanel.layout = BoxLayout(capturedPanel, BoxLayout.Y_AXIS)
        capturedPanel.minimumSize = Dimension(128, 256)
        for (color in game.teams) {
            val capPanel = JScrollPane()
            capPanel.add(JLabel("${color.name} Captured Pieces: "))
            capturedPanel.add(capPanel)
        }

        val scrollPane = JScrollPane(movesList)
        movesPanel.add(scrollPane, BorderLayout.CENTER)
    }


    fun updateMoves() {
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