open class Player(val board: Board, val color: COLOR) {
    var piecesCaptured = mutableListOf<PieceType>()
}

class HumanPlayer(board: Board, color: COLOR): Player(board, color) {

}

class AIPlayer(board: Board, color: COLOR): Player(board, color) {
    fun getPieceCells(): MutableList<Cell> {
        val pieceCells = mutableListOf<Cell>()

        for ((cell, piece) in this.board.getBoardState()) {
            if (piece != null && piece.color == color) {
                pieceCells.add(cell)
            }
        }
        return pieceCells
    }
}