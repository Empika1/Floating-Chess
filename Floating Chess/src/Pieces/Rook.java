package Pieces;

import javax.swing.*;
import java.util.*;
import Images.*;
import Utils.*;
import Game.*;

public final class Rook extends Piece {
    static final String pieceName = "Rook";

    public String getPieceName() {
        return pieceName;
    }

    static final double maxAngleFromCardinal = 5;
    static final double maxSlopeFromRightCardinal = Math.tan(maxAngleFromCardinal);
    static final double minSlopeFromTopCardinal = 1/maxSlopeFromRightCardinal;

    public boolean isInValidAngle(Vector2I pos) {
        if(pos.x == getTruePos().x)
            return true;

        Vector2I diff = pos.subtract(getTruePos());
        diff.x = Math.abs(diff.x);
        diff.y = Math.abs(diff.y);
        double absoluteSlope = diff.y / diff.x;

        return absoluteSlope <= maxSlopeFromRightCardinal || absoluteSlope >= minSlopeFromTopCardinal;
    }

    public boolean canMoveTo(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        return true;
    }

    public Vector2I closestValidPoint(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        return new Vector2I(0, 0);
    }

    static final int hitboxRadius = (int)(0.375 * Game.boardSizeI.x / 8);

    public int getHitboxRadius() {
        return hitboxRadius;
    }

    static final int materialValue = 5;

    public int getMaterialValue() {
        return materialValue;
    }

    static ImageIcon blackImage = ImageManager.resize(ImageManager.br, pieceSizePixels);
    static ImageIcon whiteImage = ImageManager.resize(ImageManager.wr, pieceSizePixels);

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