package Interfaces;

import java.awt.Graphics;

/**
 * Interface für Objekte die sich selbst auf die Oberflaeche zeichen koennen.
 * @author steven
 * @version 1.0
 */
public interface Drawable {

    /**
     * Methode zum Zeichen der Objekte auf das Graphics-Objekt.
     * @param g Graphics Objekt
     */
    public void drawObjects(Graphics g);
}
