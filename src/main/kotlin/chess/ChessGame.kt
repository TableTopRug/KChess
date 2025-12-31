package chess

import COLOR
import Cell
import Game
import GameState
import Move
import Piece
import Player

/**
 * Chess-specific move representation.
 * Extends Move to include chess-specific properties like promotion and check status.
 *
 * @property from The source cell
 * @property to The destination cell
 * @property piece The chess piece that was moved
 * @property capturedPiece The piece captured by this move (if any)
 * @property promotion The piece type the pawn was promoted to (if applicable)
 * @property isPutInCheck Whether this move puts the opponent in check
 * @author Your Name
 * @version 1.0
 */
data class ChessMove(
    override val from: Cell,
    override val to: Cell,
    override val piece: ChessPiece,
    var capturedPiece: Piece? = null,
    var promotion: ChessPieceType? = null,
    var isPutInCheck: Boolean = false
): Move(from, to, piece) {
    constructor(move: Move) : this(move.from, move.to, move.piece as ChessPiece) {
    }
}

/**
 * Represents the state of a chess game after a hypothetical move.
 * Used for move validation and AI decision-making.
 *
 * @property board The board state after the simulated move
 * @property wouldBeInCheck Whether the moving player would be in check
 * @property capturedPiece The piece that would be captured (if any)
 * @property isCheckingOpponent Whether the move would put the opponent in check
 * @author Your Name
 * @version 1.0
 */
data class SimulatedChessGameState(
    val board: HashMap<Cell, Piece?>,
    val wouldBeInCheck: Boolean,
    val capturedPiece: Piece?,
    val isCheckingOpponent: Boolean
)

/**
 * Main Chess game implementation.
 * Manages chess-specific rules, turn management, check/checkmate detection, and pawn promotion.
 *
 * @property players List of chess players
 * @property board The chess board instance
 * @property moveHistory Complete history of all moves made
 * @property currentTurn The color of the player whose turn it is
 * @author Your Name
 * @version 1.0
 */
class Chess(players: List<Player>) : Game(players, listOf(COLOR.WHITE, COLOR.BLACK)) {
    override val board: ChessBoard = ChessBoard()

    private val moveListeners = mutableListOf<() -> Unit>()

    private var currentTurn: COLOR = COLOR.WHITE
    private var uiManager: ChessGameUIManager? = null

    val moveHistory: MutableList<ChessMove> = mutableListOf()


    init {
        require(players.size % 2 == 0) { "Chess requires multiples of 2 players." }
        require(players[0].color != players[1].color) { "Players must have different colors." }
    }


    /**
     * Gets the move history in standard chess notation.
     * Format: "1. e2e4 c7c5 2. g1f3..."
     *
     * @return List of formatted move strings
     */
    override fun getFormattedMoveHistory(): List<String> {
        return moveHistory.mapIndexed { index, move ->
            val moveNum = (index / 2) + 1
            val piece = when(move.piece.pieceType) {
                ChessPieceType.KING -> "K"
                ChessPieceType.QUEEN -> "Q"
                ChessPieceType.ROOK -> "R"
                ChessPieceType.BISHOP -> "B"
                ChessPieceType.KNIGHT -> "N"
                ChessPieceType.PAWN -> "P"
            }
            val capture = if (move.capturedPiece != null) "x" else "-"
            val promotion = if (move.promotion != null) "=${move.promotion.toString()[0]}" else ""
            val check = if (move.isPutInCheck) "+" else ""
            val moveStr = "$piece${move.from.col}${move.from.row}$capture${move.to.col}${move.to.row}$promotion$check"

            if (index % 2 == 0) {
                "$moveNum. $moveStr"
            } else {
                moveStr
            }
        }
    }

    /**
     * Gets a human-readable description of the last move made.
     *
     * @return String describing the last move or game start message
     */
    override fun getLastMoveDescription(): String {
        if (moveHistory.isEmpty()) return "Game started"

        val lastMove = moveHistory.last()
        val color = if (lastMove.piece.color == COLOR.WHITE) "White" else "Black"
        val piece = lastMove.piece.pieceType.toString().lowercase()
        val capture = if (lastMove.capturedPiece != null) " captures ${lastMove.capturedPiece!!.type}" else ""
        val promotion = if (lastMove.promotion != null) " and promotes to ${lastMove.promotion}" else ""

        return "$color $piece from ${lastMove.from.col}${lastMove.from.row} to ${lastMove.to.col}${lastMove.to.row}$capture$promotion"
    }

