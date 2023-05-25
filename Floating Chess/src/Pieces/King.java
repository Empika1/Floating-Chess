package Pieces;

import javax.swing.*;
import java.util.*;
import Images.*;

public final class King extends Piece {
    static final String pieceName = "King";

    public String getPieceName() {
        return pieceName;
    }

    public double moveRadius = 1;

    public boolean canMoveTo(double x, double y, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        if (isOverlappingEdge(x, y) || isOverlappingSameColorPiece(x, y, whitePieces, blackPieces))
            return false;
        if ((x - getTrueX()) * (x - getTrueX()) + (y - getTrueY()) * (y - getTrueY()) <= moveRadius * moveRadius)
            return true;
        return false;
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