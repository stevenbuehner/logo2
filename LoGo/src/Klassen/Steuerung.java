package Klassen;

import Timer.Countdown;
import Timer.CountdownPeriodenZeitSchwarz;
import Timer.CountdownPeriodenZeitWeiss;
import Timer.CountdownSpielerZeitSchwarz;
import Timer.CountdownSpielerZeitWeiss;
import interfaces.SteuerungIntface;
import logo.LoGoApp;

/**
 * Die Klasse dient zur Steuerung des Spielflusses
 * @author steven
 * @version 0.2
 */
public class Steuerung implements SteuerungIntface {

    // Die Datenklasse
    private Spielfeld dasSpielfeld;


    // Timer fuer Spieler und PeriodenZeiten.
    Countdown   periodenZeitSchwarz;
    Countdown   periodenZeitWeiss;
    Countdown   spielerZeitSchwarz;
    Countdown   spielerZeitWeiss;

    
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

        tmpSpielfeld.setSpielerSchwarz( new Spieler(spielerNameSchwarz, spielZeitSchwarz, 0, 0 ) );
        tmpSpielfeld.setSpielerWeiss( new Spieler(spielerNameWeiss, spielZeitWeiss, 0, komiFuerWeiss) );
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
     * @param bereitsInitialisiertesSpielfeld Bevorzugter Initialisierer des Spieles, der gleich eingesamtes
     * Spielfeld als bereitsInitialisiertesSpielfeld ueberigbt.
     */
    public void initMitSpielfeld( Spielfeld bereitsInitialisiertesSpielfeld ){
        /*
         * Ueberpruefroutinen fuer das Spielfeld
         * Diese muessen noch programmiert werden ...
        */
        this.dasSpielfeld = bereitsInitialisiertesSpielfeld;

        // Timer initialisieren
        // Vorraussetzung zum Initialisieren ist ein Objekt vom Typ Spieler in this.dasSpielfeld
        if (this.dasSpielfeld.getSpielerSchwarz() != null ||
                this.dasSpielfeld.getSpielerWeiss() != null ){
            /*
        this.periodenZeitSchwarz    = new CountdownPeriodenZeitSchwarz(
                false,
                this.dasSpielfeld.getPeriodenZeit());
        this.periodenZeitWeiss      = new CountdownPeriodenZeitWeiss(
                false,
                this.dasSpielfeld.getPeriodenZeit());
        this.spielerZeitSchwarz     = new CountdownSpielerZeitSchwarz(
                false,
                this.dasSpielfeld.getSpielerSchwarz().getVerbleibendeSpielzeitInMS());
        this.spielerZeitWeiss     = new CountdownSpielerZeitWeiss(
                false,
                this.dasSpielfeld.getSpielerWeiss().getVerbleibendeSpielzeitInMS());
       */ }
        else{
            throw new UnsupportedOperationException("Timer können nicht initialisiert werden: Fehlendes Spieler-Objekt in Spielfeld");
        }


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

        // Zum Debuggen
        System.out.println( "Klick auf Punkt (" + xPos + "|" + yPos + ")" );

        // Variablendeklaration
        Spielfeld brett         = this.dasSpielfeld;
        int returnWert          = Konstante.FEHLER;
        int klickenderSpieler   = brett.getSpielerAnDerReihe();

        if(brett.getSpielZustand() != Konstante.SPIEL_LAUEFT)
            return;


        // Timer während der Berechnung stoppen und die Zeiten zum Spieler zurückspeichern
        if( brett.getSpielerAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ ){
            this.spielerZeitSchwarz.stoppeCountdown();
            this.periodenZeitSchwarz.stoppeCountdown();
            brett.getSpielerSchwarz().setVerbleibendeSpielzeitInMS(this.spielerZeitSchwarz.getRemainingTime());
        }
        else{
            this.spielerZeitWeiss.stoppeCountdown();
            this.spielerZeitSchwarz.stoppeCountdown();
            brett.getSpielerWeiss().setVerbleibendeSpielzeitInMS(this.spielerZeitWeiss.getRemainingTime());
        }

        // Versuche den Stein zu setzen und speichere das Resulatat in returnWert
        returnWert = this.dasSpielfeld.macheZug(xPos, yPos);

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


        // aktualisiere die GefangenenAnzahl
        LoGoApp.meineOberflaeche.setGefangeneSteineSchwarz(brett.getSpielerSchwarz().getGefangenenAnzahl());
        LoGoApp.meineOberflaeche.setGefangeneSteineWeiss(brett.getSpielerWeiss().getGefangenenAnzahl());


        // Setze das Spiel wieder fort und starte die nötigen Timer
        if( this.dasSpielfeld.getSpielerAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ ){
            // Wenn der Spieler keine verbleibende Spielzeit mehr hat, verwende den Periodentimer
            if( brett.getSpielerSchwarz().getVerbleibendeSpielzeitInMS() > 0 ){
                this.spielerZeitSchwarz.setRemainingTime( brett.getSpielerSchwarz().getVerbleibendeSpielzeitInMS() );
            }
            else{
                // Wenn der Spieler einen ungültigen Zug geklickt hat, spiele mit den vorherigen Periodenzeiten weiter
                // Bei Spielerwechsel, bekommt der Timer wieder die Periodenzeit auf Maximal gesetzt
                if( klickenderSpieler != brett.getSpielerAnDerReihe() )
                    this.periodenZeitSchwarz.setRemainingTime(brett.getPeriodenZeit());

                // Starte den Countdown, bzw. setze den Countdown fort
                this.periodenZeitSchwarz.starteCountdown();
            }
            // Hat der Spieler noch eigene Spielzeit, dann mache mit dieser weiter
            this.spielerZeitSchwarz.setRemainingTime(brett.getSpielerSchwarz().getVerbleibendeSpielzeitInMS());
            this.spielerZeitSchwarz.starteCountdown();
        }
        else{
            // Wenn der Spieler keine verbleibende Spielzeit mehr hat, verwende den Periodentimer
            if( brett.getSpielerWeiss().getVerbleibendeSpielzeitInMS() > 0 ){
                this.spielerZeitWeiss.setRemainingTime( brett.getSpielerWeiss().getVerbleibendeSpielzeitInMS() );
            }
            else{
                // Wenn der Spieler einen ungültigen Zug geklickt hat, spiele mit den vorherigen Periodenzeiten weiter
                // Bei Spielerwechsel, bekommt der Timer wieder die Periodenzeit auf Maximal gesetzt
                if( klickenderSpieler != brett.getSpielerAnDerReihe() )
                    this.periodenZeitWeiss.setRemainingTime(brett.getPeriodenZeit());

                // Starte den Countdown, bzw. setze den Countdown fort
                this.periodenZeitWeiss.starteCountdown();
            }
            // Hat der Spieler noch eigene Spielzeit, dann mache mit dieser weiter
            this.spielerZeitWeiss.setRemainingTime(brett.getSpielerWeiss().getVerbleibendeSpielzeitInMS());
            this.spielerZeitWeiss.starteCountdown();        }

    }

