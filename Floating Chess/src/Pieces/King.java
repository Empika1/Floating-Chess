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

    public boolean isInMoveRadius(Vector2I pos) {
        return pos.subtract(getTruePos()).getSquaredLength() <= moveRadius * moveRadius;
    }

    public boolean canMoveTo(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        if (isOverlappingEdge(pos) || isOverlappingSameColorPiece(pos, whitePieces,
                blackPieces)) {
            return false;
        }

        return isInMoveRadius(pos);
    }

    public Vector2I closestValidPoint(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        Vector2I searchPos = new Vector2I();
        double closestLengthSquaredSoFar = Double.MAX_VALUE;
        Vector2I closestPointSoFar = new Vector2I();

        for(searchPos.x = getTruePos().x - moveRadius; searchPos.x <= getTruePos().x + moveRadius; searchPos.x++) {
            for(searchPos.y = getTruePos().y - moveRadius; searchPos.y <= getTruePos().y + moveRadius; searchPos.y++) {
                if(canMoveTo(searchPos, whitePieces, blackPieces)) {
                double lengthSquared = searchPos.subtract(pos).getSquaredLength();
                    if(lengthSquared < closestLengthSquaredSoFar) {
                        closestLengthSquaredSoFar = lengthSquared;
                        closestPointSoFar = searchPos.copy();
                    }
                }
            }
        }
        if(closestPointSoFar.equals(getTruePos()));
        return closestPointSoFar;
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