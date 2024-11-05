package com.game.chess.minimax;

import com.game.chess.pieces.Position;

public class Move {
    private Position startPosition;
    private Position endPosition;
    private int score;

    public Move(Position startPosition, Position endPosition, int score) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.score = score;
    }

    public Move(int score) {
        this.score = score;
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Position startPosition) {
        this.startPosition = startPosition;
    }

    public Position getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(Position endPosition) {
        this.endPosition = endPosition;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
