package Images;

import java.awt.Image;
import javax.swing.ImageIcon;

public class ImageManager {
    public static ImageIcon wp = getInstance().wp_;
    private ImageIcon wp_ = new ImageIcon(getClass().getResource("/Images/wp.png"));
    public static ImageIcon bp = getInstance().bp_;
    private ImageIcon bp_ = new ImageIcon(getClass().getResource("/Images/bp.png"));

    public static ImageIcon wn = getInstance().wn_;
    private ImageIcon wn_ = new ImageIcon(getClass().getResource("/Images/wn.png"));
    public static ImageIcon bn = getInstance().bn_;
    private ImageIcon bn_ = new ImageIcon(getClass().getResource("/Images/bn.png"));

    public static ImageIcon wb = getInstance().wb_;
    private ImageIcon wb_ = new ImageIcon(getClass().getResource("/Images/wb.png"));
    public static ImageIcon bb = getInstance().bb_;
    private ImageIcon bb_ = new ImageIcon(getClass().getResource("/Images/bb.png"));

    public static ImageIcon wr = getInstance().wr_;
    private ImageIcon wr_ = new ImageIcon(getClass().getResource("/Images/wr.png"));
    public static ImageIcon br = getInstance().br_;
    private ImageIcon br_ = new ImageIcon(getClass().getResource("/Images/br.png"));

    public static ImageIcon wq = getInstance().wq_;
    private ImageIcon wq_ = new ImageIcon(getClass().getResource("/Images/wq.png"));
    public static ImageIcon bq = getInstance().bq_;
    private ImageIcon bq_ = new ImageIcon(getClass().getResource("/Images/bq.png"));

    public static ImageIcon wk = getInstance().wk_;
    private ImageIcon wk_ = new ImageIcon(getClass().getResource("/Images/wk.png"));
    public static ImageIcon bk = getInstance().bk_;
    private ImageIcon bk_ = new ImageIcon(getClass().getResource("/Images/bk.png"));
    
    public static ImageIcon board = getInstance().board_;
    private ImageIcon board_ = new ImageIcon(getClass().getResource("/Images/board.png"));

    private static ImageManager instance = null;

    private static ImageManager getInstance() {
        if (instance == null)
            instance = new ImageManager();

        return instance;
    }

    private ImageManager() {
    }

    public static ImageIcon resize(ImageIcon original, int width, int height) {
        Image imgOriginal = original.getImage();
        Image imgScaled = imgOriginal.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(imgScaled);
    }
}