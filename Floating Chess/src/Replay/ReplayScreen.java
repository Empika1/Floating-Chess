package Replay;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import Pieces.*;
import Replay.*;
import Utils.*;
import Board.*;
import Images.ImageManager;
import App.*;

public class ReplayScreen extends JPanel {
    Replay r;

    JFileChooser fc;

    Board board;

    CapturedPieces whitePiecesCaptured;
    JButton menuButton;
    ChessTimer blackTimer;

    CapturedPieces blackPiecesCaptured;
    JButton backButton;
    JButton forwardButton;
    ChessTimer whiteTimer;

    JButton saveButton;
    JButton loadButton;

    public ReplayScreen(Replay replay) {
        r = replay;

        fc = new JFileChooser();
        FileFilter fcFilter = new FileFilter() {
            public boolean accept(File f) {
                return f.getName().endsWith(".fcreplay");
            }
            public String getDescription() {
                return "Floating Chess Replay files";
            }
        };
        fc.setFileFilter(fcFilter);

        setLayout(new GridBagLayout());
        setBackground(UIManager.getColor("Panel.background"));

        board = new Board(false);
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

        JPanel backForwardButtonContainer = new JPanel(new GridLayout(1, 2));
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
                backOneMove();
            }
        });
        backForwardButtonContainer.add(backButton);

        forwardButton = new JButton(ImageManager.resize(ImageManager.forwardButton, new Vector2I(12, 12)));
        forwardButton.setFocusPainted(false);
        if (r == null)
            forwardButton.setEnabled(false);
        forwardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                forwardOneMove();
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

        JPanel saveLoadButtonContainer = new JPanel(new GridLayout(1, 2));
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
                saveReplay();
            }
        });
        saveLoadButtonContainer.add(saveButton);

        loadButton = new JButton("Load Replay");
        loadButton.setFocusPainted(false);
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openReplay();
            }
        });
        saveLoadButtonContainer.add(loadButton);

        setVisible(true);
        if(r != null)
            setBoardState();
    }

    int currentMoveNum = 0;
    void setBoardState() {
        Move currentMove = r.moves.get(currentMoveNum);
        board.whitePieces = currentMove.whitePieces();
        board.blackPieces = currentMove.blackPieces();
        board.whitePiecesCaptured.clear();
        board.whitePiecesCaptured.addAll(currentMove.whitePiecesCaptured());
        board.blackPiecesCaptured.clear();
        board.blackPiecesCaptured.addAll(currentMove.blackPiecesCaptured());
        whiteTimer.setTimeLeft(currentMove.whiteTimeLeft());
        blackTimer.setTimeLeft(currentMove.blackTimeLeft());
        board.draw();
        whitePiecesCaptured.draw(board);
        blackPiecesCaptured.draw(board);
    }

    void backOneMove() {
        currentMoveNum--;
        setBoardState();
        forwardButton.setEnabled(true);
        if(currentMoveNum == 0)
            backButton.setEnabled(false);
    }

    void forwardOneMove() {
        currentMoveNum++;
        setBoardState();
        backButton.setEnabled(true);
        if(currentMoveNum == r.moves.size() - 1)
            forwardButton.setEnabled(false);
    }

    void openReplay() {
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                Replay loadedReplay = Replay.readFromFile(fc.getSelectedFile());
                App.displayReplayScreen(loadedReplay);
            } catch (IOException | ClassNotFoundException e) {
            }
        }
    }

    void saveReplay() {
        fc.setSelectedFile(new File("Replay.fcreplay"));
        if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                Replay.saveToFile(r, fc.getSelectedFile());
            } catch (IOException e) {
            }
        }
    }
}