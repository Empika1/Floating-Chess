package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import Pieces.*;
import Utils.*;
import Images.*;
import Board.*;

public class Game extends JPanel {

    Board board;
    CapturedPieces whitePiecesCaptured;
    CapturedPieces blackPiecesCaptured;
    public Game() {
        setLayout(new GridBagLayout());
        board = new Board();
        Insets boardPadding = new Insets(10, 10, 10, 10);
        GridBagConstraints boardConstraints = new GridBagConstraints();
        boardConstraints.insets = boardPadding;
        boardConstraints.gridheight = 1;
        boardConstraints.gridwidth = 3;
        boardConstraints.gridx = 0;
        boardConstraints.gridy = 1;
        add(board, boardConstraints);

        whitePiecesCaptured = new CapturedPieces(ChessColor.WHITE, board);
        GridBagConstraints whitePiecesCapturedConstraints = new GridBagConstraints();
        whitePiecesCapturedConstraints.insets = boardPadding;
        whitePiecesCapturedConstraints.gridheight = 1;
        whitePiecesCapturedConstraints.gridwidth = 1;
        whitePiecesCapturedConstraints.gridx = 0;
        whitePiecesCapturedConstraints.gridy = 0;
        add(whitePiecesCaptured, whitePiecesCapturedConstraints);

        blackPiecesCaptured = new CapturedPieces(ChessColor.BLACK, board);
        GridBagConstraints blackPiecesCapturedConstraints = new GridBagConstraints();
        blackPiecesCapturedConstraints.insets = boardPadding;
        blackPiecesCapturedConstraints.gridheight = 1;
        blackPiecesCapturedConstraints.gridwidth = 1;
        blackPiecesCapturedConstraints.gridx = 0;
        blackPiecesCapturedConstraints.gridy = 2;
        add(blackPiecesCaptured, blackPiecesCapturedConstraints);

        setVisible(true);
    }

    Vector2I mousePosGame = new Vector2I();
    boolean mousePressedGame;

    public void startRendering() {
        Thread gameThread = new Thread(() -> {
            while (true) {
                mousePosGame = board.mousePos;
                mousePressedGame = board.mousePressed;
                movePieces();
                board.draw();
                whitePiecesCaptured.draw(board);
                blackPiecesCaptured.draw(board);
            }
        });
        gameThread.run();
    }

    public static final Vector2I boardSizeI = new Vector2I(2048, 2048);

    public void movePieces() {
        if (board.turnNumber % 2 == 0)
            board.turn = ChessColor.BLACK;
        else
            board.turn = ChessColor.WHITE;

        if (mousePressedGame && board.heldPiece == null) {
            if (board.turn == ChessColor.WHITE) {
                for (Piece p : board.whitePieces) {
                    if (p.isInHitbox(mousePosGame)) {
                        board.heldPiece = p;
                        board.whitePieces.remove(p);
                        break;
                    }
                }
            } else {
                for (Piece p : board.blackPieces) {
                    if (p.isInHitbox(mousePosGame)) {
                        board.heldPiece = p;
                        board.blackPieces.remove(p);
                        break;
                    }
                }
            }
        }

        if (!mousePressedGame && board.heldPiece != null) {
            board.heldPiece.setTruePos(board.heldPiece.getVisiblePos(), true);
            ArrayList<Piece> capturedPieces = board.heldPiece.oppositeColorPiecesOverlapping(
                    board.heldPiece.getVisiblePos(),
                    board.whitePieces, board.blackPieces);
            if (board.turn == ChessColor.WHITE) {
                for (Piece p : capturedPieces) {
                    board.blackPieces.remove(p);
                    board.blackPiecesCaptured.add(p);
                }
                board.whitePieces.add(board.heldPiece);
            } else {
                for (Piece p : capturedPieces) {
                    board.whitePieces.remove(p);
                    board.whitePiecesCaptured.add(p);
                }
                board.blackPieces.add(board.heldPiece);
            }
            board.heldPiece = null;
            board.turnNumber++;
        }

        if (board.heldPiece != null) {
            Vector2I closestValidPointToMouse = board.heldPiece.closestValidPoint(mousePosGame, board.whitePieces,
                    board.blackPieces);
            board.heldPiece.setVisiblePos(closestValidPointToMouse);
        }
    }
}