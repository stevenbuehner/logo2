/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor
 */
package Klassen;

/**
 *
 * @author steven
 */
public class Konstante {

    // Konstanten
    /**
     * Konstante Wenn ein Fehler auftritt ist FEHLER
     */
    public final static int FEHLER = -1;
    /**
     * Ist ein Schnittpunkt nicht belegt, also Leer, so wird dies mit der Konstante
     * SCHNITTPUNKT_LEER ausgedrueckt.
     */
    public final static int SCHNITTPUNKT_LEER = 0;
    /**
     * Ist ein Schnittpunkt mit einem schwarzen Stein belegt, so wird dies mit
     * der Konstante SCHNITTPUNKT_SCHWARZ ausgedrueckt.
     */
    public final static int SCHNITTPUNKT_SCHWARZ = 1;
    /**
     * Ist ein Schnittpunkt mit einem weissen Stein belegt, so wird dies mit
     * der Konstante SCHNITTPUNKT_WEISS ausgedrueckt.
     */
    public final static int SCHNITTPUNKT_WEISS = 2;
    /**
     * Ist ein Schnittpunkt wegen der Ko-Regel verboten, so wird dies mit
     * der Konstante SCHNITTPUNKT_VERBOTEN ausgedrueckt.
     */
    public final static int SCHNITTPUNKT_VERBOTEN = 3;


    // Konstanten fuer Gefangenenstatus
    /**
     * Ist der Status eines Steines unbekannt, wird dies mit der Konstante
     * STEIN_UNGEWISS ausgedrueckt.
     */
    public final static int STEIN_UNGEWISS = 4;
    /**
     * Ist der Status eines Steines gefangen, wird dies mit der Konstante
     * STEIN_GEFANGEN ausgedrueckt.
     */
    public final static int STEIN_GEFANGEN = 5;
    /**
     * Ist der Status eines Steines lebendig, wird dies mit der Konstante
     * STEIN_LEBENDIG ausgedrueckt.
     */
    public final static int STEIN_LEBENDIG = 6;


    // Konstanten fuer Spielauswertung
    /**
     * Soll ein Schnittpunkt als schwarzes Gebiet markiert werden, wird dies
     * mit der Konstante SCHNITTPUNKT_GEBIET_SCHWARZ ausgedrueckt.
     */
    public final static int SCHNITTPUNKT_GEBIET_SCHWARZ = 8;
    /**
     * Soll ein Schnittpunkt als weisses Gebiet markiert werden, wird dies
     * mit der Konstante SCHNITTPUNKT_GEBIET_WEISS ausgedrueckt.
     */
    public final static int SCHNITTPUNKT_GEBIET_WEISS = 9;
    /**
     * Soll ein schwarzer Stein als gefangen markiert werden, wird dies
     * mit der Konstante SCHNITTPUNKT_SCHWARZ_GEFANGEN ausgedrueckt.
     */
    public final static int SCHNITTPUNKT_SCHWARZ_GEFANGEN = 10;
    /**
     * Soll ein weisser Stein als gefangen markiert werden, wird dies
     * mit der Konstante SCHNITTPUNKT_WEISS_GEFANGEN ausgedrueckt.
     */
    public final static int SCHNITTPUNKT_WEISS_GEFANGEN = 11;


    // Konstanten fuer den Zustand des Spielfeldes
    /**
     * Befindet sich das Spiel in der Gebietsauswertungsphase, wird das durch
     * die Konstante SPIEL_GEBIETSAUSWERTUNG signalisiert. Hier koennen Steine
     * als Lebendig, oder Tot markiert werden
     */
    public final static int SPIEL_GEBIETSAUSWERTUNG = 101;
    /**
     * Befindet sich das Spiel in der Spielphase, wird das durch
     * die Konstante SPIEL_LAEUFT signalisiert. Das Spiel ist gestartet und
     * pausiert nicht.
     */
    public final static int SPIEL_LAUEFT = 102;
    /**
     * Wird das Spiel gerade pausiert, wird das durch
     * die Konstante SPIEL_PAUSIERT signalisiert.
     */
    public final static int SPIEL_PAUSIERT = 103;
    /**
     * Wurde das Spiel durch Aufgabe, Zeitaus oder Auszaehlen beendet, wird das durch
     * die Konstante SPIEL_BEENDET signalisiert.
     */
    public final static int SPIEL_BEENDET = 104;


    // Konstanten um zu Unterscheiden, wie das spiel beendet wurde
    /**
     * Wurde das Spiel durch Ueberschreiten des Zeitlimits beendet, kann dies
     * durch die Konstante BEENDET_DURCH_ZEIT ausgedrueckt werden.
     */
    public final static int BEENDET_DURCH_ZEIT = 201;
    /**
     * Wurde das Spiel durch auszahlen der Gebiete beendet, kann dies
     * durch die Konstante BEENDET_DURCH_AUSZAEHLEN ausgedrueckt werden.
     */
    public final static int BEENDET_DURCH_AUSZAEHLEN = 202;
    /**
     * Wurde das Spiel durch Aufgabe eines Spielers beendet, kann dies
     * durch die Konstante BEENDET_DURCH_AUFGEBEN ausgedrueckt werden.
     */
    public final static int BEENDET_DURCH_AUFGEBEN = 203;
}
