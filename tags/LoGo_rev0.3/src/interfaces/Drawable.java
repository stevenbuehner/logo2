package interfaces;

import java.awt.Graphics;

/**
 * Interface fuer Objekte die sich selbst auf die Oberflaeche zeichenn koennen
 * @author steven
 * @version 1.0
 * @since $date: $
 */
public interface Drawable {

    /**
     * Methode zum zeichen der Objekte
     * @param g
     */
    public void drawObjects(Graphics g);

}
