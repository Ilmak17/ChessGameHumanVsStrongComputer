package com.game.chess.pieces;

import com.game.chess.board.Board;
import com.game.chess.pieces.enums.Color;
import com.game.chess.pieces.enums.PieceType;

public class Bishop extends SlidingPiece {

    public Bishop(Board board, Color color, Position position) {
        super(board, color, position);
    }

    @Override
    public boolean isValidMove(Position destPosition) {
        return isValidDiagonalMove(destPosition) && isDestinationAvailable(destPosition);
    }

    @Override
    public boolean canAttack(Position destPosition) {
        return isValidDiagonalMove(destPosition);
    }

    @Override
    public int getPieceValue() {
        return 3;
    }

    @Override
    public String getPieceType() {
        return PieceType.BISHOP.getName();
    }

    @Override
    public String getSymbol() {
        return getColor().equals(Color.BLACK) ? "♝" : "♗";
    }
}
