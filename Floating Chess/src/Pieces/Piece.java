//the base class that all chess pieces inherit from
package Pieces;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.*;
import Images.ImageManager;
import Utils.*;
import Board.*;

public abstract class Piece implements Serializable {
    protected int id; // the unique ID of a piece, can be used to search through a list of pieces for a specific one.

    public int getID() {
        return id;
    }

    public void setID(int set) {
        id = set;
    }

    public abstract String getPieceName(); // The name of a piece, for example "King" or "Bishop". Implemented by subclasses.

    public abstract PieceType getPieceType(); // Gets the type of a piece. This is very similar to the name but is a cool and epic enum

    protected ChessColor color; // The color of a piece, either black or white

    public ChessColor getColor() {
        return color;
    }

    public void setColor(ChessColor set) {
        color = set;
    }

    protected Vector2I truePos; // The true position of a piece. This is different from visible pos because if a piece is held, its visible pos will change but its true pos wont

    public Vector2I getTruePos() {
        return truePos;
    }

    public void setTruePos(Vector2I set, boolean isInGame) {
        truePos = set;
        visiblePos = set; // if the true pos is changed, the visible pos should update too
        if (isInGame)
            setHasMoved(true); // if the piece has changed position in the game, that means it has moved
    }

    protected boolean hasMoved = false; // if the piece has moved. used by pawns to determine if they can move 2 squares forward, and also by the king/rook for castling

    public boolean getHasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean set) {
        hasMoved = set;
    }

    protected Vector2I visiblePos; // the visible pos. usually the same as the true pos, unless the piece is being held

    public Vector2I getVisiblePos() {
        return visiblePos;
    }

    public void setVisiblePos(Vector2I set) {
        visiblePos = set;
    }

    public abstract Vector2I closestValidPoint(Vector2I pos, ArrayList<Piece> whitePieces,
            ArrayList<Piece> blackPieces); // the closest point to an input pos that a piece can move to. implemented by subclasses.

    public abstract int getHitboxRadius(); // the radius of a piece's hitbox.

    public boolean isInHitbox(Vector2I pos) { // if an input point is in a piece's hitbox or not
        Vector2I diff = pos.subtract(getTruePos());
        double distanceSquared = diff.x * diff.x + diff.y * diff.y; // pythagorean theorem
        return distanceSquared <= getHitboxRadius() * getHitboxRadius();
    }

    public boolean hitboxOverlapsHitbox(Vector2I thisPos, double thisRadius, Piece b) { // if the hitbox of this piece overlaps the hitbox of another piece. pos and radius are parameters so you can check if it would be overlapping another piece if said parameters were changed
        return Geometry.doesCircleOverlapCircle(thisPos, thisRadius, b.getTruePos(), b.getHitboxRadius());
    }

    public boolean hitboxOverlapsHitbox(Vector2I thisPos, Piece b) { // same as the other method but without radius parameter. used more often because hitbox radius doesnt really change
        return hitboxOverlapsHitbox(thisPos, getHitboxRadius(), b);
    }

    public boolean isOverlappingEdge(Vector2I thisPos, double thisRadius) { // if a piece is overlapping the edge of the board
        return thisPos.x - thisRadius < 0 || thisPos.y - thisRadius < 0
                || thisPos.x + thisRadius > Board.boardSizeI.x
                || thisPos.y + thisRadius > Board.boardSizeI.y;
    }

    public boolean isOverlappingEdge(Vector2I thisPos) { // same as the other method but without radius parameter for the same reason
        return isOverlappingEdge(thisPos, getHitboxRadius());
    }

    public boolean isOverlappingSameColorPiece(Vector2I thisPos, double thisRadius, ArrayList<Piece> whitePieces,
            ArrayList<Piece> blackPieces) { // loops through every piece of the same color as this piece and checks if its hitbox is overlapping with any of them
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
            ArrayList<Piece> blackPieces) { // same but without radius param
        return isOverlappingSameColorPiece(thisPos, getHitboxRadius(), whitePieces, blackPieces);
    }

    public boolean isOverlappingOppositeColorPiece(Vector2I thisPos, double thisRadius, ArrayList<Piece> whitePieces,
            ArrayList<Piece> blackPieces) { // same but for opposite color pieces
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
            ArrayList<Piece> blackPieces) { // same but without radius param
        return isOverlappingOppositeColorPiece(thisPos, getHitboxRadius(), whitePieces, blackPieces);
    }

    public ArrayList<Piece> oppositeColorPiecesOverlapping(Vector2I thisPos, double thisRadius,
            ArrayList<Piece> whitePieces,
            ArrayList<Piece> blackPieces) { // loops through all the pieces of the opposite color and returns all of them that are overlapping this piece
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
            ArrayList<Piece> blackPieces) { // same but without radius param
        return oppositeColorPiecesOverlapping(thisPos, getHitboxRadius(), whitePieces, blackPieces);
    }

    public abstract int getMaterialValue(); // the material value of the piece. i dont think this is actually used anywhere... implemented by subclasses

    public abstract ImageIcon getImageIcon(); // the icon to use while drawing this piece. implemented by subclasses.

    public abstract ImageIcon getMoveAreaIcon(); // the icon of the area this piece would be able to move to on an empty board. implemented by subclasses.

    public abstract ImageIcon getHitboxIcon(); // the icon of this piece's hitbox. implemented by subclasses

    public static final Vector2I pieceSizePixels = new Vector2I(Board.boardSizePixels.x / 8,
            Board.boardSizePixels.y / 8); // the size of a piece icon in pixels. exactly 1/8th the size of a board.

    public void drawPiece(Graphics g, JPanel board, Color tint) { // draws a piece to the board, modulating it to a specific color if wanted
        Vector2I visiblePosPanel = Board.iPosToPixelPos(getVisiblePos()); // converts the piece's position to pixel coords
        if (tint != null) { // if there is a color to tint to, tint then draw
            ImageIcon modulatedIcon = ImageManager.tint(getImageIcon(), tint);
            modulatedIcon.paintIcon(board, g, visiblePosPanel.x - (pieceSizePixels.x / 2),
                    visiblePosPanel.y - (pieceSizePixels.y / 2)); // -piecesize/2 is because the piece coordinates represent the center of the piece but its drawn from the top left corner so i need to shift it over
        } else { // else dont tint, just draw
            getImageIcon().paintIcon(board, g, visiblePosPanel.x - (pieceSizePixels.x / 2),
                    visiblePosPanel.y - (pieceSizePixels.y / 2)); // same shift thing
        }
    }

    public void drawMoveArea(Graphics g, JPanel board) { // draws the area this piece can move to
        Vector2I truePosPanel = Board.iPosToPixelPos(getTruePos()); // converts the pieces position to pixel coords
        getMoveAreaIcon().paintIcon(board, g, truePosPanel.x - (getMoveAreaIcon().getIconWidth() / 2),
                truePosPanel.y - (getMoveAreaIcon().getIconHeight() / 2)); // draws it, same shift thing as drawpiece
    }

    public void drawHitbox(Graphics g, JPanel board) { // exact same as the 2 methods above
        Vector2I visiblePosPanel = Board.iPosToPixelPos(getVisiblePos());
        getHitboxIcon().paintIcon(board, g, visiblePosPanel.x - (getHitboxIcon().getIconWidth() / 2),
                visiblePosPanel.y - (getHitboxIcon().getIconHeight() / 2));
    }
}