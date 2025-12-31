# KChess - Quick Start Guide for Developers

## Overview

KChess is a Kotlin-based chess game with support for human and AI players. The codebase is organized into a general game engine (Board, Game, Piece, Player) and a chess-specific implementation.

## Getting Started

### Prerequisites
- Kotlin 1.7+
- Java 11+
- Gradle 7.0+

### Building the Project
```bash
./gradlew build
```

### Running the Game
```bash
./gradlew run
# or
./gradlew build && java -jar build/libs/KChess-1.0-SNAPSHOT.jar
```

## Project Structure Quick Tour

### Core Game Engine (`src/main/kotlin/`)

| File | Purpose |
|------|---------|
| `Board.kt` | Board management, piece movement, highlighting |
| `Game.kt` | Game state, turn management, move history |
| `Piece.kt` | Piece definitions and types |
| `Player.kt` | Human and AI player base classes |
| `Screen.kt` | Screen navigation system |
| `UI.kt` | Base UI components |
| `Main.kt` | Application entry point |

### Chess Implementation (`src/main/kotlin/chess/`)

| File | Purpose |
|------|---------|
| `ChessBoard.kt` | 8x8 board initialization and chess rules |
| `ChessGame.kt` | Chess logic (check, checkmate, promotion) |
| `ChessPiece.kt` | Piece types with movement rules |
| `ChessPlayer.kt` | Chess player types |
| `ChessGameUiManager.kt` | Chess-specific UI (move display, promotion) |
| `ChessScreenManager.kt` | Chess screen management |

## Understanding the Flow

### Game Initialization
```
main()
  ↓
initUI() → Create JFrame with 3 panels
  ↓
Chess() → Create chess game
  ↓
ChessBoard() → Initialize 8x8 board with pieces
  ↓
HumanChessPlayer() → Create human players
  ↓
Game loop → Process moves
```

### Move Execution Flow
```
User clicks piece
  ↓
addPieceOnClick() → Validate turn
  ↓
doGetMovementOptions() → Calculate valid moves
  ↓
Cell.highlight() → Show valid destinations
  ↓
User clicks destination
  ↓
game.makeMove() → Execute move
  ↓
isValidMove() checks:
  - Is it player's turn?
  - Is move legal?
  - Would king be in check?
  ↓
Board updated & UI refreshed
  ↓
Turn switches
```

## Key Concepts

