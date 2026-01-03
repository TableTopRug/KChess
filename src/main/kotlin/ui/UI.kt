package ui

import game.Game
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

    fun showLeftPanel() {
        if (!leftPanelVisible) {
            frame.add(leftPanel, BorderLayout.WEST)
            leftPanelVisible = true
            frame.revalidate()
            frame.repaint()
        }
    }

    fun setLeftPanelContent(content: JPanel) {
        leftPanel.removeAll()
        leftPanel.add(content, BorderLayout.CENTER)
        leftPanel.revalidate()
        leftPanel.repaint()
    }

    fun clearLeftPanel() {
        leftPanel.removeAll()
        leftPanel.revalidate()
        leftPanel.repaint()
    }

    fun hideLeftPanel() {
        if (leftPanelVisible) {
            frame.remove(leftPanel)
            leftPanelVisible = false
            frame.revalidate()
            frame.repaint()
        }
    }

    fun showRightPanel() {
        if (!rightPanelVisible) {
            frame.add(rightPanel, BorderLayout.EAST)
            rightPanelVisible = true
            frame.revalidate()
            frame.repaint()
        }
    }

    fun setRightPanelContent(content: JPanel) {
        rightPanel.removeAll()
        rightPanel.add(content, BorderLayout.CENTER)
        rightPanel.revalidate()
        rightPanel.repaint()
    }

    fun clearRightPanel() {
        rightPanel.removeAll()
        rightPanel.revalidate()
        rightPanel.repaint()
    }

    fun hideRightPanel() {
        if (rightPanelVisible) {
            frame.remove(rightPanel)
            rightPanelVisible = false
            frame.revalidate()
            frame.repaint()
        }
    }

    fun showMovesPanel() {
        showRightPanel()
    }

    fun hideMovesPanel() {
        hideRightPanel()
    }

    fun showInfoPanel() {
        showLeftPanel()
    }

    fun hideInfoPanel() {
        hideLeftPanel()
    }

    fun setCenterContent(content: JPanel) {
        centerPanel.removeAll()
        centerPanel.add(content, BorderLayout.CENTER)
        centerPanel.revalidate()
        centerPanel.repaint()
    }

    fun clearCenter() {
        centerPanel.removeAll()
        centerPanel.revalidate()
        centerPanel.repaint()
    }
}

open class GameUIManager(override val frame: JFrame): UIManager(frame) {
    lateinit var game: Game
    lateinit var sceneManager: SceneManager


    constructor(frame: JFrame, game: Game) : this(frame) {
        this.game = game
    }

    constructor(frame: JFrame, game: Game, sceneManager: SceneManager) : this(frame) {
        this.game = game
        this.sceneManager = sceneManager
    }


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

    fun createMainMenuScreen(): Array<JPanel?> {
        val panel = JPanel()
        panel.layout = BorderLayout()
        panel.add(JLabel("Main Menu - To be implemented"), BorderLayout.CENTER)

        return arrayOf(null, panel, null)
    }

    fun createGameSelectScreen(): Array<JPanel?> {
        val panel = JPanel()
        panel.layout = BorderLayout()
        panel.add(JLabel("Game Select - To be implemented"), BorderLayout.CENTER)

        return arrayOf(null, panel, null)
    }

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