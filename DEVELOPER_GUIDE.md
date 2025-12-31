# KChess - Developer Quick Reference

## Project Structure

```
src/main/kotlin/
├── Board.kt              # Core board and cell classes
├── Game.kt              # Game state and base game class
├── Main.kt              # Application entry point
├── Piece.kt             # Piece type and piece base class
├── Player.kt            # Player and AI player classes
├── Screen.kt            # Screen management system
├── UI.kt                # Base UI manager
└── chess/               # Chess-specific implementation
    ├── ChessBoard.kt        # 8x8 chess board
    ├── ChessGame.kt         # Chess game logic
    ├── ChessPiece.kt        # Chess pieces and types
    ├── ChessPlayer.kt       # Chess player types
    ├── ChessGameUiManager.kt # Chess UI
    └── ChessScreenManager.kt # Chess screen management
```

## Key Classes and Their Responsibilities

### Game Engine (Core)

| Class | Responsibility |
|-------|-----------------|
| `Board` | Manages pieces, validates moves, handles highlighting |
| `Cell` | Represents a single board square with UI rendering |
| `Game` | Manages game state, turns, and move history |
| `Piece` | Base for all game pieces with movement rules |
| `Player` | Represents players (human or AI) |

### Chess Implementation

| Class | Responsibility |
|-------|-----------------|
| `Chess` | Chess-specific game logic (check, checkmate, promotion) |
| `ChessBoard` | 8x8 board with chess piece setup |
| `ChessPiece` | Chess pieces with evaluation |
| `ChessPieceType` | Movement rules for each piece type |
| `ChessPlayer` | Chess player base class |
| `HumanChessPlayer` | Human player (UI-controlled) |
| `AIChessPlayer` | Abstract AI player base |

### UI and Display

| Class | Responsibility |
|-------|-----------------|
| `ScreenManager` | Screen navigation and layout |
| `GameScreen` | Screen type enumeration |
| `GameUIManager` | Base UI manager for move display |
| `ChessGameUIManager` | Chess-specific UI (promotion dialog) |
| `ChessScreenManager` | Chess screen management (game over) |

## Common Operations

### Making a Move

```kotlin
// Through UI
board.doGetMovementOptions(fromCell, piece, game)
// When highlighted cell is clicked, move is made

// Programmatically
game.makeMove(fromCell, toCell, player)
```

### Checking Game State

```kotlin
val state = game.getGameState()
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

// Piece evaluation
val value = piece.value() // Returns material value
```

### Highlighting Moves

```kotlin
// Highlight a cell
cell.highlight {
    // Action when highlighted cell is clicked
}

// Remove highlight
cell.deHighlight()

// Remove all highlights for a piece's moves
board.removeAllHighlights(highlightedCells)
```

## Interfaces

### PieceType
Implemented by `ChessPieceType` enum. Defines:
- `movement()`: Calculate possible moves
- `validateMove()`: Validate a specific move

## Data Classes

| Class | Purpose |
|-------|---------|
| `GameState` | Snapshot of game at a point in time |
| `Move` | Base move representation |
| `ChessMove` | Chess move with promotion, capture info |
| `SimulatedChessGameState` | Hypothetical game state after a move |

## Enumerations

| Enum | Values |
|------|--------|
| `COLOR` | BLACK, WHITE |
| `GameScreen` | MAIN_MENU, GAME_SELECT, IN_GAME, GAME_OVER, SETTINGS |
| `ChessPieceType` | PAWN, BISHOP, KNIGHT, ROOK, KING, QUEEN |

## Extension Functions

### Cell Extensions
- `Cell.highlight(op: () -> Unit)`: Highlights cell and attaches click listener
- `Cell.deHighlight()`: Removes highlight from cell

## Important Notes

### Turn Management
- White moves first (typically)
- `getCurrentTurn()` returns whose turn it is
- Moves automatically switch turns when validated
- AI player check in main loop: `currentPlayer is AIPlayer`

### Move Validation
- Moves are validated before execution
- King-in-check moves are rejected automatically
- Piece-specific rules are enforced (pawn captures diagonal, etc.)
- Board obstacles block sliding pieces

### Pawn Promotion
- Occurs when pawn reaches row 8 (white) or row 1 (black)
- `isPawnAtEndOfBoard()` checks for promotion
- `promotePawn()` shows dialog and replaces piece
- Promotion is tracked: `wasPromotedFromPawn`

### Material Evaluation
- Pawn = 1 point
- Bishop/Knight = 3 points
- Rook = 5 points
- Queen = 9 points
- King = MAX_VALUE (irreplaceable)

## Extending the System

### Adding a New Piece Type
1. Add to `ChessPieceType` enum
2. Implement `movement()` and `validateMove()`
3. Add image file: `src/main/resources/pieces/[w|b]-[type].png`
4. Update `ChessPiece.image()` if needed

### Adding an AI Strategy
1. Extend `AIChessPlayer`
2. Implement abstract methods:
   - `takeTurn()`: Main AI decision
   - `evaluateBoard()`: Evaluate position
   - `evaluateMove()`: Score a move
   - `selectMoveWithPolicy()`: Choose best move
3. Register in main game loop

### Adding a New Screen
1. Create JPanel
2. Register with ScreenManager: `registerScreen(GameScreen.NEW_SCREEN, panel)`
3. Switch to: `screenManager.switchTo(GameScreen.NEW_SCREEN)`

## Debugging Tips

### Piece Movement Issues
- Check `ChessPieceType.validateMove()` for piece-specific rules
- Verify board bounds in `getPieceMovementOptions()`
- Check obstacle detection in sliding pieces

### Highlighting Issues
- Ensure `Cell.highlight()` is called with valid lambda
- Verify `Cell.deHighlight()` removes all layers
- Check `highlightedCells` list is properly maintained

### UI/Display Issues
- Check `Cell.preferredSize` is set (default 64x64)
- Verify image scaling: `Image.SCALE_SMOOTH`
- Ensure JLayeredPane layers are correct (PALETTE_LAYER for pieces, MODAL_LAYER for highlights)

### Turn/Move Issues
- Verify `makeMove()` returns true for successful moves
- Check player color matches current turn
- Ensure game is not `gameOver`

## Performance Considerations

- Move calculation happens on demand (not pre-calculated)
- Board state is mutable (changes during moves)
- Highlighted cells list can grow/shrink with interactions
- UI updates through Swing EDT (consider threading for AI)

---
**Last Updated**: December 30, 2025

