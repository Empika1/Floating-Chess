//Its a pawn. Yup
//Some of the things in this file are very complicated and hard to explain so I will not comment all the details. Sorry.
//Also, many of the things are explained in Piece.java already so I will not explain them twice
package Pieces;

import javax.swing.*;
import java.util.*;
import Images.*;
import Utils.*;
import Board.*;

public final class Pawn extends Piece {
    public String getPieceName() {
        return "Pawn";
    }

    public PieceType getPieceType() {
        return PieceType.PAWN;
    }

    static final int moveLength = Board.boardSizeI.x / 8; //the length that a pawn is able to move forward
    static final int moveLengthDiagonal = moveLength; //the length that a pawn is able to move forward when taking diagonally

    public Vector2I closestValidPoint(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) { //gets the closest point to a given input point that the pawn is able to move to
        int moveLengthScaled = (hasMoved ? moveLength : moveLength * 2);

        Vector2I searchDir = color == ChessColor.WHITE ? new Vector2I(0, -1) : new Vector2I(0, 1);
        Vector2I searchPos = getTruePos().copy();
        Vector2I foundPos1 = getTruePos().copy();
        double foundSquaredDistance1 = getTruePos().subtract(pos).getSquaredLength();
        while (true) {
            searchPos = searchPos.add(searchDir);
            if (color == ChessColor.WHITE) {
                if (searchPos.y < getTruePos().y - moveLengthScaled)
                    break;
            } else {
                if (searchPos.y > getTruePos().y + moveLengthScaled)
                    break;
            }

            if (isOverlappingEdge(searchPos) || isOverlappingOppositeColorPiece(searchPos, whitePieces, blackPieces)
                    || isOverlappingSameColorPiece(searchPos, whitePieces, blackPieces))
                break;

            double currentSquaredDistance = pos.subtract(searchPos).getSquaredLength();
            if (currentSquaredDistance < foundSquaredDistance1) {
                foundPos1 = searchPos.copy();
                foundSquaredDistance1 = currentSquaredDistance;
            }
        }

        searchDir = color == ChessColor.WHITE ? new Vector2I(-1, -1) : new Vector2I(-1, 1);
        searchPos = getTruePos().copy();
        Vector2I foundPos2 = getTruePos().copy();
        double foundSquaredDistance2 = getTruePos().subtract(pos).getSquaredLength();
        HashSet<Piece> oppositeColorPiecesThatHaveBeenOverlapped2 = new HashSet<Piece>();
        search2: while (true) {
            searchPos = searchPos.add(searchDir);
            if (color == ChessColor.WHITE) {
                if (searchPos.y < getTruePos().y - moveLengthDiagonal)
                    break;
            } else {
                if (searchPos.y > getTruePos().y + moveLengthDiagonal)
                    break;
            }

            if (isOverlappingEdge(searchPos) || isOverlappingSameColorPiece(searchPos, whitePieces, blackPieces))
                break;

            for (Piece p : oppositeColorPiecesThatHaveBeenOverlapped2) {
                if (!oppositeColorPiecesOverlapping(searchPos, whitePieces, blackPieces).contains(p))
                    break search2;
            }

            if (!isOverlappingOppositeColorPiece(searchPos, whitePieces, blackPieces))
                continue;

            oppositeColorPiecesThatHaveBeenOverlapped2
                .addAll(oppositeColorPiecesOverlapping(searchPos, whitePieces, blackPieces));

            double currentSquaredDistance = pos.subtract(searchPos).getSquaredLength();
            if (currentSquaredDistance < foundSquaredDistance2) {
                foundPos2 = searchPos.copy();
                foundSquaredDistance2 = currentSquaredDistance;
            }
        }

        searchDir = color == ChessColor.WHITE ? new Vector2I(1, -1) : new Vector2I(1, 1);
        searchPos = getTruePos().copy();
        Vector2I foundPos3 = getTruePos().copy();
        double foundSquaredDistance3 = getTruePos().subtract(pos).getSquaredLength();
        HashSet<Piece> oppositeColorPiecesThatHaveBeenOverlapped3 = new HashSet<Piece>();
        search3: while (true) {
            searchPos = searchPos.add(searchDir);
            if (color == ChessColor.WHITE) {
                if (searchPos.y < getTruePos().y - moveLengthDiagonal)
                    break;
            } else {
                if (searchPos.y > getTruePos().y + moveLengthDiagonal)
                    break;
            }

            if (isOverlappingEdge(searchPos) || isOverlappingSameColorPiece(searchPos, whitePieces, blackPieces))
                break;

            for (Piece p : oppositeColorPiecesThatHaveBeenOverlapped3) {
                if (!oppositeColorPiecesOverlapping(searchPos, whitePieces, blackPieces).contains(p))
                    break search3;
            }

            if (!isOverlappingOppositeColorPiece(searchPos, whitePieces, blackPieces))
                continue;

            oppositeColorPiecesThatHaveBeenOverlapped3
                .addAll(oppositeColorPiecesOverlapping(searchPos, whitePieces, blackPieces));

            double currentSquaredDistance = pos.subtract(searchPos).getSquaredLength();
            if (currentSquaredDistance < foundSquaredDistance3) {
                foundPos3 = searchPos.copy();
                foundSquaredDistance3 = currentSquaredDistance;
            }
        }

        double minDistance = Math.min(foundSquaredDistance1, Math.min(foundSquaredDistance2, foundSquaredDistance3));
        if (minDistance == foundSquaredDistance1)
            return foundPos1;
        else if (minDistance == foundSquaredDistance2)
            return foundPos2;
        else
            return foundPos3;
    }

    static final int hitboxRadius = (int) (0.35 * Board.boardSizeI.x / 8);

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

    public ImageIcon getMoveAreaIcon() { //the pawn has vertically reflected move areas depending on if its white or black, and if it has moved already or not
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
            Board.iPosToPixelPos(new Vector2I(hitboxRadius * 2, hitboxRadius * 2)));

    public ImageIcon getHitboxIcon() {
        return hitboxImage;
    }
}