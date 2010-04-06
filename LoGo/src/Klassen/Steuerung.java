package Klassen;

import interfaces.SteuerungIntface;
import logo.LoGoApp;

/**
 *
 * @author steven
 * @version 0.2
 * @param Die Klasse dient zur Steuerung des Spielflusses
 */
public class Steuerung implements SteuerungIntface {

    private Spieler spielerSchwarz;
    private Spieler spielerWeiss;

    /* Wenn die Spielerzeit aufgebraucht ist gibt es noch die Periodenzeit.
    * Diese ist fuer alle Spieler gleich. In wirklich heisst dieser Wert Byo-yomi.
    */
    private long    periodenZeit;

    // Die Datenklasse
    private Spielfeld dasSpielfeld;


    public Steuerung( ){
        this( 9, 60*1000 );     // Standardwerte
    }

    public Steuerung ( int spielFeldGroesse, long periodenZeit ){
        this.periodenZeit   = periodenZeit;

        // Initialisiere nicht angegebenes mit Standardwerten
        this.initMitEinstellungen(
                "Steven",
                "Tommy",
                30*60*1000,
                30*60*1000,
                0,
                spielFeldGroesse);
    }

    public void initMitEinstellungen(
            String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeitSchwarz,
            long spielZeitWeiss,
            float komiFuerWeiss,
            int spielfeldGroesse,
            int vorgabeSteineFuerSchwarz) {

        this.spielerSchwarz = new Spieler(spielerNameSchwarz, spielZeitSchwarz, 0 );
        this.spielerWeiss   = new Spieler(spielerNameWeiss, spielZeitWeiss, komiFuerWeiss);
        this.dasSpielfeld   = new Spielfeld(spielfeldGroesse);
        this.periodenZeit   = 60*2*1000;    //Standardwert 2 Min


        switch( vorgabeSteineFuerSchwarz ){
            case 0:
                // Nichts zu tun
                break;
                /*
                 * Hier muessen noch die Einstellungen fuer das Spielfeld in Form
                 * von setzeStein();
                 */
                
            default:
                break;
        }




        throw new UnsupportedOperationException("Not fully supported yet.");
    }

    public void initMitEinstellungen(
            String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeitSchwarz,
            long spielZeitWeiss,
            float komiFuerWeiss,
            int spielfeldGroesse) {

        this.initMitEinstellungen(
                spielerNameSchwarz,
                spielerNameWeiss,
                spielZeitSchwarz,
                spielZeitWeiss,
                komiFuerWeiss,
                spielfeldGroesse,
                0);
    }

    public void initMitEinstellungen(
            String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeitSchwarz,
            long spielZeitWeiss,
            int spielfeldGroesse) {

            this.initMitEinstellungen(
                spielerNameSchwarz,
                spielerNameWeiss,
                spielZeitSchwarz,
                spielZeitWeiss,
                0,
                spielfeldGroesse,
                0);
    }

    public void initMitEinstellungenFuerStartformation(
            String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeitSchwarz,
            long spielZeitWeiss,
            float komiFuerWeiss,
            int spielfeldGroesse) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void initMitEinstellungenFuerStartformation(
            String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeitSchwarz,
            long spielZeitWeiss,
            int spielfeldGroesse) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void initMitDatenModell(
            Spielfeld feld,
            String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeitSchwarz,
            long spielZeitWeiss,
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
