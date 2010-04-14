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
    private int aktuellAngezeigteZugnummer;


    // Timer fuer Spieler und PeriodenZeiten.
    Countdown   periodenZeitSchwarz;
    Countdown   periodenZeitWeiss;
    Countdown   spielerZeitSchwarz;
    Countdown   spielerZeitWeiss;

    
    public Steuerung( ){
        this( 19, 60*1000 );     // Standardwerte
    }

    public Steuerung ( int spielFeldGroesse, long periodenZeit ){

        // Initialisiere nicht angegebenes mit Standardwerten
        this.initMitEinstellungen(
                "Steven",
                "Marit",
                10*1000,
                10*1000,
                30*1000,
                0,
                spielFeldGroesse,
                3);
    }

    /**Implementierung des Interfaces
     * @see SteuerungIntface
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

        this.aktuellAngezeigteZugnummer = 0;
        Spielfeld tmpSpielfeld   = new Spielfeld(spielfeldGroesse);

        tmpSpielfeld.setSpielerSchwarz( new Spieler(spielerNameSchwarz, spielZeitSchwarz, 0, 0 ) );
        tmpSpielfeld.setSpielerWeiss( new Spieler(spielerNameWeiss, spielZeitWeiss, 0, komiFuerWeiss) );
        tmpSpielfeld.setPeriodenZeit(periodenZeit);

        /* Was intern Passiert: Im Spielfeld wird ein Initialfeld generiert,
         * welches Festlegt, dass immer die Vorgabesteine auf dem Brett liegen
         * muessen.*/
        tmpSpielfeld.initialisiereFeldMitVorgabenFuerSchwarz(vorgabeSteineFuerSchwarz);
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

    /**Implementierung des Interfaces
     * @see SteuerungIntface
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

    /**Implementierung des Interfaces
     * @see SteuerungIntface
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

    /**Implementierung des Interfaces
     * @see SteuerungIntface
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

    /**Implementierung des Interfaces
     * @see SteuerungIntface
     */
    public void klickAufFeld(int xPos, int yPos) {

        // Zum Debuggen
        System.out.println( "Klick auf Punkt (" + xPos + "|" + yPos + ")" );

        // Variablendeklaration
        Spielfeld brett         = this.dasSpielfeld;
        int returnWert          = Konstante.FEHLER;
        int klickenderSpieler   = brett.getSpielerFarbeAnDerReihe();
        long periodenZeit       = brett.getPeriodenZeit();

        if(brett.getSpielZustand() != Konstante.SPIEL_LAUEFT)
            return;


        // Timer während der Berechnung stoppen und die Zeiten zum Spieler zurückspeichern
        if( brett.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ ){
            this.spielerZeitSchwarz.stoppeCountdown();
            this.periodenZeitSchwarz.stoppeCountdown();
            brett.getSpielerSchwarz().setVerbleibendeSpielzeitInMS(this.spielerZeitSchwarz.getRemainingTime());
        }
        else{
            this.spielerZeitWeiss.stoppeCountdown();
            this.periodenZeitWeiss.stoppeCountdown();
            brett.getSpielerWeiss().setVerbleibendeSpielzeitInMS(this.spielerZeitWeiss.getRemainingTime());
        }

        // Versuche den Stein zu setzen und speichere das Resulatat in returnWert
        returnWert = this.dasSpielfeld.macheZug(xPos, yPos);

        switch (returnWert){
            case 1:
                // Rueckgabe erfolgreich! Spielerwechsel
                this.setAktuelleAngeigteZugnummer(this.dasSpielfeld.getLetzteZugnummer());
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
        if( this.dasSpielfeld.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ ){
            // Wenn der Spieler keine verbleibende Spielzeit mehr hat, verwende den Periodentimer
            if( brett.getSpielerSchwarz().getVerbleibendeSpielzeitInMS() > 0 ){
                this.spielerZeitSchwarz.setRemainingTime( brett.getSpielerSchwarz().getVerbleibendeSpielzeitInMS() );
                this.spielerZeitSchwarz.starteCountdown();
            }
            else{
                /* Wenn der Spieler einen ungültigen Zug geklickt hat,
                 * spiele mit den vorherigen Periodenzeiten weiter
                 * Bei Spielerwechsel, bekommt der Timer wieder die Periodenzeit
                 * auf Maximal gesetzt und der andere Spieler wieder die volle
                 * Zeit angezeigt.
                 * Damit der Spieler mit der aktuell herunterzaehlenden Periodenzeit
                 * auch die Sekunden angezeigt bekommt, bevor der Timer ausloest,
                 * muss auch dieser hier vorher extra uebermittelt werden
                 */
                if( klickenderSpieler != brett.getSpielerFarbeAnDerReihe() ){
                    this.periodenZeitSchwarz.setRemainingTime(periodenZeit);
                    LoGoApp.meineOberflaeche.setAnzeigePeriodenZeitSchwarz(periodenZeit);
                }

                // Starte den Countdown, bzw. setze den Countdown fort
                this.periodenZeitSchwarz.starteCountdown();
                LoGoApp.meineOberflaeche.setSchwarzAmZug();
            }
        }
        else{
            // Wenn der Spieler keine verbleibende Spielzeit mehr hat, verwende den Periodentimer
            if( brett.getSpielerWeiss().getVerbleibendeSpielzeitInMS() > 0 ){
                this.spielerZeitWeiss.setRemainingTime( brett.getSpielerWeiss().getVerbleibendeSpielzeitInMS() );
                this.spielerZeitWeiss.starteCountdown();
            }
            else{
                /* Wenn der Spieler einen ungültigen Zug geklickt hat,
                 * spiele mit den vorherigen Periodenzeiten weiter
                 * Bei Spielerwechsel, bekommt der Timer wieder die Periodenzeit
                 * auf Maximal gesetzt und der andere Spieler wieder die volle
                 * Zeit angezeigt.
                 * Damit der Spieler mit der aktuell herunterzaehlenden Periodenzeit
                 * auch die Sekunden angezeigt bekommt, bevor der Timer ausloest,
                 * muss auch dieser hier vorher extra uebermittelt werden
                 */
                if( klickenderSpieler != brett.getSpielerFarbeAnDerReihe() ){
                    this.periodenZeitWeiss.setRemainingTime(brett.getPeriodenZeit());
                    LoGoApp.meineOberflaeche.setAnzeigePeriodenZeitWeiss(periodenZeit);
                }

                // Starte den Countdown, bzw. setze den Countdown fort
                this.periodenZeitWeiss.starteCountdown();
                LoGoApp.meineOberflaeche.setWeissAmZug();
            }
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungIntface
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

                /* Der Obeflaeche das aktuelle Spielfeld übergeben, damit
                 * Vorgaben etc. eingezeichnet werden können
                 */
                LoGoApp.meineOberflaeche.setBrettOberflaeche(
                        this.dasSpielfeld.getAktuelesSpielFeld(),
                        this.dasSpielfeld.getSpielfeldGroesse());
                
                // Der Oberfläche den Spieler der am Zug ist übergeben und benötigte Timer starten
                if( this.dasSpielfeld.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ ){
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

    /**Implementierung des Interfaces
     * @see SteuerungIntface
     */
    public void buttonAufgeben() {

        // Timer stoppen und Restzeiten in das Spielfeld zurückspeichern
        if ( Konstante.SCHNITTPUNKT_SCHWARZ == this.dasSpielfeld.getSpielerFarbeAnDerReihe() ){
            // Schwarzer Spieler spielte gerade

            // Stoppe Timer von Schwarz
            this.spielerZeitSchwarz.stoppeCountdown();
            this.periodenZeitSchwarz.stoppeCountdown();
            this.dasSpielfeld.getSpielerSchwarz().setVerbleibendeSpielzeitInMS(
                    this.spielerZeitSchwarz.getRemainingTime());
        }
        else{
            // Weisser Spieler spielte gerade

            // Stoppe Timer von Weiss
            this.spielerZeitWeiss.stoppeCountdown();
            this.periodenZeitWeiss.stoppeCountdown();
            this.dasSpielfeld.getSpielerWeiss().setVerbleibendeSpielzeitInMS(
                    this.spielerZeitWeiss.getRemainingTime());
        }
        
        // Spielstatus auf "Spiel aufgegeben" setzen
        this.dasSpielfeld.setSpielZustand( Konstante.SPIEL_AUFGEGEBEN );

    }

    /**Implementierung des Interfaces
     * @see SteuerungIntface
     */
    public void buttonPassen() {
        
        this.klickAufFeld(-1, -1);

        /*
         * Alternativ dazu könnte man auch die Timer einzeln abprüfen und
         * paussieren lassen.
         *
         * Im Anschluss die Funktion this.dasSpielfeld.zugPassen() aufrufen und
         * dann dir richtigen Timer wieder starten. Als letztes sagt man der GUI
         * wieder welcher Spieler am Zug ist.
         *
        */
    }

    /**Implementierung des Interfaces
     * @see SteuerungIntface
     */
    public void buttonPause(){
        int aktuelerSpieler = this.dasSpielfeld.getSpielerFarbeAnDerReihe();

        if ( Konstante.SCHNITTPUNKT_SCHWARZ == aktuelerSpieler ){
            // Schwarzer Spieler spielt gerade

            // Stoppe Timer von Schwarz
            this.spielerZeitSchwarz.stoppeCountdown();
            this.periodenZeitSchwarz.stoppeCountdown();
            this.dasSpielfeld.getSpielerSchwarz().setVerbleibendeSpielzeitInMS(
                    this.spielerZeitSchwarz.getRemainingTime());
        }
        else{
            // Weisser Spieler spielt gerade

            // Stoppe Timer von Weiss
            this.spielerZeitWeiss.stoppeCountdown();
            this.periodenZeitWeiss.stoppeCountdown();
            this.dasSpielfeld.getSpielerWeiss().setVerbleibendeSpielzeitInMS(
                    this.spielerZeitWeiss.getRemainingTime());
        }

        // Spielstatus auf pausiert setzen.
        this.dasSpielfeld.setSpielZustand(Konstante.SPIEL_PAUSIERT);
    }

    /**Implementierung des Interfaces
     * @see SteuerungIntface
     */
    public void buttonSpielForsetzen(){

        Spielfeld brett         = this.dasSpielfeld;
        
        // Setze das Spiel wieder fort und starte die nötigen Timer
        if( this.dasSpielfeld.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ ){
            // Wenn der Spieler keine verbleibende Spielzeit mehr hat, verwende den Periodentimer
            if( brett.getSpielerSchwarz().getVerbleibendeSpielzeitInMS() > 0 ){
                this.spielerZeitSchwarz.starteCountdown();
            }
            else{           
                // Setze den Countdown fort
                this.periodenZeitSchwarz.starteCountdown();
            }
            LoGoApp.meineOberflaeche.setSchwarzAmZug();
        }
        else{
            // Wenn der Spieler keine verbleibende Spielzeit mehr hat, verwende den Periodentimer
            if( brett.getSpielerWeiss().getVerbleibendeSpielzeitInMS() > 0 ){
                this.spielerZeitWeiss.starteCountdown();
            }
            else{
                // Starte den Countdown, bzw. setze den Countdown fort
                this.periodenZeitWeiss.starteCountdown();
            }
            LoGoApp.meineOberflaeche.setWeissAmZug();
        }

        // Spielstatus nach dem Aufnehmen des Spieles wieder auf "Spiel läuft" setzen
        this.dasSpielfeld.setSpielZustand(Konstante.SPIEL_LAUEFT);
    }

    /**Implementierung des Interfaces
     * @see SteuerungIntface
     */
    public void buttonSpielSpeichern() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**Implementierung des Interfaces
     * @see SteuerungIntface
     */
    public void buttonUndo() {

        /* In dieser Funktion muss noch der Timer einbebunden werden und
         *vielleicht veraendert werden, welcher spieler an der Reihe ist.*/
       if(this.getAktuellAngezeigteZugnummer() > 0){
           this.setAktuelleAngeigteZugnummer(this.getAktuellAngezeigteZugnummer()-1);
       }
       else{
           this.setAktuelleAngeigteZugnummer(0);
       }
       LoGoApp.meineOberflaeche.setBrettOberflaeche(this.dasSpielfeld.getSpielfeldZumZeitpunkt(this.getAktuellAngezeigteZugnummer()),
                                                    this.dasSpielfeld.getSpielfeldGroesse());
    }

    /**Implementierung des Interfaces
     * @see SteuerungIntface
     */
    public void buttonRedo() {
        /* In dieser Funktion muss noch der Timer einbebunden werden und
         *vielleicht veraendert werden, welcher spieler an der Reihe ist.*/
        if(this.getAktuellAngezeigteZugnummer() < this.dasSpielfeld.getLetzteZugnummer()){
            this.setAktuelleAngeigteZugnummer(this.getAktuellAngezeigteZugnummer() + 1);
        }
        else {
            this.setAktuelleAngeigteZugnummer(this.dasSpielfeld.getLetzteZugnummer());
        }
        LoGoApp.meineOberflaeche.setBrettOberflaeche(this.dasSpielfeld.getSpielfeldZumZeitpunkt(this.getAktuellAngezeigteZugnummer()),
                                                     this.dasSpielfeld.getSpielfeldGroesse());
    }

    /**Implementierung des Interfaces
     * @see SteuerungIntface
     */
    public void buttonSpringeZumStart() {
        this.setAktuelleAngeigteZugnummer(0);
        LoGoApp.meineOberflaeche.setBrettOberflaeche(this.dasSpielfeld.getSpielfeldZumZeitpunkt(this.getAktuellAngezeigteZugnummer()),
                                                     this.dasSpielfeld.getSpielfeldGroesse());
    }

    /**Implementierung des Interfaces
     * @see SteuerungIntface
     */
    public void buttonSpringeZumEnde() {
        this.setAktuelleAngeigteZugnummer(this.dasSpielfeld.getLetzteZugnummer());
        LoGoApp.meineOberflaeche.setBrettOberflaeche(this.dasSpielfeld.getSpielfeldZumZeitpunkt(this.getAktuellAngezeigteZugnummer()),
                                                     this.dasSpielfeld.getSpielfeldGroesse());
    }

    /**Implementierung des Interfaces
     * @see SteuerungIntface
     */
    public void zeitAbgelaufenSchwarzHauptzeit() {
        this.dasSpielfeld.getSpielerSchwarz().setVerbleibendeSpielzeitInMS(0);
        this.periodenZeitSchwarz.setRemainingTime(this.dasSpielfeld.getPeriodenZeit());
        this.periodenZeitSchwarz.starteCountdown();    }

    /**Implementierung des Interfaces
     * @see SteuerungIntface
     */
    public void zeitAbgelaufenSchwarzPeriodenzeit() {
        this.dasSpielfeld.setSpielZustand(Konstante.SPIEL_BEENDET_DURCH_APP);
        LoGoApp.meineOberflaeche.gibFehlermeldungAus("Zeit abgelaufen! Spiel wurde beendet!");
       }

    /**Implementierung des Interfaces
     * @see SteuerungIntface
     */
    public void zeitAbgelaufenWeissHauptzeit() {
        this.dasSpielfeld.getSpielerWeiss().setVerbleibendeSpielzeitInMS(0);
        this.periodenZeitWeiss.setRemainingTime(this.dasSpielfeld.getPeriodenZeit());
        this.periodenZeitWeiss.starteCountdown();
    }

    /**Implementierung des Interfaces
     * @see SteuerungIntface
     */
    public void zeitAbgelaufenWeissPeriodenzeit() {
        this.dasSpielfeld.setSpielZustand(Konstante.SPIEL_BEENDET_DURCH_APP);
        LoGoApp.meineOberflaeche.gibFehlermeldungAus("Zeit abgelaufen! Spiel wurde beendet!");
    }

    /**
     * @param nummer Setzt die den Aktuellen Zug auf nummer
     */
    private void setAktuelleAngeigteZugnummer(int nummer){
        this.aktuellAngezeigteZugnummer = nummer;
    }

    /**
     * @return Gibt die Aktuelle Zugnummer der Anzeige zurueck.
     */
    private int getAktuellAngezeigteZugnummer() {
        return this.aktuellAngezeigteZugnummer;
    }

}
