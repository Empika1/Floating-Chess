package App;

import javax.swing.*;

import java.awt.*;
import Game.*;
import Menu.*;
import Replay.*;

public class App {

    static JFrame f;
    static JPanel cards;

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf");
        JFrame.setDefaultLookAndFeelDecorated(true);

        f = new JFrame("Floating Chess");
        displayMenuScreen();
    }

    public static void displayMenuScreen() {
        f.setContentPane(new MenuScreen());
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void displayTimeControlDialog() {
        Object[] options = { "1 minute", "3 minute", "5 minute", "10 minute", "20 minute", "30 minute", "1 hour",
                "1 day" };
        Object n = JOptionPane.showInputDialog(null,
                "Choose a time control", "Time Control",
                JOptionPane.PLAIN_MESSAGE, null,
                options, options[3]);
        if (n.equals(options[0]))
            displayGameScreen(60000);
        else if (n.equals(options[1]))
            displayGameScreen(180000);
        else if (n.equals(options[2]))
            displayGameScreen(300000);
        else if (n.equals(options[3]))
            displayGameScreen(600000);
        else if (n.equals(options[4]))
            displayGameScreen(1200000);
        else if (n.equals(options[5]))
            displayGameScreen(1800000);
        else if (n.equals(options[6]))
            displayGameScreen(3600000);
        else
            displayGameScreen(86400000);
    }

    public static void displayGameScreen(int time) {
        GameScreen g = new GameScreen(time);
        f.setContentPane(g);
        Thread t = new Thread(() -> {
            g.startGame();
        });
        t.start();
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void displayReplayScreen(Replay r) {
        f.setContentPane(new ReplayScreen(r));
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}