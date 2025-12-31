import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.math.abs

/**
 * Interface defining the movement and validation behavior for different piece types.
 *
 * @author Your Name
 * @version 1.0
 */
interface PieceType {
    /**
     * Calculates all theoretically possible moves for a piece of this type.
     * May include moves that are blocked or invalid, which are filtered later.
     *
     * @param cell The cell the piece is currently on
     * @param p The piece to calculate moves for
     * @return List of possible coordinate pairs (column, row)
     */
    fun movement(cell: Cell, p: Piece): List<Pair<Char, Short>>

    /**
     * Validates if a specific move is legal according to piece type rules.
     * Checks obstacles, capture rules, and other piece-specific constraints.
     *
     * @param board The current board state
     * @param from The source cell
     * @param to The destination cell
     * @return True if the move is valid, false otherwise
     */
    fun validateMove(board: HashMap<Cell, Piece?>, from: Cell, to: Cell): Boolean
}

/**
 * Abstract base class for all game pieces.
 * Contains common properties and methods for pieces.
 *
 * @property type The type of piece determining its movement rules
 * @property color The color/owner of the piece
 * @property moves The number of times this piece has moved
 * @author Your Name
 * @version 1.0
 */
abstract class Piece(open var type: PieceType, open val color: COLOR) {
    var moves = 0;

    /**
     * Gets the point value of this piece for material balance calculations.
     *
     * @return The point value of the piece
     */
    abstract fun value(): Int

    /**
     * Gets the image representation of this piece.
     *
     * @return BufferedImage of the piece
     */
    abstract fun image(): BufferedImage
};