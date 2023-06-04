package Board;

import java.awt.Dimension;

import javax.swing.*;
import javax.swing.text.*;

import Utils.*;
import Pieces.*;

public class ChessTimer extends JTextPane {

    ChessColor color;
    int startingTimeMs;

    public ChessTimer(ChessColor col, int startingTime, float fontSize, Dimension preferredSize) {
        color = col;
        setStartingTime(startingTime);
        setText(StringFormatting.msToTime(startingTime));
        setFont(getFont().deriveFont(fontSize));
        setPreferredSize(preferredSize);

        StyledDocument doc = getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
    }

    public ChessColor getColor() {
        return color;
    }

    public void setStartingTime(int set) {
        startingTimeMs = set;
        timeLeftMs = set;
        updateText();
        currentTimeMs = System.currentTimeMillis();
        oldTimeMs = System.currentTimeMillis();
    }

    public int getStartingTime() {
        return startingTimeMs;
    }

    int timeLeftMs;

    public void setTimeLeft(int set) {
        timeLeftMs = set;
        updateText();
        currentTimeMs = System.currentTimeMillis();
        oldTimeMs = System.currentTimeMillis();
    }

    public int getTimeLeft() {
        return timeLeftMs;
    }

    boolean running = false;

    public void resume() {
        running = true;
        currentTimeMs = System.currentTimeMillis();
        oldTimeMs = System.currentTimeMillis();
    }

    public void pause() {
        running = false;
    }

    long currentTimeMs;
    long oldTimeMs;

    int timeLeftMsOld;

    static final int msToUpdate = 100;

    public void updateTime() {
        if (running) {
            currentTimeMs = System.currentTimeMillis();
            int deltaTimeMs = (int)(currentTimeMs - oldTimeMs);
            timeLeftMs -= deltaTimeMs;
            oldTimeMs = currentTimeMs;

            if (Math.abs(timeLeftMs - timeLeftMsOld) >= msToUpdate) {
                if (timeLeftMs < 0)
                    timeLeftMs = 0;
                updateText();
                timeLeftMsOld = timeLeftMs;
            }
        }
    }

    void updateText() {
        setText(StringFormatting.msToTime(timeLeftMs));
    }
}
