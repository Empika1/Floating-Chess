package App;
import javax.swing.*;

import java.awt.*;
import Game.*;
import Menu.*;

public class App {

    static JFrame f;
    public static void main(String[] args) throws Exception{
        UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf");
        JFrame.setDefaultLookAndFeelDecorated(true);

        f = new JFrame("Floating Chess");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        displayMenuScreen();
    }

    public static void displayMenuScreen() {
        f.invalidate();
        MenuScreen m = new MenuScreen();
        f.setContentPane(m);
        f.pack();
        f.setLocationRelativeTo(null);
        f.validate();
        f.setVisible(true);
    }

    public static void displayGameScreen() {
        f.invalidate();
        GameScreen g = new GameScreen();
        f.setContentPane(g);
        f.pack();
        f.setLocationRelativeTo(null);
        f.validate();
        f.setVisible(true);
        g.startGame();
    }
}