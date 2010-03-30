/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Klassen;

/**
 *
 * @author steven
 * @version 0.1
 * @param Die Klasse dient zum Verwalten der Daten eines Spielers
 */
public class Steuerung {

    private String spielerName;
    private long verbleibendeSpielzeitInMS;


    /**
     * @return Name des Spielers in Textform
     */
    public String getSpielerName(){
        return this.spielerName;
    }

    /**
     * @param Den Spielername zum Speichern in der Klasse übergeben
     */
    public void setSpielername( String spielerName ){
        this.spielerName = spielerName;
    }


    /**
     *
     * @return Die verbleibende Spielzeit des Spielers in Millesekunden
     */
    public long getVerbleibendeSpielzeitInMS(){
        return this.verbleibendeSpielzeitInMS;
    }

    /**
     *
     * @param Speichern der restlichen verbleibenden Spielzeit
     * des Spielers in Millesekunden
     */
    public void setVerbleibendeSpielzeitInMS( long verbleibendeSpielzeit ){
        this.verbleibendeSpielzeitInMS = verbleibendeSpielzeit;
    }

    /**
     *
     * @return Die verbleibende Spielzeit des Spielers in Sekunden gerundet.
     */
    public long getVerbleibendeSpielzeitInSek(){
        return verbleibendeSpielzeitInMS/60;
    }

    /**
     *
     * @param Speichern der restlichen verbleibenden Spielzeit in Sekunden.
     * Da der Wert intern in Millesekunden gespeichert wird, sollte diese
     * Funktion nur sehr bedacht verwendet werden, da ansonsten dem Spieler
     * wertvolle Millesekunden im Spiel fehlen könnten.
     */
    public void setVerbleibendeSpielzeitInSek( long verbleibendeSpielzeitInSek ){
        this.verbleibendeSpielzeitInMS = verbleibendeSpielzeitInSek*60;
    }




}
