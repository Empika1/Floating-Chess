package App;
import javax.swing.*;

import java.awt.*;
import Game.*;
import Menu.*;

public class App {

    static JFrame f;
    static JPanel cards;
    public static void main(String[] args) throws Exception{
        UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf");
        JFrame.setDefaultLookAndFeelDecorated(true);

        f = new JFrame("Floating Chess");
        cards = new JPanel(new CardLayout());
        f.setContentPane(cards);
        cards.add(new MenuScreen(), "MenuScreen");
        cards.add(new GameScreen(), "GameScreen");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        displayMenuScreen();
    }

    public static void displayMenuScreen() {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, "MenuScreen");
    }

    public static void displayGameScreen() {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, "GameScreen");
    }
}