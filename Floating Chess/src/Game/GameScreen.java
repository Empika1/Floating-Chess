package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import Pieces.*;
import Replay.*;
import Utils.*;
import Board.*;
import Images.ImageManager;

public class GameScreen extends JPanel {

    Board board;

    CapturedPieces whitePiecesCaptured;
    JButton menuButton;
    ChessTimer blackTimer;

    CapturedPieces blackPiecesCaptured;
    JButton backButton;
    ChessTimer whiteTimer;

    public GameScreen() {
        setupPanel();
        addMoveToReplay();
    }

    void setupPanel() {
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

        menuButton = new JButton("Menu");
        menuButton.setFocusPainted(false);
        GridBagConstraints menuButtonConstraints = new GridBagConstraints();
        menuButtonConstraints.insets = new Insets(10, 10, 0, 10);
        menuButtonConstraints.gridheight = 1;
        menuButtonConstraints.gridwidth = 1;
        menuButtonConstraints.gridx = 1;
        menuButtonConstraints.gridy = 0;
        add(menuButton, menuButtonConstraints);

        blackTimer = new ChessTimer(ChessColor.BLACK, 10000, 14f, whitePiecesCaptured.getPreferredSize());
        blackTimer.setPreferredSize(whitePiecesCaptured.getPreferredSize());
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

        backButton = new JButton(ImageManager.resize(ImageManager.backButton, new Vector2I(12, 12)));
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                backOneMove();
            }
        });
        backButton.setEnabled(false);
        backButton.setFocusPainted(false);
        GridBagConstraints backButtonConstraints = new GridBagConstraints();
        backButtonConstraints.insets = new Insets(10, 10, 10, 10);
        backButtonConstraints.gridheight = 1;
        backButtonConstraints.gridwidth = 1;
        backButtonConstraints.gridx = 1;
        backButtonConstraints.gridy = 2;
        add(backButton, backButtonConstraints);

        whiteTimer = new ChessTimer(ChessColor.WHITE, 10000, 14f, blackPiecesCaptured.getPreferredSize());
        GridBagConstraints whiteTimerConstraints = new GridBagConstraints();
        whiteTimerConstraints.insets = new Insets(10, 10, 10, 10);
        whiteTimerConstraints.gridheight = 1;
        whiteTimerConstraints.gridwidth = 1;
        whiteTimerConstraints.gridx = 2;
        whiteTimerConstraints.gridy = 2;
        add(whiteTimer, whiteTimerConstraints);

        setVisible(true);
    }

    Replay gameReplay = new Replay();

    void addMoveToReplay() {
        gameReplay.moves.add(new Move(board.whitePieces,
                board.blackPieces,
                board.whitePiecesCaptured,
                board.blackPiecesCaptured,
                whiteTimer.getTimeLeft(),
                blackTimer.getTimeLeft(),
                board.turnNumber).copy());
    }

    void removeMoveFromReplay() {
        gameReplay.moves.remove(gameReplay.moves.size() - 1);
    }

    Vector2I mousePosGame = new Vector2I();
    boolean mouseLeftPressedGame;
    boolean mouseRightPressedGame;

    public void startGame() {
        Thread gameThread = new Thread(() -> {
            while (!isGameOver) {
                mousePosGame = board.mousePos;
                mouseLeftPressedGame = board.mouseLeftPressed;
                mouseRightPressedGame = board.mouseRightPressed;
                movePieces();
                updateTimers();
                checkForLoss();
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

        Vector2I boardBuffer = new Vector2I(-50, -50);
        if ((!Geometry.isPointInRect(boardBuffer, boardSizeI.subtract(boardBuffer), mousePosGame))
                && board.heldPiece != null) {
            board.heldPiece.setVisiblePos(board.heldPiece.getTruePos());
            if (board.turn == ChessColor.WHITE)
                board.whitePieces.add(board.heldPiece);
            else
                board.blackPieces.add(board.heldPiece);
            board.heldPiece = null;
            mouseLeftPressedGame = false;
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
                    whiteTimer.pause();
                    blackTimer.resume();
                } else {
                    for (Piece p : capturedPieces) {
                        board.whitePieces.remove(p);
                        board.whitePiecesCaptured.add(p);
                    }
                    board.blackPieces.add(board.heldPiece);
                    blackTimer.pause();
                    whiteTimer.resume();
                }

                board.heldPiece = null;
                addMoveToReplay();
                board.piecesThatWillBeCaptured.clear();
                board.turnNumber++;
                backButton.setEnabled(true);
            }
        }
    }

    void updateTimers() {
        blackTimer.updateTime();
        whiteTimer.updateTime();
    }

    boolean isGameOver = false;
    ChessColor wonPlayer = null;
    LossState lossState = null;

    void checkForLoss() {
        if (blackTimer.getTimeLeft() == 0) {
            isGameOver = true;
            wonPlayer = ChessColor.WHITE;
            lossState = LossState.OUTOFTIME;
        } else if (whiteTimer.getTimeLeft() == 0) {
            isGameOver = true;
            wonPlayer = ChessColor.BLACK;
            lossState = LossState.OUTOFTIME;
        } else {
            for (Piece p : board.whitePiecesCaptured) {
                if (p.getPieceType() == PieceType.KING) {
                    isGameOver = true;
                    wonPlayer = ChessColor.BLACK;
                    lossState = LossState.KINGCAPTURED;
                }
            }
            for (Piece p : board.blackPiecesCaptured) {
                if (p.getPieceType() == PieceType.KING) {
                    isGameOver = true;
                    wonPlayer = ChessColor.WHITE;
                    lossState = LossState.KINGCAPTURED;
                }
            }
        }

        if (isGameOver) {
            gameReplay.lossState = lossState;
            backButton.setEnabled(false);
            showGameOverDialog();
        }
    }

    void showGameOverDialog() {
        String wonColor = wonPlayer == ChessColor.WHITE ? "White" : "Black";
        String lostColor = wonPlayer == ChessColor.WHITE ? "Black" : "White";
        String winString = lossState == LossState.KINGCAPTURED
                ? wonColor + " wins, by capturing " + lostColor + "'s king!"
                : wonColor + " wins, as " + lostColor + " has run out of time!";
        ImageIcon winIcon = ImageManager.resize(wonPlayer == ChessColor.WHITE ? ImageManager.wk : ImageManager.bk,
                new Vector2I(64, 64));

        Object[] options = { "Play Again",
                "Menu",
                "View Replay" };
        int n = JOptionPane.showOptionDialog(null,
                winString,
                "Game Over",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                winIcon,
                options,
                options[0]);
    }

    void backOneMove() {
        Move lastMove = gameReplay.moves.get(gameReplay.moves.size() - 2).copy();
        board.whitePieces = lastMove.whitePieces();
        board.blackPieces = lastMove.blackPieces();
        board.whitePiecesCaptured = lastMove.whitePiecesCaptured();
        board.blackPiecesCaptured = lastMove.blackPiecesCaptured();
        whiteTimer.setTimeLeft(lastMove.whiteTimeLeft());
        blackTimer.setTimeLeft(lastMove.blackTimeLeft());
        board.turnNumber = lastMove.turn();

        if (gameReplay.moves.size() <= 2)
            backButton.setEnabled(false);

        removeMoveFromReplay();
    }
}