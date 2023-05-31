import javax.swing.*;
import java.awt.*;
import Game.*;

public class App {
    public static void main(String[] args) {
        /*JFrame f = new JFrame("title");
        f.setSize(600, 600);
        f.setLayout(null);
        f.setVisible(true);
        Game r = new Game();
        f.add(r);
        r.setLocation(0, 0);
        r.startRendering();*/

        JFrame f = new JFrame("title");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel p = new JPanel(new BorderLayout());
        f.getContentPane().add(p, BorderLayout.PAGE_START);
        Game g = new Game();
        g.setPreferredSize(new Dimension(512, 512));
        p.add(g, BorderLayout.PAGE_START);
        f.pack();
        f.setVisible(true);
        g.startRendering();
    }
}