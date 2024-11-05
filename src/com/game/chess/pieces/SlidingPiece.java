package com.game.chess.pieces;

import com.game.chess.board.Board;
import com.game.chess.pieces.enums.Color;

public abstract class SlidingPiece extends Piece {

    SlidingPiece(Board board, Color color, Position position) {
        super(board, color, position);
    }

    protected boolean isValidLinearMove(Position destPosition) {
        if (getPosition().equals(destPosition)) {
            return false;
        }

        return isMovingInLine(destPosition) && isPathClear(destPosition);
    }

    protected boolean isValidDiagonalMove(Position destPosition) {
        if (getPosition().equals(destPosition)) {
            return false;
        }

        return isMovingDiagonally(destPosition) && isPathClear(destPosition);
    }

    protected boolean isPathClear(Position destPosition) {
        Position curPosition = getPosition();
        int stepCol = Integer.compare(destPosition.getCol(), curPosition.getCol());
        int stepRow = Integer.compare(destPosition.getRow(), curPosition.getRow());

        int col = curPosition.getCol() + stepCol;
        int row = curPosition.getRow() + stepRow;

        while (col != destPosition.getCol() || row != destPosition.getRow()) {
            if (getBoard().pieceExistsAt(new Position(row, col))) {
                return false;
            }
            col += stepCol;
            row += stepRow;
        }
        return true;
    }

    protected boolean isMovingInLine(Position destPosition) {
        return destPosition.getCol() == getPosition().getCol()
                || destPosition.getRow() == getPosition().getRow();
    }

    protected boolean isMovingDiagonally(Position destPosition) {
        Position curPosition = getPosition();
        int dCol = Math.abs(destPosition.getCol() - curPosition.getCol());
        int dRow = Math.abs(destPosition.getRow() - curPosition.getRow());

        return dCol == dRow;
    }
}
