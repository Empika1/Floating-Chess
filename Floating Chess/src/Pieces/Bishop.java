package Pieces;

import javax.swing.*;
import java.util.*;
import Images.*;
import Utils.*;
import Game.*;

public final class Bishop extends Piece {
    static final String pieceName = "Bishop";

    public String getPieceName() {
        return pieceName;
    }

    static final double maxAngleFromDiagonal = 5;
    static final double maxSlopeFromRightCardinal = Math.tan((45 + maxAngleFromDiagonal) * Math.PI / 180);
    static final double minSlopeFromRightCardinal = Math.tan((45 - maxAngleFromDiagonal) * Math.PI / 180);

    public boolean isInValidAngle(Vector2I pos) {
        if (pos.x == getTruePos().x)
            return true;

        Vector2I diff = pos.subtract(getTruePos());
        diff.x = Math.abs(diff.x);
        diff.y = Math.abs(diff.y);
        double absoluteSlope = ((double) diff.y) / diff.x;

        return absoluteSlope <= maxSlopeFromRightCardinal && absoluteSlope >= minSlopeFromRightCardinal;
    }

    static final double moveLength = Math.sqrt(2) * Game.boardSizeI.x / 8;
    Rook fakeRook = new Rook();

    public Vector2I closestClearPointOnLine(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        fakeRook.setTruePos(getTruePos(), false);
        fakeRook.setColor(getColor());
        Vector2I closestClearPoint = fakeRook.closestClearPointOnLine(pos, whitePieces, blackPieces);
        Vector2I diff = closestClearPoint.subtract(getTruePos());
        Vector2I floorDiff = diff.setLength((Math.floor(diff.getLength() / moveLength)) * moveLength);
        return getTruePos().add(floorDiff);
    }

    public boolean canMoveTo(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        if (isOverlappingEdge(pos))
            return false;

        if (!isInValidAngle(pos))
            return false;

        return closestClearPointOnLine(pos, whitePieces, blackPieces).equals(pos);
    }

    public Vector2I closestValidPoint(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        if (pos == getTruePos())
            return pos;

        Vector2I diff = pos.subtract(getTruePos());
        Vector2I searchStartPos = new Vector2I();
        Vector2I searchDir1 = new Vector2I();
        Vector2I searchDir2 = new Vector2I();
        if (Math.abs(diff.x) > 0 && Math.abs(diff.y) > 0) {
            searchStartPos = new Vector2I(Geometry.lineLineIntersection(new Vector2(pos), -1, new Vector2(getTruePos()), 1));
            searchDir1 = new Vector2I(-1, 1);
            searchDir2 = new Vector2I(1, -1);
        } else {
            searchStartPos = new Vector2I(Geometry.lineLineIntersection(new Vector2(pos), 1, new Vector2(getTruePos()), -1));
            searchDir1 = new Vector2I(-1, -1);
            searchDir2 = new Vector2I(1, 1);
        }

        Vector2I closestPosSoFar = null;
        double closestSquaredDistanceSoFar = Double.MAX_VALUE;
        Vector2I searchPos;
        double searchSquaredDistance;
        for (int i = 0;; i++) {
            searchPos = searchStartPos.add(searchDir1.scale(i));
            if (!isInValidAngle(searchPos))
                return closestPosSoFar;

            searchPos = closestClearPointOnLine(searchPos, whitePieces, blackPieces);
            searchSquaredDistance = searchPos.subtract(pos).getSquaredLength();
            if (searchSquaredDistance < closestSquaredDistanceSoFar) {
                closestSquaredDistanceSoFar = searchSquaredDistance;
                closestPosSoFar = searchPos.copy();
            }

            searchPos = searchStartPos.add(searchDir2.scale(i));
            searchPos = closestClearPointOnLine(searchPos, whitePieces, blackPieces);
            searchSquaredDistance = searchPos.subtract(pos).getSquaredLength();
            if (searchSquaredDistance < closestSquaredDistanceSoFar) {
                closestSquaredDistanceSoFar = searchSquaredDistance;
                closestPosSoFar = searchPos.copy();
            }
        }
    }

    static final int hitboxRadius = (int) (0.375 * Game.boardSizeI.x / 8);

    public int getHitboxRadius() {
        return hitboxRadius;
    }

    static final int materialValue = 3;

    public int getMaterialValue() {
        return materialValue;
    }

    static ImageIcon blackImage = ImageManager.resize(ImageManager.bb, pieceSizePixels);
    static ImageIcon whiteImage = ImageManager.resize(ImageManager.wb, pieceSizePixels);

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