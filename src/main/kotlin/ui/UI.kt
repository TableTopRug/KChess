package ui

import game.Chess
import game.Game
import ui.chess.ChessUIManager
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import javax.swing.*

/**
 * Base class for game UI management.
 * Handles display of move history and game information.
 *
 * @property game The game instance being managed
 * @property movesPanel The panel for displaying move history
 * @property infoPanel The panel for displaying game information
 * @property movesListModel The model for the moves list
 * @property movesList The JList component displaying moves
 * @author TableTopRug
 * @version 1.0
 */
abstract class UIManager(open val frame: JFrame) {
    protected val leftPanel: JPanel = JPanel()
    protected val centerPanel: JPanel = JPanel()
    protected val rightPanel: JPanel = JPanel()

    protected var leftPanelVisible: Boolean = false
    protected var rightPanelVisible: Boolean = false

    /** Data model for the move history list */
    internal val movesListModel = DefaultListModel<String>()
    /** JList component that displays move history */
    internal val movesList = JList(movesListModel)
    internal val movesPanel: JPanel = rightPanel
    internal val infoPanel: JPanel = leftPanel
    internal val gamePanel: JPanel = centerPanel


    init {
        initializeLayout()
        configurePanels()
    }

    /**
     * Updates the displayed move history from the game.
     * Retrieves formatted moves and displays them in pairs (White, Black).
     * Auto-scrolls to the most recent move.
     */
    abstract fun updateMoves()

    /**
     * Initializes the main UI layout with three-panel border layout.
     * Sets up the frame and panel configuration for the application.
     */
    open fun initializeLayout() {
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.minimumSize = Dimension(800, 600)
        frame.setLocationRelativeTo(null);
        frame.layout = BorderLayout()

        // Set fixed sizes for side panels
        leftPanel.preferredSize = Dimension(200, 600)
        leftPanel.minimumSize = Dimension(50, 125)
        leftPanel.maximumSize = Dimension(400, 1200)

        rightPanel.preferredSize = Dimension(200, 600)
        rightPanel.minimumSize = Dimension(50, 125)
        rightPanel.maximumSize = Dimension(400, 1200)

        //debug colors
        leftPanel.background = Color.RED
        centerPanel.background = Color.GREEN
        rightPanel.background = Color.BLUE

        frame.add(leftPanel, BorderLayout.WEST)
        frame.add(centerPanel, BorderLayout.CENTER)
        frame.add(rightPanel, BorderLayout.EAST)
    }

    open fun configurePanels() {
        movesPanel.layout = BorderLayout()
        movesPanel.minimumSize = Dimension(128, 256)
        movesPanel.add(JLabel("Move History"), BorderLayout.NORTH)

        val scrollPane = JScrollPane(movesList)
        movesPanel.add(scrollPane, BorderLayout.CENTER)
    }

    /**
     * Displays both side panels (left and right) on the UI.
     * Makes side panels visible if they were previously hidden.
     */
    fun showSidePanels() {
        if (!leftPanelVisible) {
            frame.add(leftPanel, BorderLayout.WEST)
            leftPanelVisible = true
        }
        if (!rightPanelVisible) {
            frame.add(rightPanel, BorderLayout.EAST)
            rightPanelVisible = true
        }
        frame.revalidate()
        frame.repaint()
    }

    /**
     * Hides both side panels (left and right) from the UI.
     * Removes side panels from view to maximize center panel space.
     */
    fun hideSidePanels() {
        if (leftPanelVisible) {
            frame.remove(leftPanel)
            leftPanelVisible = false
        }
        if (rightPanelVisible) {
            frame.remove(rightPanel)
            rightPanelVisible = false
        }
        frame.revalidate()
        frame.repaint()
    }

    /**
     * Displays the left side panel (typically for game information).
     * Makes the left panel visible if it was previously hidden.
     */
    fun showLeftPanel() {
        if (!leftPanelVisible) {
            frame.add(leftPanel, BorderLayout.WEST)
            leftPanelVisible = true
            frame.revalidate()
            frame.repaint()
        }
    }

    /**
     * Sets the content of the left panel.
     * Replaces all existing content with the provided JPanel.
     *
     * @param content The JPanel to display in the left panel
     */
    fun setLeftPanelContent(content: JPanel) {
        leftPanel.removeAll()
        leftPanel.add(content, BorderLayout.CENTER)
        leftPanel.revalidate()
        leftPanel.repaint()
    }

    /**
     * Clears all content from the left panel.
     * Removes all child components from the left panel.
     */
    fun clearLeftPanel() {
        leftPanel.removeAll()
        leftPanel.revalidate()
        leftPanel.repaint()
    }

    /**
     * Hides the left side panel.
     * Removes the left panel from view if it is currently visible.
     */
    fun hideLeftPanel() {
        if (leftPanelVisible) {
            frame.remove(leftPanel)
            leftPanelVisible = false
            frame.revalidate()
            frame.repaint()
        }
    }

