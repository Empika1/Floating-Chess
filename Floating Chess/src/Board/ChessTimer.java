package Board;

import java.awt.Dimension;

import javax.swing.*;
import Utils.*;
import Pieces.*;

public class ChessTimer extends JTextPane {

    ChessColor color;
    long startingTimeMs;
    public ChessTimer(ChessColor col, long startingTime, float fontSize, Dimension preferredSize) {
        color = col;
        setStartingTime(startingTime);
        setText(StringFormatting.msToTime(startingTime));
        setFont(getFont().deriveFont(fontSize));
        setPreferredSize(preferredSize);
    }

    public ChessColor getColor() {
        return color;
    }

    public void setStartingTime(long set) {
        startingTimeMs = set;
        timeLeftMs = set;
        updateText();
    }

    long timeLeftMs;

    public void setTimeLeft(long set) {
        timeLeftMs = set;
        updateText();
    }

    public long getTimeLeft() {
        return timeLeftMs;
    }

    boolean running = false;

    public void start() {
        running = true;
    }

    public void pause() {
        running = false;
    }

    long currentTimeMs = System.currentTimeMillis();
    long oldTimeMs = System.currentTimeMillis();

    long timeLeftMsOld;

    static final int msToUpdate = 100;
    public void updateTime() {
        if (running) {
            System.out.println(timeLeftMs);
            currentTimeMs = System.currentTimeMillis();
            long deltaTimeMs = currentTimeMs - oldTimeMs;
            timeLeftMs -= deltaTimeMs;
            oldTimeMs = currentTimeMs;

            if(Math.abs(timeLeftMs - timeLeftMsOld) >= msToUpdate) {
                updateText();
                timeLeftMsOld = timeLeftMs;
            }
        }
    }

    void updateText() {
        setText(StringFormatting.msToTime(timeLeftMs));
    }
}
