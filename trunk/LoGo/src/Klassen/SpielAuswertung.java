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
     *
     * @param xPos
     * @param yPos
     * @return Integer, der angibt, ob die funktion ausgeführt werden konnte
     * 1 -> Ok
     * -1 -> Fehler, Koordinate nicht auf Brett
     */
    public int markiereStein(int xPos, int yPos){
        if(xPos>=1 && xPos<=this.getFeldGroesse() && yPos>=1 && yPos<=this.getFeldGroesse()){
            int xKoord = xPos-1;
            int yKoord = yPos-1;
            switch(this.auswertungBrett[xKoord][yKoord].getBelegungswert()){
                case Konstante.SCHNITTPUNKT_SCHWARZ:
                case Konstante.SCHNITTPUNKT_WEISS:
                    for(int i=0; i<this.getFeldGroesse(); i++){
                        for(int j=0; j<this.getFeldGroesse(); j++){
                            this.auswertungBrett[i][j].setAnalysiert(false);
                            this.auswertungBrett[i][j].setMarkiert(false);
                        }
                    }
                    return this.markiereSteinAlsGefangen(xKoord, yKoord);
                case Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN:
                case Konstante.SCHNITTPUNKT_WEISS_GEFANGEN:
                    for(int i=0; i<this.getFeldGroesse(); i++){
                        for(int j=0; j<this.getFeldGroesse(); j++){
                            this.auswertungBrett[i][j].setAnalysiert(false);
                            this.auswertungBrett[i][j].setMarkiert(false);
                        }
                    }
                    return this.markiereSteinAlsNichtGefangen(xKoord, yKoord);
                case Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ:
                case Konstante.SCHNITTPUNKT_GEBIET_WEISS:
                case Konstante.SCHNITTPUNKT_LEER:
                    return -1;
                default: /* Darf nicht passieren */
                    throw new UnsupportedOperationException("Das angeklickte Feld hat einen unerwarteten Wert");
            }
        }
        else {
            return -1;
        }
    }

    /**
     * Damit man das Feld auswerten kann, muss der Benutzer signalisieren, welche
     * Steine auf dem Brett tot sind.
     * Erstmal als einfache Variante. Rekursiv lohnt sich aber vielleicht doch.
     * @param xPos X-Koordinate (von 0 bis Feldgroesse-1)
     * @param yPos Y-Koordinate (von 0 bis Feldgroesse-1)
     * @return Integer signalisiert wie funktion ausgegangen ist:
     * 1  : Brett wurde erfolgreich veraendert
     * 1  : Eingabe war Ok, aber Stein war schon gefangen: daher keine Aenderung
     * -2 : Eingabe war falsch: Koordinaten nicht auf dem Brett!
     * -3 : Koordinate auf die geklickt wurde ist nicht mit einem Stein belegt!
     */
    private int markiereSteinAlsGefangen(int xPos, int yPos){
        /* Bevor die Suche beginnt sind alle Schnittpunkte demarkiert
         * Die Farbe des Steines der angeklickt wurde muss markiert werden,
         * damit man spaeter die Gebiete markieren kann*/
        int farbe = this.auswertungBrett[xPos][yPos].getBelegungswert();
        int farbeGefangen = -1;
        int gegenfarbe = -1;
        int gegenfarbeGefangen = -1;
        int gebietMarkierung = -1;
        switch(farbe){
            case Konstante.SCHNITTPUNKT_SCHWARZ:
                farbeGefangen = Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN;
                gegenfarbe = Konstante.SCHNITTPUNKT_WEISS;
                gegenfarbeGefangen = Konstante.SCHNITTPUNKT_WEISS_GEFANGEN;
                gebietMarkierung = Konstante.SCHNITTPUNKT_GEBIET_WEISS;
                break;
            case Konstante.SCHNITTPUNKT_WEISS:
                farbeGefangen = Konstante.SCHNITTPUNKT_WEISS_GEFANGEN;
                gegenfarbe = Konstante.SCHNITTPUNKT_SCHWARZ;
                gegenfarbeGefangen = Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN;
                gebietMarkierung = Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ;
                break;
            default: /* darf nicht passieren */
                throw new UnsupportedOperationException("Farbe des Steins falsch definiert");
        }

        /* Die Eingabe ist auf jeden Fall ein Stein, der entweder schwarz
         * oder weiss ist. Er lebt auf jeden fall. Daher wird der Stein
         * in eine Liste aufgenommen und nach weiteren Steinen gesucht.*/
        List<AnalyseSchnittpunkt> listeSteine = new ArrayList<AnalyseSchnittpunkt>();
        int momElement = 0;
        listeSteine.add(this.auswertungBrett[xPos][yPos]);
        this.auswertungBrett[xPos][yPos].setMarkiert(true);

        /* Solange die Liste noch nicht vollstaendig durchsucht ist, betrachte
         * die nachbarn, wenn diese existieren */
        do{
            /* Nun werden die Nachbarsteine betrachtet. Sind die Steine nicht
             * besetzt (leer, gefangen_schwarz, gefangen_weiss) so werden sie
             * einfach aufgenommen.
             * Sind die Steine von der Gegenfarbe und Lebendig, so wird nichts
             * gemacht. Sind sie von der Gegenfarbe und tot, so werden sie
             * "lebendig markiert", also mit der markiereSteinAlsNicht Gefangen
             * Sind die Steine von der gleichen Farbe und Lebendig, so werden
             * sie als Tot gesetzt, sind sie schon tot, wird nichts gemacht.*/

            /* 1. Linke Seite */
            if( listeSteine.get(momElement).getXPos() != 0){
                if((this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_LEER ||
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ ||
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == farbe ||
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_WEISS) &&
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getMarkiert() == false){
                    listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()]);
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].setMarkiert(true);
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == gegenfarbe){
                    /* nichts */
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == farbeGefangen){
                    /* nichts */
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == gegenfarbeGefangen){
                    this.markiereSteinAlsNichtGefangen(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getXPos(),
                                                       this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getYPos());
                }
                else {
                    throw new UnsupportedOperationException("Farbe des Steins falsch definiert");
                }

            }

            /* 2. Rechte Seite */
            if( listeSteine.get(momElement).getXPos() != this.getFeldGroesse()-1){
                if((this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_LEER ||
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ ||
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == farbe ||
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_WEISS) &&
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getMarkiert() == false){
                    listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()]);
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].setMarkiert(true);
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == gegenfarbe){
                    /* nichts */
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == farbeGefangen){
                    /* nichts */
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == gegenfarbeGefangen){
                    this.markiereSteinAlsNichtGefangen(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getXPos(),
                                                       this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getYPos());
                }
                else {
                    throw new UnsupportedOperationException("Farbe des Steins falsch definiert");
                }

            }

            /* 3. Untere Seite */
            if( listeSteine.get(momElement).getYPos() != 0){
                if((this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_LEER ||
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ ||
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                        == farbe ||
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_WEISS) &&
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getMarkiert()
                        == false){
                    listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1]);
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].setMarkiert(true);
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                        == gegenfarbe){
                    /* nichts */
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                        == farbeGefangen){
                    /* nichts */
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                        == gegenfarbeGefangen){
                    this.markiereSteinAlsNichtGefangen(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getXPos(),
                                                       this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getYPos());
                }
                else {
                    throw new UnsupportedOperationException("Farbe des Steins falsch definiert");
                }

            }

            /* 4. Obere Seite */
            if( listeSteine.get(momElement).getYPos() != this.getFeldGroesse()-1){
                if((this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_LEER ||
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ ||
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                        == farbe ||
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_WEISS) &&
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getMarkiert() == false){
                    listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1]);
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].setMarkiert(true);
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                        == gegenfarbe){
                    /* nichts */
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                        == farbeGefangen){
                    /* nichts */
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                        == gegenfarbeGefangen){
                    this.markiereSteinAlsNichtGefangen(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getXPos(),
                                                       this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getYPos());
                }
                else {
                    throw new UnsupportedOperationException("Farbe des Steins falsch definiert");
                }

            }
            momElement++;
            this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()].setAnalysiert(true);
        }while(momElement<listeSteine.size());
        /* Jetzt sind nur Leere Steine, oder steine der Angeklickten farbe in
         * der Liste. Diese werden jetzt ummarkiert */

        for(int k=listeSteine.size()-1; k>=0; k--){
            if(listeSteine.get(k).getBelegungswert() != farbe){
                this.auswertungBrett[listeSteine.get(k).getXPos()][listeSteine.get(k).getYPos()].setBelegungswert(gebietMarkierung);
            }
            else {
                this.auswertungBrett[listeSteine.get(k).getXPos()][listeSteine.get(k).getYPos()].setBelegungswert(farbeGefangen);
            }
            listeSteine.remove(k);
        }
        return 1;
    }

    /**
     * Moechte der Benutzer eine Markierung rueckgaengig machen, kann er das
     * mit dieser Funktion. Es ist Aufgabe der Steuerung, ob der Benutzer
     * einen Stein tot oder untot markieren will.
     * @param xPos X-Koordinate
     * @param yPos Y-Koordinate
     */
    private int markiereSteinAlsNichtGefangen(int xPos, int yPos){
        /* Der Stein ist auf jeden Fall ein toter Stein auf dem Brett
         * Nun muessen einige variablen gesetzt werden */
        int farbe = -1;
        int gegenfarbe = -1;
        int farbeGefangen = -1;
        int gegenfarbeGefangen = -1;
        switch (this.auswertungBrett[xPos][yPos].getBelegungswert()){
            case Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN:
                farbe = Konstante.SCHNITTPUNKT_SCHWARZ;
                gegenfarbe = Konstante.SCHNITTPUNKT_WEISS;
                farbeGefangen = Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN;
                gegenfarbeGefangen = Konstante.SCHNITTPUNKT_WEISS_GEFANGEN;
                break;
            case Konstante.SCHNITTPUNKT_WEISS_GEFANGEN:
                farbe = Konstante.SCHNITTPUNKT_WEISS;
                gegenfarbe = Konstante.SCHNITTPUNKT_SCHWARZ;
                farbeGefangen = Konstante.SCHNITTPUNKT_WEISS_GEFANGEN;
                gegenfarbeGefangen = Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN;
                break;
            default: /* darf nicht passieren */
                 throw new UnsupportedOperationException("Farbe des Steins falsch definiert");
        }
        List<AnalyseSchnittpunkt> listeSteine = new ArrayList<AnalyseSchnittpunkt>();
        int momElement = 0;

        /* Der erste Stein wird auf jeden fall aufgenommen */
        listeSteine.add(this.auswertungBrett[xPos][yPos]);
        this.auswertungBrett[xPos][yPos].setMarkiert(true);

        /* Solange die Liste noch nicht durchsucht ist: Weitersuchen */
        do{
            /* Nun werden die Nachbarn untersucht. Sind diese von der gleichen
             * Farbe und gefangen, so werden sie aufgenommen. Sind sie schon
             * lebendig, werden sie ignoriert.
             * Ist der nachbar leer oder gebietsmarkiert, wird er aufgenommen
             * Ist der Nachbar von der Anderen Farbe,  wird nichts gemacht
             */

            /* 1. Linker Nachbar */
            if(listeSteine.get(momElement).getXPos()!=0){
                if( (this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == farbeGefangen ||
                     this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ ||
                     this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_WEISS ||
                     this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_LEER) &&
                     this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getMarkiert()
                        == false){
                    listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()]);
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].setMarkiert(true);
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == farbe ||
                        this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == gegenfarbe ||
                        this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == gegenfarbeGefangen){
                    /* nichts */
                }
            }

            /* 2. Rechte Seite */
            if(listeSteine.get(momElement).getXPos()!=this.getFeldGroesse()-1){
                if( (this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == farbeGefangen ||
                     this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ ||
                     this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_WEISS ||
                     this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_LEER) &&
                     this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getMarkiert()
                        == false){
                    listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()]);
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].setMarkiert(true);
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == farbe ||
                        this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == gegenfarbe ||
                        this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == gegenfarbeGefangen){
                    /* nichts */
                }
            }

            /* 3. Untere Seite */
            if(listeSteine.get(momElement).getYPos()!=0){
                if( (this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                        == farbeGefangen ||
                     this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ ||
                     this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_WEISS ||
                     this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_LEER) &&
                     this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getMarkiert()
                        == false){
                    listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1]);
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].setMarkiert(true);
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                        == farbe ||
                        this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                        == gegenfarbe ||
                        this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                        == gegenfarbeGefangen){
                    /* nichts */
                }
            }

            /* 4. Obere Seite */
            if(listeSteine.get(momElement).getYPos()!=this.getFeldGroesse()-1){
                if( (this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                        == farbeGefangen ||
                     this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ ||
                     this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_WEISS ||
                     this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_LEER) &&
                     this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getMarkiert()
                        == false){
                    listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1]);
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].setMarkiert(true);
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                        == farbe ||
                        this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                        == gegenfarbe ||
                        this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                        == gegenfarbeGefangen){
                    /* nichts */
                }
            }
            this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()].setAnalysiert(true);
            momElement++;
        }while(momElement<listeSteine.size());
        /* Alle Felder sind durchsucht */
        for(int k=listeSteine.size()-1; k>=0; k--)
        {
            if(listeSteine.get(k).getBelegungswert() != farbeGefangen){
                this.auswertungBrett[listeSteine.get(k).getXPos()][listeSteine.get(k).getYPos()].setBelegungswert(farbe);
            }
            else {
                this.auswertungBrett[listeSteine.get(k).getXPos()][listeSteine.get(k).getYPos()].setBelegungswert(Konstante.SCHNITTPUNKT_LEER);
            }
            listeSteine.remove(k);
        }
        return 1;
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
                 * beginnen.
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
                           /* Ist nachbarstein lebendig oder tot */
                           else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_SCHWARZ ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_WEISS ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_WEISS_GEFANGEN){
                                   switch(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()){
                                       case Konstante.SCHNITTPUNKT_SCHWARZ :
                                       case Konstante.SCHNITTPUNKT_WEISS_GEFANGEN:
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
                                       case Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN:
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
                                            == Konstante.SCHNITTPUNKT_GEBIET_WEISS){
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

                           /* Ist nachbarstein lebendig oder tot */
                           else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_SCHWARZ ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_WEISS ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_WEISS_GEFANGEN){
                                   switch(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()){
                                       case Konstante.SCHNITTPUNKT_SCHWARZ :
                                       case Konstante.SCHNITTPUNKT_WEISS_GEFANGEN:
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
                                       case Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN:
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
                                            == Konstante.SCHNITTPUNKT_GEBIET_WEISS){
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
                           
                           /* Ist nachbarstein lebendig oder tot */
                           else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_SCHWARZ ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_WEISS ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_WEISS_GEFANGEN){
                                   switch(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()){
                                       case Konstante.SCHNITTPUNKT_SCHWARZ :
                                       case Konstante.SCHNITTPUNKT_WEISS_GEFANGEN:
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
                                       case Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN:
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
                                            == Konstante.SCHNITTPUNKT_GEBIET_WEISS){
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

                           /* Ist nachbarstein lebendig oder tot */
                           else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_SCHWARZ ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_WEISS ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN ||
                                   this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                                   == Konstante.SCHNITTPUNKT_WEISS_GEFANGEN){
                                   switch(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()){
                                       case Konstante.SCHNITTPUNKT_SCHWARZ :
                                       case Konstante.SCHNITTPUNKT_WEISS_GEFANGEN:
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
                                       case Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN:
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
                                            == Konstante.SCHNITTPUNKT_GEBIET_WEISS){
                               gebietMarkieren = false;
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

}