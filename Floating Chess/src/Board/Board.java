//A chess board. Used in all three of the screens of the game. Contains a board image, and lists of pieces.
package Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import Pieces.*;
import Utils.*;
import Images.*;

public class Board extends JPanel implements MouseListener, MouseMotionListener {
    public static final Vector2I boardSizePixels = new Vector2I(512, 512); // The size of the board in screen pixels
    public static final Vector2I boardSizeI = new Vector2I(2048, 2048); // The size of the board internally, used by piece movement logic. This is higher resolution than the screen to give the illusion of true floating point movement
    public Board(boolean setupPieces) { // sets up the JPanel, and if chosen, arranges the pieces
        setupPanel();
        if (setupPieces)
            setupPieces();
    }

    static ImageIcon boardIcon = ImageManager.resize(ImageManager.board, boardSizePixels); // the board image

    void setupPanel() {
        setPreferredSize(new Dimension(boardSizePixels.x, boardSizePixels.y));
        setVisible(true);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public int turnNumber = 1;
    public ChessColor turn = ChessColor.WHITE;
    public Piece heldPiece; // the piece that is currently being held
    public ArrayList<Piece> whitePieces = new ArrayList<Piece>(); // the white pieces that are on the board stil
    public ArrayList<Piece> blackPieces = new ArrayList<Piece>(); // the black pieces that are on the board stil
    public ArrayList<Piece> whitePiecesCaptured = new ArrayList<Piece>(); // the white pieces that have been captured
    public ArrayList<Piece> blackPiecesCaptured = new ArrayList<Piece>(); // the black pieces that have been captured

    public ArrayList<Piece> getWhitePiecesStartingPosition() { // creates an arraylist of all the white pieces in their
                                                               // normal chess starting position
        int id = 16;
        ArrayList<Piece> whitePiecesSetup = new ArrayList<Piece>();

        whitePiecesSetup.add(new Rook());
        whitePiecesSetup.get(0).setID(id++);
        whitePiecesSetup.get(0).setTruePos(new Vector2I((int) (0.5 * boardSizeI.x / 8), (int) (7.5 * boardSizeI.x / 8)),
                false);
        whitePiecesSetup.add(new Knight());
        whitePiecesSetup.get(1).setID(id++);
        whitePiecesSetup.get(1).setTruePos(new Vector2I((int) (1.5 * boardSizeI.x / 8), (int) (7.5 * boardSizeI.x / 8)),
                false);
        whitePiecesSetup.add(new Bishop());
        whitePiecesSetup.get(2).setID(id++);
        whitePiecesSetup.get(2).setTruePos(new Vector2I((int) (2.5 * boardSizeI.x / 8), (int) (7.5 * boardSizeI.x / 8)),
                false);
        whitePiecesSetup.add(new Queen());
        whitePiecesSetup.get(3).setID(id++);
        whitePiecesSetup.get(3).setTruePos(new Vector2I((int) (3.5 * boardSizeI.x / 8), (int) (7.5 * boardSizeI.x / 8)),
                false);
        whitePiecesSetup.add(new King());
        whitePiecesSetup.get(4).setID(id++);
        whitePiecesSetup.get(4).setTruePos(new Vector2I((int) (4.5 * boardSizeI.x / 8), (int) (7.5 * boardSizeI.x / 8)),
                false);
        whitePiecesSetup.add(new Bishop());
        whitePiecesSetup.get(5).setID(id++);
        whitePiecesSetup.get(5).setTruePos(new Vector2I((int) (5.5 * boardSizeI.x / 8), (int) (7.5 * boardSizeI.x / 8)),
                false);
        whitePiecesSetup.add(new Knight());
        whitePiecesSetup.get(6).setID(id++);
        whitePiecesSetup.get(6).setTruePos(new Vector2I((int) (6.5 * boardSizeI.x / 8), (int) (7.5 * boardSizeI.x / 8)),
                false);
        whitePiecesSetup.add(new Rook());
        whitePiecesSetup.get(7).setID(id++);
        whitePiecesSetup.get(7).setTruePos(new Vector2I((int) (7.5 * boardSizeI.x / 8), (int) (7.5 * boardSizeI.x / 8)),
                false);

        for (int i = 0; i <= 7; i++) {
            whitePiecesSetup.add(new Pawn());
            whitePiecesSetup.get(i + 8).setID(id++);
            whitePiecesSetup.get(i + 8)
                    .setTruePos(new Vector2I((int) ((i + 0.5) * boardSizeI.x / 8), (int) (6.5 * boardSizeI.x / 8)),
                            false);
        }

        for (int i = 0; i <= 15; i++)
            whitePiecesSetup.get(i).setColor(ChessColor.WHITE);

        return whitePiecesSetup;
    }

    public ArrayList<Piece> getBlackPiecesStartingPosition() { // creates an arraylist of all the black pieces in their
                                                               // normal chess starting position
        int id = 0;
        ArrayList<Piece> blackPiecesSetup = new ArrayList<Piece>();

        blackPiecesSetup.add(new Rook());
        blackPiecesSetup.get(0).setID(id++);
        blackPiecesSetup.get(0).setTruePos(new Vector2I((int) (0.5 * boardSizeI.x / 8), (int) (0.5 * boardSizeI.x / 8)),
                false);
        blackPiecesSetup.add(new Knight());
        blackPiecesSetup.get(1).setID(id++);
        blackPiecesSetup.get(1).setTruePos(new Vector2I((int) (1.5 * boardSizeI.x / 8), (int) (0.5 * boardSizeI.x / 8)),
                false);
        blackPiecesSetup.add(new Bishop());
        blackPiecesSetup.get(2).setID(id++);
        blackPiecesSetup.get(2).setTruePos(new Vector2I((int) (2.5 * boardSizeI.x / 8), (int) (0.5 * boardSizeI.x / 8)),
                false);
        blackPiecesSetup.add(new Queen());
        blackPiecesSetup.get(3).setID(id++);
        blackPiecesSetup.get(3).setTruePos(new Vector2I((int) (3.5 * boardSizeI.x / 8), (int) (0.5 * boardSizeI.x / 8)),
                false);
        blackPiecesSetup.add(new King());
        blackPiecesSetup.get(4).setID(id++);
        blackPiecesSetup.get(4).setTruePos(new Vector2I((int) (4.5 * boardSizeI.x / 8), (int) (0.5 * boardSizeI.x / 8)),
                false);
        blackPiecesSetup.add(new Bishop());
        blackPiecesSetup.get(5).setID(id++);
        blackPiecesSetup.get(5).setTruePos(new Vector2I((int) (5.5 * boardSizeI.x / 8), (int) (0.5 * boardSizeI.x / 8)),
                false);
        blackPiecesSetup.add(new Knight());
        blackPiecesSetup.get(6).setID(id++);
        blackPiecesSetup.get(6).setTruePos(new Vector2I((int) (6.5 * boardSizeI.x / 8), (int) (0.5 * boardSizeI.x / 8)),
                false);
        blackPiecesSetup.add(new Rook());
        blackPiecesSetup.get(7).setID(id++);
        blackPiecesSetup.get(7).setTruePos(new Vector2I((int) (7.5 * boardSizeI.x / 8), (int) (0.5 * boardSizeI.x / 8)),
                false);

        for (int i = 0; i <= 7; i++) {
            blackPiecesSetup.add(new Pawn());
            blackPiecesSetup.get(i + 8).setID(id++);
            blackPiecesSetup.get(i + 8)
                    .setTruePos(new Vector2I((int) ((i + 0.5) * boardSizeI.x / 8), (int) (1.5 * boardSizeI.x / 8)),
                            false);
        }

        for (int i = 0; i <= 15; i++)
            blackPiecesSetup.get(i).setColor(ChessColor.BLACK);

        return blackPiecesSetup;
    }

    void setupPieces() { // sets the black and white pieces to their proper starting position
        blackPieces = getBlackPiecesStartingPosition();
        whitePieces = getWhitePiecesStartingPosition();
    }

    public void draw() { // validates and paints the board. called by one of the three screens when a piece is moved or something like that
        validate();
        repaint();
    }

    BufferedImage offScreenBuffer = new BufferedImage(boardSizePixels.x, boardSizePixels.y,
            BufferedImage.TYPE_INT_ARGB); // buffer used to prevent flicker

    public ArrayList<Piece> piecesThatWillBeCaptured = new ArrayList<Piece>(); // the pieces that are about to be captured by the held piece
    Color capturedPieceModulateColor = new Color(255, 255, 255, 128); // the color that pieces that will be captured are set to. this is used so you can tell what pieces you are about to capture

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g; // sets rendering hints to make the images smooth and epic
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        super.paintComponent(g); // calls paintcomponent on the jpanel that this extends
        Graphics bbg = offScreenBuffer.getGraphics();

        // Clear the off-screen buffer
        bbg.setColor(new Color(0, 0, 0, 255));
        bbg.fillRect(0, 0, getSize().width + 100, getSize().height + 100);

        // draw objects to bbg
        bbg.setColor(new Color(255, 0, 0, 255));
        boardIcon.paintIcon(this, bbg, 0, 0);

        if (heldPiece != null) { // if a piece is currently being held:
            if (heldPiece.getPieceType() == PieceType.PAWN) { // if it is a pawn, draw the pawn promotion line
                if (heldPiece.getColor() == ChessColor.WHITE)
                    ImageManager.whitePawnPromotionLine.paintIcon(this, bbg, 0, 0);
                else
                    ImageManager.blackPawnPromotionLine.paintIcon(this, bbg, 0, 0);
            }
            heldPiece.drawMoveArea(bbg, this); // draw the area that the heldpiece can move to
            heldPiece.drawHitbox(bbg, this); // draw the hitbox of the heldpiece
            for (Piece p : blackPieces) // draw the hitbox of all black and white pieces on the board
                p.drawHitbox(bbg, this);
            for (Piece p : whitePieces)
                p.drawHitbox(bbg, this);
        }

        for (Piece p : blackPieces) { // draw each piece to the board
            if (piecesThatWillBeCaptured.contains(p))
                p.drawPiece(bbg, this, capturedPieceModulateColor); // if it will be captured, modulate its color
            else
                p.drawPiece(bbg, this, null);
        }
        for (Piece p : whitePieces) {
            if (piecesThatWillBeCaptured.contains(p))
                p.drawPiece(bbg, this, capturedPieceModulateColor);
            else
                p.drawPiece(bbg, this, null);
        }

        if (heldPiece != null) // draw the held piece
            heldPiece.drawPiece(bbg, this, null);

        // draw bbg to g
        g.drawImage(offScreenBuffer, 0, 0, this);
        g.dispose();
    }

