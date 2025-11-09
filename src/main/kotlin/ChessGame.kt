data class ChessMove(
    override val from: Cell,
    override val to: Cell,
    override val piece: ChessPiece,
    val capturedPiece: Piece?
): Move(from, to, piece)

class Chess(players: List<Player>) : Game(players, listOf(COLOR.WHITE, COLOR.BLACK)) {
    override val board: ChessBoard = ChessBoard()
    private var currentTurn: COLOR = COLOR.WHITE
    val moveHistory: MutableList<ChessMove> = mutableListOf()

    init {
        require(players.size % 2 == 0) { "Chess requires multiples of 2 players." }
        require(players[0].color != players[1].color) { "Players must have different colors." }
    }

    override fun getGameState(): GameState {
        return GameState(
            board = board.getBoardState(),
            currentTurn = currentTurn,
            moveHistory = moveHistory.toList()
        )
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

        board.doPieceMove(from, to)
        moveHistory.add(ChessMove(from, to, piece, capturedPiece))

        if (capturedPiece != null) {
            player.piecesCaptured.add(capturedPiece.type)
        }

        // Switch turns
        currentTurn = if (currentTurn == COLOR.WHITE) COLOR.BLACK else COLOR.WHITE

        return true
    }

    fun getCurrentTurn(): COLOR = currentTurn
}