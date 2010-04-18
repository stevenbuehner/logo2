/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import Klassen.Spielfeld;

/**
 * Das ist das Interface fuer die Steuerung. Die folgenden Funktionen muessen daher implementiert sein.
 * @author tommy
 */
public interface SteuerungInterface {

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
    public void initMitEinstellungen(String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeitSchwarz,
            long spielZeitWeiss,
            long periodenZeit,
            float komiFuerWeiss,
            int spielfeldGroesse,
            int vorgabeSteineFuerSchwarz);

    public void initMitEinstellungen(String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeitSchwarz,
            long spielZeitWeiss,
            long periodenZeit,
            float komiFuerWeiss,
            int spielfeldGroesse);

    public void initMitEinstellungen(String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeitSchwarz,
            long spielZeitWeiss,
            long periodenZeit,
            int spielfeldGroesse);

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
    public void initMitEinstellungenFuerStartformation(String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeitSchwarz,
            long spielZeitWeiss,
            long periodenZeit,
            float komiFuerWeiss,
            int spielfeldGroesse);

    public void initMitEinstellungenFuerStartformation(String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeitSchwarz,
            long spielZeitWeiss,
            long periodenZeit,
            int spielfeldGroesse);

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
    public void initMitDatenModell(Spielfeld feld,
            String spielerNameSchwarz,
            String spielerNameWeiss,
            long spielZeitSchwarz,
            long spielZeitWeiss,
            long periodenZeit,
            float komiFuerWeiss);

    /**
     * Wenn bei der GUI auf einen Schnittpunkt geklickt wurde, muss die
     * Steuerung reagieren. Die Koordinaten werden dann uebermittelt
     * @param xPos X-Koordinate (1-Spielfeldgroesse)
     * @param yPos Y-Koordinate (1-Spielfeldgroesse)
     */
    public void klickAufFeld(int xPos, int yPos);

    /**
     * Spieler klickt auf "Spiel Starten" damit wird dann die Validierung
     * des Spielfelds vorgenommen und bei Erfolg das Spiel gestartet.
     */
    public void buttonSpielStarten();

    /**
     * Spieler klickt auf Aufgeben. Steuerung muss Dialogfeld od. Aehnliches
     * Anzeigen und Spiel dann beenden
     */
    public void buttonAufgeben();

    /**
     * Spieler klickt auf Passen. Spielzug muss ausgefuehrt werden und der
     * naechste ist dran. Wird 2 mal hintereinander gepasst, wird das Spiel
     * beendet und gezaehlt.
     */
    public void buttonPassen();

    /**
     * Spieler klickt auf Pause. Das Spiel, und damit die Spielzeit, wird
     * angehalten. Das Brett wird abgedunkelt. Nach dem Klicken auf Pause, wird
     * wäre es gut, wenn in der GUI der Button deaktiviert wird und stattdessen
     * der Button "Spiel Fortsetzen" aktiviert wird.
     */
    public void buttonPause();

    /**
     * Der Button "Spiel fortsetzen" steht zur Verfügung nachdem der Button
     * Pause gedrückt wurde. Mit ihm kann das Spiel wieder aufgenommen werden.
     * Nach dem Klick auf Fortsetzen, wird der Button Pause wieder freigegeben
     * und der Button Fortsetzen wieder deaktiviert
     */
    public void buttonSpielForsetzen();

    /**
     * Spieler klickt auf Speichern. Spiel muss als sgf gespeichert werden.
     */
    public void buttonSpielSpeichern();

    /**
     * Diese Methode wird ausgeloesst, wenn der User das beendigen des
     * Spieles in der GUI signalisiert. Die Steuerung muss ueberpruefen,
     * ob ein Spiel gerade laueft, etwas gespeichert werden muss oder vorher
     * noch eine Abfrage an den User gemacht werden soll.
     */
    public void buttonSpielBeenden();

    /**
     * Spieler klickt auf Undo. Spielzug wird rueckgaengig gemacht
     */
    public void buttonUndo();

    /**
     * Spieler klickt auf Redo. Spielzug der geUndot wurde wird rueckgaengig
     * gemacht (Es wir einfach um 1 nach vorn gegangen)
     */
    public void buttonRedo();

    /**
     * Spieler klickt auf Zu-Start-Button. Anfangssituation wird geladen.
     */
    public void buttonSpringeZumStart();

    /**
     * Spieler klickt auf Zu-Ende-Button. Letzte Situation auf Brett wird
     * hergestellt.
     */
    public void buttonSpringeZumEnde();

    /**
     * Hauptzeit des schwarzen Spielers ist abgelaufen. Periodenzeit muss starten
     */
    public void zeitAbgelaufenSchwarzHauptzeit();

    /**
     * Periodenzeit des schwarzen Spielers ist abgelaufen. Schwarz verliert.
     * Spiel beendet.
     */
    public void zeitAbgelaufenSchwarzPeriodenzeit();

    /**
     * Hauptzeit des weissen Spielers ist abgelaufen. Periodenzeit muss starten
     */
    public void zeitAbgelaufenWeissHauptzeit();

    /**
     * Periodenzeit des weissen Spielers ist abgelaufen. Weiss verliert.
     * Spiel beendet.
     */
    public void zeitAbgelaufenWeissPeriodenzeit();
}
