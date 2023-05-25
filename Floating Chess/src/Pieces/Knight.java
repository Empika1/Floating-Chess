package Pieces;

import javax.swing.*;
import java.util.*;
import Images.*;

public final class Knight extends Piece {
    static final String pieceName = "Knight";

    public String getPieceName() {
        return pieceName;
    }

    public boolean canMoveTo(double x, double y, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        if (isOverlappingEdge(x, y) || isOverlappingSameColorPiece(x, y, whitePieces, blackPieces))
            return false;
        return true;
    }

    static final double hitboxRadius = 0.375;

    public double getHitboxRadius() {
        return hitboxRadius;
    }

    static final double hurtboxRadius = 0.375;

    public double getHurtboxRadius() {
        return hurtboxRadius;
    }

    static final int materialValue = 3;

    public int getMaterialValue() {
        return materialValue;
    }

    static ImageIcon blackImage = ImageManager.resize(ImageManager.bn, pieceSizeX, pieceSizeY);
    static ImageIcon whiteImage = ImageManager.resize(ImageManager.wn, pieceSizeX, pieceSizeY);

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