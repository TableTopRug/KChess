package chess

import COLOR
import Cell
import Game
import Piece
import Player


open class ChessPlayer(color: COLOR): Player(color) {
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

class HumanChessPlayer(color: COLOR): ChessPlayer(color) {
}

abstract class AIChessPlayer(color: COLOR): ChessPlayer(color) {
    abstract fun takeTurn(game: Game, move: Chess?)
    abstract fun evaluateBoard(game: Game): Int
    abstract fun evaluateMove(state: SimulatedChessGameState): Int
    abstract fun selectMoveWithPolicy(moves: List<Triple<Cell, Cell, Float>>): Pair<Cell, Cell>
}