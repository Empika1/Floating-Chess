package Pieces;

import javax.swing.*;

import java.util.*;
import Images.*;
import Utils.*;
import Game.*;
import Board.*;

public final class Pawn extends Piece {
    static final String pieceName = "Pawn";

    public String getPieceName() {
        return pieceName;
    }

    static final int moveLength = Game.boardSizeI.x / 8;
    static final int moveLengthDiagonal = moveLength;

    public boolean canMoveTo(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        if (isOverlappingEdge(pos) || isOverlappingSameColorPiece(pos, whitePieces,
                blackPieces))
            return false;

        final int moveLengthScaled = (hasMoved ? moveLength : moveLength * 2);

        Vector2I posRelative = new Vector2I(pos.x, 0);
        Vector2I truePosRelative = new Vector2I(getTruePos().x, 0);
        if (getColor() == ChessColor.WHITE) {
            posRelative.y = pos.y;
            truePosRelative.y = getTruePos().y;
        } else {
            posRelative.y = Game.boardSizeI.y - pos.y;
            truePosRelative.y = Game.boardSizeI.y - getTruePos().y;
        }

        if (posRelative.x == truePosRelative.x && posRelative.y <= truePosRelative.y
                && posRelative.y >= truePosRelative.y - moveLengthScaled
                && !isOverlappingOppositeColorPiece(pos, whitePieces, blackPieces)) {
            for (Piece p : whitePieces) {
                Vector2[] lineCircleIntersections = Geometry.lineCircleIntersections(new Vector2(pos),
                        new Vector2(getTruePos()), new Vector2(p.getTruePos()),
                        getHitboxRadius() + p.getHitboxRadius());
                for (Vector2 v : lineCircleIntersections) {
                    if (Geometry.isPointInRect(new Vector2(pos), new Vector2(getTruePos()), v))
                        return false;
                }
            }
            for (Piece p : blackPieces) {
                Vector2[] lineCircleIntersections = Geometry.lineCircleIntersections(new Vector2(pos),
                        new Vector2(getTruePos()), new Vector2(p.getTruePos()),
                        getHitboxRadius() + p.getHitboxRadius());
                for (Vector2 v : lineCircleIntersections) {
                    if (Geometry.isPointInRect(new Vector2(pos), new Vector2(getTruePos()), v))
                        return false;
                }
            }
            return true;
        }

        if (Math.abs(posRelative.x - truePosRelative.x) == truePosRelative.y - posRelative.y
                && posRelative.y <= truePosRelative.y
                && posRelative.y >= truePosRelative.y - moveLengthDiagonal
                && isOverlappingOppositeColorPiece(pos, whitePieces, blackPieces)) {
            ArrayList<Piece> sameColorPieces = color == ChessColor.WHITE ? whitePieces : blackPieces;
            for (Piece p : sameColorPieces) {
                Vector2[] lineCircleIntersections = Geometry.lineCircleIntersections(new Vector2(pos),
                        new Vector2(getTruePos()), new Vector2(p.getTruePos()),
                        getHitboxRadius() + p.getHitboxRadius());
                for (Vector2 v : lineCircleIntersections) {
                    if (Geometry.isPointInRect(new Vector2(pos), new Vector2(getTruePos()), v))
                        return false;
                }
            }

            ArrayList<Piece> oppositeColorPieces = color == ChessColor.WHITE ? blackPieces : whitePieces;
            for (Piece p : oppositeColorPieces) {
                Vector2[] lineCircleIntersections = Geometry.lineCircleIntersections(new Vector2(pos),
                        new Vector2(getTruePos()), new Vector2(p.getTruePos()),
                        getHitboxRadius() + p.getHitboxRadius());
                for (Vector2 v : lineCircleIntersections) {
                    if (Geometry.isPointInRect(new Vector2(pos), new Vector2(getTruePos()), v)
                            && !hitboxOverlapsHitbox(pos, p))
                        return false;
                }
            }
            return true;
        }

        return false;
    }