    public static Vector2I iPosToPixelPos(Vector2I orig) { // converts from internal (i) position to rendering (pixel) position
        return new Vector2I(orig.x * boardSizePixels.x / boardSizeI.x, orig.y * boardSizePixels.y / boardSizeI.y);
    }

    public static Vector2I pixelPosToIPos(Vector2I orig) { // vice versa
        return new Vector2I(orig.x * boardSizeI.x / boardSizePixels.x, orig.y * boardSizeI.y / boardSizePixels.y);
    }

    public volatile Vector2I mousePos = new Vector2I(); // the current position of the mouse
    public volatile boolean mouseLeftPressed = false; // if the left mouse button is pressed
    public volatile boolean mouseRightPressed = false; // if the right mouse button is pressed

    // update all mouse stuff
    @Override
    public void mouseMoved(MouseEvent m) {
        mousePos = pixelPosToIPos(new Vector2I(m.getX(), m.getY()));
    }

    @Override
    public void mouseDragged(MouseEvent m) {
        mousePos = pixelPosToIPos(new Vector2I(m.getX(), m.getY()));
    }

    @Override
    public void mousePressed(MouseEvent m) {
        if (m.getButton() == MouseEvent.BUTTON1)
            mouseLeftPressed = true;
        if (m.getButton() == MouseEvent.BUTTON3)
            mouseRightPressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent m) {
        if (m.getButton() == MouseEvent.BUTTON1)
            mouseLeftPressed = false;
        if (m.getButton() == MouseEvent.BUTTON3)
            mouseRightPressed = false;
    }

    @Override
    public void mouseClicked(MouseEvent m) {
    }

    @Override
    public void mouseEntered(MouseEvent m) {
    }

    @Override
    public void mouseExited(MouseEvent m) {
    }
}