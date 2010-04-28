/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.awt.Point;

/**
 *
 * @author steven
 * @version 0.1
 */
public interface OberflaecheInterface {

    /**
     * Das Brett wird hier Dargestellt. Dabei ist die Koordinate (0,0) links unten!
     * @param spielfeld ist ein zweidimensionales Integer-Array mit der Groesse
     * @param spielfeldGroesse Das Array kann die Werte SCHNITTPUNKT_LEER,
     * SCHNITTPUNKT_SCHWARZ, SCHNITTPUNKT_WEISS und SCHNITTPUNKT_VERBOTEN enthalten,
     * welche in der Klasse Konstante spezifiziert sind.
     * Der Parameter @param marierterStein ist null wenn nichts zu markieren ist.
     * Ansonsten gibt er den als zuletzt gelegtenStein zum markieren fest.
     */
    public void setBrettOberflaeche(int spielfeld[][], int spielfeldGroesse, Point markierterStein);

    /**
     * Setzt die Zeitanzeige der Periodenzeit fuer den weissen Spieler
     * auf der GUI
     * @param periodenZeitInMS Periodenzeit in Millisekunden
     */
    public void setAnzeigePeriodenZeitWeiss(long periodenZeitInMS);

    /**
     * Setzt die Zeitanzeige der Periodenzeit fuer den schwarzen Spieler
     * auf der GUI
     * @param periodenZeitInMS Periodenzeit in Millisekunden
     */
    public void setAnzeigePeriodenZeitSchwarz(long periodenZeitInMS);

    /**
     * Setzt die Zeitanzeige der Hauptzeit fuer den weissen Spieler
     * auf der GUI
     * @param spielerZeitInMS Hauptzeit in Millisekunden
     */
    public void setAnzeigeSpielerZeitWeiss(long spielerZeitInMS);

    /**
     * Setzt die Zeitanzeige der Hauptzeit fuer den schwarzen Spieler
     * auf der GUI
     * @param spielerZeitInMS Hauptzeit in Millisekunden
     */
    public void setAnzeigeSpielerZeitSchwarz(long spielerZeitInMS);

    /**
     * Setzt den namen des weissen Spielers auf der GUI
     * @param spielername Name des Spielers
     */
    public void setSpielernameWeiss(String spielername);

    /**
     * Setzt den namen des schwarzen Spielers auf der GUI
     * @param spielername Name des Spielers
     */
    public void setSpielernameSchwarz(String spielername);

    /**
     * Veraendert die Anzeige der gefangenen weissen Steine
     * @param anzGefangenerSteiner Anzahl der Steine
     */
    public void setGefangeneSteineWeiss(int anzGefangenerSteiner);

    /**
     * Veraendert die Anzeige der gefangenen schwarzen Steine
     * @param anzGefangenerSteiner Anzahl der Steine
     */
    public void setGefangeneSteineSchwarz(int anzGefangenerSteiner);

    /**
     * Macht auf der GUI sichtbar, dass Schwarz am Zug ist.
     */
    public void setSchwarzAmZug();

    /**
     * Macht auf der GUI sichtbar, dass Weiss am Zug ist.
     */
    public void setWeissAmZug();

    /**
     *
     * Sagt der Oberflaeche ob es grafisch eine @param undoMoeglich keit erlauben soll
     */
    public void setUndoErlaubt(boolean undoMoeglich);

    /**
     * 
     * Sagt der Oberflaeche ob es grafisch eine @param redoMoeglich keit erlauben soll
     */
    public void setRedoErlaubt(boolean redoMoeglich);

    /**
     * Gibt auf der GUI eine Fehlermeldung aus.
     * @param fehlertext Auszugebender Fehlertext
     */
    public void gibFehlermeldungAus(String fehlertext);

    /**
     * Mit dem Parameter @param visible kann die Oberflaeche ein- oder
     * ausgeblendet werden. Achtung! Countdowns etc. können gegebenenfalls
     * trotzdem noch laufen!
     */
    public void setVisible (boolean visible);

    /**
     * Der Oberflaeche kann über @param setPause ein Tipp gegeben werden, ob
     * sie eine Art Pause- oder Blackscreen anzeigen möchte/sollte.
     */
    public void setPauseScreen(boolean setPause);

    public void setAuswertungsButtonsVisible (boolean visible );
}
