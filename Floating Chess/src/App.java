import javax.swing.*;

import java.awt.*;
import Game.*;

public class App {
    public static void main(String[] args) throws Exception{
        UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf");
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame f = new JFrame("title");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        GameScreen g = new GameScreen();
        f.getContentPane().add(g, BorderLayout.PAGE_START);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        g.startGame();
    }
}