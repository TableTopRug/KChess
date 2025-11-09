enum class COLOR {
    BLACK,
    WHITE
}

data class GameState(
    val board: HashMap<Cell, Piece?>,
    val currentTurn: COLOR,
    val moveHistory: List<Move>
)

open class Move(
    open val from: Cell,
    open val to: Cell,
    open val piece: Piece
)

abstract class Game(val players: List<Player>, teams: List<COLOR>) {
    val observers: List<Player> = mutableListOf()

    abstract val board: Board

    // Add methods for game state observation
    abstract fun getGameState(): GameState
    abstract fun isValidMove(from: Cell, to: Cell, player: Player): Boolean
    abstract fun makeMove(from: Cell, to: Cell, player: Player): Boolean
}