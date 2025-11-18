import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Image.SCALE_SMOOTH
import javax.swing.*
import javax.swing.border.Border


class ChessGameUIManager(private val game: Chess, private val movesPanel: JPanel, private val capturedPanel: JPanel):
        GameUIManager(game, movesPanel, capturedPanel) {
    val capturePanels = mutableMapOf<COLOR, JPanel>()

    init {
        capturedPanel.layout = BoxLayout(capturedPanel, BoxLayout.Y_AXIS)
        capturedPanel.minimumSize = Dimension(128, 256)
        for (color in game.teams) {
            val colorContainer = JPanel()
            colorContainer.layout = BorderLayout()
            colorContainer.add(JLabel("${color.name} Captured Pieces: "), BorderLayout.NORTH)

            val pPanel = JPanel()
            pPanel.layout = FlowLayout(FlowLayout.LEFT, 2, 2)

            val capPanel = JScrollPane(pPanel)
            capPanel.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
            capPanel.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED

            colorContainer.add(capPanel, BorderLayout.CENTER)
            capturedPanel.add(capPanel)

            capturePanels[color] = pPanel
        }

        val scrollPane = JScrollPane(movesList)
        movesPanel.add(scrollPane, BorderLayout.CENTER)
    }

    override fun updateMoves() {
        movesListModel.clear()
        val moves = game.getFormattedMoveHistory()
        val rawMoves = game.moveHistory

        // Display moves in pairs (White, Black)
        for (i in moves.indices step 2) {
            val whitMove = moves[i]
            val blackMove = if (i + 1 < moves.size) moves[i + 1] else ""
            movesListModel.addElement("$whitMove    $blackMove")
        }

        capturePanels.forEach { it.component2().removeAll() }

        for (player in game.players) {
            val playerColor = player.color
            val capturedPieces = player.piecesCaptured
            for (piece in capturedPieces) {
                val pieceIcon = ImageIcon(piece.image().getScaledInstance(32, 32, SCALE_SMOOTH))
                val pieceLabel = JLabel(pieceIcon)
                capturePanels[playerColor]!!.add(pieceLabel)
            }
            capturePanels[playerColor]!!.revalidate()
            capturePanels[playerColor]!!.repaint()
        }

        // Auto-scroll to bottom
        movesList.ensureIndexIsVisible(movesListModel.size() - 1)
    }
}