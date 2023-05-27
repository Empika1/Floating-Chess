package Pieces;

import javax.swing.*;
import java.util.*;
import Images.*;
import Utils.*;
import Game.*;

public final class Pawn extends Piece {
    static final String pieceName = "Pawn";

    public String getPieceName() {
        return pieceName;
    }

    static final int moveLength = Game.boardSizeI.x / 8;
    static final int moveLengthDiagonal = (int) (moveLength / Math.sqrt(2));

    public boolean canMoveTo(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        if (isOverlappingEdge(pos) || isOverlappingSameColorPiece(pos, whitePieces,
                blackPieces))
            return false;

        if (pos.x == getTruePos().x && pos.y <= getTruePos().y && pos.y >= getTruePos().y - moveLength)
            return true;

        if (Math.abs(pos.x - getTruePos().x) == getTruePos().y - pos.y && pos.y <= getTruePos().y
                && pos.y >= getTruePos().y - moveLengthDiagonal && isOverlappingOppositeColorPiece(pos, whitePieces, blackPieces))
            return true;

        return false;
    }

    public Vector2I closestValidPoint(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        Vector2I searchStartPos = new Vector2I(getTruePos().x, 0);
        Vector2I searchPos = searchStartPos.copy();

        Vector2I foundPos1;
        double foundLengthSquared1 = Double.MAX_VALUE;
        if (pos.y < getTruePos().y - moveLength)
            searchStartPos.y = getTruePos().y - moveLength;
        if (pos.y > getTruePos().y)
            searchStartPos.y = getTruePos().y;
        else
            searchStartPos.y = pos.y;

        for (int i = 0;; i++) {
            searchPos.y = searchStartPos.y + i;
            if (canMoveTo(searchPos, whitePieces, blackPieces)) {
                foundPos1 = searchPos.copy();
                foundLengthSquared1 = pos.subtract(searchPos).getSquaredLength();
                break;
            }

            searchPos.y = searchStartPos.y - i;
            if (canMoveTo(searchPos, whitePieces, blackPieces)) {
                foundPos1 = searchPos.copy();
                foundLengthSquared1 = pos.subtract(searchPos).getSquaredLength();
                break;
            }
        }

        Vector2I foundPos2;
        double foundLengthSquared2 = Double.MAX_VALUE;
        Vector2I topRightIntersection = new Vector2I((getTruePos().y - pos.y) + getTruePos().x, pos.y);
        Vector2I bottomLeftIntersection = new Vector2I(pos.x, getTruePos().y - (pos.x - getTruePos().x));
        searchStartPos.x = (topRightIntersection.x + bottomLeftIntersection.x) / 2;
        searchStartPos.y = (topRightIntersection.y + bottomLeftIntersection.y) / 2;
        for (int i = 0;; i++) {
            searchPos.x = searchStartPos.x + i;
            searchPos.y = searchStartPos.y - i;
            if (canMoveTo(searchPos, whitePieces, blackPieces)) {
                foundPos2 = searchPos.copy();
                foundLengthSquared2 = pos.subtract(searchPos).getSquaredLength();
                break;
            }

            searchPos.x = searchStartPos.x - i;
            searchPos.y = searchStartPos.y + i;
            if (canMoveTo(searchPos, whitePieces, blackPieces)) {
                foundPos2 = searchPos.copy();
                foundLengthSquared2 = pos.subtract(searchPos).getSquaredLength();
                break;
            }
        }

        Vector2I foundPos3;
        double foundLengthSquared3 = Double.MAX_VALUE;
        Vector2I topLeftIntersection = new Vector2I(getTruePos().x - (getTruePos().y - pos.y), pos.y);
        Vector2I bottomRightIntersection = new Vector2I(pos.x, getTruePos().y + (pos.x - getTruePos().x));
        searchStartPos.x = (topLeftIntersection.x + bottomRightIntersection.x) / 2;
        searchStartPos.y = (int) Math.ceil((topLeftIntersection.y + bottomRightIntersection.y) / 2.0);
        System.out.println(searchStartPos);
        System.out.println(searchStartPos);
        for (int i = 0;; i++) {
            searchPos.x = searchStartPos.x - i;
            searchPos.y = searchStartPos.y - i;
            if (canMoveTo(searchPos, whitePieces, blackPieces)) {
                foundPos3 = searchPos.copy();
                foundLengthSquared3 = pos.subtract(searchPos).getSquaredLength();
                break;
            }

            searchPos.x = searchStartPos.x + i;
            searchPos.y = searchStartPos.y + i;
            if (canMoveTo(searchPos, whitePieces, blackPieces)) {
                foundPos3 = searchPos.copy();
                foundLengthSquared3 = pos.subtract(searchPos).getSquaredLength();
                break;
            }
        }

        double minLengthSquared = Math.min(foundLengthSquared1, Math.min(foundLengthSquared2, foundLengthSquared3));
        if (minLengthSquared == foundLengthSquared1)
            return foundPos1;
        if (minLengthSquared == foundLengthSquared2)
            return foundPos2;
        if (minLengthSquared == foundLengthSquared3)
            return foundPos3;
        return null;
    }

    static final int hitboxRadius = (int) (0.375 * Game.boardSizeI.x / 8);

    public int getHitboxRadius() {
        return hitboxRadius;
    }

    static final int materialValue = 1;

    public int getMaterialValue() {
        return materialValue;
    }

    static ImageIcon blackImage = ImageManager.resize(ImageManager.bp, pieceSizePixels);
    static ImageIcon whiteImage = ImageManager.resize(ImageManager.wp, pieceSizePixels);

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