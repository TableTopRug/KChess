package ui.chess

import game.COLOR
import piece.ChessPiece
import piece.ChessPieceType
import game.Chess
import ui.GameScene
import ui.GameUIManager
import java.awt.*
import java.awt.Image.SCALE_SMOOTH
import javax.swing.*

/**
 * Chess-specific UI manager for displaying game information.
 * Extends GameUIManager to add chess-specific features like captured pieces display.
 *
 * @property game The chess game instance
 * @property movesPanel The panel for displaying move history
 * @property capturedPanel The panel for displaying captured pieces
 * @property capturePanels Map of color to their captured pieces panel
 * @author TableTopRug
 * @version 1.0
 */
class ChessUIManager private constructor(override val frame: JFrame, override val game: Chess):
        GameUIManager(frame, game) {
    /** Map of player color to the panel displaying their captured pieces */
    val capturePanels = mutableMapOf<COLOR, JPanel>()


    companion object {
        fun create(frame: JFrame, game: Chess): ChessUIManager {
            // Setup all UI panels
            val leftPanel = JPanel().apply {
                preferredSize = Dimension(200, 600)
                background = Color.RED
            }

            val rightPanel = JPanel().apply {
                preferredSize = Dimension(200, 600)
                background = Color.BLUE
            }

            val centerPanel = JPanel().apply {
                background = Color.GREEN
            }

            // Add panels to frame
            frame.add(leftPanel, BorderLayout.WEST)
            frame.add(centerPanel, BorderLayout.CENTER)
            frame.add(rightPanel, BorderLayout.EAST)

            // Create managers
            val gameUI = ChessUIManager(frame, game)

            // Setup game
            game.board.game = game
            centerPanel.add(game.board)
            game.subscribeAsUIManager(gameUI)

            // Add listeners
            game.addMoveListener {
                SwingUtilities.invokeLater {
                    gameUI.updateMoves()
                }
            }

            return gameUI
        }
    }

    init {
        this.sceneManager = ChessSceneManager(this)

        var gameScreenList: Array<JPanel?> = arrayOf(null, null, null)

        var capturedPanel = JPanel()
        capturedPanel.layout = BoxLayout(capturedPanel, BoxLayout.Y_AXIS)
        capturedPanel.minimumSize = Dimension(128, 256)

        for (color in game.teams) {
            val colorContainer = JPanel()
            colorContainer.layout = BorderLayout()
            colorContainer.add(JLabel("${color.name} Captured Pieces: "), BorderLayout.NORTH)

            val pPanel = JPanel()
            pPanel.layout = FlowLayout(FlowLayout.LEFT, 2, 2)

            val capPanel = JScrollPane(pPanel)
            capPanel.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
            capPanel.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED

            colorContainer.add(capPanel, BorderLayout.CENTER)
            capturedPanel.add(colorContainer)

            capturePanels[color] = pPanel
        }

        var moveListPanel = JPanel()

        movesList.font = Font("Monospaced", Font.ROMAN_BASELINE, 14)

        val scrollPane = JScrollPane(movesList)
        moveListPanel.add(scrollPane, BorderLayout.CENTER)

        gameScreenList[0] = capturedPanel
        gameScreenList[1] = game.board
        gameScreenList[2] = moveListPanel

        this.sceneManager.registerScene(GameScene.IN_GAME, gameScreenList)
    }

    /**
     * Updates the displayed move history and captured pieces.
     * Refreshes both the move list and the captured pieces panels.
     */
    override fun updateMoves() {
        movesListModel.clear()
        val moves = game.getFormattedMoveHistory()
        val rawMoves = game.moveHistory

        // Display moves in pairs (White, Black)
        for (i in moves.indices step 2) {
            val whitMove = moves[i]
            val blackMove = if (i + 1 < moves.size) moves[i + 1] else ""
            val space = " ".repeat(15 - whitMove.length)
            movesListModel.addElement("$whitMove$space$blackMove")
        }

        capturePanels.forEach { it.component2().removeAll() }

        for (player in game.players) {
            val playerColor = player.color
            val capturedPieces = player.piecesCaptured
            for (piece in capturedPieces) {
                val pieceIcon = ImageIcon(piece.image().getScaledInstance(32, 32, SCALE_SMOOTH))
                val pieceLabel = JLabel(pieceIcon)
                capturePanels[playerColor]!!.add(pieceLabel)
            }
            capturePanels[playerColor]!!.revalidate()
            capturePanels[playerColor]!!.repaint()
        }

        // Auto-scroll to bottom
        movesList.ensureIndexIsVisible(movesListModel.size() - 1)
    }

    /**
     * Displays a dialog for the player to choose a piece for pawn promotion.
     * Shows Queen, Rook, Bishop, and Knight options with their images.
     *
     * @param color The color of the pawn being promoted
     * @return The selected ChessPieceType, or null if dialog was cancelled
     */
    fun doGetPromotionChoice(color: COLOR): ChessPieceType? {
        val options = arrayOf(
            ImageIcon(ChessPiece(ChessPieceType.QUEEN, color).image().getScaledInstance(64, 64, Image.SCALE_SMOOTH), "Queen"),
            ImageIcon(ChessPiece(ChessPieceType.ROOK, color).image().getScaledInstance(64, 64, Image.SCALE_SMOOTH), "Rook"),
            ImageIcon(ChessPiece(ChessPieceType.BISHOP, color).image().getScaledInstance(64, 64, Image.SCALE_SMOOTH), "Bishop"),
            ImageIcon(ChessPiece(ChessPieceType.KNIGHT, color).image().getScaledInstance(64, 64, Image.SCALE_SMOOTH), "Knight"),
        )
        val choice = JOptionPane.showOptionDialog(
            null,
            "Choose a piece for promotion:",
            "Pawn Promotion",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]
        )

        return when (choice) {
            0 -> ChessPieceType.QUEEN
            1 -> ChessPieceType.ROOK
            2 -> ChessPieceType.BISHOP
            3 -> ChessPieceType.KNIGHT
            else -> null
        }
    }
}