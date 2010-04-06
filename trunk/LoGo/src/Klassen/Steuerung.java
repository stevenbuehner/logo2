/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Klassen;

import interfaces.SteuerungIntface;
import logo.LoGoApp;

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

    // Die Datenklasse
    private Spielfeld dasSpielfeld;


    public Steuerung( ){
        this( 9, 60*1000 );     // Standardwerte
    }

    public Steuerung ( int spielFeldGroesse, long periodenZeit ){
        this.dasSpielfeld   = new Spielfeld( spielFeldGroesse );
        this.periodenZeit   = periodenZeit;
        this.spielerSchwarz = new Spieler("Steven", 100000, 0 );
        this.spielerWeiss   = new Spieler("Steven", 100000, 0 );
    }

    public void initMitEinstellungen(
            String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeit1,
            long spielZeit2,
            float komiFuerWeiss,
            int spielfeldGroesse,
            int vorgabeSteineFuerSchwarz) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void initMitEinstellungen(
            String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeit1,
            long spielZeit2,
            float komiFuerWeiss,
            int spielfeldGroesse) {
        
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void initMitEinstellungen(
            String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeit1,
            long spielZeit2,
            int spielfeldGroesse) {


        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void initMitEinstellungenFuerStartformation(
            String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeit1,
            long spielZeit2,
            float komiFuerWeiss,
            int spielfeldGroesse) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void initMitEinstellungenFuerStartformation(
            String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeit1,
            long spielZeit2,
            int spielfeldGroesse) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void initMitDatenModell(
            Spielfeld feld,
            String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeit1,
            long spielZeit2,
            float komiFuerWeiss) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void klickAufFeld(int xPos, int yPos) {
        
        System.out.println( "Klick auf Punkt (" + xPos + "|" + yPos + ")" );

        Spielfeld brett = this.dasSpielfeld;
        
        boolean returnWert = this.dasSpielfeld.setStein(xPos, yPos, Konstante.SCHNITTPUNKT_SCHWARZ);

        LoGoApp.meineOberflaeche.setBrettOberflaeche(
                this.dasSpielfeld.getAktuelesSpielFeld(),
                this.dasSpielfeld.getSpielfeldGroesse());
       
        throw new UnsupportedOperationException("Not fully supported yet.");
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
