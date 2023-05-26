package Pieces;

import javax.swing.*;
import java.util.*;
import Images.*;
import Utils.*;
import Game.*;

public final class Knight extends Piece {
    static final String pieceName = "Knight";

    public String getPieceName() {
        return pieceName;
    }

    public boolean canMoveTo(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        return true;
    }

    static final int hitboxRadius = (int)(0.375 * Game.boardSizeI.x / 8);

    public int getHitboxRadius() {
        return hitboxRadius;
    }

    static final int materialValue = 3;

    public int getMaterialValue() {
        return materialValue;
    }

    static ImageIcon blackImage = ImageManager.resize(ImageManager.bn, pieceSizePixels);
    static ImageIcon whiteImage = ImageManager.resize(ImageManager.wn, pieceSizePixels);

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