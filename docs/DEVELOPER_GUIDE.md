# Developer Guide

## Common Tasks

### Making a Move

```kotlin
val from = board.board.keys.find { it.col == 'e' && it.row == 2.toShort() }!!
val to = board.board.keys.find { it.col == 'e' && it.row == 4.toShort() }!!
val player = game.players.find { it.color == COLOR.WHITE }!!

if (game.makeMove(from, to, player)) {
    println("Move successful!")
}
```

### Checking Game State

```kotlin
val currentTurn = game.getCurrentTurn()
val isValid = game.isValidMove(from, to, player)
val pieces = game.getPiecesForPlayer(player)
```

### Chess-Specific Operations

```kotlin
// Check detection
if (chess.isKingInCheck(COLOR.WHITE)) { }

// Checkmate detection
if (chess.isCheckmate(COLOR.BLACK)) { }

// Pawn promotion
if (chess.isPawnAtEndOfBoard(cell)) {
    val newPiece = chess.promotePawn(cell)
}
```

### Highlighting Moves

```kotlin
// Highlight a cell
cell.highlight {
    // Action when clicked
}

// Remove highlight
cell.deHighlight()

// Remove all highlights
board.removeAllHighlights(highlightedCells)
```

## Architecture

### Game Engine (Core)

| Class | Purpose |
|-------|---------|
| `Board` | Manages pieces and movement |
| `Cell` | Represents a board square |
| `Game` | Game state and turn management |
| `Piece` | Base piece class |
| `Player` | Human and AI players |

### Chess Implementation

| Class | Purpose |
|-------|---------|
| `Chess` | Chess game logic |
| `ChessBoard` | 8x8 board setup |
| `ChessPiece` | Chess piece definitions |
| `ChessPlayer` | Chess player types |
| `ChessGameUIManager` | Chess UI |
| `ChessScreenManager` | Screen management |

## Key Concepts

### Board Coordinates
- Columns: 'a' to 'h' (left to right)
- Rows: 1 to 8 (bottom to top)
- Cell: Column + Row (e.g., "e4", "d7")

### Turn Management
- WHITE moves first
- Alternates after each successful move
- `getCurrentTurn()` returns whose turn it is

### Check/Checkmate
- Check: King is under attack
- Checkmate: Checked with no legal moves
- Detected after opponent's move

## Debugging

```kotlin
// Show board state
game.board.getBoardState().forEach { (cell, piece) ->
    if (piece != null) {
        println("${cell.col}${cell.row}: ${piece.type} (${piece.color})")
    }
}

// Check highlighted cells
println("Highlighted cells: ${game.board.highlightedCells.size}")

// Check piece count
val whitePieces = game.getPiecesForPlayer(game.players.first())
println("White pieces: ${whitePieces.size}")
```

## Building & Running

```bash
# Build
./gradlew build

# Run
./gradlew run
# or
java -jar build/libs/KChess-1.0-SNAPSHOT.jar
```

## IDE Integration

**IntelliJ IDEA / Android Studio:**
- Hover over any class/method to see documentation
- Press `Ctrl+Q` for full documentation
- Type `Ctrl+Space` for autocomplete

**VS Code:**
- Hover for documentation popup
- Go to definition to see comments

## Common Issues

| Issue | Solution |
|-------|----------|
| Move not working | Check turn: `getCurrentTurn()` |
| Highlight not showing | Verify `Cell.preferredSize` is set |
| King moves into check | `isKingInCheck()` not called properly |
| Pawn not promoting | Check `isPawnAtEndOfBoard()` |

See [API_REFERENCE.md](API_REFERENCE.md) for complete class documentation.

