package board

import game.Game
import game.Move
import piece.Piece
import java.awt.Color
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.Image
import javax.swing.*

/**
 * Abstract base class for game boards.
 * Provides core functionality for managing board state, pieces, and piece movements.
 *
 * @property size The size of the board (e.g., 8 for chess)
 * @author TableTopRug
 * @version 1.0
 */
abstract class Board(val size: Short): JPanel() {
    /** Mapping of pieces to their current cell locations */
    val pieces: MutableMap<Piece, Cell> = mutableMapOf();

    /** List of cells currently highlighted for valid moves */
    val highlightedCells: MutableList<Cell> = mutableListOf()

    /** Internal representation of the board state: Cell -> Piece (or null) */
    internal val board: HashMap<Cell, Piece?> = HashMap()

    init {
        this.preferredSize = Dimension(512, 512)
        this.layout = GridLayout(size.toInt(), size.toInt())
    }

    /**
     * Abstract method for handling piece click events on a cell.
     * Implementation-specific for different game types.
     *
     * @param cell The cell that was clicked
     * @param piece The piece on the cell
     */
    abstract fun addPieceOnClick(cell: Cell, piece: Piece)

    /**
     * Removes all highlights from the given cells and clears the highlight list.
     *
     * @param cells List of cells to remove highlights from
     */
    fun removeAllHighlights(cells: List<Cell>) {
        val cellsList = ArrayList(cells)

        cellsList.forEach{cell ->
            highlightedCells.remove(cell)
            cell.deHighlight()
        }
    }

    /**
     * Calculates and displays all valid movement options for a piece.
     * Highlights valid target cells and sets up click handlers for moves.
     *
     * @param from The source cell
     * @param p The piece being moved
     * @param game Optional game reference for handling turn-based moves
     */
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

    /**
     * Gets all possible movement options for a piece at a given cell.
     * Filters out-of-bounds moves and applies piece-specific movement validation.
     *
     * @param c The cell the piece is on
     * @param p The piece to get movement options for
     * @return List of valid target cells
     */
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

    /**
     * Executes a piece movement from one cell to another.
     * Updates the board state and refreshes the UI.
     *
     * @param from The source cell
     * @param to The destination cell
     * @return The move that was executed
     */
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

    /**
     * Gets the current board state.
     *
     * @return HashMap of cell to piece mappings
     */
    fun getBoardState(): HashMap<Cell, Piece?> {
        return board
    }

}

/**
 * Represents a single cell on the game board.
 * Extends JLayeredPane to support layering of visual elements (piece images, highlights).
 *
 * @property row The row coordinate of the cell
 * @property col The column coordinate of the cell
 * @author TableTopRug
 * @version 1.0
 */
data class Cell(val row: Short, val col: Char): JLayeredPane() {
    /** Initializes cell dimensions and UI properties */
    init {
        this.layout = null;
        this.minimumSize = Dimension(16, 16)
        this.preferredSize = Dimension(64, 64)

        this.isOpaque = true
    }
}

/**
 * Highlights a cell with a yellow semi-transparent button and attaches a click listener.
 * Used to indicate valid move targets.
 *
 * @receiver The cell to highlight
 * @param op The operation to perform when the highlighted cell is clicked
 * @see Cell
 */
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

/**
 * Removes the highlight from a cell by removing all components in the MODAL_LAYER.
 * Refreshes the cell's display to show the underlying piece or board color.
 *
 * @receiver The cell to remove highlight from
 * @see Cell
 * @see highlight
 */
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