package Game;

import javax.swing.*;

import Images.ImageManager;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import Pieces.*;
import Utils.*;

public class Game extends JPanel implements MouseListener, MouseMotionListener {

    public Game() {
        setupPieces();
        setupPanel();
        setupInput();
    }

    void setupPieces() {
        int id = 0;
        blackPieces.add(new Rook());
        blackPieces.get(0).setID(id++);
        blackPieces.get(0).setTruePos(new Vector2I((int) (0.5 * boardSizeI.x / 8), (int) (0.5 * boardSizeI.x / 8)), false);
        blackPieces.add(new Knight());
        blackPieces.get(1).setID(id++);
        blackPieces.get(1).setTruePos(new Vector2I((int) (1.5 * boardSizeI.x / 8), (int) (0.5 * boardSizeI.x / 8)), false);
        blackPieces.add(new Bishop());
        blackPieces.get(2).setID(id++);
        blackPieces.get(2).setTruePos(new Vector2I((int) (2.5 * boardSizeI.x / 8), (int) (0.5 * boardSizeI.x / 8)), false);
        blackPieces.add(new Queen());
        blackPieces.get(3).setID(id++);
        blackPieces.get(3).setTruePos(new Vector2I((int) (3.5 * boardSizeI.x / 8), (int) (0.5 * boardSizeI.x / 8)), false);
        blackPieces.add(new King());
        blackPieces.get(4).setID(id++);
        blackPieces.get(4).setTruePos(new Vector2I((int) (4.5 * boardSizeI.x / 8), (int) (0.5 * boardSizeI.x / 8)), false);
        blackPieces.add(new Bishop());
        blackPieces.get(5).setID(id++);
        blackPieces.get(5).setTruePos(new Vector2I((int) (5.5 * boardSizeI.x / 8), (int) (0.5 * boardSizeI.x / 8)), false);
        blackPieces.add(new Knight());
        blackPieces.get(6).setID(id++);
        blackPieces.get(6).setTruePos(new Vector2I((int) (6.5 * boardSizeI.x / 8), (int) (0.5 * boardSizeI.x / 8)), false);
        blackPieces.add(new Rook());
        blackPieces.get(7).setID(id++);
        blackPieces.get(7).setTruePos(new Vector2I((int) (7.5 * boardSizeI.x / 8), (int) (0.5 * boardSizeI.x / 8)), false);

        for (int i = 0; i <= 7; i++) {
            blackPieces.add(new Pawn());
            blackPieces.get(i + 8).setID(id++);
            blackPieces.get(i + 8)
                    .setTruePos(new Vector2I((int) ((i + 0.5) * boardSizeI.x / 8), (int) (1.5 * boardSizeI.x / 8)), false);
        }

        for (int i = 0; i <= 15; i++)
            blackPieces.get(i).setColor(ChessColor.BLACK);

        whitePieces.add(new Rook());
        whitePieces.get(0).setID(id++);
        whitePieces.get(0).setTruePos(new Vector2I((int) (0.5 * boardSizeI.x / 8), (int) (7.5 * boardSizeI.x / 8)), false);
        whitePieces.add(new Knight());
        whitePieces.get(1).setID(id++);
        whitePieces.get(1).setTruePos(new Vector2I((int) (1.5 * boardSizeI.x / 8), (int) (7.5 * boardSizeI.x / 8)), false);
        whitePieces.add(new Bishop());
        whitePieces.get(2).setID(id++);
        whitePieces.get(2).setTruePos(new Vector2I((int) (2.5 * boardSizeI.x / 8), (int) (7.5 * boardSizeI.x / 8)), false);
        whitePieces.add(new Queen());
        whitePieces.get(3).setID(id++);
        whitePieces.get(3).setTruePos(new Vector2I((int) (3.5 * boardSizeI.x / 8), (int) (7.5 * boardSizeI.x / 8)), false);
        whitePieces.add(new King());
        whitePieces.get(4).setID(id++);
        whitePieces.get(4).setTruePos(new Vector2I((int) (4.5 * boardSizeI.x / 8), (int) (7.5 * boardSizeI.x / 8)), false);
        whitePieces.add(new Bishop());
        whitePieces.get(5).setID(id++);
        whitePieces.get(5).setTruePos(new Vector2I((int) (5.5 * boardSizeI.x / 8), (int) (7.5 * boardSizeI.x / 8)), false);
        whitePieces.add(new Knight());
        whitePieces.get(6).setID(id++);
        whitePieces.get(6).setTruePos(new Vector2I((int) (6.5 * boardSizeI.x / 8), (int) (7.5 * boardSizeI.x / 8)), false);
        whitePieces.add(new Rook());
        whitePieces.get(7).setID(id++);
        whitePieces.get(7).setTruePos(new Vector2I((int) (7.5 * boardSizeI.x / 8), (int) (7.5 * boardSizeI.x / 8)), false);

        for (int i = 0; i <= 7; i++) {
            whitePieces.add(new Pawn());
            whitePieces.get(i + 8).setID(id++);
            whitePieces.get(i + 8)
                    .setTruePos(new Vector2I((int) ((i + 0.5) * boardSizeI.x / 8), (int) (6.5 * boardSizeI.x / 8)), false);
        }

        for (int i = 0; i <= 15; i++)
            whitePieces.get(i).setColor(ChessColor.WHITE);
    }

