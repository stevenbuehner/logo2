/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Klassen;

/**
 * Klasse die die Spieler erstellt
 * @author steven
 * @version 0.1
 */
public class Spieler {

    private String spielerName;
    private long verbliebendeSpielzeitInMS;
    private float komiPunkte;
    private int gefangenenAnzahl;

    /**
     * Der Spieler wird mit dem Konstruktor sofort initialisiert.
     * @param spielerName Name des Spielers
     * @param spielzeitInMS Spielzeit des Spielers in Millisekunden
     * @param gefangenenAnzahl Anzahl der gefangenen Steine des Spielers
     * @param komiPunkte Anzahl der Komi des Spielers
     */
    public Spieler(String spielerName, long spielzeitInMS, int gefangenenAnzahl, float komiPunkte) {
        this.setSpielerName(spielerName);
        this.setVerbleibendeSpielzeitInMS(spielzeitInMS);
        this.setGefangenenAnzahl(gefangenenAnzahl);
        this.setKomiPunkte(komiPunkte);
    }

    /**
     *
     * @return Den Namen des Spielers
     */
    public String getSpielerName() {
        return this.spielerName;
    }

    /**
     *
     * @param spielerName setzt den Namen des Spielers
     */
    public void setSpielerName(String spielerName) {
        this.spielerName = spielerName;
    }

    /**
     *
     * @return Verbleibende Spielerzeit des Spielers in Millesekunden.
     * Ist diese aufgebraucht, kann er nur noch die verbleibende Periodenzeit nutzen.
     *
     */
    public long getVerbleibendeSpielzeitInMS() {
        return this.verbliebendeSpielzeitInMS;
    }

    /**
     *
     * @param zeitInMS Verbleibende Spielerzeit des Spielers in Millesekunden.
     * Ist diese aufgebraucht, kann er nur noch die verbleibende Periodenzeit nutzen.
     */
    public void setVerbleibendeSpielzeitInMS(long zeitInMS) {
        this.verbliebendeSpielzeitInMS = zeitInMS;
    }

    /**
     * Gibt die verbleibende Spielerzeit des Spielers in Sekunden.
     * Ist diese aufgebraucht, kann er nur noch die verbleibende Periodenzeit nutzen.
     * Achtung, intern wird die Zeit in Sekunden gespeichert. Dies ist darum nur
     * ein gerundeter Wert.
     *
     * @return Verbleibende Spielerzeit des Spielers in Sekunden.
     */
    public long getVerbleibendeSpielzeitInSek() {
        return this.verbliebendeSpielzeitInMS / 60;
    }

    /**
     * Funktion zum Setzen der verbleibenden Spielerzeit des Spielers in Sekunden.
     * Ist diese aufgebraucht, kann er nur noch die verbleibende Periodenzeit nutzen.
     * Achtung, intern wird die Zeit in Sekunden gespeichert. Dies ist darum nur
     * ein gerundeter Wert. Diese Funktion sollte darum nur mit Bedacht verwendet
     * werden.
     *
     * @param zeitInSek Verbleibende Spielerzeit des Spielers in Sekunden.
     */
    public void setVerbleibendeSpielzeitInSek(long zeitInSek) {
        this.verbliebendeSpielzeitInMS = zeitInSek * 60;
    }

    /**
     *
     * @return Komi Punkte auf 0,5 gerundet (Die Runden Funktion ist aber noch
     * nicht implementiert!)
     */
    public float getKomiPunkte() {

        return this.komiPunkte;
    }

    /**
     *
     * @param komiPunkte Zum Setzen der komiPunkte. Die Werte werden bei der Ausgabe dann
     * wieder auf 0,5 gerundet.
     */
    public void setKomiPunkte(float komiPunkte) {
        this.komiPunkte = komiPunkte;
    }

    /**
     *
     * @return Gibt die aktuelle Gefangenenanzahl des Spielers zurück
     */
    public int getGefangenenAnzahl() {
        return this.gefangenenAnzahl;
    }

    /**
     *
     * @param neueGefangenenAnzahl Überschreibt die aktuelle Gefangenenanzahl mit neueGefangenenAnzahl
     */
    public void setGefangenenAnzahl(int neueGefangenenAnzahl) {
        this.gefangenenAnzahl = neueGefangenenAnzahl;
    }

    /**
     *
     * @param zusaetzlicheGefangene Fügt der GefangenenAnzahl des Spielers zusaetzlicheGefangene hinzu.
     */
    public void addGefangenenAnzahl(int zusaetzlicheGefangene) {
        this.gefangenenAnzahl += zusaetzlicheGefangene;
    }
}
