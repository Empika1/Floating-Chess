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
import App.*;

public class GameScreen extends JPanel {

    Board board;

    CapturedPieces whitePiecesCaptured;
    JButton menuButton;
    ChessTimer blackTimer;

    CapturedPieces blackPiecesCaptured;
    JButton backButton;
    ChessTimer whiteTimer;

    public GameScreen(int time) {
        setupPanel(time);
        addMoveToReplay();
    }

    void setupPanel(int time) {
        setLayout(new GridBagLayout());
        setBackground(UIManager.getColor("Panel.background"));

        board = new Board(true);
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
        menuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                App.displayMenuScreen();
            }
        });
        GridBagConstraints menuButtonConstraints = new GridBagConstraints();
        menuButtonConstraints.insets = new Insets(10, 10, 0, 10);
        menuButtonConstraints.gridheight = 1;
        menuButtonConstraints.gridwidth = 1;
        menuButtonConstraints.gridx = 1;
        menuButtonConstraints.gridy = 0;
        add(menuButton, menuButtonConstraints);

        blackTimer = new ChessTimer(ChessColor.BLACK, time, 14f, whitePiecesCaptured.getPreferredSize());
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

        whiteTimer = new ChessTimer(ChessColor.WHITE, time, 14f, blackPiecesCaptured.getPreferredSize());
        whiteTimer.setPreferredSize(blackPiecesCaptured.getPreferredSize());
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
            Rook rightRook = null;
            Rook leftRook = null;
            //If king has castled, move the corresponding rook to its castling position too
            if (board.heldPiece.getPieceType() == PieceType.KING && !board.heldPiece.getHasMoved()) {
                ArrayList<Piece> sameColorPieces = board.heldPiece.getColor() == ChessColor.WHITE ? board.whitePieces
                        : board.blackPieces;
                for (Piece p : sameColorPieces) {
                    if (p.getPieceType() == PieceType.ROOK && p.getTruePos().x > GameScreen.boardSizeI.x * 0.5
                            && !p.getHasMoved()) {
                        rightRook = (Rook) p;
                        break;
                    }
                }
                for (Piece p : sameColorPieces) {
                    if (p.getPieceType() == PieceType.ROOK && p.getTruePos().x < GameScreen.boardSizeI.x * 0.5
                            && !p.getHasMoved()) {
                        leftRook = (Rook) p;
                        break;
                    }
                }
                if (rightRook != null)
                    rightRook.setVisiblePos(rightRook.getTruePos().copy());
                if (leftRook != null)
                    leftRook.setVisiblePos(leftRook.getTruePos().copy());
                if (board.heldPiece.getVisiblePos().x == (int) (GameScreen.boardSizeI.x * 6.5 / 8)) {
                    rightRook.setVisiblePos(
                            new Vector2I((int) (GameScreen.boardSizeI.x * 5.5 / 8), rightRook.getTruePos().y));
                } else if (board.heldPiece.getVisiblePos().x == (int) (GameScreen.boardSizeI.x * 2.5 / 8)) {
                    leftRook.setVisiblePos(
                            new Vector2I((int) (GameScreen.boardSizeI.x * 3.5 / 8), leftRook.getTruePos().y));
                }
            }

            ArrayList<Piece> capturedPieces = board.heldPiece.oppositeColorPiecesOverlapping(
                    board.heldPiece.getVisiblePos(),
                    board.whitePieces, board.blackPieces);
            board.piecesThatWillBeCaptured = capturedPieces;

            if (!mouseLeftPressedGame) {
                board.heldPiece.setTruePos(board.heldPiece.getVisiblePos(), true); //move the heldpiece to its proper position
                if (rightRook != null)
                    rightRook.setTruePos(rightRook.getVisiblePos(), true); //move the rooks to their proper positions if the move was a castle
                if (leftRook != null)
                    leftRook.setTruePos(leftRook.getVisiblePos(), true);
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

                if(board.heldPiece.getPieceType() == PieceType.PAWN) {
                    if(board.heldPiece.getColor() == ChessColor.WHITE) {
                        if(board.heldPiece.getTruePos().y <= boardSizeI.y * 0.5 / 8 + 2)
                            showPiecePromotionDialog((Pawn)board.heldPiece);
                    }
                    else {
                        if(board.heldPiece.getTruePos().y >= boardSizeI.y * 7.5 / 8 - 2)
                            showPiecePromotionDialog((Pawn)board.heldPiece);
                    }
                }

                board.heldPiece = null;
                board.piecesThatWillBeCaptured.clear();
                board.turnNumber++;
                addMoveToReplay();
                backButton.setEnabled(true);
            }
        }
    }

    void showPiecePromotionDialog(Pawn p) {
        Object[] options = { "Knight", "Bishop", "Rook", "Queen" };
        Object n = JOptionPane.showInputDialog(null,
                "Choose a piece to promote your pawn to", "Pawn Promotion",
                JOptionPane.PLAIN_MESSAGE, null,
                options, options[3]);
        Piece promotedPiece;
        if (n.equals(options[0]))
            promotedPiece = new Knight();
        else if (n.equals(options[1]))
            promotedPiece = new Bishop();
        else if (n.equals(options[2]))
            promotedPiece = new Rook();
        else
            promotedPiece = new Queen();

        promotedPiece.setColor(p.getColor());
        promotedPiece.setTruePos(p.getTruePos(), true);
        promotedPiece.setID(p.getID());

        if(p.getColor() == ChessColor.WHITE) {
            board.whitePieces.remove(p);
            board.whitePieces.add(promotedPiece);
        }
        else {
            board.blackPieces.remove(p);
            board.blackPieces.add(promotedPiece);
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
        switch (n) {
            case 0:
                App.displayTimeControlDialog();
                break;
            case 1:
                App.displayMenuScreen();
                break;
            case 2:
                App.displayReplayScreen(gameReplay);
                break;
        }
    }

    void backOneMove() {
        if (board.heldPiece == null) {
            Move lastMove = gameReplay.moves.get(gameReplay.moves.size() - 2).copy();
            board.whitePieces = lastMove.whitePieces();
            board.blackPieces = lastMove.blackPieces();
            board.whitePiecesCaptured.clear();
            board.whitePiecesCaptured.addAll(lastMove.whitePiecesCaptured());
            board.blackPiecesCaptured.clear();
            board.blackPiecesCaptured.addAll(lastMove.blackPiecesCaptured());
            whiteTimer.setTimeLeft(lastMove.whiteTimeLeft());
            blackTimer.setTimeLeft(lastMove.blackTimeLeft());
            board.turnNumber = lastMove.turnNumber();
            board.turn = lastMove.turnNumber() % 2 == 0 ? ChessColor.BLACK : ChessColor.WHITE;

            if (gameReplay.moves.size() <= 2)
                backButton.setEnabled(false);

            removeMoveFromReplay();
        }
    }
}