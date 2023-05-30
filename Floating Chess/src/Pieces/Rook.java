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
    static final double maxSlopeFromRightCardinal = Math.tan(maxAngleFromCardinal * Math.PI / 180);
    static final double minSlopeFromTopCardinal = 1 / maxSlopeFromRightCardinal;

    public boolean isInValidAngle(Vector2I pos) {
        if (pos.x == getTruePos().x)
            return true;

        Vector2I diff = pos.subtract(getTruePos());
        diff.x = Math.abs(diff.x);
        diff.y = Math.abs(diff.y);
        double absoluteSlope = ((double) diff.y) / diff.x;

        return absoluteSlope <= maxSlopeFromRightCardinal || absoluteSlope >= minSlopeFromTopCardinal;
    }

    public Vector2I closestClearPointOnLine(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        Vector2I furthestPosSoFar = pos;
        double furthestSquaredDistanceSoFar = pos.subtract(getTruePos()).getSquaredLength();

        Vector2 posV2 = new Vector2(pos);
        Vector2 truePosV2 = new Vector2(getTruePos());

        Vector2 topIntersection = Geometry.lineLineIntersection(truePosV2, posV2, new Vector2(0, getHitboxRadius()),
                new Vector2(Game.boardSizeI.x, getHitboxRadius()));
        Vector2 bottomIntersection = Geometry.lineLineIntersection(truePosV2, posV2,
                new Vector2(0, Game.boardSizeI.y - getHitboxRadius()),
                new Vector2(Game.boardSizeI.x, Game.boardSizeI.y - getHitboxRadius()));
        Vector2 leftIntersection = Geometry.lineLineIntersection(truePosV2, posV2, new Vector2(getHitboxRadius(), 0),
                new Vector2(getHitboxRadius(), Game.boardSizeI.y));
        Vector2 rightIntersection = Geometry.lineLineIntersection(truePosV2, posV2,
                new Vector2(Game.boardSizeI.x - getHitboxRadius(), 0),
                new Vector2(Game.boardSizeI.x - getHitboxRadius(), Game.boardSizeI.y));
        Vector2[] edgeIntersections = new Vector2[] { topIntersection, bottomIntersection, leftIntersection,
                rightIntersection };
        for (Vector2 i : edgeIntersections) {
            if (i == null)
                continue;
            if (Geometry.isPointInRect(posV2, truePosV2, i)) {
                double squaredDistanceToIntersection = truePosV2.subtract(i).getSquaredLength();
                if (squaredDistanceToIntersection < furthestSquaredDistanceSoFar) {
                    furthestSquaredDistanceSoFar = squaredDistanceToIntersection;
                    furthestPosSoFar = new Vector2I(i);
                }
            }
        }

        ArrayList<Piece> sameColorPieces = color == ChessColor.WHITE ? whitePieces : blackPieces;
        for (Piece p : sameColorPieces) {
            Vector2[] lineCircleIntersections = Geometry.lineCircleIntersections(posV2, truePosV2,
                    new Vector2(p.getTruePos()), (double) (p.getHitboxRadius() + getHitboxRadius()));
            for (Vector2 intersectionPoint : lineCircleIntersections) {
                if (Geometry.isPointInRect(posV2, truePosV2, intersectionPoint)) {
                    double squaredDistanceToIntersection = truePosV2.subtract(intersectionPoint).getSquaredLength();
                    if (squaredDistanceToIntersection < furthestSquaredDistanceSoFar) {
                        furthestSquaredDistanceSoFar = squaredDistanceToIntersection;
                        furthestPosSoFar = new Vector2I(intersectionPoint);
                    }
                }
            }
        }

        ArrayList<Piece> oppositeColorPieces = color == ChessColor.WHITE ? blackPieces : whitePieces;
        for (Piece p : oppositeColorPieces) {
            Vector2[] lineCircleIntersections = Geometry.lineCircleIntersections(posV2, truePosV2,
                    new Vector2(p.getTruePos()), (double) (p.getHitboxRadius() + getHitboxRadius()));
            if (lineCircleIntersections.length == 1) {
                double squaredDistanceToIntersection = truePosV2.subtract(lineCircleIntersections[0])
                        .getSquaredLength();
                if (squaredDistanceToIntersection < furthestSquaredDistanceSoFar) {
                    furthestSquaredDistanceSoFar = squaredDistanceToIntersection;
                    furthestPosSoFar = new Vector2I(lineCircleIntersections[0]);
                }
            } else if (lineCircleIntersections.length == 2) {
                double squaredDistanceToIntersection1 = truePosV2.subtract(lineCircleIntersections[0])
                        .getSquaredLength();
                double squaredDistanceToIntersection2 = truePosV2.subtract(lineCircleIntersections[1])
                        .getSquaredLength();
                if (squaredDistanceToIntersection1 > squaredDistanceToIntersection2) {
                    if (squaredDistanceToIntersection1 < furthestSquaredDistanceSoFar) {
                        furthestSquaredDistanceSoFar = squaredDistanceToIntersection1;
                        furthestPosSoFar = new Vector2I(lineCircleIntersections[0]);
                    }
                } else {
                    if (squaredDistanceToIntersection2 < furthestSquaredDistanceSoFar) {
                        furthestSquaredDistanceSoFar = squaredDistanceToIntersection2;
                        furthestPosSoFar = new Vector2I(lineCircleIntersections[1]);
                    }
                }
            }
        }
        return furthestPosSoFar;
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
        if (Math.abs(diff.y) <= Math.abs(diff.x)) {
            searchStartPos = new Vector2I(pos.x, getTruePos().y);
            searchDir1 = new Vector2I(0, 1);
            searchDir2 = new Vector2I(0, -1);
        } else {
            searchStartPos = new Vector2I(getTruePos().x, pos.y);
            searchDir1 = new Vector2I(1, 0);
            searchDir2 = new Vector2I(-1, 0);
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