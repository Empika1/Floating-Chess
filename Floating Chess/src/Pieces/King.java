package Pieces;

import javax.swing.*;
import java.util.*;
import Images.*;
import Utils.*;

public final class King extends Piece {
    static final String pieceName = "King";

    public String getPieceName() {
        return pieceName;
    }

    public double moveRadius = 1;

    public boolean canMoveTo(Vector2 pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        /*
         * if (isOverlappingEdge(x, y) || isOverlappingSameColorPiece(x, y, whitePieces,
         * blackPieces))
         * return false;
         */
        if (pos.subtract(getTruePos()).getSquaredLength() <= moveRadius * moveRadius)
            return true;
        return false;
    }

    public Vector2 closestValidPoint(Vector2 pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        if (canMoveTo(pos, whitePieces, blackPieces))
            return pos;

        if (pos.y == getTruePos().y) {
            if (pos.x > getTruePos().x)
                return new Vector2(getTruePos().x + moveRadius, getTruePos().y);
            if (pos.x > getTruePos().x)
            return new Vector2(getTruePos().x - moveRadius, getTruePos().y);
        }

        Vector2 diff = pos.subtract(getTruePos());
        Vector2 scaledDiff = diff.scale(moveRadius);
        return getTruePos().add(scaledDiff);
    }

    static final double hitboxRadius = 0.375;

    public double getHitboxRadius() {
        return hitboxRadius;
    }

    static final double hurtboxRadius = 0.375;

    public double getHurtboxRadius() {
        return hurtboxRadius;
    }

    static final int materialValue = Integer.MAX_VALUE;

    public int getMaterialValue() {
        return materialValue;
    }

    static ImageIcon blackImage = ImageManager.resize(ImageManager.bk, pieceSizeX, pieceSizeY);
    static ImageIcon whiteImage = ImageManager.resize(ImageManager.wk, pieceSizeX, pieceSizeY);

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