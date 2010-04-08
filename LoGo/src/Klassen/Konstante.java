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

    // Konstanten fuer den Zustand des Spielfeldes
    public final static int SPIEL_UNVOLLSTAENDIG        = 100;
    public final static int SPIEL_VALIDIERT             = 101;
    public final static int SPIEL_LAUEFT                = 102;
    public final static int SPIEL_PAUSIERT              = 103;
    public final static int SPIEL_AUFGEGEBEN            = 104;
    public final static int SPIEL_BEENDET_DURCH_APP     = 105;


}
