import javax.swing.*;

import Images.ImageManager;

import java.awt.*;

public class App extends JFrame {
    public static void main(String[] args) {
        new App();
    }

    public App() {
        setSize(1000, 1000);
        setVisible(true);
    }

    public void paint(Graphics g) {
        ImageManager.resize(ImageManager.board, 256, 256).paintIcon(this, g, 0, 0);
    }
}