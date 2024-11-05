package com.game.chess.pieces;

import com.game.chess.board.Board;
import com.game.chess.pieces.enums.Color;
import com.game.chess.pieces.enums.PieceType;

import java.util.List;
import java.util.Scanner;

import static com.game.chess.pieces.enums.PieceType.BISHOP;
import static com.game.chess.pieces.enums.PieceType.KNIGHT;
import static com.game.chess.pieces.enums.PieceType.QUEEN;
import static com.game.chess.pieces.enums.PieceType.ROOK;
import static java.util.Objects.nonNull;

public class Pawn extends Piece {
    private static final List<String> PROMOTION_TYPES =
            List.of(QUEEN.getName(), ROOK.getName(), BISHOP.getName(), KNIGHT.getName());

    public Pawn(Board board, Color color, Position position) {
        super(board, color, position);
    }

    @Override
    public boolean isValidMove(Position destPosition) {
        if (getPosition().equals(destPosition)) {
            return false;
        }
        Board board = getBoard();
        Position curPosition = getPosition();
        int direction = getColor().equals(Color.WHITE) ? 1 : -1;

        if (destPosition.getCol() == curPosition.getCol()) {
            if (destPosition.getRow() == curPosition.getRow() + direction) {
                return !board.pieceExistsAt(destPosition);
            } else if ((curPosition.getRow() == 1 && getColor().equals(Color.WHITE)) ||
                    (curPosition.getRow() == 6 && getColor().equals(Color.BLACK))) {
                Position newPos = new Position(curPosition.getRow() + direction, curPosition.getCol());

                return destPosition.getRow() == curPosition.getRow() + 2 * direction
                        && !board.pieceExistsAt(newPos)
                        && !board.pieceExistsAt(destPosition);
            }
        } else if (Math.abs(destPosition.getCol() - curPosition.getCol()) == 1 &&
                destPosition.getRow() == curPosition.getRow() + direction) {
            Piece pieceByPosition = board.getPieceByPosition(destPosition);
            return nonNull(pieceByPosition) && !pieceByPosition.getColor().equals(getColor());
        }

        return false;
    }

    @Override
    public void move(Position destPosition) {
        Board board = getBoard();
        validateMove(destPosition);

        if (board.pieceExistsAt(destPosition) && board.isNotPieceColor(destPosition, getColor())) {
            board.capture(destPosition);
        }

        forceMove(destPosition);

        if (shouldPromote()) {
            promote(new Scanner(System.in));
        }
    }

    @Override
    public boolean canAttack(Position destPosition) {
        Position curPosition = getPosition();
        if (curPosition.equals(destPosition)) {
            return false;
        }

        int direction = getColor().equals(Color.WHITE) ? 1 : -1;
        int dCol = Math.abs(destPosition.getCol() - curPosition.getCol());
        int dRow = destPosition.getRow() - curPosition.getRow();

        return dCol == 1 && dRow == direction;
    }

    @Override
    public int getPieceValue() {
        return 1;
    }

    @Override
    public String getPieceType() {
        return PieceType.PAWN.getName();
    }

    @Override
    public String getSymbol() {
        return getColor().equals(Color.BLACK) ? "♟" : "♙";
    }

    private void promote(Scanner scanner) {
        System.out.println("Choose a new piece (Q - Queen, R - Rook, B - Bishop, N - Knight): ");
        String promotionChoice = String.valueOf(scanner.next().toUpperCase().charAt(0));

        while (!PROMOTION_TYPES.contains(promotionChoice)) {
            System.out.println("Choose correct piece (Q - Queen, R - Rook, B - Bishop, N - Knight): ");
            promotionChoice = String.valueOf(scanner.next().toUpperCase().charAt(0));
        }

        List<Piece> pieces = getBoard().getPieces();
        pieces.remove(this);

        switch (PieceType.convertToType(promotionChoice)) {
            case QUEEN -> pieces.add(new Queen(getBoard(), getColor(), getPosition()));
            case ROOK -> pieces.add(new Rook(getBoard(), getColor(), getPosition()));
            case BISHOP -> pieces.add(new Bishop(getBoard(), getColor(), getPosition()));
            case KNIGHT -> pieces.add(new Knight(getBoard(), getColor(), getPosition()));
            default -> throw new IllegalArgumentException("Incorrect promotion choice");
        }
    }

    private boolean shouldPromote() {
        return (getColor().equals(Color.WHITE) && getPosition().getRow() == 7) ||
                (getColor().equals(Color.BLACK) && getPosition().getRow() == 0);
    }
}
