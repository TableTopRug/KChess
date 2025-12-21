import java.awt.Color
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.Image
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*


abstract class Board(val size: Short): JPanel() {
    val pieces: List<Piece> = mutableListOf();
    val highlightedCells: MutableList<Cell> = mutableListOf()

    internal val board: HashMap<Cell, Piece?> = HashMap()

    init {
        this.preferredSize = Dimension(512, 512)
        this.layout = GridLayout(size.toInt(), size.toInt())
    }

    abstract fun addPieceOnClick(cell: Cell, piece: Piece)

    fun removeAllHighlights(cells: List<Cell>) {
        val cellsList = ArrayList(cells)

        cellsList.forEach{cell ->
            highlightedCells.remove(cell)
            cell.deHighlight()
        }
    }

    fun doGetMovementOptions(from: Cell, p: Piece, game: Game? = null) {
        val posMoves = getPieceMovementOptions(from, p)

        for  (cell: Cell in posMoves) {
            cell.highlight {
                if (game != null) {
                    // Use game's makeMove to handle turn switching
                    val currentPlayer = game.players.find { it.color == p.color }
                    if (currentPlayer != null && game.makeMove(from, cell, currentPlayer)) {
                        removeAllHighlights(posMoves)
                    }
                } else {
                    // Fallback to direct move (for non-game usage)
                    doPieceMove(from, cell)
                    removeAllHighlights(posMoves)
                }
            }
            highlightedCells.add(cell)
        }
    }

    fun getPieceMovementOptions(c: Cell, p: Piece): List<Cell> {
        var positions = p.type.movement(c, p)
        val pos = mutableListOf<Cell>()

        for (position in positions) {
            if (position.first in 'a' until 'a' + size.toInt() && position.second in 1.toShort().rangeTo(size)) {
                val cell = board.keys.find { it.col == position.first && it.row == position.second }
                if (cell != null && p.type.validateMove(this.board, c, cell)) {
                    pos.add(cell)
                }
            }
        }

        return pos
    }

    open fun doPieceMove(from: Cell, to: Cell): Move {
        val piece = board[from]

        board[to] = piece
        board[from] = null

        to.removeAll()
        if (piece != null) {
            var iLab = JLabel(ImageIcon(piece.image().getScaledInstance(to.preferredSize.width, to.preferredSize.height, Image.SCALE_SMOOTH)));
            iLab.setBounds(0, 0, to.preferredSize.width, to.preferredSize.height);
            to.add(iLab, JLayeredPane.PALETTE_LAYER)
            piece.moves++
            addPieceOnClick(to, piece)
        }
        from.removeAll()

        to.revalidate()
        to.repaint()
        from.revalidate()
        from.repaint()

        return Move(from, to, piece!!)
    }

    fun getBoardState(): HashMap<Cell, Piece?> {
        return board
    }

}



data class Cell(val row: Short, val col: Char): JLayeredPane() {
    init {
        this.layout = null;
        this.minimumSize = Dimension(16, 16)
        this.preferredSize = Dimension(64, 64)

        this.isOpaque = true
    }
}

fun Cell.highlight(op: () -> Unit) {
    val button = JButton()
    button.size = this.preferredSize
    button.background = Color(255, 255, 100, 96)
    button.isOpaque = true
    button.icon = null
    button.rolloverIcon = null
    button.pressedIcon = null
    button.disabledIcon = null
    button.isContentAreaFilled = true
    button.isDefaultCapable = false  // Disable default button appearance
    button.isFocusPainted = false   // Disable focus painting
    button.isRolloverEnabled = false // Disable rollover effects
    button.addActionListener { op() }
    button.setBounds(0, 0, this.preferredSize.width, this.preferredSize.height);
    this.add(button)
    this.setLayer(button, JLayeredPane.MODAL_LAYER)

    revalidate()
    repaint()
}

fun Cell.deHighlight() {
//    System.out.println("De-highlighting cell $col$row")
    for (layer in getComponentsInLayer(JLayeredPane.MODAL_LAYER)) {
        remove(layer)
    }

    // Force complete refresh
//    invalidate()
    revalidate()
    repaint()
}