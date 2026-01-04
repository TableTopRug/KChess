# API Reference

Complete documentation of all public classes, methods, and interfaces in the KChess project.

**Note**: All classes include comprehensive Javadoc comments. Hover over code in your IDE to see full documentation.

## Core Classes

### Board

```kotlin
abstract class Board(val size: Short): JPanel()
```

**Description**: Abstract base class for game boards. Manages board state, piece placement, and movement validation.

**Properties**:
- `size: Short` - Board size (8 for chess)
- `pieces: MutableMap<Piece, Cell>` - Piece locations mapping
- `highlightedCells: MutableList<Cell>` - Currently highlighted cells
- `board: HashMap<Cell, Piece?>` - Internal board state

**Key Methods**:
- `getPieceMovementOptions(cell: Cell, piece: Piece): List<Cell>` - Get valid moves for a piece
- `doPieceMove(from: Cell, to: Cell): Move` - Execute a piece movement
- `removeAllHighlights(cells: List<Cell>)` - Clear all highlights
- `addPieceOnClick(cell: Cell, piece: Piece)` - Handle piece click (abstract)
- `getBoardState(): HashMap<Cell, Piece?>` - Get current board state

### Cell

```kotlin
data class Cell(val row: Short, val col: Char): JLayeredPane()
```

**Description**: Represents a single board square. Extends JLayeredPane to support layering of piece images and highlights.

**Properties**:
- `row: Short` - Row coordinate (1-8 for chess)
- `col: Char` - Column coordinate ('a'-'h' for chess)
- `layout: LayoutManager` - No layout (absolute positioning)
- `minimumSize: Dimension` - Minimum 16x16 pixels
- `preferredSize: Dimension` - Typically 64x64 pixels
- `isOpaque: Boolean` - Cell background is visible

**Extension Functions**:
- `highlight(op: () -> Unit)` - Add highlight button with click listener
- `deHighlight()` - Remove all highlights from this cell

### Game

```kotlin
abstract class Game(val players: List<Player>, val teams: List<COLOR>)
```

**Description**: Abstract base class for all game types. Manages game state, turns, and player management.

**Properties**:
- `players: List<Player>` - All players in the game
- `teams: List<COLOR>` - Team colors/sides
- `observers: List<Player>` - Observer players
- `board: Board` - Abstract, implemented by subclasses
- `gameOver: Boolean` - Game end flag
- `winner: Player?` - Winning player if game is over

**Abstract Methods**:
- `getFormattedMoveHistory(): List<String>` - Get moves in readable format
- `getLastMoveDescription(): String` - Describe last move
- `getGameState(): GameState` - Get complete game state
- `getPiecesForPlayer(player: Player): List<Piece>` - Get player's pieces
- `isValidMove(from: Cell, to: Cell, player: Player): Boolean` - Validate move
- `makeMove(from: Cell, to: Cell, player: Player): Boolean` - Execute move

### Piece

```kotlin
abstract class Piece(open var type: PieceType, open val color: COLOR)
```

**Description**: Abstract base class for all game pieces.

**Properties**:
- `type: PieceType` - Type defining movement rules
- `color: COLOR` - Piece color/owner
- `moves: Int` - Number of times piece has moved

**Abstract Methods**:
- `value(): Int` - Get piece's point value
- `image(): BufferedImage` - Get piece image for display

### Player

```kotlin
abstract class Player(val color: COLOR)
```

**Description**: Abstract base class for all players (human and AI).

**Properties**:
- `color: COLOR` - Player's color/team
- `piecesCaptured: MutableList<Piece>` - Captured opponent pieces

**Methods**:
- `simulateMove(game: Game, from: Cell, to: Cell): Any?` - Test move without executing
- `takeTurn(game: Game, move: Move? = null)` - Execute player's turn

## Chess Classes

### Chess

```kotlin
class Chess(players: List<Player>): Game(players, listOf(COLOR.WHITE, COLOR.BLACK))
```

**Description**: Chess game implementation with full rules and move validation.

**Properties**:
- `board: ChessBoard` - 8x8 chess board
- `moveHistory: MutableList<ChessMove>` - All moves made
- `players: List<Player>` - Two chess players

**Key Methods**:
- `isKingInCheck(color: COLOR): Boolean` - Check if king is under attack
- `isCheckmate(color: COLOR): Boolean` - Check for checkmate condition
- `isPawnAtEndOfBoard(cell: Cell): Boolean` - Check if pawn needs promotion
- `promotePawn(cell: Cell): ChessPiece` - Promote pawn to selected piece
- `getCurrentTurn(): COLOR` - Get current player's color
- `makeMove(from: Cell, to: Cell, player: Player): Boolean` - Execute chess move
- `addMoveListener(listener: () -> Unit)` - Subscribe to move events
- `subscribeAsUIManager(manager: UIManager)` - Register UI for updates

