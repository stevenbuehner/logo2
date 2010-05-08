package Interfaces;

import java.awt.Graphics;

/**
 * Definiert die Zugriffsweisen auf von der GUI auf die Spieleruhren.
 * Klasse bekommt im Konstruktor xPos, yPos vom Mittelpunkt, den Radius
 * und die Startzeit
 * @author steven, tommy
 * @version 0.1
 */
public interface SpielerUhren {

    /**
     * Funktion um der Uhr die restliche Zeit in Millesekunden zu übergeben,
     * die der Spieler noch hat. Diese Funktion wird periodisch aufgerufen,
     * immer wenn sich die Anzeige ändern soll.
     * @param zeit Zeit in Millesekunden.
     */
    public void restzeitInMS(long restzeit);

    /**
     * @param istAktiv Der Parameter dient dazu in der Uhr einzustellen,
     * ob diese als aktiviert oder als inaktiv dargestellt werden soll.
     *
     */
    public void uhrAktiv(boolean istAktiv);

    /**
     *
     * Zum zeichnen der Uhr wird diese Funktion bei jeder Zeichenoperation
     * zyklisch von der Oberfläche aufgerufen. Die Uhr muss dann auf das
     * Graphics Objekt g gezeichnet werden. Die Position und der Radius
     * der Uhr werden dieser bereits im Konstruktor übergeben.
     * @param g Graphics Objekt g
     */
    public void zeichneZeiger(Graphics g);
}