     /**
     * Spieler klickt auf "Spiel Starten" damit wird dann die Validierung
     * des Spielfelds vorgenommen und bei Erfolg das Spiel gestartet.
     */
    public void buttonSpielStarten(){
        // Überprüfe ob die Initialisierung korrekt war

        if( this.dasSpielfeld != null ){
            this.dasSpielfeld.spielfeldValidiert();

            // Wenn das Spiel validiert ist, starte ein neues Spiel
            if(this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_VALIDIERT){
                this.dasSpielfeld.setSpielZustand( Konstante.SPIEL_LAUEFT);


                // Timer initialisieren
                // Vorraussetzung zum Initialisieren ist ein Objekt vom Typ Spieler in this.dasSpielfeld
                if (this.dasSpielfeld.getSpielerSchwarz() != null ||
                        this.dasSpielfeld.getSpielerWeiss() != null ){
                this.periodenZeitSchwarz    = new CountdownPeriodenZeitSchwarz(
                        false,
                        this.dasSpielfeld.getPeriodenZeit());
                this.periodenZeitWeiss      = new CountdownPeriodenZeitWeiss(
                        false,
                        this.dasSpielfeld.getPeriodenZeit());
                this.spielerZeitSchwarz     = new CountdownSpielerZeitSchwarz(
                        false,
                        this.dasSpielfeld.getSpielerSchwarz().getVerbleibendeSpielzeitInMS());
                this.spielerZeitWeiss     = new CountdownSpielerZeitWeiss(
                        false,
                        this.dasSpielfeld.getSpielerWeiss().getVerbleibendeSpielzeitInMS());
                }

                
                // Oberfläche füllen
                LoGoApp.meineOberflaeche.setGefangeneSteineSchwarz(
                        this.dasSpielfeld.getSpielerSchwarz().getGefangenenAnzahl());
                LoGoApp.meineOberflaeche.setGefangeneSteineWeiss(
                        this.dasSpielfeld.getSpielerWeiss().getGefangenenAnzahl());
                LoGoApp.meineOberflaeche.setSpielernameSchwarz(
                        this.dasSpielfeld.getSpielerSchwarz().getSpielerName());
                LoGoApp.meineOberflaeche.setSpielernameWeiss(
                        this.dasSpielfeld.getSpielerWeiss().getSpielerName());

                // Zeiten auf der Oberfläche anzeigen
                LoGoApp.meineOberflaeche.setAnzeigePeriodenZeitSchwarz(
                        this.dasSpielfeld.getPeriodenZeit());
                LoGoApp.meineOberflaeche.setAnzeigePeriodenZeitWeiss(
                        this.dasSpielfeld.getPeriodenZeit());
                LoGoApp.meineOberflaeche.setAnzeigeSpielerZeitSchwarz(
                        this.dasSpielfeld.getSpielerSchwarz().getVerbleibendeSpielzeitInMS());
                LoGoApp.meineOberflaeche.setAnzeigeSpielerZeitWeiss(
                        this.dasSpielfeld.getSpielerWeiss().getVerbleibendeSpielzeitInMS());

                // Initialisiere alle benötigten Timer neu
                this.periodenZeitSchwarz.setRemainingTime(this.dasSpielfeld.getPeriodenZeit());
                this.periodenZeitWeiss.setRemainingTime(this.dasSpielfeld.getPeriodenZeit());
                this.spielerZeitSchwarz.setRemainingTime(this.dasSpielfeld.getSpielerSchwarz().getVerbleibendeSpielzeitInMS());
                this.spielerZeitWeiss.setRemainingTime(this.dasSpielfeld.getSpielerWeiss().getVerbleibendeSpielzeitInMS());


                
                // Der Oberfläche den Spieler der am Zug ist übergeben und benötigte Timer starten
                if( this.dasSpielfeld.getSpielerAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ ){
                    this.spielerZeitSchwarz.starteCountdown();
                    LoGoApp.meineOberflaeche.setSchwarzAmZug();
                }
                else{
                    this.spielerZeitWeiss.starteCountdown();
                    LoGoApp.meineOberflaeche.setWeissAmZug();
                }

            }
        }
        else{
            // Es existiert noch kein Objekt vom Typ Spielfeld
            LoGoApp.meineOberflaeche.gibFehlermeldungAus( "Fehler: Es existiert noch kein Spielfeld! Bitte erstellen Sie erst ein neues Spiel.");
            throw new UnsupportedOperationException("Es existiert noch kein Spielfeld.");
        }
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
    public void buttonSpringeZumStart() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Spieler klickt auf Zu-Ende-Button. Letzte Situation auf Brett wird
     * hergestellt.
     */
    public void buttonSpringeZumEnde() {
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
