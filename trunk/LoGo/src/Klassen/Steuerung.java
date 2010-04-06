/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Klassen;

import interfaces.SteuerungIntface;

/**
 *
 * @author steven
 * @version 0.1
 * @param Die Klasse dient zum Verwalten der Daten eines Spielers
 */
public class Steuerung implements SteuerungIntface {

    private Spieler spielerSchwarz;
    private Spieler spielerWeiss;

    /* Wenn die Spielerzeit aufgebraucht ist gibt es noch die Periodenzeit.
    * Diese ist fuer alle Spieler gleich.
    */
    private long    periodenZeit;   
    private Spielfeld dasSpielfeld;


    public void Steuerung( ){

    }

    public void initMitEinstellungen(String spielerName1, String spielerName2, long spielZeit1, long spielZeit2, float komiFuerWeiss, int spielfeldGroesse, int vorgabeSteineFuerSchwarz) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void initMitEinstellungen(String spielerName1, String spielerName2, long spielZeit1, long spielZeit2, float komiFuerWeiss, int spielfeldGroesse) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void initMitEinstellungen(String spielerName1, String spielerName2, long spielZeit1, long spielZeit2, int spielfeldGroesse) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void initMitEinstellungenFuerStartformation(String spielerName1, String spielerName2, long spielZeit1, long spielZeit2, float komiFuerWeiss, int spielfeldGroesse) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void initMitEinstellungenFuerStartformation(String spielerName1, String spielerName2, long spielZeit1, long spielZeit2, int spielfeldGroesse) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void initMitDatenModell(Spielfeld feld, String spielerName1, String spielerName2, long spielZeit1, long spielZeit2, float komiFuerWeiss) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void klickAufFeld(int xPos, int yPos) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void buttonAufgeben() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void buttonPassen() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void buttonPause() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void buttonSpielSpeichern() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void buttonUndo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void buttonRedo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void buttonToStart() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void buttonToEnd() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void zeitAbgelaufenSpieler1Hauptzeit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void zeitAbgelaufenSpieler1Periodenzeit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void zeitAbgelaufenSpieler2Hauptzeit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void zeitAbgelaufenSpieler2Periodenzeit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