### ChessBoard

```kotlin
class ChessBoard(size: Short = 8): Board(size)
```

**Description**: Chess-specific 8x8 board with initial piece setup.

**Features**:
- Initializes all pieces in starting positions
- Handles piece click events for move selection
- Enforces turn-based gameplay
- Supports pawn promotion

**Key Methods**:
- `addPieceOnClick(cell: Cell, piece: Piece)` - Set up piece click handler
- `doPieceMove(from: Cell, to: Cell): ChessMove` - Execute move with special rules

### ChessPiece

```kotlin
data class ChessPiece(var pieceType: ChessPieceType, override val color: COLOR): Piece(pieceType, color)
```

**Description**: Represents a single chess piece with type and color.

**Properties**:
- `pieceType: ChessPieceType` - Type of piece
- `color: COLOR` - White or black
- `wasPromotedFromPawn: Boolean` - Promotion history flag

**Methods**:
- `value(): Int` - Material value (Pawn=1, Knight/Bishop=3, Rook=5, Queen=9, King=MAX)
- `image(): BufferedImage` - Get piece image

### ChessPieceType

```kotlin
enum class ChessPieceType: PieceType {
    PAWN, BISHOP, KNIGHT, ROOK, KING, QUEEN
}
```

**Description**: Enum of chess piece types, each implementing movement rules.

**Type-Specific Behavior**:

| Piece | Movement | Special Rules |
|-------|----------|----------------|
| PAWN | Forward 1 (2 first move) | Captures diagonally, promotes at end |
| BISHOP | Diagonal unlimited | Cannot jump pieces |
| KNIGHT | L-shape (2+1) | Can jump pieces |
| ROOK | Horizontal/vertical unlimited | Cannot jump pieces |
| KING | All directions 1 square | Cannot move into check |
| QUEEN | Bishop + Rook movement | Cannot jump pieces |

**Methods** (all types implement):
- `movement(cell: Cell, p: Piece): List<Pair<Char, Short>>` - Calculate possible moves
- `validateMove(board: HashMap<Cell, Piece?>, from: Cell, to: Cell): Boolean` - Validate specific move

### ChessPlayer

```kotlin
open class ChessPlayer(color: COLOR): Player(color)
class HumanChessPlayer(color: COLOR): ChessPlayer(color)
abstract class AIChessPlayer(color: COLOR): ChessPlayer(color)
```

**Description**: Chess player implementations for human and AI players.

**ChessPlayer Methods**:
- `simulateMove(game: Game, from: Cell, to: Cell): SimulatedChessGameState?` - Test move

**HumanChessPlayer**:
- Controlled through UI click events
- No special implementation needed

**AIChessPlayer** (abstract):
- `takeTurn(game: Game, move: Chess?)` - Execute AI turn (abstract)
- `evaluateBoard(game: Game): Int` - Evaluate board position (abstract)
- `evaluateMove(state: SimulatedChessGameState): Int` - Evaluate move (abstract)
- `selectMoveWithPolicy(moves: List<Triple<Cell, Cell, Float>>): Pair<Cell, Cell>` - Select move (abstract)

## UI Classes

### UIManager

```kotlin
abstract class UIManager(open val frame: JFrame)
```

**Description**: Base UI management class. Handles three-panel layout (left, center, right).

**Properties**:
- `frame: JFrame` - Main application window
- `leftPanel: JPanel` - Left side panel (game info)
- `centerPanel: JPanel` - Center panel (game board)
- `rightPanel: JPanel` - Right side panel (move history)

**Key Methods**:
- `updateMoves()` - Update move history display (abstract)
- `showLeftPanel()`, `hideLeftPanel()` - Show/hide left panel
- `showRightPanel()`, `hideRightPanel()` - Show/hide right panel
- `showSidePanels()`, `hideSidePanels()` - Show/hide both side panels
- `setCenterContent(content: JPanel)` - Set board panel
- `setLeftPanelContent(content: JPanel)` - Set left panel content
- `setRightPanelContent(content: JPanel)` - Set right panel content

### GameUIManager

```kotlin
open class GameUIManager(override val frame: JFrame): UIManager(frame)
```

**Description**: Game-specific UI manager.

**Constructors**:
- `GameUIManager(frame: JFrame)`
- `GameUIManager(frame: JFrame, game: Game)`
- `GameUIManager(frame: JFrame, game: Game, sceneManager: SceneManager)`

**Properties**:
- `game: Game` - Associated game instance
- `sceneManager: SceneManager` - Screen manager

**Methods**:
- `updateMoves()` - Display formatted moves
- `createMainMenuScreen(): Array<JPanel?>` - Create main menu
- `createGameSelectScreen(): Array<JPanel?>` - Create game selection
- `createPlayerTypeSelectScreen(): Array<JPanel?>` - Create player selection

