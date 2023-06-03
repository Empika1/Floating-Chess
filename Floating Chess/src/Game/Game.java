package Game;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.*;
import Pieces.*;
import Utils.*;
import Board.*;

public class Game extends JPanel {

    Board board;
    CapturedPieces whitePiecesCaptured;
    JButton menuButton;
    JTextPane blackTimer;

    CapturedPieces blackPiecesCaptured;
    JTextPane whiteTimer;

    public Game() {
        setLayout(new GridBagLayout());
        setBackground(UIManager.getColor("Panel.background"));

        board = new Board();
        GridBagConstraints boardConstraints = new GridBagConstraints();
        boardConstraints.insets = new Insets(10, 10, 0, 10);
        boardConstraints.gridheight = 1;
        boardConstraints.gridwidth = 3;
        boardConstraints.gridx = 0;
        boardConstraints.gridy = 1;
        add(board, boardConstraints);

        whitePiecesCaptured = new CapturedPieces(ChessColor.WHITE, board);
        GridBagConstraints whitePiecesCapturedConstraints = new GridBagConstraints();
        whitePiecesCapturedConstraints.insets = new Insets(10, 10, 0, 10);
        whitePiecesCapturedConstraints.gridheight = 1;
        whitePiecesCapturedConstraints.gridwidth = 1;
        whitePiecesCapturedConstraints.gridx = 0;
        whitePiecesCapturedConstraints.gridy = 0;
        add(whitePiecesCaptured, whitePiecesCapturedConstraints);

        menuButton = new JButton("Menu");;
        GridBagConstraints menuButtonConstraints = new GridBagConstraints();
        menuButtonConstraints.insets = new Insets(10, 10, 0, 10);
        menuButtonConstraints.gridheight = 1;
        menuButtonConstraints.gridwidth = 1;
        menuButtonConstraints.gridx = 1;
        menuButtonConstraints.gridy = 0;
        add(menuButton, menuButtonConstraints);

        blackTimer = new JTextPane();
        blackTimer.setText("10:00");
        blackTimer.setPreferredSize(whitePiecesCaptured.getPreferredSize());
        blackTimer.setFont(blackTimer.getFont().deriveFont(14f));
        StyledDocument blackTimerDoc = blackTimer.getStyledDocument();
        SimpleAttributeSet blackTimerCenter = new SimpleAttributeSet();
        StyleConstants.setAlignment(blackTimerCenter, StyleConstants.ALIGN_CENTER);
        blackTimerDoc.setParagraphAttributes(0, blackTimerDoc.getLength(), blackTimerCenter, false);
        GridBagConstraints blackTimerConstraints = new GridBagConstraints();
        blackTimerConstraints.insets = new Insets(10, 10, 0, 10);
        blackTimerConstraints.gridheight = 1;
        blackTimerConstraints.gridwidth = 1;
        blackTimerConstraints.gridx = 2;
        blackTimerConstraints.gridy = 0;
        add(blackTimer, blackTimerConstraints);

        blackPiecesCaptured = new CapturedPieces(ChessColor.BLACK, board);
        GridBagConstraints blackPiecesCapturedConstraints = new GridBagConstraints();
        blackPiecesCapturedConstraints.insets = new Insets(10, 10, 10, 10);
        blackPiecesCapturedConstraints.gridheight = 1;
        blackPiecesCapturedConstraints.gridwidth = 1;
        blackPiecesCapturedConstraints.gridx = 0;
        blackPiecesCapturedConstraints.gridy = 2;
        add(blackPiecesCaptured, blackPiecesCapturedConstraints);

        whiteTimer = new JTextPane();
        whiteTimer.setText("10:00");
        whiteTimer.setPreferredSize(whitePiecesCaptured.getPreferredSize());
        whiteTimer.setFont(whiteTimer.getFont().deriveFont(14f));
        StyledDocument whiteTimerDoc = whiteTimer.getStyledDocument();
        SimpleAttributeSet whiteTimerCenter = new SimpleAttributeSet();
        StyleConstants.setAlignment(whiteTimerCenter, StyleConstants.ALIGN_CENTER);
        whiteTimerDoc.setParagraphAttributes(0, whiteTimerDoc.getLength(), whiteTimerCenter, false);
        GridBagConstraints whiteTimerConstraints = new GridBagConstraints();
        whiteTimerConstraints.insets = new Insets(10, 10, 10, 10);
        whiteTimerConstraints.gridheight = 1;
        whiteTimerConstraints.gridwidth = 1;
        whiteTimerConstraints.gridx = 2;
        whiteTimerConstraints.gridy = 2;
        add(whiteTimer, whiteTimerConstraints);

        setVisible(true);
    }

