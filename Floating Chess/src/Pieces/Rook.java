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
        if (isOverlappingEdge(pos) || isOverlappingSameColorPiece(pos, whitePieces,
                blackPieces))
            return false;

        if (pos == getTruePos())
            return true;

        if (!isInValidAngle(pos))
            return false;

        Vector2I diff = pos.subtract(getTruePos());
        Vector2I searchDir = new Vector2I();
        if (diff.y > 0 && diff.y > Math.abs(diff.x))
            searchDir = new Vector2I(0, 1);
        if (diff.y < 0 && -diff.y > Math.abs(diff.x))
            searchDir = new Vector2I(0, -1);
        if (diff.x > 0 && diff.x > Math.abs(diff.y))
            searchDir = new Vector2I(1, 0);
        if (diff.x < 0 && -diff.x > Math.abs(diff.y))
            searchDir = new Vector2I(-1, 0);

        Vector2I perpendicularSearchDir1 = new Vector2I(searchDir.y, searchDir.x);
        Vector2I perpendicularSearchDir2 = new Vector2I(-searchDir.y, -searchDir.x);
        Vector2I currentSearchPos = getTruePos();
        HashSet<Piece> piecesThatHaveBeenOverlapped = new HashSet<Piece>();
        for (int i = 0;; i++) {
            currentSearchPos = getTruePos().add(searchDir.scale(i));
            if (isOverlappingSameColorPiece(currentSearchPos, whitePieces, blackPieces)) {
                for (int i2 = 1;; i2++) {
                    currentSearchPos = getTruePos().add(searchDir.scale(i)).add(perpendicularSearchDir1.scale(i2));
                    if (!isInValidAngle(currentSearchPos))
                        return false;
                    if (!isOverlappingSameColorPiece(currentSearchPos, whitePieces, blackPieces))
                        break;
                    currentSearchPos = getTruePos().add(searchDir.scale(i)).add(perpendicularSearchDir2.scale(i2));
                    if (!isOverlappingSameColorPiece(currentSearchPos, whitePieces, blackPieces))
                        break;
                }
            }

            ArrayList<Piece> oppositeColorPiecesOverlapping = oppositeColorPiecesOverlapping(currentSearchPos,
                    whitePieces, blackPieces);
            for (Piece p : piecesThatHaveBeenOverlapped) {
                if (!oppositeColorPiecesOverlapping.contains(p))
                    return false;
            }

            currentSearchPos = getTruePos().add(searchDir.scale(i));
            HashSet<Piece> piecesOverlappedNow = new HashSet<Piece>();
            if (isOverlappingOppositeColorPiece(currentSearchPos, whitePieces, blackPieces)) {
                for (int i2 = 1;; i2++) {
                    currentSearchPos = getTruePos().add(searchDir.scale(i)).add(perpendicularSearchDir1.scale(i2));
                    if (!isInValidAngle(currentSearchPos)) {
                        piecesThatHaveBeenOverlapped.addAll(piecesOverlappedNow);
                        break;
                    }
                    if (isOverlappingOppositeColorPiece(currentSearchPos, whitePieces, blackPieces)) {
                        piecesOverlappedNow
                                .addAll(oppositeColorPiecesOverlapping(currentSearchPos, whitePieces, blackPieces));
                    } else {
                        piecesOverlappedNow.clear();
                        break;
                    }
                    currentSearchPos = getTruePos().add(searchDir.scale(i)).add(perpendicularSearchDir2.scale(i2));
                    if (isOverlappingOppositeColorPiece(currentSearchPos, whitePieces, blackPieces)) {
                        piecesOverlappedNow
                                .addAll(oppositeColorPiecesOverlapping(currentSearchPos, whitePieces, blackPieces));
                    } else {
                        piecesOverlappedNow.clear();
                        break;
                    }
                }
            }

            if (searchDir.x == 0) {
                if (currentSearchPos.y == pos.y)
                    return true;
            } else {
                if (currentSearchPos.x == pos.x)
                    return true;
            }
        }
    }

    public Vector2I closestValidPoint(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        Vector2I closestValidPointTopQuadrant = closestValidPointInQuadrant(pos, whitePieces, blackPieces, new Vector2I(0, -1));
        Vector2I closestValidPointBottomQuadrant = closestValidPointInQuadrant(pos, whitePieces, blackPieces, new Vector2I(0, 1));
        Vector2I closestValidPointRightQuadrant = closestValidPointInQuadrant(pos, whitePieces, blackPieces, new Vector2I(1, 0));
        Vector2I closestValidPointLeftQuadrant = closestValidPointInQuadrant(pos, whitePieces, blackPieces, new Vector2I(-1, 0));
        double closestDistanceTopQuadrant = pos.subtract(closestValidPointTopQuadrant).getLength();
        double closestDistanceBottomQuadrant = pos.subtract(closestValidPointBottomQuadrant).getLength();
        double closestDistanceRightQuadrant = pos.subtract(closestValidPointRightQuadrant).getLength();
        double closestDistanceLeftQuadrant = pos.subtract(closestValidPointLeftQuadrant).getLength();
        double closestDistance = Math.min(closestDistanceTopQuadrant, Math.min(closestDistanceBottomQuadrant, Math.min(closestDistanceLeftQuadrant, closestDistanceRightQuadrant)));
        if(closestDistance == closestDistanceTopQuadrant) return closestValidPointTopQuadrant;
        if(closestDistance == closestDistanceBottomQuadrant) return closestValidPointBottomQuadrant;
        if(closestDistance == closestDistanceRightQuadrant) return closestValidPointRightQuadrant;
        if(closestDistance == closestDistanceLeftQuadrant) return closestValidPointLeftQuadrant;
        return pos;
    }

    public Vector2I closestValidPointInQuadrant(Vector2I pos, ArrayList<Piece> whitePieces,
            ArrayList<Piece> blackPieces, Vector2I searchDir) {
        Vector2I perpendicularSearchDir1 = new Vector2I(searchDir.y, searchDir.x);
        Vector2I perpendicularSearchDir2 = new Vector2I(-searchDir.y, -searchDir.x);
        Vector2I currentSearchPos = getTruePos();
        Vector2I closestPosOnLineSoFar = new Vector2I();
        double closestSquaredDistanceOnLineSoFar = Double.MAX_VALUE;
        HashSet<Piece> piecesThatHaveBeenOverlapped = new HashSet<Piece>();
        searchOnLine: while (true) {
            currentSearchPos = currentSearchPos.add(searchDir);
            double currentSquaredDistance = pos.subtract(currentSearchPos).getSquaredLength();
            if (currentSquaredDistance >= closestSquaredDistanceOnLineSoFar || isOverlappingEdge(currentSearchPos))
                break;

            if (isOverlappingSameColorPiece(currentSearchPos, whitePieces, blackPieces)) {
                Vector2I currentSearchPosGoAround;
                for (int i = 1;; i++) {
                    currentSearchPosGoAround = currentSearchPos.add(perpendicularSearchDir1.scale(i));
                    if (!isInValidAngle(currentSearchPosGoAround))
                        break searchOnLine;
                    if (isOverlappingEdge(currentSearchPosGoAround)) 
                        continue;
                    if (!isOverlappingSameColorPiece(currentSearchPosGoAround, whitePieces, blackPieces))
                        break;
                    currentSearchPosGoAround = currentSearchPos.add(perpendicularSearchDir2.scale(i));
                    if (!isOverlappingSameColorPiece(currentSearchPosGoAround, whitePieces, blackPieces))
                        break;
                }
            }

            ArrayList<Piece> oppositeColorPiecesOverlapping = oppositeColorPiecesOverlapping(currentSearchPos,
                    whitePieces, blackPieces);
            for (Piece p : piecesThatHaveBeenOverlapped) {
                if (!oppositeColorPiecesOverlapping.contains(p))
                    break;
            }

            HashSet<Piece> piecesOverlappedNow = new HashSet<Piece>();
            if (isOverlappingOppositeColorPiece(currentSearchPos, whitePieces, blackPieces)) {
                Vector2I currentSearchPosGoAround;
                for (int i = 1;; i++) {
                    currentSearchPosGoAround = currentSearchPos.add(perpendicularSearchDir1.scale(i));
                    if (!isInValidAngle(currentSearchPosGoAround)) {
                        piecesThatHaveBeenOverlapped.addAll(piecesOverlappedNow);
                        break;
                    }
                    if (isOverlappingEdge(currentSearchPosGoAround)) 
                        continue;
                    if (isOverlappingOppositeColorPiece(currentSearchPosGoAround, whitePieces, blackPieces)) {
                        piecesOverlappedNow
                                .addAll(oppositeColorPiecesOverlapping(currentSearchPosGoAround, whitePieces, blackPieces));
                    } else {
                        piecesOverlappedNow.clear();
                        break;
                    }
                    currentSearchPosGoAround = currentSearchPos.add(perpendicularSearchDir1.scale(i));
                    if (isOverlappingOppositeColorPiece(currentSearchPosGoAround, whitePieces, blackPieces)) {
                        piecesOverlappedNow
                                .addAll(oppositeColorPiecesOverlapping(currentSearchPosGoAround, whitePieces, blackPieces));
                    } else {
                        piecesOverlappedNow.clear();
                        break;
                    }
                }
            }
            
            int i = 0;
            for(Piece p : piecesThatHaveBeenOverlapped) {
                i++;
            }
            System.out.println(i);

            closestSquaredDistanceOnLineSoFar = currentSquaredDistance;
            closestPosOnLineSoFar = currentSearchPos.copy();
        }

        Vector2I closestPosSoFar = new Vector2I();
        double closestSquaredDistanceSoFar = Double.MAX_VALUE;
        for(int i = 1;; i++) {
            currentSearchPos = closestPosOnLineSoFar.add(perpendicularSearchDir1.scale(i));
            if(!isInValidAngle(currentSearchPos))
                return closestPosSoFar;
            double squaredDistance = currentSearchPos.subtract(pos).getLength();
            if(squaredDistance < closestSquaredDistanceSoFar) {
                closestSquaredDistanceSoFar = squaredDistance;
                closestPosSoFar = currentSearchPos.copy();
            }
            currentSearchPos = closestPosOnLineSoFar.add(perpendicularSearchDir2.scale(i));
            squaredDistance = currentSearchPos.subtract(pos).getLength();
            if(squaredDistance < closestSquaredDistanceSoFar) {
                closestSquaredDistanceSoFar = squaredDistance;
                closestPosSoFar = currentSearchPos.copy();
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