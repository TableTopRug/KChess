# Developer Guide

## Overview

This guide provides common development tasks, architecture overview, and debugging tips for the KChess project.

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
val gameState = game.getGameState()
```

### Chess-Specific Operations

```kotlin
// Check detection
if (chess.isKingInCheck(COLOR.WHITE)) { 
    println("White king is in check!")
}

// Checkmate detection
if (chess.isCheckmate(COLOR.BLACK)) { 
    println("Black is checkmate!")
}

// Pawn promotion
if (chess.isPawnAtEndOfBoard(cell)) {
    val newPiece = chess.promotePawn(cell)
    println("Pawn promoted to ${newPiece.pieceType}")
}
```

### Highlighting Valid Moves

```kotlin
// Highlight a cell for valid move
cell.highlight {
    // Action when highlighted cell is clicked
    doGetMovementOptions(from, to)
}

// Remove highlight from a single cell
cell.deHighlight()

// Remove all highlights from multiple cells
board.removeAllHighlights(highlightedCells)
```

### Subscribing to Game Events

```kotlin
// Subscribe to move events
chess.addMoveListener {
    println("Move was made!")
    uiManager.updateMoves()
}

// Subscribe as UI manager for game updates
chess.subscribeAsUIManager(uiManager)
```

## Architecture Overview

### Core Game Engine

| Class | Purpose | Key Methods |
|-------|---------|-------------|
| `Board` | Abstract board management | `getPieceMovementOptions()`, `doPieceMove()`, `removeAllHighlights()` |
| `Cell` | Board square representation | `highlight()`, `deHighlight()` |
| `Game` | Abstract game base class | `makeMove()`, `isValidMove()`, `getGameState()` |
| `Piece` | Abstract piece base class | `value()`, `image()` |
| `Player` | Abstract player base class | `simulateMove()`, `takeTurn()` |

### Chess Implementation

| Class | Purpose | Key Methods |
|-------|---------|-------------|
| `Chess` | Chess game logic | `isKingInCheck()`, `isCheckmate()`, `promotePawn()` |
| `ChessBoard` | 8x8 board setup | Initializes pieces, manages clicks |
| `ChessPiece` | Chess piece definition | Stores type, color, move count |
| `ChessPieceType` | Piece movement rules | `movement()`, `validateMove()` |
| `ChessPlayer` | Chess player base | `simulateMove()` |
| `HumanChessPlayer` | Human player | UI-driven moves |
| `AIChessPlayer` | AI player base | Abstract methods for AI logic |

### UI Layer

| Class | Purpose | Key Methods |
|-------|---------|-------------|
| `UIManager` | Base UI management | Panel management, move display |
| `GameUIManager` | Game-specific UI | Screen creation, move updates |
| `SceneManager` | Screen navigation | `switchTo()`, `registerScene()` |
| `ChessUIManager` | Chess-specific UI | Pawn promotion dialog, captured pieces display |
| `ChessSceneManager` | Chess screen management | Game over screen handling |

## Key Concepts

### Board Coordinates
- **Columns**: 'a' to 'h' (left to right, ASCII characters)
- **Rows**: 1 to 8 (bottom to top, represented as Short)
- **Cell Reference**: Column + Row (e.g., "e4", "d7")
- **Cell Creation**: `Cell(row = 4.toShort(), col = 'e')`

### Turn Management
- WHITE always moves first
- Alternates after each successful move
- `getCurrentTurn()` returns whose turn it is (COLOR enum)
- Enforced by `makeMove()` validation

### Check/Checkmate
- **Check**: King is under direct attack from opponent piece
- **Checkmate**: King is in check with no legal moves available
- **Stalemate**: Not implemented yet (future enhancement)
- Detected after opponent completes move

### Move Validation
- Validates turn order first
- Checks piece ownership
- Verifies legal movement according to piece type
- Prevents moving into check (for the moving player)

### Piece Images
- Located in `src/main/resources/pieces/`
- Naming convention: `{color}-{type}.png`
  - Color: `w` (white) or `b` (black)
  - Type: `pawn`, `rook`, `knight`, `bishop`, `queen`, `king`
- Examples: `w-pawn.png`, `b-king.png`

## Debugging

### Show Board State
```kotlin
game.board.getBoardState().forEach { (cell, piece) ->
    if (piece != null) {
        println("${cell.col}${cell.row}: ${piece.type} (${piece.color})")
    }
}
```

### Check Highlighted Cells
```kotlin
println("Highlighted cells: ${game.board.highlightedCells.size}")
game.board.highlightedCells.forEach {
    println("  ${it.col}${it.row}")
}
```

### Check Piece Count
```kotlin
val whitePieces = game.getPiecesForPlayer(game.players.first())
val blackPieces = game.getPiecesForPlayer(game.players.last())
println("White pieces: ${whitePieces.size}, Black pieces: ${blackPieces.size}")
```

### Move History
```kotlin
game.moveHistory.forEach {
    println("${it.piece.color} ${it.piece.type}: ${it.from.col}${it.from.row} -> ${it.to.col}${it.to.row}")
}
```

### Game State Snapshot
```kotlin
val state = game.getGameState()
println("Current turn: ${state.currentTurn}")
println("Total moves: ${state.moveHistory.size}")
println("Board size: ${state.board.size}")
```

## Building & Running

### Build
```bash
./gradlew build
```

### Run
```bash
./gradlew run
```

### Clean Build
```bash
./gradlew clean build
```

### Generate JAR
```bash
./gradlew jar
```

### Run Tests (when available)
```bash
./gradlew test
```

## IDE Integration

### IntelliJ IDEA / Android Studio
- **Hover** over any class or method to see Javadoc
- **Ctrl+Q** (Windows) or **Cmd+J** (Mac) for full documentation popup
- **Ctrl+Space** for autocomplete with documentation
- **Alt+Insert** (Windows) or **Cmd+N** (Mac) for code generation
- **Shift+F1** for external documentation

### VS Code with Kotlin Plugin
- Hover for inline documentation
- **Go to Definition** (F12) to see comments
- **Peek Definition** (Alt+F12) for quick view

## Common Issues & Solutions

| Issue | Cause | Solution |
|-------|-------|----------|
| Move not working | Wrong player's turn | Check `getCurrentTurn()` before move |
| Highlight not showing | Cell size too small | Ensure `Cell.preferredSize` is > 16x16 |
| King moves into check | Logic error | Verify `isKingInCheck()` is called in `makeMove()` |
| Pawn not promoting | Condition not met | Check `isPawnAtEndOfBoard()` is called for all pieces |
| Piece image missing | Resource path wrong | Verify image exists in `resources/pieces/` |
| UI freezes | Game loop blocking | Ensure game updates happen on Swing EDT |

## Performance Tips

- Use `HashMap` for board state (already done)
- Cache movement options when possible
- Use `SwingUtilities.invokeLater()` for UI updates from game thread
- Avoid recursive validation when checking moves

## Future Enhancements

Planned features for future development:

1. **Additional Game Types**
   - Checkers implementation
   - Othello/Reversi implementation

2. **Advanced AI**
   - Minimax algorithm with alpha-beta pruning
   - Position evaluation functions
   - Opening books

3. **Game Features**
   - Stalemate detection
   - En passant capture
   - Castling move validation
   - 50-move draw rule
   - 3-fold repetition

4. **UI Improvements**
   - Animated piece movement
   - Sound effects
   - Theme customization
   - Network multiplayer

See [API_REFERENCE.md](API_REFERENCE.md) for complete class documentation.

---

**Last Updated**: January 3, 2026  
**Author**: TableTopRug with AI assistance

