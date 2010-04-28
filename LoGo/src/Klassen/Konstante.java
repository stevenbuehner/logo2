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
    public final static int FEHLER                      = -1;
    public final static int SCHNITTPUNKT_LEER           = 0;
    public final static int SCHNITTPUNKT_SCHWARZ        = 1;
    public final static int SCHNITTPUNKT_WEISS          = 2;
    public final static int SCHNITTPUNKT_VERBOTEN       = 3;

    // Konstanten fuer Gefangenenstatus
    public final static int STEIN_UNGEWISS              = 4;
    public final static int STEIN_GEFANGEN              = 5;
    public final static int STEIN_LEBENDIG              = 6;

    // Konstanten fuer Spielauswertung
    public final static int SCHNITTPUNKT_GEBIET_SCHWARZ = 8;
    public final static int SCHNITTPUNKT_GEBIET_WEISS   = 9;
    public final static int SCHNITTPUNKT_SCHWARZ_GEFANGEN = 10;
    public final static int SCHNITTPUNKT_WEISS_GEFANGEN = 11;

    // Konstanten fuer den Zustand des Spielfeldes
    public final static int SPIEL_GEBIETSAUSWERTUNG     = 101;  // Im Auszählmodus, anklicken von lebenden und toten Steinen
    public final static int SPIEL_LAUEFT                = 102;  // Spiel ist gestartet und pausiert nicht
    public final static int SPIEL_PAUSIERT              = 103;
    public final static int SPIEL_BEENDET               = 104;  // Das Spiel wurde beendet (aufgegeben, zeitaus, ...)

}
