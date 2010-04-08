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

        /**
     * Am Anfang des Spieles muss ein Spielfeld initialisiert werden.
     * @param spielerNameSchwarz Name des schwarzen Spielers
     * @param spielerNameWeiss Name des weissen Spielers
     * @param spielZeitSchwarz Absolute Zeit des schwarzen Spielers
     * @param spielZeitWeiss Absolute Zeit des weissen Spielers
     * @param periodenZeit Byo-Yomi fuer beide Spieler
     * @param komiFuerWeiss Punkte zum Spielstaerkenausgleich fuer Weiss
     * @param spielfeldGroesse Groesse des Spielfeldes
     * @param vorgabeSteineFuerSchwarz Vorgabe zum Spielstaerkeausgleich fuer
     * Schwarz
     */
    public void initMitEinstellungen(
            String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeitSchwarz,
            long spielZeitWeiss,
            long periodenZeit,
            float komiFuerWeiss,
            int spielfeldGroesse,
            int vorgabeSteineFuerSchwarz) {

        Spielfeld tmpSpielfeld   = new Spielfeld(spielfeldGroesse);

        tmpSpielfeld.setSpielerSchwarz( new Spieler(spielerNameSchwarz, spielZeitSchwarz, 0 ) );
        tmpSpielfeld.setSpielerWeiss( new Spieler(spielerNameWeiss, spielZeitWeiss, komiFuerWeiss) );
        tmpSpielfeld.setPeriodenZeit(periodenZeit);


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

        this.initMitSpielfeld(tmpSpielfeld);

          //Zum Testen wurde dies hier auskommentiert, da es sonst nicht funktionieren wuerde
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

    /**
     *
     * @param Bevorzugter Initialisierer des Spieles, der gleich eingesamtes
     * Spielfeld als bereitsInitialisiertesSpielfeld ueberigbt.
     */
    public void initMitSpielfeld( Spielfeld bereitsInitialisiertesSpielfeld ){
        /*
         * Ueberpruefroutinen fuer das Spielfeld
         * Diese muessen noch programmiert werden ...
        */
        this.dasSpielfeld = bereitsInitialisiertesSpielfeld;
    }

   /**
     * Die Gleiche Funktion wie initMitEinstellungen, nur damit das Programm
     * weiss, dass dem Benutzer noch die Moeglichkeit geboten werden muss
     * eine eigene Startformation zu erstellen
     * @param spielerNameSchwarz Name des schwarzen Spielers
     * @param spielerNameWeiss Name des weissen Spielers
     * @param spielZeitSchwarz Absolute Zeit des schwarzen Spielers
     * @param spielZeitWeiss Absolute Zeit des weissen Spielers
     * @param periodenZeit Byo-Yomi fuer beide Spieler
     * @param komiFuerWeiss Punkte zum Spielstaerkenausgleich fuer Weiss
     * @param spielfeldGroesse Groesse des Spielfeldes
     */
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

    /**
     * Dient dafuer, ein Spiel zu laden und das komplette Feld zu uebergeben.
     * Spielfeldgroesse muss nicht uebergeben werden, da diese im Spielfeld
     * gespeichert ist.
     * @param feld Spielfeld, welches schon fertig und gueltig konstruiert wurde
     * @param spielerNameSchwarz Name des schwarzen Spielers
     * @param spielerNameWeiss Name des weissen Spielers
     * @param spielZeitSchwarz Absolute Zeit des schwarzen Spielers
     * @param spielZeitWeiss Absolute Zeit des weissen Spielers
     * @param periodenZeit Byo-Yomi fuer beide Spieler
     * @param komiFuerWeiss Punkte zum Spielstaerkenausgleich fuer Weiss
     */
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

    /**
     * Wenn bei der GUI auf einen Schnittpunkt geklickt wurde, muss die
     * Steuerung reagieren. Die Koordinaten werden dann uebermittelt
     * @param xPos X-Koordinate (1-Spielfeldgroesse)
     * @param yPos Y-Koordinate (1-Spielfeldgroesse)
     */
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

    /**
     * Spieler klickt auf Aufgeben. Steuerung muss Dialogfeld od. Aehnliches
     * Anzeigen und Spiel dann beenden
     */
    public void buttonAufgeben() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Spieler klickt auf Passen. Spielzug muss ausgefuehrt werden und der
     * naechste ist dran. Wird 2 mal hintereinander gepasst, wird das Spiel
     * beendet und gezaehlt.
     */
    public void buttonPassen() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Spieler klickt auf Pause. Das Spiel, und damit die Spielzeit, wird
     * angehalten. Das Brett wird abgedunkelt.
     */
    public void buttonPause() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Spieler klickt auf Speichern. Spiel muss als sgf gespeichert werden.
     */
    public void buttonSpielSpeichern() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Spieler klickt auf Undo. Spielzug wird rueckgaengig gemacht
     */
    public void buttonUndo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Spieler klickt auf Redo. Spielzug der geUndot wurde wird rueckgaengig
     * gemacht (Es wir einfach um 1 nach vorn gegangen)
     */
    public void buttonRedo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Spieler klickt auf Zu-Start-Button. Anfangssituation wird geladen.
     */
    public void buttonToStart() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Spieler klickt auf Zu-Ende-Button. Letzte Situation auf Brett wird
     * hergestellt.
     */
    public void buttonToEnd() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Hauptzeit des schwarzen Spielers ist abgelaufen. Periodenzeit muss starten
     */
    public void zeitAbgelaufenSchwarzHauptzeit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Periodenzeit des schwarzen Spielers ist abgelaufen. Schwarz verliert.
     * Spiel beendet.
     */
    public void zeitAbgelaufenSchwarzPeriodenzeit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Hauptzeit des weissen Spielers ist abgelaufen. Periodenzeit muss starten
     */
    public void zeitAbgelaufenWeissHauptzeit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Periodenzeit des weissen Spielers ist abgelaufen. Weiss verliert.
     * Spiel beendet.
     */
    public void zeitAbgelaufenWeissPeriodenzeit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @return Getter-Funktion für das Spielfeld
     */
    public Spielfeld getSpielfeld(){
        return this.dasSpielfeld;
    }

}