### Board Coordinates
- Columns: 'a' to 'h' (left to right)
- Rows: 1 to 8 (bottom to top from white's perspective)
- Cell identifier: Column + Row (e.g., "e4", "d7")

### Cell Highlighting
- **Yellow semi-transparent buttons** = Valid move destinations
- Clicking highlighted cell executes the move
- Highlights automatically cleared after move

### Turn Management
- WHITE moves first
- Alternates after each successful move
- `getCurrentTurn()` returns whose turn it is

### Check/Checkmate
- Checked: King is attacked and must escape
- Checkmate: Checked with no legal moves → game ends
- Only detected after opponent moves

## Common Tasks

### Add a New Piece Type

1. Add to `ChessPieceType` enum:
```kotlin
BISHOP {
    override fun movement(cell: Cell, p: Piece): List<Pair<Char, Short>> {
        // Calculate diagonal moves
    }
    override fun validateMove(...): Boolean {
        // Validate diagonal movement
    }
}
```

2. Add image: `src/main/resources/pieces/[w|b]-bishop.png`

3. Update piece initialization in `ChessBoard.init()`

### Implement an AI Player

1. Create AI class extending `AIChessPlayer`:
```kotlin
class MyAI(color: COLOR): AIChessPlayer(color) {
    override fun takeTurn(game: Game, move: Chess?) {
        // Find best move and execute
    }
    
    override fun evaluateBoard(game: Game): Int {
        // Score the position
    }
    
    override fun evaluateMove(state: SimulatedChessGameState): Int {
        // Score a move
    }
    
    override fun selectMoveWithPolicy(moves: List<Triple<Cell, Cell, Float>>): Pair<Cell, Cell> {
        // Choose best move
    }
}
```

2. Register in `main()`:
```kotlin
val game = Chess(
    listOf(
        HumanChessPlayer(COLOR.WHITE),
        MyAI(COLOR.BLACK)
    )
)
```

### Debug a Move Issue

1. Check if move is being recognized:
```kotlin
val isValid = game.isValidMove(from, to, player)
println("Valid: $isValid")
```

2. Check piece type rules:
```kotlin
val piece = board.getBoardState()[from]
val moves = board.getPieceMovementOptions(from, piece)
println("Piece: ${piece?.type}, Available moves: ${moves.size}")
```

3. Check king safety:
```kotlin
val inCheck = chess.isKingInCheck(player.color)
println("In check: $inCheck")
```

## Code Examples

### Making a Move Programmatically
```kotlin
val from = board.board.keys.find { it.col == 'e' && it.row == 2.toShort() }!!
val to = board.board.keys.find { it.col == 'e' && it.row == 4.toShort() }!!
val player = game.players.find { it.color == COLOR.WHITE }!!

if (game.makeMove(from, to, player)) {
    println("Move successful!")
} else {
    println("Invalid move")
}
```

### Checking Piece Values
```kotlin
game.getPiecesForPlayer(player).forEach { piece ->
    println("${piece.type}: ${piece.value()} points")
}
```

### Getting Move History
```kotlin
val moves = game.getFormattedMoveHistory()
moves.forEach { println(it) }
```

### Simulating a Move
```kotlin
val player = game.players.first() as ChessPlayer
val state = player.simulateMove(game as Chess, from, to)
state?.let {
    println("Captured: ${it.capturedPiece}")
    println("Would be in check: ${it.wouldBeInCheck}")
}
```

## Useful Debugging Prints

```kotlin
// Show current board state
game.board.getBoardState().forEach { (cell, piece) ->
    if (piece != null) {
        println("${cell.col}${cell.row}: ${piece.type} (${piece.color})")
    }
}

// Show current turn
println("Current turn: ${game.getCurrentTurn()}")

// Show move history
println("Moves: ${game.getFormattedMoveHistory().size}")

// Check highlighted cells
println("Highlighted cells: ${game.board.highlightedCells.size}")

// Check pieces for player
val whitePieces = game.getPiecesForPlayer(game.players.first())
println("White pieces remaining: ${whitePieces.size}")
```

## Performance Tips

1. **Move calculation is on-demand**: Moves aren't pre-calculated
2. **Use EDT for UI updates**: Wrap UI changes in `SwingUtilities.invokeLater {}`
3. **Cache board state for AI**: Create snapshots for analysis
4. **Limit search depth for AI**: Full board evaluation is expensive

## Testing

### Run Tests
```bash
./gradlew test
```

### Test a Move
```kotlin
@Test
fun testPawnMove() {
    val game = Chess(listOf(
        HumanChessPlayer(COLOR.WHITE),
        HumanChessPlayer(COLOR.BLACK)
    ))
    
    val from = game.board.board.keys.find { it.col == 'e' && it.row == 2.toShort() }!!
    val to = game.board.board.keys.find { it.col == 'e' && it.row == 4.toShort() }!!
    val player = game.players.first()
    
    assertTrue(game.makeMove(from, to, player))
}
```

## Common Issues and Solutions

| Issue | Solution |
|-------|----------|
| Move not working | Check if it's player's turn: `getCurrentTurn()` |
| Highlight not showing | Verify `Cell.preferredSize` is set |
| King can move into check | Check `isKingInCheck()` is being called |
| Wrong piece moving | Verify `addPieceOnClick()` is attached correctly |
| Pawn not promoting | Check `isPawnAtEndOfBoard()` returns true |

## Architecture Decisions

### Why Extension Functions for Cell?
- Kotlin idiom for adding behavior to classes
- Keeps Cell class clean and lightweight
- `highlight()` and `deHighlight()` are UI-specific

### Why Abstract Classes?
- Game, Board, Piece are abstract to allow different implementations
- Chess extends Game with chess-specific rules
- AIPlayer is abstract for different AI strategies

### Why Mutable Collections?
- Board state changes during moves
- Highlighted cells list changes with selection
- Easier to update UI in place

## Resources

- **DOCUMENTATION_SUMMARY.md** - Complete class and method listing
- **DEVELOPER_GUIDE.md** - Common operations and extending
- **DOCUMENTATION_INDEX.md** - Full documentation index
- **Source Code Comments** - Javadoc in all classes

## Getting Help

1. Check the source code Javadoc (hover in IDE)
2. Read the DEVELOPER_GUIDE.md
3. Review game flow diagrams in DOCUMENTATION_INDEX.md
4. Look at Main.kt for initialization example

---

**Ready to code?** Pick a file from the structure above and start exploring!

Suggested starting points:
1. Read `Main.kt` to understand initialization
2. Look at `ChessGame.kt` to understand game logic
3. Review `ChessBoard.kt` to see piece setup
4. Explore `ChessPiece.kt` for movement rules

Good luck! 🎉

