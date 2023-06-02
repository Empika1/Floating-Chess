package Pieces;

import javax.swing.*;
import java.util.*;
import Images.*;
import Utils.*;
import Game.*;
import Board.*;

public final class Knight extends Piece {
    static final String pieceName = "Knight";

    public String getPieceName() {
        return pieceName;
    }

    static final int moveRadius = (int) (Math.sqrt(5) * Game.boardSizeI.x / 8.0);

    Vector2I closestPointOnRadius(Vector2I pos) {
        Vector2I diff = pos.subtract(getTruePos());
        if (diff.getSquaredLength() == 0)
            return new Vector2I(getTruePos().x + moveRadius, getTruePos().y);
        Vector2I diffScaled = diff.setLength(moveRadius);
        return getTruePos().add(diffScaled);
    }

    public boolean canMoveTo(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        if (isOverlappingEdge(pos) || isOverlappingSameColorPiece(pos, whitePieces,
                blackPieces))
            return false;

        return pos == closestPointOnRadius(pos);
    }

    public Vector2I closestValidPoint(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
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

    static final int hitboxRadius = (int) (0.35 * Game.boardSizeI.x / 8);

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
            Board.boardPosToPanelPos(new Vector2I(hitboxRadius * 2, hitboxRadius * 2)));

    public ImageIcon getHitboxIcon() {
        return hitboxImage;
    }
}