    public static final Vector2I boardSizePixels = new Vector2I(512, 512);

    BufferedImage offScreenBuffer;
    static ImageIcon boardIcon = ImageManager.resize(ImageManager.board, boardSizePixels);

    void setupPanel() {
        setSize(boardSizePixels.x, boardSizePixels.y);
        setVisible(true);
        offScreenBuffer = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_ARGB);
    }

    void setupInput() {
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    Vector2I mousePosGame = new Vector2I();
    boolean mousePressedGame;

    public void startRendering() {
        Thread gameThread = new Thread(() -> {
            while (true) {
                mousePosGame = mousePos.copy();
                mousePressedGame = mousePressed;
                movePieces();
                validate();
                repaint();
            }
        });
        gameThread.run();
    }

    int turnNumber = 1;
    ChessColor turn = ChessColor.WHITE;
    Piece heldPiece;
    ArrayList<Piece> whitePieces = new ArrayList<Piece>();
    ArrayList<Piece> blackPieces = new ArrayList<Piece>();
    ArrayList<Piece> whitePiecesCaptured = new ArrayList<Piece>();
    ArrayList<Piece> blackPiecesCaptured = new ArrayList<Piece>();

    public static final Vector2I boardSizeI = new Vector2I(2048, 2048);

    public void movePieces() {
        if (turnNumber % 2 == 0)
            turn = ChessColor.BLACK;
        else
            turn = ChessColor.WHITE;

        if (mousePressedGame && heldPiece == null) {
            if (turn == ChessColor.WHITE) {
                for (Piece p : whitePieces) {
                    if (p.isInHitbox(mousePosGame)) {
                        heldPiece = p;
                        whitePieces.remove(p);
                        break;
                    }
                }
            } else {
                for (Piece p : blackPieces) {
                    if (p.isInHitbox(mousePosGame)) {
                        heldPiece = p;
                        blackPieces.remove(p);
                        break;
                    }
                }
            }
        }

        if (!mousePressedGame && heldPiece != null) {
            heldPiece.setTruePos(heldPiece.getVisiblePos(), true);
            ArrayList<Piece> capturedPieces = heldPiece.oppositeColorPiecesOverlapping(heldPiece.getVisiblePos(), whitePieces, blackPieces);
            if (turn == ChessColor.WHITE) {
                for(Piece p : capturedPieces) {
                    blackPieces.remove(p);
                }
                whitePieces.add(heldPiece);
            } else {
                for(Piece p : capturedPieces) {
                    whitePieces.remove(p);
                }
                blackPieces.add(heldPiece);
            }
            heldPiece = null;
            turnNumber++;
        }

        if (heldPiece != null) {
            Vector2I closestValidPointToMouse = heldPiece.closestValidPoint(mousePosGame, whitePieces, blackPieces);
            heldPiece.setVisiblePos(closestValidPointToMouse);
        }
    }

    BufferedImage b = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);

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

        /*
         * for(int i = 0; i < 1000; i++) {
         * for(int i2 = 0; i2 < 1000; i2++) {
         * int p = ((i/4) << 24) | ((i2/4) << 16) | ((i/4) << 8) | (i2/4);
         * b.setRGB(i, i2, p);
         * }
         * }
         */
        ImageIcon test = new ImageIcon(b);
        test.paintIcon(this, bbg, 0, 0);

        // draw bbg to g
        g.drawImage(offScreenBuffer, 0, 0, this);
        g.dispose();
    }

    public Vector2I boardPosToPanelPos(Vector2I orig) {
        return new Vector2I(orig.x * boardSizePixels.x / boardSizeI.x, orig.y * boardSizePixels.y / boardSizeI.y);
    }

    public Vector2I panelPosToBoardPos(Vector2I orig) {
        return new Vector2I(orig.x * boardSizeI.x / boardSizePixels.x, orig.y * boardSizeI.y / boardSizePixels.y);
    }

    volatile Vector2I mousePos = new Vector2I();
    volatile boolean mousePressed = false;

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