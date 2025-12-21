package chess

import COLOR
import Cell
import Piece
import PieceType
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.math.abs


enum class ChessPieceType: PieceType {
    PAWN {
        override fun movement(cell: Cell, p: Piece): List<Pair<Char, Short>> {
            val mves = mutableListOf<Pair<Char, Short>>()
            val direction = if (p.color == COLOR.WHITE) 1 else -1

            if (p.moves == 0)
                mves.add(Pair(cell.col, (cell.row + direction * 2).toShort()))

            mves.add(Pair(cell.col, (cell.row + direction).toShort()))
            mves.add(Pair((cell.col + 1), (cell.row + direction).toShort()))
            mves.add(Pair((cell.col - 1), (cell.row + direction).toShort()))

            return mves;
        }

        override fun validateMove(board: HashMap<Cell, Piece?>, from: Cell, to: Cell): Boolean {
            if (to.row == (if (board[from]?.color!!.ordinal % 2 == 0) (from.row - 1).toShort() else (from.row + 1).toShort())) {
                if (from.col == to.col) {
                    if (board[to] == null) {
                        return true
                    } else {
                        return false
                    }
                } else if (from.col + 1 == to.col || from.col - 1 == to.col) {
                    val targetPiece = board[to]
                    if (targetPiece != null && targetPiece.color != board[from]?.color) {
                        return true
                    }
                }
            } else if (
                board[from]?.moves == 0
                && to.row == (if (board[from]?.color!!.ordinal % 2 == 0) (from.row - 2).toShort() else (from.row + 2).toShort())
                && from.col == to.col
            ) {
                val intermediateCell = Cell(
                    (from.row + if (board[from]?.color!!.ordinal % 2 == 0) -1 else 1).toShort(),
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
            if (board[to]?.color != board[from]?.color) {
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
            if (board[to]?.color != board[from]?.color) {
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

//    abstract override fun movement(cell: Cell, p: Piece): List<Pair<Char, Short>>

    override fun validateMove(board: HashMap<Cell, Piece?>, from: Cell, to: Cell): Boolean {
        val dir = Pair(
            if (to.col - from.col > 0) 1 else if (to.col - from.col < 0) -1 else 0,
            if (to.row - from.row > 0) 1 else if (to.row - from.row < 0) -1 else 0
        )

        // Calculate maximum steps needed
        val maxSteps = maxOf(
            abs(to.col - from.col),
            abs(to.row - from.row)
        )

        for (step in 1..maxSteps) {
            val nextCol = (from.col + (dir.first * step))
            val nextRow = (from.row + (dir.second * step)).toShort()
            val nextCell = Cell(nextRow, nextCol)

            if (nextCell == to) {
                return board[to]?.color != board[from]?.color
            }

            if (board[nextCell] != null) {
                return false
            }
        }

        return false
    }
}

data class ChessPiece(var pieceType: ChessPieceType, override val color: COLOR): Piece(pieceType, color) {
    var wasPromotedFromPawn = false

    constructor(piece: Piece) : this(piece.type as ChessPieceType, piece.color) {
        this.moves = piece.moves
    }

    constructor(piece: Piece, newType: ChessPieceType) : this(newType, piece.color) {
        this.moves = piece.moves
        this.wasPromotedFromPawn = if (piece.type == ChessPieceType.PAWN) true else false
    }

    override fun value(): Int {
        return when(pieceType) {
            ChessPieceType.PAWN -> 1;
            ChessPieceType.BISHOP, ChessPieceType.KNIGHT -> 3;
            ChessPieceType.ROOK -> 5;
            ChessPieceType.QUEEN -> 9;
            ChessPieceType.KING -> Int.MAX_VALUE;
        }
    }

    override fun image(): BufferedImage {
        var path: String = "/pieces/"


        path += when(color) {
            COLOR.BLACK -> "b-"
            COLOR.WHITE -> "w-"
        }

        path += when(pieceType) {
            ChessPieceType.PAWN -> "pawn.png";
            ChessPieceType.BISHOP -> "bishop.png";
            ChessPieceType.KNIGHT -> "knight.png";
            ChessPieceType.ROOK -> "rook.png";
            ChessPieceType.QUEEN -> "queen.png";
            ChessPieceType.KING -> "king.png";
        }

        return ImageIO.read(this.javaClass.getResourceAsStream(path));
    }

    fun movement(cell: Cell): List<Pair<Char, Short>> {
        return pieceType.movement(cell, this)
    }
}
