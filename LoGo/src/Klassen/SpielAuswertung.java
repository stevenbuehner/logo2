/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Klassen;

import java.util.ArrayList;
import java.util.List;
/**
 * Diese Klasse uebernimmt die Auswertung eines Spielfeldes
 * Dabei werden Gebietspunkte, Gefangenenpunkte und Komi beruecksichtigt
 * @author tommy
 */
public class SpielAuswertung {
    private int komiFuerWeiss;
    private int gefangeneVonSchwarz;
    private int gefangeneVonWeiss;
    private AnalyseSchnittpunkt auswertungBrett[][];
    private int brettGroesse;
    private int eingegebenesFeld[][];

    /**
     * Der Konstruktor wird erst aufgerufen wenn das Spiel ausgewertet werden
     * soll. Brettgroesse und Komi sind daher bekannt
     * @param brettGroesse Bettgroesse
     * @param komiFuerWeiss Komi: Punkte werden Weiss gutgeschrieben.
     */
    public SpielAuswertung(int brettGroesse, int komiFuerWeiss){
        this.brettGroesse = brettGroesse;
        this.komiFuerWeiss = komiFuerWeiss;
        this.setGefangeneVonSchwarz(0);
        this.setGefangeneVonWeiss(0);
        this.auswertungBrett = new AnalyseSchnittpunkt[this.getFeldGroesse()][this.getFeldGroesse()];
        for(int i=0; i<this.getFeldGroesse(); i++){
            for(int j=0; j<this.getFeldGroesse(); j++){
                this.auswertungBrett[i][j] = new AnalyseSchnittpunkt(i,j,Konstante.SCHNITTPUNKT_LEER);
            }
        }
        this.eingegebenesFeld = new int[this.getFeldGroesse()][this.getFeldGroesse()];
        for(int i=0; i<this.getFeldGroesse(); i++){
            for(int j=0; j<this.getFeldGroesse(); j++){
                this.eingegebenesFeld[i][j] = Konstante.SCHNITTPUNKT_LEER;
            }
        }
    }

    public SpielAuswertung(int brettGroesse){
        this(brettGroesse, 0);
    }

    /**
     * Legt die Zahl der Steine fest, die der schwarze Spieler gefangen hat.
     * @param gefangeneVonSchwarz Anzahl der Steine
     */
    public void setGefangeneVonSchwarz(int gefangeneVonSchwarz){
        this.gefangeneVonSchwarz = gefangeneVonSchwarz;
    }

    /**
     * Gibt die Anzahl der Steine wieder, die als Gefangenenanzahl fuer Schwarz
     * eingestellt wurden.
     * @return Anzahl der Steine
     */
    public int getGefangeneVonSchwarz(){
        return this.gefangeneVonSchwarz;
    }

    /**
     * Legt die Zahl der Steine fest, die der weisse Spieler gefangen hat.
     * @param gefangeneVonWeiss Anzahl der Steine
     */
    public void setGefangeneVonWeiss(int gefangeneVonWeiss){
        this.gefangeneVonWeiss = gefangeneVonWeiss;
    }

    /**
     * Gibt die Anzahl der Steine wieder, die als Gefangenenanzahl fuer Schwarz
     * eingestellt wurden.
     * @return Anzahl der Steine
     */
    public int getGefangeneVonWeiss(){
        return this.gefangeneVonWeiss;
    }

    /**
     * Fuer interne Zwecke. Gibt die eingestellte Feldgroesse wieder.
     * @return Feldgroesse  (auswertungBrett)
     */
    private int getFeldGroesse(){
        return this.brettGroesse;
    }

    /**
     * Diese Funktion liest das auszuwertende Feld ein. Bevor irgendetwas berechnet
     * wird, sollte das Feld initialisiert werden. Sonst ist es Leer. Dabei wird
     * kein Unterschied zwischen einem Verbotenen Punkt und einem Leeren Schnittpunkt
     * gemacht. Der Grund ist, weil sowieso nicht weiter gespielt wird und der
     * Schnittpunkt daher als leer anzusehen ist.
     * @param feld Zu initialisierendes Feld (2-Dim-Array)
     */
    public void auswertungInitialisieren(int feld[][]){
        for(int i=0; i<this.getFeldGroesse(); i++){
            for(int j=0; j<this.getFeldGroesse(); j++){
                switch(feld[i][j]){
                    case Konstante.SCHNITTPUNKT_SCHWARZ :
                        this.eingegebenesFeld[i][j] = Konstante.SCHNITTPUNKT_SCHWARZ;
                        this.auswertungBrett[i][j].setBelegungswert(Konstante.SCHNITTPUNKT_SCHWARZ);
                        break;
                    case Konstante.SCHNITTPUNKT_WEISS :
                        this.eingegebenesFeld[i][j] = Konstante.SCHNITTPUNKT_WEISS;
                        this.auswertungBrett[i][j].setBelegungswert(Konstante.SCHNITTPUNKT_WEISS);
                        break;
                    case Konstante.SCHNITTPUNKT_LEER :
                    case Konstante.SCHNITTPUNKT_VERBOTEN:
                        this.eingegebenesFeld[i][j] = Konstante.SCHNITTPUNKT_LEER;
                        this.auswertungBrett[i][j].setBelegungswert(Konstante.SCHNITTPUNKT_LEER);
                        break;
                    default : /* Hierer darf man nicht kommen */
                        throw new UnsupportedOperationException("Das eingegebene Feld enthaelt unerwartete Werte an Stelle (" + i + "|" + j + ")");
                }
            }
        }
        this.findeReineGebiete();
    }

