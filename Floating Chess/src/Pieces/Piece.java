package Pieces;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import Game.*;
import Utils.*;

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

    protected Vector2 truePos;

    public Vector2 getTruePos() {
        return truePos;
    }

    public void setTruePos(Vector2 set) {
        truePos = set;
        visiblePos = set;
    }

    protected Vector2 visiblePos;

    public Vector2 getVisiblePos() {
        return visiblePos;
    }

    public void setVisiblePos(Vector2 set) {
        visiblePos = set;
    }

    public abstract boolean canMoveTo(Vector2 pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces);

    public abstract Vector2 closestValidPoint(Vector2 pos, ArrayList<Piece> whitePieces,
            ArrayList<Piece> blackPieces);

    public abstract double getHitboxRadius();

    public boolean isInHitbox(Vector2 pos) {
        Vector2 diff = pos.subtract(getTruePos());
        double distanceSquared = diff.x * diff.x + diff.y * diff.y;
        return distanceSquared <= getHitboxRadius() * getHitboxRadius();
    }

    public boolean hitboxOverlapsHitbox(Vector2 thisPos, Piece b) {
        Vector2 diff = thisPos.subtract(b.getTruePos());
        double distanceSquared = diff.x * diff.x + diff.y * diff.y;
        return distanceSquared <= (getHitboxRadius() + b.getHitboxRadius()) * (getHitboxRadius() + b.getHitboxRadius());
    }

    public boolean isOverlappingEdge(Vector2 thisPos) {
        return thisPos.x - getHitboxRadius() < 0 || thisPos.y - getHitboxRadius() < 0
                || thisPos.x + getHitboxRadius() > 8 || thisPos.y + getHitboxRadius() > 8;
    }

    public boolean isOverlappingSameColorPiece(Vector2 thisPos, ArrayList<Piece> whitePieces,
            ArrayList<Piece> blackPieces) {
        if (color == ChessColor.WHITE) {
            for (Piece p : whitePieces) {
                if (equals(p))
                    continue;
                if (hitboxOverlapsHitbox(thisPos, p))
                    return true;
            }

        } else {
            for (Piece p : blackPieces) {
                if (equals(p))
                    continue;
                if (hitboxOverlapsHitbox(thisPos, p))
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
        Vector2I posPanel = game.boardPosToPanelPos(getVisiblePos());
        getImageIcon().paintIcon(game, g, posPanel.x - (pieceSizeX / 2), posPanel.y - (pieceSizeY / 2));
    }
}