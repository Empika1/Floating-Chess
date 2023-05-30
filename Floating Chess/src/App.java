import javax.swing.*;
import Game.*;

public class App {
    public static void main(String[] args) {
        JFrame f = new JFrame("title");
        f.setSize(600, 600);
        f.setLayout(null);
        f.setVisible(true);
        Game r = new Game();
        f.add(r);
        r.setLocation(0, 0);
        r.startRendering();
    }
}