    /**
     * Damit man das Feld auswerten kann, muss der Benutzer signalisieren, welche
     * Steine auf dem Brett tot sind.
     * Erstmal als einfache Variante. Rekursiv lohnt sich aber vielleicht doch.
     * @param xPos X-Koordinate (von 1 bis Feldgroesse)
     * @param yPos Y-Koordinate (von 1 bis Feldgroesse)
     * @return Integer signalisiert wie funktion ausgegangen ist:
     * 1  : Brett wurde erfolgreich veraendert
     * 0  : Eingabe war Ok, aber Stein war schon gefangen: daher keine Aenderung
     * -1 : Eingabe war falsch: Koordinaten nicht auf dem Brett!
     * -2 : Koordinate auf die geklickt wurde ist nicht mit einem Stein belegt!
     */
    public int markiereSteinAlsGefangen(int xPos, int yPos){
     return 1;
    }

    /**
     * Moechte der Benutzer eine Markierung rueckgaengig machen, kann er das
     * mit dieser Funktion. Es ist Aufgabe der Steuerung, ob der Benutzer
     * einen Stein tot oder untot markieren will.
     * @param xPos X-Koordinate
     * @param yPos Y-Koordinate
     */
    public void markiereSteinAlsNichtGefangen(int xPos, int yPos){

    }

    /**
     * Wenn bei der Auswertung irgendwas Schief laeft (Weil der Benutzer aus
     * Versehen viele Gruppen als Tot markiert hat, die in Wirklichkeit leben)
     * kann die Auswertung auch einfach neu gestartet werden.
     */
    public void initialisiereNeu(){
        for(int i=0; i<this.getFeldGroesse(); i++){
            for(int j=0; j<this.getFeldGroesse(); j++){
                switch(this.eingegebenesFeld[i][j]){
                    case Konstante.SCHNITTPUNKT_SCHWARZ :
                        this.auswertungBrett[i][j].setBelegungswert(Konstante.SCHNITTPUNKT_SCHWARZ);
                        break;
                    case Konstante.SCHNITTPUNKT_WEISS :
                        this.auswertungBrett[i][j].setBelegungswert(Konstante.SCHNITTPUNKT_WEISS);
                        break;
                    case Konstante.SCHNITTPUNKT_LEER :
                        this.auswertungBrett[i][j].setBelegungswert(Konstante.SCHNITTPUNKT_LEER);
                        break;
                    default : /* Hierer darf man nicht kommen */
                        throw new UnsupportedOperationException("Das eingegebene Feld enthaelt unerwartete Werte an Stelle (" + i + "|" + j + ")");
                }
            }
        }
        this.findeReineGebiete();
    }

    /**
     * Um das Ergebnis der Auswertung sichtbar zu machen, wird ein Feld zurueckgegeben,
     * bei dem man sieht, welche Teile des Brettes schon wie ausgewertet wurden.
     * @return Brett mit folgenden moeglichen Werten:
     * - Konstante.SCHNITTPUNKT_SCHWARZ
     * - Konstante.SCHNITTPUNKT_WEISS
     * - Konstante.SCHNITTPUNKT_LEER
     * - Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ
     * - Konstante.SCHNITTPUNKT_GEBIET_WEISS
     */
    public int[][] getAusgewertetesFeld(){
        int rueckgabe[][] = new int[this.getFeldGroesse()][this.getFeldGroesse()];
        for(int i=0; i<this.getFeldGroesse(); i++){
            for(int j=0; j<this.getFeldGroesse(); j++){
              rueckgabe[i][j] = this.auswertungBrett[i][j].getBelegungswert();
            }
        }
        return rueckgabe;
    }

    /**
     * Diese Funktion ist dafuer verantwortlich reine Gebiete (also eindeutige)
     * zu marikieren. Sie kann jederzeit angewandt werden.
     * Diese Funktion macht somit einen Vorschlag zur Gebietsverteilung und
     * erkennt, wenn reines Gebiet beim Gefangenenklicken falsch gesetzt wurde.
     * Das Ergebnis dieser Funktion ist, dass das Auswertungsbrett veraendert
     * wird und zwar sind die aenderungen wie folgt von anderen Funktionen zu
     * interpretieren: Ist ein Schnittpunkt als schwarzes oder weisses Gebiet
     * markiert, sind diese Punkte folglich nicht neutral. Ist nach der Funktion
     * immer noch ein Feld als leer markiert, so kann man dieses Feld vorerst
     * als neutral betrachten. Dies ist fuer Seki und Pseudopunkte wichtig.
     * Dafuer durfen auf dem Brett nur Felder mit Folgenden Werten sein:
     * Konstante.SCHNITTPUNKT_SCHWARZ
     * Konstante.SCHNITTPUNKT_WEISS
     * Konstante.SCHNITTPUNKT_LEER
     * Das bedeutet die Funktion wird nur beim initialisieren Aufgerufen!
     */
    private void findeReineGebiete() {
    
    }

    /**
     * Tricky: Wenn nach dem Fuellsteine setzen eine Gruppe nur eine Freiheit hat,
     * aber trotzdem lebendig ist, so ist das Gebiet der Gruppe an dieser einen
     * Freiheit leer. Dies kommt daher, dass wenn neutrale Steine gesetzt werden,
     * diese Freiheit auch gefuellt werden muss.
     */
    private void findePseudoPunkte() {

    }

    /**
     * Wenn eine Gruppe als Tot markiert wurd, koennen dadurch Felder als
     * Punkte fuer den Gegner markiert bleiben.
     * Diese Funktion findet daher Punkte, die von lebenden schwarzen und weissen
     * Steinen umschlossen sind. Dabei werden die Gebietspunkte wieder als leer
     * gekennzeichnet und tote Steine wieder zum leben erweckt.
     */
    private void findeNichtPunkte() {

    }
}