package chess

import COLOR
import Cell
import Game
import GameState
import Move
import Piece
import Player

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

data class SimulatedChessGameState(
    val board: HashMap<Cell, Piece?>,
    val wouldBeInCheck: Boolean,
    val capturedPiece: Piece?,
    val isCheckingOpponent: Boolean
)

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

    override fun getLastMoveDescription(): String {
        if (moveHistory.isEmpty()) return "Game started"

        val lastMove = moveHistory.last()
        val color = if (lastMove.piece.color == COLOR.WHITE) "White" else "Black"
        val piece = lastMove.piece.pieceType.toString().lowercase()
        val capture = if (lastMove.capturedPiece != null) " captures ${lastMove.capturedPiece!!.type}" else ""
        val promotion = if (lastMove.promotion != null) " and promotes to ${lastMove.promotion}" else ""

        return "$color $piece from ${lastMove.from.col}${lastMove.from.row} to ${lastMove.to.col}${lastMove.to.row}$capture$promotion"
    }

    override fun getGameState(): GameState {
        return GameState(
            board = board.getBoardState(),
            currentTurn = currentTurn,
            moveHistory = moveHistory.toList()
        )
    }

    override fun getPiecesForPlayer(player: Player): List<Piece> {
        return board.pieces.keys.filter { p -> p.color == player.color }
    }

    override fun isValidMove(from: Cell, to: Cell, player: Player): Boolean {
        if (player.color != currentTurn) return false

        val piece = board.getBoardState()[from] ?: return false
        if (piece.color != player.color) return false

        val validMoves = board.getPieceMovementOptions(from, piece)
        return validMoves.contains(to)
    }

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

    fun getCurrentTurn(): COLOR = currentTurn

    fun addMoveListener(listener: () -> Unit) {
        moveListeners.add(listener)
    }

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

    fun isPawnAtEndOfBoard(cell: Cell): Boolean {
        val piece = board.getBoardState()[cell] as? ChessPiece ?: return false
        if (piece.pieceType != ChessPieceType.PAWN) return false

        return (piece.color == COLOR.WHITE && cell.row.toInt() == 8) ||
               (piece.color == COLOR.BLACK && cell.row.toInt() == 1)
    }

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
     * Gets all cells that a piece at the given position can capture
     * (includes empty squares for non-pawn pieces, only occupied for pawns)
     */
    fun getAllPotentialCaptures(fromCell: Cell): List<Cell> {
        val piece = board.getBoardState()[fromCell] ?: return emptyList()
        return board.getPieceMovementOptions(fromCell, piece).filter { targetCell ->
            val targetPiece = board.getBoardState()[targetCell]
            targetPiece != null && targetPiece.color != piece.color
        }
    }

    /**
     * Gets all legal moves for a piece (including non-captures)
     */
    fun getAllLegalMoves(fromCell: Cell): List<Cell> {
        val piece = board.getBoardState()[fromCell] ?: return emptyList()
        return board.getPieceMovementOptions(fromCell, piece)
    }

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

    fun subscribeAsUIManager(uiManager: ChessGameUIManager) {
        this.uiManager = uiManager;
    }

    private fun notifyMoveCompleted() {
        moveListeners.forEach { it() }
    }


}