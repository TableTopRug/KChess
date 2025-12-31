package chess

import COLOR
import GameScreen
import ScreenManager
import java.awt.*
import javax.swing.*


class ChessScreenManager(val frame: JFrame): ScreenManager(frame) {
    val gameOverScreen: JPanel
    var isGameOverPrepared: Boolean = false

    private var gbc = GridBagConstraints()


    init {
        gameOverScreen = createGameOverScreen()
    }


    override fun prepareScreen(screenType: GameScreen, vararg args: Any?) {
        when(screenType) {
            GameScreen.GAME_OVER -> {
                if (args.isEmpty() || args[0] !is COLOR) {
                    throw IllegalArgumentException("prepareGameOverScreen requires a COLOR argument representing the winner.")
                }

                val winner = args[0] as COLOR
                prepareGameOverScreen(winner)
            }
            else -> super.prepareScreen(screenType, *args)
        }
    }

    override fun switchTo(screenType: GameScreen) {
        when (screenType) {
            GameScreen.GAME_OVER -> {
                if (!isGameOverPrepared) {
                    throw IllegalStateException("Game Over screen not prepared. Call prepareGameOverScreen(winner: COLOR) before switching to GAME_OVER.")
                }

                frame.contentPane.add(gameOverScreen, JLayeredPane.POPUP_LAYER)
            }
            else -> {
                frame.contentPane.remove(gameOverScreen)
                super.switchTo(screenType)
            }
        }
    }

    fun createGameOverScreen(): JPanel {
        val overlay = JPanel(GridBagLayout())
        overlay.background = Color(0, 0, 0, 180)  // Semi-transparent black
        overlay.bounds = frame.bounds

        gbc.gridx = 0
        gbc.insets = Insets(10, 10, 10, 10)

        gbc.gridy = 0
        val newGameBtn = JButton("Go to Main Menu")

        newGameBtn.addActionListener {
            this.switchTo(GameScreen.MAIN_MENU)
        }

        gbc.gridy = 1
        overlay.add(newGameBtn, gbc)

        return overlay
    }

    fun prepareGameOverScreen(winner: COLOR) {
        val label = JLabel("${winner.name} Wins!")
        label.font = Font("Arial", Font.BOLD, 32)
        label.foreground = Color.WHITE

        gbc.gridy = 0
        gameOverScreen.add(label, gbc)

        isGameOverPrepared = true
    }

    override fun reset() {
        isGameOverPrepared = false
        gameOverScreen.removeAll()
        super.reset()
    }
}