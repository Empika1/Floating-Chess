package App;
import javax.swing.*;

import java.awt.*;
import Game.*;
import Menu.*;
import Replay.*;

public class App {

    static JFrame f;
    static JPanel cards;
    public static void main(String[] args) throws Exception{
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

    public static void displayGameScreen() {
        GameScreen g = new GameScreen();
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