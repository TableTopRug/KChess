import kotlin.collections.HashMap


class Board(val size: Short) {
    val pieces: List<Piece> = mutableListOf();
    val board: HashMap<Cell, Piece?> = HashMap()

    init {
        for (x: Char in 'a'.rangeTo(('a' + size.toInt()))) {
            for (y in 1.toShort()..size) {
                board.set(Cell(y.toShort(), x), null);
            }
        }

        val startCol = 'a'..'h'
        val startRow = shortArrayOf(1, 2, 7, 8)

        for (y in startRow) {
            when (y.toInt()) {
                1 -> {
                    board.set(Cell(y, 'a'), Piece(PieceType.ROOK, false))
                    board.set(Cell(y, 'b'), Piece(PieceType.KNIGHT, false))
                    board.set(Cell(y, 'c'), Piece(PieceType.BISHOP, false))
                    board.set(Cell(y, 'd'), Piece(PieceType.QUEEN, false))
                    board.set(Cell(y, 'e'), Piece(PieceType.KING, false))
                    board.set(Cell(y, 'f'), Piece(PieceType.BISHOP, false))
                    board.set(Cell(y, 'g'), Piece(PieceType.KNIGHT, false))
                    board.set(Cell(y, 'h'), Piece(PieceType.ROOK, false))
                }
                2 -> {
                    for (x: Char in startCol) {
                        board.set(Cell(y, x), Piece(PieceType.PAWN, false))
                    }
                }
                7 -> {
                    for (x: Char in startCol) {
                        board.set(Cell(y, x), Piece(PieceType.PAWN, true))
                    }
                }
                8 -> {
                    board.set(Cell(y, 'a'), Piece(PieceType.ROOK, true))
                    board.set(Cell(y, 'b'), Piece(PieceType.KNIGHT, true))
                    board.set(Cell(y, 'c'), Piece(PieceType.BISHOP, true))
                    board.set(Cell(y, 'e'), Piece(PieceType.KING, true))
                    board.set(Cell(y, 'd'), Piece(PieceType.QUEEN, true))
                    board.set(Cell(y, 'f'), Piece(PieceType.BISHOP, true))
                    board.set(Cell(y, 'g'), Piece(PieceType.KNIGHT, true))
                    board.set(Cell(y, 'h'), Piece(PieceType.ROOK, true))
                }
            }
        }
    }
}

data class Cell(val row: Short, val col: Char)