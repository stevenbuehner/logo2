/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Klassen;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author steven, tommy
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
        /* Feld muss als Leeres Feld initialisiert werden */
        int i,j;
        for(i=0; i<spielfeldGroesse; i++){
            for(j=0; i<spielfeldGroesse; j++){
                aktuellesSpielfeldCache[i][j] = Konstante.SCHNITTPUNKT_LEER;
            }
        }
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
     * @param gefangenenAnzahl Speichern der Anzahl der gefangenen schwarzen Stein (Punkte für weiss)
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
     * @param gefangenenAnzahl Anzahl der gefangenen weissen Stein (Punkte für schwarz)
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
     * @param zeitpunkt Zeipunkt zu dem das Spielfeld zurückgegeben werden soll.
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
     * @param zeitpunkt Zeitpunkt auf den das Spielfeld FEST gesetzt werden soll.
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
     * @param xPos X-Position des Spielfelds.
     * Diese kann Werte zwischen 1 und der Feldlänge enthalten
     * @param yPos Y-Position des Spielfelds.
     * Diese kann Werte zwischen 1 und der Feldlänge enthalten
     * @return Gibt zurueck, ob das setzen erfolgreich war
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

    /*
     * Wollen wir die Funktion loeschen? Ich waere dafuer, da wir sie wahrscheinlich nicht
     * brauchen. gez tommy
     */
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


    /**
     * Diese Funktion soll Steine vom Spielfeld nehmen, wenn dies moeglich ist.
     * Dabei benoetigt sie eine Koordinate mit der sie Beginnen kann, damit klar
     * ist, welche Gruppe gemeint ist. Von dieser Koordinate aus versucht sie dann
     * Steine vom Brett zu entfernen.
     * @param xPos Ist die X-Koordinate des Steines, der zur Gruppe gehoert,
     * die vom Brett zu nehmen versucht wird.
     * @param yPos Ist die Y-Koordinate des Steins.
     * @param Feld Ist ein 2-Dimensionales Array vom Typ AnalyseSchnittpunkt. Es
     * dient als Brettstellung, die zu betrachten ist.
     * @return Der Rueckgabewert kennzeichnet bei Erfolg (also wenn die Gruppe
     * vom Brett genommen werden konnte) wieviele Steine entfernt worden. Bei
     * Misserfolg gibt es 2 Moeglichkeiten: 1. Die Gruppe konnte nicht entfernt
     * werden, da sie noch Freiheiten besitzt (Rueckgabewert 0). 2. Die Gruppe
     * konnte nicht entfernt werden, weil an (xPos, yPos) gar kein Stein liegt
     * (Rueckgabewert -1)
     */
    private int versucheSteinZuNehmen(int xPos, int yPos, AnalyseSchnittpunkt feld[][]){
        /* Zuerst testen ob an (x,y) ueberhaupt ein Stein ist */
        if(feld[xPos][yPos].getBelegungswert()==Konstante.SCHNITTPUNKT_LEER || feld[xPos][yPos].getBelegungswert()==Konstante.SCHNITTPUNKT_VERBOTEN){
            return -1;
        }

        /* Noch abfangen, ob Wert falsch ist, denn logischer weise sollte jetzt
         * der Belegungswert schwarz oder weiss sein */
        if(feld[xPos][yPos].getBelegungswert()!=Konstante.SCHNITTPUNKT_SCHWARZ && feld[xPos][yPos].getBelegungswert()!=Konstante.SCHNITTPUNKT_WEISS){
            return -2;
        }

        /* Jetzt beginnt die eigentliche Prozedur, da man weiss, das es sich entweder
         * um einen schwarzen oder einen weissen Stein handelt. Zuerst wird getestet ob die
         * Steine schon betrachtet wurden. Ist dies der Fall wird abgebrochen und
         * gesagt, das keine Steine gefangen worden (Da diese wenn sie schon
         * markiert sind auch schon vom Brett genommen worden, oder nicht)*/
        if(feld[xPos][yPos].getSteinStatus()==Konstante.STEIN_GEFANGEN || feld[xPos][yPos].getSteinStatus()==Konstante.STEIN_LEBENDIG){
            return 0; // nichts veraendert
        }

        /* Es muss also was veraendert werden. Am ende der Funktion werden
         * entweder alle Steine der Gruppe als lebendig markiert, oder alle als
         * tot markiert und vom Brett (richtigen spielfeld) genommen. */
        boolean fangbar = true;
        int feldGroesse = this.getSpielfeldGroesse();
        int farbe = feld[xPos][yPos].getBelegungswert();
        AnalyseSchnittpunkt [] listeSteine;             // Das sollte man mit ner
        listeSteine = new AnalyseSchnittpunkt[362];     // Collection auch loesen koennen
        int momElement = 0;
        int endElement = 1;
        feld[xPos][yPos].setMarkiert(true);
        listeSteine[0] = feld[xPos][yPos];
        /* Jetzt solange nach Steinen suchen und diese in Liste aufnehmen,
         * bis keine Steine mehr da sind */
        do{
            /* Jetzt, wenn Nachbarsteine existieren und diese noch nicht
             * markiert sind, dann werden diese in Liste aufgenommen.*/

            /* Wenn Stein linken Nachbarn hat*/
             if(listeSteine[momElement].getXPos()!=0){
                 /* Nachsehen, ob linker nachbar Leer, dann Gruppe nicht fangbar! */
                 if(feld[listeSteine[momElement].getXPos()-1][listeSteine[momElement].getYPos()].getBelegungswert() == Konstante.SCHNITTPUNKT_LEER ||
                    feld[listeSteine[momElement].getXPos()-1][listeSteine[momElement].getYPos()].getBelegungswert() == Konstante.SCHNITTPUNKT_VERBOTEN){
                     fangbar=false; // Gruppe nicht fangbar, da sie eine Freiheit hat.
                 }
                 /* Der Stein hat also einen Nachbarstein, wenn dieser nicht der
                  * Steinfarbe entspricht, muss er auch nicht in die Liste
                  * Aufgenommen werden. Hat er allerdings die gleiche Farbe,
                  * wird er markiert und in die Liste aufgenommen */
                 else if(feld[listeSteine[momElement].getXPos()-1][listeSteine[momElement].getYPos()].getBelegungswert() == farbe){
                     /* Nachbarstein ist also von der gleichen Farbe. Dieser
                      * muss markiert und in die Liste aufgenommen werden, aber
                      * nur wenn sie noch nicht markiert sind */
                     if(feld[listeSteine[momElement].getXPos()-1][listeSteine[momElement].getYPos()].getMarkiert() == false){
                         feld[listeSteine[momElement].getXPos()-1][listeSteine[momElement].getYPos()].setMarkiert(true);
                         listeSteine[endElement] = feld[listeSteine[momElement].getXPos()-1][listeSteine[momElement].getYPos()];
                         ++endElement;
                     }
                 }
             }

             /* Wenn Stein rechten Nachbarn hat */
             if(listeSteine[momElement].getXPos()!=feldGroesse-1){
                 /* Nachsehen, ob linker nachbar Leer, dann Gruppe nicht fangbar! */
                 if(feld[listeSteine[momElement].getXPos()+1][listeSteine[momElement].getYPos()].getBelegungswert() == Konstante.SCHNITTPUNKT_LEER ||
                    feld[listeSteine[momElement].getXPos()+1][listeSteine[momElement].getYPos()].getBelegungswert() == Konstante.SCHNITTPUNKT_VERBOTEN){
                     fangbar=false; // Gruppe nicht fangbar, da sie eine Freiheit hat.
                 }
                 /* Der Stein hat also einen Nachbarstein, wenn dieser nicht der
                  * Steinfarbe entspricht, muss er auch nicht in die Liste
                  * Aufgenommen werden. Hat er allerdings die gleiche Farbe,
                  * wird er markiert und in die Liste aufgenommen */
                 else if(feld[listeSteine[momElement].getXPos()+1][listeSteine[momElement].getYPos()].getBelegungswert() == farbe){
                     /* Nachbarstein ist also von der gleichen Farbe. Dieser
                      * muss markiert und in die Liste aufgenommen werden, aber
                      * nur wenn sie noch nicht markiert sind */
                     if(feld[listeSteine[momElement].getXPos()+1][listeSteine[momElement].getYPos()].getMarkiert() == false){
                         feld[listeSteine[momElement].getXPos()+1][listeSteine[momElement].getYPos()].setMarkiert(true);
                         listeSteine[endElement] = feld[listeSteine[momElement].getXPos()+1][listeSteine[momElement].getYPos()];
                         ++endElement;
                     }
                 }
             }

             /* Wenn Stein oberen Nachbarn hat (Oben und unten werden vielleicht
              * umdefiniert, ist aber egal fuer den Algorithmus!)*/
             if(listeSteine[momElement].getYPos()!=feldGroesse-1){
                 /* Nachsehen, ob linker nachbar Leer, dann Gruppe nicht fangbar! */
                 if(feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()+1].getBelegungswert() == Konstante.SCHNITTPUNKT_LEER ||
                    feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()+1].getBelegungswert() == Konstante.SCHNITTPUNKT_VERBOTEN){
                     fangbar=false; // Gruppe nicht fangbar, da sie eine Freiheit hat.
                 }
                 /* Der Stein hat also einen Nachbarstein, wenn dieser nicht der
                  * Steinfarbe entspricht, muss er auch nicht in die Liste
                  * Aufgenommen werden. Hat er allerdings die gleiche Farbe,
                  * wird er markiert und in die Liste aufgenommen */
                 else if(feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()+1].getBelegungswert() == farbe){
                     /* Nachbarstein ist also von der gleichen Farbe. Dieser
                      * muss markiert und in die Liste aufgenommen werden, aber
                      * nur wenn sie noch nicht markiert sind */
                     if(feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()+1].getMarkiert() == false){
                         feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()+1].setMarkiert(true);
                         listeSteine[endElement] = feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()+1];
                         ++endElement;
                     }
                 }
             }

             /* Wenn Stein unteren Nachbarn hat */
             if(listeSteine[momElement].getYPos()!=0){
                 /* Nachsehen, ob linker nachbar Leer, dann Gruppe nicht fangbar! */
                 if(feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()-1].getBelegungswert() == Konstante.SCHNITTPUNKT_LEER ||
                    feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()-1].getBelegungswert() == Konstante.SCHNITTPUNKT_VERBOTEN){
                     fangbar=false; // Gruppe nicht fangbar, da sie eine Freiheit hat.
                 }
                 /* Der Stein hat also einen Nachbarstein, wenn dieser nicht der
                  * Steinfarbe entspricht, muss er auch nicht in die Liste
                  * Aufgenommen werden. Hat er allerdings die gleiche Farbe,
                  * wird er markiert und in die Liste aufgenommen */
                 else if(feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()-1].getBelegungswert() == farbe){
                     /* Nachbarstein ist also von der gleichen Farbe. Dieser
                      * muss markiert und in die Liste aufgenommen werden, aber
                      * nur wenn sie noch nicht markiert sind */
                     if(feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()-1].getMarkiert() == false){
                         feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()-1].setMarkiert(true);
                         listeSteine[endElement] = feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()-1];
                         ++endElement;
                     }
                 }
             }

             /* Jetzt wurden alle Nachbarn aufgenommen. Der naechste Stein wird
              * also untersucht. Doch zuerst muss der jetzige sauber
              * abgeschlossen werden */
             feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()].setAnalysiert(true);
             ++momElement;
        }while(momElement<endElement);

        /* Alle Steine der Gruppe sind jetzt in der Liste. Jetzt wird weiter
         * gemacht, ja nachdem die Steine gefangen werden konnten */
        if(fangbar==false){
            /* Steine als lebendig markieren */
            /* da momElement gerade auf leeres Feld zeigt! dekrementieren */
            for(int i = momElement - 1; i>=0; --i) {
                feld[listeSteine[i].getXPos()][listeSteine[i].getYPos()].setSteinStatus(Konstante.STEIN_LEBENDIG);
            }
        return 0; // wurde ja kein Stein gefangen
        }
        else {
            for(int i = momElement -1; i>=0; --i) {
                feld[listeSteine[i].getXPos()][listeSteine[i].getYPos()].setSteinStatus(Konstante.STEIN_GEFANGEN);
                aktuellesSpielfeldCache[listeSteine[i].getXPos()][listeSteine[i].getYPos()] = Konstante.SCHNITTPUNKT_LEER;
            }
        return momElement; // momElement ist gerade die Gefangenenzahl ;)
        }
    }

}
