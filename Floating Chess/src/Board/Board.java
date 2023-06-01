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

    void setupPieces() {
        int id = 0;
        blackPieces.add(new Rook());
        blackPieces.get(0).setID(id++);
        blackPieces.get(0).setTruePos(new Vector2I((int) (0.5 * boardSizeI.x / 8), (int) (0.5 * boardSizeI.x / 8)),
                false);
        blackPieces.add(new Knight());
        blackPieces.get(1).setID(id++);
        blackPieces.get(1).setTruePos(new Vector2I((int) (1.5 * boardSizeI.x / 8), (int) (0.5 * boardSizeI.x / 8)),
                false);
        blackPieces.add(new Bishop());
        blackPieces.get(2).setID(id++);
        blackPieces.get(2).setTruePos(new Vector2I((int) (2.5 * boardSizeI.x / 8), (int) (0.5 * boardSizeI.x / 8)),
                false);
        blackPieces.add(new Queen());
        blackPieces.get(3).setID(id++);
        blackPieces.get(3).setTruePos(new Vector2I((int) (3.5 * boardSizeI.x / 8), (int) (0.5 * boardSizeI.x / 8)),
                false);
        blackPieces.add(new King());
        blackPieces.get(4).setID(id++);
        blackPieces.get(4).setTruePos(new Vector2I((int) (4.5 * boardSizeI.x / 8), (int) (0.5 * boardSizeI.x / 8)),
                false);
        blackPieces.add(new Bishop());
        blackPieces.get(5).setID(id++);
        blackPieces.get(5).setTruePos(new Vector2I((int) (5.5 * boardSizeI.x / 8), (int) (0.5 * boardSizeI.x / 8)),
                false);
        blackPieces.add(new Knight());
        blackPieces.get(6).setID(id++);
        blackPieces.get(6).setTruePos(new Vector2I((int) (6.5 * boardSizeI.x / 8), (int) (0.5 * boardSizeI.x / 8)),
                false);
        blackPieces.add(new Rook());
        blackPieces.get(7).setID(id++);
        blackPieces.get(7).setTruePos(new Vector2I((int) (7.5 * boardSizeI.x / 8), (int) (0.5 * boardSizeI.x / 8)),
                false);

        for (int i = 0; i <= 7; i++) {
            blackPieces.add(new Pawn());
            blackPieces.get(i + 8).setID(id++);
            blackPieces.get(i + 8)
                    .setTruePos(new Vector2I((int) ((i + 0.5) * boardSizeI.x / 8), (int) (1.5 * boardSizeI.x / 8)),
                            false);
        }

        for (int i = 0; i <= 15; i++)
            blackPieces.get(i).setColor(ChessColor.BLACK);

        whitePieces.add(new Rook());
        whitePieces.get(0).setID(id++);
        whitePieces.get(0).setTruePos(new Vector2I((int) (0.5 * boardSizeI.x / 8), (int) (7.5 * boardSizeI.x / 8)),
                false);
        whitePieces.add(new Knight());
        whitePieces.get(1).setID(id++);
        whitePieces.get(1).setTruePos(new Vector2I((int) (1.5 * boardSizeI.x / 8), (int) (7.5 * boardSizeI.x / 8)),
                false);
        whitePieces.add(new Bishop());
        whitePieces.get(2).setID(id++);
        whitePieces.get(2).setTruePos(new Vector2I((int) (2.5 * boardSizeI.x / 8), (int) (7.5 * boardSizeI.x / 8)),
                false);
        whitePieces.add(new Queen());
        whitePieces.get(3).setID(id++);
        whitePieces.get(3).setTruePos(new Vector2I((int) (3.5 * boardSizeI.x / 8), (int) (7.5 * boardSizeI.x / 8)),
                false);
        whitePieces.add(new King());
        whitePieces.get(4).setID(id++);
        whitePieces.get(4).setTruePos(new Vector2I((int) (4.5 * boardSizeI.x / 8), (int) (7.5 * boardSizeI.x / 8)),
                false);
        whitePieces.add(new Bishop());
        whitePieces.get(5).setID(id++);
        whitePieces.get(5).setTruePos(new Vector2I((int) (5.5 * boardSizeI.x / 8), (int) (7.5 * boardSizeI.x / 8)),
                false);
        whitePieces.add(new Knight());
        whitePieces.get(6).setID(id++);
        whitePieces.get(6).setTruePos(new Vector2I((int) (6.5 * boardSizeI.x / 8), (int) (7.5 * boardSizeI.x / 8)),
                false);
        whitePieces.add(new Rook());
        whitePieces.get(7).setID(id++);
        whitePieces.get(7).setTruePos(new Vector2I((int) (7.5 * boardSizeI.x / 8), (int) (7.5 * boardSizeI.x / 8)),
                false);

        for (int i = 0; i <= 7; i++) {
            whitePieces.add(new Pawn());
            whitePieces.get(i + 8).setID(id++);
            whitePieces.get(i + 8)
                    .setTruePos(new Vector2I((int) ((i + 0.5) * boardSizeI.x / 8), (int) (6.5 * boardSizeI.x / 8)),
                            false);
        }

        for (int i = 0; i <= 15; i++)
            whitePieces.get(i).setColor(ChessColor.WHITE);
    }

    public void draw() {
        validate();
        repaint();
    }

    BufferedImage offScreenBuffer = new BufferedImage(boardSizePixels.x, boardSizePixels.y,
            BufferedImage.TYPE_INT_ARGB);;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics bbg = offScreenBuffer.getGraphics();

        // Clear the off-screen buffer
        bbg.setColor(new Color(0, 0, 0, 255));
        bbg.fillRect(0, 0, getSize().width + 100, getSize().height + 100);

        // draw objects to bbg
        bbg.setColor(new Color(255, 0, 0, 255));
        boardIcon.paintIcon(this, bbg, 0, 0);
        for (Piece p : blackPieces)
            p.draw(bbg, this);
        for (Piece p : whitePieces)
            p.draw(bbg, this);
        if (heldPiece != null)
            heldPiece.draw(bbg, this);

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
    public volatile boolean mousePressed = false;

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
        if (m.getButton() == MouseEvent.BUTTON1) {
            mousePressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent m) {
        if (m.getButton() == MouseEvent.BUTTON1) {
            mousePressed = false;
        }
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
