package GUI;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * Objekte dieser Klasse können einem JFrame als Panel zugeordnet werden.
 * Die Klasse dient dazu, um schnell und einfach ein Hintergrundbild zu einer
 * Oberfläche hinzuzufügen.
 * @author steven
 */
public class BackgroundImagePanel extends JPanel {

    BufferedImage[] images;
    int imageIndex = 0;

    /**
     * Konstruktor der Klasse
     * @param image Bild, das im Hintergrund des Panels angezeigt werden soll
     */
    public BackgroundImagePanel(BufferedImage image) {
        this.images = new BufferedImage[1];
        this.images[0] = image;
        setOpaque(true);
        setLayout(new BorderLayout());
    }

    /**
     * @param images Bilder (Mehrzahl) die im Hintergrund des Panels animiert
     * werden sollen.
     * @see showNextImage()
     * @see #paintComponent(java.awt.Graphics) 
     */
    public BackgroundImagePanel(BufferedImage[] images) {
        this.images = images;
        setOpaque(true);                // required for content panes
        setLayout(new BorderLayout());  // default for content pane
    }

    /**
     * Überschriebene paint-Funktion, welche das GrafikObjekt auf den Hintergrund
     * zeichnet. Soll der Hintergrund animiert sein, muss ein Image-Array mit
     * dem Konstruktor bereits übergeben worden sein. Außerdem muss die Funktion
     * showNextImage() zyklisch aufgerufen werden.
     * @param g Graphics Objekt um darauf das Hintergrundbild zu zeichnen
     * @see #showNextImage() 
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();
        // Center the current image.
        int x = (w - images[imageIndex].getWidth()) / 2;
        int y = (h - images[imageIndex].getHeight()) / 2;
        g.drawImage(images[imageIndex], x, y, this);
    }

    /**
     * Diese Methode muss zyklisch aufgerufen werden, wenn ein animierter
     * Hintergrund erwünscht ist. Sie inkrementiert den Zähler auf das im Konstruktor
     * übergebene Image-Array. Wenn das Array am Ende angelangt ist, beginnt
     * der Zähler wieder bei 0.
     */
    public void showNextImage() {
        imageIndex++;
        if (imageIndex > images.length - 1) {
            imageIndex = 0;
        }
        repaint();
    }
}
