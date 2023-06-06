package Pieces;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.*;
import Game.*;
import Images.ImageManager;
import Utils.*;
import Board.*;

public abstract class Piece implements Serializable {
    protected int id;

    public int getID() {
        return id;
    }

    public void setID(int set) {
        id = set;
    }

    public abstract String getPieceName();

    public abstract PieceType getPieceType();

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

    // public abstract boolean canMoveTo(Vector2I pos, ArrayList<Piece> whitePieces,
    // ArrayList<Piece> blackPieces);

    public abstract Vector2I closestValidPoint(Vector2I pos, ArrayList<Piece> whitePieces,
            ArrayList<Piece> blackPieces);

    public abstract int getHitboxRadius();

    public boolean isInHitbox(Vector2I pos) {
        Vector2I diff = pos.subtract(getTruePos());
        double distanceSquared = diff.x * diff.x + diff.y * diff.y;
        return distanceSquared <= getHitboxRadius() * getHitboxRadius();
    }

    public boolean hitboxOverlapsHitbox(Vector2I thisPos, double thisRadius, Piece b) {
        return Geometry.doesCircleOverlapCircle(thisPos, thisRadius, b.getTruePos(), b.getHitboxRadius());
    }

    public boolean hitboxOverlapsHitbox(Vector2I thisPos, Piece b) {
        return hitboxOverlapsHitbox(thisPos, getHitboxRadius(), b);
    }

    public boolean isOverlappingEdge(Vector2I thisPos, double thisRadius) {
        return thisPos.x - thisRadius < 0 || thisPos.y - thisRadius < 0
                || thisPos.x + thisRadius > GameScreen.boardSizeI.x
                || thisPos.y + thisRadius > GameScreen.boardSizeI.y;
    }

    public boolean isOverlappingEdge(Vector2I thisPos) {
        return isOverlappingEdge(thisPos, getHitboxRadius());
    }

    public boolean isOverlappingSameColorPiece(Vector2I thisPos, double thisRadius, ArrayList<Piece> whitePieces,
            ArrayList<Piece> blackPieces) {
        if (color == ChessColor.WHITE) {
            for (Piece p : whitePieces) {
                if (equals(p))
                    continue;
                if (hitboxOverlapsHitbox(thisPos, thisRadius, p))
                    return true;
            }

        } else {
            for (Piece p : blackPieces) {
                if (equals(p))
                    continue;
                if (hitboxOverlapsHitbox(thisPos, thisRadius, p))
                    return true;
            }
        }
        return false;
    }

    public boolean isOverlappingSameColorPiece(Vector2I thisPos, ArrayList<Piece> whitePieces,
            ArrayList<Piece> blackPieces) {
        return isOverlappingSameColorPiece(thisPos, getHitboxRadius(), whitePieces, blackPieces);
    }

    public boolean isOverlappingOppositeColorPiece(Vector2I thisPos, double thisRadius, ArrayList<Piece> whitePieces,
            ArrayList<Piece> blackPieces) {
        if (color == ChessColor.BLACK) {
            for (Piece p : whitePieces) {
                if (equals(p))
                    continue;
                if (hitboxOverlapsHitbox(thisPos, thisRadius, p))
                    return true;
            }

        } else {
            for (Piece p : blackPieces) {
                if (equals(p))
                    continue;
                if (hitboxOverlapsHitbox(thisPos, thisRadius, p))
                    return true;
            }
        }
        return false;
    }

    public boolean isOverlappingOppositeColorPiece(Vector2I thisPos, ArrayList<Piece> whitePieces,
            ArrayList<Piece> blackPieces) {
        return isOverlappingOppositeColorPiece(thisPos, getHitboxRadius(), whitePieces, blackPieces);
    }

    public ArrayList<Piece> oppositeColorPiecesOverlapping(Vector2I thisPos, double thisRadius,
            ArrayList<Piece> whitePieces,
            ArrayList<Piece> blackPieces) {
        ArrayList<Piece> toReturn = new ArrayList<Piece>();
        if (color == ChessColor.BLACK) {
            for (Piece p : whitePieces) {
                if (equals(p))
                    continue;
                if (hitboxOverlapsHitbox(thisPos, thisRadius, p))
                    toReturn.add(p);
            }
            return toReturn;

        } else {
            for (Piece p : blackPieces) {
                if (equals(p))
                    continue;
                if (hitboxOverlapsHitbox(thisPos, thisRadius, p))
                    toReturn.add(p);
            }
            return toReturn;
        }
    }

    public ArrayList<Piece> oppositeColorPiecesOverlapping(Vector2I thisPos, ArrayList<Piece> whitePieces,
            ArrayList<Piece> blackPieces) {
        return oppositeColorPiecesOverlapping(thisPos, getHitboxRadius(), whitePieces, blackPieces);
    }

    public abstract int getMaterialValue();

    public abstract ImageIcon getImageIcon();

    public abstract ImageIcon getMoveAreaIcon();

    public abstract ImageIcon getHitboxIcon();

    public static final Vector2I pieceSizePixels = new Vector2I(Board.boardSizePixels.x / 8,
            Board.boardSizePixels.y / 8);

    public void drawPiece(Graphics g, JPanel board, Color modulate) {
        Vector2I visiblePosPanel = Board.boardPosToPanelPos(getVisiblePos());
        if (modulate != null) {
            ImageIcon modulatedIcon = ImageManager.modulateColor(getImageIcon(), modulate);
            modulatedIcon.paintIcon(board, g, visiblePosPanel.x - (pieceSizePixels.x / 2),
                    visiblePosPanel.y - (pieceSizePixels.y / 2));
        } else {
            getImageIcon().paintIcon(board, g, visiblePosPanel.x - (pieceSizePixels.x / 2),
                    visiblePosPanel.y - (pieceSizePixels.y / 2));
        }
    }

    public void drawMoveArea(Graphics g, JPanel board) {
        Vector2I truePosPanel = Board.boardPosToPanelPos(getTruePos());
        getMoveAreaIcon().paintIcon(board, g, truePosPanel.x - (getMoveAreaIcon().getIconWidth() / 2),
                truePosPanel.y - (getMoveAreaIcon().getIconHeight() / 2));
    }

    public void drawHitbox(Graphics g, JPanel board) {
        Vector2I visiblePosPanel = Board.boardPosToPanelPos(getVisiblePos());
        getHitboxIcon().paintIcon(board, g, visiblePosPanel.x - (getHitboxIcon().getIconWidth() / 2),
                visiblePosPanel.y - (getHitboxIcon().getIconHeight() / 2));
    }
}