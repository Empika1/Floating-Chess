import javax.swing.*;
import Game.*;
//import Images.*;

public class App {
    public static void main(String[] args) {
        JFrame f = new JFrame("title");
        f.setSize(1024, 1024);
        f.setLayout(null);
        f.setVisible(true);
        Game r = new Game();
        f.add(r);
        r.setLocation(100, 100);
        r.startRendering();

        //System.out.println(new ImageManager().test);
    }
}