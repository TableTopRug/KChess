import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.math.abs


interface PieceType {
    fun movement(cell: Cell, p: Piece): List<Pair<Char, Short>>
    fun validateMove(board: HashMap<Cell, Piece?>, from: Cell, to: Cell): Boolean
}

abstract class Piece(open var type: PieceType, open val color: COLOR) {
    var moves = 0;

    abstract fun value(): Int
    abstract fun image(): BufferedImage
};