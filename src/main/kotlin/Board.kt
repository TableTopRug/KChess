import java.awt.Color
import java.awt.Container
import java.awt.Dimension
import java.awt.GridLayout
import javax.swing.BorderFactory
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import kotlin.collections.HashMap


class Board(val size: Short): JPanel() {
    val pieces: List<Piece> = mutableListOf();
    val board: HashMap<Cell, Piece?> = HashMap()

    init {
        this.setSize(Dimension(128, 128));
        this.layout = GridLayout(size.toInt(), size.toInt())
        var black = true

        for (x: Char in 'a' until 'a' + size.toInt()) {
            black = !black
            for (y in 1.toShort().rangeTo(size)) {
                val cell = Cell(y.toShort(), x)

                cell.size = Dimension(16, 16)
                cell.background = if (black) Color.BLACK else Color.WHITE
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cell.add(JLabel("$x, $y"));

                board[cell] = null;
                this.add(cell)
                black = !black
            }
        }

        val startCol = 'a'..'h'
        val startRow = shortArrayOf(1, 2, 7, 8)

//        for (y in startRow) {
//            when (y.toInt()) {
//                1 -> {
//                    board.set(Cell(y, 'a'), Piece(PieceType.ROOK, false))
//                    board.set(Cell(y, 'b'), Piece(PieceType.KNIGHT, false))
//                    board.set(Cell(y, 'c'), Piece(PieceType.BISHOP, false))
//                    board.set(Cell(y, 'd'), Piece(PieceType.QUEEN, false))
//                    board.set(Cell(y, 'e'), Piece(PieceType.KING, false))
//                    board.set(Cell(y, 'f'), Piece(PieceType.BISHOP, false))
//                    board.set(Cell(y, 'g'), Piece(PieceType.KNIGHT, false))
//                    board.set(Cell(y, 'h'), Piece(PieceType.ROOK, false))
//                }
//                2 -> {
//                    for (x: Char in startCol) {
//                        board.set(Cell(y, x), Piece(PieceType.PAWN, false))
//                    }
//                }
//                7 -> {
//                    for (x: Char in startCol) {
//                        board.set(Cell(y, x), Piece(PieceType.PAWN, true))
//                    }
//                }
//                8 -> {
//                    board.set(Cell(y, 'a'), Piece(PieceType.ROOK, true))
//                    board.set(Cell(y, 'b'), Piece(PieceType.KNIGHT, true))
//                    board.set(Cell(y, 'c'), Piece(PieceType.BISHOP, true))
//                    board.set(Cell(y, 'e'), Piece(PieceType.KING, true))
//                    board.set(Cell(y, 'd'), Piece(PieceType.QUEEN, true))
//                    board.set(Cell(y, 'f'), Piece(PieceType.BISHOP, true))
//                    board.set(Cell(y, 'g'), Piece(PieceType.KNIGHT, true))
//                    board.set(Cell(y, 'h'), Piece(PieceType.ROOK, true))
//                }
//            }
//        }
    }
}

data class Cell(val row: Short, val col: Char): JPanel()