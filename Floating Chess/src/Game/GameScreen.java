//The screen where the game is played. This is the meat of the game.
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
    Board board; //the board used for this gamescreen

    CapturedPieces whitePiecesCaptured; //the white capturedpieces instance
    JButton menuButton; //the button that goes to the menu
    ChessTimer blackTimer; //the timer for the black player

    CapturedPieces blackPiecesCaptured; //the black capturedpieces instance
    JButton backButton; //the button that goes back one move
    ChessTimer whiteTimer; //the timer for the white player

    public GameScreen(int time) { //time is the time control that this game is played on
        setupPanel(time);
        addMoveToReplay(); //add the initial state of the game to the replay
    }

    void setupPanel(int time) {
        setLayout(new GridBagLayout()); //set the panel layout to a gridbaglayout
        setBackground(UIManager.getColor("Panel.background")); //set the background color to what it should be

        board = new Board(true); //create a board, and setup the pieces
        GridBagConstraints boardConstraints = new GridBagConstraints(); //put it where it should be in the container
        boardConstraints.insets = new Insets(10, 10, 0, 10);
        boardConstraints.gridheight = 1;
        boardConstraints.gridwidth = 3;
        boardConstraints.gridx = 0;
        boardConstraints.gridy = 1;
        add(board, boardConstraints); //add it to the panel

        whitePiecesCaptured = new CapturedPieces(ChessColor.WHITE, board); //same as board, just make it and add it
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
                App.displayMenuScreen(); //if the menubutton is pressed, go to the menu
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
                backOneMove(); //if the back button is pressed, go back one move
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

    Replay gameReplay = new Replay(); //the replay of the game currently being played

    void addMoveToReplay() { //create a move and add it to the replay 
        gameReplay.moves.add(new Move(board.whitePieces,
                board.blackPieces,
                board.whitePiecesCaptured,
                board.blackPiecesCaptured,
                whiteTimer.getTimeLeft(),
                blackTimer.getTimeLeft(),
                board.turnNumber).copy());
    }

    void removeMoveFromReplay() { //remove the last move from the replay. This is used in the back button
        gameReplay.moves.remove(gameReplay.moves.size() - 1);
    }

    Vector2I mousePosBoard = new Vector2I(); //the mouse pos and buttons pressed, as defined by the board
    boolean mouseLeftPressedBoard;
    boolean mouseRightPressedBoard;

    public void startGame() { //starts the game loop. done in a thread so other stuff like event handling still happens
        Thread gameThread = new Thread(() -> {
            while (!isGameOver) { //if the game is stil going, do all this stuff
                mousePosBoard = board.mousePos; //set all the mouse stuff to what it should be. this is done in advance to avoid multithreading shenanigans
                mouseLeftPressedBoard = board.mouseLeftPressed;
                mouseRightPressedBoard = board.mouseRightPressed;
                movePieces();
                updateTimers();
                checkForLoss();
                board.draw();
                whitePiecesCaptured.draw();
                blackPiecesCaptured.draw();
            }
        });
        gameThread.run(); ///run the thread
    }

    public void movePieces() { //alright, get ready
        board.turn = board.turnNumber % 2 == 0 ? ChessColor.BLACK : ChessColor.WHITE; //set the turn to what it should be

        Vector2I boardBuffer = new Vector2I(-50, -50); //the buffer of pixels around the board where the mouse will be considered to 
        if ((!Geometry.isPointInRect(boardBuffer, Board.boardSizeI.subtract(boardBuffer), mousePosBoard))
                && board.heldPiece != null) { //if the mouse is not in the board, and a piece was being held, stop holding it
            board.heldPiece.setVisiblePos(board.heldPiece.getTruePos()); //put the piece back where it was
            if (board.turn == ChessColor.WHITE)
                board.whitePieces.add(board.heldPiece); //put the held piece back in whitepieces/blackpieces
            else
                board.blackPieces.add(board.heldPiece);
            board.heldPiece = null;
            mouseLeftPressedBoard = false; //say that the left button isnt pressed anymore
            board.piecesThatWillBeCaptured.clear(); //remove all the pieces that would have been captured
        }
        else if (mouseLeftPressedBoard && board.heldPiece == null) { //else if the left mouse button is pressed and there is not a piece being held yet
            if (board.turn == ChessColor.WHITE) { //loop through all the pieces on the board of the right color, find which one is being held, and make it the held piece
                for (Piece p : board.whitePieces) {
                    if (p.isInHitbox(mousePosBoard)) {
                        board.heldPiece = p;
                        board.whitePieces.remove(p);
                        break;
                    }
                }
            } else {
                for (Piece p : board.blackPieces) {
                    if (p.isInHitbox(mousePosBoard)) {
                        board.heldPiece = p;
                        board.blackPieces.remove(p);
                        break;
                    }
                }
            }
        }

        if (board.heldPiece != null) { //if there is a piece being held
            Vector2I closestValidPointToMouse = board.heldPiece.closestValidPoint(mousePosBoard, board.whitePieces,
                    board.blackPieces); //get the closest point to the mouse that the piece can move to
            board.heldPiece.setVisiblePos(closestValidPointToMouse); //set its visible position to there
            Rook rightRook = null; //these are for castling purposes
            Rook leftRook = null;
            if (board.heldPiece.getPieceType() == PieceType.KING && !board.heldPiece.getHasMoved()) { //if the king is being held, and it hasn't moved in the game yet
                ArrayList<Piece> sameColorPieces = board.heldPiece.getColor() == ChessColor.WHITE ? board.whitePieces
                        : board.blackPieces; //get the pieces of the same color as the held piece
                for (Piece p : sameColorPieces) { //loop through the pieces, and if there is a rook on the right side of the board, make it the rightrook
                    if (p.getPieceType() == PieceType.ROOK && p.getTruePos().x > Board.boardSizeI.x * 0.5
                            && !p.getHasMoved()) {
                        rightRook = (Rook) p;
                        break;
                    }
                }
                for (Piece p : sameColorPieces) { //ditto for leftrook
                    if (p.getPieceType() == PieceType.ROOK && p.getTruePos().x < Board.boardSizeI.x * 0.5
                            && !p.getHasMoved()) {
                        leftRook = (Rook) p;
                        break;
                    }
                }
                if (rightRook != null) //if there is a right rook, then set its visible position to its actual position. this is so it doesnt get stuck in its castling position if you abort a castle
                    rightRook.setVisiblePos(rightRook.getTruePos().copy());
                if (leftRook != null)
                    leftRook.setVisiblePos(leftRook.getTruePos().copy()); //dito for leftrook
                if (board.heldPiece.getVisiblePos().x == (int) (Board.boardSizeI.x * 6.5 / 8)) { //if the king is in its right castling position
                    rightRook.setVisiblePos(
                            new Vector2I((int) (Board.boardSizeI.x * 5.5 / 8), rightRook.getTruePos().y)); //set the rightrook to its corresponding castling position
                } else if (board.heldPiece.getVisiblePos().x == (int) (Board.boardSizeI.x * 2.5 / 8)) { //if the king is in its left castline position
                    leftRook.setVisiblePos(
                            new Vector2I((int) (Board.boardSizeI.x * 3.5 / 8), leftRook.getTruePos().y)); //set the leftrook to its corresponding castling position
                }
            }

            ArrayList<Piece> capturedPieces = board.heldPiece.oppositeColorPiecesOverlapping(
                    board.heldPiece.getVisiblePos(),
                    board.whitePieces, board.blackPieces);
            board.piecesThatWillBeCaptured = capturedPieces; //set the pieces that will be captured

            if (!mouseLeftPressedBoard) { //if the left button is released (the move is made)
                board.heldPiece.setTruePos(board.heldPiece.getVisiblePos(), true); //move the heldpiece to its proper position
                if (rightRook != null)
                    rightRook.setTruePos(rightRook.getVisiblePos(), true); //move the rooks to their proper positions if the move was a castle
                if (leftRook != null)
                    leftRook.setTruePos(leftRook.getVisiblePos(), true);
                if (board.turn == ChessColor.WHITE) {
                    for (Piece p : capturedPieces) { //remove all the pieces that were captured
                        board.blackPieces.remove(p);
                        board.blackPiecesCaptured.add(p);
                    }
                    board.whitePieces.add(board.heldPiece); //add the held piece back to the white pieces
                    whiteTimer.pause(); //pause the white timer
                    blackTimer.resume(); //resume the black timer
                } else { //ditto but for black
                    for (Piece p : capturedPieces) { 
                        board.whitePieces.remove(p);
                        board.whitePiecesCaptured.add(p);
                    }
                    board.blackPieces.add(board.heldPiece);
                    blackTimer.pause();
                    whiteTimer.resume();
                }

                if(board.heldPiece.getPieceType() == PieceType.PAWN) { //if the piece that just moved was a pawn, and it got to the pawn promotion area, show the promotion dialog
                    if(board.heldPiece.getColor() == ChessColor.WHITE) {
                        if(board.heldPiece.getTruePos().y <= Board.boardSizeI.y * 0.5 / 8 + 2) //the +2 and -2 are just to account for if the pawn is very close but not quite at the end of the board
                            showPiecePromotionDialog((Pawn)board.heldPiece);
                    }
                    else {
                        if(board.heldPiece.getTruePos().y >= Board.boardSizeI.y * 7.5 / 8 - 2)
                            showPiecePromotionDialog((Pawn)board.heldPiece);
                    }
                }

                board.heldPiece = null; //unhold the held piece
                board.piecesThatWillBeCaptured.clear(); //clear all the pieces that would have been captured
                board.turnNumber++; //bump the turn number
                addMoveToReplay(); //add the move that was just made to the replay
                backButton.setEnabled(true); //enable the back button (this is disabled at the start so you cant go back from the starting position and break stuff)
            }
        }
    }

    void showPiecePromotionDialog(Pawn p) { //shows the dialog for promoting a pawn
        Object[] options = { "Knight", "Bishop", "Rook", "Queen" };
        Object n = JOptionPane.showInputDialog(null,
                "Choose a piece to promote your pawn to", "Pawn Promotion",
                JOptionPane.PLAIN_MESSAGE, null,
                options, options[3]);
        Piece promotedPiece; //creates a new piece
        if (n.equals(options[0])) //makes it the right type
            promotedPiece = new Knight();
        else if (n.equals(options[1]))
            promotedPiece = new Bishop();
        else if (n.equals(options[2]))
            promotedPiece = new Rook();
        else
            promotedPiece = new Queen();

        promotedPiece.setColor(p.getColor()); //makes it the same place, color, and id as the old pawn
        promotedPiece.setTruePos(p.getTruePos(), true);
        promotedPiece.setID(p.getID());

        if(p.getColor() == ChessColor.WHITE) { //removes the pawn and puts the new one in its place
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
    ChessColor wonPlayer = null; //the player who won
    LossState lossState = null; //how the game ended

    void checkForLoss() {
        if (blackTimer.getTimeLeft() == 0) { //if the black player has run out of time
            isGameOver = true;
            wonPlayer = ChessColor.WHITE;
            lossState = LossState.OUTOFTIME;
        } else if (whiteTimer.getTimeLeft() == 0) { //if the white player has run out of time
            isGameOver = true;
            wonPlayer = ChessColor.BLACK;
            lossState = LossState.OUTOFTIME;
        } else {
            for (Piece p : board.whitePiecesCaptured) { //if the white king was captured
                if (p.getPieceType() == PieceType.KING) {
                    isGameOver = true;
                    wonPlayer = ChessColor.BLACK;
                    lossState = LossState.KINGCAPTURED;
                }
            }
            for (Piece p : board.blackPiecesCaptured) { //if the black king was captured
                if (p.getPieceType() == PieceType.KING) {
                    isGameOver = true;
                    wonPlayer = ChessColor.WHITE;
                    lossState = LossState.KINGCAPTURED;
                }
            }
        }

        if (isGameOver) { //if the game is over, end the game
            gameReplay.lossState = lossState; //updates the loss state of the replay
            backButton.setEnabled(false); //disables the back button, so you cant undo the game ending
            showGameOverDialog();
        }
    }

    void showGameOverDialog() {
        String wonColor = wonPlayer == ChessColor.WHITE ? "White" : "Black"; //the name of the color that won
        String lostColor = wonPlayer == ChessColor.WHITE ? "Black" : "White"; //the opposite
        String winString = lossState == LossState.KINGCAPTURED
                ? wonColor + " wins, by capturing " + lostColor + "'s king!"
                : wonColor + " wins, as " + lostColor + " has run out of time!"; //the string to be displayed on the dialog
        ImageIcon winIcon = ImageManager.resize(wonPlayer == ChessColor.WHITE ? ImageManager.wk : ImageManager.bk,
                new Vector2I(64, 64)); //the icon to be displayed on the win screen (the winning player's king)

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
                App.displayTimeControlDialog(); //if replay, then display the time control dialog for a new game
                break;
            case 1:
                App.displayMenuScreen(); //if menu, go to menu
                break;
            case 2:
                App.displayReplayScreen(gameReplay); //if replay, go to replay and pass in the game's replay
                break;
        }
    }

    void backOneMove() {
        if (board.heldPiece == null) { //just so you can't do any shenanigans with going back a move while holding a piece somehow
            Move lastMove = gameReplay.moves.get(gameReplay.moves.size() - 2).copy(); //get the move just before the move that was just made
            board.whitePieces = lastMove.whitePieces(); //set all the settings of the board to what they were at that move
            board.blackPieces = lastMove.blackPieces();
            board.whitePiecesCaptured.clear();
            board.whitePiecesCaptured.addAll(lastMove.whitePiecesCaptured());
            board.blackPiecesCaptured.clear();
            board.blackPiecesCaptured.addAll(lastMove.blackPiecesCaptured());
            whiteTimer.setTimeLeft(lastMove.whiteTimeLeft());
            blackTimer.setTimeLeft(lastMove.blackTimeLeft());
            board.turnNumber = lastMove.turnNumber();
            board.turn = lastMove.turnNumber() % 2 == 0 ? ChessColor.BLACK : ChessColor.WHITE;
            if(board.turn == ChessColor.WHITE) { //pause and start the correct timers
                whiteTimer.resume();
                blackTimer.pause();
            } else {
                blackTimer.resume();
                whiteTimer.pause();
            }

            if (gameReplay.moves.size() <= 2) //if you've gone back to the initial state, disable the back button
                backButton.setEnabled(false);

            removeMoveFromReplay();
        }
    }
}