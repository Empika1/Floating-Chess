package Game;

import javax.swing.*;

import Images.ImageManager;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import Pieces.*;
import java.util.*;

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
        blackPieces.get(0).setXPos(0.5);
        blackPieces.get(0).setYPos(0.5);
        blackPieces.add(new Knight());
        blackPieces.get(1).setID(id++);
        blackPieces.get(1).setXPos(1.5);
        blackPieces.get(1).setYPos(0.5);
        blackPieces.add(new Bishop());
        blackPieces.get(2).setID(id++);
        blackPieces.get(2).setXPos(2.5);
        blackPieces.get(2).setYPos(0.5);
        blackPieces.add(new Queen());
        blackPieces.get(3).setID(id++);
        blackPieces.get(3).setXPos(3.5);
        blackPieces.get(3).setYPos(0.5);
        blackPieces.add(new King());
        blackPieces.get(4).setID(id++);
        blackPieces.get(4).setXPos(4.5);
        blackPieces.get(4).setYPos(0.5);
        blackPieces.add(new Bishop());
        blackPieces.get(5).setID(id++);
        blackPieces.get(5).setXPos(5.5);
        blackPieces.get(5).setYPos(0.5);
        blackPieces.add(new Knight());
        blackPieces.get(6).setID(id++);
        blackPieces.get(6).setXPos(6.5);
        blackPieces.get(6).setYPos(0.5);
        blackPieces.add(new Rook());
        blackPieces.get(7).setID(id++);
        blackPieces.get(7).setXPos(7.5);
        blackPieces.get(7).setYPos(0.5);

        for (int i = 0; i <= 7; i++) {
            blackPieces.add(new Pawn());
            blackPieces.get(i + 8).setID(id++);
            blackPieces.get(i + 8).setXPos(i + 0.5);
            blackPieces.get(i + 8).setYPos(1.5);
        }

        for (int i = 0; i <= 15; i++)
            blackPieces.get(i).setColor(ChessColor.BLACK);

        whitePieces.add(new Rook());
        whitePieces.get(0).setID(id++);
        whitePieces.get(0).setXPos(0.5);
        whitePieces.get(0).setYPos(7.5);
        whitePieces.add(new Knight());
        whitePieces.get(1).setID(id++);
        whitePieces.get(1).setXPos(1.5);
        whitePieces.get(1).setYPos(7.5);
        whitePieces.add(new Bishop());
        whitePieces.get(2).setID(id++);
        whitePieces.get(2).setXPos(2.5);
        whitePieces.get(2).setYPos(7.5);
        whitePieces.add(new Queen());
        whitePieces.get(3).setID(id++);
        whitePieces.get(3).setXPos(3.5);
        whitePieces.get(3).setYPos(7.5);
        whitePieces.add(new King());
        whitePieces.get(4).setID(id++);
        whitePieces.get(4).setXPos(4.5);
        whitePieces.get(4).setYPos(7.5);
        whitePieces.add(new Bishop());
        whitePieces.get(5).setID(id++);
        whitePieces.get(5).setXPos(5.5);
        whitePieces.get(5).setYPos(7.5);
        whitePieces.add(new Knight());
        whitePieces.get(6).setID(id++);
        whitePieces.get(6).setXPos(6.5);
        whitePieces.get(6).setYPos(7.5);
        whitePieces.add(new Rook());
        whitePieces.get(7).setID(id++);
        whitePieces.get(7).setXPos(7.5);
        whitePieces.get(7).setYPos(7.5);

        for (int i = 0; i <= 7; i++) {
            whitePieces.add(new Pawn());
            whitePieces.get(i + 8).setID(id++);
            whitePieces.get(i + 8).setXPos(i + 0.5);
            whitePieces.get(i + 8).setYPos(6.5);
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

    public void startRendering() {
        Thread gameThread = new Thread(() -> {
            while (true) {
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
        //Thread.yield();
        if (turnNumber % 2 == 0)
            turn = ChessColor.BLACK;
        else
            turn = ChessColor.WHITE;

        if (mousePressed && heldPiece == null) {
            if (turn == ChessColor.WHITE) {
                for (Piece p : whitePieces) {
                    if (p.isInHitbox(mouseX, mouseY)) {
                        heldPiece = p;
                        whitePieces.remove(p);
                        break;
                    }
                }
            } else {
                for (Piece p : blackPieces) {
                    if (p.isInHitbox(mouseX, mouseY)) {
                        heldPiece = p;
                        blackPieces.remove(p);
                        break;
                    }
                }
            }
        }

        if (!mousePressed && heldPiece != null) {
            if (turn == ChessColor.WHITE) {
                whitePieces.add(heldPiece);
            } else {
                blackPieces.add(heldPiece);
            }
            heldPiece = null;
            turnNumber++;
        }

        if (heldPiece != null) {
            heldPiece.setXPos(mouseX);
            heldPiece.setYPos(mouseY);
        }
    }

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
        if(heldPiece != null)
            heldPiece.draw(bbg, this);

        //new ImageManager().test.paintIcon(this, bbg, 0, 0);

        // draw bbg to g
        g.drawImage(offScreenBuffer, 0, 0, this);
        g.dispose();
    }

    public int boardXToPanelX(double xOrig) {
        return (int) (xOrig / 8 * boardIcon.getIconWidth());
    }

    public int boardYToPanelY(double yOrig) {
        return (int) (yOrig / 8 * boardIcon.getIconHeight());
    }

    public double panelXToBoardX(int xOrig) {
        return ((double) xOrig * 8) / boardIcon.getIconWidth();
    }

    public double panelYToBoardY(int yOrig) {
        return ((double) yOrig * 8) / boardIcon.getIconHeight();
    }

    volatile double mouseX;
    volatile double mouseY;
    volatile boolean mousePressed = false;

    @Override
    public void mouseMoved(MouseEvent m) {
        mouseX = panelXToBoardX(m.getX());
        mouseY = panelYToBoardY(m.getY());
    }

    @Override
    public void mouseDragged(MouseEvent m) {
        mouseX = panelXToBoardX(m.getX());
        mouseY = panelYToBoardY(m.getY());
    }

    @Override
    public void mousePressed(MouseEvent m) {
        if(m.getButton() == MouseEvent.BUTTON1) {
            mousePressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent m) {
        if(m.getButton() == MouseEvent.BUTTON1) {
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