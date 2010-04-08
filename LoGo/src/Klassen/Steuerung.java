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

    // Die Datenklasse
    private Spielfeld dasSpielfeld;


    public Steuerung( ){
        this( 9, 60*1000 );     // Standardwerte
    }

    public Steuerung ( int spielFeldGroesse, long periodenZeit ){

        // Initialisiere nicht angegebenes mit Standardwerten
        this.initMitEinstellungen(
                "Steven",
                "Tommy",
                30*60*1000,
                30*60*1000,
                60*1000,
                0,
                spielFeldGroesse,
                0);
    }

    public void initMitEinstellungen(
            String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeitSchwarz,
            long spielZeitWeiss,
            long periodenZeit,
            float komiFuerWeiss,
            int spielfeldGroesse,
            int vorgabeSteineFuerSchwarz) {

        this.dasSpielfeld   = new Spielfeld(spielfeldGroesse);
        this.dasSpielfeld.setSpielerSchwarz( new Spieler(spielerNameSchwarz, spielZeitSchwarz, 0 ) );
        this.dasSpielfeld.setSpielerWeiss( new Spieler(spielerNameWeiss, spielZeitWeiss, komiFuerWeiss) );
        this.dasSpielfeld.setPeriodenZeit(periodenZeit);


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




//        throw new UnsupportedOperationException("Not fully supported yet.");
    }

    public void initMitEinstellungen(
            String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeitSchwarz,
            long spielZeitWeiss,
            long periodenZeit,
            float komiFuerWeiss,
            int spielfeldGroesse) {

        this.initMitEinstellungen(
                spielerNameSchwarz,
                spielerNameWeiss,
                spielZeitSchwarz,
                spielZeitWeiss,
                periodenZeit,
                komiFuerWeiss,
                spielfeldGroesse,
                0);
    }

    public void initMitEinstellungen(
            String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeitSchwarz,
            long spielZeitWeiss,
            long periodenZeit,
            int spielfeldGroesse) {

            this.initMitEinstellungen(
                spielerNameSchwarz,
                spielerNameWeiss,
                spielZeitSchwarz,
                spielZeitWeiss,
                periodenZeit,
                0,
                spielfeldGroesse,
                0);
    }

    public void initMitEinstellungenFuerStartformation(
            String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeitSchwarz,
            long spielZeitWeiss,
            long periodenZeit,
            float komiFuerWeiss,
            int spielfeldGroesse) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void initMitEinstellungenFuerStartformation(
            String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeitSchwarz,
            long spielZeitWeiss,
            long periodenZeit,
            int spielfeldGroesse) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void initMitDatenModell(
            Spielfeld feld,
            String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeitSchwarz,
            long spielZeitWeiss,
            long periodenZeit,
            float komiFuerWeiss) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void klickAufFeld(int xPos, int yPos) {
        
        System.out.println( "Klick auf Punkt (" + xPos + "|" + yPos + ")" );

        Spielfeld brett = this.dasSpielfeld;
        
        int returnWert = this.dasSpielfeld.setStein(xPos, yPos);

        switch (returnWert){
            case 1:
                // Rueckgabe erfolgreich! Spielerwechsel
                LoGoApp.meineOberflaeche.setBrettOberflaeche(
                    this.dasSpielfeld.getAktuelesSpielFeld(),
                    this.dasSpielfeld.getSpielfeldGroesse());
                break;
            case 0:
                LoGoApp.meineOberflaeche.gibFehlermeldungAus("Verboten: Spielpunkt befindet sich nicht auf dem Brett" );
                break;
            case -1:
                LoGoApp.meineOberflaeche.gibFehlermeldungAus("Koregel: Verbotener Zug" );
                break;
            case -2:
                LoGoApp.meineOberflaeche.gibFehlermeldungAus("Dieser Schnittpunkt ist bereits belegt" );
                break;
            case -3:
                LoGoApp.meineOberflaeche.gibFehlermeldungAus("Selbstmord ist nicht erlaubt!" );
                break;
            default:
                // Das darf nicht vorkommen
                throw new UnsupportedOperationException("Dieser Wert darf nie vorkommen");
        }


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

    public void zeitAbgelaufenSchwarzHauptzeit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void zeitAbgelaufenSchwarzPeriodenzeit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void zeitAbgelaufenWeissHauptzeit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void zeitAbgelaufenWeissPeriodenzeit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
