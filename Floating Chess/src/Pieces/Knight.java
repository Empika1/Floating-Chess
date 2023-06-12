//Its a knight. Yup
//Some of the things in this file are very complicated and hard to explain so I will not comment all the details. Sorry.
//Also, many of the things are explained in Piece.java already so I will not explain them twice
package Pieces;

import javax.swing.*;
import java.util.*;
import Images.*;
import Utils.*;
import Board.*;

public final class Knight extends Piece {
    public String getPieceName() {
        return "Knight";
    }

    public PieceType getPieceType() {
        return PieceType.KNIGHT;
    }

    static final int moveRadius = (int) (Math.sqrt(5) * Board.boardSizeI.x / 8.0); //the radius of the circle around the knight that the knight is able to move to

    Vector2I closestPointOnRadius(Vector2I pos) { //gets the closest point on the radius to an input position
        Vector2I diff = pos.subtract(getTruePos());
        if (diff.getSquaredLength() == 0)
            return new Vector2I(getTruePos().x + moveRadius, getTruePos().y);
        Vector2I diffScaled = diff.setLength(moveRadius);
        return getTruePos().add(diffScaled);
    }

    public Vector2I closestValidPoint(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) { //loops through all points on the move radius and finds the closest to a given input position that can be moved to
        Vector2I searchPosSquare = new Vector2I(getTruePos().x + moveRadius, getTruePos().y + moveRadius);
        Vector2I searchPos;
        double searchDistanceSquared;
        Vector2I closestPosSoFar = null;
        double closestSquaredDistanceSoFar = Double.MAX_VALUE;
        for (; searchPosSquare.x >= getTruePos().x - moveRadius; searchPosSquare.x--) {
            searchPos = closestPointOnRadius(searchPosSquare);
            if (!(isOverlappingEdge(searchPos) || isOverlappingSameColorPiece(searchPos, whitePieces,
                    blackPieces))) {
                searchDistanceSquared = searchPos.subtract(pos).getSquaredLength();
                if (searchDistanceSquared < closestSquaredDistanceSoFar) {
                    closestSquaredDistanceSoFar = searchDistanceSquared;
                    closestPosSoFar = searchPos.copy();
                }
            }
        }
        for (; searchPosSquare.y >= getTruePos().y - moveRadius; searchPosSquare.y--) {
            searchPos = closestPointOnRadius(searchPosSquare);
            if (!(isOverlappingEdge(searchPos) || isOverlappingSameColorPiece(searchPos, whitePieces,
                    blackPieces))) {
                searchDistanceSquared = searchPos.subtract(pos).getSquaredLength();
                if (searchDistanceSquared < closestSquaredDistanceSoFar) {
                    closestSquaredDistanceSoFar = searchDistanceSquared;
                    closestPosSoFar = searchPos.copy();
                }
            }
        }
        for (; searchPosSquare.x <= getTruePos().x + moveRadius; searchPosSquare.x++) {
            searchPos = closestPointOnRadius(searchPosSquare);
            if (!(isOverlappingEdge(searchPos) || isOverlappingSameColorPiece(searchPos, whitePieces,
                    blackPieces))) {
                searchDistanceSquared = searchPos.subtract(pos).getSquaredLength();
                if (searchDistanceSquared < closestSquaredDistanceSoFar) {
                    closestSquaredDistanceSoFar = searchDistanceSquared;
                    closestPosSoFar = searchPos.copy();
                }
            }
        }
        for (; searchPosSquare.y < getTruePos().y + moveRadius; searchPosSquare.y++) {
            searchPos = closestPointOnRadius(searchPosSquare);
            if (!(isOverlappingEdge(searchPos) || isOverlappingSameColorPiece(searchPos, whitePieces,
                    blackPieces))) {
                searchDistanceSquared = searchPos.subtract(pos).getSquaredLength();
                if (searchDistanceSquared < closestSquaredDistanceSoFar) {
                    closestSquaredDistanceSoFar = searchDistanceSquared;
                    closestPosSoFar = searchPos.copy();
                }
            }
        }

        return closestPosSoFar;
    }

    static final int hitboxRadius = (int) (0.35 * Board.boardSizeI.x / 8);

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

    static ImageIcon moveAreaImage = ImageManager.resize(ImageManager.knightMove, Board.boardSizePixels);

    public ImageIcon getMoveAreaIcon() {
        return moveAreaImage;
    }

    static ImageIcon hitboxImage = ImageManager.resize(ImageManager.hitbox,
            Board.iPosToPixelPos(new Vector2I(hitboxRadius * 2, hitboxRadius * 2)));

    public ImageIcon getHitboxIcon() {
        return hitboxImage;
    }
}