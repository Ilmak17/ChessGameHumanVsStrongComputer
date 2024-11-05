package com.game.chess.minimax;

import com.game.chess.board.Board;
import com.game.chess.pieces.Piece;
import com.game.chess.pieces.Position;
import com.game.chess.pieces.enums.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

public class MiniMaxImpl implements MiniMax {

    private static final int DEPTH = 3;
    private static final int MAX = Integer.MAX_VALUE;
    private static final int MIN = Integer.MIN_VALUE;
    private static final List<Position> CENTER_POSITIONS = List.of(
            new Position(3, 3), new Position(3, 4),
            new Position(4, 3), new Position(4, 4),
            new Position(2, 3), new Position(2, 4),
            new Position(3, 2), new Position(3, 5),
            new Position(4, 2), new Position(4, 5),
            new Position(5, 3), new Position(5, 4)
    );

    @Override
    public Move getBestMove(Board board, Color color) {
        return minimax(board, color, DEPTH, MIN, MAX, true)
                .orElse(new Move(0));
    }

    private Optional<Move> minimax(Board board, Color color, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || board.isCheckmate(color)) {
            return Optional.of(new Move(calculateHeuristicValue(board, color)));
        }

        Move bestMove = null;
        int bestScore = maximizingPlayer ? MIN : MAX;

        List<Piece> pieces = new ArrayList<>(board.getPieces());

        for (Piece piece : pieces) {
            if (isPlayerPiece(piece, maximizingPlayer, color)) {
                for (Position position : piece.getAllPossibleMoves()) {
                    if (board.isMoveLeavingKingInCheck(piece, position)) continue;

                    Position startPosition = piece.getPosition();
                    Piece capturedPiece = board.executeTemporaryMove(piece, position);

                    int evalScore = minimax(board, color.opposite(), depth - 1, alpha, beta, !maximizingPlayer)
                            .map(Move::getScore)
                            .orElse(maximizingPlayer ? MIN : MAX);

                    board.undoMove(piece, startPosition, position, capturedPiece);

                    if (maximizingPlayer) {
                        if (evalScore > bestScore) {
                            bestScore = evalScore;
                            bestMove = new Move(startPosition, position, bestScore);
                        }
                        alpha = Math.max(alpha, evalScore);
                    } else {
                        if (evalScore < bestScore) {
                            bestScore = evalScore;
                            bestMove = new Move(startPosition, position, bestScore);
                        }
                        beta = Math.min(beta, evalScore);
                    }

                    if (beta <= alpha) {
                        break;
                    }
                }
            }
        }

        if (isNull(bestMove)) return Optional.of(new Move(bestScore));

        return Optional.of(bestMove);
    }

    private boolean isPlayerPiece(Piece piece, boolean maximizingPlayer, Color color) {
        return piece.getColor() == (maximizingPlayer ? color : color.opposite());
    }

    private int calculateHeuristicValue(Board board, Color color) {
        return evaluatePieceScore(board, color)
                + evaluateKingSafetyScore(board, color)
                + evaluateControlCenterScore(board, color)
                + evaluateMobilityScore(board, color);
    }

    private static int evaluatePieceScore(Board board, Color color) {
        return board.getPieces().stream()
                .mapToInt(piece -> piece.getColor() == color ? piece.getPieceValue() : -piece.getPieceValue())
                .sum();
    }

    private int evaluateKingSafetyScore(Board board, Color color) {
        return Optional.ofNullable(board.findKing(color))
                .map(Piece::getPosition)
                .filter(pos -> pos.getRow() > 1 && pos.getRow() < 6 && pos.getCol() > 1 && pos.getCol() < 6)
                .map(pos -> -50)
                .orElse(0);
    }

    private int evaluateControlCenterScore(Board board, Color color) {
       return board.getPieces().stream()
                .flatMap(piece -> piece.getAllPossibleMoves().stream()
                        .filter(CENTER_POSITIONS::contains)
                        .map(move -> piece.getColor() == color ? 5 : -5))
                .mapToInt(Integer::intValue)
                .sum();
    }

    private int evaluateMobilityScore(Board board, Color color) {
        return board.getPieces().stream()
                .filter(piece -> piece.getColor() == color)
                .mapToInt(piece -> piece.getAllPossibleMoves().size())
                .sum() -
                board.getPieces().stream()
                        .filter(piece -> piece.getColor() != color)
                        .mapToInt(piece -> piece.getAllPossibleMoves().size())
                        .sum();
    }
}
