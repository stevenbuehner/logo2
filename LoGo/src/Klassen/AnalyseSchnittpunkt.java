/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Klassen;

/**
 * Diese Klasse dient nur dazu eine Spielstellung zu analysieren. Durch bestimmte
 * Eigenschaften kann man markieren, ob man den entsprechenden Schnittpunkt
 * beispielsweise schon in ein Liste aufgenommen hat.
 * @author tommy
 */
public class AnalyseSchnittpunkt {

    private int xPos;
    private int yPos;
    private int belegungswert;
    private int steinStatus;
    private boolean markiert;
    private boolean analysiert;

    /**
     * Der Analyseschnittpunkt dient als Datenstruktur zur Auswertung von Brettstellungen.
     * In ihm kann man die Koordinaten eines Steines speichern, sowie Flags setzen
     * uns sich seinen Belegungswert merken. Wird kein Belegungswert und keine
     * Koordinate im Konstruktor angegeben, so liegen die Koordinaten auf (-1,-1)
     * und der Belegungswert ist leer
     */
    public AnalyseSchnittpunkt() {
        this.setXPos(-1);
        this.setYPos(-1);
        this.setSteinStatus(Konstante.STEIN_UNGEWISS);
        this.setMarkiert(false);
        this.setAnalysiert(false);
        this.setBelegungswert(Konstante.SCHNITTPUNKT_LEER);
    }

    /**
     * Wie der Standardkonstruktor, nur kann man hier noch die Koordinate des
     * Punktes angeben. Der Belegungswert ist immer noch leer
     * @param xPos X-Koordinate (von 0 oder 1 beginnend, kann beliebig sein)
     * @param yPos Y-Koordinate (von 0 oder 1 beginnend, kann beliebig sein)
     * @see AnalyseSchnittpunkt
     */
    public AnalyseSchnittpunkt(int xPos, int yPos) {
        this.setXPos(xPos);
        this.setYPos(yPos);
        this.setBelegungswert(Konstante.SCHNITTPUNKT_LEER);
        this.setSteinStatus(Konstante.STEIN_UNGEWISS);
        this.setMarkiert(false);
        this.setAnalysiert(false);
    }

    /**
     * Zusatzlich zum Standardkonstruktor kann man noch Koordinaten und Belegungswert
     * angeben.
     * @param xPos (von 0 oder 1 beginnend, kann beliebig sein)
     * @param yPos (von 0 oder 1 beginnend, kann beliebig sein)
     * @param belegungswert Was liegt auf dem Schnittpunkt (Farbe, Gebiet, Ko, Leer)
     * @see AnalyseSchnittpunkt
     */
    public AnalyseSchnittpunkt(int xPos, int yPos, int belegungswert) {
        this.setXPos(xPos);
        this.setYPos(yPos);
        this.setBelegungswert(belegungswert);
        this.setSteinStatus(Konstante.STEIN_UNGEWISS);
        this.setMarkiert(false);
        this.setAnalysiert(false);
    }

    /**
     * @param belegungswert Setzt den aktuellen Belegungswert. Dabei ist ein Wert
     * der Klasse Konstante zu waehlen.
     */
    public void setBelegungswert(int belegungswert) {
        this.belegungswert = belegungswert;
    }

    /**
     * @return Gibt den aktuellen Belegungswert zurueck. Das sind Werte (int) die
     * man mit der Klasse Konstante vergleichen kann.
     */
    public int getBelegungswert() {
        return this.belegungswert;
    }

    /**
     * @param markiert Setzt den markiert (boolean) wert. Dient also als Flag.
     */
    public void setMarkiert(boolean markiert) {
        this.markiert = markiert;
    }

    /**
     * @return Gibt den Status zurueck, ob der Schnittpunkt markiert wurde.
     */
    public boolean getMarkiert() {
        return this.markiert;
    }

    /**
     * Setzt den Schnittpunkt als analysiert, oder nicht analysiert. Dient also
     * als Flag bei Suchalgorithmen
     * @param analysiert Wurde der Schnittpunkt schon analysiert?
     */
    public void setAnalysiert(boolean analysiert) {
        this.analysiert = analysiert;
    }

    /**
     * @return Status, ob Schnittpunkt schon analysiert wurde.
     */
    public boolean getAnalysiert() {
        return this.analysiert;
    }

    /**
     * @param steinStatus Setzt den Status (Gefangenenstatus) des Schnittpunktes.
     * Wurde der Stein gefangen ist es SteinGefangen, ist er lebendig dann SteinLebendig
     * und ist es ungewiss dann SteinUngewiss.
     */
    public void setSteinStatus(int steinStatus) {
        this.steinStatus = steinStatus;
    }

    /**
     * @return getSteinStatus gibt den Gefangenenstatus des Schittpunktes wieder.
     */
    public int getSteinStatus() {
        return this.steinStatus;
    }

    /**
     * @param xPos Setzt die X-Koordinate des Schnittpunkts
     */
    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    /**
     * @return Gibt die X-Koordinate des Schnittpunkts wieder.
     */
    public int getXPos() {
        return this.xPos;
    }

    /**
     * @param yPos Setzt die Y-Koordinate des Schnittpunkts
     */
    public void setYPos(int yPos) {
        this.yPos = yPos;
    }

    /**
     * @return Gibt die Y-Koordinate des Schnittpunkts wieder.
     */
    public int getYPos() {
        return this.yPos;
    }
}
