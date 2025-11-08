import java.awt.Color
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.Image
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*


class Board(val size: Short): JPanel() {
    val pieces: List<Piece> = mutableListOf();
    val board: HashMap<Cell, Piece?> = HashMap()
    val highlightedCells: MutableList<Cell> = mutableListOf()

    init {
        this.preferredSize = Dimension(512, 512)
        this.layout = GridLayout(size.toInt(), size.toInt())
        var black = true

        for (y in 1.toShort().rangeTo(size)) {
            black = !black
            for (x: Char in 'a' until 'a' + size.toInt()) {
                val cell = Cell(y.toShort(), x)

//                cell.size = Dimension(16, 16)
                cell.background = if (black) Color.BLACK else Color.WHITE
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//                cell.add(JLabel("$x, $y"));

                board[cell] = null;
                this.add(cell)
                black = !black
            }
        }

        val startCol = 'a'..'h'
        val startRow = shortArrayOf(1, 2, 7, 8)

        for (y in startRow) {
            when (y.toInt()) {
                1 -> {
                    board[Cell(y, 'a')] = Piece(PieceType.ROOK, false)
                    board[Cell(y, 'b')] = Piece(PieceType.KNIGHT, false)
                    board[Cell(y, 'c')] = Piece(PieceType.BISHOP, false)
                    board[Cell(y, 'd')] = Piece(PieceType.QUEEN, false)
                    board[Cell(y, 'e')] = Piece(PieceType.KING, false)
                    board[Cell(y, 'f')] = Piece(PieceType.BISHOP, false)
                    board[Cell(y, 'g')] = Piece(PieceType.KNIGHT, false)
                    board[Cell(y, 'h')] = Piece(PieceType.ROOK, false)
                }
                2 -> {
                    for (x: Char in startCol) {
                        board[Cell(y, x)] = Piece(PieceType.PAWN, false)
                    }
                }
                7 -> {
                    for (x: Char in startCol) {
                        board[Cell(y, x)] = Piece(PieceType.PAWN, true)
                    }
                }
                8 -> {
                    board[Cell(y, 'a')] = Piece(PieceType.ROOK, true)
                    board[Cell(y, 'b')] = Piece(PieceType.KNIGHT, true)
                    board[Cell(y, 'c')] = Piece(PieceType.BISHOP, true)
                    board[Cell(y, 'e')] = Piece(PieceType.KING, true)
                    board[Cell(y, 'd')] = Piece(PieceType.QUEEN, true)
                    board[Cell(y, 'f')] = Piece(PieceType.BISHOP, true)
                    board[Cell(y, 'g')] = Piece(PieceType.KNIGHT, true)
                    board[Cell(y, 'h')] = Piece(PieceType.ROOK, true)
                }
            }
        }

        board.forEach { t, u ->
            if (u != null) {
                addPieceOnClick(t, u)
            }
        }

//        this.addComponentListener(object : ComponentAdapter() {
//            override fun componentResized(e: ComponentEvent) {
//                for (comp: JComponent in this.)
//                val size: Dimension = this.getSize()
//                originalPanel.setBounds(0, 0, size.width, size.height)
//                coverButton.setBounds(0, 0, size.width, size.height)
//            }
//        });
    }

    fun addPieceOnClick(cell: Cell, piece: Piece) {
        var iLab = JLabel(ImageIcon(piece.image().getScaledInstance(cell.preferredSize.width, cell.preferredSize.height, Image.SCALE_SMOOTH)));
//                iLab.text = "${if (u.isBlack) "Black" else "White"}_${u.type}"
        iLab.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                removeAllHighlights(highlightedCells)
                // This code executes when the JLabel is clicked
                println("Cell: ${cell.col}${cell.row}; Piece: ${if (piece.isBlack) "Black" else "White"} ${piece.type} :: ${cell.size}")
//                        JOptionPane.showMessageDialog(frame, "You clicked the label!")
                doGetMovementOptions(cell, piece)
            }
        })
        iLab.setBounds(0, 0, cell.preferredSize.width, cell.preferredSize.height);
        cell.add(iLab, JLayeredPane.PALETTE_LAYER)
    }

    fun removeAllHighlights(cells: List<Cell>) {
        val cellsList = ArrayList(cells)

        cellsList.forEach{cell ->
            highlightedCells.remove(cell)
            cell.deHighlight()
        }
    }

    fun doGetMovementOptions(from: Cell, p: Piece) {
        val posMoves = getPieceMovementOptions(from, p)

        for  (cell: Cell in posMoves) {
            cell.highlight {
                doPieceMove(from, cell)
                removeAllHighlights(posMoves)
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

    fun doPieceMove(from: Cell, to: Cell) {
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
    invalidate()
    revalidate()
    repaint()
}