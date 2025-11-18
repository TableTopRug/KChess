abstract class Player(val color: COLOR) {
    var piecesCaptured = mutableListOf<Piece>()

    /**
     * Simulates a move without affecting the actual board state
     * Returns the resulting game state
     */
    abstract fun simulateMove(game: Game, from: Cell, to: Cell): Any?

    // Players observe and act through the game
    open fun takeTurn(game: Game, move: Move? = null) {
        // Base implementation - human players use UI
    }
}

abstract class AIPlayer(color: COLOR): Player(color) {
    override fun takeTurn(game: Game, move: Move?) {
        // AI logic here
        val gameState = game.getGameState()
        val pieceCells = getPieceCells(gameState.board)

        TODO()
    }

    private fun getPieceCells(boardState: HashMap<Cell, Piece?>): MutableList<Cell> {
        val pieceCells = mutableListOf<Cell>()
        for ((cell, piece) in boardState) {
            if (piece != null && piece.color == color) {
                pieceCells.add(cell)
            }
        }
        return pieceCells
    }
}