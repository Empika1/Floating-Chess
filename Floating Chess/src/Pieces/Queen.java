package Pieces;

import javax.swing.*;
import java.util.*;
import Images.*;
import Utils.*;
import Game.*;

public final class Queen extends Piece {
    static final String pieceName = "Queen";

    public String getPieceName() {
        return pieceName;
    }

    Rook fakeRook = new Rook();
    Bishop fakeBishop = new Bishop();
    public boolean canMoveTo(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        fakeRook.setTruePos(getTruePos(), false);
        fakeRook.setColor(getColor());
        fakeBishop.setTruePos(getTruePos(), false);
        fakeBishop.setColor(getColor());
        return fakeRook.canMoveTo(pos, whitePieces, blackPieces) || fakeBishop.canMoveTo(pos, whitePieces, blackPieces);
    }

    public Vector2I closestValidPoint(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        fakeRook.setTruePos(getTruePos(), false);
        fakeRook.setColor(getColor());
        fakeBishop.setTruePos(getTruePos(), false);
        fakeBishop.setColor(getColor());
        Vector2I rookPoint = fakeRook.closestValidPoint(pos, whitePieces, blackPieces);
        double rookSquaredDistance = pos.subtract(rookPoint).getSquaredLength();
        Vector2I bishopPoint = fakeBishop.closestValidPoint(pos, whitePieces, blackPieces);
        double bishopSquaredDistance = pos.subtract(bishopPoint).getSquaredLength();
        if(rookSquaredDistance < bishopSquaredDistance)
            return rookPoint;
        else
            return bishopPoint;
    }

    static final int hitboxRadius = (int)(0.375 * Game.boardSizeI.x / 8);

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
}