    Vector2I mousePosGame = new Vector2I();
    boolean mouseLeftPressedGame;
    boolean mouseRightPressedGame;

    public void startRendering() {
        Thread gameThread = new Thread(() -> {
            while (true) {
                mousePosGame = board.mousePos;
                mouseLeftPressedGame = board.mouseLeftPressed;
                mouseRightPressedGame = board.mouseRightPressed;
                movePieces();
                updateTimers();
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

        Vector2I boardBuffer = new Vector2I(-20, -20);
        if ((!Geometry.isPointInRect(boardBuffer, boardSizeI.subtract(boardBuffer), mousePosGame))
                && board.heldPiece != null) {
            board.heldPiece.setVisiblePos(board.heldPiece.getTruePos());
            if (board.turn == ChessColor.WHITE)
                board.whitePieces.add(board.heldPiece);
            else
                board.blackPieces.add(board.heldPiece);
            board.heldPiece = null;
            board.piecesThatWillBeCaptured.clear();
        }

        if (mouseLeftPressedGame && board.heldPiece == null) {
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

        if (board.heldPiece != null) {
            Vector2I closestValidPointToMouse = board.heldPiece.closestValidPoint(mousePosGame, board.whitePieces,
                    board.blackPieces);
            board.heldPiece.setVisiblePos(closestValidPointToMouse);

            ArrayList<Piece> capturedPieces = board.heldPiece.oppositeColorPiecesOverlapping(
                    board.heldPiece.getVisiblePos(),
                    board.whitePieces, board.blackPieces);
            board.piecesThatWillBeCaptured = capturedPieces;

            if (!mouseLeftPressedGame) {
                board.heldPiece.setTruePos(board.heldPiece.getVisiblePos(), true);
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
                board.piecesThatWillBeCaptured.clear();
                board.turnNumber++;
            }
        }
    }

    long currentTimeMs = System.currentTimeMillis();
    long oldTimeMs = System.currentTimeMillis();
    long whiteTimeRemainingMs = 600000;
    long whiteTimeRemainingMsOld = whiteTimeRemainingMs;
    long blackTimeRemainingMs = 600000;
    long blackTimeRemainingMsOld = blackTimeRemainingMs;
    static final int msToUpdate = 100;
    public void updateTimers() {
        currentTimeMs = System.currentTimeMillis();
        long deltaTime = currentTimeMs - oldTimeMs;
        if(board.turn == ChessColor.WHITE) {
            whiteTimeRemainingMs -= deltaTime;
            if(whiteTimeRemainingMsOld >= whiteTimeRemainingMs + msToUpdate) {
                whiteTimeRemainingMsOld = whiteTimeRemainingMs;
                whiteTimer.setText(StringFormatting.msToTime(whiteTimeRemainingMs));
            }
        }
        else {
            blackTimeRemainingMs -= deltaTime;
            if(blackTimeRemainingMs % 100 == 0)
            if(blackTimeRemainingMsOld >= blackTimeRemainingMs + msToUpdate) {
                blackTimeRemainingMsOld = blackTimeRemainingMs;
                blackTimer.setText(StringFormatting.msToTime(blackTimeRemainingMs));
            }
        }

        oldTimeMs = currentTimeMs;
    }
}