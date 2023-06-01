package Board;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import Images.*;
import Utils.*;
import Pieces.*;

public class CapturedPieces extends JPanel {
    static final Vector2I capturedPiecesSizePixels = new Vector2I(100, 20);

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

    static final Vector2I iconSizePixels = new Vector2I(20, 20);
    static final int overlappingPiecesSpacing = 6;
    static final int nonOverlappingPiecesSpacing = 15;

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

    static final ImageIcon bk = ImageManager.resize(ImageManager.bp, iconSizePixels);
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
        ArrayList<Pawn> capturedPawns = new ArrayList<Pawn>();
        ArrayList<Knight> capturedKnights = new ArrayList<Knight>();
        ArrayList<Bishop> capturedBishops = new ArrayList<Bishop>();
        ArrayList<Rook> capturedRooks = new ArrayList<Rook>();
        ArrayList<Queen> capturedQueens = new ArrayList<Queen>();
        ArrayList<King> capturedKings = new ArrayList<King>();
        for (Piece p : capturedPieces) {
            if (p.getClass().equals(Pawn.class))
                capturedPawns.add((Pawn) p);
            else if (p.getClass().equals(Knight.class))
                capturedKnights.add((Knight) p);
            else if (p.getClass().equals(Bishop.class))
                capturedBishops.add((Bishop) p);
            else if (p.getClass().equals(Rook.class))
                capturedRooks.add((Rook) p);
            else if (p.getClass().equals(Queen.class))
                capturedQueens.add((Queen) p);
            else if (p.getClass().equals(King.class))
                capturedKings.add((King) p);
        }

        int currentDrawPos = 0;
        for (Pawn p : capturedPawns) {
            pawn.paintIcon(this, bbg, currentDrawPos, 0);
            currentDrawPos += overlappingPiecesSpacing;
        }
        if (capturedKnights.size() != 0)
            currentDrawPos += nonOverlappingPiecesSpacing;
        for (Knight p : capturedKnights) {
            knight.paintIcon(this, bbg, currentDrawPos, 0);
            currentDrawPos += overlappingPiecesSpacing;
        }
        if (capturedBishops.size() != 0)
            currentDrawPos += nonOverlappingPiecesSpacing;
        for (Bishop p : capturedBishops) {
            bishop.paintIcon(this, bbg, currentDrawPos, 0);
            currentDrawPos += overlappingPiecesSpacing;
        }
        if (capturedRooks.size() != 0)
            currentDrawPos += nonOverlappingPiecesSpacing;
        for (Rook p : capturedRooks) {
            rook.paintIcon(this, bbg, currentDrawPos, 0);
            currentDrawPos += overlappingPiecesSpacing;
        }
        if (capturedQueens.size() != 0)
            currentDrawPos += nonOverlappingPiecesSpacing;
        for (Queen p : capturedQueens) {
            queen.paintIcon(this, bbg, currentDrawPos, 0);
            currentDrawPos += overlappingPiecesSpacing;
        }
        if (capturedKings.size() != 0)
            currentDrawPos += nonOverlappingPiecesSpacing;
        for (King p : capturedKings) {
            king.paintIcon(this, bbg, currentDrawPos, 0);
            currentDrawPos += overlappingPiecesSpacing;
        }

        // draw bbg to g
        g.drawImage(offScreenBuffer, 0, 0, this);
        g.dispose();
    }
}
