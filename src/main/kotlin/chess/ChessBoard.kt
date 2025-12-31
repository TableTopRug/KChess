package chess

import Board
import COLOR
import Cell
import Piece
import java.awt.Color
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.Image
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.BorderFactory
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JLayeredPane


class ChessBoard(size: Short = 8): Board(size) {
    var game: Chess? = null


    init {
        this.preferredSize = Dimension(512, 512)
        this.layout = GridLayout(size.toInt(), size.toInt())
        var black = true

        for (y in 1.toShort().rangeTo(size)) {
            black = !black
            for (x: Char in 'a' until 'a' + size.toInt()) {
                val cell = Cell(y.toShort(), x)

//                cell.size = Dimension(16, 16)
                cell.background = if (black) Color.BLACK else Color.WHITE
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//                cell.add(JLabel("$x, $y"));

                this.board[cell] = null;
                this.add(cell)
                black = !black
            }
        }

        val startCol = 'a'..'h'
        val startRow = shortArrayOf(1, 2, 7, 8)

        for (y in startRow) {
            when (y.toInt()) {
                1 -> {
                    board[Cell(y, 'a')] = ChessPiece(ChessPieceType.ROOK, COLOR.WHITE)
                    pieces[board[Cell(y, 'a')]!!] = Cell(y, 'a')
                    board[Cell(y, 'b')] = ChessPiece(ChessPieceType.KNIGHT, COLOR.WHITE)
                    pieces[board[Cell(y, 'b')]!!] = Cell(y, 'b')
                    board[Cell(y, 'c')] = ChessPiece(ChessPieceType.BISHOP, COLOR.WHITE)
                    pieces[board[Cell(y, 'c')]!!] = Cell(y, 'c')
                    board[Cell(y, 'd')] = ChessPiece(ChessPieceType.QUEEN, COLOR.WHITE)
                    pieces[board[Cell(y, 'd')]!!] = Cell(y, 'd')
                    board[Cell(y, 'e')] = ChessPiece(ChessPieceType.KING, COLOR.WHITE)
                    pieces[board[Cell(y, 'e')]!!] = Cell(y, 'e')
                    board[Cell(y, 'f')] = ChessPiece(ChessPieceType.BISHOP, COLOR.WHITE)
                    pieces[board[Cell(y, 'f')]!!] = Cell(y, 'f')
                    board[Cell(y, 'g')] = ChessPiece(ChessPieceType.KNIGHT, COLOR.WHITE)
                    pieces[board[Cell(y, 'g')]!!] = Cell(y, 'g')
                    board[Cell(y, 'h')] = ChessPiece(ChessPieceType.ROOK, COLOR.WHITE)
                    pieces[board[Cell(y, 'h')]!!] = Cell(y, 'h')
                }
                2 -> {
                    for (x: Char in startCol) {
                        board[Cell(y, x)] = ChessPiece(ChessPieceType.PAWN, COLOR.WHITE)
                        pieces[board[Cell(y, x)]!!] = Cell(y, x)
                    }
                }
                7 -> {
                    for (x: Char in startCol) {
                        board[Cell(y, x)] = ChessPiece(ChessPieceType.PAWN, COLOR.BLACK)
                        pieces[board[Cell(y, x)]!!] = Cell(y, x)
                    }
                }
                8 -> {
                    board[Cell(y, 'a')] = ChessPiece(ChessPieceType.ROOK, COLOR.BLACK)
                    pieces[board[Cell(y, 'a')]!!] = Cell(y, 'a')
                    board[Cell(y, 'b')] = ChessPiece(ChessPieceType.KNIGHT, COLOR.BLACK)
                    pieces[board[Cell(y, 'b')]!!] = Cell(y, 'b')
                    board[Cell(y, 'c')] = ChessPiece(ChessPieceType.BISHOP, COLOR.BLACK)
                    pieces[board[Cell(y, 'c')]!!] = Cell(y, 'c')
                    board[Cell(y, 'd')] = ChessPiece(ChessPieceType.QUEEN, COLOR.BLACK)
                    pieces[board[Cell(y, 'e')]!!] = Cell(y, 'd')
                    board[Cell(y, 'e')] = ChessPiece(ChessPieceType.KING, COLOR.BLACK)
                    pieces[board[Cell(y, 'd')]!!] = Cell(y, 'e')
                    board[Cell(y, 'f')] = ChessPiece(ChessPieceType.BISHOP, COLOR.BLACK)
                    pieces[board[Cell(y, 'f')]!!] = Cell(y, 'f')
                    board[Cell(y, 'g')] = ChessPiece(ChessPieceType.KNIGHT, COLOR.BLACK)
                    pieces[board[Cell(y, 'g')]!!] = Cell(y, 'g')
                    board[Cell(y, 'h')] = ChessPiece(ChessPieceType.ROOK, COLOR.BLACK)
                    pieces[board[Cell(y, 'h')]!!] = Cell(y, 'h')
                }
            }
        }

        board.forEach { (t, u) ->
            if (u != null) {
                addPieceOnClick(t, u)
            }
        }
    }

    override fun addPieceOnClick(cell: Cell, piece: Piece) {
        var iLab = JLabel(ImageIcon(piece.image().getScaledInstance(cell.preferredSize.width, cell.preferredSize.height, Image.SCALE_SMOOTH)))

        iLab.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                removeAllHighlights(highlightedCells)
//                println("Cell: ${cell.col}${cell.row}; Piece: ${piece.color} ${piece.type}")

                // Check if it's the correct player's turn
                game?.let { g ->
                    val currentPlayer = g.players.find { it.color == g.getCurrentTurn() }
                    if (currentPlayer is HumanChessPlayer && piece.color == currentPlayer.color) {
                        doGetMovementOptions(cell, piece, g)
                    } else {
                        println("Not your turn!")
                    }
                }
            }
        })
        iLab.setBounds(0, 0, cell.preferredSize.width, cell.preferredSize.height)
        cell.add(iLab, JLayeredPane.PALETTE_LAYER)
    }

    override fun doPieceMove(from: Cell, to: Cell): ChessMove {
        var move = super.doPieceMove(from, to)
        var chessMove = ChessMove(move)

        game?.let { g ->
            val movingPiece = board[to] as? ChessPiece
            pieces[movingPiece!!] = to
            if (movingPiece != null && g.isPawnAtEndOfBoard(to)) {
                val newPiece = g.promotePawn(to)
                chessMove.promotion = newPiece.pieceType
            }
        }

        return chessMove
    }
}