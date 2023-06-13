//The screen where replays are watched
package Replay;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import Pieces.*;
import Utils.*;
import Board.*;
import Images.ImageManager;
import App.*;

public class ReplayScreen extends JPanel {
    Replay r; // The replay to watch

    JFileChooser fc; // The filechooser to use for saving and loading replay files

    Board board; // the board used for this replayscreen

    CapturedPieces whitePiecesCaptured; // the white capturedpieces instance
    JButton menuButton; // the button to go back to the menu
    ChessTimer blackTimer; // the timer for the black player

    CapturedPieces blackPiecesCaptured; // the black capturedpieces instance
    JButton backButton; // the button to go back one move
    JButton forwardButton; // the button to go forward one move
    ChessTimer whiteTimer; // the timer for the white player

    JButton saveButton; // the button to save the current replay to a file
    JButton loadButton; // the button to load a replay from a file

    public ReplayScreen(Replay replay) {
        r = replay; // set the replay to what it should be

        fc = new JFileChooser();
        FileFilter fcFilter = new FileFilter() {
            public boolean accept(File f) {
                return f.getName().endsWith(".fcreplay"); // you can only select .fcreplay files
            }

            public String getDescription() {
                return "Floating Chess Replay files"; // says that a /fcreplay file is a Floating Chess Replay File
            }
        };
        fc.setFileFilter(fcFilter); // sets the file filter

        setLayout(new GridBagLayout()); // set the panel layout to a gridbaglayout
        setBackground(UIManager.getColor("Panel.background")); // set the background color to what it should be

        board = new Board(false); // create a new board, but don't put any pieces on it
        GridBagConstraints boardConstraints = new GridBagConstraints(); // put it where it should be in the container
        boardConstraints.insets = new Insets(10, 10, 0, 10);
        boardConstraints.gridheight = 1;
        boardConstraints.gridwidth = 3;
        boardConstraints.gridx = 0;
        boardConstraints.gridy = 1;
        add(board, boardConstraints);

        whitePiecesCaptured = new CapturedPieces(ChessColor.WHITE, board); // ok the rest of this is literally just adding more stuff to the panel, you get the idea
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
                App.displayMenuScreen(); // go back to the menu if the button is pressed
            }
        });
        GridBagConstraints menuButtonConstraints = new GridBagConstraints();
        menuButtonConstraints.insets = new Insets(10, 10, 0, 10);
        menuButtonConstraints.gridheight = 1;
        menuButtonConstraints.gridwidth = 1;
        menuButtonConstraints.gridx = 1;
        menuButtonConstraints.gridy = 0;
        add(menuButton, menuButtonConstraints);

        blackTimer = new ChessTimer(ChessColor.BLACK, 600000, 14f, whitePiecesCaptured.getPreferredSize());
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
        blackPiecesCapturedConstraints.insets = new Insets(10, 10, 0, 10);
        blackPiecesCapturedConstraints.gridheight = 1;
        blackPiecesCapturedConstraints.gridwidth = 1;
        blackPiecesCapturedConstraints.gridx = 0;
        blackPiecesCapturedConstraints.gridy = 2;
        add(blackPiecesCaptured, blackPiecesCapturedConstraints);

        JPanel backForwardButtonContainer = new JPanel(new GridLayout(1, 2)); // the container that holds the back and forward buttons because they dont fit nicely in the gridbaglayout
        GridBagConstraints backForwardButtonConstraints = new GridBagConstraints();
        backForwardButtonConstraints.insets = new Insets(10, 10, 0, 10);
        backForwardButtonConstraints.gridheight = 1;
        backForwardButtonConstraints.gridwidth = 1;
        backForwardButtonConstraints.gridx = 1;
        backForwardButtonConstraints.gridy = 2;
        add(backForwardButtonContainer, backForwardButtonConstraints);

        backButton = new JButton(ImageManager.resize(ImageManager.backButton, new Vector2I(12, 12)));
        backButton.setFocusPainted(false);
        if (r == null)
            backButton.setEnabled(false);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                backOneMove(); // go back one move if the button is pressed
            }
        });
        backForwardButtonContainer.add(backButton);

        forwardButton = new JButton(ImageManager.resize(ImageManager.forwardButton, new Vector2I(12, 12)));
        forwardButton.setFocusPainted(false);
        if (r == null)
            forwardButton.setEnabled(false);
        forwardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                forwardOneMove(); // go forward one move if the button is pressed
            }
        });
        backForwardButtonContainer.add(forwardButton);

        whiteTimer = new ChessTimer(ChessColor.WHITE, 600000, 14f, blackPiecesCaptured.getPreferredSize());
        whiteTimer.setPreferredSize(blackPiecesCaptured.getPreferredSize());
        GridBagConstraints whiteTimerConstraints = new GridBagConstraints();
        whiteTimerConstraints.insets = new Insets(10, 10, 0, 10);
        whiteTimerConstraints.gridheight = 1;
        whiteTimerConstraints.gridwidth = 1;
        whiteTimerConstraints.gridx = 2;
        whiteTimerConstraints.gridy = 2;
        add(whiteTimer, whiteTimerConstraints);

        JPanel saveLoadButtonContainer = new JPanel(new GridLayout(1, 2)); // the container that holds the save and load buttons, for the same reason as the back and forward ones
        GridBagConstraints saveLoadButtonConstraints = new GridBagConstraints();
        saveLoadButtonConstraints.insets = new Insets(10, 10, 10, 10);
        saveLoadButtonConstraints.gridheight = 1;
        saveLoadButtonConstraints.gridwidth = 3;
        saveLoadButtonConstraints.gridx = 0;
        saveLoadButtonConstraints.gridy = 3;
        add(saveLoadButtonContainer, saveLoadButtonConstraints);

        saveButton = new JButton("Save Replay");
        saveButton.setFocusPainted(false);
        if (r == null)
            saveButton.setEnabled(false);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveReplay(); // save the replay to a file
            }
        });
        saveLoadButtonContainer.add(saveButton);

        loadButton = new JButton("Load Replay");
        loadButton.setFocusPainted(false);
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openReplay(); // load a replay from a file
            }
        });
        saveLoadButtonContainer.add(loadButton);

        setVisible(true);
        if (r != null)
            setBoardState(); // if there is a replay
    }

    int currentMoveNum = 0; // the current move

    void setBoardState() {
        Move currentMove = r.moves.get(currentMoveNum); // get the current move
        board.whitePieces = currentMove.whitePieces(); // update all stats of the board
        board.blackPieces = currentMove.blackPieces();
        board.whitePiecesCaptured.clear();
        board.whitePiecesCaptured.addAll(currentMove.whitePiecesCaptured());
        board.blackPiecesCaptured.clear();
        board.blackPiecesCaptured.addAll(currentMove.blackPiecesCaptured());
        whiteTimer.setTimeLeft(currentMove.whiteTimeLeft());
        blackTimer.setTimeLeft(currentMove.blackTimeLeft());
        board.draw();
        whitePiecesCaptured.draw();
        blackPiecesCaptured.draw();
    }

    void backOneMove() { // goes back one move
        currentMoveNum--; // reduce the current move
        setBoardState(); // update board state
        forwardButton.setEnabled(true); // it is always possible to go forward one move if you have just gone back one move
        if (currentMoveNum == 0)
            backButton.setEnabled(false); // if there are no more moves to go back, disable back button
    }

    void forwardOneMove() { // goes forward one move, exact same as backonemove but in reverse
        currentMoveNum++;
        setBoardState();
        backButton.setEnabled(true);
        if (currentMoveNum == r.moves.size() - 1)
            forwardButton.setEnabled(false);
    }

    void openReplay() { // opens a replay from a file
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { // shows open dialog, and if successful
            try {
                Replay loadedReplay = Replay.readFromFile(fc.getSelectedFile()); // tries to read the file to a replay
                App.displayReplayScreen(loadedReplay); // tries to display a new replay screen with that file
            } catch (IOException | ClassNotFoundException e) { // if your replay file is invalid, do nothing (great
                                                               // error handling i know)
            }
        }
    }

    void saveReplay() { // saves a replay to a file
        fc.setSelectedFile(new File("Replay.fcreplay")); // sets the default file name as "Replay.fcreplay"
        if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) { // shows save dialog, and if successful
            try { // tries to save the file to a replay
                Replay.saveToFile(r, fc.getSelectedFile());
            } catch (IOException e) { // if you manage to save it wrong somehow, do nothing
            }
        }
    }
}