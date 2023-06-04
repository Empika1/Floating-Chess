package Pieces;

import javax.swing.*;
import java.util.*;
import Images.*;
import Utils.*;
import Game.*;
import Board.*;

public final class King extends Piece {
    public String getPieceName() {
        return "King";
    }

    public PieceType getPieceType() {
        return PieceType.KING;
    }

    final static int halfMoveSideLength = (int) (GameScreen.boardSizeI.x / 8);

    public boolean isInMoveSquare(Vector2I pos) {
        return Geometry.isPointInRect(getTruePos().subtract(new Vector2I(halfMoveSideLength, halfMoveSideLength)),
                getTruePos().add(new Vector2I(halfMoveSideLength, halfMoveSideLength)), pos);
    }

    public boolean canMoveTo(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        if (isOverlappingEdge(pos) || isOverlappingSameColorPiece(pos, whitePieces,
                blackPieces)) {
            return false;
        }

        if (!isInMoveSquare(pos))
            return false;

        ArrayList<Piece> sameColorPieces = color == ChessColor.WHITE ? whitePieces : blackPieces;
        Vector2 posV2 = new Vector2(pos);
        Vector2 truePosV2 = new Vector2(getTruePos());
        for (Piece p : sameColorPieces) {
            Vector2[] lineCircleIntersections = Geometry.lineCircleIntersections(posV2, truePosV2,
                    new Vector2(p.getTruePos()), getHitboxRadius() + p.getHitboxRadius());
            if (lineCircleIntersections.length != 0) {
                if (Geometry.isPointInRect(truePosV2, posV2, lineCircleIntersections[0])
                        || Geometry.isPointInRect(truePosV2, posV2, lineCircleIntersections[1]))
                    return false;
            }
        }

        return true;
    }

    public Vector2I closestValidPoint(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        Vector2I searchPos = new Vector2I();
        double closestLengthSquaredSoFar = Double.MAX_VALUE;
        Vector2I closestPointSoFar = new Vector2I();

        for (searchPos.x = getTruePos().x - halfMoveSideLength; searchPos.x <= getTruePos().x
                + halfMoveSideLength; searchPos.x++) {
            for (searchPos.y = getTruePos().y - halfMoveSideLength; searchPos.y <= getTruePos().y
                    + halfMoveSideLength; searchPos.y++) {
                if (canMoveTo(searchPos, whitePieces, blackPieces)) {
                    double lengthSquared = searchPos.subtract(pos).getSquaredLength();
                    if (lengthSquared < closestLengthSquaredSoFar) {
                        closestLengthSquaredSoFar = lengthSquared;
                        closestPointSoFar = searchPos.copy();
                    }
                }
            }
        }
        return closestPointSoFar;
    }

    static final int hitboxRadius = (int) (0.35 * GameScreen.boardSizeI.x / 8);

    public int getHitboxRadius() {
        return hitboxRadius;
    }

    static final int materialValue = Integer.MAX_VALUE;

    public int getMaterialValue() {
        return materialValue;
    }

    static ImageIcon blackImage = ImageManager.resize(ImageManager.bk, pieceSizePixels);
    static ImageIcon whiteImage = ImageManager.resize(ImageManager.wk, pieceSizePixels);

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

    static ImageIcon moveAreaImage = ImageManager.resize(ImageManager.kingMove, Board.boardSizePixels.scale(0.25));

    public ImageIcon getMoveAreaIcon() {
        return moveAreaImage;
    }

    static ImageIcon hitboxImage = ImageManager.resize(ImageManager.hitbox,
            Board.boardPosToPanelPos(new Vector2I(hitboxRadius * 2, hitboxRadius * 2)));

    public ImageIcon getHitboxIcon() {
        return hitboxImage;
    }
}