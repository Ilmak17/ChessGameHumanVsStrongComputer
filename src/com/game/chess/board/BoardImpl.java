package com.game.chess.board;

import com.game.chess.pieces.Position;
import com.game.chess.pieces.King;
import com.game.chess.pieces.Piece;
import com.game.chess.pieces.enums.Color;

import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class BoardImpl implements Board {
    private int checkingKingIdx;
    private int threateningPieceIdx;
    private final List<Piece> pieces = new ArrayList<>();

    public BoardImpl() {
        BoardInitializer.initializeBoard(pieces, this);
    }

    @Override
    public Piece getPieceByPosition(Position position) {
        return pieces.stream()
                .filter(piece -> piece.getPosition().equals(position))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean pieceExistsAt(Position position) {
        return nonNull(getPieceByPosition(position));
    }

    @Override
    public boolean isNotPieceColor(Position position, Color color) {
        Piece piece = getPieceByPosition(position);

        return nonNull(piece) && !piece.getColor().equals(color);
    }

    @Override
    public void capture(Position position) {
        pieces.removeIf(piece -> piece.getPosition().equals(position));
    }

    @Override
    public boolean isKingInCheck(Color color) {
        Piece checkedKing = findKing(color);

        if (isNull(checkedKing)) {
            return false;
        }

        Piece threateningPiece = pieces.stream()
                .filter(piece -> !piece.getColor().equals(color) && piece.isValidMove(checkedKing.getPosition()))
                .findFirst()
                .orElse(null);

        if (nonNull(threateningPiece)) {
            threateningPieceIdx = pieces.indexOf(threateningPiece);
            checkingKingIdx = pieces.indexOf(checkedKing);

            return true;
        }

        return false;
    }

    @Override
    public boolean isSquareUnderAttack(Position position, Color color) {
        return pieces.stream()
                .filter(piece -> !piece.getColor().equals(color))
                .anyMatch(piece -> piece.canAttack(position));
    }

    @Override
    public boolean isCheckmate(Color color) {
        if (!isKingInCheck(color)) return false;

        Piece king = pieces.get(checkingKingIdx);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position potentialPosition = new Position(row, col);
                if (king.isValidMove(potentialPosition)) {
                    return false;
                }
            }
        }

        Piece threateningPiece = pieces.get(threateningPieceIdx);

        return pieces.stream()
                .filter(piece -> piece.getColor() == color && !(piece instanceof King))
                .noneMatch(piece -> piece.isValidMove(threateningPiece.getPosition()) ||
                        canBlockThreat(king.getPosition(), threateningPiece.getPosition(), piece));
    }

    @Override
    public boolean isMoveLeavingKingInCheck(Piece piece, Position position) {
        Position originalPosition = piece.getPosition();
        Piece capturedPiece = executeTemporaryMove(piece, position);
        boolean kingInCheck = isKingInCheck(piece.getColor());

        piece.forceMove(originalPosition);
        if (nonNull(capturedPiece)) {
            capturedPiece.setCaptured(false);
            getPieces().add(capturedPiece);
        }

        return kingInCheck;
    }

    @Override
    public List<Piece> getPieces() {
        return pieces;
    }

    @Override
    public Piece executeTemporaryMove(Piece piece, Position position) {
        Piece capturedPiece = null;
        if (pieceExistsAt(position) && isNotPieceColor(position, piece.getColor())) {
            capturedPiece = getPieceByPosition(position);
            capturedPiece.setCaptured(true);
            getPieces().remove(capturedPiece);
        }
        piece.forceMove(position);

        return capturedPiece;
    }

    @Override
    public void undoMove(Piece piece, Position originalPosition, Position newPosition, Piece capturedPiece) {
        piece.forceMove(originalPosition);
        if (nonNull(capturedPiece)) {
            capturedPiece.setCaptured(false);
            pieces.add(capturedPiece);
        }
    }

    @Override
    public Piece findKing(Color color) {
        return pieces.stream()
                .filter(piece -> piece.getColor().equals(color) && piece instanceof King)
                .findFirst()
                .orElse(null);
    }

    private List<Position> getBlockingPositions(Position kingPos, Position threatPos) {
        List<Position> blockingPositions = new ArrayList<>();

        if (kingPos.equals(threatPos)) {
            return blockingPositions;
        }

        int rowDirection = Integer.compare(threatPos.getRow(), kingPos.getRow());
        int colDirection = Integer.compare(threatPos.getCol(), kingPos.getCol());

        int currentRow = kingPos.getRow() + rowDirection;
        int currentCol = kingPos.getCol() + colDirection;

        while (currentRow != threatPos.getRow() || currentCol != threatPos.getCol()) {
            blockingPositions.add(new Position(currentRow, currentCol));

            currentRow += rowDirection;
            currentCol += colDirection;

            if (currentRow < 0 || currentRow > 7 || currentCol < 0 || currentCol > 7) {
                break;
            }
        }

        return blockingPositions;
    }

    private boolean canBlockThreat(Position kingPos, Position threatPos, Piece piece) {
        List<Position> blockingPositions = getBlockingPositions(kingPos, threatPos);

        return blockingPositions
                .stream()
                .anyMatch(piece::isValidMove);
    }
}
