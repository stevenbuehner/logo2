/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package interfaces;

import Klassen.Spielfeld;

/**
 * Das ist das Interface fuer die Steuerung. Die folgenden Funktionen muessen daher implementiert sein.
 * @author tommy
 */
public interface SteuerungIntface {
    /**
     * Am Anfang des Spieles muss ein Spielfeld initialisiert werden.
     * @param spielerNameSchwarz
     * @param spielerNameWeiss
     * @param spielZeit1
     * @param spielZeit2
     * @param komiFuerWeiss
     * @param spielfeldGroesse
     * @param vorgabeSteineFuerSchwarz
     */
    public void initMitEinstellungen(String spielerNameSchwarz, String spielerNameWeiss, long spielZeit1, long spielZeit2, float komiFuerWeiss, int spielfeldGroesse, int vorgabeSteineFuerSchwarz);
    public void initMitEinstellungen(String spielerNameSchwarz, String spielerNameWeiss, long spielZeit1, long spielZeit2, float komiFuerWeiss, int spielfeldGroesse);
    public void initMitEinstellungen(String spielerNameSchwarz, String spielerNameWeiss, long spielZeit1, long spielZeit2, int spielfeldGroesse);
    public void initMitEinstellungenFuerStartformation(String spielerNameSchwarz, String spielerNameWeiss, long spielZeit1, long spielZeit2, float komiFuerWeiss, int spielfeldGroesse);
    public void initMitEinstellungenFuerStartformation(String spielerName1, String spielerName2, long spielZeit1, long spielZeit2, int spielfeldGroesse);
    public void initMitDatenModell(Spielfeld feld, String spielerNameSchwarz, String spielerNameWeiss, long spielZeit1, long spielZeit2, float komiFuerWeiss);
    public void klickAufFeld(int xPos, int yPos);
    public void buttonAufgeben();
    public void buttonPassen();
    public void buttonPause();
    public void buttonSpielSpeichern();
    public void buttonUndo();
    public void buttonRedo();
    public void buttonToStart();
    public void buttonToEnd();
    public void zeitAbgelaufenSpieler1Hauptzeit();
    public void zeitAbgelaufenSpieler1Periodenzeit();
    public void zeitAbgelaufenSpieler2Hauptzeit();
    public void zeitAbgelaufenSpieler2Periodenzeit();
}
