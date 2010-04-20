package interfaces;

import java.awt.Graphics;

/**
 *
 * @author steven
 * @version 0.1
 *
 * Klasse bekommt im Konstruktor xPos, yPos linkeEcke oben und die Winkelabweichung
 * senkrecht zum Lot. Im Uhrzeigersinn positive Werte. Negative Werte sind gegen
 * den Uhrzeigersin.
 */
public interface SpielerZettel {

    public void setSpielername( String spielername );

    public void setGefangenenAnzahl( int anzahl );

    public void setFehlermeldung( String fehlermeldung );

    public void zeichneDich(Graphics g);

}
