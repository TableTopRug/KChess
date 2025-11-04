import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

enum class PieceType {
    PAWN,
    BISHOP,
    KNIGHT,
    ROOK,
    KING,
    QUEEN
}

data class Piece(var type: PieceType, val isBlack: Boolean);

fun Piece.value(): Int {
    when(type) {
        PieceType.PAWN -> return 1;
        PieceType.BISHOP, PieceType.KNIGHT -> return 3;
        PieceType.ROOK -> return 5;
        PieceType.QUEEN -> return 9;
        PieceType.KING -> return Int.MAX_VALUE;
    }
}

fun Piece.image(): BufferedImage {
    when(type) {
        PieceType.PAWN -> if (isBlack)
                                return ImageIO.read(this.javaClass.getResourceAsStream("/pieces/b-pawn.png"))
                            else
                                return ImageIO.read(this.javaClass.getResourceAsStream("/pieces/w-pawn.png"))
        PieceType.BISHOP -> if (isBlack)
                                return ImageIO.read(this.javaClass.getResourceAsStream("/pieces/b-bishop.png"))
                            else
                                return ImageIO.read(this.javaClass.getResourceAsStream("/pieces/w-bishop.png"))
        PieceType.KNIGHT -> if (isBlack)
                                return ImageIO.read(this.javaClass.getResourceAsStream("/pieces/b-knight.png"))
                            else
                                return ImageIO.read(this.javaClass.getResourceAsStream("/pieces/w-knight.png"))
        PieceType.ROOK ->if (isBlack)
                                return ImageIO.read(this.javaClass.getResourceAsStream("/pieces/b-rook.png"))
                            else
                                return ImageIO.read(this.javaClass.getResourceAsStream("/pieces/w-rook.png"))
        PieceType.QUEEN -> if (isBlack)
                                return ImageIO.read(this.javaClass.getResourceAsStream("/pieces/b-queen.png"))
                            else
                                return ImageIO.read(this.javaClass.getResourceAsStream("/pieces/w-queen.png"));
        PieceType.KING -> if (isBlack)
                                return ImageIO.read(this.javaClass.getResourceAsStream("/pieces/b-king.png"))
                            else
                                return ImageIO.read(this.javaClass.getResourceAsStream("/pieces/w-king.png"))
    }
}