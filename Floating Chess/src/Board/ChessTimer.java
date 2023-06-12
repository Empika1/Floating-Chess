//A timer for timed games. Used in game and replay screens
package Board;

import java.awt.Dimension;

import javax.swing.*;
import javax.swing.text.*;

import Utils.*;
import Pieces.*;

public class ChessTimer extends JTextPane {
    ChessColor color; //the color of this timer instance, which determines which pieces it should display
    int startingTimeMs; //the time that this timer starts at

    public ChessTimer(ChessColor col, int startingTime, float fontSize, Dimension preferredSize) { //creats a timer. preferredSize is used because I want this to be the same size as the capturedPieces box, so that is passed in when this is created
        color = col; //sets all the stuff
        setStartingTime(startingTime);
        setText(StringFormatting.msToTime(startingTime));
        setFont(getFont().deriveFont(fontSize));
        setPreferredSize(preferredSize);
        setEditable(false);
        setBackground(new JTextPane().getBackground()); //an editable jtextpane has a different bg color than a non-editable one, so i set it to the color of a editable one

        StyledDocument doc = getStyledDocument(); //centers text, idk why this needs 4 lines of code...
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
    }

    public ChessColor getColor() {
        return color;
    }

    public void setStartingTime(int set) { //set the starting time, and set the current system time and old system time, and update the text
        startingTimeMs = set;
        timeLeftMs = set;
        updateText();
        currentTimeMs = System.currentTimeMillis();
        oldTimeMs = System.currentTimeMillis();
    }

    public int getStartingTime() {
        return startingTimeMs;
    }

    int timeLeftMs; //the time remaining on the timer

    public void setTimeLeft(int set) { //set the time left, and set the current system time and old system time, and update the text
        timeLeftMs = set;
        updateText();
        currentTimeMs = System.currentTimeMillis();
        oldTimeMs = System.currentTimeMillis();
    }

    public int getTimeLeft() {
        return timeLeftMs;
    }

    boolean running = false; //is the timer currently counting down

    public void resume() { //resume the timer, and set the current system time and old system time
        running = true;
        currentTimeMs = System.currentTimeMillis();
        oldTimeMs = System.currentTimeMillis();
    }

    public void pause() {
        running = false;
    }

    long currentTimeMs; //the current system time
    long oldTimeMs; //the system time one timer reset ago, saved to calculate deltatime

    int timeLeftMsOld; //the time left one timer reset ago

    static final int msToUpdate = 100; //the ms between text updates. This is set as 100 ms because the timer only has 100 ms precision

    public void updateTime() {
        if (running) {
            currentTimeMs = System.currentTimeMillis(); //update the current time
            int deltaTimeMs = (int)(currentTimeMs - oldTimeMs); //calculate the time that has passed since the last timer update
            timeLeftMs -= deltaTimeMs; //update the time left
            oldTimeMs = currentTimeMs; //update the old time left, so it is always exactly 1 update behind the current time left

            if (Math.abs(timeLeftMs - timeLeftMsOld) >= msToUpdate) { //check if mstoupdate ms has passed since the last timer update
                if (timeLeftMs < 0) //make sure timeleft can never be negative
                    timeLeftMs = 0;
                updateText(); //update the text
                timeLeftMsOld = timeLeftMs; //update timeleftmsold, so it is also exactly 1 update behind the current time left
            }
        }
    }

    void updateText() {
        setText(StringFormatting.msToTime(timeLeftMs)); //set the time left
    }
}