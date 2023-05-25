package Pieces;

import javax.swing.*;
import java.util.*;
import Images.*;
import Utils.*;

public final class Queen extends Piece {
    static final String pieceName = "Queen";

    public String getPieceName() {
        return pieceName;
    }

    public boolean canMoveTo(Vector2 pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        return true;
    }

    public Vector2 closestValidPoint(Vector2 pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        return pos.copy();
    }

    static final double hitboxRadius = 0.375;

    public double getHitboxRadius() {
        return hitboxRadius;
    }

    static final double hurtboxRadius = 0.375;

    public double getHurtboxRadius() {
        return hurtboxRadius;
    }

    static final int materialValue = 9;

    public int getMaterialValue() {
        return materialValue;
    }

    static ImageIcon blackImage = ImageManager.resize(ImageManager.bq, pieceSizeX, pieceSizeY);
    static ImageIcon whiteImage = ImageManager.resize(ImageManager.wq, pieceSizeX, pieceSizeY);

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