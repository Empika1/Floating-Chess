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

    public boolean canMoveTo(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        if (isOverlappingEdge(pos))
            return false;

        if (!isInValidAngle(pos))
            return false;

        ArrayList<Piece> sameColorPieces = color == ChessColor.WHITE ? whitePieces : blackPieces;
        for (Piece p : sameColorPieces) {
            Vector2 posV2 = new Vector2(pos);
            Vector2 truePosV2 = new Vector2(getTruePos());
            Vector2[] lineCircleIntersections = Geometry.lineCircleIntersections(posV2, truePosV2,
                    new Vector2(p.getTruePos()), (double) (p.getHitboxRadius() + getHitboxRadius()));
            for (Vector2 intersectionPoint : lineCircleIntersections) {
                System.out.println(p.getPieceName() + " " + p.getColor());
                if (Geometry.isPointInRect(posV2, truePosV2, intersectionPoint))
                    return false;
            }
        }

        ArrayList<Piece> oppositeColorPieces = color == ChessColor.WHITE ? blackPieces : whitePieces;
        for (Piece p : oppositeColorPieces) {
            Vector2 posV2 = new Vector2(pos);
            Vector2 truePosV2 = new Vector2(getTruePos());
            Vector2[] lineCircleIntersections = Geometry.lineCircleIntersections(posV2, truePosV2,
                    new Vector2(p.getTruePos()), (double) (p.getHitboxRadius() + getHitboxRadius()));
            if (lineCircleIntersections.length == 1) {
                if (Geometry.isPointInRect(posV2, truePosV2, lineCircleIntersections[0])) {
                    if (!hitboxOverlapsHitbox(getTruePos(), p))
                        return false;
                }
            } else if (lineCircleIntersections.length == 2) {
                boolean point1Invalid = false;
                boolean point2Invalid = false;
                if (Geometry.isPointInRect(posV2, truePosV2, lineCircleIntersections[0])) {
                    if (!hitboxOverlapsHitbox(getTruePos(), p))
                        point1Invalid = true;
                }
                if (Geometry.isPointInRect(posV2, truePosV2, lineCircleIntersections[1])) {
                    if (!hitboxOverlapsHitbox(getTruePos(), p))
                        point2Invalid = true;
                }
                if (point1Invalid && point2Invalid)
                    return false;
            }
        }

        return true;
    }

    Vector2I oldValidPos = new Vector2I();

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

        Vector2I searchPos;
        Vector2I closestPosSoFar = null;
        double closestDistanceSoFar = Double.MAX_VALUE;
        double searchDistance;
        for (int i = 0;; i++) {
            searchPos = searchStartPos.add(searchDir1.scale(i));
            if (!isInValidAngle(searchPos))
                return closestPosSoFar;
                
            searchDistance = pos.subtract(searchPos).getLength();
            if (searchDistance < closestDistanceSoFar) {
                closestDistanceSoFar = searchDistance;
                closestPosSoFar = searchPos;
            }

            searchPos = searchStartPos.add(searchDir2.scale(i));
            searchDistance = pos.subtract(searchPos).getLength();
            if (searchDistance < closestDistanceSoFar) {
                closestDistanceSoFar = searchDistance;
                closestPosSoFar = searchPos;
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