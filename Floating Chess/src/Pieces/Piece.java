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

    protected double xPos;

    public double getXPos() {
        return xPos;
    }

    public void setXPos(double set) {
        xPos = set;
    }

    protected double yPos;

    public double getYPos() {
        return yPos;
    }

    public void setYPos(double set) {
        yPos = set;
    }

    public abstract boolean canMoveTo(double x, double y, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces);

    public abstract double getHitboxRadius();

    public abstract double getHurtboxRadius();

    public boolean isInHitbox(double xOther, double yOther) {
        double xDiff = xOther - xPos;
        double yDiff = yOther - yPos;
        double distanceSquared = xDiff * xDiff + yDiff * yDiff;
        return distanceSquared <= getHitboxRadius();
    }

    public boolean isInHurtbox(double xOther, double yOther) {
        double xDiff = xOther - xPos;
        double yDiff = yOther - yPos;
        double distanceSquared = xDiff * xDiff + yDiff * yDiff;
        return distanceSquared <= getHurtboxRadius();
    }

    public static boolean hitboxOverlapsHitbox(Piece a, Piece b) {
        double xDiff = a.getXPos() - b.getXPos();
        double yDiff = a.getYPos() - b.getYPos();
        double distanceSquared = xDiff * xDiff + yDiff * yDiff;
        return distanceSquared <= a.getHitboxRadius() + b.getHitboxRadius();
    }

    public static boolean hurtboxOverlapsHitbox(Piece a, Piece b) {
        double xDiff = a.getXPos() - b.getXPos();
        double yDiff = a.getYPos() - b.getYPos();
        double distanceSquared = xDiff * xDiff + yDiff * yDiff;
        return distanceSquared <= a.getHurtboxRadius() + b.getHitboxRadius();
    }

    public static boolean hurtboxOverlapsHurtbox(Piece a, Piece b) {
        double xDiff = a.getXPos() - b.getXPos();
        double yDiff = a.getYPos() - b.getYPos();
        double distanceSquared = xDiff * xDiff + yDiff * yDiff;
        return distanceSquared <= a.getHurtboxRadius() + b.getHurtboxRadius();
    }

    public boolean isOverlappingEdge() {
        return getXPos() - getHitboxRadius() < 0 || getXPos() - getHurtboxRadius() < 0
                || getYPos() - getHitboxRadius() < 0
                || getYPos() - getHurtboxRadius() < 0 || getXPos() + getHitboxRadius() > 8
                || getXPos() + getHurtboxRadius() > 8 || getYPos() + getHitboxRadius() > 8
                || getYPos() + getHurtboxRadius() > 8;
    }

    public boolean isOverlappingSameColorPiece(ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        if (color == ChessColor.WHITE) {
            for (Piece p : whitePieces)
                if (hitboxOverlapsHitbox(this, p) || hurtboxOverlapsHitbox(this, p) || hurtboxOverlapsHurtbox(this, p))
                    return true;
        } else {
            for (Piece p : blackPieces)
                if (hitboxOverlapsHitbox(this, p) || hurtboxOverlapsHitbox(this, p) || hurtboxOverlapsHurtbox(this, p))
                    return true;
        }
        return false;
    }

    public abstract int getMaterialValue();

    public abstract ImageIcon getImageIcon();

    public static final int pieceSizeX = Game.boardSizeX / 8;
    public static final int pieceSizeY = Game.boardSizeY / 8;

    public void draw(Graphics g, Game game) {
        int xPosPanel = game.boardXToPanelX(getXPos());
        int yPosPanel = game.boardYToPanelY(getYPos());
        getImageIcon().paintIcon(game, g, xPosPanel - (pieceSizeX / 2), yPosPanel - (pieceSizeY / 2));
    }
}