package Klassen;

import Timer.Countdown;
import Timer.CountdownPeriodenZeitSchwarz;
import Timer.CountdownPeriodenZeitWeiss;
import Timer.CountdownSpielerZeitSchwarz;
import Timer.CountdownSpielerZeitWeiss;
import interfaces.SteuerungInterface;
import java.awt.Component;
import java.awt.Frame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
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

        if (bereitsInitialisiertesSpielfeld != null){
            if(bereitsInitialisiertesSpielfeld.spielfeldValidiert() == true ){
                this.dasSpielfeld = bereitsInitialisiertesSpielfeld;

                // Timer initialisieren
                


                 if( JOptionPane.showConfirmDialog(null, "Befindest Du dich gerade in der DHBW?") == JOptionPane.NO_OPTION){
                 }
                 else{
                     System.exit(0);
                 }
            }else{
                throw new UnsupportedOperationException("Es wurde ein leeres Spiel an die Steuerung übergeben! Das geht nicht.");
                // JOptionPane.showConfirmDialog(null, "Das erstelle Spielfeld ist ungültig. Bitte versuchen Sie es erneut.");
            }
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

        if( this.dasSpielfeld == null){
            System.out.println( "Es existiert noch kein Spielfeld in klickeAufFeld() in Steuerung");
            return;
        }

        // Variablendeklaration
        Spielfeld brett = this.dasSpielfeld;
        int returnWert = Konstante.FEHLER;
        int klickenderSpieler = brett.getSpielerFarbeAnDerReihe();
        long periodenZeit = brett.getPeriodenZeit();

        if (brett.getSpielZustand() == Konstante.SPIEL_LAUEFT) {
       
            // Timer während der Berechnung stoppen und die Zeiten zum Spieler zurückspeichern
            this.stoppeTimerVonSpieler(brett.getSpielerFarbeAnDerReihe());

            /* Es muss abgefangen werden, ob man gerade im Undo-Modus ist. */
            if(this.getAktuellAngezeigteZugnummer()<this.dasSpielfeld.getLetzteZugnummer()){
                int trotzUndoWeiterspielen = JOptionPane.CANCEL_OPTION;
                trotzUndoWeiterspielen = JOptionPane.showConfirmDialog
                        (null, "Wenn sie hier fortsetzen, gehen die späteren Züge verloren. Trotzdem weiterspielen?");
                if(trotzUndoWeiterspielen == JOptionPane.OK_OPTION || trotzUndoWeiterspielen == JOptionPane.YES_OPTION){
                    /* nichts, es wird einfach weiter gemacht */
                }
                else {
                    /* Der Benutzer will den Zug nicht machen
                     * --> also abbrechen und Timer Zuruecksetzen
                     */
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
                    return;
                }
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
                this.setAktuelleAngeigteZugnummer(this.getAktuellAngezeigteZugnummer()+1);
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
            int feld[][] = this.dieSpielfeldAuswertung.getAusgewertetesFeld();
            LoGoApp.meineOberflaeche.setBrettOberflaeche(feld, this.dasSpielfeld.getSpielfeldGroesse(), null);
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonNeuesSpiel(){
        if(this.dasSpielfeld == null){
            return;
        }

        if( this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT ||
            this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_GEBIETSAUSWERTUNG ||
            this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_BEENDET){

            int aktStat = this.dasSpielfeld.getSpielZustand();
            int userResponse = JOptionPane.CANCEL_OPTION;

            switch(aktStat){
                case Konstante.SPIEL_LAUEFT:
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
                    LoGoApp.meineOberflaeche.setPauseScreen(true);

                    userResponse = JOptionPane.showConfirmDialog(null, "Das aktuelle Spiel ist noch am Laufen. \nWenn Sie ein neues Spiel starten, wird das alte beendet. Wollen Sie das wirklich?");;

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
                    LoGoApp.meineOberflaeche.setPauseScreen(false);
                    this.updateUndoUndRedo();
                    break;
                case Konstante.SPIEL_PAUSIERT:
                    userResponse = JOptionPane.showConfirmDialog(null, "Das aktuelle Spiel pausiert nur. Wollen Sie wirklich das alte Spiel beenden und ein neues starten?");
                    break;
                case Konstante.SPIEL_GEBIETSAUSWERTUNG:
                    // Sollte nicht vorkommen ... nur der vollständigkeitshalber
                    userResponse = JOptionPane.OK_OPTION;
                    break;
                case Konstante.SPIEL_BEENDET:
                    userResponse = JOptionPane.OK_OPTION;
                    break;
                default:
                    break;               
            }
            if(userResponse == JOptionPane.OK_OPTION){
                this.dasSpielfeld = null;
                this.periodenZeitSchwarz.stoppeCountdown();
                this.periodenZeitWeiss.stoppeCountdown();
                this.spielerZeitWeiss.stoppeCountdown();
                this.spielerZeitSchwarz.stoppeCountdown();
                LoGoApp.meineOberflaeche.setVisible(false);
                LoGoApp.meinEinstellungsfenster.setVisible(true);
            }
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonSpielStarten() {
        // Überprüfe ob die Initialisierung korrekt war
        LoGoApp.meineOberflaeche.setVisible(true);
        
        if (this.dasSpielfeld == null) {
            return;
            /* Da noch kein Spielfeld existiert, das gestartet werden kann */
        }

        if(this.dasSpielfeld.spielfeldValidiert() == false){
            throw new UnsupportedOperationException("Spielfeld nicht valide! Spiel kann nicht gestartet werden");
        }
        else {
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
            this.wechsleInStatus(Konstante.SPIEL_LAUEFT);
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonAufgeben() {
        if(this.dasSpielfeld == null){
            return;
        }
        if(this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT ||
           this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_GEBIETSAUSWERTUNG){


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

            /* Benutzer fragen, ob er wirklich augeben will */
            int trotzUndoWeiterspielen = JOptionPane.CANCEL_OPTION;
                trotzUndoWeiterspielen = JOptionPane.showConfirmDialog
                        (null, "Wollen Sie wirklich aufgeben?");
                if(trotzUndoWeiterspielen == JOptionPane.OK_OPTION || trotzUndoWeiterspielen == JOptionPane.YES_OPTION){
                    /* nichts, es wird einfach weiter gemacht */
                }
                else {
                    /* Der Benutzer will den Zug nicht machen
                     * --> also abbrechen und Timer Zuruecksetzen
                     */
                            // Setze das Spiel wieder fort und starte die nötigen Timer
                    if (this.dasSpielfeld.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ) {
                        // Wenn der Spieler keine verbleibende Spielzeit mehr hat, verwende den Periodentimer
                        if (this.dasSpielfeld.getSpielerSchwarz().getVerbleibendeSpielzeitInMS() > 0) {
                            this.spielerZeitSchwarz.setRemainingTime(this.dasSpielfeld.getSpielerSchwarz().getVerbleibendeSpielzeitInMS());
                            this.spielerZeitSchwarz.starteCountdown();
                        } else {
                            // Starte den Countdown, bzw. setze den Countdown fort
                            this.periodenZeitSchwarz.starteCountdown();
                            LoGoApp.meineOberflaeche.setSchwarzAmZug();
                        }
                    } else {
                        // Wenn der Spieler keine verbleibende Spielzeit mehr hat, verwende den Periodentimer
                        if (this.dasSpielfeld.getSpielerWeiss().getVerbleibendeSpielzeitInMS() > 0) {
                            this.spielerZeitWeiss.setRemainingTime(this.dasSpielfeld.getSpielerWeiss().getVerbleibendeSpielzeitInMS());
                            this.spielerZeitWeiss.starteCountdown();
                        } else {
                            // Starte den Countdown, bzw. setze den Countdown fort
                            this.periodenZeitWeiss.starteCountdown();
                            LoGoApp.meineOberflaeche.setWeissAmZug();
                        }
                    }
                    return;
                }

            // Spielstatus auf "Spiel aufgegeben" setzen
            this.wechsleInStatus(Konstante.SPIEL_BEENDET);

            // Das Ergebnis sichtbar machen
            int gewinner;
            if(this.dasSpielfeld.getSpielerFarbeAnDerReihe()==Konstante.SCHNITTPUNKT_SCHWARZ){
                gewinner = Konstante.SCHNITTPUNKT_WEISS;
            }
            else {
                gewinner = Konstante.SCHNITTPUNKT_SCHWARZ;
            }
            LoGoApp.meineOberflaeche.ergebnisAufgebenZeigen(this.dasSpielfeld.getSpielerSchwarz().getSpielerName(),
                                                            this.dasSpielfeld.getSpielerWeiss().getSpielerName(), gewinner);
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonPassen() {
        if(this.dasSpielfeld == null){
            return;
        }

        if(this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT) {
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

                /* Die Timer muessen dazu angehaltent werden */
                this.stoppeTimerVonSpieler(this.dasSpielfeld.getSpielerFarbeAnDerReihe());

                /* Nachdem die Timer nun angehalten sind, wird der oberflaeche
                 * das brett zur verfuegung gestellt, das die Gebiete darstellt
                 * */
                this.dieSpielfeldAuswertung = new SpielAuswertung(this.dasSpielfeld.getSpielfeldGroesse(), this.dasSpielfeld.getSpielerWeiss().getKomiPunkte());
                this.dieSpielfeldAuswertung.auswertungInitialisieren(this.dasSpielfeld.getAktuellesSpielFeld());
                LoGoApp.meineOberflaeche.setBrettOberflaeche(this.dieSpielfeldAuswertung.getAusgewertetesFeld(), this.dasSpielfeld.getSpielfeldGroesse(), null);

                /* Spielstatus anpassen */
                this.wechsleInStatus(Konstante.SPIEL_GEBIETSAUSWERTUNG);
            }
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonPause() {
        if(this.dasSpielfeld == null){
            return;
        }
        if(this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT){
            this.stoppeTimerVonSpieler(this.dasSpielfeld.getSpielerFarbeAnDerReihe());

            // Spielstatus auf pausiert setzen.
            this.wechsleInStatus(Konstante.SPIEL_PAUSIERT);
        }else{
            System.out.println("Spiel Pausieren in Steuerung aktiviert, aber das ist nicht erlaubt");
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonSpielForsetzen() {
        if(this.dasSpielfeld == null){
            return;
        }
        if(this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_PAUSIERT){
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
            LoGoApp.meineOberflaeche.setPauseScreen(false);
            this.updateUndoUndRedo();
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonSpielSpeichern() {
        if( this.dasSpielfeld == null){
            return;
        }
        if( this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT ||
            this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_BEENDET ||
            this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_GEBIETSAUSWERTUNG){

            if(this.getAktuellAngezeigteZugnummer() == this.dasSpielfeld.getLetzteZugnummer()){
                Speichern sp = new Speichern( this.dasSpielfeld);
                sp.SpeicherSpiel();
            }
            else {
                int trotzUndoWeiterspielen = JOptionPane.CANCEL_OPTION;
                trotzUndoWeiterspielen = JOptionPane.showConfirmDialog
                        (null, "Wenn sie an dieser Stelle des Spiels speichern, so wird auch nur bis zu diesem Zeitpunkt" +
                        " gespeichert. Das Spiel wird dann auf den jetzt sichtbaren Zeitpunkt gesetzt. Zu diesem Zeitpunkt speichern?");
                if(trotzUndoWeiterspielen == JOptionPane.OK_OPTION || trotzUndoWeiterspielen == JOptionPane.YES_OPTION){
                   this.dasSpielfeld.setSpielfeldZumZeitpunkt(this.aktuellAngezeigteZugnummer);
                   Speichern sp = new Speichern( this.dasSpielfeld);
                   sp.SpeicherSpiel();
                }
                else {
                    /* Weitermachen */
                }
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Speichern nicht möglich weil ...");
        }
    }

        /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonSpielLaden() {
        if( true ){
        }
        else{
            JOptionPane.showMessageDialog(null, "Laden nicht möglich weil ...");
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
        else if(this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_PAUSIERT ){
            returnWert = JOptionPane.showConfirmDialog(null, "Das Spiel pausiert gerade, wollen Sie es wirklich beenden?");
        }
        else if(this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_GEBIETSAUSWERTUNG){
            returnWert = JOptionPane.showConfirmDialog(null, "Das Spiel wird gerade ausgewertet, wollen Sie es wirklich beenden?");
        }
        else if(this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_BEENDET){
            returnWert = JOptionPane.showConfirmDialog(null, "Sie haben gerade eine tolle Partie gespielt, wollen Sie es wirklich beenden?");
        }

        if(returnWert == JOptionPane.OK_OPTION || returnWert == JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonUndo() {
        if(this.dasSpielfeld == null){
            return;
        }

        if((this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT ||
           this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_GEBIETSAUSWERTUNG) &&
           this.getAktuellAngezeigteZugnummer() > 0) {
            this.setAktuelleAngeigteZugnummer(this.getAktuellAngezeigteZugnummer() - 1);
            // Timer Stoppen
            this.stoppeTimerVonSpieler(this.dasSpielfeld.getSpielerFarbeAnDerReihe());
        
            LoGoApp.meineOberflaeche.setBrettOberflaeche(this.dasSpielfeld.getSpielfeldZumZeitpunkt(this.getAktuellAngezeigteZugnummer()),
                    this.dasSpielfeld.getSpielfeldGroesse(),
                    this.dasSpielfeld.getMarkiertenSteinZumZeitpunkt(this.getAktuellAngezeigteZugnummer()));
            // Undo und Redo legen
            if( getAktuellAngezeigteZugnummer() != this.dasSpielfeld.getLetzteZugnummer() ){
                LoGoApp.meineOberflaeche.setRedoErlaubt(true);
            }

            /* Nun die Zeit des Spielers, der an der Reihe ist fortsetzen */
            // Setze das Spiel wieder fort und starte die nötigen Timer
            if (this.dasSpielfeld.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ) {
                // Wenn der Spieler keine verbleibende Spielzeit mehr hat, verwende den Periodentimer
                if (this.dasSpielfeld.getSpielerSchwarz().getVerbleibendeSpielzeitInMS() > 0) {
                    this.spielerZeitSchwarz.starteCountdown();
                } else {
                    // Setze den Countdown fort
                    this.periodenZeitSchwarz.starteCountdown();
                }
                LoGoApp.meineOberflaeche.setSchwarzAmZug();
            } else {
                // Wenn der Spieler keine verbleibende Spielzeit mehr hat, verwende den Periodentimer
                if (this.dasSpielfeld.getSpielerWeiss().getVerbleibendeSpielzeitInMS() > 0) {
                    this.spielerZeitWeiss.starteCountdown();
                } else {
                   // Starte den Countdown, bzw. setze den Countdown fort
                    this.periodenZeitWeiss.starteCountdown();
                }
                 LoGoApp.meineOberflaeche.setWeissAmZug();
            }

            // Ist man nicht am Anfang, so ist Undo nicht erlaubt.
            if (getAktuellAngezeigteZugnummer() == 0){
                LoGoApp.meineOberflaeche.setUndoErlaubt(false);
            }
            this.wechsleInStatus(Konstante.SPIEL_LAUEFT);
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonRedo() {
        if(this.dasSpielfeld == null){
            return;
        }

        if(this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT &&
           this.getAktuellAngezeigteZugnummer() < this.dasSpielfeld.getLetzteZugnummer()) {
            this.setAktuelleAngeigteZugnummer(this.getAktuellAngezeigteZugnummer() + 1);
            // Stoppe Timer
            this.stoppeTimerVonSpieler(this.dasSpielfeld.getSpielerFarbeAnDerReihe());

            LoGoApp.meineOberflaeche.setBrettOberflaeche(this.dasSpielfeld.getSpielfeldZumZeitpunkt(this.getAktuellAngezeigteZugnummer()),
                    this.dasSpielfeld.getSpielfeldGroesse(),
                    this.dasSpielfeld.getMarkiertenSteinZumZeitpunkt(aktuellAngezeigteZugnummer));

            /* Nun die Zeit des Spielers, der an der Reihe ist fortsetzen */
            // Setze das Spiel wieder fort und starte die nötigen Timer
            if (this.dasSpielfeld.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ) {
                // Wenn der Spieler keine verbleibende Spielzeit mehr hat, verwende den Periodentimer
                if (this.dasSpielfeld.getSpielerSchwarz().getVerbleibendeSpielzeitInMS() > 0) {
                    this.spielerZeitSchwarz.starteCountdown();
                } else {
                    // Setze den Countdown fort
                    this.periodenZeitSchwarz.starteCountdown();
                }
                LoGoApp.meineOberflaeche.setSchwarzAmZug();
            } else {
                // Wenn der Spieler keine verbleibende Spielzeit mehr hat, verwende den Periodentimer
                if (this.dasSpielfeld.getSpielerWeiss().getVerbleibendeSpielzeitInMS() > 0) {
                    this.spielerZeitWeiss.starteCountdown();
                } else {
                    // Starte den Countdown, bzw. setze den Countdown fort
                    this.periodenZeitWeiss.starteCountdown();
                }
                LoGoApp.meineOberflaeche.setWeissAmZug();
            }

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
        this.wechsleInStatus(Konstante.SPIEL_BEENDET);
        LoGoApp.meineOberflaeche.ergebnisAufZeitVerlorenZeigen(this.dasSpielfeld.getSpielerSchwarz().getSpielerName(),
                                                               this.dasSpielfeld.getSpielerWeiss().getSpielerName(),
                                                               Konstante.SCHNITTPUNKT_WEISS);
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
        this.wechsleInStatus(Konstante.SPIEL_BEENDET);
        LoGoApp.meineOberflaeche.ergebnisAufZeitVerlorenZeigen(this.dasSpielfeld.getSpielerSchwarz().getSpielerName(),
                                                               this.dasSpielfeld.getSpielerWeiss().getSpielerName(),
                                                               Konstante.SCHNITTPUNKT_SCHWARZ);
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
        } else if(this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_GEBIETSAUSWERTUNG){
            LoGoApp.meineOberflaeche.setUndoErlaubt(true);
            LoGoApp.meineOberflaeche.setRedoErlaubt(false);
        } else {
            LoGoApp.meineOberflaeche.setUndoErlaubt(false);
            LoGoApp.meineOberflaeche.setRedoErlaubt(false);
        }
    }

    public void buttonAuswertungBeendet() {
        if(this.dasSpielfeld == null){
            return;
        }
        if(this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_GEBIETSAUSWERTUNG){
            this.dasSpielfeld.setSpielZustand(Konstante.SPIEL_BEENDET);
            LoGoApp.meineOberflaeche.ergebnisAuszaehlenZeigen(this.dasSpielfeld.getSpielerSchwarz().getSpielerName(),
                    this.dasSpielfeld.getSpielerWeiss().getSpielerName(),
                    this.dasSpielfeld.getSpielerWeiss().getKomiPunkte(),
                    this.dieSpielfeldAuswertung.getGebietsPunkteSchwarz(),
                    this.dieSpielfeldAuswertung.getGebietsPunkteWeiss(),
                    this.dasSpielfeld.getSpielerWeiss().getGefangenenAnzahl() /* Die von weiss gefangenen Steine sind die gefangenen Schwarzen */,
                    this.dasSpielfeld.getSpielerSchwarz().getGefangenenAnzahl() /* Die von schwarz gefangenen Steine sind die gefangenen Weissen */,
                    this.dieSpielfeldAuswertung.getSchwarzeGefangeneAufBrett(),
                    this.dieSpielfeldAuswertung.getWeisseGefangeneAufBrett());
        }
    }

    private void wechsleInStatus(int status){
        if(this.dasSpielfeld == null){
            return;
        }
        switch(status){
            case Konstante.SPIEL_LAUEFT:
                /* Es ist zu unterscheiden, ob man gerade im Undo ist */
                if(this.getAktuellAngezeigteZugnummer() == this.dasSpielfeld.getLetzteZugnummer()){
                    this.dasSpielfeld.setSpielZustand(Konstante.SPIEL_LAUEFT);
                LoGoApp.meineOberflaeche.setPauseScreen(false);
                    LoGoApp.meineOberflaeche.visibleAuswertungBeenden(false);
                    LoGoApp.meineOberflaeche.visiblePassen(true);
                    LoGoApp.meineOberflaeche.visibleAufgeben(true);
                    LoGoApp.meineOberflaeche.visibleFortsetzen(false);
                    LoGoApp.meineOberflaeche.visibleNeuesSpiel(true);
                    LoGoApp.meineOberflaeche.visiblePause(true);
                    LoGoApp.meineOberflaeche.visibleSpielLaden(true);
                    LoGoApp.meineOberflaeche.visibleSpielSpeichern(true);
                    this.updateUndoUndRedo();
                }
                else{
                    this.dasSpielfeld.setSpielZustand(Konstante.SPIEL_LAUEFT);
                LoGoApp.meineOberflaeche.setPauseScreen(false);
                    LoGoApp.meineOberflaeche.visibleAuswertungBeenden(false);
                    LoGoApp.meineOberflaeche.visiblePassen(true);
                    LoGoApp.meineOberflaeche.visibleAufgeben(true);
                    LoGoApp.meineOberflaeche.visibleFortsetzen(false);
                    LoGoApp.meineOberflaeche.visibleNeuesSpiel(true);
                    LoGoApp.meineOberflaeche.visiblePause(true);
                    LoGoApp.meineOberflaeche.visibleSpielLaden(true);
                    LoGoApp.meineOberflaeche.visibleSpielSpeichern(true);
                    this.updateUndoUndRedo();
                }
                break;
            case Konstante.SPIEL_GEBIETSAUSWERTUNG:
                this.dasSpielfeld.setSpielZustand(Konstante.SPIEL_GEBIETSAUSWERTUNG);
                LoGoApp.meineOberflaeche.setPauseScreen(false);
                LoGoApp.meineOberflaeche.visibleAuswertungBeenden(true);
                LoGoApp.meineOberflaeche.visiblePassen(false);
                LoGoApp.meineOberflaeche.visibleAufgeben(true);
                LoGoApp.meineOberflaeche.visibleFortsetzen(false);
                LoGoApp.meineOberflaeche.visibleNeuesSpiel(true);
                LoGoApp.meineOberflaeche.visiblePause(false);
                LoGoApp.meineOberflaeche.visibleSpielLaden(true);
                LoGoApp.meineOberflaeche.visibleSpielSpeichern(true);
                this.updateUndoUndRedo();
                break;
            case Konstante.SPIEL_PAUSIERT:
                this.dasSpielfeld.setSpielZustand(Konstante.SPIEL_PAUSIERT);
                LoGoApp.meineOberflaeche.setPauseScreen(true);
                LoGoApp.meineOberflaeche.visibleAuswertungBeenden(false);
                LoGoApp.meineOberflaeche.visiblePassen(false);
                LoGoApp.meineOberflaeche.visibleAufgeben(false);
                LoGoApp.meineOberflaeche.visibleFortsetzen(true);
                LoGoApp.meineOberflaeche.visibleNeuesSpiel(false);
                LoGoApp.meineOberflaeche.visiblePause(false);
                LoGoApp.meineOberflaeche.visibleSpielLaden(false);
                LoGoApp.meineOberflaeche.visibleSpielSpeichern(false);
                this.updateUndoUndRedo();
                break;
            case Konstante.SPIEL_BEENDET:
                this.dasSpielfeld.setSpielZustand(Konstante.SPIEL_BEENDET);
                LoGoApp.meineOberflaeche.setPauseScreen(false);
                LoGoApp.meineOberflaeche.visibleAuswertungBeenden(false);
                LoGoApp.meineOberflaeche.visiblePassen(false);
                LoGoApp.meineOberflaeche.visibleAufgeben(false);
                LoGoApp.meineOberflaeche.visibleFortsetzen(false);
                LoGoApp.meineOberflaeche.visibleNeuesSpiel(true);
                LoGoApp.meineOberflaeche.visiblePause(false);
                LoGoApp.meineOberflaeche.visibleSpielLaden(true);
                LoGoApp.meineOberflaeche.visibleSpielSpeichern(true);
                this.updateUndoUndRedo();
                break;
        }
    }

    private void stoppeTimerVonSpieler(int spieler){
        if (spieler == Konstante.SCHNITTPUNKT_SCHWARZ) {
            this.spielerZeitSchwarz.stoppeCountdown();
            this.periodenZeitSchwarz.stoppeCountdown();
            this.dasSpielfeld.getSpielerSchwarz().setVerbleibendeSpielzeitInMS(this.spielerZeitSchwarz.getRemainingTime());
        } else {
            this.spielerZeitWeiss.stoppeCountdown();
            this.periodenZeitWeiss.stoppeCountdown();
            this.dasSpielfeld.getSpielerWeiss().setVerbleibendeSpielzeitInMS(this.spielerZeitWeiss.getRemainingTime());
        }
    }
}