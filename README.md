# KChess - A Kotlin Chess Game

## Overview

KChess is a Kotlin-based chess game implementation with support for human players and AI opponents. Built with Swing for the UI and fully documented with Javadoc-style comments.

## Features

- **8x8 Chess Board** - Standard chess gameplay with all piece types
- **Move Validation** - Full implementation of chess rules including pawn promotion
- **Check/Checkmate Detection** - Proper game ending conditions
- **Move History** - Complete move tracking and display
- **Player Management** - Support for human and AI players
- **Modular Architecture** - Extensible design for additional game types

## Documentation

All source code is fully documented with Javadoc comments. Access documentation in three ways:

1. **In-IDE Documentation**: Hover over any class or method in your IDE
2. **Quick Docs**: Press `Ctrl+Q` (Windows) or `Cmd+J` (Mac)
3. **Documentation Files**:
   - **[docs/API_REFERENCE.md](docs/API_REFERENCE.md)** - Complete class and method documentation
   - **[docs/DEVELOPER_GUIDE.md](docs/DEVELOPER_GUIDE.md)** - Common tasks and development examples

## Project Structure

```
src/main/kotlin/
├── GameApplication.kt    - Main application lifecycle
├── Main.kt               - Entry point
│
├── board/                - Board and cell management
│   ├── Board.kt         - Abstract board base class
│   └── ChessBoard.kt    - Chess-specific 8x8 board
│
├── game/                 - Game logic and rules
│   ├── Game.kt          - Abstract game class
│   └── ChessGame.kt     - Chess game implementation
│
├── piece/                - Piece definitions
│   ├── Piece.kt         - Abstract piece base class
│   └── ChessPiece.kt    - Chess piece types and movement
│
├── player/               - Player management
│   ├── Player.kt        - Abstract player base class
│   └── ChessPlayer.kt   - Chess-specific players (Human/AI)
│
└── ui/                   - User interface
    ├── UI.kt            - Base UI manager
    ├── Scene.kt         - Screen navigation
    └── chess/           - Chess-specific UI
        ├── ChessUI.kt   - Chess UI manager
        └── ChessScreen.kt - Chess screen manager
```

## Building & Running

### Prerequisites
- Java 8 or higher
- Gradle

### Build
```bash
./gradlew build
```

### Run
```bash
./gradlew run
```

Or run the compiled jar:
```bash
java -jar build/libs/KChess-1.0-SNAPSHOT.jar
```

## Credits

See [CREDITS.md](CREDITS.md) for project credits and acknowledgments.

## Documentation History

All documentation created and maintained by TableTopRug with assistance from AI tools (Claude Haiku 4.5 for documentation writing, Claude Sonnet 4.5 for guidance and refinement).

---

**Last Updated**: January 3, 2026  
**Project Status**: Active Development  
**Version**: 1.0 Snapshot

