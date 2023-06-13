//A display of pieces that have been captured. Used in Game and Replay screens.
package Board;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import Images.*;
import Utils.*;
import Pieces.*;

public class CapturedPieces extends JPanel {
    static final Vector2I iconSizePixels = new Vector2I(25, 25); // the size of a single captured piece in pixels
    static final int sameTypePiecesSpacing = 6; // the spacing in pixels between two pieces of the same type
    static final int differentTypePiecesSpacing = 16; // the spacing in pixels between two pieces of different types

    // the size in pixels of this whole jpanel, calculated as the maximum width you would need if every piece is captured
    static final Vector2I capturedPiecesSizePixels = new Vector2I(
            16 * sameTypePiecesSpacing + 5 * differentTypePiecesSpacing + iconSizePixels.x, iconSizePixels.y);

    ChessColor color; // the color of this capturedPieces instance, which determines which pieces it should display
    ImageIcon pawn; // a captured pawn of the right color
    ImageIcon knight; // etc.
    ImageIcon bishop;
    ImageIcon rook;
    ImageIcon queen;
    ImageIcon king;
    ArrayList<Piece> capturedPieces;

    public CapturedPieces(ChessColor col, Board board) {
        this.board = board; // sets the board

        color = col;
        if (color == ChessColor.WHITE) { // sets all the piece icons to be their correct colors
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

    Board board = null; // the board that this capturedPieces instance is referencing

    public void draw() { // validates and paints the board. called by one of the three screens when a piece is moved or something like that
        validate();
        repaint();
    }

    Color backgroundColor = new JTextPane().getBackground(); // the background color, set as the same as the default background color of a JTextPane because I like that color

    BufferedImage offScreenBuffer = new BufferedImage(capturedPiecesSizePixels.x, capturedPiecesSizePixels.y,
            BufferedImage.TYPE_INT_ARGB); // buffer used to prevent flicker

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g; // sets rendering hints to make the images smooth and epic
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        super.paintComponent(g); // calls paintcomponent on the jpanel that this extends
        Graphics bbg = offScreenBuffer.getGraphics();

        // Clear the off-screen buffer
        bbg.setColor(backgroundColor);
        bbg.fillRect(0, 0, getSize().width + 100, getSize().height + 100);

        // draw objects to bbg
        int capturedPawnNum = 0; // get the numbers of each type of pieces that have been captured
        int capturedKnightNum = 0;
        int capturedBishopNum = 0;
        int capturedRookNum = 0;
        int capturedQueenNum = 0;
        int capturedKingNum = 0;
        for (Piece p : capturedPieces) {
            if (p.getPieceType().equals(PieceType.PAWN))
                capturedPawnNum++;
            else if (p.getPieceType().equals(PieceType.KNIGHT))
                capturedKnightNum++;
            else if (p.getPieceType().equals(PieceType.BISHOP))
                capturedBishopNum++;
            else if (p.getPieceType().equals(PieceType.ROOK))
                capturedRookNum++;
            else if (p.getPieceType().equals(PieceType.QUEEN))
                capturedQueenNum++;
            else if (p.getPieceType().equals(PieceType.KING))
                capturedKingNum++;
        }

        // goofy expression that gets the width in pixels of all the captured pieces
        int capturedPiecesWidth = capturedPieces.size() * sameTypePiecesSpacing
                + differentTypePiecesSpacing * ((capturedPawnNum != 0 ? 1 : 0) + (capturedKnightNum != 0 ? 1 : 0)
                        + (capturedBishopNum != 0 ? 1 : 0) + (capturedRookNum != 0 ? 1 : 0)
                        + (capturedQueenNum != 0 ? 1 : 0) + (capturedKingNum != 0 ? 1 : 0));

        int currentDrawPos = -differentTypePiecesSpacing + (capturedPiecesSizePixels.x - capturedPiecesWidth) / 2; // changes the draw pos depending on the width of the pieces so the pieces are always centered
        if (capturedPawnNum != 0) // bumps drawpos forward if pawns exist, this pattern repeats for all other piece types
            currentDrawPos += differentTypePiecesSpacing;
        for (int i = 0; i < capturedPawnNum; i++) { // draws pawns, this pattern repeats for all other piece types
            pawn.paintIcon(this, bbg, currentDrawPos, 0);
            currentDrawPos += sameTypePiecesSpacing;
        }
        if (capturedKnightNum != 0)
            currentDrawPos += differentTypePiecesSpacing;
        for (int i = 0; i < capturedKnightNum; i++) {
            knight.paintIcon(this, bbg, currentDrawPos, 0);
            currentDrawPos += sameTypePiecesSpacing;
        }
        if (capturedBishopNum != 0)
            currentDrawPos += differentTypePiecesSpacing;
        for (int i = 0; i < capturedBishopNum; i++) {
            bishop.paintIcon(this, bbg, currentDrawPos, 0);
            currentDrawPos += sameTypePiecesSpacing;
        }
        if (capturedRookNum != 0)
            currentDrawPos += differentTypePiecesSpacing;
        for (int i = 0; i < capturedRookNum; i++) {
            rook.paintIcon(this, bbg, currentDrawPos, 0);
            currentDrawPos += sameTypePiecesSpacing;
        }
        if (capturedQueenNum != 0)
            currentDrawPos += differentTypePiecesSpacing;
        for (int i = 0; i < capturedQueenNum; i++) {
            queen.paintIcon(this, bbg, currentDrawPos, 0);
            currentDrawPos += sameTypePiecesSpacing;
        }
        if (capturedKingNum != 0)
            currentDrawPos += differentTypePiecesSpacing;
        for (int i = 0; i < capturedKingNum; i++) {
            king.paintIcon(this, bbg, currentDrawPos, 0);
            currentDrawPos += sameTypePiecesSpacing;
        }

        // draw bbg to g
        g.drawImage(offScreenBuffer, 0, 0, this);
        g.dispose();
    }
}