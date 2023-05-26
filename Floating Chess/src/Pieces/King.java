package Pieces;

import javax.swing.*;
import java.util.*;
import Images.*;
import Utils.*;
import Game.*;

public final class King extends Piece {
    static final String pieceName = "King";

    public String getPieceName() {
        return pieceName;
    }

    int moveRadius = (int) (0.375 * Game.boardSizeI.x / 8);

    public boolean canMoveTo(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        if (isOverlappingEdge(pos) || isOverlappingSameColorPiece(pos, whitePieces,
                blackPieces)) {
            return false;
        }

        if (pos.subtract(getTruePos()).getSquaredLength() <= moveRadius * moveRadius)
            return true;
        return false;
    }

    static final int hitboxRadius = (int) (0.375 * Game.boardSizeI.x / 8);

    public int getHitboxRadius() {
        return hitboxRadius;
    }

    static final int materialValue = Integer.MAX_VALUE;

    public int getMaterialValue() {
        return materialValue;
    }

    static ImageIcon blackImage = ImageManager.resize(ImageManager.bk, pieceSizePixels);
    static ImageIcon whiteImage = ImageManager.resize(ImageManager.wk, pieceSizePixels);

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