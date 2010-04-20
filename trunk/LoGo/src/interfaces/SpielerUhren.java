package interfaces;

import java.awt.Graphics;

/**
 *
 * @author steven, tommy
 * @version 0.1
 *
 * Klasse bekommt im Konstruktor xPos, yPos vom Mittelpunkt, den Radius
 * und die Startzeit
 *
 */
public interface SpielerUhren {

    public void restzeitInMS( long zeit);

    public void uhrAktiv( boolean istAktiv );

    public void zeichneZeiger(Graphics g);

}
