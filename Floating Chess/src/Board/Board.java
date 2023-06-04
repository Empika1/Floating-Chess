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
    public static final Vector2I boardSizePixels = new Vector2I(512, 512);
    public static final Vector2I boardSizeI = new Vector2I(2048, 2048);

    public Board() {
        setupPanel();
        setupPieces();
    }

    static ImageIcon boardIcon = ImageManager.resize(ImageManager.board, boardSizePixels);

    void setupPanel() {
        setPreferredSize(new Dimension(boardSizePixels.x, boardSizePixels.y));
        setVisible(true);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public int turnNumber = 1;
    public ChessColor turn = ChessColor.WHITE;
    public Piece heldPiece;
    public ArrayList<Piece> whitePieces = new ArrayList<Piece>();
    public ArrayList<Piece> blackPieces = new ArrayList<Piece>();
    public ArrayList<Piece> whitePiecesCaptured = new ArrayList<Piece>();
    public ArrayList<Piece> blackPiecesCaptured = new ArrayList<Piece>();

    public ArrayList<Piece> getWhitePiecesStartingPosition() {
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

    public ArrayList<Piece> getBlackPiecesStartingPosition() {
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

    void setupPieces() {
        blackPieces = getBlackPiecesStartingPosition();
        whitePieces = getWhitePiecesStartingPosition();
    }

    public void draw() {
        validate();
        repaint();
    }

    BufferedImage offScreenBuffer = new BufferedImage(boardSizePixels.x, boardSizePixels.y,
            BufferedImage.TYPE_INT_ARGB);

    public ArrayList<Piece> piecesThatWillBeCaptured = new ArrayList<Piece>();
    Color capturedPieceModulateColor = new Color(255, 255, 255, 128);

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        super.paintComponent(g);
        Graphics bbg = offScreenBuffer.getGraphics();

        // Clear the off-screen buffer
        bbg.setColor(new Color(0, 0, 0, 255));
        bbg.fillRect(0, 0, getSize().width + 100, getSize().height + 100);

        // draw objects to bbg
        bbg.setColor(new Color(255, 0, 0, 255));
        boardIcon.paintIcon(this, bbg, 0, 0);

        if (heldPiece != null) {
            heldPiece.drawMoveArea(bbg, this);
            heldPiece.drawHitbox(bbg, this);
            for (Piece p : blackPieces)
                p.drawHitbox(bbg, this);
            for (Piece p : whitePieces)
                p.drawHitbox(bbg, this);
        }

        for (Piece p : blackPieces) {
            if (piecesThatWillBeCaptured.contains(p))
                p.drawPiece(bbg, this, capturedPieceModulateColor);
            else
                p.drawPiece(bbg, this, null);
        }
        for (Piece p : whitePieces) {
            if (piecesThatWillBeCaptured.contains(p))
                p.drawPiece(bbg, this, capturedPieceModulateColor);
            else
                p.drawPiece(bbg, this, null);
        }

        if (heldPiece != null)
            heldPiece.drawPiece(bbg, this, null);

        // draw bbg to g
        g.drawImage(offScreenBuffer, 0, 0, this);
        g.dispose();
    }

    public static Vector2I boardPosToPanelPos(Vector2I orig) {
        return new Vector2I(orig.x * boardSizePixels.x / boardSizeI.x, orig.y * boardSizePixels.y / boardSizeI.y);
    }

    public static Vector2I panelPosToBoardPos(Vector2I orig) {
        return new Vector2I(orig.x * boardSizeI.x / boardSizePixels.x, orig.y * boardSizeI.y / boardSizePixels.y);
    }

    public volatile Vector2I mousePos = new Vector2I();
    public volatile boolean mouseLeftPressed = false;
    public volatile boolean mouseRightPressed = false;

    @Override
    public void mouseMoved(MouseEvent m) {
        mousePos = panelPosToBoardPos(new Vector2I(m.getX(), m.getY()));
    }

    @Override
    public void mouseDragged(MouseEvent m) {
        mousePos = panelPosToBoardPos(new Vector2I(m.getX(), m.getY()));
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
