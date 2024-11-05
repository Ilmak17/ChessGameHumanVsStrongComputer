# Console Chess Game

This is a simple chess game that allows a human player to play against a strong computer opponent in a console environment. In this advanced version, the computer uses a more sophisticated AI to challenge experienced players.

## How to Play

1. **Start the Game**: When you run the program, the chessboard will appear in the console. Each square on the board is represented by letters and numbers (e.g., `a1`, `b8`), following standard chess notation.

2. **Turn-based Play**: The game is set up for **human vs. computer** play. The human player and the computer take turns making moves. The computer uses the Minimax algorithm with alpha-beta pruning to evaluate moves, making it a challenging opponent.

3. **Options**:
    - **Make a Move**: The human player enters moves in chess notation (e.g., `e2 to e4`), and the computer calculates its response using an advanced evaluation function.
    - **Offer a Draw**: The human player can offer a draw if they believe the game is balanced or they want to end it.
    - **Give Up**: The human player can resign if they feel they cannot win the game.

4. **Display**: The board updates after each move, showing the current positions of all pieces and indicating whose turn it is.

## Features

- **Minimax with Alpha-Beta Pruning**: The computer opponent uses the Minimax algorithm with alpha-beta pruning to efficiently evaluate possible moves, allowing it to make stronger decisions.
- **Heuristic Evaluation Function**: The computer's moves are evaluated based on a custom heuristic function that considers:
    - **King Safety**: Calculates how well-protected the king is.
    - **Piece Value**: Considers the material value of pieces on the board.
    - **Center Control**: Rewards control over the center squares, which is essential for good positioning.
    - **Mobility Score**: Measures the number of legal moves available, encouraging the computer to maintain an active position.

- **Chess Notation**: The board and moves follow the standard chess notation for easy understanding.
- **Human vs. Advanced Computer Gameplay**: This version provides a challenging experience for more advanced players due to the enhanced AI.
- **Real-time Updates**: After each move, the board updates in the console to reflect the new positions of the pieces.

## Example Gameplay

After each move, the board is displayed with updated positions. A prompt will appear, allowing the human player to:
- Make their next move
- Offer a draw
- Resign from the game

![img.png](img.png)

The example screenshot shows the initial setup of the board at the beginning of the game, with White to move.

---

This README provides a comprehensive guide to the advanced version of your chess game with a strong computer AI. Let me know if you need any further customization or technical explanations!
