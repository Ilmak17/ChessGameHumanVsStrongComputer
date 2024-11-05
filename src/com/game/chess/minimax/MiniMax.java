package com.game.chess.minimax;

import com.game.chess.board.Board;
import com.game.chess.pieces.enums.Color;

public interface MiniMax {
    Move getBestMove(Board board, Color color);
}
