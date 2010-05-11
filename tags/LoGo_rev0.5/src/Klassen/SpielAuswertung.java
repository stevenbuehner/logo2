package Klassen;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse uebernimmt die Auswertung eines Spielfeldes
 * Dabei werden Gebietspunkte, Gefangenenpunkte und Komi beruecksichtigt
 * @author tommy
 */
public class SpielAuswertung {

    private float komiFuerWeiss;
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
    public SpielAuswertung(int brettGroesse, float komiFuerWeiss) {
        this.brettGroesse = brettGroesse;
        this.komiFuerWeiss = komiFuerWeiss;
        this.setGefangeneVonSchwarz(0);
        this.setGefangeneVonWeiss(0);
        this.auswertungBrett = new AnalyseSchnittpunkt[this.getFeldGroesse()][this.getFeldGroesse()];
        for (int i = 0; i < this.getFeldGroesse(); i++) {
            for (int j = 0; j < this.getFeldGroesse(); j++) {
                this.auswertungBrett[i][j] = new AnalyseSchnittpunkt(i, j, Konstante.SCHNITTPUNKT_LEER);
            }
        }
        this.eingegebenesFeld = new int[this.getFeldGroesse()][this.getFeldGroesse()];
        for (int i = 0; i < this.getFeldGroesse(); i++) {
            for (int j = 0; j < this.getFeldGroesse(); j++) {
                this.eingegebenesFeld[i][j] = Konstante.SCHNITTPUNKT_LEER;
            }
        }
    }

    /**
     * Vereinfachung des Standartkonstruktors. Man muss nur die Brettgroesse
     * angeben. Das Komi wird auf 0 gestetzt
     * @param brettGroesse Brettgroesse des Spielfeldes
     * @see SpielAuswertung
     */
    public SpielAuswertung(int brettGroesse) {
        this(brettGroesse, 0);
    }

    /**
     * Legt die Zahl der Steine fest, die der schwarze Spieler gefangen hat.
     * @param gefangeneVonSchwarz Anzahl der Steine
     */
    public void setGefangeneVonSchwarz(int gefangeneVonSchwarz) {
        this.gefangeneVonSchwarz = gefangeneVonSchwarz;
    }

    /**
     * Gibt die Anzahl der Steine wieder, die als Gefangenenanzahl fuer Schwarz
     * eingestellt wurden.
     * @return Anzahl der Steine
     */
    public int getGefangeneVonSchwarz() {
        return this.gefangeneVonSchwarz;
    }

    /**
     * Legt die Zahl der Steine fest, die der weisse Spieler gefangen hat.
     * @param gefangeneVonWeiss Anzahl der Steine
     */
    public void setGefangeneVonWeiss(int gefangeneVonWeiss) {
        this.gefangeneVonWeiss = gefangeneVonWeiss;
    }

    /**
     * Gibt die Anzahl der Steine wieder, die als Gefangenenanzahl fuer Schwarz
     * eingestellt wurden.
     * @return Anzahl der Steine
     */
    public int getGefangeneVonWeiss() {
        return this.gefangeneVonWeiss;
    }

