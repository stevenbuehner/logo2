/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Klassen;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author steven
 * @version 0.1
 */
public class Spielfeld {

    // interne Speicherobjekte
    private int letzteZugnummer;
    private int spielfeldGroesse;
    private int gefangenenAnzahlWeiss;
    private int gefangenenAnzahlSchwarz;
    private List<Spielzug> spielZugCollection;
    
    // Chache Funktionen zum schnelleren Spielen
    private int[][] aktuellesSpielfeldCache;
    private int spielfeldCacheMitZugnummerStand;

    public void Spielfeld(int spielfeldGroesse) {
        this.spielfeldGroesse = spielfeldGroesse;
        this.letzteZugnummer = 0;
        this.setGefangenenAnzahlSchwarz(0);
        this.setGefangenenAnzahlWeiss(0);
        this.spielZugCollection = new ArrayList<Spielzug>();


        // Chache Funktionen setzen
        aktuellesSpielfeldCache = new int[this.getSpielfeldGroesse()][this.getSpielfeldGroesse()];
        spielfeldCacheMitZugnummerStand = this.letzteZugnummer;

    }

    /**
     * 
     * @return Die Nummer des zuletzt gespielten Zuges
     */
    public int getLetzteZugnummer() {
        return this.letzteZugnummer;
    }

    /**
     *
     * @return Größe des aktuellen Spielfeldes
     */
    public int getSpielfeldGroesse() {
        return this.spielfeldGroesse;
    }

    /**
     *
     * @return Anzahl der gefangenen schwarzen Stein (Punkte für weiss)
     */
    public int getGefangenenAnzahlWeiss() {
        return this.gefangenenAnzahlWeiss;
    }

    /**
     *
     * @param Speichern der Anzahl der gefangenen schwarzen Stein (Punkte für weiss)
     */
    public void setGefangenenAnzahlWeiss(int gefangenenAnzahl) {
        this.gefangenenAnzahlWeiss = gefangenenAnzahl;
    }

    /**
     *
     * @return Anzahl der gefangenen weissen Stein (Punkte für schwarz)
     */
    public int getGefangenenanzahlSchwarz() {
        return this.gefangenenAnzahlSchwarz;
    }

    /**
     * 
     * @param Anzahl der gefangenen weissen Stein (Punkte für schwarz)
     */
    public void setGefangenenAnzahlSchwarz(int gefangenenAnzahl) {
        this.gefangenenAnzahlSchwarz = gefangenenAnzahl;
    }

    /**
     *
     * @return Gibt das aktuelle Spielfeld als Array zurück
     */
    public int[][] getAktuelesSpielFeld() {

        int spielfeld[][];

        if (this.spielfeldCacheMitZugnummerStand != this.letzteZugnummer) {
            spielfeld = new int[this.getSpielfeldGroesse()][this.getSpielfeldGroesse()];

            // Spielfeld initialisieren
            for (int i = this.getSpielfeldGroesse() - 1; i >= 0; i--) {
                for (int j = this.getSpielfeldGroesse() - 1; j >= 0; j--) {
                    spielfeld[j][i] = Konstante.SCHNITTPUNKT_LEER;
                }
            }
            /* Jetzt ist das Brett noch leer. Es muss also mit den richtigen
               Werten gefuellt werden - Fehlt noch*/

            this.aktuellesSpielfeldCache = spielfeld;
            this.spielfeldCacheMitZugnummerStand = this.letzteZugnummer;

            /*
             * Hier muss jetzt noch nach dem initialisieren die Werte aus der Collection
             * abgefragt werden und ins Feld eingetragen werden
             *
             */

        } else {
            spielfeld = this.aktuellesSpielfeldCache;
        }

        return spielfeld;
    }

    /**
     *
     * @param Zeipunkt zu dem das Spielfeld zurückgegeben werden soll.
     * In erster Linie für REDO und UNDO gedacht, nicht zum häufigen aufrufen,
     * da das Spielfeld hierbei immer neu generiert werden muss
     * @return Spielfeld zum übergebenen Zeipunkt in Form eines Arrays
     */
    public int[][] getSpielfeldZumZeitpunkt(int zeitpunkt) {

        int spielfeld[][];

        if (zeitpunkt == this.spielfeldCacheMitZugnummerStand) {
            spielfeld = this.aktuellesSpielfeldCache;
        } else {
            spielfeld = new int[this.getSpielfeldGroesse()][this.getSpielfeldGroesse()];

            // Spielfeld initialisieren
            for (int i = this.getSpielfeldGroesse() - 1; i >= 0; i--) {
                for (int j = this.getSpielfeldGroesse() - 1; j >= 0; j--) {
                    spielfeld[j][i] = Konstante.SCHNITTPUNKT_LEER;
                }
            }

            /*
             * Hier muss jetzt noch nach dem initialisieren die Werte aus der Collection
             * abgefragt werden und ins Feld eingetragen werden
             */
        }

        return spielfeld;
    }