    public Vector2I closestValidPoint(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        int moveLengthScaled = (hasMoved ? moveLength : moveLength * 2);

        Vector2I posRelative = new Vector2I(pos.x, 0);
        Vector2I truePosRelative = new Vector2I(getTruePos().x, 0);
        if (getColor() == ChessColor.WHITE) {
            posRelative.y = pos.y;
            truePosRelative.y = getTruePos().y;
        } else {
            posRelative.y = Game.boardSizeI.y - pos.y;
            truePosRelative.y = Game.boardSizeI.y - getTruePos().y;
        }

        Vector2I searchStartPos = new Vector2I(truePosRelative.x, 0);
        Vector2I searchPos = searchStartPos.copy();
        Vector2I searchPosAbsolute = searchStartPos.copy();

        Vector2I foundPos1;
        double foundLengthSquared1 = Double.MAX_VALUE;
        if (posRelative.y < truePosRelative.y - moveLengthScaled)
            searchStartPos.y = truePosRelative.y - moveLengthScaled;
        if (posRelative.y > truePosRelative.y)
            searchStartPos.y = truePosRelative.y;
        else
            searchStartPos.y = posRelative.y;

        for (int i = 0;; i++) {
            searchPos.y = searchStartPos.y + i;
            if (getColor() == ChessColor.WHITE)
                searchPosAbsolute.y = searchPos.y;
            else
                searchPosAbsolute.y = Game.boardSizeI.y - searchPos.y;

            if (searchPos.y >= truePosRelative.y) {
                foundPos1 = getTruePos();
                foundLengthSquared1 = pos.subtract(getTruePos()).getSquaredLength();
                break;
            }

            if (canMoveTo(searchPosAbsolute, whitePieces, blackPieces)) {
                foundPos1 = searchPosAbsolute.copy();
                foundLengthSquared1 = pos.subtract(searchPosAbsolute).getSquaredLength();
                break;
            }

            searchPos.y = searchStartPos.y - i;
            if (getColor() == ChessColor.WHITE)
                searchPosAbsolute.y = searchPos.y;
            else
                searchPosAbsolute.y = Game.boardSizeI.y - searchPos.y;

            if (canMoveTo(searchPosAbsolute, whitePieces, blackPieces)) {
                foundPos1 = searchPosAbsolute.copy();
                foundLengthSquared1 = pos.subtract(searchPosAbsolute).getSquaredLength();
                break;
            }
        }

        Vector2I foundPos2;
        double foundLengthSquared2 = Double.MAX_VALUE;
        Vector2I topRightIntersection = new Vector2I((truePosRelative.y - posRelative.y) + truePosRelative.x,
                posRelative.y);
        Vector2I bottomLeftIntersection = new Vector2I(posRelative.x,
                truePosRelative.y - (posRelative.x - truePosRelative.x));
        searchStartPos.x = (topRightIntersection.x + bottomLeftIntersection.x) / 2;
        searchStartPos.y = (topRightIntersection.y + bottomLeftIntersection.y) / 2;
        for (int i = 0;; i++) {
            searchPos.x = searchStartPos.x + i;
            searchPos.y = searchStartPos.y - i;
            if (getColor() == ChessColor.WHITE)
                searchPosAbsolute = searchPos;
            else
                searchPosAbsolute = new Vector2I(searchPos.x, Game.boardSizeI.y - searchPos.y);

            if (canMoveTo(searchPosAbsolute, whitePieces, blackPieces)) {
                foundPos2 = searchPosAbsolute.copy();
                foundLengthSquared2 = pos.subtract(searchPosAbsolute).getSquaredLength();
                break;
            }

            searchPos.x = searchStartPos.x - i;
            searchPos.y = searchStartPos.y + i;
            if (getColor() == ChessColor.WHITE)
                searchPosAbsolute = searchPos;
            else
                searchPosAbsolute = new Vector2I(searchPos.x, Game.boardSizeI.y - searchPos.y);

            if (searchPos.y >= truePosRelative.y) {
                foundPos2 = getTruePos();
                foundLengthSquared2 = pos.subtract(getTruePos()).getSquaredLength();
                break;
            }

            if (canMoveTo(searchPosAbsolute, whitePieces, blackPieces)) {
                foundPos2 = searchPosAbsolute.copy();
                foundLengthSquared2 = pos.subtract(searchPosAbsolute).getSquaredLength();
                break;
            }
        }

        Vector2I foundPos3;
        double foundLengthSquared3 = Double.MAX_VALUE;
        Vector2I topLeftIntersection = new Vector2I(truePosRelative.x - (truePosRelative.y - posRelative.y),
                posRelative.y);
        Vector2I bottomRightIntersection = new Vector2I(posRelative.x,
                truePosRelative.y + (posRelative.x - truePosRelative.x));
        searchStartPos.x = (topLeftIntersection.x + bottomRightIntersection.x) / 2;
        searchStartPos.y = (int) Math.ceil((topLeftIntersection.y + bottomRightIntersection.y) / 2.0);
        for (int i = 0;; i++) {
            searchPos.x = searchStartPos.x - i;
            searchPos.y = searchStartPos.y - i;
            if (getColor() == ChessColor.WHITE)
                searchPosAbsolute = searchPos;
            else
                searchPosAbsolute = new Vector2I(searchPos.x, Game.boardSizeI.y - searchPos.y);

            if (canMoveTo(searchPosAbsolute, whitePieces, blackPieces)) {
                foundPos3 = searchPosAbsolute.copy();
                foundLengthSquared3 = pos.subtract(searchPosAbsolute).getSquaredLength();
                break;
            }

            searchPos.x = searchStartPos.x + i;
            searchPos.y = searchStartPos.y + i;
            if (getColor() == ChessColor.WHITE)
                searchPosAbsolute = searchPos;
            else
                searchPosAbsolute = new Vector2I(searchPos.x, Game.boardSizeI.y - searchPos.y);

            if (searchPos.y >= truePosRelative.y) {
                foundPos3 = getTruePos();
                foundLengthSquared3 = pos.subtract(getTruePos()).getSquaredLength();
                break;
            }

            if (canMoveTo(searchPosAbsolute, whitePieces, blackPieces)) {
                foundPos3 = searchPosAbsolute.copy();
                foundLengthSquared3 = pos.subtract(searchPosAbsolute).getSquaredLength();
                break;
            }
        }

        double minLengthSquared = Math.min(foundLengthSquared1, Math.min(foundLengthSquared2, foundLengthSquared3));
        if (minLengthSquared == foundLengthSquared1) {
            return foundPos1;
        }
        if (minLengthSquared == foundLengthSquared2) {
            return foundPos2;
        }
        if (minLengthSquared == foundLengthSquared3) {
            return foundPos3;
        }
        return getTruePos();
    }

