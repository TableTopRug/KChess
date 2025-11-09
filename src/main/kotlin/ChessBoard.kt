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
                    board[Cell(y, 'a')] = ChessPiece(PieceType.ROOK, COLOR.WHITE)
                    board[Cell(y, 'b')] = Piece(PieceType.KNIGHT, COLOR.WHITE)
                    board[Cell(y, 'c')] = Piece(PieceType.BISHOP, COLOR.WHITE)
                    board[Cell(y, 'd')] = Piece(PieceType.QUEEN, COLOR.WHITE)
                    board[Cell(y, 'e')] = Piece(PieceType.KING, COLOR.WHITE)
                    board[Cell(y, 'f')] = Piece(PieceType.BISHOP, COLOR.WHITE)
                    board[Cell(y, 'g')] = Piece(PieceType.KNIGHT, COLOR.WHITE)
                    board[Cell(y, 'h')] = Piece(PieceType.ROOK, COLOR.WHITE)
                }
                2 -> {
                    for (x: Char in startCol) {
                        board[Cell(y, x)] = Piece(PieceType.PAWN, COLOR.WHITE)
                    }
                }
                7 -> {
                    for (x: Char in startCol) {
                        board[Cell(y, x)] = Piece(PieceType.PAWN, COLOR.BLACK)
                    }
                }
                8 -> {
                    board[Cell(y, 'a')] = Piece(PieceType.ROOK, COLOR.BLACK)
                    board[Cell(y, 'b')] = Piece(PieceType.KNIGHT, COLOR.BLACK)
                    board[Cell(y, 'c')] = Piece(PieceType.BISHOP, COLOR.BLACK)
                    board[Cell(y, 'e')] = Piece(PieceType.KING, COLOR.BLACK)
                    board[Cell(y, 'd')] = Piece(PieceType.QUEEN, COLOR.BLACK)
                    board[Cell(y, 'f')] = Piece(PieceType.BISHOP, COLOR.BLACK)
                    board[Cell(y, 'g')] = Piece(PieceType.KNIGHT, COLOR.BLACK)
                    board[Cell(y, 'h')] = Piece(PieceType.ROOK, COLOR.BLACK)
                }
            }
        }

        board.forEach { t, u ->
            if (u != null) {
                addPieceOnClick(t, u)
            }
        }

//        this.addComponentListener(object : ComponentAdapter() {
//            override fun componentResized(e: ComponentEvent) {
//                for (comp: JComponent in this.)
//                val size: Dimension = this.getSize()
//                originalPanel.setBounds(0, 0, size.width, size.height)
//                coverButton.setBounds(0, 0, size.width, size.height)
//            }
//        });
    }

    override fun addPieceOnClick(cell: Cell, piece: Piece) {
        var iLab = JLabel(ImageIcon(piece.image().getScaledInstance(cell.preferredSize.width, cell.preferredSize.height, Image.SCALE_SMOOTH)));
//                iLab.text = "${if (u.isBlack) "Black" else "White"}_${u.type}"
        iLab.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                removeAllHighlights(highlightedCells)
                // This code executes when the JLabel is clicked
                println("Cell: ${cell.col}${cell.row}; Piece: $piece.color ${piece.type} :: ${cell.size}")
//                        JOptionPane.showMessageDialog(frame, "You clicked the label!")
                doGetMovementOptions(cell, piece)
            }
        })
        iLab.setBounds(0, 0, cell.preferredSize.width, cell.preferredSize.height);
        cell.add(iLab, JLayeredPane.PALETTE_LAYER)
    }
}