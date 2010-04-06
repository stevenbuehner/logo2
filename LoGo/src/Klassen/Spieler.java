/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Klassen;

/**
 *
 * @author steven
 * @version 0.1
 */
public class Spieler {

    private String spielerName;
    private long verbliebendeSpielzeitInMS;
    private float komiPunkte;


    public Spieler( String spielerName, long spielzeitInMS, float komiPunkte ){
        this.setSpielerName(spielerName);
        this.setVerbleibendeSpielzeitInMS(spielzeitInMS);
        this.setKomiPunkte(komiPunkte);
    }

    /**
     *
     * @return Den Namen des Spielers
     */
    public String getSpielerName(){
        return this.spielerName;
    }

    /**
     *
     * @param spielerName setzt den Namen des Spielers
     */
    public void setSpielerName( String spielerName ){
        this.spielerName = spielerName;
    }

    /**
     *
     * @return Verbleibende Spielerzeit des Spielers in Millesekunden.
     * Ist diese aufgebraucht, kann er nur noch die verbleibende Periodenzeit nutzen.
     *
     */
    public long getVerbleibendeSpielzeitInMS(){
        return this.verbliebendeSpielzeitInMS;
    }

    /**
     *
     * @param Verbleibende Spielerzeit des Spielers in Millesekunden.
     * Ist diese aufgebraucht, kann er nur noch die verbleibende Periodenzeit nutzen.
     */
    public void setVerbleibendeSpielzeitInMS( long zeitInMS ){
        this.verbliebendeSpielzeitInMS = zeitInMS;
    }

    /**
     *
     * @return Verbleibende Spielerzeit des Spielers in Sekunden.
     * Ist diese aufgebraucht, kann er nur noch die verbleibende Periodenzeit nutzen.
     * Achtung, intern wird die Zeit in Sekunden gespeichert. Dies ist darum nur
     * ein gerundeter Wert.
     *
     */
    public long getVerbleibendeSpielzeitInSek(){
        return this.verbliebendeSpielzeitInMS/60;
    }

    /**
     *
     * @param Verbleibende Spielerzeit des Spielers in Sekunden.
     * Ist diese aufgebraucht, kann er nur noch die verbleibende Periodenzeit nutzen.
     * Achtung, intern wird die Zeit in Sekunden gespeichert. Dies ist darum nur
     * ein gerundeter Wert. Diese Funktion sollte darum nur mit Bedacht verwendet
     * werden.
     */
    public void setVerbleibendeSpielzeitInSek( long zeitInSek ){
        this.verbliebendeSpielzeitInMS = zeitInSek*60;
    }

    /**
     *
     * @return Komi Punkte auf 0,5 gerundet (Die Runden Funktion ist aber noch
     * nicht implementiert!)
     */
    public float getKomiPunkte(){

        return this.komiPunkte;
    }

    /**
     *
     * @param Zum Setzen der komiPunkte. Die Werte werden bei der Ausgabe dann
     * wieder auf 0,5 gerundet.
     */
    public void setKomiPunkte( float komiPunkte ){
        this.komiPunkte = komiPunkte;
    }

}
