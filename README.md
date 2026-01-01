# MazeQuest – Algorithmic Maze Game

MazeQuest is an educational Java Swing game that teaches algorithmic thinking through play. Players write pseudocode to guide a character through randomly generated mazes, collect carrots, and reach the exit—while learning efficiency, control flow, and debugging concepts along the way.

The game blends classic maze generation algorithms with a simple scripting language, making it ideal for students learning programming fundamentals.


---

## Game Features

### Procedural Maze Generation

* Uses a **Depth-First Search (DFS)** recursive backtracking algorithm
* Generates a unique **perfect maze** on every run (no loops, one valid path)

### Code-to-Move Gameplay

* Control the character using a custom command language
* Example commands: `GO(3)`, `LEFT()`, `CATCH()`

### Progressive Level System

* Difficulty increases gradually
* Levels range from **Level 1 (4 × 5 grid)** to **Level 5 (8 × 12 grid)**

### Step-by-Step Debugging

* Run, Pause, and Step modes
* Visualize code execution line by line

### Syntax Highlighting & Error Detection

* Highlights the currently executing line
* Detects and flags syntax errors in user code

### Scoring System

* Small carrot: **+10 points**
* Level completion: **+100 points**
* Efficiency tracking compares executed moves vs. written commands

---

## Installation & Setup

### Prerequisites

* **Java Development Kit (JDK) 8 or higher**

### Project Structure

Make sure the directory structure looks like this so images and sounds load correctly:

```
MazeQuest/
├── src/
│   └── mazegame/
│       ├── MazeGame.java
│       ├── MazePanel.java
│       ├── ControlPanel.java
│       ├── CommandProcessor.java
│       ├── MazeGenerator.java
│       ├── SoundManager.java
│       ├── Cell.java
│       └── background.wav   // Optional background music
```

---

### Sound Note

The `SoundManager` looks for a file named `background.wav` inside the package directory.
If the file is missing, the game will still run—just without background music.

---

## Command Syntax Guide

Commands are **case-insensitive** and can be combined.

| Command       | Description                       | Example     |
| ------------- | --------------------------------- | ----------- |
| GO()          | Move forward 1 step               | GO()        |
| LEFT()        | Turn 90° left                     | LEFT()      |
| RIGHT()       | Turn 90° right                    | RIGHT()     |
| CATCH()       | Collect a carrot                  | CATCH()     |
| GO(n)         | Move forward n steps              | GO(5)       |
| LEFT(n)       | Turn left n times                 | LEFT(2)     |
| GO(n, ACTION) | Move n times, then perform action | GO(3, LEFT) |

---

## Example Algorithm

```
GO(3)           // Move forward 3 blocks
RIGHT()         // Turn right
GO(2, CATCH)    // Move 2 blocks and collect carrot
LEFT(2)         // Turn around (180 degrees)
GO(5, CATCH)    // Move to exit and finish level
```

This demonstrates sequencing, repetition, and action chaining—core algorithmic ideas.

---

## Class Overview

**MazeGame.java**
Main entry point. Handles window setup, game loop, and level transitions.

**MazeGenerator.java**
Implements DFS-based recursive backtracking to generate mazes.

**CommandProcessor.java**
Parses user commands using regular expressions and builds an execution queue.

**MazePanel.java**
Handles rendering of the maze grid, character, and animations using `Graphics2D`.

**ControlPanel.java**
Manages the code editor UI, line numbers, and execution controls.

---

## Gameplay Screenshots

The following images are captured directly from MazeQuest gameplay and demonstrate the maze layout, code editor, and step-by-step execution system.

![Gameplay Screenshot 1](https://github.com/user-attachments/assets/e645e1c0-555c-43c2-b44d-3e10d0a09386)
*Default theme — Level 1 (introductory maze size and mechanics)*

![Gameplay Screenshot 2](https://github.com/user-attachments/assets/72e671b6-30ab-4c13-85dc-16cc17b4f0f0)
*Default theme — Level 4 (larger maze with increased complexity)*

![Gameplay Screenshot 3](https://github.com/user-attachments/assets/fe8aadb0-d126-4653-8f6f-26bf6c723a91)
*Stranger Things theme — alternate visual style with the same algorithmic gameplay*
