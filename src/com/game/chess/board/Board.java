package com.game.chess.board;

import com.game.chess.pieces.Position;
import com.game.chess.pieces.Piece;
import com.game.chess.pieces.enums.Color;

import java.util.List;

public interface Board {
    Piece getPieceByPosition(Position position);

    List<Piece> getPieces();

    boolean pieceExistsAt(Position position);

    boolean isNotPieceColor(Position position, Color color);

    void capture(Position position);

    boolean isKingInCheck(Color color);

    boolean isSquareUnderAttack(Position position, Color color);

    boolean isCheckmate(Color color);

    boolean isMoveLeavingKingInCheck(Piece piece, Position position);

    Piece executeTemporaryMove(Piece piece, Position position);

    void undoMove(Piece piece, Position startPosition, Position pos, Piece capturedPiece);

    Piece findKing(Color color);
}
