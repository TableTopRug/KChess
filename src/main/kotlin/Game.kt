import javax.swing.JPanel

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

abstract class Game(val players: List<Player>, val teams: List<COLOR>) {
    val observers: List<Player> = mutableListOf()

    abstract val board: Board

    var gameOver = false;
    var winner: Player? = null;


    // Return formatted move data instead of managing UI
    abstract fun getFormattedMoveHistory(): List<String>
    abstract fun getLastMoveDescription(): String

    // Add methods for game state observation
    abstract fun getGameState(): GameState
    abstract fun getPiecesForPlayer(player: Player): List<Piece>
    abstract fun isValidMove(from: Cell, to: Cell, player: Player): Boolean
    abstract fun makeMove(from: Cell, to: Cell, player: Player): Boolean
}