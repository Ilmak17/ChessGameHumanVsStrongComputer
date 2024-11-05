package com.game.chess.game;

import com.game.chess.board.BoardImpl;
import com.game.chess.board.Board;
import com.game.chess.game.input.SelectedPiece;
import com.game.chess.minimax.MiniMaxImpl;
import com.game.chess.minimax.MiniMax;
import com.game.chess.pieces.Piece;
import com.game.chess.pieces.Position;
import com.game.chess.pieces.enums.Color;
import com.game.chess.ui.Visual;
import com.game.chess.ui.VisualImpl;
import com.game.chess.game.input.InputHelper;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

import static java.util.Objects.isNull;

public class GameImpl implements Game {
    private final Board board;
    private final Visual visual;
    private final Scanner scanner;
    private final Random random;
    private final MiniMax miniMax;
    private boolean running;
    private boolean isWhiteTurn;
    private boolean drawOffered;
    private boolean isHumanTurn;

    private static final String MOVE = "1";
    private static final String DRAW = "2";
    private static final String SURRENDER = "3";

    public GameImpl() {
        board = new BoardImpl();
        visual = new VisualImpl(board);
        scanner = new Scanner(System.in);
        random = new Random();
        miniMax = new MiniMaxImpl();

        this.running = true;
        this.isWhiteTurn = true;
        this.drawOffered = false;
        this.isHumanTurn = true;
    }

    @Override
    public void startGame() {
        while (running) {
            visual.print();
            System.out.println("Turn of: " + (isWhiteTurn ? "White Player" : "Black Player"));

            if (isHumanTurn) {
                String choice = getOption();
                handleChoice(choice);
            } else {
                makeComputerMove();
            }
        }
        visual.print();
        scanner.close();
    }

    private String getOption() {
        System.out.println("Choose option:");
        System.out.println("1. Make a move");
        System.out.println("2. Offer a draw");
        System.out.println("3. Give up");

        return scanner.next();
    }

    private void handleChoice(String choice) {
        switch (choice) {
            case MOVE -> makeHumanMove();
            case DRAW -> offerDraw();
            case SURRENDER -> giveUp();
            default -> System.out.println("Invalid selection. Please try again.");
        }
    }

    private void makeComputerMove() {
        Optional.ofNullable(miniMax.getBestMove(board, isWhiteTurn ? Color.WHITE : Color.BLACK))
                .ifPresent(bestMove -> {
                    board.getPieceByPosition(bestMove.getStartPosition())
                            .move(bestMove.getEndPosition());
                    updateTurn();
                    getGameState();
                });
    }

    private void makeHumanMove() {
        boolean successful = false;

        while (!successful) {
            try {
                SelectedPiece pieceSelection = getSelectedPiece();
                if (!isCorrectColor(pieceSelection.getSelectedPieceIndex())) {
                    System.out.println("Invalid move: Wrong color selected. Turn is for: " + getColor());
                    return;
                }

                successful = tryMove(pieceSelection);
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            } catch (Exception e) {
                System.out.println("Invalid move: Piece not found or incorrect input. Please Try again.");
            }
        }

        updateTurn();
        getGameState();
    }

    private void offerDraw() {
        drawOffered = true;
        System.out.println("A draw was offered.");
        updateTurn();
        confirmDraw();
    }

    private void confirmDraw() {
        System.out.println("Your opponent offered a draw. Do you accept? (yes/no)");
        int randomValue = random.nextInt(2);
        if (randomValue == 1) {
            System.out.println("The game ended in a draw.");
            running = false;
            return;
        }
        System.out.println("Draw offer declined.");
        drawOffered = false;
        updateTurn();
    }

    private boolean tryMove(SelectedPiece selectedPiece) {
        String input = getInput("Select a target field (Example E3): ");
        Position targetPosition = toPosition(input);
        Piece piece = board.getPieceByPosition(selectedPiece.getPosition());

        if (isNull(piece)) {
            System.out.println("Invalid move: No piece found at the selected position.");
            return false;
        }
        if (board.isMoveLeavingKingInCheck(piece, targetPosition)) {
            System.out.println("Invalid move: Piece move is in check. Please try again.");
            return false;
        }

        piece.move(targetPosition);

        return true;
    }

    private void giveUp() {
        System.out.println("The " + getColor() + " Player Gave up. End of the game...");
        running = false;
    }

    private void getGameState() {
        Color color = getColor();
        if (board.isCheckmate(color)) {
            System.out.println("Check Mate! " + color + " Player loses.");
            running = false;
            return;
        }
        if (board.isKingInCheck(color)) {
            System.out.println("The " + color + " King is in check!");
        }
    }

    private SelectedPiece getSelectedPiece() {
        String input = getInput("Pick a piece (example E2): ");
        Position position = toPosition(input);
        int pieceIndex = InputHelper.findPieceIndexByPosition(board.getPieces(), position);

        return SelectedPiece.Builder.newBuilder()
                .selectedPieceIndex(pieceIndex)
                .position(position)
                .build();
    }

    private String getInput(String val) {
        System.out.print(val);

        return scanner.next();
    }

    private boolean isCorrectColor(int pieceIndex) {
        List<Piece> pieces = board.getPieces();

        return pieces.get(pieceIndex).getColor() == (isWhiteTurn ? Color.WHITE : Color.BLACK);
    }

    private Position toPosition(String input) {
        char colChar = input.charAt(0);
        char rowChar = input.charAt(1);

        return new Position(InputHelper.returnRow(rowChar), InputHelper.returnCol(colChar));
    }

    private Color getColor() {
        return isWhiteTurn ? Color.WHITE : Color.BLACK;
    }

    private void updateTurn() {
        isWhiteTurn = !isWhiteTurn;
        isHumanTurn = !isHumanTurn;
    }
}
