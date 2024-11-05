package com.game.chess.pieces;

import com.game.chess.board.Board;
import com.game.chess.pieces.enums.Color;
import com.game.chess.pieces.enums.PieceType;

public class Knight extends Piece {

    public Knight(Board board, Color color, Position position) {
        super(board, color, position);
    }

    @Override
    public boolean isValidMove(Position destPosition) {
        return isDestinationAvailable(destPosition) && isValidKnightMove(destPosition);
    }

    @Override
    public boolean canAttack(Position destPosition) {
        if (getPosition().equals(destPosition)) {
            return false;
        }

        return isValidKnightMove(destPosition);
    }

    @Override
    public int getPieceValue() {
        return 3;
    }

    @Override
    public String getPieceType() {
        return PieceType.KNIGHT.getName();
    }

    @Override
    public String getSymbol() {
        return getColor().equals(Color.BLACK) ? "♞" : "♘";
    }

    private boolean isValidKnightMove(Position destPosition) {
        Position curPosition = getPosition();
        int dCol = Math.abs(destPosition.getCol() - curPosition.getCol());
        int dRow = Math.abs(destPosition.getRow() - curPosition.getRow());

        return (dCol == 1 && dRow == 2) || (dCol == 2 && dRow == 1);
    }
}
