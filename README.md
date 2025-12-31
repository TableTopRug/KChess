# KChess Project Documentation

## Overview

KChess is a Kotlin-based chess game with support for human and AI players. This folder contains documentation for developers.

## Quick Links

- **[DEVELOPER_GUIDE.md](docs/DEVELOPER_GUIDE.md)** - Common tasks and development reference
- **[API_REFERENCE.md](docs/API_REFERENCE.md)** - Class and method documentation

## Source Code Documentation

All source code is fully documented with Javadoc comments:

- Hover over any class or method in your IDE to see documentation
- Press `Ctrl+Q` (Windows) or `Cmd+J` (Mac) for quick documentation
- All documentation authored by: TableTopRug

## Project Structure

```
src/main/kotlin/
├── Core Game Engine
│   ├── Board.kt         - Board and Cell classes
│   ├── Game.kt          - Game state and logic
│   ├── Piece.kt         - Piece types
│   ├── Player.kt        - Player management
│   ├── Screen.kt        - Screen navigation
│   ├── UI.kt            - UI management
│   └── Main.kt          - Application entry
│
└── chess/               - Chess implementation
    ├── ChessBoard.kt    - 8x8 board
    ├── ChessGame.kt     - Chess rules
    ├── ChessPiece.kt    - Chess pieces
    ├── ChessPlayer.kt   - Chess players
    ├── ChessGameUiManager.kt   - Chess UI
    └── ChessScreenManager.kt   - Screen management
```

## Credits

See [../CREDITS.md](CREDITS.md) for project credits.