    /**
     *
     * @param Zeitpunkt auf den das Spielfeld FEST gesetzt werden soll.
     * Ein Redo ist dann nicht mehr möglich. Das Spiel wird zu diesem Zeitpunkt
     * weitergespielt.
     * @return True, wenn die Funktion erfolgreich ausgeführt wurde, sonst false
     */
    public boolean setSpielfeldZumZeitpunkt(int zeitpunkt) {
        /*
         * Setzen des Spiels auf den Zeitpunkt:
         * - prüfen ob Möglich
         * - überflüssie Spielzüge aus der Collection löschen
         * - Chache erneuern
         * - ChachZähler erneuern
         * - SpielZeitpunkt erneuern
         * - Bei Erfolg true zurückgeben, sonst false
         */

        return true;
    }

    /**
     * 
     * @param X-Position des Spielfelds. 
     * Diese kann Werte zwischen 1 und der Feldlänge enthalten
     * @param Y-Position des Spielfelds. 
     * Diese kann Werte zwischen 1 und der Feldlänge enthalten
     *
     */
    public boolean setStein(int xPos, int yPos) {

        /* Die Funktion setzt den Stein fuer abhaengig vom letzten gespielten Zug.
         * Wenn also Spieler schwarz den letzten Zug gesetzt hat, wird der neue
         * Zug für Spieler weiss eingetragen.
         */


         return   setStein( xPos, yPos, this.getSpielerAnDerReihe() );


         /* Weitere Aufgaben:
         *  - Prüfen des Zuges, ob er Möglich ist (Doppelzüge beachten)
         *      => Ueber die Funktion setSteinMoeglich( ... )
         *  - Prüfen wer am Zug ist
         *  - Cache-Werte INKREMENTELL erneuern.
         */
        
        //DUmmy
    }

    /**
     *
     * @param xPos X-Position des Spielfelds.
     * Diese kann Werte zwischen 1 und der Feldlänge enthalten
     * @param yPos Y-Position des Spielfelds.
     * Diese kann Werte zwischen 1 und der Feldlänge enthalten
     * @param spielerfarbe Farbe des setzenden Spielers
     * @return setStein gibt zurueck, ob das Setzen erfolgreich
     */
    public boolean setStein(int xPos, int yPos, int spielerfarbe) {
        /*
         * siehe Funktion setStein(...)
         */
        return false; // Erstmal false machen... sollte geaendert werden wenn die Funktion was macht!!
    }

    public boolean setSteinMoeglich( int xPos, int yPos, int spielerfarbe ) {
        /*
         * Prüft ab ob an diesem Punkt ein Stein gesetzt werden darf.
         * Funktion wird verwendet, wenn zum Beispiel über einen Schnittpunkt
         * mit der Maus gefahren wird
         *
         * Evt. kann auch hierfür eine Cache-Collection aufgebaut werden,
         * die mit dem setzen eines neuen Steines dann allerdings wieder gelöscht
         * werden muss.
         */


        return true;
    }

    private int getSpielerAnDerReihe(){

        int letzterSpieler = getSpielerVonLetztemZug();
        
        if (letzterSpieler == Konstante.SCHNITTPUNKT_SCHWARZ){
            return Konstante.SCHNITTPUNKT_WEISS;
        }
        else if (letzterSpieler == Konstante.SCHNITTPUNKT_WEISS){
            return Konstante.SCHNITTPUNKT_SCHWARZ;
        }
        else{
            // FEHLER
            return Konstante.FEHLER;
        }
        

    }

    private int getSpielerVonLetztemZug(){
        /* Ich bin mir nicht ganz sicher ob die Funktion funktioniert
             und ob sie auch noch funktioniert, wenn mal Elemente in der Collection
             gelöscht wurden.
         */
        return this.spielZugCollection.get(this.spielZugCollection.size()-1).getFarbe();
    }

}
