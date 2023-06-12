//The main menu / title screen. This is what the game starts at
package Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Board.*;
import Images.ImageManager;
import App.*;

public class MenuScreen extends JPanel {
    public MenuScreen() {
        setupPanel();
    }

    static final ImageIcon titleLabel = ImageManager.resize(ImageManager.title, 450);
    JLayeredPane layeredPane; // layeredpane is used because i want a board behind the buttons so they have to
                              // be on diff layers
    Board board; // the board in the background

    void setupPanel() {
        setLayout(new BorderLayout()); //set the layout to a boring layout because the interesting stuff is on the layeredpane
        layeredPane = new JLayeredPane(); //make the layoutpane, add it, and set its layout
        add(layeredPane);
        layeredPane.setLayout(new GridBagLayout());
        layeredPane.setBackground(UIManager.getColor("Panel.background"));

        if (Math.random() > 0.1) //make it so the bg board either shows up with or without pieces, with no pieces as an easter egg
            board = new Board(true);
        else
            board = new Board(false);
        GridBagConstraints boardConstraints = new GridBagConstraints();
        boardConstraints.insets = new Insets(10, 10, 10, 10);
        boardConstraints.gridheight = 4;
        boardConstraints.gridwidth = 1;
        boardConstraints.gridx = 0;
        boardConstraints.gridy = 0;
        layeredPane.add(board, boardConstraints, 1); //add the board to a further back layer. everything else goes on layer 0

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
                App.displayTimeControlDialog(); //if play button is pressed, show time control dialog
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
                App.displayReplayScreen(null); //if replay button is pressed, go to the replay screen with no replay
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