    /**
     * Gets the current complete game state.
     *
     * @return GameState object with board, current turn, and move history
     */
    override fun getGameState(): GameState {
        return GameState(
            board = board.getBoardState(),
            currentTurn = currentTurn,
            moveHistory = moveHistory.toList()
        )
    }

    /**
     * Gets all pieces owned by a player.
     *
     * @param player The player to get pieces for
     * @return List of pieces owned by the player
     */
    override fun getPiecesForPlayer(player: Player): List<Piece> {
        return board.pieces.keys.filter { p -> p.color == player.color }
    }

    /**
     * Validates if a move is legal for the given player.
     * Checks turn order, piece ownership, and game rules (no moving into check).
     *
     * @param from The source cell
     * @param to The destination cell
     * @param player The player attempting the move
     * @return True if the move is valid, false otherwise
     */
    override fun isValidMove(from: Cell, to: Cell, player: Player): Boolean {
        if (player.color != currentTurn) return false

        val piece = board.getBoardState()[from] ?: return false
        if (piece.color != player.color) return false

        val validMoves = board.getPieceMovementOptions(from, piece)
        return validMoves.contains(to)
    }

    /**
     * Executes a move if it is valid.
     * Handles piece movement, capture, check/checkmate detection, and turn switching.
     *
     * @param from The source cell
     * @param to The destination cell
     * @param player The player making the move
     * @return True if the move was executed, false if it was invalid
     */
    override fun makeMove(from: Cell, to: Cell, player: Player): Boolean {
        if (!isValidMove(from, to, player)) return false

        val piece = board.getBoardState()[from] as? ChessPiece ?: return false
        val capturedPiece = board.getBoardState()[to]

        // Simulate move to check if it leaves king in check
        board.getBoardState()[to] = piece
        board.getBoardState()[from] = null

        if (isKingInCheck(player.color)) {
            // Undo move
            board.getBoardState()[from] = piece
            board.getBoardState()[to] = capturedPiece
            return false
        }

        // Undo simulation and do real move
        board.getBoardState()[from] = piece
        board.getBoardState()[to] = capturedPiece

        var move = board.doPieceMove(from, to)
        val eColor = if (player.color == COLOR.WHITE) COLOR.BLACK else COLOR.WHITE
        val oKinginCheck = isKingInCheck(eColor)
        move.isPutInCheck = oKinginCheck

        if (capturedPiece != null) {
            player.piecesCaptured.add(capturedPiece)
            move.capturedPiece = capturedPiece
        }

        if (oKinginCheck) {
            if (isCheckmate(eColor)) {
                gameOver = true
                winner = player
            }
        }

        moveHistory.add(move)
        notifyMoveCompleted()

        // Switch turns
        currentTurn = if (currentTurn == COLOR.WHITE) COLOR.BLACK else COLOR.WHITE

        return true
    }

    /**
     * Gets the current player's turn.
     *
     * @return The color of the player whose turn it is
     */
    fun getCurrentTurn(): COLOR = currentTurn

    /**
     * Registers a listener to be called when a move is completed.
     *
     * @param listener The callback function to invoke
     */
    fun addMoveListener(listener: () -> Unit) {
        moveListeners.add(listener)
    }

