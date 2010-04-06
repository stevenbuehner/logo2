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
public interface SteuerungIntface {
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
     * Spieler klickt auf Aufgeben. Steuerung muss Dialogfeld od. Aehnliches
     * Anzeigen und Spiel dann beenden
     */
    public void buttonAufgeben();
    public void buttonPassen();
    public void buttonPause();
    public void buttonSpielSpeichern();
    public void buttonUndo();
    public void buttonRedo();
    public void buttonToStart();
    public void buttonToEnd();
    public void zeitAbgelaufenSpieler1Hauptzeit();
    public void zeitAbgelaufenSpieler1Periodenzeit();
    public void zeitAbgelaufenSpieler2Hauptzeit();
    public void zeitAbgelaufenSpieler2Periodenzeit();
}
