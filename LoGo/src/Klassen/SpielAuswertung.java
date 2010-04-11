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
     * @param xPos X-Koordinate
     * @param yPos Y-Koordinate
     */
    public void markiereSteinAlsGefangen(int xPos, int yPos){

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
                if(this.auswertungBrett[i][j].getBelegungswert() == Konstante.SCHNITTPUNKT_NEUTRAL){
                    rueckgabe[i][j] = Konstante.SCHNITTPUNKT_LEER;
                }
                else {
                    rueckgabe[i][j] = this.auswertungBrett[i][j].getBelegungswert();
                }
            }
        }
        return rueckgabe;
    }

    /**
     * Diese Funktion ist dafuer verantwortlich Gebiete am anfang der Auswertung
     * zu marikieren. Sie dient somit als Ausgangssituation der Analyse.
     * Diese Funktion macht somit einen Vorschlag zur Gebietsverteilung.
     * Dafuer durfen auf dem Brett nur Felder mit Folgenden Werten sein:
     * Konstante.SCHNITTPUNKT_SCHWARZ
     * Konstante.SCHNITTPUNKT_WEISS
     * Konstante.SCHNITTPUNKT_LEER
     * Das bedeutet die Funktion wird nur beim initialisieren Aufgerufen!
     */
    private void findeReineGebiete() {

        /* Bevor es los geht muss Analysiert ueberall als False eingestellt
         * werden. Somit werden auch alle Punkte betrachtet.*/
        for(int i=0; i<this.getFeldGroesse(); i++){
            for(int j=0; j<this.getFeldGroesse(); j++){
                this.auswertungBrett[i][j].setAnalysiert(false);
            }
        }
        /*
         * Hier muss jeder Schnittpunkt untersucht werden. Ist dieser als Leer
         * markiert, wird versucht ein Reihnes Gebiet reinzulegen.
         */
        List<AnalyseSchnittpunkt> listeSteine = new ArrayList<AnalyseSchnittpunkt>();
        int momElement = 0;
        int farbe = Konstante.STEIN_UNGEWISS; // Bedeutet hier Farbe ungewiss!
        boolean gebietRein = true;
        for(int i=0; i<this.getFeldGroesse(); i++){
            for(int j=0; j<this.getFeldGroesse(); j++){
                /* Ist der Schnittpunkt leer, muss er ausgewertet werden */
                if(this.auswertungBrett[i][j].getBelegungswert() == Konstante.SCHNITTPUNKT_LEER){
                    /* Ist der Schnittpunkt leer, so kommt es drauf an, ob er
                     * schon Analysiert wurde */
                    if(this.auswertungBrett[i][j].getAnalysiert() == false){
                        /* Die Farbe der umliegenden Steine ist noch unbekannt.
                         * und es wird erst ausgegangen, das das gebiet rein ist*/
                        farbe = Konstante.STEIN_UNGEWISS;
                        gebietRein = true;
                        /* Der gefundene Schnittpunkt ist also noch nicht
                         * betrachtet worden. Daher beginnt jetzt die suche.
                         * Es wird eine Liste erstellt, in der die Steine
                         * gespeichert werden koennen. */
                        this.auswertungBrett[i][j].setMarkiert(true);
                        listeSteine.add(this.auswertungBrett[i][j]);
                        momElement = 0;
                        /* Solange die Liste noch nicht komplett durchsucht ist,
                         * versuche weitere Steine Aufzunehmen */
                        do{
                            /* Jetzt muessen die Nachbarsteine, wenn sie vorhanden
                             * sind betrachtet werden.
                             * Ist der Nachbarstein Leer und noch nicht markiert,
                             * so wird er in die Liste aufgenommen und markiert.
                             * Ist der Nachbarstein von Einer bestimmten Farbe,
                             * so muss nachgesehen werden, ob schon eine Farbe
                             * gefunden wurd: Wenn ja, und die Farben verschieden
                             * sind, so wird das feld nicht markiert. Es ist dann
                             * neutral. Sind die Farben gleich, ist alles Ok. 
                             * Wurde noch keine Farbe gefunden, muss sie eingestellt
                             * werden. 
                             */

                            /* 1. Linke Seite */
                            if(listeSteine.get(momElement).getXPos()!=0){
                                if(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                                                                == Konstante.SCHNITTPUNKT_LEER &&
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getMarkiert()
                                                                                == false){
                                   listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()]);
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].setMarkiert(true);
                                }
                                else {
                                    switch(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()){
                                        case Konstante.SCHNITTPUNKT_SCHWARZ:
                                            switch(farbe){
                                                case Konstante.STEIN_UNGEWISS:
                                                    farbe = Konstante.SCHNITTPUNKT_SCHWARZ;
                                                    break;
                                                case Konstante.SCHNITTPUNKT_SCHWARZ:
                                                    break;
                                                case Konstante.SCHNITTPUNKT_WEISS:
                                                    gebietRein = false;
                                                    break;
                                                default: /* Darf nicht passieren*/
                                                    throw new UnsupportedOperationException("Die Farbe enthaelt unerwartete Werte " + farbe +" (Reine Gebiete finden)");
                                            }
                                            break;
                                        case Konstante.SCHNITTPUNKT_WEISS:
                                            switch(farbe){
                                                case Konstante.STEIN_UNGEWISS:
                                                    farbe = Konstante.SCHNITTPUNKT_WEISS;
                                                    break;
                                                case Konstante.SCHNITTPUNKT_WEISS:
                                                    break;
                                                case Konstante.SCHNITTPUNKT_SCHWARZ:
                                                    gebietRein = false;
                                                    break;
                                                default: /* Darf nicht passieren*/
                                                    throw new UnsupportedOperationException("Die Farbe enthaelt unerwartete Werte " + farbe +" (Reine Gebiete finden)");
                                            }
                                            break;
                                        default : /* Darf nicht passieren*/
                                            throw new UnsupportedOperationException("Der Schnittpunkt im auswertungsBrett hat falsche Werte");
                                    }
                                }
                            }
                            
                            /* 2. Rechte Seite */
                            if(listeSteine.get(momElement).getXPos()!=this.getFeldGroesse()-1){
                                if(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                                                                == Konstante.SCHNITTPUNKT_LEER &&
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getMarkiert()
                                                                                == false){
                                   listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()]);
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].setMarkiert(true);
                                }
                                else {
                                    switch(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()){
                                        case Konstante.SCHNITTPUNKT_SCHWARZ:
                                            switch(farbe){
                                                case Konstante.STEIN_UNGEWISS:
                                                    farbe = Konstante.SCHNITTPUNKT_SCHWARZ;
                                                    break;
                                                case Konstante.SCHNITTPUNKT_SCHWARZ:
                                                    break;
                                                case Konstante.SCHNITTPUNKT_WEISS:
                                                    gebietRein = false;
                                                    break;
                                                default: /* Darf nicht passieren*/
                                                    throw new UnsupportedOperationException("Die Farbe enthaelt unerwartete Werte " + farbe +" (Reine Gebiete finden)");
                                            }
                                            break;
                                        case Konstante.SCHNITTPUNKT_WEISS:
                                            switch(farbe){
                                                case Konstante.STEIN_UNGEWISS:
                                                    farbe = Konstante.SCHNITTPUNKT_WEISS;
                                                    break;
                                                case Konstante.SCHNITTPUNKT_WEISS:
                                                    break;
                                                case Konstante.SCHNITTPUNKT_SCHWARZ:
                                                    gebietRein = false;
                                                    break;
                                                default: /* Darf nicht passieren*/
                                                    throw new UnsupportedOperationException("Die Farbe enthaelt unerwartete Werte " + farbe +" (Reine Gebiete finden)");
                                            }
                                            break;
                                        default : /* Darf nicht passieren*/
                                            throw new UnsupportedOperationException("Der Schnittpunkt im auswertungsBrett hat falsche Werte");
                                    }
                                }
                            }

                            /* 3. Untere Seite */
                            if(listeSteine.get(momElement).getYPos()!=0){
                                if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                                                                                == Konstante.SCHNITTPUNKT_LEER &&
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getMarkiert()
                                                                                == false){
                                   listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1]);
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].setMarkiert(true);
                                }
                                else {
                                    switch(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()){
                                        case Konstante.SCHNITTPUNKT_SCHWARZ:
                                            switch(farbe){
                                                case Konstante.STEIN_UNGEWISS:
                                                    farbe = Konstante.SCHNITTPUNKT_SCHWARZ;
                                                    break;
                                                case Konstante.SCHNITTPUNKT_SCHWARZ:
                                                    break;
                                                case Konstante.SCHNITTPUNKT_WEISS:
                                                    gebietRein = false;
                                                    break;
                                                default: /* Darf nicht passieren*/
                                                    throw new UnsupportedOperationException("Die Farbe enthaelt unerwartete Werte " + farbe +" (Reine Gebiete finden)");
                                            }
                                            break;
                                        case Konstante.SCHNITTPUNKT_WEISS:
                                            switch(farbe){
                                                case Konstante.STEIN_UNGEWISS:
                                                    farbe = Konstante.SCHNITTPUNKT_WEISS;
                                                    break;
                                                case Konstante.SCHNITTPUNKT_WEISS:
                                                    break;
                                                case Konstante.SCHNITTPUNKT_SCHWARZ:
                                                    gebietRein = false;
                                                    break;
                                                default: /* Darf nicht passieren*/
                                                    throw new UnsupportedOperationException("Die Farbe enthaelt unerwartete Werte " + farbe +" (Reine Gebiete finden)");
                                            }
                                            break;
                                        default : /* Darf nicht passieren*/
                                            throw new UnsupportedOperationException("Der Schnittpunkt im auswertungsBrett hat falsche Werte");
                                    }
                                }
                            }

                            /* 4. Obere Seite */
                            if(listeSteine.get(momElement).getYPos()!=this.getFeldGroesse()-1){
                                if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                                                                                == Konstante.SCHNITTPUNKT_LEER &&
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getMarkiert()
                                                                                == false){
                                   listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1]);
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].setMarkiert(true);
                                }
                                else {
                                    switch(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()){
                                        case Konstante.SCHNITTPUNKT_SCHWARZ:
                                            switch(farbe){
                                                case Konstante.STEIN_UNGEWISS:
                                                    farbe = Konstante.SCHNITTPUNKT_SCHWARZ;
                                                    break;
                                                case Konstante.SCHNITTPUNKT_SCHWARZ:
                                                    break;
                                                case Konstante.SCHNITTPUNKT_WEISS:
                                                    gebietRein = false;
                                                    break;
                                                default: /* Darf nicht passieren*/
                                                    throw new UnsupportedOperationException("Die Farbe enthaelt unerwartete Werte " + farbe +" (Reine Gebiete finden)");
                                            }
                                            break;
                                        case Konstante.SCHNITTPUNKT_WEISS:
                                            switch(farbe){
                                                case Konstante.STEIN_UNGEWISS:
                                                    farbe = Konstante.SCHNITTPUNKT_WEISS;
                                                    break;
                                                case Konstante.SCHNITTPUNKT_WEISS:
                                                    break;
                                                case Konstante.SCHNITTPUNKT_SCHWARZ:
                                                    gebietRein = false;
                                                    break;
                                                default: /* Darf nicht passieren*/
                                                    throw new UnsupportedOperationException("Die Farbe enthaelt unerwartete Werte " + farbe +" (Reine Gebiete finden)");
                                            }
                                            break;
                                        default : /* Darf nicht passieren*/
                                            throw new UnsupportedOperationException("Der Schnittpunkt im auswertungsBrett hat falsche Werte");
                                    }
                                }
                            }

                            /* Zuletzt muss sauber abgeschlossen werden. */
                            listeSteine.get(momElement).setAnalysiert(true);
                            momElement++;
                        }while(momElement<listeSteine.size());

                        /* Jetzt sind in listeSteine alle Leeren Schnittpunkte,
                         * die sich mit dem Punkt (i,j) im Gleichen bereich
                         * befinden. Jetzt haengt alles weitere von der Variable
                         * gebietRein ab. Ist das Gebiet rein, so muessen alle
                         * Schnittpunkte als Gebiet der Variable farbe markiert
                         * werden. Ist gebietRein false so werden die Punkte
                         * als leer markiert. Die listeSteine muss dann geloescht
                         * werden. */
                        if(gebietRein == true){
                            for(int k = momElement-1; k>=0; --k){
                                switch(farbe){
                                    case Konstante.SCHNITTPUNKT_SCHWARZ:
                                        listeSteine.get(k).setBelegungswert(Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ);
                                        break;
                                    case Konstante.SCHNITTPUNKT_WEISS:
                                        listeSteine.get(k).setBelegungswert(Konstante.SCHNITTPUNKT_GEBIET_WEISS);
                                        break;
                                    default: /* Darf nicht passieren */
                                        throw new UnsupportedOperationException("Farbe wurde nicht definiert.");
                                }
                                listeSteine.remove(k);
                            }
                            if(listeSteine.size()!=0){
                                throw new UnsupportedOperationException("ListeSteine konnte nicht geleert werden");
                            }
                        }
                        else {
                            for(int k = momElement-1; k>=0; --k ){
                                listeSteine.get(k).setBelegungswert(Konstante.SCHNITTPUNKT_LEER);
                                listeSteine.remove(k);
                            }
                            if(listeSteine.size()!=0){
                                throw new UnsupportedOperationException("ListeSteine konnte nicht geleert werden");
                            }
                        }
                    }
                }
            }
        }

        /* Jetzt wurde das ganze Brett durchsucht. Nun muss noch das Analysiert-
         * Flag auf false gesetzt werden. Dann koennen die anderen Funktionen
         * sicherer arbeiten. */
        for(int i=0; i<this.getFeldGroesse(); i++){
            for(int j=0; j<this.getFeldGroesse(); j++){
                this.auswertungBrett[i][j].setAnalysiert(false);
            }
        }
        /* Der Vorschlag vom Programm ist somit fertig! */
    }

    /**
     * Diese Gruppe setzt neutrale Steine. Somit koennen alle Schnittpunkte
     * analysiert werden.
     */
    private void setzeFuellsteine() {

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
     * Funktion, die die Auswertung intern steuert.
     */
    private void werteBrettAus(){
        this.setzeFuellsteine();
        this.findePseudoPunkte();
    }
}
