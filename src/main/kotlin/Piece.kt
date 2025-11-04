enum class PieceType {
    PAWN,
    BISHOP,
    KNIGHT,
    ROOK,
    KING,
    QUEEN
}

data class Piece(var type: PieceType, val isBlack: Boolean);

fun Piece.value(): Int {
    when(type) {
        PieceType.PAWN -> return 1;
        PieceType.BISHOP, PieceType.KNIGHT -> return 3;
        PieceType.ROOK -> return 5;
        PieceType.QUEEN -> return 9;
        PieceType.KING -> return Int.MAX_VALUE;
    }
}