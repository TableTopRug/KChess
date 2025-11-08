import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.abs

enum class PieceType {
    PAWN {
        override fun movement(cell: Cell, p: Piece): List<Pair<Char, Short>> {
            val mves = mutableListOf<Pair<Char, Short>>()

            if (p.moves == 0)
                mves.add(Pair(cell.col, (cell.row + if (p.isBlack) -2 else 2).toShort()))

            mves.add(Pair(cell.col, (cell.row + if (p.isBlack) -1 else 1).toShort()))
            mves.add(Pair((cell.col + 1), (cell.row + if (p.isBlack) -1 else 1).toShort()))
            mves.add(Pair((cell.col - 1), (cell.row + if (p.isBlack) -1 else 1).toShort()))

            return mves;
        }

        override fun validateMove(board: HashMap<Cell, Piece?>, from: Cell, to: Cell): Boolean {
            if (to.row == (if (board[from]?.isBlack == true) (from.row - 1).toShort() else (from.row + 1).toShort())) {
                if (from.col == to.col) {
                    if (board[to] == null) {
                        return true
                    } else {
                        return false
                    }
                } else if (from.col + 1 == to.col || from.col - 1 == to.col) {
                    val targetPiece = board[to]
                    if (targetPiece != null && targetPiece.isBlack != board[from]?.isBlack) {
                        return true
                    }
                }
            } else if (
                board[from]?.moves == 0
                && to.row == (if (board[from]?.isBlack == true) (from.row - 2).toShort() else (from.row + 2).toShort())
                && from.col == to.col
            ) {
                val intermediateCell = Cell(
                    (from.row + if (board[from]?.isBlack == true) -1 else 1).toShort(),
                    from.col
                )
                if (board[to] == null && board[intermediateCell] == null) {
                    return true
                }
            }

            return false
        }
    },
    BISHOP {
        override fun movement(cell: Cell, p: Piece): List<Pair<Char, Short>> {
            val mves = mutableListOf<Pair<Char, Short>>()
            var rowInc = 1
            var colInc = 1
            var curCol = cell.col
            var curRow = cell.row

            do {
                curCol += colInc
                curRow = (curRow + rowInc).toShort()
                mves.add(Pair(curCol, curRow))
            } while(curCol in 'a'..('a' + 128) && curRow in 1..128)

            rowInc = -1
            colInc = 1
            curCol = cell.col
            curRow = cell.row

            do {
                curCol += colInc
                curRow = (curRow + rowInc).toShort()
                mves.add(Pair(curCol, curRow))
            } while(curCol in 'a'..('a' + 128) && curRow in 1..128)

            rowInc = -1
            colInc = -1
            curCol = cell.col
            curRow = cell.row

            do {
                curCol += colInc
                curRow = (curRow + rowInc).toShort()
                mves.add(Pair(curCol, curRow))
            } while(curCol in 'a'..('a' + 128) && curRow in 1..128)

            rowInc = 1
            colInc = -1
            curCol = cell.col
            curRow = cell.row

            do {
                curCol += colInc
                curRow = (curRow + rowInc).toShort()
                mves.add(Pair(curCol, curRow))
            } while(curCol in 'a'..('a' + 128) && curRow in 1..128)


            return mves;
        }
    },
    KNIGHT {
        override fun movement(cell: Cell, p: Piece): List<Pair<Char, Short>> {
            val mves = mutableListOf<Pair<Char, Short>>()

            mves.add(Pair((cell.col + 1), (cell.row + 2).toShort()))
            mves.add(Pair((cell.col + 1), (cell.row - 2).toShort()))
            mves.add(Pair((cell.col - 1), (cell.row + 2).toShort()))
            mves.add(Pair((cell.col - 1), (cell.row - 2).toShort()))
            mves.add(Pair((cell.col + 2), (cell.row + 1).toShort()))
            mves.add(Pair((cell.col + 2), (cell.row - 1).toShort()))
            mves.add(Pair((cell.col - 2), (cell.row + 1).toShort()))
            mves.add(Pair((cell.col - 2), (cell.row - 1).toShort()))

            return mves;
        }

        override fun validateMove(board: HashMap<Cell, Piece?>, from: Cell, to: Cell): Boolean {
            if (board[to]?.isBlack != board[from]?.isBlack) {
                if (
                    abs(from.col - to.col) == 2 && abs(from.row - to.row) == 1
                    || abs(from.col - to.col) == 1 && abs(from.row - to.row) == 2
                ){
                    return true
                }
            }

            return false
        }
    } ,
    ROOK {
        override fun movement(cell: Cell, p: Piece): List<Pair<Char, Short>> {
            val mves = mutableListOf<Pair<Char, Short>>()
            var curCol = cell.col
            var curRow = cell.row

            do {
                mves.add(Pair(curCol, ++curRow))
            } while(curRow in 1..128)

            curRow = cell.row

            do {
                mves.add(Pair(curCol, --curRow))
            } while(curRow in 1..128)

            curRow = cell.row
            curCol = cell.col

            do {
                mves.add(Pair(++curCol, curRow))
            } while(curCol in 'a'..('a' + 128))

            curCol = cell.col

            do {
                mves.add(Pair(--curCol, curRow))
            } while(curCol in 'a'..('a' + 128))

            return mves;
        }
    },
    KING {
        override fun movement(cell: Cell, p: Piece): List<Pair<Char, Short>> {
            val mves = mutableListOf<Pair<Char, Short>>()

            mves.add(Pair(cell.col, (cell.row + 1).toShort()))
            mves.add(Pair(cell.col, (cell.row - 1).toShort()))
            mves.add(Pair((cell.col + 1), cell.row))
            mves.add(Pair((cell.col - 1), cell.row))
            mves.add(Pair((cell.col + 1), (cell.row + 1).toShort()))
            mves.add(Pair((cell.col + 1), (cell.row - 1).toShort()))
            mves.add(Pair((cell.col - 1), (cell.row + 1).toShort()))
            mves.add(Pair((cell.col - 1), (cell.row - 1).toShort()))

            return mves;
        }

        override fun validateMove(board: HashMap<Cell, Piece?>, from: Cell, to: Cell): Boolean {
            if (board[to]?.isBlack != board[from]?.isBlack) {
                if (
                    abs(from.col - to.col) <= 1 && abs(from.row - to.row) <= 1
                ){
                    return true
                }
            }

            return false
        }
    },
    QUEEN {
        override fun movement(cell: Cell, p: Piece): List<Pair<Char, Short>> {
            val mves = mutableListOf<Pair<Char, Short>>()

            mves.addAll(BISHOP.movement(cell, p))
            mves.addAll(ROOK.movement(cell, p))

            return mves;
        }
    };

    abstract fun movement(cell: Cell, p: Piece): List<Pair<Char, Short>>

    open fun validateMove(board: HashMap<Cell, Piece?>, from: Cell, to: Cell): Boolean {
        val dir = Pair(
            if (to.col - from.col > 1) 1 else if (to.col - from.col < 0) -1 else 0,
            if (to.row - from.row > 1) 1 else if (to.row - from.row < 0) -1 else 0
        )

        // Calculate maximum steps needed
        val maxSteps = maxOf(
            kotlin.math.abs(to.col - from.col),
            kotlin.math.abs(to.row - from.row)
        )

        for (step in 1..maxSteps) {
            val nextCol = (from.col + (dir.first * step))
            val nextRow = (from.row + (dir.second * step)).toShort()
            val nextCell = Cell(nextRow, nextCol)

            if (nextCell == to) {
                return board[to]?.isBlack != board[from]?.isBlack
            }

            if (board[nextCell] != null) {
                return false
            }
        }

        return false
    }
}

data class Piece(var type: PieceType, val isBlack: Boolean) {
    var moves = 0;
};

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