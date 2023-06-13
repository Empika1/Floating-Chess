//Its a queen. Yup
//Some of the things in this file are very complicated and hard to explain so I will not comment all the details. Sorry.
//Also, many of the things are explained in Piece.java already so I will not explain them twice
package Pieces;

import javax.swing.*;
import java.util.*;
import Images.*;
import Utils.*;
import Board.*;

public final class Queen extends Piece {
    public String getPieceName() {
        return "Queen";
    }

    public PieceType getPieceType() {
        return PieceType.QUEEN;
    }

    Rook fakeRook = new Rook(); // a queen can move in the same way as a bishop and rook combined, so i made it literally a bishop and rook combined
    Bishop fakeBishop = new Bishop();

    public Vector2I closestValidPoint(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        fakeRook.setTruePos(getTruePos(), false); // moves the imaginary rook to the same place as this
        fakeRook.setColor(getColor()); // sets the imaginary rook's color to this one's
        fakeBishop.setTruePos(getTruePos(), false); // same with bishop
        fakeBishop.setColor(getColor());
        Vector2I rookPoint = fakeRook.closestValidPoint(pos, whitePieces, blackPieces); // finds the closest point that the rook can move to
        double rookSquaredDistance = pos.subtract(rookPoint).getSquaredLength(); // gets the distance to that point
        Vector2I bishopPoint = fakeBishop.closestValidPoint(pos, whitePieces, blackPieces); // finds the closest point that the bishop can move to
        double bishopSquaredDistance = pos.subtract(bishopPoint).getSquaredLength(); // gets the distance to that point

        if (rookSquaredDistance < bishopSquaredDistance) // returns the closer of the 2 points
            return rookPoint;
        else
            return bishopPoint;
    }

    static final int hitboxRadius = (int) (0.35 * Board.boardSizeI.x / 8);

    public int getHitboxRadius() {
        return hitboxRadius;
    }

    static final int materialValue = 9;

    public int getMaterialValue() {
        return materialValue;
    }

    static ImageIcon blackImage = ImageManager.resize(ImageManager.bq, pieceSizePixels);
    static ImageIcon whiteImage = ImageManager.resize(ImageManager.wq, pieceSizePixels);

    public ImageIcon getImageIcon() {
        switch (color) {
            case BLACK:
                return blackImage;
            case WHITE:
                return whiteImage;
            default:
                return null;
        }
    }

    static ImageIcon moveAreaImage = ImageManager.resize(ImageManager.queenMove, Board.boardSizePixels.scale(2));

    public ImageIcon getMoveAreaIcon() {
        return moveAreaImage;
    }

    static ImageIcon hitboxImage = ImageManager.resize(ImageManager.hitbox,
            Board.iPosToPixelPos(new Vector2I(hitboxRadius * 2, hitboxRadius * 2)));

    public ImageIcon getHitboxIcon() {
        return hitboxImage;
    }
}