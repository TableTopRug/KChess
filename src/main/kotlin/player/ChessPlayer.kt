package player

import game.COLOR
import board.Cell
import game.Game
import game.Chess
import game.SimulatedChessGameState

/**
 * Base class for all chess players (human and AI).
 * Provides common chess-specific functionality like move simulation.
 *
 * @property color The color of the player (white or black)
 * @author TableTopRug
 * @version 1.0
 */
open class ChessPlayer(color: COLOR): Player(color) {
    /**
     * Simulates a move without affecting the actual board state.
     * Creates a hypothetical board state after the move.
     *
     * @param game The chess game instance
     * @param from The source cell
     * @param to The destination cell
     * @return A SimulatedChessGameState with the hypothetical board state, or null if game is not Chess
     */
    override fun simulateMove(game: Game, from: Cell, to: Cell): SimulatedChessGameState? {
        val chessGame = game as? Chess ?: return null

        val boardState = chessGame.board.getBoardState()
        val piece = boardState[from] ?: return null

        // Create a copy of the board state
        val simulatedBoard = HashMap(boardState)
        simulatedBoard[to] = piece
        simulatedBoard[from] = null

        return SimulatedChessGameState(
            board = simulatedBoard,
            wouldBeInCheck = chessGame.isKingInCheck(color), // Check with simulated state
            capturedPiece = boardState[to],
            isCheckingOpponent = false // Calculate if needed
        )
    }
}

/**
 * Represents a human chess player.
 * Moves are made through the UI by clicking on pieces and destination squares.
 *
 * @property color The color of the player
 * @author TableTopRug
 * @version 1.0
 */
class HumanChessPlayer(color: COLOR): ChessPlayer(color) {
}

/**
 * Abstract base class for AI chess players.
 * Provides abstract methods that AI implementations must override for strategy.
 *
 * @property color The color of the AI player
 * @author TableTopRug
 * @version 1.0
 */
abstract class AIChessPlayer(color: COLOR): ChessPlayer(color) {
    /**
     * Executes an AI player's turn by evaluating the board and selecting a move.
     *
     * @param game The chess game instance
     * @param move Optional move parameter (not used in this signature)
     */
    abstract fun takeTurn(game: Game, move: Chess?)

    /**
     * Evaluates the current board position and returns a score.
     * Positive scores favor the AI, negative scores favor the opponent.
     *
     * @param game The chess game to evaluate
     * @return An integer score representing board position evaluation
     */
    abstract fun evaluateBoard(game: Game): Int

    /**
     * Evaluates a hypothetical move and returns a score for that move.
     * Used for move selection and ranking.
     *
     * @param state The simulated game state after the move
     * @return An integer score for the move
     */
    abstract fun evaluateMove(state: SimulatedChessGameState): Int

    /**
     * Selects the best move from a list of candidate moves using a policy.
     * The policy could be greedy (best score), random, or other strategies.
     *
     * @param moves List of candidate moves with associated scores (from_cell, to_cell, score)
     * @return A pair of cells representing the selected move
     */
    abstract fun selectMoveWithPolicy(moves: List<Triple<Cell, Cell, Float>>): Pair<Cell, Cell>
}