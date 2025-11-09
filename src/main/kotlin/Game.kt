enum class COLOR {
    BLACK,
    WHITE
}

abstract class Game(players: List<Player>, teams: List<COLOR>) {
    val players: List<Player> = players
    val observers: List<Player> = mutableListOf()
    abstract var board: Board
}

class Chess(players: List<Player>) : Game(players, listOf(COLOR.WHITE, COLOR.BLACK)) {
    init {
        require(players.size % 2 == 0) { "Chess requires multiples of 2 players." }
        this.board = ChessBoard()
    }
}