### SceneManager

```kotlin
open class SceneManager(open val uIManager: UIManager)
```

**Description**: Manages screen/scene navigation in the application.

**Properties**:
- `uIManager: UIManager` - Associated UI manager
- `currentScreen: GameScene` - Currently displayed screen
- `screens: Map<GameScene, Array<JPanel?>>` - Registered screens

**Methods**:
- `switchTo(sceneType: GameScene)` - Switch to specified screen
- `registerScene(screenType: GameScene, panel: JPanel)` - Register single panel
- `registerScene(screenType: GameScene, panels: Array<JPanel?>)` - Register three panels
- `getCurrentScene(): GameScene` - Get current screen
- `prepareScene(screenType: GameScene, vararg args: Any?)` - Prepare screen before showing
- `reset()` - Return to main menu

### ChessUIManager

```kotlin
class ChessUIManager(override val frame: JFrame, override val game: Chess): GameUIManager(frame, game)
```

**Description**: Chess-specific UI manager with move history and captured pieces display.

**Properties**:
- `capturePanels: Map<COLOR, JPanel>` - Panels showing captured pieces

**Methods**:
- `updateMoves()` - Update move history and captured pieces
- `doGetPromotionChoice(color: COLOR): ChessPieceType?` - Show pawn promotion dialog
- `create(frame: JFrame, game: Chess): ChessUIManager` - Factory method

### ChessSceneManager

```kotlin
class ChessSceneManager(override val uIManager: ChessUIManager): SceneManager(uIManager)
```

**Description**: Chess-specific screen manager with game over screen.

**Methods**:
- `prepareScene(screenType: GameScene, vararg args: Any?)` - Prepare screen (handles game over)
- `switchTo(sceneType: GameScene)` - Switch screen with overlay support
- `createGameOverScreen(): JPanel` - Create game over overlay
- `prepareGameOverScreen(winner: COLOR)` - Prepare game over with winner
- `reset()` - Reset to initial state

## Enumerations

### COLOR

```kotlin
enum class COLOR {
    BLACK,
    WHITE
}
```

**Description**: Represents player colors/teams in the game.

### GameScene

```kotlin
enum class GameScene {
    MAIN_MENU,
    GAME_SELECT,
    IN_GAME,
    GAME_OVER,
    SETTINGS
}
```

**Description**: Represents all possible game screens.

### GameType

```kotlin
enum class GameType {
    CHESS,
    CHECKERS,
    OTHELLO,
    CUSTOM
}
```

**Description**: Supported game types (Checkers and Othello are placeholders for future implementation).

## Data Classes

### GameState

```kotlin
data class GameState(
    val board: HashMap<Cell, Piece?>,
    val currentTurn: COLOR,
    val moveHistory: List<Move>
)
```

**Description**: Snapshot of game state at a specific point in time.

### Move

```kotlin
open class Move(
    open val from: Cell,
    open val to: Cell,
    open val piece: Piece
)
```

**Description**: Represents a single move in the game.

### ChessMove

```kotlin
data class ChessMove(
    override val from: Cell,
    override val to: Cell,
    override val piece: ChessPiece,
    var capturedPiece: Piece? = null,
    var promotion: ChessPieceType? = null,
    var isPutInCheck: Boolean = false
): Move(from, to, piece)
```

**Description**: Chess-specific move with additional information.

### SimulatedChessGameState

```kotlin
data class SimulatedChessGameState(
    val board: HashMap<Cell, Piece?>,
    val wouldBeInCheck: Boolean,
    val capturedPiece: Piece?,
    val isCheckingOpponent: Boolean
)
```

**Description**: Result of simulating a hypothetical move.

## Interfaces

### PieceType

```kotlin
interface PieceType {
    fun movement(cell: Cell, p: Piece): List<Pair<Char, Short>>
    fun validateMove(board: HashMap<Cell, Piece?>, from: Cell, to: Cell): Boolean
}
```

**Description**: Defines movement rules for piece types.

**Methods**:
- `movement()` - Calculate all theoretically possible moves
- `validateMove()` - Validate a specific move considering obstacles

## IDE Integration

### Quick Documentation
- **Windows**: `Ctrl+Q`
- **Mac**: `Cmd+J`

### Goto Definition
- **Windows/Linux**: `Ctrl+Click` or `Ctrl+B`
- **Mac**: `Cmd+Click` or `Cmd+B`

### Auto-complete with Docs
- **Windows/Linux**: `Ctrl+Space`
- **Mac**: `Cmd+Space`

---

See [DEVELOPER_GUIDE.md](DEVELOPER_GUIDE.md) for usage examples and common tasks.

**Last Updated**: January 3, 2026  
**API Version**: 1.0

