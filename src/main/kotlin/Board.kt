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

    init {
        this.preferredSize = Dimension(128, 128)
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
                var iLab = JLabel(ImageIcon(u.image().getScaledInstance(t.width, t.height, Image.SCALE_SMOOTH)));
                iLab.addMouseListener(object : MouseAdapter() {
                    override fun mouseClicked(e: MouseEvent) {
                        // This code executes when the JLabel is clicked
                        println("Cell: ${t.col}${t.row}; Piece: ${if (u.isBlack) "Black" else "White"} ${u.type} :: ${t.size}")
//                        JOptionPane.showMessageDialog(frame, "You clicked the label!")
                        doGetMovementOptions(t, u)
                    }
                })
                iLab.setBounds( 0, 0, t.width, t.height);
                t.add(iLab, JLayeredPane.PALETTE_LAYER)
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

    fun removeAllHighlights(cells: List<Cell>) {
        cells.forEach{cell ->
            cell.deHighlight()
        }
    }

    fun doGetMovementOptions(cell: Cell, p: Piece) {
        val posMoves = getPieceMovementOptions(cell, p)

        for  (cell: Cell in posMoves) {
            cell.highlight { removeAllHighlights(posMoves) }
        }
    }

    fun getPieceMovementOptions(cell: Cell, p: Piece): MutableList<Cell> {
        when(p.type) {
            PieceType.PAWN -> {
                var amt = if (p.moves == 0) arrayOf(1, 2) else arrayOf(1)
                if (p.isBlack) amt.forEachIndexed { index, i -> amt[index] = -i }

                val pos: MutableList<Cell> = ArrayList()

                for (i: Int in amt) {
                    val row = (cell.row + i).toShort()
                    if (row in 1..size)
                        pos.add(Cell(row, cell.col))
                }
                return pos
            }
            PieceType.BISHOP -> TODO()
            PieceType.KNIGHT -> TODO()
            PieceType.ROOK -> TODO()
            PieceType.KING -> TODO()
            PieceType.QUEEN -> TODO()
        }
    }


}

data class Cell(val row: Short, val col: Char): JLayeredPane() {
    init {
        this.setPreferredSize(Dimension(64, 64))
    }
}

fun Cell.highlight(op: () -> Unit) {
    val button = JButton()
    button.background = Color(255, 255, 100, 96)
    button.isOpaque = false
    button.addActionListener { op() }
    button.setBounds( 0, 0, this.width, this.height);
    this.add(button, JLayeredPane.MODAL_LAYER)
}

fun Cell.deHighlight() {
    this.remove(JLayeredPane.MODAL_LAYER)
}