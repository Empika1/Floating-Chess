package Board;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import Images.*;
import Utils.*;
import Pieces.*;

public class CapturedPieces extends JPanel {
    static final Vector2I iconSizePixels = new Vector2I(20, 20);
    static final int overlappingPiecesSpacing = 6;
    static final int nonOverlappingPiecesSpacing = 14;

    static final Vector2I capturedPiecesSizePixels = new Vector2I(16 * overlappingPiecesSpacing + 5 * nonOverlappingPiecesSpacing + iconSizePixels.x, 20);

    ChessColor color;
    ImageIcon pawn;
    ImageIcon knight;
    ImageIcon bishop;
    ImageIcon rook;
    ImageIcon queen;
    ImageIcon king;
    ArrayList<Piece> capturedPieces;

    public CapturedPieces(ChessColor col, Board board) {
        this.board = board;

        color = col;
        if (color == ChessColor.WHITE) {
            capturedPieces = board.whitePiecesCaptured;
            pawn = wp;
            knight = wn;
            bishop = wb;
            rook = wr;
            queen = wq;
            king = wk;
        } else {
            capturedPieces = board.blackPiecesCaptured;
            pawn = bp;
            knight = bn;
            bishop = bb;
            rook = br;
            queen = bq;
            king = bk;
        }
        setPreferredSize(new Dimension(capturedPiecesSizePixels.x, capturedPiecesSizePixels.y));
        setVisible(true);
    }

    static final ImageIcon bp = ImageManager.resize(ImageManager.bp, iconSizePixels);
    static final ImageIcon wp = ImageManager.resize(ImageManager.wp, iconSizePixels);

    static final ImageIcon bn = ImageManager.resize(ImageManager.bn, iconSizePixels);
    static final ImageIcon wn = ImageManager.resize(ImageManager.wn, iconSizePixels);

    static final ImageIcon bb = ImageManager.resize(ImageManager.bb, iconSizePixels);
    static final ImageIcon wb = ImageManager.resize(ImageManager.wb, iconSizePixels);

    static final ImageIcon br = ImageManager.resize(ImageManager.br, iconSizePixels);
    static final ImageIcon wr = ImageManager.resize(ImageManager.wr, iconSizePixels);

    static final ImageIcon bq = ImageManager.resize(ImageManager.bq, iconSizePixels);
    static final ImageIcon wq = ImageManager.resize(ImageManager.wq, iconSizePixels);

    static final ImageIcon bk = ImageManager.resize(ImageManager.bk, iconSizePixels);
    static final ImageIcon wk = ImageManager.resize(ImageManager.wk, iconSizePixels);

    Board board = null;

    public void draw(Board b) {
        board = b;
        validate();
        repaint();
    }

    BufferedImage offScreenBuffer = new BufferedImage(capturedPiecesSizePixels.x, capturedPiecesSizePixels.y,
            BufferedImage.TYPE_INT_ARGB);

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics bbg = offScreenBuffer.getGraphics();

        // Clear the off-screen buffer
        bbg.setColor(new Color(255, 255, 255, 255));
        bbg.fillRect(0, 0, getSize().width + 100, getSize().height + 100);

        // draw objects to bbg
        int capturedPawnNum = 0;
        int capturedKnightNum = 0;
        int capturedBishopNum = 0;
        int capturedRookNum = 0;
        int capturedQueenNum = 0;
        int capturedKingNum = 0;
        for (Piece p : capturedPieces) {
            if (p.getClass().equals(Pawn.class))
                capturedPawnNum++;
            else if (p.getClass().equals(Knight.class))
                capturedKnightNum++;
            else if (p.getClass().equals(Bishop.class))
                capturedBishopNum++;
            else if (p.getClass().equals(Rook.class))
                capturedRookNum++;
            else if (p.getClass().equals(Queen.class))
                capturedQueenNum++;
            else if (p.getClass().equals(King.class))
                capturedKingNum++;
        }

        int currentDrawPos = -nonOverlappingPiecesSpacing;
        if (capturedPawnNum != 0)
            currentDrawPos += nonOverlappingPiecesSpacing;
        for (int i = 0; i < capturedPawnNum; i++) {
            pawn.paintIcon(this, bbg, currentDrawPos, 0);
            currentDrawPos += overlappingPiecesSpacing;
        }
        if (capturedKnightNum != 0)
            currentDrawPos += nonOverlappingPiecesSpacing;
        for (int i = 0; i < capturedKnightNum; i++) {
            knight.paintIcon(this, bbg, currentDrawPos, 0);
            currentDrawPos += overlappingPiecesSpacing;
        }
        if (capturedBishopNum != 0)
            currentDrawPos += nonOverlappingPiecesSpacing;
        for (int i = 0; i < capturedBishopNum; i++) {
            bishop.paintIcon(this, bbg, currentDrawPos, 0);
            currentDrawPos += overlappingPiecesSpacing;
        }
        if (capturedRookNum != 0)
            currentDrawPos += nonOverlappingPiecesSpacing;
        for (int i = 0; i < capturedRookNum; i++) {
            rook.paintIcon(this, bbg, currentDrawPos, 0);
            currentDrawPos += overlappingPiecesSpacing;
        }
        if (capturedQueenNum != 0)
            currentDrawPos += nonOverlappingPiecesSpacing;
        for (int i = 0; i < capturedQueenNum; i++) {
            queen.paintIcon(this, bbg, currentDrawPos, 0);
            currentDrawPos += overlappingPiecesSpacing;
        }
        if (capturedKingNum != 0)
            currentDrawPos += nonOverlappingPiecesSpacing;
        for (int i = 0; i < capturedKingNum; i++) {
            king.paintIcon(this, bbg, currentDrawPos, 0);
            currentDrawPos += overlappingPiecesSpacing;
        }

        // draw bbg to g
        g.drawImage(offScreenBuffer, 0, 0, this);
        g.dispose();
    }
}
