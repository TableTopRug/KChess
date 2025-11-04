package tabletoprg


import Board
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Container
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.*
import java.awt.event.ActionListener


val frame = JFrame("My App")

val lpanel = JPanel()
val mpanel = Board(8)
val rpanel = JPanel()



fun initUI() {
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.minimumSize = Dimension(400, 300)
    frame.setLocationRelativeTo(null);
    frame.layout = BorderLayout()

    lpanel.minimumSize = Dimension(256, 64)
    rpanel.minimumSize = Dimension(512, 32)

    lpanel.background = Color.RED
    mpanel.background = Color.GREEN
    rpanel.background = Color.BLUE

    lpanel.add(JLabel("Pieces: "))
    rpanel.add(JLabel("Moves: "))

    frame.add(lpanel, BorderLayout.WEST)
    frame.add(mpanel, BorderLayout.CENTER)
    frame.add(rpanel, BorderLayout.EAST)
}

fun main(args: Array<String>) {
    initUI()

    frame.pack()

    frame.isVisible = true
}