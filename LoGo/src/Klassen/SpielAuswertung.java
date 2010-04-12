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
        this.findeGebiete();
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
        this.findeGebiete();
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
     * Diese Funktion findet Gebiete. Das bedeutet sich sucht nach "Flecken"
     * auf dem Brett, die nur von einer Steinfarbe (lebende) umschlossen sind.
     *
     * Die suche beginnt bei einem leeren Schnittpunkt und setzt dann mit
     * Breitensuche fort.
     */
    private void findeGebiete() {
        /* Als erstes werden alle Schnittpunkte als nicht analysiert und nicht
         * markiert gekennzeichnet. */
        for(int i=0; i<this.getFeldGroesse(); i++){
            for(int j=0; j<this.getFeldGroesse(); j++){
                this.auswertungBrett[i][j].setAnalysiert(false);
                this.auswertungBrett[i][j].setMarkiert(false);
            }
        }


        int farbe = -1;
        boolean gebietMarkieren = true;
        List<AnalyseSchnittpunkt> listeSteine = new ArrayList<AnalyseSchnittpunkt>();
        int momElement = 0;

        /* Jetzt wird jeder Schnittpunkt angesehen und versucht von ihm aus
         * eine Breitensuche zu starten
         */
        for(int i=0; i<this.getFeldGroesse(); i++){
            for(int j=0; j<this.getFeldGroesse(); j++){
                /* Vorraussetzung, fuer die Analyse ist, das der Schnittpunkt
                 * noch nicht analysiert wurd. Ist der Schnittpunkt Leer, beginnt
                 * eine normale suche. Eine suche darf nur bei einem Leeren Feld
                 * beginnen. Findet man waehrend der suche tote Steine
                 */
                if(this.auswertungBrett[i][j].getBelegungswert() == Konstante.SCHNITTPUNKT_LEER  &&
                   this.auswertungBrett[i][j].getAnalysiert() == false){
                   /* Nun beginnt die suche bei diesem Schnittpunkt */
                   farbe = -1;            // Farbe am anfang unbekannt
                   gebietMarkieren = true;
                   momElement = 0;

                   /* Als erstes wird der Schnittpunkt (i,j) aufgenommen. Von
                    * hier aus beginnt die Suche */
                   listeSteine.add(this.auswertungBrett[i][j]);
                   this.auswertungBrett[i][j].setMarkiert(true);

                   /* Solange die Liste nicht abgearbeitet wurde, suche weiter */
                   do{
                       /* Nun werden nacheinander die Nachbarsteine durchsucht.
                        * Vorrausgesetzt, er ist leer und nicht markiert, wird
                        * er aufgenommen.
                        * Findet man einen lebenden Stein, wird die Farbe und
                        * vielleicht gebietMarkieren veraender.
                        * Findet man einen toten Stein, oder einen als Gebiet
                        * markierten Punkt, wird gebietMarkieren als false
                        * gesetzt und am Ende wird somit nichts gemacht. Tote
                        * Steine und Gebiet-Markierte-Punkte werden auch nicht
                        * in die Liste aufgenommen.
                        */

                       /* 1. Linke Seite */
                       if(listeSteine.get(momElement).getXPos()!=0){
                           if(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_LEER &&
                              this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getMarkiert()
                                   == false){
                               /* Stein aufnehmen */
                               listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()]);
                               this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].setMarkiert(true);
                           }
                           /* Ist nachbarstein lebendig */
                           else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_SCHWARZ ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_WEISS){
                                   switch(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()){
                                       case Konstante.SCHNITTPUNKT_SCHWARZ :
                                           switch(farbe){
                                               case -1:
                                                   farbe = Konstante.SCHNITTPUNKT_SCHWARZ;
                                                   break;
                                               case Konstante.SCHNITTPUNKT_SCHWARZ :
                                                   /* nichts */
                                                   break;
                                               case Konstante.SCHNITTPUNKT_WEISS :
                                                   gebietMarkieren = false;
                                                   break;
                                               default: /* Darf nicht passieren */
                                                   throw new UnsupportedOperationException("Variable farbe enthaelt unerwartete Werte -> " + farbe);
                                           }
                                           break;
                                       case Konstante.SCHNITTPUNKT_WEISS :
                                           switch(farbe){
                                               case -1:
                                                   farbe = Konstante.SCHNITTPUNKT_WEISS;
                                                   break;
                                               case Konstante.SCHNITTPUNKT_SCHWARZ :
                                                   gebietMarkieren = false;
                                                   break;
                                               case Konstante.SCHNITTPUNKT_WEISS :
                                                   /* nichts */
                                                   break;
                                               default: /* Darf nicht passieren */
                                                   throw new UnsupportedOperationException("Variable farbe enthaelt unerwartete Werte -> " + farbe);
                                           }
                                           break;
                                       default : /* Darf nicht passieren */
                                           throw new UnsupportedOperationException("Fehler in switch case, eigentlich muesste hier SCHNITTPUNKT_SCHWARZ oder SCHNITTPUNKT_WEISS stehen");
                                   }
                           }

                           /* Komische Werte abfangen */
                           else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                            == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                            == Konstante.SCHNITTPUNKT_GEBIET_WEISS ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                            == Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                            == Konstante.SCHNITTPUNKT_WEISS_GEFANGEN){
                               gebietMarkieren = false;
                           }
                       }

                       /* 2. Rechte Seite */
                       if(listeSteine.get(momElement).getXPos()!=this.getFeldGroesse()-1){
                           if(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_LEER &&
                              this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getMarkiert()
                                   == false){
                               /* Stein aufnehmen */
                               listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()]);
                               this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].setMarkiert(true);
                           }
                           /* Ist nachbarstein lebendig */
                           else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_SCHWARZ ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_WEISS){
                                   switch(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()){
                                       case Konstante.SCHNITTPUNKT_SCHWARZ :
                                           switch(farbe){
                                               case -1:
                                                   farbe = Konstante.SCHNITTPUNKT_SCHWARZ;
                                                   break;
                                               case Konstante.SCHNITTPUNKT_SCHWARZ :
                                                   /* nichts */
                                                   break;
                                               case Konstante.SCHNITTPUNKT_WEISS :
                                                   gebietMarkieren = false;
                                                   break;
                                               default: /* Darf nicht passieren */
                                                   throw new UnsupportedOperationException("Variable farbe enthaelt unerwartete Werte -> " + farbe);
                                           }
                                           break;
                                       case Konstante.SCHNITTPUNKT_WEISS :
                                           switch(farbe){
                                               case -1:
                                                   farbe = Konstante.SCHNITTPUNKT_WEISS;
                                                   break;
                                               case Konstante.SCHNITTPUNKT_SCHWARZ :
                                                   gebietMarkieren = false;
                                                   break;
                                               case Konstante.SCHNITTPUNKT_WEISS :
                                                   /* nichts */
                                                   break;
                                               default: /* Darf nicht passieren */
                                                   throw new UnsupportedOperationException("Variable farbe enthaelt unerwartete Werte -> " + farbe);
                                           }
                                           break;
                                       default : /* Darf nicht passieren */
                                           throw new UnsupportedOperationException("Fehler in switch case, eigentlich muesste hier SCHNITTPUNKT_SCHWARZ oder SCHNITTPUNKT_WEISS stehen");
                                   }
                           }

                           /* Komische Werte abfangen */
                           else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                            == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                            == Konstante.SCHNITTPUNKT_GEBIET_WEISS ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                            == Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                            == Konstante.SCHNITTPUNKT_WEISS_GEFANGEN){
                               gebietMarkieren = false;
                           }
                       }

                       /* 3. Untere Seite */
                       if(listeSteine.get(momElement).getYPos()!=0){
                           if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_LEER &&
                              this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getMarkiert()
                                   == false){
                               /* Stein aufnehmen */
                               listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1]);
                               this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].setMarkiert(true);
                           }
                           /* Ist nachbarstein lebendig */
                           else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_SCHWARZ ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_WEISS){
                                   switch(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()){
                                       case Konstante.SCHNITTPUNKT_SCHWARZ :
                                           switch(farbe){
                                               case -1:
                                                   farbe = Konstante.SCHNITTPUNKT_SCHWARZ;
                                                   break;
                                               case Konstante.SCHNITTPUNKT_SCHWARZ :
                                                   /* nichts */
                                                   break;
                                               case Konstante.SCHNITTPUNKT_WEISS :
                                                   gebietMarkieren = false;
                                                   break;
                                               default: /* Darf nicht passieren */
                                                   throw new UnsupportedOperationException("Variable farbe enthaelt unerwartete Werte -> " + farbe);
                                           }
                                           break;
                                       case Konstante.SCHNITTPUNKT_WEISS :
                                           switch(farbe){
                                               case -1:
                                                   farbe = Konstante.SCHNITTPUNKT_WEISS;
                                                   break;
                                               case Konstante.SCHNITTPUNKT_SCHWARZ :
                                                   gebietMarkieren = false;
                                                   break;
                                               case Konstante.SCHNITTPUNKT_WEISS :
                                                   /* nichts */
                                                   break;
                                               default: /* Darf nicht passieren */
                                                   throw new UnsupportedOperationException("Variable farbe enthaelt unerwartete Werte -> " + farbe);
                                           }
                                           break;
                                       default : /* Darf nicht passieren */
                                           throw new UnsupportedOperationException("Fehler in switch case, eigentlich muesste hier SCHNITTPUNKT_SCHWARZ oder SCHNITTPUNKT_WEISS stehen");
                                   }
                           }

                           /* Komische Werte abfangen */
                           else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                                            == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                                            == Konstante.SCHNITTPUNKT_GEBIET_WEISS ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                                            == Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                                            == Konstante.SCHNITTPUNKT_WEISS_GEFANGEN){
                               gebietMarkieren = false;
                           }
                       }

                       /* 4. Obere Seite */
                       if(listeSteine.get(momElement).getYPos()!=0){
                           if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_LEER &&
                              this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getMarkiert()
                                   == false){
                               /* Stein aufnehmen */
                               listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1]);
                               this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].setMarkiert(true);
                           }
                           /* Ist nachbarstein lebendig */
                           else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_SCHWARZ ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_WEISS){
                                   switch(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()){
                                       case Konstante.SCHNITTPUNKT_SCHWARZ :
                                           switch(farbe){
                                               case -1:
                                                   farbe = Konstante.SCHNITTPUNKT_SCHWARZ;
                                                   break;
                                               case Konstante.SCHNITTPUNKT_SCHWARZ :
                                                   /* nichts */
                                                   break;
                                               case Konstante.SCHNITTPUNKT_WEISS :
                                                   gebietMarkieren = false;
                                                   break;
                                               default: /* Darf nicht passieren */
                                                   throw new UnsupportedOperationException("Variable farbe enthaelt unerwartete Werte -> " + farbe);
                                           }
                                           break;
                                       case Konstante.SCHNITTPUNKT_WEISS :
                                           switch(farbe){
                                               case -1:
                                                   farbe = Konstante.SCHNITTPUNKT_WEISS;
                                                   break;
                                               case Konstante.SCHNITTPUNKT_SCHWARZ :
                                                   gebietMarkieren = false;
                                                   break;
                                               case Konstante.SCHNITTPUNKT_WEISS :
                                                   /* nichts */
                                                   break;
                                               default: /* Darf nicht passieren */
                                                   throw new UnsupportedOperationException("Variable farbe enthaelt unerwartete Werte -> " + farbe);
                                           }
                                           break;
                                       default : /* Darf nicht passieren */
                                           throw new UnsupportedOperationException("Fehler in switch case, eigentlich muesste hier SCHNITTPUNKT_SCHWARZ oder SCHNITTPUNKT_WEISS stehen");
                                   }
                           }

                           /* Komische Werte abfangen */
                           else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                                            == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                                            == Konstante.SCHNITTPUNKT_GEBIET_WEISS ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                                            == Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                                            == Konstante.SCHNITTPUNKT_WEISS_GEFANGEN){
                               throw new UnsupportedOperationException("Bei suche von reinen Gebieten ging man von unmarkierten Schnittpunkt aus und hat schon Markierte Steine / Gebiete gefunden -> Fehler");
                           }
                       }
                       /* Alle Seiten untersucht. Nun zum naechsten Stein */
                       listeSteine.get(momElement).setAnalysiert(true);
                       momElement++;
                   }while(momElement<listeSteine.size());
                   /* Jetzt sind alle Steine aufgenommen worden. In abhaengigkeit
                    * von gebietMarkieren wird fortgefahren. Wenn man das gebiet
                    * Markieren soll, muss noch nach der farbe gefragt werden.
                    * Diese darf dabei nicht undefiniert sein. (leeres Brett)*/
                   if(gebietMarkieren == true && farbe != -1){
                       int markierfarbe;
                       if(farbe == Konstante.SCHNITTPUNKT_SCHWARZ) {
                           markierfarbe = Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ;
                       }
                       else if(farbe == Konstante.SCHNITTPUNKT_WEISS) {
                           markierfarbe = Konstante.SCHNITTPUNKT_GEBIET_WEISS;
                       }
                       else {
                           /* Wenn unerwartet, einfach als Leer markieren! */
                           markierfarbe = Konstante.SCHNITTPUNKT_LEER;
                       }
                       for(int k=listeSteine.size()-1; k>=0; k--){
                           this.auswertungBrett[listeSteine.get(k).getXPos()][listeSteine.get(k).getYPos()].setBelegungswert(markierfarbe);
                           listeSteine.remove(k);
                       }
                   }
                   else {
                       for(int k=listeSteine.size()-1; k>=0; k--){
                           this.auswertungBrett[listeSteine.get(k).getXPos()][listeSteine.get(k).getYPos()].setBelegungswert(Konstante.SCHNITTPUNKT_LEER);
                           listeSteine.remove(k);
                       }
                   }
                }
            }
        }
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