    static final int hitboxRadius = (int) (0.35 * Game.boardSizeI.x / 8);

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

    static ImageIcon moveAreaImageNormalWhite = ImageManager.resize(ImageManager.pawnMoveNormalWhite,
            Board.boardSizePixels);
    static ImageIcon moveAreaImageFirstWhite = ImageManager.resize(ImageManager.pawnMoveFirstWhite,
            Board.boardSizePixels);
    static ImageIcon moveAreaImageNormalBlack = ImageManager.resize(ImageManager.pawnMoveNormalBlack,
            Board.boardSizePixels);
    static ImageIcon moveAreaImageFirstBlack = ImageManager.resize(ImageManager.pawnMoveFirstBlack,
            Board.boardSizePixels);

    public ImageIcon getMoveAreaIcon() {
        if (hasMoved) {
            if (getColor() == ChessColor.WHITE)
                return moveAreaImageNormalWhite;
            else
                return moveAreaImageNormalBlack;
        } else {
            if (getColor() == ChessColor.WHITE)
                return moveAreaImageFirstWhite;
            else
                return moveAreaImageFirstBlack;
        }
    }

    static ImageIcon hitboxImage = ImageManager.resize(ImageManager.hitbox,
            Board.boardPosToPanelPos(new Vector2I(hitboxRadius * 2, hitboxRadius * 2)));

    public ImageIcon getHitboxIcon() {
        return hitboxImage;
    }
}