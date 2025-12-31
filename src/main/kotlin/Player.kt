/**
 * Abstract base class for all players in a game.
 * Provides common functionality for human and AI players.
 *
 * @property color The color/team of the player
 * @property piecesCaptured List of opponent pieces captured by this player
 * @author TableTopRug
 * @version 1.0
 */
abstract class Player(val color: COLOR) {
    var piecesCaptured = mutableListOf<Piece>()

    /**
     * Simulates a move without affecting the actual board state.
     * Used for AI decision-making and move validation.
     *
     * @param game The game instance
     * @param from The source cell
     * @param to The destination cell
     * @return The simulated game state result
     */
    abstract fun simulateMove(game: Game, from: Cell, to: Cell): Any?

    /**
     * Executes a player's turn. Base implementation for human players uses UI.
     *
     * @param game The game instance
     * @param move Optional move to make (used by some implementations)
     */
    open fun takeTurn(game: Game, move: Move? = null) {
        // Base implementation - human players use UI
    }
}

/**
 * Abstract base class for AI players.
 * Extends Player to provide additional AI-specific functionality.
 *
 * @property color The color/team of the AI player
 * @author TableTopRug
 * @version 1.0
 */
abstract class AIPlayer(color: COLOR): Player(color) {
    /**
     * Executes an AI player's turn by evaluating the board and selecting a move.
     *
     * @param game The game instance
     * @param move Optional move parameter (not used by AI)
     */
    override fun takeTurn(game: Game, move: Move?) {
        // AI logic here
        val gameState = game.getGameState()
        val pieceCells = getPieceCells(gameState.board)

        TODO()
    }

    /**
     * Filters the board state to find cells containing this player's pieces.
     *
     * @param boardState The current board state
     * @return List of cells containing pieces owned by this player
     */
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