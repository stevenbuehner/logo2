package interfaces;

/**
 *
 * @author steven
 * @version 0.1
 *
 * Klasse bekommt im Konstruktor xPos, yPos vom Mittelpunkt, den Radius
 * und die Startzeit
 *
 */
public interface SpielerUhren {

    public void restzeitInMS( long zeit);

    public void uhrAktiv( boolean istAktiv );

}
