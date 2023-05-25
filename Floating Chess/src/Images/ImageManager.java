package Images;

import java.awt.Image;
import javax.swing.ImageIcon;

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

    private ImageManager() {
    }

    public static ImageIcon resize(ImageIcon original, int width, int height) {
        Image imgOriginal = original.getImage();
        Image imgScaled = imgOriginal.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(imgScaled);
    }
}