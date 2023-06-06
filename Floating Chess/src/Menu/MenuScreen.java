package Menu;

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

public class MenuScreen extends JPanel {
    public MenuScreen() {
        setupPanel();
    }

    static final ImageIcon titleLabel = ImageManager.resize(ImageManager.title, 450);
    JLayeredPane layeredPane;
    Board board;

    void setupPanel() {
        setLayout(new BorderLayout());
        layeredPane = new JLayeredPane();
        add(layeredPane);
        layeredPane.setLayout(new GridBagLayout());
        layeredPane.setBackground(UIManager.getColor("Panel.background"));

        board = new Board(false);
        GridBagConstraints boardConstraints = new GridBagConstraints();
        boardConstraints.insets = new Insets(10, 10, 10, 10);
        boardConstraints.gridheight = 4;
        boardConstraints.gridwidth = 1;
        boardConstraints.gridx = 0;
        boardConstraints.gridy = 0;
        layeredPane.add(board, boardConstraints, 1);

        JLabel title = new JLabel(titleLabel, JLabel.CENTER);
        GridBagConstraints titleConstraints = new GridBagConstraints();
        titleConstraints.insets = new Insets(110, 10, 0, 10);
        titleConstraints.gridheight = 1;
        titleConstraints.gridwidth = 1;
        titleConstraints.gridx = 0;
        titleConstraints.gridy = 0;
        layeredPane.add(title, titleConstraints, 0);

        JButton playButton = new JButton("Play");
        playButton.setPreferredSize(new Dimension(165, 53));
        playButton.setFont(playButton.getFont().deriveFont(Font.BOLD, 18f));
        playButton.setBackground(playButton.getBackground().darker());
        playButton.setFocusPainted(false);
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                App.displayTimeControlDialog();
            }
        });
        GridBagConstraints playButtonConstraints = new GridBagConstraints();
        playButtonConstraints.insets = new Insets(5, 10, 0, 10);
        playButtonConstraints.gridheight = 1;
        playButtonConstraints.gridwidth = 1;
        playButtonConstraints.gridx = 0;
        playButtonConstraints.gridy = 1;
        layeredPane.add(playButton, playButtonConstraints, 0);

        JButton replayButton = new JButton("View Replay");
        replayButton.setPreferredSize(new Dimension(185, 53));
        replayButton.setFont(playButton.getFont().deriveFont(Font.BOLD, 18f));
        replayButton.setBackground(replayButton.getBackground().darker());
        replayButton.setFocusPainted(false);
        replayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                App.displayReplayScreen(null);
            }
        });
        GridBagConstraints replayButtonConstraints = new GridBagConstraints();
        replayButtonConstraints.insets = new Insets(5, 10, 0, 10);
        replayButtonConstraints.gridheight = 1;
        replayButtonConstraints.gridwidth = 1;
        replayButtonConstraints.gridx = 0;
        replayButtonConstraints.gridy = 2;
        layeredPane.add(replayButton, replayButtonConstraints, 0);
    }
}
