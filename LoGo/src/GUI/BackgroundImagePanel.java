package GUI;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author steven
 */
public class BackgroundImagePanel extends JPanel {
    BufferedImage[] images;
    int imageIndex = 0;

    public BackgroundImagePanel( BufferedImage image ){
        this.images = new BufferedImage[1];
        this.images[0] = image;
        setOpaque(true);
        setLayout(new BorderLayout());
    }

    public BackgroundImagePanel(BufferedImage[] images) {
        this.images = images;
        setOpaque(true);                // required for content panes
        setLayout(new BorderLayout());  // default for content pane
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();
        // Center the current image.
        int x = (w - images[imageIndex].getWidth())/2;
        int y = (h - images[imageIndex].getHeight())/2;
        g.drawImage(images[imageIndex], x, y, this);
    }

    public void showNextImage() {
        imageIndex++;
        if(imageIndex > images.length-1)
            imageIndex = 0;
        repaint();
    }
}