import javax.swing.*;
import Game.*;

public class App {
    public static void main(String[] args) {
        JFrame f = new JFrame("title");
        f.setLayout(null);

        f.setSize(1024, 1024);
        f.setLayout(null);
        f.setVisible(true);
        Game r = new Game();
        r.setLocation(100, 100);
        f.add(r);
        r.startRendering();
    }
}