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
        blackPieces.get(0).setTruePos(new Vector2(0.5, 0.5));
        blackPieces.add(new Knight());
        blackPieces.get(1).setID(id++);
        blackPieces.get(1).setTruePos(new Vector2(1.5, 0.5));
        blackPieces.add(new Bishop());
        blackPieces.get(2).setID(id++);
        blackPieces.get(2).setTruePos(new Vector2(2.5, 0.5));
        blackPieces.add(new Queen());
        blackPieces.get(3).setID(id++);
        blackPieces.get(3).setTruePos(new Vector2(3.5, 0.5));
        blackPieces.add(new King());
        blackPieces.get(4).setID(id++);
        blackPieces.get(4).setTruePos(new Vector2(4.5, 0.5));
        blackPieces.add(new Bishop());
        blackPieces.get(5).setID(id++);
        blackPieces.get(5).setTruePos(new Vector2(5.5, 0.5));
        blackPieces.add(new Knight());
        blackPieces.get(6).setID(id++);
        blackPieces.get(6).setTruePos(new Vector2(6.5, 0.5));
        blackPieces.add(new Rook());
        blackPieces.get(7).setID(id++);
        blackPieces.get(7).setTruePos(new Vector2(7.5, 0.5));

        for (int i = 0; i <= 7; i++) {
            blackPieces.add(new Pawn());
            blackPieces.get(i + 8).setID(id++);
            blackPieces.get(i + 8).setTruePos(new Vector2(i + 0.5, 1.5));
        }

        for (int i = 0; i <= 15; i++)
            blackPieces.get(i).setColor(ChessColor.BLACK);

        whitePieces.add(new Rook());
        whitePieces.get(0).setID(id++);
        whitePieces.get(0).setTruePos(new Vector2(0.5, 7.5));
        whitePieces.add(new Knight());
        whitePieces.get(1).setID(id++);
        whitePieces.get(1).setTruePos(new Vector2(1.5, 7.5));
        whitePieces.add(new Bishop());
        whitePieces.get(2).setID(id++);
        whitePieces.get(2).setTruePos(new Vector2(2.5, 7.5));
        whitePieces.add(new Queen());
        whitePieces.get(3).setID(id++);
        whitePieces.get(3).setTruePos(new Vector2(3.5, 7.5));
        whitePieces.add(new King());
        whitePieces.get(4).setID(id++);
        whitePieces.get(4).setTruePos(new Vector2(4.5, 7.5));
        whitePieces.add(new Bishop());
        whitePieces.get(5).setID(id++);
        whitePieces.get(5).setTruePos(new Vector2(5.5, 7.5));
        whitePieces.add(new Knight());
        whitePieces.get(6).setID(id++);
        whitePieces.get(6).setTruePos(new Vector2(6.5, 7.5));
        whitePieces.add(new Rook());
        whitePieces.get(7).setID(id++);
        whitePieces.get(7).setTruePos(new Vector2(7.5, 7.5));

        for (int i = 0; i <= 7; i++) {
            whitePieces.add(new Pawn());
            whitePieces.get(i + 8).setID(id++);
            whitePieces.get(i + 8).setTruePos(new Vector2(i + 0.5, 6.5));
        }

        for (int i = 0; i <= 15; i++)
            whitePieces.get(i).setColor(ChessColor.WHITE);
    }

    public static final int boardSizeX = 512;
    public static final int boardSizeY = 512;

    BufferedImage offScreenBuffer;
    static ImageIcon boardIcon = ImageManager.resize(ImageManager.board, boardSizeX, boardSizeY);

    void setupPanel() {
        setSize(boardSizeX, boardSizeY);
        setVisible(true);
        offScreenBuffer = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_ARGB);
    }

    void setupInput() {
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    Vector2 mousePosGame = new Vector2();
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
            heldPiece.setTruePos(heldPiece.getVisiblePos());
            if (turn == ChessColor.WHITE) {
                whitePieces.add(heldPiece);
            } else {
                blackPieces.add(heldPiece);
            }
            heldPiece = null;
            turnNumber++;
        }

        if (heldPiece != null) {
            Vector2 closestValidPointToMouse = heldPiece.closestValidPoint(mousePosGame, whitePieces, blackPieces);
            heldPiece.setVisiblePos(closestValidPointToMouse);
            System.out.println(closestValidPointToMouse.x + " " + closestValidPointToMouse.y);
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

    public Vector2I boardPosToPanelPos(Vector2 orig) {
        return new Vector2I((int) (orig.x / 8 * boardIcon.getIconWidth()),
                (int) (orig.y / 8 * boardIcon.getIconHeight()));
    }

    public Vector2 panelPosToBoardPos(Vector2I orig) {
        return new Vector2(((double) orig.x * 8) / boardIcon.getIconWidth(), ((double) orig.y * 8) / boardIcon.getIconHeight());
    }

    volatile Vector2 mousePos = new Vector2();
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