    /**
     * Checks if the king of the specified color is currently in check.
     *
     * @param color The color of the king to check
     * @return True if the king is in check, false otherwise
     */
    fun isKingInCheck(color: COLOR): Boolean {
        val kingCell = board.getBoardState().entries.find {
            it.value is ChessPiece &&
                    (it.value as ChessPiece).pieceType == ChessPieceType.KING &&
                    it.value?.color == color
        }?.key ?: return false

        // Check if any opponent piece can attack the king
        for ((cell, piece) in board.getBoardState()) {
            if (piece != null && piece.color != color) {
                val moves = board.getPieceMovementOptions(cell, piece)
                if (moves.contains(kingCell)) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * Checks if a pawn has reached the end of the board and is eligible for promotion.
     *
     * @param cell The cell to check
     * @return True if a pawn on the cell can be promoted, false otherwise
     */
    fun isPawnAtEndOfBoard(cell: Cell): Boolean {
        val piece = board.getBoardState()[cell] as? ChessPiece ?: return false
        if (piece.pieceType != ChessPieceType.PAWN) return false

        return (piece.color == COLOR.WHITE && cell.row.toInt() == 8) ||
               (piece.color == COLOR.BLACK && cell.row.toInt() == 1)
    }

    /**
     * Promotes a pawn at the given cell to a new piece type.
     * Displays a dialog for the player to choose the promotion piece.
     *
     * @param cell The cell containing the pawn to promote
     * @return The new promoted piece
     */
    fun promotePawn(cell: Cell): ChessPiece {
        val piece = board.getBoardState()[cell] as? ChessPiece ?: throw IllegalArgumentException("No pawn at given cell")
        if (piece.pieceType != ChessPieceType.PAWN) throw IllegalArgumentException("Piece at given cell is not a pawn")

        val newPieceType = uiManager?.doGetPromotionChoice(piece.color)
        val newPiece = ChessPiece(piece, newPieceType ?: ChessPieceType.QUEEN)

        board.getBoardState()[cell] = newPiece

        val boardCell = board.board.keys.find { it.col == cell.col && it.row == cell.row }

        if (boardCell != null) {
            boardCell.removeAll()
            board.addPieceOnClick(boardCell, newPiece)
            boardCell.revalidate()
            boardCell.repaint()
        }

        return newPiece
    }

    /**
     * Gets all cells that a piece at the given position can capture.
     * Includes only occupied squares with opponent pieces.
     *
     * @param fromCell The cell the piece is on
     * @return List of cells containing capturable opponent pieces
     */
    fun getAllPotentialCaptures(fromCell: Cell): List<Cell> {
        val piece = board.getBoardState()[fromCell] ?: return emptyList()
        return board.getPieceMovementOptions(fromCell, piece).filter { targetCell ->
            val targetPiece = board.getBoardState()[targetCell]
            targetPiece != null && targetPiece.color != piece.color
        }
    }

    /**
     * Gets all legal moves for a piece at the given position.
     * Includes both captures and non-capture moves.
     *
     * @param fromCell The cell the piece is on
     * @return List of all legal destination cells
     */
    fun getAllLegalMoves(fromCell: Cell): List<Cell> {
        val piece = board.getBoardState()[fromCell] ?: return emptyList()
        return board.getPieceMovementOptions(fromCell, piece)
    }

    /**
     * Checks if the player with the given color is in checkmate.
     * A player is in checkmate if they are in check and have no legal moves to escape.
     *
     * @param color The color of the player to check
     * @return True if the player is in checkmate, false otherwise
     */
    fun isCheckmate(color: COLOR): Boolean {
        if (!isKingInCheck(color)) return false

        // Check if any move can get out of check
        for (piece in board.pieces.keys) {
            if (piece.color == color) {
                val cell = board.pieces[piece] ?: continue
                val moves = board.getPieceMovementOptions(cell, piece)

                for (move in moves) {
                    // Simulate move
                    val capturedPiece = board.getBoardState()[move]
                    board.getBoardState()[move] = piece
                    board.getBoardState()[cell] = null

                    val stillInCheck = isKingInCheck(color)

                    // Undo move
                    board.getBoardState()[cell] = piece
                    board.getBoardState()[move] = capturedPiece

                    if (!stillInCheck) return false
                }
            }
        }
        return true
    }

    /**
     * Registers the UI manager for this game.
     * Used for displaying pawn promotion dialogs.
     *
     * @param uiManager The ChessGameUIManager to use
     */
    fun subscribeAsUIManager(uiManager: ChessGameUIManager) {
        this.uiManager = uiManager;
    }

    private fun notifyMoveCompleted() {
        moveListeners.forEach { it() }
    }


}