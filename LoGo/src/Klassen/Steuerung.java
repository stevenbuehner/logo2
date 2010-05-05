package Klassen;

import GUI.FensterSpieloberflaeche;
import GUI.GrafikLib;
import Timer.Countdown;
import Timer.CountdownPeriodenZeitSchwarz;
import Timer.CountdownPeriodenZeitWeiss;
import Timer.CountdownSpielerZeitSchwarz;
import Timer.CountdownSpielerZeitWeiss;
import Interfaces.SteuerungInterface;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    /* Startinfos zum Spiel */
    long startZeitSchwarz = 0;
    long startZeitWeiss = 0;

    public Steuerung() {
        this(9, 60 * 1000);     // Standardwerte
    }

    public Steuerung(int spielFeldGroesse, long periodenZeit) {

        // Initialisiere nicht angegebenes mit Standardwerten
        this.initMitEinstellungen(
                "Steven",
                "Marit",
                45 * 60 * 1000,
                120 * 60 * 1000,
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

        if (bereitsInitialisiertesSpielfeld != null) {
            String validierungsAntwort = bereitsInitialisiertesSpielfeld.spielfeldValidiert();

            if (validierungsAntwort == null) {
                this.dasSpielfeld = bereitsInitialisiertesSpielfeld;
                this.dieSpielfeldAuswertung = null;
                // Timer initialisieren
                String ipAddr = null;

                try {
                    InetAddress addr = InetAddress.getLocalHost();
                    ipAddr = addr.getHostAddress();

                    if (LoGoApp.debug) {
                        System.out.println("IP-Adresse: " + ipAddr);
                    }

                } catch (UnknownHostException ex) {
                    Logger.getLogger(Steuerung.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (ipAddr == null || ipAddr.indexOf("10.") == 0) {
                    if (JOptionPane.showConfirmDialog(null, "Befindest Du dich gerade in der DHBW?") == JOptionPane.NO_OPTION) {
                    } else {
                        System.exit(0);
                    }
                }


            } else {
                if (LoGoApp.debug) {
                    System.out.println("Spielfeld ist nicht VALIDE in der Steuerung angekommen!!\n"+validierungsAntwort);
                }
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
        if (LoGoApp.debug) {
            System.out.println("Klick auf Punkt (" + xPos + "|" + yPos + ")");
        }

        if (this.dasSpielfeld == null && LoGoApp.debug) {
            System.out.println("Es existiert noch kein Spielfeld in klickeAufFeld() in Steuerung");
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
            if (this.getAktuellAngezeigteZugnummer() < this.dasSpielfeld.getLetzteZugnummer()) {
                int trotzUndoWeiterspielen = JOptionPane.CANCEL_OPTION;
                trotzUndoWeiterspielen = JOptionPane.showConfirmDialog(null, "Wenn sie hier fortsetzen, gehen die späteren Züge verloren. Trotzdem weiterspielen?");
                if (trotzUndoWeiterspielen == JOptionPane.OK_OPTION || trotzUndoWeiterspielen == JOptionPane.YES_OPTION) {
                    /* nichts, es wird einfach weiter gemacht */
                } else {
                    /* Der Benutzer will den Zug nicht machen
                     * --> also abbrechen und Timer Zuruecksetzen
                     */
                    if (this.dasSpielfeld.getIgnoreTime() == false) {
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
                    if (this.dasSpielfeld.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ) {
                        LoGoApp.meineOberflaeche.setSchwarzAmZug();
                    } else {
                        LoGoApp.meineOberflaeche.setWeissAmZug();
                    }
                    return;
                }
            }

            /* Man darf nur einen Stein versuchen zu setzen wenn es kein
             * Passen ist*/
            if (xPos != -1 && yPos != -1) {
                // Bevor ein Zug gemacht wird, werden Fehlermeldungen geloescht
                this.leereFehlerMeldungen();

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
                        this.leereFehlerMeldungen();
                        this.gibMeldungAnSpieler(this.dasSpielfeld.getSpielerFarbeAnDerReihe(), "Verboten: Spielpunkt befindet sich nicht auf dem Brett");
                        break;
                    case -1:
                        this.leereFehlerMeldungen();
                        this.gibMeldungAnSpieler(this.dasSpielfeld.getSpielerFarbeAnDerReihe(), "Koregel: Verbotener Zug");
                        break;
                    case -2:
                        this.leereFehlerMeldungen();
                        this.gibMeldungAnSpieler(this.dasSpielfeld.getSpielerFarbeAnDerReihe(), "Schnittpunkt bereits belegt");
                        break;
                    case -3:
                        this.leereFehlerMeldungen();
                        this.gibMeldungAnSpieler(this.dasSpielfeld.getSpielerFarbeAnDerReihe(), "Selbstmord ist nicht erlaubt!");
                        break;
                    default:
                        // Das darf nicht vorkommen
                        throw new UnsupportedOperationException("Dieser Wert darf nie vorkommen");
                }
            } else {
                this.dasSpielfeld.zugPassen();
                this.setAktuelleAngeigteZugnummer(this.getAktuellAngezeigteZugnummer() + 1);
            }

            // Gefangenenzahlen aktualisieren
            LoGoApp.meineOberflaeche.setGefangeneSteineSchwarz(this.dasSpielfeld.getSpielerSchwarz().getGefangenenAnzahl());
            LoGoApp.meineOberflaeche.setGefangeneSteineWeiss(this.dasSpielfeld.getSpielerWeiss().getGefangenenAnzahl());


            // aktualisiere die GefangenenAnzahl
            LoGoApp.meineOberflaeche.setGefangeneSteineSchwarz(brett.getSpielerSchwarz().getGefangenenAnzahl());
            LoGoApp.meineOberflaeche.setGefangeneSteineWeiss(brett.getSpielerWeiss().getGefangenenAnzahl());


            if (this.dasSpielfeld.getIgnoreTime() == false) {
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
                    }
                }
            }
            if (this.dasSpielfeld.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ) {
                LoGoApp.meineOberflaeche.setSchwarzAmZug();
            } else {
                LoGoApp.meineOberflaeche.setWeissAmZug();
            }

        } else if (this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_GEBIETSAUSWERTUNG) {
            this.dieSpielfeldAuswertung.markiereStein(xPos, yPos);
            int feld[][] = this.dieSpielfeldAuswertung.getAusgewertetesFeld();
            LoGoApp.meineOberflaeche.setBrettOberflaeche(feld, this.dasSpielfeld.getSpielfeldGroesse(), null);
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonNeuesSpiel() {
        if (this.dasSpielfeld == null) {
            return;
        }

        if (this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT
                || this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_GEBIETSAUSWERTUNG
                || this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_BEENDET
                || this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_PAUSIERT) {

            int aktStat = this.dasSpielfeld.getSpielZustand();
            int userResponse = JOptionPane.CANCEL_OPTION;

            switch (aktStat) {
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

                    userResponse = JOptionPane.showConfirmDialog(null, "Das aktuelle Spiel ist noch am Laufen. \nWenn Sie ein neues Spiel starten, wird das alte beendet. Wollen Sie das wirklich?");

                    Spielfeld brett = this.dasSpielfeld;

                    if (this.dasSpielfeld.getIgnoreTime() == false) {
                        // Setze das Spiel wieder fort und starte die nötigen Timer
                        if (this.dasSpielfeld.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ) {
                            // Wenn der Spieler keine verbleibende Spielzeit mehr hat, verwende den Periodentimer
                            if (brett.getSpielerSchwarz().getVerbleibendeSpielzeitInMS() > 0) {
                                this.spielerZeitSchwarz.starteCountdown();
                            } else {
                                // Setze den Countdown fort
                                this.periodenZeitSchwarz.starteCountdown();
                            }
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
                    }
                    if (this.dasSpielfeld.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ) {
                        LoGoApp.meineOberflaeche.setSchwarzAmZug();
                    } else {
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
            if (userResponse == JOptionPane.OK_OPTION) {
                this.dasSpielfeld = null;
                this.dieSpielfeldAuswertung = null;
                this.periodenZeitSchwarz.stoppeCountdown();
                this.periodenZeitWeiss.stoppeCountdown();
                this.spielerZeitWeiss.stoppeCountdown();
                this.spielerZeitSchwarz.stoppeCountdown();
                LoGoApp.meineOberflaeche.setVisible(false);
                LoGoApp.meinEinstellungsfenster.macheFensterSichtbar(true);
            }
        }
    }

    /**
     * @see SteuerungInterface
     */
    public void buttonNeuesSchnellstartSpiel() {
        Spielfeld standardSpiel = new Spielfeld(13);
        
        standardSpiel.setSpielerSchwarz(new Spieler("Schwarz", 30 * 60 * 1000, 0, 0));
        standardSpiel.setSpielerWeiss(new Spieler("Weiß", 30 * 60 * 1000, 0, 0));
        standardSpiel.setPeriodenZeit(60*1000);

        this.initMitSpielfeld(standardSpiel);
        this.buttonSpielStarten();
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonSpielStarten() {
        // Überprüfe ob die Initialisierung korrekt war
        LoGoApp.meineOberflaeche.setVisible(true);
        LoGoApp.meinEinstellungsfenster.macheFensterSichtbar(false);
        LoGoApp.meinAuswertungsfenster.setVisible(false);

        

        if (this.dasSpielfeld == null) {
            return;
            /* Da noch kein Spielfeld existiert, das gestartet werden kann */
        }

        /* Periodenzeit setzen */
        if(this.dasSpielfeld.getSpielerSchwarz().getVerbleibendeSpielzeitInMS() > 0){
            LoGoApp.meineOberflaeche.schwarzInPeriodenZeit(false);
        } else {
            LoGoApp.meineOberflaeche.schwarzInPeriodenZeit(true);
        }

        if(this.dasSpielfeld.getSpielerWeiss().getVerbleibendeSpielzeitInMS() > 0){
            LoGoApp.meineOberflaeche.weissInPeriodenZeit(false);
        } else {
            LoGoApp.meineOberflaeche.weissInPeriodenZeit(true);
        }

        /* Startwerte der Zeiten abspeichern */
        this.startZeitSchwarz = this.dasSpielfeld.getSpielerSchwarz().getVerbleibendeSpielzeitInMS();
        this.startZeitWeiss   = this.dasSpielfeld.getSpielerWeiss().getVerbleibendeSpielzeitInMS();
        
        String validierungsAntwort = this.dasSpielfeld.spielfeldValidiert();
        if (validierungsAntwort != null) {
            throw new UnsupportedOperationException("Spielfeld nicht valide! Spiel kann nicht gestartet werden");
        } else {
            // Timer initialisieren
            // Vorraussetzung zum Initialisieren ist ein Objekt vom Typ Spieler in this.dasSpielfeld
            if (this.dasSpielfeld.getSpielerSchwarz() != null
                    || this.dasSpielfeld.getSpielerWeiss() != null) {
                if (this.dasSpielfeld.getIgnoreTime() == false) {
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
                } else {
                    this.periodenZeitSchwarz = new CountdownPeriodenZeitSchwarz(
                            false,
                            1);
                    this.periodenZeitWeiss = new CountdownPeriodenZeitWeiss(
                            false,
                            1);
                    this.spielerZeitSchwarz = new CountdownSpielerZeitSchwarz(
                            false,
                            1);
                    this.spielerZeitWeiss = new CountdownSpielerZeitWeiss(
                            false,
                            1);
                }
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
            if (this.dasSpielfeld.getIgnoreTime() == false) {
                this.periodenZeitSchwarz.setRemainingTime(this.dasSpielfeld.getPeriodenZeit());
                this.periodenZeitWeiss.setRemainingTime(this.dasSpielfeld.getPeriodenZeit());
                this.spielerZeitSchwarz.setRemainingTime(this.dasSpielfeld.getSpielerSchwarz().getVerbleibendeSpielzeitInMS());
                this.spielerZeitWeiss.setRemainingTime(this.dasSpielfeld.getSpielerWeiss().getVerbleibendeSpielzeitInMS());
            } else {
                this.periodenZeitSchwarz.setRemainingTime(1);
                this.periodenZeitWeiss.setRemainingTime(1);
                this.spielerZeitSchwarz.setRemainingTime(1);
                this.spielerZeitWeiss.setRemainingTime(1);
            }

            /* Die Variable fuer die zugnummer muss eingestellt werden */
            this.setAktuelleAngeigteZugnummer(this.dasSpielfeld.getLetzteZugnummer());
            
            /* Der Obeflaeche das aktuelle Spielfeld übergeben, damit
             * Vorgaben etc. eingezeichnet werden können
             */
            LoGoApp.meineOberflaeche.setBrettOberflaeche(
                    this.dasSpielfeld.getSpielfeldZumZeitpunkt(aktuellAngezeigteZugnummer),
                    this.dasSpielfeld.getSpielfeldGroesse(),
                    this.dasSpielfeld.getMarkiertenSteinZumZeitpunkt(aktuellAngezeigteZugnummer));

            // Der Oberfläche den Spieler der am Zug ist übergeben und benötigte Timer starten
            if (this.dasSpielfeld.getIgnoreTime() == false) {
                if (this.dasSpielfeld.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ) {
                    this.spielerZeitSchwarz.starteCountdown();
                } else {
                    this.spielerZeitWeiss.starteCountdown();
                }
            }
            if (this.dasSpielfeld.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ) {
                LoGoApp.meineOberflaeche.setSchwarzAmZug();
            } else {
                LoGoApp.meineOberflaeche.setWeissAmZug();
            }
            LoGoApp.meineOberflaeche.setSpielernameSchwarz(this.dasSpielfeld.getSpielerSchwarz().getSpielerName());
            LoGoApp.meineOberflaeche.setSpielernameWeiss(this.dasSpielfeld.getSpielerWeiss().getSpielerName());
            this.setzeGefangenZahlAufOberklaeche();
            this.leereFehlerMeldungen();
            this.wechsleInStatus(Konstante.SPIEL_LAUEFT);
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonAufgeben() {
        if (this.dasSpielfeld == null) {
            return;
        }
        if (this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT
                || this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_GEBIETSAUSWERTUNG) {


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
            trotzUndoWeiterspielen = JOptionPane.showConfirmDialog(null, "Wollen Sie wirklich aufgeben?");
            if (trotzUndoWeiterspielen == JOptionPane.OK_OPTION || trotzUndoWeiterspielen == JOptionPane.YES_OPTION) {
                /* nichts, es wird einfach weiter gemacht */
            } else {
                /* Der Benutzer will den Zug nicht machen
                 * --> also abbrechen und Timer Zuruecksetzen
                 */

                if (this.dasSpielfeld.getIgnoreTime() == false) {
                    // Setze das Spiel wieder fort und starte die nötigen Timer
                    if (this.dasSpielfeld.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ) {
                        // Wenn der Spieler keine verbleibende Spielzeit mehr hat, verwende den Periodentimer
                        if (this.dasSpielfeld.getSpielerSchwarz().getVerbleibendeSpielzeitInMS() > 0) {
                            this.spielerZeitSchwarz.setRemainingTime(this.dasSpielfeld.getSpielerSchwarz().getVerbleibendeSpielzeitInMS());
                            this.spielerZeitSchwarz.starteCountdown();
                        } else {
                            // Starte den Countdown, bzw. setze den Countdown fort
                            this.periodenZeitSchwarz.starteCountdown();
                        }
                    } else {
                        // Wenn der Spieler keine verbleibende Spielzeit mehr hat, verwende den Periodentimer
                        if (this.dasSpielfeld.getSpielerWeiss().getVerbleibendeSpielzeitInMS() > 0) {
                            this.spielerZeitWeiss.setRemainingTime(this.dasSpielfeld.getSpielerWeiss().getVerbleibendeSpielzeitInMS());
                            this.spielerZeitWeiss.starteCountdown();
                        } else {
                            // Starte den Countdown, bzw. setze den Countdown fort
                            this.periodenZeitWeiss.starteCountdown();
                        }
                    }
                }
                if (this.dasSpielfeld.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ) {
                    LoGoApp.meineOberflaeche.setSchwarzAmZug();
                } else {
                    LoGoApp.meineOberflaeche.setWeissAmZug();
                }
                return;
            }

            // Spielstatus auf "Spiel aufgegeben" setzen
            this.wechsleInStatus(Konstante.SPIEL_BEENDET);

            // Das Ergebnis sichtbar machen
            int gewinner;
            if (this.dasSpielfeld.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ) {
                gewinner = Konstante.SCHNITTPUNKT_WEISS;
            } else {
                gewinner = Konstante.SCHNITTPUNKT_SCHWARZ;
            }
            LoGoApp.meinAuswertungsfenster.ergebnisAufgebenZeigen(this.dasSpielfeld.getSpielerSchwarz().getSpielerName(),
                    this.dasSpielfeld.getSpielerWeiss().getSpielerName(), gewinner);
            LoGoApp.meinAuswertungsfenster.setVisible(true);
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonPassen() {
        if (this.dasSpielfeld == null) {
            return;
        }

        if (this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT) {
            if (this.dasSpielfeld.getAnzahlLetzterPassZuege() >= 2) {
                this.stoppeTimerVonSpieler(this.dasSpielfeld.getSpielerFarbeAnDerReihe());
                this.dieSpielfeldAuswertung = new SpielAuswertung(this.dasSpielfeld.getSpielfeldGroesse(), this.dasSpielfeld.getSpielerWeiss().getKomiPunkte());
                this.dieSpielfeldAuswertung.auswertungInitialisieren(this.dasSpielfeld.getSpielfeldZumZeitpunkt(aktuellAngezeigteZugnummer));
                LoGoApp.meineOberflaeche.setBrettOberflaeche(this.dieSpielfeldAuswertung.getAusgewertetesFeld(), this.dasSpielfeld.getSpielfeldGroesse(), null);

                /* Spielstatus anpassen */
                this.wechsleInStatus(Konstante.SPIEL_GEBIETSAUSWERTUNG);
            }
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
            if (this.dasSpielfeld.getAnzahlLetzterPassZuege() >= 2) {
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
        if (this.dasSpielfeld == null) {
            return;
        }
        if (this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT) {
            this.stoppeTimerVonSpieler(this.dasSpielfeld.getSpielerFarbeAnDerReihe());

            // Spielstatus auf pausiert setzen.
            this.wechsleInStatus(Konstante.SPIEL_PAUSIERT);
        } else {
            if (LoGoApp.debug) {
                System.out.println("Spiel Pausieren in Steuerung aktiviert, aber das ist nicht erlaubt");
            }
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonSpielForsetzen() {
        if (this.dasSpielfeld == null) {
            return;
        }
        if (this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_PAUSIERT) {
            Spielfeld brett = this.dasSpielfeld;

            /* Nun die Zeit des Spielers, der an der Reihe ist fortsetzen */
            // Setze das Spiel wieder fort und starte die nötigen Timer
            if (this.dasSpielfeld.getIgnoreTime() == false) {
                if (this.dasSpielfeld.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ) {
                    // Wenn der Spieler keine verbleibende Spielzeit mehr hat, verwende den Periodentimer
                    if (this.dasSpielfeld.getSpielerSchwarz().getVerbleibendeSpielzeitInMS() > 0) {
                        this.spielerZeitSchwarz.starteCountdown();
                    } else {
                        // Setze den Countdown fort
                        this.periodenZeitSchwarz.starteCountdown();
                    }
                } else {
                    // Wenn der Spieler keine verbleibende Spielzeit mehr hat, verwende den Periodentimer
                    if (this.dasSpielfeld.getSpielerWeiss().getVerbleibendeSpielzeitInMS() > 0) {
                        this.spielerZeitWeiss.starteCountdown();
                    } else {
                        // Starte den Countdown, bzw. setze den Countdown fort
                        this.periodenZeitWeiss.starteCountdown();
                    }
                }
            }

            if (this.dasSpielfeld.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ) {
                LoGoApp.meineOberflaeche.setSchwarzAmZug();
            } else {
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

        // zum Testen
        if (this.dieSpielfeldAuswertung != null && this.dasSpielfeld != null) {
            float pSchw = this.dasSpielfeld.getSpielerWeiss().getKomiPunkte()
                    + this.dieSpielfeldAuswertung.getGebietsPunkteSchwarz()
                    + this.dasSpielfeld.getSpielerSchwarz().getGefangenenAnzahl()
                    + this.dieSpielfeldAuswertung.getSchwarzeGefangeneAufBrett();
            float pWeiss = this.dieSpielfeldAuswertung.getGebietsPunkteWeiss()
                    + this.dasSpielfeld.getSpielerWeiss().getGefangenenAnzahl()
                    + this.dieSpielfeldAuswertung.getWeisseGefangeneAufBrett();

            boolean returnWert = this.historyVersenden(this.dasSpielfeld.getSpielerSchwarz().getSpielerName(),
                    this.dasSpielfeld.getSpielerWeiss().getSpielerName(),
                    pSchw, pWeiss);
            if (LoGoApp.debug) {
                System.out.println("Spielstand zur DB gesendet: " + returnWert);
            }
        } else {
            if (LoGoApp.debug) {
                System.out.println("Das Senden zur DB geht erst NACH der Spielauswertung!");
            }
        }

        if (this.dasSpielfeld == null) {
            return;
        }
        if (this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT
                || this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_BEENDET
                || this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_GEBIETSAUSWERTUNG) {

            if (this.getAktuellAngezeigteZugnummer() == this.dasSpielfeld.getLetzteZugnummer()) {
                Speichern sp = new Speichern(this.dasSpielfeld);
                sp.SpeicherSpiel();
            } else {
                int trotzUndoWeiterspielen = JOptionPane.CANCEL_OPTION;
                trotzUndoWeiterspielen = JOptionPane.showConfirmDialog(null, "Wenn sie an dieser Stelle des Spiels speichern, so wird auch nur bis zu diesem Zeitpunkt"
                        + " gespeichert. Das Spiel wird dann auf den jetzt sichtbaren Zeitpunkt gesetzt. Zu diesem Zeitpunkt speichern?");
                if (trotzUndoWeiterspielen == JOptionPane.OK_OPTION || trotzUndoWeiterspielen == JOptionPane.YES_OPTION) {
                    this.dasSpielfeld.setSpielfeldZumZeitpunkt(this.aktuellAngezeigteZugnummer);
                    Speichern sp = new Speichern(this.dasSpielfeld);
                    sp.SpeicherSpiel();
                } else {
                    /* Weitermachen */
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Speichern nicht möglich weil ...");
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonSpielLaden() {

        int returnWert = JOptionPane.CANCEL_OPTION;

        if (this.dasSpielfeld != null && this.dasSpielfeld.getSpielZustand() != Konstante.SPIEL_BEENDET) {
            // Spiel läuft noch
            if (this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT) {
                returnWert = JOptionPane.showConfirmDialog(null, "Das Spiel läuft noch, wollen Sie es wirklich beenden und ein neues Laden?");
            } else if (this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_PAUSIERT) {
                returnWert = JOptionPane.showConfirmDialog(null, "Das Spiel pausiert gerade, wollen Sie es wirklich beenden um ein neues zu laden?");
            } else if (this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_GEBIETSAUSWERTUNG) {
                returnWert = JOptionPane.showConfirmDialog(null, "Das Spiel wird gerade ausgewertet, wollen Sie es wirklich beenden um ein neues zu laden?");
            }
        }

        //Return Wert ist Null wenn neues Spiel geladen werden soll
        if (returnWert == 0) {
            // Es soll ein neues Spiel geladen werden
            Laden ladenObjekt = new Laden();
            ladenObjekt.LadeSpiel();

            Spielfeld neuesSpielfeld = ladenObjekt.getSpielfeld();
            String fehlermeldung = null;

            if (neuesSpielfeld != null) {
                fehlermeldung = neuesSpielfeld.spielfeldValidiert();
            }

            if (fehlermeldung == null) {
                this.initMitSpielfeld(neuesSpielfeld);
                this.buttonSpielStarten();
                this.buttonPause();
            } else {
                JOptionPane.showConfirmDialog((FensterSpieloberflaeche) LoGoApp.meineOberflaeche, "Das geladene Spielfeld enthält nicht zulässige Werte.\nBitte verwenden Sie ausschließlich zugelassene SGF-Spieldateien.");
            }
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonSpielBeenden() {
        int returnWert = JOptionPane.CANCEL_OPTION;
        if (this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT) {
            returnWert = JOptionPane.showConfirmDialog(null, "Das Spiel läuft noch, wollen Sie es wirklich beenden?");
        } else if (this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_PAUSIERT) {
            returnWert = JOptionPane.showConfirmDialog(null, "Das Spiel pausiert gerade, wollen Sie es wirklich beenden?");
        } else if (this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_GEBIETSAUSWERTUNG) {
            returnWert = JOptionPane.showConfirmDialog(null, "Das Spiel wird gerade ausgewertet, wollen Sie es wirklich beenden?");
        } else if (this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_BEENDET) {
            returnWert = JOptionPane.showConfirmDialog(null, "Sie haben gerade eine tolle Partie gespielt, wollen Sie es wirklich beenden?");
        }

        if (returnWert == JOptionPane.OK_OPTION || returnWert == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonUndo() {
        if (this.dasSpielfeld == null) {
            return;
        }

        if ((this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT
                || this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_GEBIETSAUSWERTUNG)
                && this.getAktuellAngezeigteZugnummer() > 0) {
            this.setAktuelleAngeigteZugnummer(this.getAktuellAngezeigteZugnummer() - 1);
            // Fehlermeldungen loeschen
            this.leereFehlerMeldungen();
            this.setzeGefangenZahlAufOberklaeche();

            // Timer Stoppen
            this.stoppeTimerVonSpieler(this.dasSpielfeld.getSpielerFarbeAnDerReihe());


            LoGoApp.meineOberflaeche.setBrettOberflaeche(this.dasSpielfeld.getSpielfeldZumZeitpunkt(this.getAktuellAngezeigteZugnummer()),
                    this.dasSpielfeld.getSpielfeldGroesse(),
                    this.dasSpielfeld.getMarkiertenSteinZumZeitpunkt(this.getAktuellAngezeigteZugnummer()));
            // Undo und Redo legen
            if (getAktuellAngezeigteZugnummer() != this.dasSpielfeld.getLetzteZugnummer()) {
                LoGoApp.meineOberflaeche.setRedoErlaubt(true);
            }
            /* Nun die Zeit des Spielers, der an der Reihe ist fortsetzen */
            // Setze das Spiel wieder fort und starte die nötigen Timer
            if (this.dasSpielfeld.getIgnoreTime() == false) {
                if (this.dasSpielfeld.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ) {
                    // Wenn der Spieler keine verbleibende Spielzeit mehr hat, verwende den Periodentimer
                    if (this.dasSpielfeld.getSpielerSchwarz().getVerbleibendeSpielzeitInMS() > 0) {
                        this.spielerZeitSchwarz.starteCountdown();
                    } else {
                        // Setze den Countdown fort
                        this.periodenZeitSchwarz.starteCountdown();
                    }
                } else {
                    // Wenn der Spieler keine verbleibende Spielzeit mehr hat, verwende den Periodentimer
                    if (this.dasSpielfeld.getSpielerWeiss().getVerbleibendeSpielzeitInMS() > 0) {
                        this.spielerZeitWeiss.starteCountdown();
                    } else {
                        // Starte den Countdown, bzw. setze den Countdown fort
                        this.periodenZeitWeiss.starteCountdown();
                    }
                }
            }

            if (this.dasSpielfeld.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ) {
                LoGoApp.meineOberflaeche.setSchwarzAmZug();
            } else {
                LoGoApp.meineOberflaeche.setWeissAmZug();
            }

            // Nach einem Undo, ist man immer im Spielfluss
            this.wechsleInStatus(Konstante.SPIEL_LAUEFT);
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonRedo() {
        if (this.dasSpielfeld == null) {
            return;
        }

        if (this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT
                && this.getAktuellAngezeigteZugnummer() < this.dasSpielfeld.getLetzteZugnummer()) {
            this.setAktuelleAngeigteZugnummer(this.getAktuellAngezeigteZugnummer() + 1);
            // Fehlermeldungen loeschen
            this.leereFehlerMeldungen();
            this.setzeGefangenZahlAufOberklaeche();

            // Stoppe Timer
            this.stoppeTimerVonSpieler(this.dasSpielfeld.getSpielerFarbeAnDerReihe());

            LoGoApp.meineOberflaeche.setBrettOberflaeche(this.dasSpielfeld.getSpielfeldZumZeitpunkt(this.getAktuellAngezeigteZugnummer()),
                    this.dasSpielfeld.getSpielfeldGroesse(),
                    this.dasSpielfeld.getMarkiertenSteinZumZeitpunkt(aktuellAngezeigteZugnummer));

            /* Nun die Zeit des Spielers, der an der Reihe ist fortsetzen */
            // Setze das Spiel wieder fort und starte die nötigen Timer
            if (this.dasSpielfeld.getIgnoreTime() == false) {
                if (this.dasSpielfeld.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ) {
                    // Wenn der Spieler keine verbleibende Spielzeit mehr hat, verwende den Periodentimer
                    if (this.dasSpielfeld.getSpielerSchwarz().getVerbleibendeSpielzeitInMS() > 0) {
                        this.spielerZeitSchwarz.starteCountdown();
                    } else {
                        // Setze den Countdown fort
                        this.periodenZeitSchwarz.starteCountdown();
                    }
                } else {
                    // Wenn der Spieler keine verbleibende Spielzeit mehr hat, verwende den Periodentimer
                    if (this.dasSpielfeld.getSpielerWeiss().getVerbleibendeSpielzeitInMS() > 0) {
                        this.spielerZeitWeiss.starteCountdown();
                    } else {
                        // Starte den Countdown, bzw. setze den Countdown fort
                        this.periodenZeitWeiss.starteCountdown();
                    }
                }
            }

            if (this.dasSpielfeld.getSpielerFarbeAnDerReihe() == Konstante.SCHNITTPUNKT_SCHWARZ) {
                LoGoApp.meineOberflaeche.setSchwarzAmZug();
            } else {
                LoGoApp.meineOberflaeche.setWeissAmZug();
            }

            /* Wenn man nach einem Redo 2 Passenzuege hat, ist man in der Auswertung */
            if(this.dasSpielfeld.getAnzahlLetzterPassZuege()>=2){
                this.wechsleInStatus(Konstante.SPIEL_GEBIETSAUSWERTUNG);
                /* Wenn nötig, stoppe Cowntdown */
                this.stoppeTimerVonSpieler(this.dasSpielfeld.getSpielerFarbeAnDerReihe());
            }
            /* sonst ist man im normalen Spielfluss */
            else {
                this.wechsleInStatus(Konstante.SPIEL_LAUEFT);
            }

            // Undo und Redo legen
            this.updateUndoUndRedo();
        }
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void buttonSpringeZumStart() {
        if (dasSpielfeld != null && dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT) {
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
        if (dasSpielfeld != null && dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT) {
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
        LoGoApp.meineOberflaeche.schwarzInPeriodenZeit(true);
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void zeitAbgelaufenSchwarzPeriodenzeit() {
        this.wechsleInStatus(Konstante.SPIEL_BEENDET);
        LoGoApp.meinAuswertungsfenster.ergebnisAufZeitVerlorenZeigen(this.dasSpielfeld.getSpielerSchwarz().getSpielerName(),
                this.dasSpielfeld.getSpielerWeiss().getSpielerName(),
                Konstante.SCHNITTPUNKT_WEISS);
        LoGoApp.meinAuswertungsfenster.setVisible(true);
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void zeitAbgelaufenWeissHauptzeit() {
        this.dasSpielfeld.getSpielerWeiss().setVerbleibendeSpielzeitInMS(0);
        this.periodenZeitWeiss.setRemainingTime(this.dasSpielfeld.getPeriodenZeit());
        this.periodenZeitWeiss.starteCountdown();
        LoGoApp.meineOberflaeche.weissInPeriodenZeit(true);
    }

    /**Implementierung des Interfaces
     * @see SteuerungInterface
     */
    public void zeitAbgelaufenWeissPeriodenzeit() {
        this.wechsleInStatus(Konstante.SPIEL_BEENDET);
        LoGoApp.meinAuswertungsfenster.ergebnisAufZeitVerlorenZeigen(this.dasSpielfeld.getSpielerSchwarz().getSpielerName(),
                this.dasSpielfeld.getSpielerWeiss().getSpielerName(),
                Konstante.SCHNITTPUNKT_SCHWARZ);
        LoGoApp.meinAuswertungsfenster.setVisible(true);
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

    private void updateUndoUndRedo() {
        if (this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_LAUEFT) {
            // Undo und Redo legen
            if (getAktuellAngezeigteZugnummer() < this.dasSpielfeld.getLetzteZugnummer()) {
                LoGoApp.meineOberflaeche.setRedoErlaubt(true);
            } else {
                LoGoApp.meineOberflaeche.setRedoErlaubt(false);
            }
            if (getAktuellAngezeigteZugnummer() > 0) {
                LoGoApp.meineOberflaeche.setUndoErlaubt(true);
            } else {
                LoGoApp.meineOberflaeche.setUndoErlaubt(false);
            }
        } else if (this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_GEBIETSAUSWERTUNG) {
            LoGoApp.meineOberflaeche.setUndoErlaubt(true);
            LoGoApp.meineOberflaeche.setRedoErlaubt(false);
        } else {
            LoGoApp.meineOberflaeche.setUndoErlaubt(false);
            LoGoApp.meineOberflaeche.setRedoErlaubt(false);
        }
    }

    public void buttonAuswertungBeendet() {
        if (this.dasSpielfeld == null) {
            return;
        }
        if (this.dasSpielfeld.getSpielZustand() == Konstante.SPIEL_GEBIETSAUSWERTUNG) {
            this.dasSpielfeld.setSpielZustand(Konstante.SPIEL_BEENDET);
            LoGoApp.meinAuswertungsfenster.ergebnisAuszaehlenZeigen(this.dasSpielfeld.getSpielerSchwarz().getSpielerName(),
                    this.dasSpielfeld.getSpielerWeiss().getSpielerName(),
                    this.dasSpielfeld.getSpielerWeiss().getKomiPunkte(),
                    this.dieSpielfeldAuswertung.getGebietsPunkteSchwarz(),
                    this.dieSpielfeldAuswertung.getGebietsPunkteWeiss(),
                    this.dasSpielfeld.getSpielerWeiss().getGefangenenAnzahl() /* Die von weiss gefangenen Steine sind die gefangenen Schwarzen */,
                    this.dasSpielfeld.getSpielerSchwarz().getGefangenenAnzahl() /* Die von schwarz gefangenen Steine sind die gefangenen Weissen */,
                    this.dieSpielfeldAuswertung.getSchwarzeGefangeneAufBrett(),
                    this.dieSpielfeldAuswertung.getWeisseGefangeneAufBrett());
            LoGoApp.meinAuswertungsfenster.setVisible(true);
        }
    }

    private void wechsleInStatus(int status) {
        if (this.dasSpielfeld == null) {
            return;
        }
        switch (status) {
            case Konstante.SPIEL_LAUEFT:
                /* Es ist zu unterscheiden, ob man gerade im Undo ist */
                if (this.getAktuellAngezeigteZugnummer() == this.dasSpielfeld.getLetzteZugnummer()) {
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
                } else {
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

    private void stoppeTimerVonSpieler(int spieler) {
        if (this.dasSpielfeld == null) {
            return;
        }
        if (this.dasSpielfeld.getIgnoreTime() == false) {
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

    private boolean historyVersenden(String nameSchwarz, String nameWeiss, float punkteSchwarz, float punkteWeiss) {

        HistoryConnector hc = new HistoryConnector();
        HistoryEintrag histEintr = new HistoryEintrag();
        histEintr.setNameSpielerSchwarz(nameSchwarz);
        histEintr.setNameSpielerWeiss(nameWeiss);
        histEintr.setPunkteSpielerSchwarz(punkteSchwarz);
        histEintr.setPunkteSpielerWeiss(punkteWeiss);
        histEintr.setDatum(new java.sql.Date(System.currentTimeMillis()));
        if (LoGoApp.debug) {
            System.out.println(histEintr.getDatum());
        }

        try {
            hc.open();
            hc.sendeNeuenHistoryEintrag(histEintr);
            hc.close();

        } catch (SQLException ex) {
            System.out.println("Datenbankübertragungsfehler: " + ex.getCause());
            return false;
        } catch (Exception ex) {
            System.out.println("Datenbankübertragungsfehler: " + ex.getCause());
            return false;
        } finally {
            return true;
        }
    }

    private void setzeGefangenZahlAufOberklaeche() {
        LoGoApp.meineOberflaeche.setGefangeneSteineSchwarz(this.dasSpielfeld.getSpielerSchwarz().getGefangenenAnzahl());
        LoGoApp.meineOberflaeche.setGefangeneSteineWeiss(this.dasSpielfeld.getSpielerWeiss().getGefangenenAnzahl());
    }

    private void leereFehlerMeldungen() {
        LoGoApp.meineOberflaeche.setSpielerMeldungSchwarz(" ");
        LoGoApp.meineOberflaeche.setSpielerMeldungWeiss(" ");
    }

    private void gibMeldungAnSpieler(int spielerFarbe, String nachicht) {
        if (spielerFarbe == Konstante.SCHNITTPUNKT_SCHWARZ) {
            LoGoApp.meineOberflaeche.setSpielerMeldungSchwarz(nachicht);
        } else if (spielerFarbe == Konstante.SCHNITTPUNKT_WEISS) {
            LoGoApp.meineOberflaeche.setSpielerMeldungWeiss(nachicht);
        }
    }

    public float getKomiWeiss() {
        if(this.dasSpielfeld!=null){
            if(this.dasSpielfeld.getSpielerWeiss()!=null){
                return this.dasSpielfeld.getSpielerWeiss().getKomiPunkte();
            }
        }
        return 0;
    }

    public boolean getIgnoreTime() {
        if(this.dasSpielfeld!=null){
            return this.dasSpielfeld.getIgnoreTime();
        }
        else{
            return true;
        }
    }

    public long getStartHauptzeitSchwarz() {
        return this.startZeitSchwarz;
    }

    public long getStartHauptzeitWeiss() {
        return this.startZeitWeiss;
    }

    public long getPeriodenZeit() {
        if(this.dasSpielfeld!=null){
            return this.dasSpielfeld.getPeriodenZeit();
        }
        return 0;
    }

    public void buttonZeigeHilfeGedrueckt() {

     /*   try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler ../GUI/resources/ChinesischerRestsatz.pdf");
        } catch (IOException ex) {
            Logger.getLogger(Steuerung.class.getName()).log(Level.SEVERE, null, ex);
        }*/

        /* Diese Variante laeuft unter Linux */
       /* URL myURL = getClass().getClassLoader().getResource("GUI/resources/ChinesischerRestsatz.pdf");
        File f = new File(myURL.getFile());
        try {
            Desktop.getDesktop().open(f);
        } catch (IOException ex) {
            Logger.getLogger(Steuerung.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        URL myURL = getClass().getClassLoader().getResource("GUI/resources/ChinesischerRestsatz.pdf");
        try {
            Desktop.getDesktop().open(new File(myURL.getFile()));
        } catch (IOException ex) {
            Logger.getLogger(Steuerung.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void buttonZeigeCreditsGedrueckt() {
        LoGoApp.meineCredits.setVisible(true);
    }
}
