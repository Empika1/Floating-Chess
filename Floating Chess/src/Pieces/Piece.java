package Pieces;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import Game.*;

public abstract class Piece {
    protected int id;

    public int getID() {
        return id;
    }

    public void setID(int set) {
        id = set;
    }

    public abstract String getPieceName();

    protected ChessColor color;

    public ChessColor getColor() {
        return color;
    }

    public void setColor(ChessColor set) {
        color = set;
    }

    protected double trueX;

    public double getTrueX() {
        return trueX;
    }

    public void setTrueX(double set) {
        trueX = set;
        visibleX = set;
    }

    protected double trueY;

    public double getTrueY() {
        return trueY;
    }

    public void setTrueY(double set) {
        trueY = set;
        visibleY = set;
    }

    protected double visibleX;

    public double getVisibleX() {
        return visibleX;
    }

    public void setVisibleX(double set) {
        visibleX = set;
    }

    protected double visibleY;

    public double getVisibleY() {
        return visibleY;
    }

    public void setVisibleY(double set) {
        visibleY = set;
    }

    public abstract boolean canMoveTo(double x, double y, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces);

    public abstract double getHitboxRadius();

    public abstract double getHurtboxRadius();

    public boolean isInHitbox(double xOther, double yOther) {
        double xDiff = xOther - trueX;
        double yDiff = yOther - trueY;
        double distanceSquared = xDiff * xDiff + yDiff * yDiff;
        return distanceSquared <= getHitboxRadius();
    }

    public boolean isInHurtbox(double xOther, double yOther) {
        double xDiff = xOther - trueX;
        double yDiff = yOther - trueY;
        double distanceSquared = xDiff * xDiff + yDiff * yDiff;
        return distanceSquared <= getHurtboxRadius();
    }

    public static boolean hitboxOverlapsHitbox(Piece a, double aX, double aY, Piece b) {
        double xDiff = aX - b.getTrueX();
        double yDiff = aY - b.getTrueY();
        double distanceSquared = xDiff * xDiff + yDiff * yDiff;
        return distanceSquared <= a.getHitboxRadius() + b.getHitboxRadius();
    }

    public static boolean hurtboxOverlapsHitbox(Piece a, double aX, double aY, Piece b) {
        double xDiff = aX - b.getTrueX();
        double yDiff = aY - b.getTrueY();
        double distanceSquared = xDiff * xDiff + yDiff * yDiff;
        return distanceSquared <= a.getHurtboxRadius() + b.getHitboxRadius();
    }

    public static boolean hurtboxOverlapsHurtbox(Piece a, double aX, double aY, Piece b) {
        double xDiff = aX - b.getTrueX();
        double yDiff = aY - b.getTrueY();
        double distanceSquared = xDiff * xDiff + yDiff * yDiff;
        return distanceSquared <= a.getHurtboxRadius() + b.getHurtboxRadius();
    }

    public boolean isOverlappingEdge(double x, double y) {
        return x - getHitboxRadius() < 0 || x - getHurtboxRadius() < 0
                || y - getHitboxRadius() < 0
                || y - getHurtboxRadius() < 0 || x + getHitboxRadius() > 8
                || x + getHurtboxRadius() > 8 || y + getHitboxRadius() > 8
                || y + getHurtboxRadius() > 8;
    }

    public boolean isOverlappingSameColorPiece(double x, double y, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        if (color == ChessColor.WHITE) {
            for (Piece p : whitePieces) {
                if(equals(p))
                    continue;
                if (hitboxOverlapsHitbox(this, x, y, p))
                    return true;
            }
                
        } else {
            for (Piece p : blackPieces) {
                if(equals(p))
                    continue;
                if (hitboxOverlapsHitbox(this, x, y, p))
                    return true;
            }
        }
        return false;
    }

    public abstract int getMaterialValue();

    public abstract ImageIcon getImageIcon();

    public static final int pieceSizeX = Game.boardSizeX / 8;
    public static final int pieceSizeY = Game.boardSizeY / 8;

    public void draw(Graphics g, Game game) {
        int xPosPanel = game.boardXToPanelX(getVisibleX());
        int yPosPanel = game.boardYToPanelY(getVisibleY());
        getImageIcon().paintIcon(game, g, xPosPanel - (pieceSizeX / 2), yPosPanel - (pieceSizeY / 2));
    }
}