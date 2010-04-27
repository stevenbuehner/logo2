package Klassen;

import Timer.Countdown;
import Timer.CountdownPeriodenZeitSchwarz;
import Timer.CountdownPeriodenZeitWeiss;
import Timer.CountdownSpielerZeitSchwarz;
import Timer.CountdownSpielerZeitWeiss;
import interfaces.SteuerungInterface;
import javax.swing.JOptionPane;
import logo.LoGoApp;

/**
 * Die Klasse dient zur Steuerung des Spielflusses
 * @author steven
 * @version 0.2
 */
public class Steuerung implements SteuerungInterface {

    // Die Datenklasse
    private Spielfeld dasSpielfeld;
    private int aktuellAngezeigteZugnummer;
    private SpielAuswertung dieSpielfeldAuswertung;
    // Timer fuer Spieler und PeriodenZeiten.
    Countdown periodenZeitSchwarz;
    Countdown periodenZeitWeiss;
    Countdown spielerZeitSchwarz;
    Countdown spielerZeitWeiss;

    public Steuerung() {
        this(9, 60 * 1000);     // Standardwerte
    }

    public Steuerung(int spielFeldGroesse, long periodenZeit) {

        // Initialisiere nicht angegebenes mit Standardwerten
        this.initMitEinstellungen(
                "Steven",
                "Marit",
                45 * 60* 1000,
                120 *60 * 1000,
                200 * 1000,
                0,
                spielFeldGroesse,
                3);
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
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
        Spielfeld tmpSpielfeld = new Spielfeld(spielfeldGroesse);

        tmpSpielfeld.setSpielerSchwarz(new Spieler(spielerNameSchwarz, spielZeitSchwarz, 0, 0));
        tmpSpielfeld.setSpielerWeiss(new Spieler(spielerNameWeiss, spielZeitWeiss, 0, komiFuerWeiss));
        tmpSpielfeld.setPeriodenZeit(periodenZeit);

        /* Was intern Passiert: Im Spielfeld wird ein Initialfeld generiert,
         * welches Festlegt, dass immer die Vorgabesteine auf dem Brett liegen
         * muessen.*/
        tmpSpielfeld.initialisiereFeldMitVorgabenFuerSchwarz(vorgabeSteineFuerSchwarz);
        this.initMitSpielfeld(tmpSpielfeld);

    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void initMitSpielfeld(Spielfeld bereitsInitialisiertesSpielfeld) {
        /*
         * Ueberpruefroutinen fuer das Spielfeld
         * Diese muessen noch programmiert werden ...
         */
        this.dasSpielfeld = bereitsInitialisiertesSpielfeld;
        this.dasSpielfeld.setSpielZustand(Konstante.SPIEL_UNVOLLSTAENDIG);

        // Timer initialisieren
        // Vorraussetzung zum Initialisieren ist ein Objekt vom Typ Spieler in this.dasSpielfeld
        if (this.dasSpielfeld.getSpielerSchwarz() != null ||
                this.dasSpielfeld.getSpielerWeiss() != null) {
        } else {
            throw new UnsupportedOperationException("Timer können nicht initialisiert werden: Fehlendes Spieler-Objekt in Spielfeld");
        }


        if( JOptionPane.showConfirmDialog(null, "Befindest Du dich gerade in der DHBW?") == JOptionPane.NO_OPTION){
        }
        else{
            System.exit(0);
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void initMitDatenModell(
            Spielfeld feld,
            String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeitSchwarz,
            long spielZeitWeiss,
            long periodenZeit,
            float komiFuerWeiss) {
        this.aktuellAngezeigteZugnummer = 0;
        Spielfeld tmpSpielfeld = feld;

        tmpSpielfeld.setSpielerSchwarz(new Spieler(spielerNameSchwarz, spielZeitSchwarz, 0, 0));
        tmpSpielfeld.setSpielerWeiss(new Spieler(spielerNameWeiss, spielZeitWeiss, 0, komiFuerWeiss));
        tmpSpielfeld.setPeriodenZeit(periodenZeit);

        /* Was intern Passiert: Im Spielfeld wird ein Initialfeld generiert,
         * welches Festlegt, dass immer die Vorgabesteine auf dem Brett liegen
         * muessen.*/
        this.initMitSpielfeld(tmpSpielfeld);

        //Zum Testen wurde dies hier auskommentiert, da es sonst nicht funktionieren wuerde
//        throw new UnsupportedOperationException("Not fully supported yet.");
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void klickAufFeld(int xPos, int yPos) {

        // Zum Debuggen
        System.out.println("Klick auf Punkt (" + xPos + "|" + yPos + ")");

        // Variablendeklaration
        Spielfeld brett = this.dasSpielfeld;
        int returnWert = Konstante.FEHLER;
        int klickenderSpieler = brett.getSpielerFarbeAnDerReihe();
        long periodenZeit = brett.getPeriodenZeit();

        if (brett.getSpielZustand() == Konstante.SPIEL_LAUEFT) {
       
            // Timer während der Berechnung stoppen und die Zeiten zum Spieler zurückspeichern
            if (brett.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ) {
                this.spielerZeitSchwarz.stoppeCountdown();
                this.periodenZeitSchwarz.stoppeCountdown();
                brett.getSpielerSchwarz().setVerbleibendeSpielzeitInMS(this.spielerZeitSchwarz.getRemainingTime());
            } else {
                this.spielerZeitWeiss.stoppeCountdown();
                this.periodenZeitWeiss.stoppeCountdown();
                brett.getSpielerWeiss().setVerbleibendeSpielzeitInMS(this.spielerZeitWeiss.getRemainingTime());
            }

            /* Man darf nur einen Stein versuchen zu setzen wenn es kein
             * Passen ist*/
            if(xPos!=-1 && yPos!=-1){
                // Versuche den Stein zu setzen und speichere das Resulatat in returnWert
                returnWert = this.dasSpielfeld.macheZug(xPos, yPos);

                switch (returnWert) {
                    case 1:
                        // Rueckgabe erfolgreich! Spielerwechsel
                        this.setAktuelleAngeigteZugnummer(this.dasSpielfeld.getLetzteZugnummer());
                        LoGoApp.meineOberflaeche.setBrettOberflaeche(
                                this.dasSpielfeld.getAktuellesSpielFeld(),
                                this.dasSpielfeld.getSpielfeldGroesse(),
                                this.dasSpielfeld.getMarkiertenSteinZumZeitpunkt(aktuellAngezeigteZugnummer));
                                this.updateUndoUndRedo();
                        break;
                    case 0:
                        LoGoApp.meineOberflaeche.gibFehlermeldungAus("Verboten: Spielpunkt befindet sich nicht auf dem Brett");
                        break;
                    case -1:
                        LoGoApp.meineOberflaeche.gibFehlermeldungAus("Koregel: Verbotener Zug");
                        break;
                    case -2:
                        LoGoApp.meineOberflaeche.gibFehlermeldungAus("Dieser Schnittpunkt ist bereits belegt");
                        break;
                    case -3:
                        LoGoApp.meineOberflaeche.gibFehlermeldungAus("Selbstmord ist nicht erlaubt!");
                        break;
                    default:
                        // Das darf nicht vorkommen
                        throw new UnsupportedOperationException("Dieser Wert darf nie vorkommen");
                }
            }
            else{
                this.dasSpielfeld.zugPassen();
            }

            // aktualisiere die GefangenenAnzahl
            LoGoApp.meineOberflaeche.setGefangeneSteineSchwarz(brett.getSpielerSchwarz().getGefangenenAnzahl());
            LoGoApp.meineOberflaeche.setGefangeneSteineWeiss(brett.getSpielerWeiss().getGefangenenAnzahl());


            // Setze das Spiel wieder fort und starte die nötigen Timer
            if (this.dasSpielfeld.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ) {
                // Wenn der Spieler keine verbleibende Spielzeit mehr hat, verwende den Periodentimer
                if (brett.getSpielerSchwarz().getVerbleibendeSpielzeitInMS() > 0) {
                    this.spielerZeitSchwarz.setRemainingTime(brett.getSpielerSchwarz().getVerbleibendeSpielzeitInMS());
                    this.spielerZeitSchwarz.starteCountdown();
                } else {
                    /* Wenn der Spieler einen ungültigen Zug geklickt hat,
                     * spiele mit den vorherigen Periodenzeiten weiter
                     * Bei Spielerwechsel, bekommt der Timer wieder die Periodenzeit
                     * auf Maximal gesetzt und der andere Spieler wieder die volle
                     * Zeit angezeigt.
                     * Damit der Spieler mit der aktuell herunterzaehlenden Periodenzeit
                     * auch die Sekunden angezeigt bekommt, bevor der Timer ausloest,
                     * muss auch dieser hier vorher extra uebermittelt werden
                     */
                    if (klickenderSpieler != brett.getSpielerFarbeAnDerReihe()) {
                        this.periodenZeitSchwarz.setRemainingTime(periodenZeit);
                        LoGoApp.meineOberflaeche.setAnzeigePeriodenZeitSchwarz(periodenZeit);
                    }

                    // Starte den Countdown, bzw. setze den Countdown fort
                    this.periodenZeitSchwarz.starteCountdown();
                    LoGoApp.meineOberflaeche.setSchwarzAmZug();
                }
            } else {
                // Wenn der Spieler keine verbleibende Spielzeit mehr hat, verwende den Periodentimer
                if (brett.getSpielerWeiss().getVerbleibendeSpielzeitInMS() > 0) {
                    this.spielerZeitWeiss.setRemainingTime(brett.getSpielerWeiss().getVerbleibendeSpielzeitInMS());
                    this.spielerZeitWeiss.starteCountdown();
                } else {
                    /* Wenn der Spieler einen ungültigen Zug geklickt hat,
                     * spiele mit den vorherigen Periodenzeiten weiter
                     * Bei Spielerwechsel, bekommt der Timer wieder die Periodenzeit
                     * auf Maximal gesetzt und der andere Spieler wieder die volle
                     * Zeit angezeigt.
                     * Damit der Spieler mit der aktuell herunterzaehlenden Periodenzeit
                     * auch die Sekunden angezeigt bekommt, bevor der Timer ausloest,
                     * muss auch dieser hier vorher extra uebermittelt werden
                     */
                    if (klickenderSpieler != brett.getSpielerFarbeAnDerReihe()) {
                        this.periodenZeitWeiss.setRemainingTime(brett.getPeriodenZeit());
                        LoGoApp.meineOberflaeche.setAnzeigePeriodenZeitWeiss(periodenZeit);
                    }

                    // Starte den Countdown, bzw. setze den Countdown fort
                    this.periodenZeitWeiss.starteCountdown();
                    LoGoApp.meineOberflaeche.setWeissAmZug();
                }
            }
        }

        else if(this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_GEBIETSAUSWERTUNG){
            this.dieSpielfeldAuswertung.markiereStein(xPos, yPos);
            LoGoApp.meineOberflaeche.setBrettOberflaeche(this.dieSpielfeldAuswertung.getAusgewertetesFeld(), this.dasSpielfeld.getSpielfeldGroesse(), null);
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonSpielStarten() {
        // Überprüfe ob die Initialisierung korrekt war

        if (this.dasSpielfeld != null) {
            if(this.dasSpielfeld.spielfeldValidiert() == false){
                throw new UnsupportedOperationException("Spielfeld nicht valide! Spiel kann nicht gestartet werden");
            }


            else {
                this.dasSpielfeld.setSpielZustand(Konstante.SPIEL_LAUEFT);


                // Timer initialisieren
                // Vorraussetzung zum Initialisieren ist ein Objekt vom Typ Spieler in this.dasSpielfeld
                if (this.dasSpielfeld.getSpielerSchwarz() != null ||
                        this.dasSpielfeld.getSpielerWeiss() != null) {
                    this.periodenZeitSchwarz = new CountdownPeriodenZeitSchwarz(
                            false,
                            this.dasSpielfeld.getPeriodenZeit());
                    this.periodenZeitWeiss = new CountdownPeriodenZeitWeiss(
                            false,
                            this.dasSpielfeld.getPeriodenZeit());
                    this.spielerZeitSchwarz = new CountdownSpielerZeitSchwarz(
                            false,
                            this.dasSpielfeld.getSpielerSchwarz().getVerbleibendeSpielzeitInMS());
                    this.spielerZeitWeiss = new CountdownSpielerZeitWeiss(
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

                // Schaltflaeche Undo und Redo einstellen
                this.updateUndoUndRedo();

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
                        /*this.dasSpielfeld.getAktuelesSpielFeld(),*/
                        this.dasSpielfeld.getSpielfeldZumZeitpunkt(aktuellAngezeigteZugnummer),
                        this.dasSpielfeld.getSpielfeldGroesse(),
                        this.dasSpielfeld.getMarkiertenSteinZumZeitpunkt(aktuellAngezeigteZugnummer));

                // Der Oberfläche den Spieler der am Zug ist übergeben und benötigte Timer starten
                if (this.dasSpielfeld.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ) {
                    this.spielerZeitSchwarz.starteCountdown();
                    LoGoApp.meineOberflaeche.setSchwarzAmZug();
                } else {
                    this.spielerZeitWeiss.starteCountdown();
                    LoGoApp.meineOberflaeche.setWeissAmZug();
                }

            }
        } else {
            // Es existiert noch kein Objekt vom Typ Spielfeld
            LoGoApp.meineOberflaeche.gibFehlermeldungAus("Fehler: Es existiert noch kein Spielfeld! Bitte erstellen Sie erst ein neues Spiel.");
            throw new UnsupportedOperationException("Es existiert noch kein Spielfeld.");
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonAufgeben() {

        // Timer stoppen und Restzeiten in das Spielfeld zurückspeichern
        if (Konstante.SCHNITTPUNKT_SCHWARZ == this.dasSpielfeld.getSpielerFarbeAnDerReihe()) {
            // Schwarzer Spieler spielte gerade

            // Stoppe Timer von Schwarz
            this.spielerZeitSchwarz.stoppeCountdown();
            this.periodenZeitSchwarz.stoppeCountdown();
            this.dasSpielfeld.getSpielerSchwarz().setVerbleibendeSpielzeitInMS(
                    this.spielerZeitSchwarz.getRemainingTime());
        } else {
            // Weisser Spieler spielte gerade

            // Stoppe Timer von Weiss
            this.spielerZeitWeiss.stoppeCountdown();
            this.periodenZeitWeiss.stoppeCountdown();
            this.dasSpielfeld.getSpielerWeiss().setVerbleibendeSpielzeitInMS(
                    this.spielerZeitWeiss.getRemainingTime());
        }

        // Spielstatus auf "Spiel aufgegeben" setzen
        this.dasSpielfeld.setSpielZustand(Konstante.SPIEL_AUFGEGEBEN);
        this.updateUndoUndRedo();
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
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

        /* Wenn die letzten beiden Zuege passen waren, so befindet sich das
         * Spiel in der Gebietsauswertungs-phase */
        if(this.dasSpielfeld.getAnzahlLetzterPassZuege()>=2){
            /* Da die letzten beiden Zuege passen waren, wird das spiel nun
             * ausgezaehlt*/
            this.dasSpielfeld.setSpielZustand(Konstante.SPIEL_GEBIETSAUSWERTUNG);

            /* Die Timer muessen dazu angehaltent werden */
            int aktuelerSpieler = this.dasSpielfeld.getSpielerFarbeAnDerReihe();

            if (Konstante.SCHNITTPUNKT_SCHWARZ == aktuelerSpieler) {
                // Schwarzer Spieler spielt gerade

                // Stoppe Timer von Schwarz
                this.spielerZeitSchwarz.stoppeCountdown();
                this.periodenZeitSchwarz.stoppeCountdown();
                this.dasSpielfeld.getSpielerSchwarz().setVerbleibendeSpielzeitInMS(
                        this.spielerZeitSchwarz.getRemainingTime());
            } else {
                // Weisser Spieler spielt gerade

                // Stoppe Timer von Weiss
                this.spielerZeitWeiss.stoppeCountdown();
                this.periodenZeitWeiss.stoppeCountdown();
                this.dasSpielfeld.getSpielerWeiss().setVerbleibendeSpielzeitInMS(
                        this.spielerZeitWeiss.getRemainingTime());
            }

            /* Nachdem die Timer nun angehalten sind, wird der oberflaeche
             * das brett zur verfuegung gestellt, das die Gebiete darstellt
             * */
            this.dieSpielfeldAuswertung = new SpielAuswertung(this.dasSpielfeld.getSpielfeldGroesse(), this.dasSpielfeld.getSpielerWeiss().getKomiPunkte());
            this.dieSpielfeldAuswertung.auswertungInitialisieren(this.dasSpielfeld.getAktuellesSpielFeld());
            LoGoApp.meineOberflaeche.setBrettOberflaeche(this.dieSpielfeldAuswertung.getAusgewertetesFeld(), this.dasSpielfeld.getSpielfeldGroesse(), null);
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonPause() {
        if(this.dasSpielfeld != null && this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT){
            int aktuelerSpieler = this.dasSpielfeld.getSpielerFarbeAnDerReihe();

            if (Konstante.SCHNITTPUNKT_SCHWARZ == aktuelerSpieler) {
                // Schwarzer Spieler spielt gerade

                // Stoppe Timer von Schwarz
                this.spielerZeitSchwarz.stoppeCountdown();
                this.periodenZeitSchwarz.stoppeCountdown();
                this.dasSpielfeld.getSpielerSchwarz().setVerbleibendeSpielzeitInMS(
                        this.spielerZeitSchwarz.getRemainingTime());
            } else {
                // Weisser Spieler spielt gerade

                // Stoppe Timer von Weiss
                this.spielerZeitWeiss.stoppeCountdown();
                this.periodenZeitWeiss.stoppeCountdown();
                this.dasSpielfeld.getSpielerWeiss().setVerbleibendeSpielzeitInMS(
                        this.spielerZeitWeiss.getRemainingTime());
            }

            // Spielstatus auf pausiert setzen.
            this.dasSpielfeld.setSpielZustand(Konstante.SPIEL_PAUSIERT);
            this.updateUndoUndRedo();
        }else{
            System.out.println("Spiel Pausieren in Steuerung aktiviert, aber das ist nicht erlaubt");
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonSpielForsetzen() {
        if(this.dasSpielfeld != null && this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_PAUSIERT){
            Spielfeld brett = this.dasSpielfeld;

            // Setze das Spiel wieder fort und starte die nötigen Timer
            if (this.dasSpielfeld.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ) {
                // Wenn der Spieler keine verbleibende Spielzeit mehr hat, verwende den Periodentimer
                if (brett.getSpielerSchwarz().getVerbleibendeSpielzeitInMS() > 0) {
                    this.spielerZeitSchwarz.starteCountdown();
                } else {
                    // Setze den Countdown fort
                    this.periodenZeitSchwarz.starteCountdown();
                }
                LoGoApp.meineOberflaeche.setSchwarzAmZug();
            } else {
                // Wenn der Spieler keine verbleibende Spielzeit mehr hat, verwende den Periodentimer
                if (brett.getSpielerWeiss().getVerbleibendeSpielzeitInMS() > 0) {
                    this.spielerZeitWeiss.starteCountdown();
                } else {
                    // Starte den Countdown, bzw. setze den Countdown fort
                    this.periodenZeitWeiss.starteCountdown();
                }
                LoGoApp.meineOberflaeche.setWeissAmZug();
            }

            // Spielstatus nach dem Aufnehmen des Spieles wieder auf "Spiel läuft" setzen
            this.dasSpielfeld.setSpielZustand(Konstante.SPIEL_LAUEFT);
            this.updateUndoUndRedo();
        }else{
            System.out.println("Spiel Fortsetzen in Steuerung aktiviert, aber das ist nicht erlaubt");
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonSpielSpeichern() {
        if( dasSpielfeld != null && dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT){
            Speichern sp = new Speichern( this.dasSpielfeld);
            sp.SpeicherSpiel();
        }
        else{
            JOptionPane.showMessageDialog(null, "Speichern nicht möglich weil ...");
        }

    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonSpielBeenden() {
        int returnWert = JOptionPane.CANCEL_OPTION;
        if(this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT ){
            returnWert = JOptionPane.showConfirmDialog(null, "Das Spiel läuft noch, wollen Sie es wirklich beenden?");
        }
        if(this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_PAUSIERT ){
            returnWert = JOptionPane.showConfirmDialog(null, "Das Spiel pausiert gerade, wollen Sie es wirklich beenden?");
        }

        if(returnWert == JOptionPane.OK_OPTION || returnWert == JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonUndo() {
        /* In dieser Funktion muss noch der Timer einbebunden werden und */

        if(this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT){
            if (this.getAktuellAngezeigteZugnummer() > 0) {
                this.setAktuelleAngeigteZugnummer(this.getAktuellAngezeigteZugnummer() - 1);
            } else {
                this.setAktuelleAngeigteZugnummer(0);
            }
            LoGoApp.meineOberflaeche.setBrettOberflaeche(this.dasSpielfeld.getSpielfeldZumZeitpunkt(this.getAktuellAngezeigteZugnummer()),
                    this.dasSpielfeld.getSpielfeldGroesse(),
                    this.dasSpielfeld.getMarkiertenSteinZumZeitpunkt(aktuellAngezeigteZugnummer));

            // Undo und Redo legen
            if( getAktuellAngezeigteZugnummer() != this.dasSpielfeld.getLetzteZugnummer() ){
                LoGoApp.meineOberflaeche.setRedoErlaubt(true);
            }
            if (getAktuellAngezeigteZugnummer() > 0){
                LoGoApp.meineOberflaeche.setUndoErlaubt(false);
            }
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonRedo() {
        /* In dieser Funktion muss noch der Timer einbebunden werden und */

        if(this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT){
            if (this.getAktuellAngezeigteZugnummer() < this.dasSpielfeld.getLetzteZugnummer()) {
                this.setAktuelleAngeigteZugnummer(this.getAktuellAngezeigteZugnummer() + 1);
            } else {
                this.setAktuelleAngeigteZugnummer(this.dasSpielfeld.getLetzteZugnummer());
            }
            LoGoApp.meineOberflaeche.setBrettOberflaeche(this.dasSpielfeld.getSpielfeldZumZeitpunkt(this.getAktuellAngezeigteZugnummer()),
                    this.dasSpielfeld.getSpielfeldGroesse(),
                    this.dasSpielfeld.getMarkiertenSteinZumZeitpunkt(aktuellAngezeigteZugnummer));

            // Undo und Redo legen
            this.updateUndoUndRedo();
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonSpringeZumStart() {
        if( dasSpielfeld != null && dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT){
            this.setAktuelleAngeigteZugnummer(0);
            LoGoApp.meineOberflaeche.setBrettOberflaeche(this.dasSpielfeld.getSpielfeldZumZeitpunkt(this.getAktuellAngezeigteZugnummer()),
                    this.dasSpielfeld.getSpielfeldGroesse(),
                    this.dasSpielfeld.getMarkiertenSteinZumZeitpunkt(aktuellAngezeigteZugnummer));

            // Undo und Redo legen
            this.updateUndoUndRedo();
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonSpringeZumEnde() {
        if( dasSpielfeld != null && dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT){
            this.setAktuelleAngeigteZugnummer(this.dasSpielfeld.getLetzteZugnummer());
            LoGoApp.meineOberflaeche.setBrettOberflaeche(this.dasSpielfeld.getSpielfeldZumZeitpunkt(this.getAktuellAngezeigteZugnummer()),
                    this.dasSpielfeld.getSpielfeldGroesse(),
                    this.dasSpielfeld.getMarkiertenSteinZumZeitpunkt(aktuellAngezeigteZugnummer));

            // Undo und Redo legen
            this.updateUndoUndRedo();
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void zeitAbgelaufenSchwarzHauptzeit() {
        this.dasSpielfeld.getSpielerSchwarz().setVerbleibendeSpielzeitInMS(0);
        this.periodenZeitSchwarz.setRemainingTime(this.dasSpielfeld.getPeriodenZeit());
        this.periodenZeitSchwarz.starteCountdown();
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void zeitAbgelaufenSchwarzPeriodenzeit() {
        this.dasSpielfeld.setSpielZustand(Konstante.SPIEL_BEENDET_DURCH_APP);
        LoGoApp.meineOberflaeche.gibFehlermeldungAus("Zeit abgelaufen! Spiel wurde beendet!");
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void zeitAbgelaufenWeissHauptzeit() {
        this.dasSpielfeld.getSpielerWeiss().setVerbleibendeSpielzeitInMS(0);
        this.periodenZeitWeiss.setRemainingTime(this.dasSpielfeld.getPeriodenZeit());
        this.periodenZeitWeiss.starteCountdown();
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void zeitAbgelaufenWeissPeriodenzeit() {
        this.dasSpielfeld.setSpielZustand(Konstante.SPIEL_BEENDET_DURCH_APP);
        LoGoApp.meineOberflaeche.gibFehlermeldungAus("Zeit abgelaufen! Spiel wurde beendet!");
    }

    /**
     * @param nummer Setzt die den Aktuellen Zug auf nummer
     */
    private void setAktuelleAngeigteZugnummer(int nummer) {
        this.aktuellAngezeigteZugnummer = nummer;
    }

    /**
     * @return Gibt die Aktuelle Zugnummer der Anzeige zurueck.
     */
    private int getAktuellAngezeigteZugnummer() {
        return this.aktuellAngezeigteZugnummer;
    }

    private void updateUndoUndRedo(){
        if( this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT){
            // Undo und Redo legen
            if( getAktuellAngezeigteZugnummer() < this.dasSpielfeld.getLetzteZugnummer() ){
                LoGoApp.meineOberflaeche.setRedoErlaubt(true);
            }else{
                LoGoApp.meineOberflaeche.setRedoErlaubt(false);
            }
            if (getAktuellAngezeigteZugnummer() > 0){
            LoGoApp.meineOberflaeche.setUndoErlaubt(true);
            }else{
                LoGoApp.meineOberflaeche.setUndoErlaubt(false);
            }
        }else{
            LoGoApp.meineOberflaeche.setUndoErlaubt(false);
            LoGoApp.meineOberflaeche.setRedoErlaubt(false);
        }
    }
    
}