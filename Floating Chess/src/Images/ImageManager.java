package Images;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import Utils.*;

public final class ImageManager {
    public static final ImageIcon wp = new ImageIcon(ImageManager.class.getResource("/Images/wp.png"));
    public static final ImageIcon bp = new ImageIcon(ImageManager.class.getResource("/Images/bp.png"));

    public static final ImageIcon wn = new ImageIcon(ImageManager.class.getResource("/Images/wn.png"));
    public static final ImageIcon bn = new ImageIcon(ImageManager.class.getResource("/Images/bn.png"));

    public static final ImageIcon wb = new ImageIcon(ImageManager.class.getResource("/Images/wb.png"));
    public static final ImageIcon bb = new ImageIcon(ImageManager.class.getResource("/Images/bb.png"));

    public static final ImageIcon wr = new ImageIcon(ImageManager.class.getResource("/Images/wr.png"));
    public static final ImageIcon br = new ImageIcon(ImageManager.class.getResource("/Images/br.png"));

    public static final ImageIcon wq = new ImageIcon(ImageManager.class.getResource("/Images/wq.png"));
    public static final ImageIcon bq = new ImageIcon(ImageManager.class.getResource("/Images/bq.png"));

    public static final ImageIcon wk = new ImageIcon(ImageManager.class.getResource("/Images/wk.png"));
    public static final ImageIcon bk = new ImageIcon(ImageManager.class.getResource("/Images/bk.png"));

    public static final ImageIcon board = new ImageIcon(ImageManager.class.getResource("/Images/board.png"));

    public static final ImageIcon pawnMoveNormalWhite = new ImageIcon(
            ImageManager.class.getResource("/Images/PawnMoveNormalWhite.png"));

    public static final ImageIcon pawnMoveFirstWhite = new ImageIcon(
            ImageManager.class.getResource("/Images/PawnMoveFirstWhite.png"));
    public static final ImageIcon pawnMoveNormalBlack = new ImageIcon(
            ImageManager.class.getResource("/Images/PawnMoveNormalBlack.png"));

    public static final ImageIcon pawnMoveFirstBlack = new ImageIcon(
            ImageManager.class.getResource("/Images/PawnMoveFirstBlack.png"));

    public static final ImageIcon knightMove = new ImageIcon(ImageManager.class.getResource("/Images/KnightMove.png"));

    public static final ImageIcon bishopMove = new ImageIcon(ImageManager.class.getResource("/Images/BishopMove.png"));

    public static final ImageIcon rookMove = new ImageIcon(ImageManager.class.getResource("/Images/RookMove.png"));

    public static final ImageIcon queenMove = new ImageIcon(ImageManager.class.getResource("/Images/QueenMove.png"));

    public static final ImageIcon kingMove = new ImageIcon(ImageManager.class.getResource("/Images/KingMove.png"));

    private ImageManager() {
    }

    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public static ImageIcon resize(ImageIcon original, Vector2I size) {
        Image imgOriginal = original.getImage();
        Image imgScaled = imgOriginal.getScaledInstance(size.x, size.y, Image.SCALE_SMOOTH);
        return new ImageIcon(imgScaled);
    }
}