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

    public Game() {
        setLayout(new BorderLayout());
        board = new Board();
        add(board);
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
                }
                board.whitePieces.add(board.heldPiece);
            } else {
                for (Piece p : capturedPieces) {
                    board.whitePieces.remove(p);
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