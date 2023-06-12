//singleton manager for all the imageicons used in the game. used everywhere.
package Images;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import Utils.*;

public final class ImageManager {
    //all my images
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

    public static final ImageIcon kingMoveNormal = new ImageIcon(ImageManager.class.getResource("/Images/KingMove.png"));

    public static final ImageIcon kingMoveLeftCastle = new ImageIcon(ImageManager.class.getResource("/Images/KingMoveLeftCastle.png"));

    public static final ImageIcon kingMoveRightCastle = new ImageIcon(ImageManager.class.getResource("/Images/KingMoveRightCastle.png"));

    public static final ImageIcon kingMoveLeftRightCastle = new ImageIcon(ImageManager.class.getResource("/Images/KingMoveLeftRightCastle.png"));

    public static final ImageIcon hitbox = new ImageIcon(ImageManager.class.getResource("/Images/Hitbox.png"));

    public static final ImageIcon backButton = new ImageIcon(ImageManager.class.getResource("/Images/BackButton.png"));

    public static final ImageIcon forwardButton = new ImageIcon(ImageManager.class.getResource("/Images/ForwardButton.png"));

    public static final ImageIcon title = new ImageIcon(ImageManager.class.getResource("/Images/FloatingChess.png"));

    public static final ImageIcon whitePawnPromotionLine = new ImageIcon(ImageManager.class.getResource("/Images/WhitePawnPromotionLine.png"));

    public static final ImageIcon blackPawnPromotionLine = new ImageIcon(ImageManager.class.getResource("/Images/BlackPawnPromotionLine.png"));

    private ImageManager() { //no public constructor for you :<
    }

    public static BufferedImage toBufferedImage(Image img) { //converts any image to a bufferedimage
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB); //create a bufferedimage

        Graphics2D bGr = bimage.createGraphics(); //draw the og image to the bufferedimage
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return bimage; //return the bufferedimage
    }

    public static ImageIcon resize(ImageIcon original, Vector2I size) { //resizes an imageicon to a new size
        Image imgOriginal = original.getImage(); //get the image from the imageicon
        Image imgScaled = imgOriginal.getScaledInstance(size.x, size.y, Image.SCALE_SMOOTH); //rescale the image
        return new ImageIcon(imgScaled); //create a new imageicon
    }

    public static ImageIcon resize(ImageIcon original, int xSize) { //resizes an imageicon to a new size but this time you only specify the new x size
        Vector2I size = new Vector2I(xSize, xSize * original.getIconHeight() / original.getIconWidth()); //calculate what the y size should be then resize
        return resize(original, size);
    }

    public static ImageIcon tint(ImageIcon original, Color col) { //recolors an imageicon
        BufferedImage img = toBufferedImage(original.getImage()); //get the image and bufferify it
        for (int x = 0; x < img.getWidth(null); x++) { //loop through every pixel in the image
            for (int y = 0; y < img.getHeight(null); y++) {
                Color currentColor = new Color(img.getRGB(x, y), true); //get the current color of that pixel
                int currentAlpha = currentColor.getAlpha();
                int currentRed = currentColor.getRed();
                int currentGreen = currentColor.getGreen();
                int currentBlue = currentColor.getBlue();
                int newAlpha = (int) ((currentAlpha / 255.0) * (col.getAlpha() / 255.0) * 255); //multiply all color components by their corresponding components in your tint color
                int newRed = (int) ((currentRed / 255.0) * (col.getRed() / 255.0) * 255);
                int newGreen = (int) ((currentGreen / 255.0) * (col.getGreen() / 255.0) * 255);
                int newBlue = (int) ((currentBlue / 255.0) * (col.getBlue() / 255.0) * 255);
                int newCol = (newAlpha << 24) | (newRed << 16) | (newGreen << 8) | newBlue; //bit shift it all together
                img.setRGB(x, y, newCol); //set the color of the pixel to the new color
            }
        }
        return new ImageIcon(img); //return the new bufferedimage
    }
}