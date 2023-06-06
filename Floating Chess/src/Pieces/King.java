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

    Vector2I kingCastlingPointRight = new Vector2I((int) (GameScreen.boardSizeI.x * 6.5 / 8),
            (int) (GameScreen.boardSizeI.x * 7.5 / 8));
    Vector2I rookCastlingPointRight = new Vector2I((int) (GameScreen.boardSizeI.x * 5.5 / 8),
            (int) (GameScreen.boardSizeI.x * 7.5 / 8));
    Vector2I kingCastlingPointLeft = new Vector2I((int) (GameScreen.boardSizeI.x * 2.5 / 8),
            (int) (GameScreen.boardSizeI.x * 7.5 / 8));
    Vector2I rookCastlingPointLeft = new Vector2I((int) (GameScreen.boardSizeI.x * 3.5 / 8),
            (int) (GameScreen.boardSizeI.x * 7.5 / 8));

    Rook getUnmovedRightRook(ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        ArrayList<Piece> sameColorPieces = getColor() == ChessColor.WHITE ? whitePieces : blackPieces;
        Rook rightRook = null;
        for (Piece p : sameColorPieces) {
            if (!p.getHasMoved() && p.getClass().equals(Rook.class) && p.getTruePos().x > truePos.x) {
                rightRook = (Rook) p;
                break;
            }
        }
        return rightRook;
    }

    boolean canCastleRight(ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        if (getHasMoved())
            return false;
        Rook rightRook = getUnmovedRightRook(whitePieces, blackPieces);
        if (rightRook == null)
            return false;

        for (Piece p : whitePieces) {
            if (p.equals(rightRook))
                continue;
            Vector2[] lineCircleIntersections = Geometry.lineCircleIntersections(new Vector2(getTruePos()),
                    new Vector2(rightRook.getTruePos()),
                    new Vector2(p.getTruePos()), getHitboxRadius() + p.getHitboxRadius());
            if (lineCircleIntersections.length != 0) {
                if (Geometry.isPointInRect(new Vector2(getTruePos()), new Vector2(rightRook.getTruePos()),
                        lineCircleIntersections[0])
                        || Geometry.isPointInRect(new Vector2(getTruePos()), new Vector2(rightRook.getTruePos()),
                                lineCircleIntersections[1])) {
                    System.out.println(lineCircleIntersections[0]);
                    return false;
                }
            }
        }
        for (Piece p : blackPieces) {
            if (p.equals(rightRook))
                continue;
            Vector2[] lineCircleIntersections = Geometry.lineCircleIntersections(new Vector2(getTruePos()),
                    new Vector2(rightRook.getTruePos()),
                    new Vector2(p.getTruePos()), getHitboxRadius() + p.getHitboxRadius());
            if (lineCircleIntersections.length != 0) {
                if (Geometry.isPointInRect(new Vector2(getTruePos()), new Vector2(rightRook.getTruePos()),
                        lineCircleIntersections[0])
                        || Geometry.isPointInRect(new Vector2(getTruePos()), new Vector2(rightRook.getTruePos()),
                                lineCircleIntersections[1])) {
                    return false;
                }
            }
        }
        return true;
    }

    Rook getUnmovedLeftRook(ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        ArrayList<Piece> sameColorPieces = getColor() == ChessColor.WHITE ? whitePieces : blackPieces;
        Rook rightRook = null;
        for (Piece p : sameColorPieces) {
            if (!p.getHasMoved() && p.getClass().equals(Rook.class) && p.getTruePos().x < truePos.x) {
                rightRook = (Rook) p;
                break;
            }
        }
        return rightRook;
    }
    
    boolean canCastleLeft(ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        if (getHasMoved())
            return false;
        Rook leftRook = getUnmovedLeftRook(whitePieces, blackPieces);
        if (leftRook == null)
            return false;

        for (Piece p : whitePieces) {
            if (p.equals(leftRook))
                continue;
            Vector2[] lineCircleIntersections = Geometry.lineCircleIntersections(new Vector2(getTruePos()),
                    new Vector2(leftRook.getTruePos()),
                    new Vector2(p.getTruePos()), p.getHitboxRadius());
            if (lineCircleIntersections.length != 0) {
                if (Geometry.isPointInRect(new Vector2(getTruePos()), new Vector2(p.getTruePos()),
                        lineCircleIntersections[0])
                        || Geometry.isPointInRect(new Vector2(getTruePos()), new Vector2(p.getTruePos()),
                                lineCircleIntersections[1])) {
                    return false;
                }
            }
        }
        for (Piece p : blackPieces) {
            if (p.equals(leftRook))
                continue;
            Vector2[] lineCircleIntersections = Geometry.lineCircleIntersections(new Vector2(getTruePos()),
                    new Vector2(leftRook.getTruePos()),
                    new Vector2(p.getTruePos()), p.getHitboxRadius());
            if (lineCircleIntersections.length != 0) {
                if (Geometry.isPointInRect(new Vector2(getTruePos()), new Vector2(p.getTruePos()),
                        lineCircleIntersections[0])
                        || Geometry.isPointInRect(new Vector2(getTruePos()), new Vector2(p.getTruePos()),
                                lineCircleIntersections[1])) {
                    return false;
                }
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

        if (getColor() == ChessColor.BLACK) {
            kingCastlingPointRight.y = (int) (GameScreen.boardSizeI.x * 0.5 / 8);
            rookCastlingPointRight.y = (int) (GameScreen.boardSizeI.x * 0.5 / 8);
            kingCastlingPointLeft.y = (int) (GameScreen.boardSizeI.x * 0.5 / 8);
            rookCastlingPointLeft.y = (int) (GameScreen.boardSizeI.x * 0.5 / 8);
        } else {
            kingCastlingPointRight.y = (int) (GameScreen.boardSizeI.x * 7.5 / 8);
            rookCastlingPointRight.y = (int) (GameScreen.boardSizeI.x * 7.5 / 8);
            kingCastlingPointLeft.y = (int) (GameScreen.boardSizeI.x * 7.5 / 8);
            rookCastlingPointLeft.y = (int) (GameScreen.boardSizeI.x * 7.5 / 8);
        }

        if(canCastleRight(whitePieces, blackPieces)) {
            double squaredDistanceToCastlingPoint = pos.subtract(kingCastlingPointRight).getSquaredLength();
            if(squaredDistanceToCastlingPoint < closestLengthSquaredSoFar) {
                getUnmovedRightRook(whitePieces, blackPieces).castle();
                return kingCastlingPointRight;
            }
        }

        if(canCastleLeft(whitePieces, blackPieces)) {
            double squaredDistanceToCastlingPoint = pos.subtract(kingCastlingPointLeft).getSquaredLength();
            if(squaredDistanceToCastlingPoint < closestLengthSquaredSoFar) {
                getUnmovedLeftRook(whitePieces, blackPieces).castle();
                return kingCastlingPointLeft;
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