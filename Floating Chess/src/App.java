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
        Game g = new Game();
        f.getContentPane().add(g, BorderLayout.PAGE_START);
        f.pack();
        f.setVisible(true);
        g.startRendering();
    }
}