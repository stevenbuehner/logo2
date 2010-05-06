package Interfaces;

import java.awt.Point;

/**
 *
 * Interface für die Schnittstelle von der Steuerung zur Oberfläche.
 * Alle von der Steuerung benötigten Zugriffsmöglichkeiten auf die Oberfläche
 * sind hier definiert.
 *
 * Prinzipiell entscheidet ein Oberflächenobjekt (GUI) selbst darüber, welche
 * der übergebenen Funktionen und Parameter sie zur Darstellung verwendet.
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
     * Ansonsten übergibt er den als zuletzt gelegtenStein als Objekt der Klasse Punkt
     * zum markieren.
     */
    public void setBrettOberflaeche(int spielfeld[][], int spielfeldGroesse, Point markierterStein);

    /**
     * Setzt die Zeitanzeige der Periodenzeit für den weißen Spieler
     * auf der GUI. Als Zeiteinheit werden Millesekunden verwendet.
     * @param periodenZeitInMS Periodenzeit in Millisekunden
     */
    public void setAnzeigePeriodenZeitWeiss(long periodenZeitInMS);

    /**
     * Setzt die Zeitanzeige der Periodenzeit für den schwarzen Spieler
     * auf der GUI. Als Zeiteinheit werden Millesekunden verwendet.
     * @param periodenZeitInMS Periodenzeit in Millisekunden
     */
    public void setAnzeigePeriodenZeitSchwarz(long periodenZeitInMS);

    /**
     * Setzt die Zeitanzeige der Hauptzeit für den weißen Spieler
     * auf der GUI. Als Zeiteinheit werden Millesekunden verwendet.
     * @param spielerZeitInMS Hauptzeit in Millisekunden
     */
    public void setAnzeigeSpielerZeitWeiss(long spielerZeitInMS);

    /**
     * Setzt die Zeitanzeige der Hauptzeit für den schwarzen Spieler
     * auf der GUI. Als Zeiteinheit werden Millesekunden verwendet.
     * @param spielerZeitInMS Hauptzeit in Millisekunden
     */
    public void setAnzeigeSpielerZeitSchwarz(long spielerZeitInMS);

    /**
     * Setzt den Namen des weißen Spielers auf der GUI durch den Parameter
     * @param spielername.
     */
    public void setSpielernameWeiss(String spielername);

    /**
     * Setzt den Namen des schwarzen Spielers auf der GUI durch den Parameter
     * @param spielername.
     */
    public void setSpielernameSchwarz(String spielername);

    /**
     * Zum übergeben der Anzahl aktueller gefangener weißer Steine an die GUI
     * Der Parameter @param anzGefangenerSteiner ist dabei die Anzahl der Steine
     */
    public void setGefangeneSteineWeiss(int anzGefangenerSteiner);

    /**
     * Zum übergeben der Anzahl aktueller gefangener schwarzer Steine an die GUI
     * Der Parameter @param anzGefangenerSteiner ist dabei die Anzahl der Steine
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
     * Übergibt eine @param meldung speziell an den weißen Spieler.
     *
     */
    public void setSpielerMeldungWeiss(String meldung);

    /**
     * Gibt eine @param meldung speziell an den schwarzen Spieler.
     */
    public void setSpielerMeldungSchwarz(String meldung);

    /**
     *
     * Sagt der Oberflaeche ob es grafisch eine @param undoMoeglich keit 
     * erlauben soll.
     */
    public void setUndoErlaubt(boolean undoMoeglich);

    /**
     * 
     * Sagt der Oberflaeche ob es grafisch eine @param redoMoeglich keit
     * erlauben soll
     */
    public void setRedoErlaubt(boolean redoMoeglich);

    /**
     * Gibt auf der GUI eine Fehlermeldung aus. Dies ist die Standardausgabe-
     * möglichkeit der Steuerung für Fehler. Es empfiehlt sich zur Ausgabe der Fehler
     * ein Popup zu verwenden.
     * @param fehlertext Auszugebender Fehlertext
     */
    public void gibFehlermeldungAus(String fehlertext);

    /**
     * Mit dem Parameter @param visible kann die Oberflaeche ein- oder
     * ausgeblendet werden. Achtung! Countdowns etc. können gegebenenfalls
     * trotzdem noch laufen! Dies muss die Steuerung selbst abfangen.
     */
    public void setVisible(boolean visible);

    /**
     * Der Oberflaeche kann über @param setPause ein Tipp gegeben werden, ob
     * sie eine Art Pause- oder Blackscreen anzeigen möchte/sollte.
     */
    public void setPauseScreen(boolean setPause);

    /**
     * Die Schaltflaechen auf der Oberflaeche fuer Passen und Aufgeben werden
     * je nach Spielstatus freigegeben
     * @param visible Wird Passen und Aufgeben freigeschaltet?
     */
    public void visiblePassen(boolean visible);

    /**
     * Ob die Schaltfläche "Aufgeben" zur Verfügung, bzw. sichtbar sein soll,
     * kann über den Parameter @param visible eingestellt werden.
     */
    public void visibleAufgeben(boolean visible);

    /**
     * Wenn man in der Auswertung des Spieles ist, muss die Schaltflaeche um
     * die Auswertung zu beenden freigegeben sein. Sonst nicht!
     * @param visible wird auswertungBeenden freigeschaltet?
     */
    public void visibleAuswertungBeenden(boolean visible);

    /**
     * Ob die Schaltfläche "Neues Spiel" zur Verfügung, bzw. sichtbar sein soll,
     * kann über den Parameter @param visible eingestellt werden.
     */
    public void visibleNeuesSpiel(boolean visible);

    /**
     * Ob die Schaltfläche "Spiel Laden" zur Verfügung, bzw. sichtbar sein soll.
     * kann über den Parameter @param visible eingestellt werden.
     */
    public void visibleSpielLaden(boolean visible);

    /**
     * Ob die Schaltfläche "Spiel speichern" zur Verfügung, bzw. sichtbar sein soll,
     * kann über den Parameter @param visible eingestellt werden.
     */
    public void visibleSpielSpeichern(boolean visible);

    /**
     * Ob die Schaltfläche "Pause" zur Verfügung, bzw. sichtbar sein soll,
     * kann über den Parameter @param visible eingestellt werden.
     * @see visibleFortsetzen
     */
    public void visiblePause(boolean visible);

    /**
     * Ob die Schaltfläche "Fortsetzen" zur Verfügung, bzw. sichtbar sein soll,
     * kann über den Parameter @param visible eingestellt werden.
     * Die Schaltfläche Fortsetzen das Gegenstück zur Schaltfläche "Pause".
     * Es empfielt sich immer nur eine von beiden sichbar zu lassen.
     * @see visiblePause
     */
    public void visibleFortsetzen(boolean visible);

    /**
     * Möglichkeit den Umschaltzeitpunkt von Spieler-Zeit zu Periodenzeit
     * mitzubekommen. Die Oberfläche bekommt über diese beiden Parameter
     * signalisiert, in welchem Zeit-Spielzustand sich der schwarze Spieler
     * gerade befindet.
     * @param periodeAktiviert
     */
    public void schwarzInPeriodenZeit(boolean periodeAktiviert);

    /**
     * Möglichkeit den Umschaltzeitpunkt von Spieler-Zeit zu Periodenzeit
     * mitzubekommen. Die Oberfläche bekommt über diese beiden Parameter
     * signalisiert, in welchem Zeit-Spielzustand sich der weisse Spieler
     * gerade befindet.
     * @param periodeAktiviert
     */
    public void weissInPeriodenZeit(boolean periodeAktiviert);

}
