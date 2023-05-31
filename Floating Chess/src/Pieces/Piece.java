package Pieces;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import Game.*;
import Utils.*;
import Board.*;

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

    public void setTruePos(Vector2I set, boolean isInGame) {
        truePos = set;
        visiblePos = set;
        if (isInGame)
            setHasMoved(true);
    }

    protected boolean hasMoved = false;

    public boolean getHasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean set) {
        hasMoved = set;
    }

    protected Vector2I visiblePos;

    public Vector2I getVisiblePos() {
        return visiblePos;
    }

    public void setVisiblePos(Vector2I set) {
        visiblePos = set;
    }

    public abstract boolean canMoveTo(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces);

    public abstract Vector2I closestValidPoint(Vector2I pos, ArrayList<Piece> whitePieces,
            ArrayList<Piece> blackPieces);

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
                || thisPos.x + getHitboxRadius() > Game.boardSizeI.x
                || thisPos.y + getHitboxRadius() > Game.boardSizeI.y;
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

    public boolean isOverlappingOppositeColorPiece(Vector2I thisPos, ArrayList<Piece> whitePieces,
            ArrayList<Piece> blackPieces) {
        if (color == ChessColor.BLACK) {
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

    public ArrayList<Piece> oppositeColorPiecesOverlapping(Vector2I thisPos, ArrayList<Piece> whitePieces,
            ArrayList<Piece> blackPieces) {
        ArrayList<Piece> toReturn = new ArrayList<Piece>();
        if (color == ChessColor.BLACK) {
            for (Piece p : whitePieces) {
                if (equals(p))
                    continue;
                if (hitboxOverlapsHitbox(thisPos, p))
                    toReturn.add(p);
            }
            return toReturn;

        } else {
            for (Piece p : blackPieces) {
                if (equals(p))
                    continue;
                if (hitboxOverlapsHitbox(thisPos, p))
                    toReturn.add(p);
            }
            return toReturn;
        }
    }

    public abstract int getMaterialValue();

    public abstract ImageIcon getImageIcon();

    public static final Vector2I pieceSizePixels = new Vector2I(Board.boardSizePixels.x / 8, Board.boardSizePixels.y / 8);

    public void draw(Graphics g, JPanel game) {
        Vector2I posPanel = Board.boardPosToPanelPos(getVisiblePos());
        getImageIcon().paintIcon(game, g, posPanel.x - (pieceSizePixels.x / 2), posPanel.y - (pieceSizePixels.y / 2));
    }
}