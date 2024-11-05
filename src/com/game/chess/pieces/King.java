package com.game.chess.pieces;

import com.game.chess.board.Board;
import com.game.chess.pieces.enums.Color;
import com.game.chess.pieces.enums.PieceType;

import java.util.List;

import static java.util.Objects.isNull;

public class King extends Piece {

    private static int rookId;

    public King(Board board, Color color, Position position) {
        super(board, color, position);
    }

    @Override
    public boolean isValidMove(Position destPosition) {
        if (getPosition().equals(destPosition)) {
            return false;
        }
        Position curPosition = getPosition();
        int dCol = Math.abs(destPosition.getCol() - curPosition.getCol());
        int dRow = Math.abs(destPosition.getRow() - curPosition.getRow());

        if (dCol == 2 && dRow == 0) {
            return canDoCastling(destPosition);
        }

        Position originalPosition = getPosition();
        forceMove(destPosition);
        boolean isUnderAttack = getBoard().isSquareUnderAttack(destPosition, getColor());
        forceMove(originalPosition);

        if (isUnderAttack) return false;

        return dCol <= 1 && dRow <= 1 && isDestinationAvailable(destPosition);
    }

    @Override
    public void move(Position destPosition) {
        if (canDoCastling(destPosition)) {
            validateMove(destPosition);

            int kingNewCol = destPosition.getCol() > getPosition().getCol() ? 6 : 2;
            int rookNewCol = destPosition.getCol() > getPosition().getCol() ? 5 : 3;
            List<Piece> pieces = getBoard().getPieces();

            forceMove(new Position(destPosition.getRow(), kingNewCol));
            pieces.get(rookId).forceMove(new Position(destPosition.getRow(), rookNewCol));

            setMoved(true);
            pieces.get(rookId).setMoved(true);

            return;
        }

        super.move(destPosition);
    }

    @Override
    public boolean canAttack(Position destPosition) {
        if (getPosition().equals(destPosition)) {
            return false;
        }

        Position curPosition = getPosition();
        int dCol = Math.abs(destPosition.getCol() - curPosition.getCol());
        int dRow = Math.abs(destPosition.getRow() - curPosition.getRow());

        return dCol <= 1 && dRow <= 1;
    }

    @Override
    public int getPieceValue() {
        return 100;
    }

    @Override
    public String getPieceType() {
        return PieceType.KING.getName();
    }

    @Override
    public String getSymbol() {
        return getColor().equals(Color.BLACK) ? "♚" : "♔";
    }

    private boolean canDoCastling(Position destPosition) {
        Board board = getBoard();

        if (board.isKingInCheck(getColor())) {
            return false;
        }

        Position curPosition = getPosition();
        int rookCol = (destPosition.getCol() > curPosition.getCol()) ? 7 : 0;
        int rookRow = curPosition.getRow();

        Piece rook = board.getPieceByPosition(new Position(rookRow, rookCol));
        if (isNull(rook) || !(rook instanceof Rook) || rook.isMoved() || rook.getColor() != getColor()) {
            return false;
        }

        int minCol = Math.min(curPosition.getCol(), rookCol);
        int maxCol = Math.max(curPosition.getCol(), rookCol);
        for (int i = minCol + 1; i < maxCol; i++) {
            Position positionBetween = new Position(curPosition.getRow(), i);
            if (board.pieceExistsAt(positionBetween) || board.isSquareUnderAttack(positionBetween, getColor())) {
                return false;
            }
        }

        List<Piece> pieces = board.getPieces();
        setRookId(pieces.indexOf(rook));

        return true;
    }

    private static synchronized void setRookId(int id) {
        rookId = id;
    }
}