    /**
     * Fuer interne Zwecke. Gibt die eingestellte Feldgroesse wieder.
     * @return Feldgroesse  (auswertungBrett)
     */
    private int getFeldGroesse() {
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
    public void auswertungInitialisieren(int feld[][]) {
        for (int i = 0; i < this.getFeldGroesse(); i++) {
            for (int j = 0; j < this.getFeldGroesse(); j++) {
                switch (feld[i][j]) {
                    case Konstante.SCHNITTPUNKT_SCHWARZ:
                        this.eingegebenesFeld[i][j] = Konstante.SCHNITTPUNKT_SCHWARZ;
                        this.auswertungBrett[i][j].setBelegungswert(Konstante.SCHNITTPUNKT_SCHWARZ);
                        break;
                    case Konstante.SCHNITTPUNKT_WEISS:
                        this.eingegebenesFeld[i][j] = Konstante.SCHNITTPUNKT_WEISS;
                        this.auswertungBrett[i][j].setBelegungswert(Konstante.SCHNITTPUNKT_WEISS);
                        break;
                    case Konstante.SCHNITTPUNKT_LEER:
                    case Konstante.SCHNITTPUNKT_VERBOTEN:
                        this.eingegebenesFeld[i][j] = Konstante.SCHNITTPUNKT_LEER;
                        this.auswertungBrett[i][j].setBelegungswert(Konstante.SCHNITTPUNKT_LEER);
                        break;
                    default: /* Hierer darf man nicht kommen */
                        throw new UnsupportedOperationException("Das eingegebene Feld enthaelt unerwartete Werte an Stelle (" + i + "|" + j + ")");
                }
            }
        }
        this.findeGebiete();
        this.suchePseudoPunkte();
    }

    /**
     *
     * @param xPos
     * @param yPos
     * @return Integer, der angibt, ob die funktion ausgeführt werden konnte
     * 1 -> Ok
     * -1 -> Fehler, Koordinate nicht auf Brett
     */
    public int markiereStein(int xPos, int yPos) {
        int rueckgabe = 0;
        if (xPos >= 1 && xPos <= this.getFeldGroesse() && yPos >= 1 && yPos <= this.getFeldGroesse()) {
            int xKoord = xPos - 1;
            int yKoord = yPos - 1;
            switch (this.auswertungBrett[xKoord][yKoord].getBelegungswert()) {
                case Konstante.SCHNITTPUNKT_SCHWARZ:
                case Konstante.SCHNITTPUNKT_WEISS:
                    for (int i = 0; i < this.getFeldGroesse(); i++) {
                        for (int j = 0; j < this.getFeldGroesse(); j++) {
                            this.auswertungBrett[i][j].setAnalysiert(false);
                            this.auswertungBrett[i][j].setMarkiert(false);
                        }
                    }
                    rueckgabe = this.markiereSteinAlsGefangen(xKoord, yKoord);
                    break;
                case Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN:
                case Konstante.SCHNITTPUNKT_WEISS_GEFANGEN:
                    for (int i = 0; i < this.getFeldGroesse(); i++) {
                        for (int j = 0; j < this.getFeldGroesse(); j++) {
                            this.auswertungBrett[i][j].setAnalysiert(false);
                            this.auswertungBrett[i][j].setMarkiert(false);
                        }
                    }
                    rueckgabe = this.markiereSteinAlsNichtGefangen(xKoord, yKoord);
                    break;
                case Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ:
                case Konstante.SCHNITTPUNKT_GEBIET_WEISS:
                case Konstante.SCHNITTPUNKT_LEER:
                    return -1;
                default: /* Darf nicht passieren */
                    throw new UnsupportedOperationException("Das angeklickte Feld hat einen unerwarteten Wert");
            }
        } else {
            return -1;
        }
        this.findeGebiete();
        this.suchePseudoPunkte();
        return rueckgabe;
    }

    /**
     * Damit man das Feld auswerten kann, muss der Benutzer signalisieren, welche
     * Steine auf dem Brett tot sind.
     * Das Resultat dieser Funktion ist, dass die Lebenden Steine die angeklickt
     * werden also Tot eingestellt werden. Tote Steine des Gegners werden dabei
     * wieder lebendig gemacht. Die suche endet beim Brettende oder bei lebendigen
     * Steinen der Gegenfarbe.
     * @param xPos X-Koordinate (von 0 bis Feldgroesse-1)
     * @param yPos Y-Koordinate (von 0 bis Feldgroesse-1)
     * @return Integer signalisiert wie funktion ausgegangen ist:
     * 1  : Brett wurde erfolgreich veraendert
     * 1  : Eingabe war Ok, aber Stein war schon gefangen: daher keine Aenderung
     * -2 : Eingabe war falsch: Koordinaten nicht auf dem Brett!
     * -3 : Koordinate auf die geklickt wurde ist nicht mit einem Stein belegt!
     */
    private int markiereSteinAlsGefangen(int xPos, int yPos) {
        /* Die Farbe des Steines der angeklickt wurde muss markiert werden,
         * damit man spaeter die Gebiete markieren kann*/
        int farbe = this.auswertungBrett[xPos][yPos].getBelegungswert();
        int farbeGefangen = -1;
        int gegenfarbe = -1;
        int gegenfarbeGefangen = -1;
        int gebietMarkierung = -1;
        switch (farbe) {
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
        do {
            /* Nun werden die Nachbarsteine betrachtet. Sind die Steine nicht
             * besetzt (leer, gefangen_schwarz, gefangen_weiss) so werden sie
             * einfach aufgenommen.
             * Sind die Steine von der Gegenfarbe und Lebendig, so wird nichts
             * gemacht. Sind sie von der Gegenfarbe und tot, so werden sie
             * "lebendig markiert", also mit der markiereSteinAlsNicht Gefangen
             * Sind die Steine von der gleichen Farbe und Lebendig, so werden
             * sie als Tot gesetzt, sind sie schon tot, wird nichts gemacht.*/

            /* 1. Linke Seite */
            if (listeSteine.get(momElement).getXPos() != 0) {
                if ((this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_LEER
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == farbe
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_WEISS)
                        && this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getMarkiert()
                        == false) {
                    listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()]);
                    this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].setMarkiert(true);
                } else if (this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getMarkiert()
                        == true) {
                    /* Dann wurde der Stein schon aufgenommen -- nichts passiert */
                } else if (this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == gegenfarbe) {
                    /* nichts */
                } else if (this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == farbeGefangen) {
                    /* nichts */
                } else if (this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == gegenfarbeGefangen) {
                    this.markiereSteinAlsNichtGefangen(this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getXPos(),
                            this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getYPos());
                } else {
                    throw new UnsupportedOperationException("Farbe des Steins falsch definiert");
                }

            }

            /* 2. Rechte Seite */
            if (listeSteine.get(momElement).getXPos() != this.getFeldGroesse() - 1) {
                if ((this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_LEER
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == farbe
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_WEISS)
                        && this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getMarkiert()
                        == false) {
                    listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()]);
                    this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].setMarkiert(true);
                } else if (this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getMarkiert()
                        == true) {
                    /* Dann wurde der Stein schon aufgenommen -- nichts passiert */
                } else if (this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == gegenfarbe) {
                    /* nichts */
                } else if (this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == farbeGefangen) {
                    /* nichts */
                } else if (this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == gegenfarbeGefangen) {
                    this.markiereSteinAlsNichtGefangen(this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getXPos(),
                            this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getYPos());
                } else {
                    throw new UnsupportedOperationException("Farbe des Steins falsch definiert");
                }

            }

            /* 3. Untere Seite */
            if (listeSteine.get(momElement).getYPos() != 0) {
                if ((this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_LEER
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getBelegungswert()
                        == farbe
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_WEISS)
                        && this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getMarkiert()
                        == false) {
                    listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1]);
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].setMarkiert(true);
                } else if (this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getMarkiert()
                        == true) {
                    /* Dann wurde der Stein schon aufgenommen -- nichts passiert */
                } else if (this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getBelegungswert()
                        == gegenfarbe) {
                    /* nichts */
                } else if (this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getBelegungswert()
                        == farbeGefangen) {
                    /* nichts */
                } else if (this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getBelegungswert()
                        == gegenfarbeGefangen) {
                    this.markiereSteinAlsNichtGefangen(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getXPos(),
                            this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getYPos());
                } else {
                    throw new UnsupportedOperationException("Farbe des Steins falsch definiert");
                }

            }

            /* 4. Untere Seite */
            if (listeSteine.get(momElement).getYPos() != this.getFeldGroesse() - 1) {
                if ((this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_LEER
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getBelegungswert()
                        == farbe
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_WEISS)
                        && this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getMarkiert()
                        == false) {
                    listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1]);
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].setMarkiert(true);
                } else if (this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getMarkiert()
                        == true) {
                    /* Dann wurde der Stein schon aufgenommen -- nichts passiert */
                } else if (this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getBelegungswert()
                        == gegenfarbe) {
                    /* nichts */
                } else if (this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getBelegungswert()
                        == farbeGefangen) {
                    /* nichts */
                } else if (this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getBelegungswert()
                        == gegenfarbeGefangen) {
                    this.markiereSteinAlsNichtGefangen(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getXPos(),
                            this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getYPos());
                } else {
                    throw new UnsupportedOperationException("Farbe des Steins falsch definiert");
                }

            }


            momElement++;
        } while (momElement < listeSteine.size());
        /* Jetzt sind nur Leere Steine, oder steine der Angeklickten farbe in
         * der Liste. Diese werden jetzt ummarkiert.
         * Sind die Steine lebendig, so werden sie als tot markiert.
         * Sind die Schnittpunkte nicht belegt (gebiet, oder leer) so werden
         * sie als leer markiert */

        for (int k = listeSteine.size() - 1; k >= 0; k--) {
            if (listeSteine.get(k).getBelegungswert() != farbe) {
                this.auswertungBrett[listeSteine.get(k).getXPos()][listeSteine.get(k).getYPos()].setBelegungswert(Konstante.SCHNITTPUNKT_LEER);
            } else {
                this.auswertungBrett[listeSteine.get(k).getXPos()][listeSteine.get(k).getYPos()].setBelegungswert(farbeGefangen);
            }
            listeSteine.remove(k);
        }
        return 1;
    }

    /**
     * Moechte der Benutzer eine Markierung rueckgaengig machen, kann er das
     * mit dieser Funktion. Es werden Steine und unbesetzte Schnittpunkte gesammelt.
     * Trifft man auf einen Stein der Gegenfarbe, so wird dort nicht weiter gesucht
     * Gebiet wird demarkiert, also als leer gekennzeichnet.
     * @param xPos X-Koordinate ( 0 bis Brettgroesse-1 )
     * @param yPos Y-Koordinate ( 0 bis Brettgroesse-1 )
     */
    private int markiereSteinAlsNichtGefangen(int xPos, int yPos) {
        /* Der Stein ist auf jeden Fall ein toter Stein auf dem Brett
         * Nun muessen einige variablen gesetzt werden */
        int farbe = -1;
        int gegenfarbe = -1;
        int farbeGefangen = -1;
        int gegenfarbeGefangen = -1;
        switch (this.auswertungBrett[xPos][yPos].getBelegungswert()) {
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
                throw new UnsupportedOperationException("Farbe des Steins (gefangen) falsch definiert");
        }
        List<AnalyseSchnittpunkt> listeSteine = new ArrayList<AnalyseSchnittpunkt>();
        int momElement = 0;

        /* Der erste Stein wird auf jeden fall aufgenommen */
        listeSteine.add(this.auswertungBrett[xPos][yPos]);
        this.auswertungBrett[xPos][yPos].setMarkiert(true);

        /* Solange die Liste noch nicht durchsucht ist: Weitersuchen */
        do {
            /* Nun werden die Nachbarn untersucht. Sind diese von der gleichen
             * Farbe und gefangen, so werden sie aufgenommen. Sind sie schon
             * lebendig, werden sie ignoriert.
             * Ist der nachbar leer oder gebietsmarkiert, wird er aufgenommen
             * Ist der Nachbar von der Anderen Farbe,  wird nichts gemacht
             */

            /* 1. Linker Nachbar */
            if (listeSteine.get(momElement).getXPos() != 0) {
                if ((this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == farbeGefangen
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_WEISS
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_LEER)
                        && this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getMarkiert()
                        == false) {
                    listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()]);
                    this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].setMarkiert(true);
                } else if (this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == farbe
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == gegenfarbe
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == gegenfarbeGefangen
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getMarkiert()
                        == true) {
                    /* nichts */
                }
            }

            /* 2. Rechte Seite */
            if (listeSteine.get(momElement).getXPos() != this.getFeldGroesse() - 1) {
                if ((this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == farbeGefangen
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_WEISS
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_LEER)
                        && this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getMarkiert()
                        == false) {
                    listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()]);
                    this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].setMarkiert(true);
                } else if (this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == farbe
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == gegenfarbe
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == gegenfarbeGefangen
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getMarkiert()
                        == true) {
                    /* nichts */
                }
            }

            /* 3. Untere Seite */
            if (listeSteine.get(momElement).getYPos() != 0) {
                if ((this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getBelegungswert()
                        == farbeGefangen
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_WEISS
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_LEER)
                        && this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getMarkiert()
                        == false) {
                    listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1]);
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].setMarkiert(true);
                } else if (this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getBelegungswert()
                        == farbe
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getBelegungswert()
                        == gegenfarbe
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getBelegungswert()
                        == gegenfarbeGefangen
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getMarkiert()
                        == true) {
                    /* nichts */
                }
            }

            /* 4. Obere Seite */
            if (listeSteine.get(momElement).getYPos() != this.getFeldGroesse() - 1) {
                if ((this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getBelegungswert()
                        == farbeGefangen
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_GEBIET_WEISS
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_LEER)
                        && this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getMarkiert()
                        == false) {
                    listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1]);
                    this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].setMarkiert(true);
                } else if (this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getBelegungswert()
                        == farbe
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getBelegungswert()
                        == gegenfarbe
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getBelegungswert()
                        == gegenfarbeGefangen
                        || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getMarkiert()
                        == true) {
                    /* nichts */
                }
            }
            momElement++;
        } while (momElement < listeSteine.size());
        /* Alle Felder sind durchsucht. In der Liste sind nun Tote Steine der
         * angeklickten Farbe, oder unbesetzte Schnittpunkte. Gebietsmarkierungen
         * werden rueckgaengig gemacht und die toten Steine werden als lebendig
         * markiert.
         */
        for (int k = listeSteine.size() - 1; k >= 0; k--) {
            if (listeSteine.get(k).getBelegungswert() != farbeGefangen) {
                this.auswertungBrett[listeSteine.get(k).getXPos()][listeSteine.get(k).getYPos()].setBelegungswert(Konstante.SCHNITTPUNKT_LEER);
                this.auswertungBrett[listeSteine.get(k).getXPos()][listeSteine.get(k).getYPos()].setMarkiert(false);
                this.auswertungBrett[listeSteine.get(k).getXPos()][listeSteine.get(k).getYPos()].setAnalysiert(false);
            } else {
                this.auswertungBrett[listeSteine.get(k).getXPos()][listeSteine.get(k).getYPos()].setBelegungswert(farbe);
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
    public void initialisiereNeu() {
        for (int i = 0; i < this.getFeldGroesse(); i++) {
            for (int j = 0; j < this.getFeldGroesse(); j++) {
                switch (this.eingegebenesFeld[i][j]) {
                    case Konstante.SCHNITTPUNKT_SCHWARZ:
                        this.auswertungBrett[i][j].setBelegungswert(Konstante.SCHNITTPUNKT_SCHWARZ);
                        break;
                    case Konstante.SCHNITTPUNKT_WEISS:
                        this.auswertungBrett[i][j].setBelegungswert(Konstante.SCHNITTPUNKT_WEISS);
                        break;
                    case Konstante.SCHNITTPUNKT_LEER:
                        this.auswertungBrett[i][j].setBelegungswert(Konstante.SCHNITTPUNKT_LEER);
                        break;
                    default: /* Hierer darf man nicht kommen */
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
    public int[][] getAusgewertetesFeld() {
        int rueckgabe[][] = new int[this.getFeldGroesse()][this.getFeldGroesse()];
        for (int i = 0; i < this.getFeldGroesse(); i++) {
            for (int j = 0; j < this.getFeldGroesse(); j++) {
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
     * Breitensuche fort. Trifft man auf einen Stein (lebendig oder tot), so
     * wird die Farbe des Gebietes ermittelt. Widersprechen sich die Werte,
     * so wird das Gebiet am Ende als leer gekennzeichnet.
     */
    private void findeGebiete() {
        /* Als erstes werden alle Schnittpunkte als nicht analysiert und nicht
         * markiert gekennzeichnet. */
        for (int i = 0; i < this.getFeldGroesse(); i++) {
            for (int j = 0; j < this.getFeldGroesse(); j++) {
                this.auswertungBrett[i][j].setAnalysiert(false);
                this.auswertungBrett[i][j].setMarkiert(false);
            }
        }

        int farbe = -1;
        boolean gebietMarkieren = true;
        List<AnalyseSchnittpunkt> listeSteine = new ArrayList<AnalyseSchnittpunkt>();
        int momElement = 0;

        /* Jetzt wird jeder Schnittpunkt angesehen und versucht von ihm aus
         * eine Breitensuche zu starten (Vorrausgesetzt der Schnittpunkt ist
         * leer).
         */
        for (int i = 0; i < this.getFeldGroesse(); i++) {
            for (int j = 0; j < this.getFeldGroesse(); j++) {
                /* Vorraussetzung, fuer die Analyse ist, das der Schnittpunkt
                 * noch nicht analysiert wurd. Eine suche beginnt bei einem
                 * nicht belegten Schnittpunkt, also Leer oder Gebiet
                 */
                if ((this.auswertungBrett[i][j].getBelegungswert() == Konstante.SCHNITTPUNKT_LEER
                        || this.auswertungBrett[i][j].getBelegungswert() == Konstante.SCHNITTPUNKT_GEBIET_WEISS
                        || this.auswertungBrett[i][j].getBelegungswert() == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ)
                        && this.auswertungBrett[i][j].getAnalysiert() == false) {
                    /* Nun beginnt die suche bei diesem Schnittpunkt */
                    farbe = -1;            // Farbe am anfang unbekannt
                    gebietMarkieren = true;
                    momElement = 0;

                    /* Als erstes wird der Schnittpunkt (i,j) aufgenommen. Von
                     * hier aus beginnt die Suche */
                    listeSteine.add(this.auswertungBrett[i][j]);
                    this.auswertungBrett[i][j].setMarkiert(true);

                    /* Solange die Liste nicht abgearbeitet wurde, suche weiter */
                    do {
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
                        if (listeSteine.get(momElement).getXPos() != 0) {
                            if ((this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_LEER
                                    || this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ
                                    || this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_GEBIET_WEISS)
                                    && this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getMarkiert()
                                    == false) {
                                /* Stein aufnehmen */
                                listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()]);
                                this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].setMarkiert(true);
                            } /* Ist nachbarstein lebendig oder tot */ else if (this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_SCHWARZ
                                    || this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_WEISS
                                    || this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN
                                    || this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_WEISS_GEFANGEN) {
                                switch (this.auswertungBrett[listeSteine.get(momElement).getXPos() - 1][listeSteine.get(momElement).getYPos()].getBelegungswert()) {
                                    case Konstante.SCHNITTPUNKT_SCHWARZ:
                                    case Konstante.SCHNITTPUNKT_WEISS_GEFANGEN:
                                        switch (farbe) {
                                            case -1:
                                                farbe = Konstante.SCHNITTPUNKT_SCHWARZ;
                                                break;
                                            case Konstante.SCHNITTPUNKT_SCHWARZ:
                                                /* nichts */
                                                break;
                                            case Konstante.SCHNITTPUNKT_WEISS:
                                                gebietMarkieren = false;
                                                break;
                                            default: /* Darf nicht passieren */
                                                throw new UnsupportedOperationException("Variable farbe enthaelt unerwartete Werte -> " + farbe);
                                        }
                                        break;
                                    case Konstante.SCHNITTPUNKT_WEISS:
                                    case Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN:
                                        switch (farbe) {
                                            case -1:
                                                farbe = Konstante.SCHNITTPUNKT_WEISS;
                                                break;
                                            case Konstante.SCHNITTPUNKT_SCHWARZ:
                                                gebietMarkieren = false;
                                                break;
                                            case Konstante.SCHNITTPUNKT_WEISS:
                                                /* nichts */
                                                break;
                                            default: /* Darf nicht passieren */
                                                throw new UnsupportedOperationException("Variable farbe enthaelt unerwartete Werte -> " + farbe);
                                        }
                                        break;
                                    default: /* Darf nicht passieren */
                                        throw new UnsupportedOperationException("Fehler in switch case, eigentlich muesste hier SCHNITTPUNKT_SCHWARZ oder SCHNITTPUNKT_WEISS stehen");
                                }
                            } /* Komische Werte abfangen */ else {
                                /* nichts passiert */
                            }
                        }

                        /* 2. Rechte Seite */
                        if (listeSteine.get(momElement).getXPos() != this.getFeldGroesse() - 1) {
                            if ((this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_LEER
                                    || this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ
                                    || this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_GEBIET_WEISS)
                                    && this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getMarkiert()
                                    == false) {
                                /* Stein aufnehmen */
                                listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()]);
                                this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].setMarkiert(true);
                            } /* Ist nachbarstein lebendig oder tot */ else if (this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_SCHWARZ
                                    || this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_WEISS
                                    || this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN
                                    || this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_WEISS_GEFANGEN) {
                                switch (this.auswertungBrett[listeSteine.get(momElement).getXPos() + 1][listeSteine.get(momElement).getYPos()].getBelegungswert()) {
                                    case Konstante.SCHNITTPUNKT_SCHWARZ:
                                    case Konstante.SCHNITTPUNKT_WEISS_GEFANGEN:
                                        switch (farbe) {
                                            case -1:
                                                farbe = Konstante.SCHNITTPUNKT_SCHWARZ;
                                                break;
                                            case Konstante.SCHNITTPUNKT_SCHWARZ:
                                                /* nichts */
                                                break;
                                            case Konstante.SCHNITTPUNKT_WEISS:
                                                gebietMarkieren = false;
                                                break;
                                            default: /* Darf nicht passieren */
                                                throw new UnsupportedOperationException("Variable farbe enthaelt unerwartete Werte -> " + farbe);
                                        }
                                        break;
                                    case Konstante.SCHNITTPUNKT_WEISS:
                                    case Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN:
                                        switch (farbe) {
                                            case -1:
                                                farbe = Konstante.SCHNITTPUNKT_WEISS;
                                                break;
                                            case Konstante.SCHNITTPUNKT_SCHWARZ:
                                                gebietMarkieren = false;
                                                break;
                                            case Konstante.SCHNITTPUNKT_WEISS:
                                                /* nichts */
                                                break;
                                            default: /* Darf nicht passieren */
                                                throw new UnsupportedOperationException("Variable farbe enthaelt unerwartete Werte -> " + farbe);
                                        }
                                        break;
                                    default: /* Darf nicht passieren */
                                        throw new UnsupportedOperationException("Fehler in switch case, eigentlich muesste hier SCHNITTPUNKT_SCHWARZ oder SCHNITTPUNKT_WEISS stehen");
                                }
                            } /* Komische Werte abfangen */ else {
                                /* nichts passiert */
                            }

                        }

                        /* 3. Untere Seite */
                        if (listeSteine.get(momElement).getYPos() != 0) {
                            if ((this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_LEER
                                    || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ
                                    || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_GEBIET_WEISS)
                                    && this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getMarkiert()
                                    == false) {
                                /* Stein aufnehmen */
                                listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1]);
                                this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].setMarkiert(true);
                            } /* Ist nachbarstein lebendig oder tot */ else if (this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_SCHWARZ
                                    || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_WEISS
                                    || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN
                                    || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_WEISS_GEFANGEN) {
                                switch (this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() - 1].getBelegungswert()) {
                                    case Konstante.SCHNITTPUNKT_SCHWARZ:
                                    case Konstante.SCHNITTPUNKT_WEISS_GEFANGEN:
                                        switch (farbe) {
                                            case -1:
                                                farbe = Konstante.SCHNITTPUNKT_SCHWARZ;
                                                break;
                                            case Konstante.SCHNITTPUNKT_SCHWARZ:
                                                /* nichts */
                                                break;
                                            case Konstante.SCHNITTPUNKT_WEISS:
                                                gebietMarkieren = false;
                                                break;
                                            default: /* Darf nicht passieren */
                                                throw new UnsupportedOperationException("Variable farbe enthaelt unerwartete Werte -> " + farbe);
                                        }
                                        break;
                                    case Konstante.SCHNITTPUNKT_WEISS:
                                    case Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN:
                                        switch (farbe) {
                                            case -1:
                                                farbe = Konstante.SCHNITTPUNKT_WEISS;
                                                break;
                                            case Konstante.SCHNITTPUNKT_SCHWARZ:
                                                gebietMarkieren = false;
                                                break;
                                            case Konstante.SCHNITTPUNKT_WEISS:
                                                /* nichts */
                                                break;
                                            default: /* Darf nicht passieren */
                                                throw new UnsupportedOperationException("Variable farbe enthaelt unerwartete Werte -> " + farbe);
                                        }
                                        break;
                                    default: /* Darf nicht passieren */
                                        throw new UnsupportedOperationException("Fehler in switch case, eigentlich muesste hier SCHNITTPUNKT_SCHWARZ oder SCHNITTPUNKT_WEISS stehen");
                                }
                            } /* Komische Werte abfangen */ else {
                                /* nichts passiert */
                            }
                        }

                        /* 4. Obere Seite */
                        if (listeSteine.get(momElement).getYPos() != this.getFeldGroesse() - 1) {
                            if ((this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_LEER
                                    || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ
                                    || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_GEBIET_WEISS)
                                    && this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getMarkiert()
                                    == false) {
                                /* Stein aufnehmen */
                                listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1]);
                                this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].setMarkiert(true);
                            } /* Ist nachbarstein lebendig oder tot */ else if (this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_SCHWARZ
                                    || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_WEISS
                                    || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN
                                    || this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getBelegungswert()
                                    == Konstante.SCHNITTPUNKT_WEISS_GEFANGEN) {
                                switch (this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos() + 1].getBelegungswert()) {
                                    case Konstante.SCHNITTPUNKT_SCHWARZ:
                                    case Konstante.SCHNITTPUNKT_WEISS_GEFANGEN:
                                        switch (farbe) {
                                            case -1:
                                                farbe = Konstante.SCHNITTPUNKT_SCHWARZ;
                                                break;
                                            case Konstante.SCHNITTPUNKT_SCHWARZ:
                                                /* nichts */
                                                break;
                                            case Konstante.SCHNITTPUNKT_WEISS:
                                                gebietMarkieren = false;
                                                break;
                                            default: /* Darf nicht passieren */
                                                throw new UnsupportedOperationException("Variable farbe enthaelt unerwartete Werte -> " + farbe);
                                        }
                                        break;
                                    case Konstante.SCHNITTPUNKT_WEISS:
                                    case Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN:
                                        switch (farbe) {
                                            case -1:
                                                farbe = Konstante.SCHNITTPUNKT_WEISS;
                                                break;
                                            case Konstante.SCHNITTPUNKT_SCHWARZ:
                                                gebietMarkieren = false;
                                                break;
                                            case Konstante.SCHNITTPUNKT_WEISS:
                                                /* nichts */
                                                break;
                                            default: /* Darf nicht passieren */
                                                throw new UnsupportedOperationException("Variable farbe enthaelt unerwartete Werte -> " + farbe);
                                        }
                                        break;
                                    default: /* Darf nicht passieren */
                                        throw new UnsupportedOperationException("Fehler in switch case, eigentlich muesste hier SCHNITTPUNKT_SCHWARZ oder SCHNITTPUNKT_WEISS stehen");
                                }
                            } /* Komische Werte abfangen */ else {
                                /* nichts passiert */
                            }
                        }
                        /* Alle Seiten untersucht. Nun zum naechsten Stein */
                        listeSteine.get(momElement).setAnalysiert(true);
                        momElement++;
                    } while (momElement < listeSteine.size());
                    /* Jetzt sind alle Steine aufgenommen worden. In abhaengigkeit
                     * von gebietMarkieren wird fortgefahren. Wenn man das gebiet
                     * Markieren soll, muss noch nach der farbe gefragt werden.
                     * Diese darf dabei nicht undefiniert sein. (leeres Brett)*/
                    if (gebietMarkieren == true && farbe != -1) {
                        int markierfarbe;
                        if (farbe == Konstante.SCHNITTPUNKT_SCHWARZ) {
                            markierfarbe = Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ;
                        } else if (farbe == Konstante.SCHNITTPUNKT_WEISS) {
                            markierfarbe = Konstante.SCHNITTPUNKT_GEBIET_WEISS;
                        } else {
                            /* Wenn unerwartet, einfach als Leer markieren! */
                            markierfarbe = Konstante.SCHNITTPUNKT_LEER;
                        }
                        for (int k = listeSteine.size() - 1; k >= 0; k--) {
                            this.auswertungBrett[listeSteine.get(k).getXPos()][listeSteine.get(k).getYPos()].setBelegungswert(markierfarbe);
                            listeSteine.remove(k);
                        }
                    } else {
                        for (int k = listeSteine.size() - 1; k >= 0; k--) {
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
    private void suchePseudoPunkte() {
        /* Brett initialisieren */
        for (int i = 0; i < this.getFeldGroesse(); i++) {
            for (int j = 0; j < this.getFeldGroesse(); j++) {
                this.auswertungBrett[i][j].setMarkiert(false);
                this.auswertungBrett[i][j].setAnalysiert(false);
            }
        }

        /* Ueberall suchen, aber nur wenn schnittpunkt noch nicht analysiert. */
        boolean rueckgabeUp = true;
        for (int i = 0; i < this.getFeldGroesse(); i++) {
            for (int j = 0; j < this.getFeldGroesse(); j++) {
                if ((this.auswertungBrett[i][j].getBelegungswert() == Konstante.SCHNITTPUNKT_SCHWARZ
                        || this.auswertungBrett[i][j].getBelegungswert() == Konstante.SCHNITTPUNKT_WEISS)
                        && this.auswertungBrett[i][j].getAnalysiert() == false) {
                    do {
                        rueckgabeUp = this.findePseudePunkte(i, j);
                    } while (rueckgabeUp == true);
                }
            }
        }
    }

    /**
     * Es wird an der Stelle (xPos, yPos) nach Pseudofreiheiten gesucht.
     * Eine Pseudofreiheit ist dabei ein unwirklicher gebietspunkt, also ein
     * als Gebietspunkt markierter Schnittpunkt, der noch zugesetzt werden muss.
     * Der Ausgangsschnittpunkt ist ein Lebendiger Stein.
     * @param xPos X-Koordinate (0 bis Brettgroesse-1)
     * @param yPos Y-Koordinate (0 bis Brettgroesse-1)
     * @return War suche and diesem Punkt erfolgreich? (Ja-> true)
     */
    private boolean findePseudePunkte(int xPos, int yPos) {
        /* Testen, ob eingabe Korrekt */
        if (xPos < 0 || xPos >= this.getFeldGroesse() || yPos < 0 || yPos >= this.getFeldGroesse()
                || (this.auswertungBrett[xPos][yPos].getBelegungswert() != Konstante.SCHNITTPUNKT_SCHWARZ
                && this.auswertungBrett[xPos][yPos].getBelegungswert() != Konstante.SCHNITTPUNKT_WEISS)) {
            return false;
        }

        /* Eingabe ist richtig, suche beginnen. Vorraussetztung ist, das die
         * Steine noch nicht markiert und nicht Analysiert sind.
         * als erstes werden die variablen definiert um die suche zu vereinfachen*/
        int farbe = -1;
        int gegenfarbe = -1;
        int gegenfarbeGefangen = -1;
        int farbeGebiet = -1;
        switch (this.auswertungBrett[xPos][yPos].getBelegungswert()) {
            case Konstante.SCHNITTPUNKT_SCHWARZ:
                farbe = Konstante.SCHNITTPUNKT_SCHWARZ;
                gegenfarbe = Konstante.SCHNITTPUNKT_WEISS;
                gegenfarbeGefangen = Konstante.SCHNITTPUNKT_WEISS_GEFANGEN;
                farbeGebiet = Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ;
                break;
            case Konstante.SCHNITTPUNKT_WEISS:
                farbe = Konstante.SCHNITTPUNKT_WEISS;
                gegenfarbe = Konstante.SCHNITTPUNKT_SCHWARZ;
                gegenfarbeGefangen = Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN;
                farbeGebiet = Konstante.SCHNITTPUNKT_GEBIET_WEISS;
                break;
            default: /* Darf nicht passieren */
                return false;
        }

        /* Nun kann die Suche beginnen. Man nimmt nur Steine von "farbe", oder
         * Leere Schnittpunkte, die nicht als Gebiet markiert sind auf.
         * Dabei ist folgende Regel zu beachten. Ist der Schnittpunkt, von dem
         * aus gerade gesucht wird Leer und unmarkiert, so kann er keine weiteren
         * leeren Schnittpunkte aufnehmen, aber Steine von "farbe".
         * Trifft man auf einen Stein der "gegenfarbe" so sucht man dort nicht
         * weiter.
         * Trifft man auf einen Stein der "gegenfarbeGefangen", so zaehlt dieser
         * als Freiheit (kann noch geaendert werden)
         * Trifft man auf einen Punkt "farbeGebiet" so ist dies auch eine Freiheit
         */

        List<AnalyseSchnittpunkt> listeSteine = new ArrayList<AnalyseSchnittpunkt>();
        int momElement = 0;

        /* Findet man Freiheiten, merkt man sich wo die letzte war. */
        int freiheiten = 0;
        int xPosFreiheit = -1;
        int yPosFreiheit = -1;

        /* Der erste Stein wird in die Liste aufgenommen, dann startet die
         * suche */
        listeSteine.add(this.auswertungBrett[xPos][yPos]);
        this.auswertungBrett[xPos][yPos].setMarkiert(true);

        do {
            /* Da man nur lebende Steine als Analysiert kennzeichnet, besitzen
             * diese, wenn sie als analysiert gekennzeichnet sind mindestens 2
             * Freiheiten. Es ist daher klar, das die Gruppe keine Pseudofreiheiten
             * hat. freiheiten kann daher auf 2 gesetzt werden.
             * 
             *
             * */
            if(listeSteine.get(momElement).getBelegungswert() != farbeGebiet){

            if(listeSteine.get(momElement).getXPos() != 0){
                if(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_LEER){
                    if(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getMarkiert()
                            == false){
                        if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()].getBelegungswert() != Konstante.SCHNITTPUNKT_LEER){
                           listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()]);
                         this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].setMarkiert(true);
                        }
                    }
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == farbe){
                    if(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getAnalysiert()
                            == true ||
                       this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getMarkiert()
                            == false){
                         listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()]);
                         this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].setMarkiert(true);
                         this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].setAnalysiert(false);
                    }
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == gegenfarbe){
                    /* nichts , das ist ein Stopper */
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == gegenfarbeGefangen){
                    /* Dann werden keine Pseudofreiheiten markiert, da dies zu kompliziert ist */
                    freiheiten = 2;
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == farbeGebiet){
                    if(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getMarkiert() == false){
                        freiheiten++;
                        xPosFreiheit = this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getXPos();
                        yPosFreiheit = this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].getYPos();
                        this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()].setMarkiert(true);
                        listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()-1][listeSteine.get(momElement).getYPos()]);
                    }
                }
                else {
                    /* nichts machen */
                }
            }

            if(listeSteine.get(momElement).getXPos() != this.getFeldGroesse()-1){
                if(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_LEER){
                    if(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getMarkiert()
                            == false){
                        if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()].getBelegungswert() != Konstante.SCHNITTPUNKT_LEER){
                           listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()]);
                         this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].setMarkiert(true);
                        }
                    }
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == farbe){
                    if(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getAnalysiert()
                            == true ||
                       this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getMarkiert()
                            == false){
                         listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()]);
                         this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].setMarkiert(true);
                         this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].setAnalysiert(false);
                    }
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == gegenfarbe){
                    /* nichts , das ist ein Stopper */
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == gegenfarbeGefangen){
                    /* Dann werden keine Pseudofreiheiten markiert, da dies zu kompliziert ist */
                    freiheiten = 2;
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getBelegungswert()
                        == farbeGebiet){
                    if(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getMarkiert() == false){
                        freiheiten++;
                        xPosFreiheit = this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getXPos();
                        yPosFreiheit = this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].getYPos();
                        this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()].setMarkiert(true);
                        listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()+1][listeSteine.get(momElement).getYPos()]);
                    }
                }
                else {
                    /* nichts machen */
                }
            }

            if(listeSteine.get(momElement).getYPos() != 0){
                if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_LEER){
                    if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getMarkiert()
                            == false){
                        if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()].getBelegungswert() != Konstante.SCHNITTPUNKT_LEER){
                           listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1]);
                         this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].setMarkiert(true);
                        }
                    }
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                        == farbe){
                    if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getAnalysiert()
                            == true ||
                       this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getMarkiert()
                            == false){
                         listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1]);
                         this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].setMarkiert(true);
                         this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].setAnalysiert(false);
                    }
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                        == gegenfarbe){
                    /* nichts , das ist ein Stopper */
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                        == gegenfarbeGefangen){
                    /* Dann werden keine Pseudofreiheiten markiert, da dies zu kompliziert ist */
                    freiheiten = 2;
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getBelegungswert()
                        == farbeGebiet){
                    if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getMarkiert() == false){
                        freiheiten++;
                        xPosFreiheit = this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getXPos();
                        yPosFreiheit = this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].getYPos();
                        this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1].setMarkiert(true);
                        listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()-1]);
                    }
                }
                else {
                    /* nichts machen */
                }
            }

            if(listeSteine.get(momElement).getYPos() != this.getFeldGroesse()-1){
                if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                        == Konstante.SCHNITTPUNKT_LEER){
                    if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getMarkiert()
                            == false){
                        if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()].getBelegungswert() != Konstante.SCHNITTPUNKT_LEER){
                           listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1]);
                         this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].setMarkiert(true);
                        }
                    }
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                        == farbe){
                    if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getAnalysiert()
                            == true ||
                       this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getMarkiert()
                            == false){
                         listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1]);
                         this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].setMarkiert(true);
                         this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].setAnalysiert(false);
                    }
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                        == gegenfarbe){
                    /* nichts , das ist ein Stopper */
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                        == gegenfarbeGefangen){
                    /* Dann werden keine Pseudofreiheiten markiert, da dies zu kompliziert ist */
                    freiheiten = 2;
                }
                else if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getBelegungswert()
                        == farbeGebiet){
                    if(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getMarkiert() == false){
                        freiheiten++;
                        xPosFreiheit = this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getXPos();
                        yPosFreiheit = this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].getYPos();
                        this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1].setMarkiert(true);
                        listeSteine.add(this.auswertungBrett[listeSteine.get(momElement).getXPos()][listeSteine.get(momElement).getYPos()+1]);
                    }
                }
                else {
                    /* nichts machen */
                }
            }
            }
            momElement++;
        } while (momElement < listeSteine.size());
        /* Nun wurden alle Steine Aufgenommen. Ist die freiheitenzahl genau 1,
         * so wurde eine Pseudofreiheit gefunden (wenn dort kein gefangener steht)
         * Ist die Anzahl nicht 1, so wurde keine Pseudofreiheit gefunden, die
         * Steine sind somit analysiert
         * Wurden Pseudofreiheiten gefunden, werden die Steine nicht als analysiert
         * gekennzeichnet und die markierung wird auch aufgehoben. So kann man
         * die suche erneut anstossen und nach weiteren Pseudofreiheiten suchen.
         */
        if (freiheiten == 1) {
            if (this.auswertungBrett[xPosFreiheit][yPosFreiheit].getBelegungswert()
                    == farbeGebiet) {
                /* Dann markiere die Pseudofreiheit */
                this.auswertungBrett[xPosFreiheit][yPosFreiheit].setBelegungswert(Konstante.SCHNITTPUNKT_LEER);
                for (int i = listeSteine.size() - 1; i >= 0; i--) {
                    if(this.auswertungBrett[listeSteine.get(i).getXPos()][listeSteine.get(i).getYPos()].getBelegungswert() == farbe){
                        this.auswertungBrett[listeSteine.get(i).getXPos()][listeSteine.get(i).getYPos()].setMarkiert(false);
                        this.auswertungBrett[listeSteine.get(i).getXPos()][listeSteine.get(i).getYPos()].setAnalysiert(false);
                    }
                    else if(this.auswertungBrett[listeSteine.get(i).getXPos()][listeSteine.get(i).getYPos()].getBelegungswert() == Konstante.SCHNITTPUNKT_LEER){
                        this.auswertungBrett[listeSteine.get(i).getXPos()][listeSteine.get(i).getYPos()].setMarkiert(false);
                        this.auswertungBrett[listeSteine.get(i).getXPos()][listeSteine.get(i).getYPos()].setAnalysiert(false);
                    }
                    else if(this.auswertungBrett[listeSteine.get(i).getXPos()][listeSteine.get(i).getYPos()].getBelegungswert() == farbeGebiet){
                        this.auswertungBrett[listeSteine.get(i).getXPos()][listeSteine.get(i).getYPos()].setMarkiert(false);
                        this.auswertungBrett[listeSteine.get(i).getXPos()][listeSteine.get(i).getYPos()].setAnalysiert(false);

                    }
                    listeSteine.remove(i);
                }
                return true;
            }
            else {
               throw new UnsupportedOperationException("Punkt als Gebiet erkannt, aber dann war Belegungswert ploetzlich falsch");
            }
        }

        for (int i = listeSteine.size() - 1; i >= 0; i--) {
            if(this.auswertungBrett[listeSteine.get(i).getXPos()][listeSteine.get(i).getYPos()].getBelegungswert() == farbe){
                        this.auswertungBrett[listeSteine.get(i).getXPos()][listeSteine.get(i).getYPos()].setMarkiert(true);
                        this.auswertungBrett[listeSteine.get(i).getXPos()][listeSteine.get(i).getYPos()].setAnalysiert(true);
            }
            else if(this.auswertungBrett[listeSteine.get(i).getXPos()][listeSteine.get(i).getYPos()].getBelegungswert() == Konstante.SCHNITTPUNKT_LEER){
                        this.auswertungBrett[listeSteine.get(i).getXPos()][listeSteine.get(i).getYPos()].setMarkiert(false);
                        this.auswertungBrett[listeSteine.get(i).getXPos()][listeSteine.get(i).getYPos()].setAnalysiert(false);
            }
            else if(this.auswertungBrett[listeSteine.get(i).getXPos()][listeSteine.get(i).getYPos()].getBelegungswert() == farbeGebiet){
                        this.auswertungBrett[listeSteine.get(i).getXPos()][listeSteine.get(i).getYPos()].setMarkiert(false);
                        this.auswertungBrett[listeSteine.get(i).getXPos()][listeSteine.get(i).getYPos()].setAnalysiert(false);

            }

            listeSteine.remove(i);
        }
        return false;
    }

    /** 
     * Gibt zurueck wieviele schwarze Steine auf dem Brett als gefangen markiert
     * sind
     * @return Anzahl der Gefangenen
     */
    public int getSchwarzeGefangeneAufBrett() {
        int rueckgabe = 0;

        for (int i = 0; i < this.getFeldGroesse(); i++) {
            for (int j = 0; j < this.getFeldGroesse(); j++) {
                if (this.auswertungBrett[i][j].getBelegungswert() == Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN) {
                    rueckgabe++;
                }
            }
        }
        return rueckgabe;
    }

    /**
     * Gibt zurueck wieviele weisse Steine auf dem Brett als gefangen markiert
     * sind
     * @return Anzahl der Gefangenen
     */
    public int getWeisseGefangeneAufBrett() {
        int rueckgabe = 0;

        for (int i = 0; i < this.getFeldGroesse(); i++) {
            for (int j = 0; j < this.getFeldGroesse(); j++) {
                if (this.auswertungBrett[i][j].getBelegungswert() == Konstante.SCHNITTPUNKT_WEISS_GEFANGEN) {
                    rueckgabe++;
                }
            }
        }
        return rueckgabe;
    }

    /**
     * Die Gebietspunkte des Schwarzen Spielers ist gleich der Anzahl der Markierten
     * gebietspunkte plus der Anzahl der auf dem Brett gefangenen Weissen Steinen.
     * @return Anzahl der Gebietspunkte fuer Schwarz
     */
    public int getGebietsPunkteSchwarz() {
        int rueckgabe = 0;

        for (int i = 0; i < this.getFeldGroesse(); i++) {
            for (int j = 0; j < this.getFeldGroesse(); j++) {
                if (this.auswertungBrett[i][j].getBelegungswert() == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ) {
                    rueckgabe++;
                }
                if (this.auswertungBrett[i][j].getBelegungswert() == Konstante.SCHNITTPUNKT_WEISS_GEFANGEN) {
                    rueckgabe++;
                }

            }
        }
        return rueckgabe;
    }

    /**
     * Die Gebietspunkte des Weissen Spielers ist gleich der Anzahl der Markierten
     * gebietspunkte plus der Anzahl der auf dem Brett gefangenen Schwarzen Steinen.
     * @return Anzahl der Gebietspunkte fuer Weiss
     */
    public int getGebietsPunkteWeiss() {
        int rueckgabe = 0;

        for (int i = 0; i < this.getFeldGroesse(); i++) {
            for (int j = 0; j < this.getFeldGroesse(); j++) {
                if (this.auswertungBrett[i][j].getBelegungswert() == Konstante.SCHNITTPUNKT_GEBIET_WEISS) {
                    rueckgabe++;
                }
                if (this.auswertungBrett[i][j].getBelegungswert() == Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN) {
                    rueckgabe++;
                }
            }
        }
        return rueckgabe;
    }
}