    /**
     * Displays the right side panel (typically for move history).
     * Makes the right panel visible if it was previously hidden.
     */
    fun showRightPanel() {
        if (!rightPanelVisible) {
            frame.add(rightPanel, BorderLayout.EAST)
            rightPanelVisible = true
            frame.revalidate()
            frame.repaint()
        }
    }

    /**
     * Sets the content of the right panel.
     * Replaces all existing content with the provided JPanel.
     *
     * @param content The JPanel to display in the right panel
     */
    fun setRightPanelContent(content: JPanel) {
        rightPanel.removeAll()
        rightPanel.add(content, BorderLayout.CENTER)
        rightPanel.revalidate()
        rightPanel.repaint()
    }

    /**
     * Clears all content from the right panel.
     * Removes all child components from the right panel.
     */
    fun clearRightPanel() {
        rightPanel.removeAll()
        rightPanel.revalidate()
        rightPanel.repaint()
    }

    /**
     * Hides the right side panel.
     * Removes the right panel from view if it is currently visible.
     */
    fun hideRightPanel() {
        if (rightPanelVisible) {
            frame.remove(rightPanel)
            rightPanelVisible = false
            frame.revalidate()
            frame.repaint()
        }
    }

    /**
     * Displays the moves panel (convenience method for right panel).
     * @see showRightPanel
     */
    fun showMovesPanel() {
        showRightPanel()
    }

    /**
     * Hides the moves panel (convenience method for right panel).
     * @see hideRightPanel
     */
    fun hideMovesPanel() {
        hideRightPanel()
    }

    /**
     * Displays the info panel (convenience method for left panel).
     * @see showLeftPanel
     */
    fun showInfoPanel() {
        showLeftPanel()
    }

    /**
     * Hides the info panel (convenience method for left panel).
     * @see hideLeftPanel
     */
    fun hideInfoPanel() {
        hideLeftPanel()
    }

    /**
     * Sets the content of the center panel.
     * Replaces all existing content with the provided JPanel.
     * The center panel typically displays the main game board.
     *
     * @param content The JPanel to display in the center panel
     */
    fun setCenterContent(content: JPanel) {
        centerPanel.removeAll()
        centerPanel.add(content, BorderLayout.CENTER)
        centerPanel.revalidate()
        centerPanel.repaint()
    }

    /**
     * Clears all content from the center panel.
     * Removes all child components from the center panel.
     */
    fun clearCenter() {
        centerPanel.removeAll()
        centerPanel.revalidate()
        centerPanel.repaint()
    }
}

open class GameUIManager private constructor(override val frame: JFrame): UIManager(frame) {
    /** The game instance associated with this UI manager */
    lateinit var game: Game
    /** The scene manager for screen navigation */
    lateinit var sceneManager: SceneManager


    companion object {
        fun create(frame: JFrame): GameUIManager {
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
            val gameUI = GameUIManager(frame)

            // Setup Game Select Screen
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


    /**
     * Updates the displayed move history from the game.
     * Retrieves formatted moves and displays them in pairs (White move, Black move).
     * Auto-scrolls to the most recent move in the list.
     */
    override fun updateMoves() {
        movesListModel.clear()
        val moves = game.getFormattedMoveHistory()

        // Display moves in pairs (White, Black)
        for (i in moves.indices step 2) {
            val whitMove = moves[i]
            val blackMove = if (i + 1 < moves.size) moves[i + 1] else ""
            movesListModel.addElement("$whitMove    $blackMove")
        }

        // Auto-scroll to bottom
        movesList.ensureIndexIsVisible(movesListModel.size() - 1)
    }

    /**
     * Creates the main menu screen panel.
     * Currently a placeholder implementation to be fully developed.
     *
     * @return Array of [left panel, center panel, right panel] for the main menu
     */
    fun createMainMenuScreen(): Array<JPanel?> {
        val panel = JPanel()
        panel.layout = BorderLayout()
        panel.add(JLabel("Main Menu - To be implemented"), BorderLayout.CENTER)

        return arrayOf(null, panel, null)
    }

    /**
     * Creates the game selection screen panel.
     * Allows players to choose which game type to play (Chess, Checkers, etc.).
     * Currently a placeholder implementation to be fully developed.
     *
     * @return Array of [left panel, center panel, right panel] for game selection
     */
    fun createGameSelectScreen(): Array<JPanel?> {
        val panel = JPanel()
        panel.layout = BorderLayout()
        panel.add(JLabel("Game Select - To be implemented"), BorderLayout.CENTER)

        return arrayOf(null, panel, null)
    }

    /**
     * Creates the player type selection screen panel.
     * Allows selection of human vs AI players before game start.
     * Currently a placeholder implementation to be fully developed.
     *
     * @return Array of [left panel, center panel, right panel] for player selection
     */
    fun createPlayerTypeSelectScreen(): Array<JPanel?> {
        val panel = JPanel()
        panel.layout = BorderLayout()
        panel.add(JLabel("Player Type Select - To be implemented"), BorderLayout.CENTER)

        val infoPanel = JPanel()
        infoPanel.layout = BorderLayout()
        infoPanel.add(JLabel("Select Player Types - To be implemented"), BorderLayout.CENTER)

        return arrayOf(null, panel, infoPanel)
    }

}