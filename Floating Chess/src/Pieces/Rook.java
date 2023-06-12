//Its a rook. Yup
//Some of the things in this file are very complicated and hard to explain so I will not comment all the details. Sorry.
//Also, many of the things are explained in Piece.java already so I will not explain them twice
package Pieces;

import javax.swing.*;
import java.util.*;
import Images.*;
import Utils.*;
import Board.*;

public final class Rook extends Piece {
    public String getPieceName() {
        return "Rook";
    }

    public PieceType getPieceType() {
        return PieceType.ROOK;
    }

    static final double maxAngleFromCardinal = 5; //the max angle off a perfect cardinal that a point can be and still be in the rook's move range
    static final double maxSlopeFromRightCardinal = Math.tan(maxAngleFromCardinal * Math.PI / 180);
    static final double minSlopeFromTopCardinal = 1 / maxSlopeFromRightCardinal;

    public boolean isInValidAngle(Vector2I pos) { //Determines if a given point is in the range of valid angles to move to
        if (pos.x == getTruePos().x)
            return true;

        Vector2I diff = pos.subtract(getTruePos());
        diff.x = Math.abs(diff.x);
        diff.y = Math.abs(diff.y);
        double absoluteSlope = ((double) diff.y) / diff.x;

        return absoluteSlope <= maxSlopeFromRightCardinal || absoluteSlope >= minSlopeFromTopCardinal;
    }

    public Vector2I closestClearPointOnLine(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) { //Given a point A and the rook at point B, this finds the point C on line AB that is closest to A but can still be moved to.
        Vector2 furthestPosSoFar = new Vector2(pos);
        double furthestSquaredDistanceSoFar = pos.subtract(getTruePos()).getSquaredLength();

        Vector2 posV2 = new Vector2(pos);
        Vector2 truePosV2 = new Vector2(getTruePos());

        Vector2 topIntersection = Geometry.lineLineIntersection(truePosV2, posV2, new Vector2(0, getHitboxRadius()),
                new Vector2(Board.boardSizeI.x, getHitboxRadius()));
        Vector2 bottomIntersection = Geometry.lineLineIntersection(truePosV2, posV2,
                new Vector2(0, Board.boardSizeI.y - getHitboxRadius()),
                new Vector2(Board.boardSizeI.x, Board.boardSizeI.y - getHitboxRadius()));
        Vector2 leftIntersection = Geometry.lineLineIntersection(truePosV2, posV2, new Vector2(getHitboxRadius(), 0),
                new Vector2(getHitboxRadius(), Board.boardSizeI.y));
        Vector2 rightIntersection = Geometry.lineLineIntersection(truePosV2, posV2,
                new Vector2(Board.boardSizeI.x - getHitboxRadius(), 0),
                new Vector2(Board.boardSizeI.x - getHitboxRadius(), Board.boardSizeI.y));
        if (topIntersection != null && truePosV2.y >= topIntersection.y && topIntersection.y >= posV2.y) {
            double squaredDistanceToIntersection = truePosV2.subtract(topIntersection).getSquaredLength();
            if (squaredDistanceToIntersection < furthestSquaredDistanceSoFar) {
                furthestSquaredDistanceSoFar = squaredDistanceToIntersection;
                furthestPosSoFar = topIntersection.copy();
            }
        }
        if (bottomIntersection != null && truePosV2.y <= bottomIntersection.y && bottomIntersection.y <= posV2.y) {
            double squaredDistanceToIntersection = truePosV2.subtract(bottomIntersection).getSquaredLength();
            if (squaredDistanceToIntersection < furthestSquaredDistanceSoFar) {
                furthestSquaredDistanceSoFar = squaredDistanceToIntersection;
                furthestPosSoFar = bottomIntersection.copy();
            }
        }
        if (leftIntersection != null && truePosV2.x >= leftIntersection.x && leftIntersection.x >= posV2.x) {
            double squaredDistanceToIntersection = truePosV2.subtract(leftIntersection).getSquaredLength();
            if (squaredDistanceToIntersection < furthestSquaredDistanceSoFar) {
                furthestSquaredDistanceSoFar = squaredDistanceToIntersection;
                furthestPosSoFar = leftIntersection.copy();
            }
        }
        if (rightIntersection != null && truePosV2.x <= rightIntersection.x && rightIntersection.x <= posV2.x) {
            double squaredDistanceToIntersection = truePosV2.subtract(rightIntersection).getSquaredLength();
            if (squaredDistanceToIntersection < furthestSquaredDistanceSoFar) {
                furthestSquaredDistanceSoFar = squaredDistanceToIntersection;
                furthestPosSoFar = rightIntersection.copy();
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
                        furthestPosSoFar = intersectionPoint.copy();
                    }
                }
            }
        }

        ArrayList<Piece> oppositeColorPieces = color == ChessColor.WHITE ? blackPieces : whitePieces;
        for (Piece p : oppositeColorPieces) {
            Vector2[] lineCircleIntersections = Geometry.lineCircleIntersections(posV2, truePosV2,
                    new Vector2(p.getTruePos()), (double) (p.getHitboxRadius() + getHitboxRadius()));
            if (lineCircleIntersections.length == 2) {
                if (Geometry.isPointInRect(posV2, truePosV2, lineCircleIntersections[0])
                        || Geometry.isPointInRect(posV2, truePosV2, lineCircleIntersections[1])) {
                    double squaredDistanceToPiece = getTruePos().subtract(p.getTruePos()).getSquaredLength();
                    if (squaredDistanceToPiece < furthestSquaredDistanceSoFar) {
                        furthestSquaredDistanceSoFar = squaredDistanceToPiece;
                        furthestPosSoFar = truePosV2
                                .add(posV2.subtract(truePosV2).setLength(Math.sqrt(squaredDistanceToPiece)));
                    }
                }
            }
        }

        Vector2 diff = furthestPosSoFar.subtract(posV2);
        Vector2I diffI = new Vector2I((int) (diff.x + Math.signum(diff.x) * 2),
                (int) (diff.y + Math.signum(diff.y) * 2));
        Vector2I furthestPosSoFarRounded = pos.add(diffI);

        return furthestPosSoFarRounded;
    }

    public Vector2I closestValidPoint(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) { //finds the closest point that can be moved to to a given point.
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

        Vector2I closestPosSoFar = getTruePos();
        double closestSquaredDistanceSoFar = Double.MAX_VALUE;
        Vector2I searchPos;
        double searchSquaredDistance;
        int maxSearch = (int) (Board.boardSizeI.x * 0.11);
        for (int i = 0; i < maxSearch; i++) {
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

        return closestPosSoFar;
    }

    static final int hitboxRadius = (int) (0.35 * Board.boardSizeI.x / 8);

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

    static ImageIcon moveAreaImage = ImageManager.resize(ImageManager.rookMove, Board.boardSizePixels.scale(2));

    public ImageIcon getMoveAreaIcon() {
        return moveAreaImage;
    }

    static ImageIcon hitboxImage = ImageManager.resize(ImageManager.hitbox,
            Board.iPosToPixelPos(new Vector2I(hitboxRadius * 2, hitboxRadius * 2)));

    public ImageIcon getHitboxIcon() {
        return hitboxImage;
    }
}