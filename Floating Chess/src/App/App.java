//The Main class of my game. This contains the JFrame and is where the switching between screens is handled
package App;

import javax.swing.*;

import Game.*;
import Menu.*;
import Replay.*;

public class App {

    static JFrame f; //the main jframe

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf"); //sets look and feel to my cool look and feel
        JFrame.setDefaultLookAndFeelDecorated(true);

        f = new JFrame("Floating Chess"); //create jframe and start on the menu screen
        displayMenuScreen(); 
    }

    public static void displayMenuScreen() { //displays menu screen and changes title
        f.setTitle("Floating Chess - Menu");
        f.setContentPane(new MenuScreen());
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void displayTimeControlDialog() { //displays the InputDialog to choose a time control when starting a game
        Object[] options = { "1 minute", "3 minute", "5 minute", "10 minute", "20 minute", "30 minute", "1 hour",
                "1 day" };
        Object n = JOptionPane.showInputDialog(null,
                "Choose a time control", "Time Control",
                JOptionPane.PLAIN_MESSAGE, null,
                options, options[3]);
        if (n.equals(options[0]))
            displayGameScreen(60000); //all of these options are in milliseconds
        else if (n.equals(options[1]))
            displayGameScreen(180000);
        else if (n.equals(options[2]))
            displayGameScreen(300000);
        else if (n.equals(options[3]))
            displayGameScreen(600000);
        else if (n.equals(options[4]))
            displayGameScreen(1200000);
        else if (n.equals(options[5]))
            displayGameScreen(1800000);
        else if (n.equals(options[6]))
            displayGameScreen(3600000);
        else
            displayGameScreen(86400000);
    }

    public static void displayGameScreen(int time) { //displays game screen with the chosen time control and changes title
        f.setTitle("Floating Chess - Game");
        GameScreen g = new GameScreen(time);
        f.setContentPane(g);
        Thread t = new Thread(() -> {
            g.startGame(); //startGame needs to be called in a thread or it freezes, not entirely sure why
        });
        t.start();
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void displayReplayScreen(Replay r) { //displays replay screen and changes title
        f.setTitle("Floating Chess - Replay");
        f.setContentPane(new ReplayScreen(r));
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}