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

    protected Vector2I truePos;

    public Vector2I getTruePos() {
        return truePos;
    }

    public void setTruePos(Vector2I set) {
        truePos = set;
        visiblePos = set;
    }

    protected Vector2I visiblePos;

    public Vector2I getVisiblePos() {
        return visiblePos;
    }

    public void setVisiblePos(Vector2I set) {
        visiblePos = set;
    }

    public abstract boolean canMoveTo(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces);

    public Vector2I closestValidPoint(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        if (pos == getTruePos() || canMoveTo(pos, whitePieces, blackPieces))
            return pos;

        Vector2I currentSearchingPos = pos.copy();
        int searchSquareMaxWidthOrHeight;
        Vector2I searchSquareCenter = new Vector2I();
        Vector2I searchSquareBottomRight = new Vector2I();
        Vector2I searchSquareTopLeft = new Vector2I();
        Vector2I closestSoFar = new Vector2I();
        double closestSoFarLength = Double.MAX_VALUE;
        searchForCloser: while (true) {
            searchSquareMaxWidthOrHeight = Math.max(Math.abs(getTruePos().x - pos.x), Math.abs(getTruePos().y - pos.y));
            searchSquareCenter.x = Math.min(currentSearchingPos.x, getTruePos().x);
            searchSquareCenter.y = Math.min(currentSearchingPos.y, getTruePos().y);
            searchSquareBottomRight.x = searchSquareCenter.x + searchSquareMaxWidthOrHeight;
            searchSquareBottomRight.x = searchSquareCenter.x + searchSquareMaxWidthOrHeight;
            searchSquareBottomRight.y = searchSquareCenter.y + searchSquareMaxWidthOrHeight;
            searchSquareTopLeft = searchSquareCenter.subtract(searchSquareBottomRight);
            if (searchSquareTopLeft.x < 0)
                searchSquareTopLeft.x = 0;
            if (searchSquareTopLeft.y < 0)
                searchSquareTopLeft.y = 0;
            if (searchSquareBottomRight.x >= Game.boardSizeI.x)
                searchSquareBottomRight.x = Game.boardSizeI.x;
            if (searchSquareBottomRight.y >= Game.boardSizeI.y)
                searchSquareBottomRight.y = Game.boardSizeI.y;

            for (currentSearchingPos.x = searchSquareTopLeft.x; currentSearchingPos.x < searchSquareBottomRight.x; currentSearchingPos.x++) {
                for (currentSearchingPos.y = searchSquareTopLeft.y; currentSearchingPos.y < searchSquareBottomRight.y; currentSearchingPos.y++) {
                    System.out.println(currentSearchingPos.x + " " + currentSearchingPos.y);
                    if (canMoveTo(currentSearchingPos, whitePieces, blackPieces)) {
                        double length = currentSearchingPos.subtract(pos).getLength();
                        if (length < closestSoFarLength) {
                            closestSoFar = currentSearchingPos;
                            closestSoFarLength = length;
                            continue searchForCloser;
                        }
                    }
                }
            }
            return closestSoFar;
        }
    }

    public abstract int getHitboxRadius();

    public boolean isInHitbox(Vector2I pos) {
        Vector2I diff = pos.subtract(getTruePos());
        double distanceSquared = diff.x * diff.x + diff.y * diff.y;
        return distanceSquared <= getHitboxRadius() * getHitboxRadius();
    }

    public boolean hitboxOverlapsHitbox(Vector2I thisPos, Piece b) {
        Vector2I diff = thisPos.subtract(b.getTruePos());
        double distanceSquared = diff.x * diff.x + diff.y * diff.y;
        return distanceSquared <= (getHitboxRadius() + b.getHitboxRadius()) * (getHitboxRadius() + b.getHitboxRadius());
    }

    public boolean isOverlappingEdge(Vector2I thisPos) {
        return thisPos.x - getHitboxRadius() < 0 || thisPos.y - getHitboxRadius() < 0
                || thisPos.x + getHitboxRadius() > Game.boardSizePixels.x || thisPos.y + Game.boardSizePixels.y > 8;
    }

    public boolean isOverlappingSameColorPiece(Vector2I thisPos, ArrayList<Piece> whitePieces,
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

    public static final Vector2I pieceSizePixels = new Vector2I(Game.boardSizePixels.x / 8, Game.boardSizePixels.y / 8);

    public void draw(Graphics g, Game game) {
        Vector2I posPanel = game.boardPosToPanelPos(getVisiblePos());
        getImageIcon().paintIcon(game, g, posPanel.x - (pieceSizePixels.x / 2), posPanel.y - (pieceSizePixels.y / 2));
    }
}