package Pieces;

import javax.swing.*;
import java.util.*;
import Images.*;
import Utils.*;
import Game.*;
import Board.*;

public final class Queen extends Piece {
    public String getPieceName() {
        return "Queen";
    }

    public PieceType getPieceType() {
        return PieceType.QUEEN;
    }

    Rook fakeRook = new Rook();
    Bishop fakeBishop = new Bishop();

    public boolean canMoveTo(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        fakeRook.setTruePos(getTruePos(), false);
        fakeRook.setColor(getColor());
        fakeBishop.setTruePos(getTruePos(), false);
        fakeBishop.setColor(getColor());
        return fakeRook.canMoveTo(pos, whitePieces, blackPieces) || fakeBishop.canMoveTo(pos, whitePieces, blackPieces);
    }

    public Vector2I closestValidPoint(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        fakeRook.setTruePos(getTruePos(), false);
        fakeRook.setColor(getColor());
        fakeBishop.setTruePos(getTruePos(), false);
        fakeBishop.setColor(getColor());
        Vector2I rookPoint = fakeRook.closestValidPoint(pos, whitePieces, blackPieces);
        double rookSquaredDistance = pos.subtract(rookPoint).getSquaredLength();
        Vector2I bishopPoint = fakeBishop.closestValidPoint(pos, whitePieces, blackPieces);
        double bishopSquaredDistance = pos.subtract(bishopPoint).getSquaredLength();
        
        if (rookSquaredDistance < bishopSquaredDistance)
            return rookPoint;
        else
            return bishopPoint;
    }

    static final int hitboxRadius = (int) (0.35 * GameScreen.boardSizeI.x / 8);

    public int getHitboxRadius() {
        return hitboxRadius;
    }

    static final int materialValue = 9;

    public int getMaterialValue() {
        return materialValue;
    }

    static ImageIcon blackImage = ImageManager.resize(ImageManager.bq, pieceSizePixels);
    static ImageIcon whiteImage = ImageManager.resize(ImageManager.wq, pieceSizePixels);

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

    static ImageIcon moveAreaImage = ImageManager.resize(ImageManager.queenMove, Board.boardSizePixels.scale(2));

    public ImageIcon getMoveAreaIcon() {
        return moveAreaImage;
    }

    static ImageIcon hitboxImage = ImageManager.resize(ImageManager.hitbox,
            Board.boardPosToPanelPos(new Vector2I(hitboxRadius * 2, hitboxRadius * 2)));

    public ImageIcon getHitboxIcon() {
        return hitboxImage;
    }
}