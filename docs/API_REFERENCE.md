# API Reference

All classes are documented with Javadoc. Hover over code in your IDE for full documentation.

## Core Classes

### Board
```kotlin
abstract class Board(val size: Short): JPanel()
```
- `pieces: MutableMap<Piece, Cell>` - Piece locations
- `highlightedCells: MutableList<Cell>` - Highlighted cells
- `getPieceMovementOptions(cell, piece)` - Get valid moves
- `doPieceMove(from, to)` - Execute move

### Cell
```kotlin
data class Cell(val row: Short, val col: Char): JLayeredPane()
```
- `highlight(op: () -> Unit)` - Highlight for valid move
- `deHighlight()` - Remove highlight

### Game
```kotlin
abstract class Game(val players: List<Player>, val teams: List<COLOR>)
```
- `makeMove(from, to, player)` - Execute move
- `isValidMove(from, to, player)` - Validate move
- `getGameState()` - Current state
- `getCurrentTurn()` - Whose turn
- `getPiecesForPlayer(player)` - Player's pieces

### Piece
```kotlin
abstract class Piece(val type: PieceType, val color: COLOR)
```
- `moves: Int` - Number of times moved
- `value()` - Material value
- `image()` - Piece image

### Player
```kotlin
abstract class Player(val color: COLOR)
```
- `simulateMove(game, from, to)` - Test move
- `takeTurn(game, move)` - Execute turn

## Chess Classes

### Chess
```kotlin
class Chess(players: List<Player>) : Game(...)
```
- `isKingInCheck(color)` - King in check?
- `isCheckmate(color)` - Checkmate?
- `isPawnAtEndOfBoard(cell)` - Pawn promotion?
- `promotePawn(cell)` - Promote pawn
- `moveHistory: List<ChessMove>` - Move history

### ChessBoard
```kotlin
class ChessBoard(size: Short = 8): Board(size)
```
- 8x8 chess board with piece setup

### ChessPiece
```kotlin
data class ChessPiece(val pieceType: ChessPieceType, val color: COLOR)
```
- `ChessPieceType` enum: PAWN, BISHOP, KNIGHT, ROOK, KING, QUEEN

### ChessPlayer
```kotlin
open class ChessPlayer(color: COLOR): Player(color)
class HumanChessPlayer(color: COLOR): ChessPlayer(color)
abstract class AIChessPlayer(color: COLOR): ChessPlayer(color)
```

## Enums

### COLOR
```kotlin
enum class COLOR { BLACK, WHITE }
```

### GameScreen
```kotlin
enum class GameScreen {
    MAIN_MENU, GAME_SELECT, IN_GAME, GAME_OVER, SETTINGS
}
```

### ChessPieceType
```kotlin
enum class ChessPieceType: PieceType {
    PAWN, BISHOP, KNIGHT, ROOK, KING, QUEEN
}
```

## Data Classes

### GameState
- `board: HashMap<Cell, Piece?>`
- `currentTurn: COLOR`
- `moveHistory: List<Move>`

### Move
- `from: Cell`
- `to: Cell`
- `piece: Piece`

### ChessMove (extends Move)
- `capturedPiece: Piece?`
- `promotion: ChessPieceType?`
- `isPutInCheck: Boolean`

## Interfaces

### PieceType
- `movement(cell, piece)` - Calculate moves
- `validateMove(board, from, to)` - Validate move

## IDE Integration

**Hover Documentation:**
All classes show full Javadoc on hover:
```
@author TableTopRug
@version 1.0
[Full documentation]
```

**Quick Docs:**
- Windows: `Ctrl+Q`
- Mac: `Cmd+J`

See [DEVELOPER_GUIDE.md](DEVELOPER_GUIDE.md) for examples and common tasks.

