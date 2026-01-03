package game

import piece.Piece
import player.Player
import board.Board
import board.Cell

/**
 * Enumeration representing player colors in the game.
 *
 * @property BLACK Represents black pieces/player
 * @property WHITE Represents white pieces/player
 * @author TableTopRug
 * @version 1.0
 */
enum class COLOR {
    BLACK,
    WHITE
}

enum class GameType {
    CHESS,
    CHECKERS,
    OTHELLO,
    CUSTOM
}

/**
 * Data class representing the complete state of a game at a specific point in time.
 *
 * @property board The current board state mapping cells to pieces
 * @property currentTurn The color of the player whose turn it is
 * @property moveHistory The list of all moves made so far
 * @author TableTopRug
 * @version 1.0
 */
data class GameState(
    val board: HashMap<Cell, Piece?>,
    val currentTurn: COLOR,
    val moveHistory: List<Move>
)

/**
 * Represents a single move in the game.
 *
 * @property from The source cell
 * @property to The destination cell
 * @property piece The piece that was moved
 * @author TableTopRug
 * @version 1.0
 */
open class Move(
    open val from: Cell,
    open val to: Cell,
    open val piece: Piece
)

/**
 * Abstract base class for all game types.
 * Manages game state, turns, and game flow.
 *
 * @property players List of players participating in the game
 * @property teams List of colors/teams in the game
 * @property observers List of players observing the game
 * @property board The game board (must be implemented by subclasses)
 * @property gameOver Flag indicating if the game has ended
 * @property winner The winning player (if game is over)
 * @author TableTopRug
 * @version 1.0
 */
abstract class Game(val players: List<Player>, val teams: List<COLOR>) {
    val observers: List<Player> = mutableListOf()

    abstract val board: Board

    var gameOver = false;
    var winner: Player? = null;

    /**
     * Gets the move history in a human-readable format.
     *
     * @return List of formatted move strings
     */
    abstract fun getFormattedMoveHistory(): List<String>

    /**
     * Gets a description of the last move made.
     *
     * @return String describing the last move
     */
    abstract fun getLastMoveDescription(): String

    /**
     * Gets the current game state.
     *
     * @return The current [GameState]
     */
    abstract fun getGameState(): GameState

    /**
     * Gets all pieces currently controlled by a player.
     *
     * @param player The player to get pieces for
     * @return List of pieces owned by the player
     */
    abstract fun getPiecesForPlayer(player: Player): List<Piece>

    /**
     * Validates if a move from one cell to another is legal for a player.
     *
     * @param from The source cell
     * @param to The destination cell
     * @param player The player attempting the move
     * @return True if the move is valid, false otherwise
     */
    abstract fun isValidMove(from: Cell, to: Cell, player: Player): Boolean

    /**
     * Executes a move from one cell to another.
     *
     * @param from The source cell
     * @param to The destination cell
     * @param player The player making the move
     * @return True if the move was successful, false otherwise
     */
    abstract fun makeMove(from: Cell, to: Cell, player: Player): Boolean
}