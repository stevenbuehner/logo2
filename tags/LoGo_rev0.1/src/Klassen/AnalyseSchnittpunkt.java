/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Klassen;

/**
 * @author tommy
 * Diese Klasse dient nur dazu eine Spielstellung zu analysieren. Durch bestimmte
 * Eigenschaften kann man markieren, ob man den entsprechenden Schnittpunkt
 * beispielsweise schon in ein Liste aufgenommen hat.
 */
public class AnalyseSchnittpunkt {
    private int xPos;
    private int yPos;
    private int belegungswert;
    private int steinStatus;
    private boolean markiert;
    private boolean analysiert;

    public AnalyseSchnittpunkt(){
        this.setXPos(-1);
        this.setYPos(-1);
        this.setSteinStatus(Konstante.STEIN_UNGEWISS);
        this.setAnalysiert(false);
        this.setMarkiert(false);
        this.setBelegungswert(Konstante.SCHNITTPUNKT_LEER);
    }

    public AnalyseSchnittpunkt(int xPos, int yPos){
        this.setXPos(xPos);
        this.setYPos(yPos);
        this.setBelegungswert(Konstante.SCHNITTPUNKT_LEER);
        this.setSteinStatus(Konstante.STEIN_UNGEWISS);
        this.setMarkiert(false);
        this.setAnalysiert(false);
    }

    public AnalyseSchnittpunkt(int xPos, int yPos, int belegungswert){
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
    public void setBelegungswert(int belegungswert){
        this.belegungswert = belegungswert;
    }

    /**
     * @return Gibt den aktuellen Belegungswert zurueck. Das sind Werte (int) die
     * man mit der Klasse Konstante vergleichen kann.
     */
    public int getBelegungswert(){
        return this.belegungswert;
    }

    /**
     * @param markiert Setzt den markiert (boolean) wert. Dient also als Flag.
     */
    public void setMarkiert(boolean markiert){
        this.markiert = markiert;
    }

    /**
     * @return Gibt den Status zurueck, ob der Schnittpunkt markiert wurde.
     */
    public boolean getMarkiert(){
        return this.markiert;
    }

    /**
     *
     * @param analysiert Stellt ein ob der Schnittpunkt analysiert ist. Dient also
     * als Flag.
     */
    public void setAnalysiert(boolean analysiert){
        this.analysiert = analysiert;
    }

    /*
     * @return Gibt den Status zurueck, ob der Schnittpunkt schon Analysiert wurde.
     */
    public boolean getAnalysiert(){
        return this.analysiert;
    }

    /**
     * @param steinStatus Setzt den Status (Gefangenenstatus) des Schnittpunktes.
     * Wurde der Stein gefangen ist es SteinGefangen, ist er lebendig dann SteinLebendig
     * und ist es ungewiss dann SteinUngewiss.
     */
    public void setSteinStatus(int steinStatus){
        this.steinStatus = steinStatus;
    }

    /**
     * @return getSteinStatus gibt den Gefangenenstatus des Schittpunktes wieder.
     */
    public int getSteinStatus(){
        return this.steinStatus;
    }

    /**
     * @param xPos Setzt die X-Koordinate des Schnittpunkts
     */
    public void setXPos(int xPos){
        this.xPos = xPos;
    }

    /**
     * @return Gibt die X-Koordinate des Schnittpunkts wieder.
     */
    public int getXPos(){
        return this.xPos;
    }

    /**
     * @param yPos Setzt die Y-Koordinate des Schnittpunkts
     */
    public void setYPos(int yPos){
        this.yPos = yPos;
    }

    /**
     * @return Gibt die Y-Koordinate des Schnittpunkts wieder.
     */
    public int getYPos(){
        return this.yPos;
    }
}
