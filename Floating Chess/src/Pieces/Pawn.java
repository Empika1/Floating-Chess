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

    int moveLength = Game.boardSizeI.x / 8;

    public boolean canMoveTo(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        if (isOverlappingEdge(pos) || isOverlappingSameColorPiece(pos, whitePieces,
                blackPieces)) {
            return false;
        }

        return pos.x == getTruePos().x && pos.y <= getTruePos().y && pos.y >= getTruePos().y - moveLength;
    }

    public Vector2I closestValidPoint(Vector2I pos, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        Vector2I searchStartPos = new Vector2I(getTruePos().x, 0);
        if (pos.y < getTruePos().y - moveLength)
            searchStartPos.y = getTruePos().y - moveLength;
        if (pos.y > getTruePos().y)
            searchStartPos.y = getTruePos().y;
        else
            searchStartPos.y = pos.y;

        Vector2I searchPos = searchStartPos.copy();
        for (int i = 0;; i++) {
            searchPos.y = searchStartPos.y + i;
            if (canMoveTo(searchPos, whitePieces, blackPieces))
                return searchPos;

            searchPos.y = searchStartPos.y - i;
            if (canMoveTo(searchPos, whitePieces, blackPieces))
                return searchPos;
        }
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