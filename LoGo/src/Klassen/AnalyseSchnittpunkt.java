/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Klassen;

/**
 *
 * @author tommy
 * Diese Klasse dient nur dazu eine Spielstellung zu analysieren. Durch bestimmte
 * Eigenschaften kann man markieren, ob man den entsprechenden Schnittpunkt
 * beispielsweise schon in ein Liste aufgenommen hat.
 */
public class AnalyseSchnittpunkt {
    private int belegungswert;
    private boolean markiert;
    private boolean analysiert;

    /**
     * Es gibt 4 Konstruktoren, je nachdem, wie man den AnalyseSchnittpunkt
     * erstellen will. Gibt man einen Parameter nicht an, wird er somit als Leer
     * (Wenn es ein Feld ist) oder als False (Bei einem Boolean) angelegt. Somit
     * definiert der Konstruktor alle Werte.
     */
    public AnalyseSchnittpunkt(){
        this.setBelegungswert(Konstante.SCHNITTPUNKT_LEER);
        this.setMarkiert(false);
        this.setAnalysiert(false);
    }

    public AnalyseSchnittpunkt(int belegungswert){
        this.setBelegungswert(belegungswert);
        this.setMarkiert(false);
        this.setAnalysiert(false);
    }

    public AnalyseSchnittpunkt(int belegungswert, boolean markiert){
        this.setBelegungswert(belegungswert);
        this.setMarkiert(markiert);
        this.setAnalysiert(false);
    }

    public AnalyseSchnittpunkt(int belegungswert, boolean markiert, boolean analysiert){
        this.setBelegungswert(belegungswert);
        this.setMarkiert(markiert);
        this.setAnalysiert(analysiert);
    }

    public void setBelegungswert(int belegungswert){
        this.belegungswert = belegungswert;
    }

    public int getBelegungswert(){
        return this.belegungswert;
    }

    public void setMarkiert(boolean markiert){
        this.markiert = markiert;
    }

    public boolean getMarkiert(){
        return this.markiert;
    }

    /**
     *
     * @param analysiert Stellt ein ob der Schnittpunkt analysiert ist. Dient also
     * als Markierung.
     */
    public void setAnalysiert(boolean analysiert){
        this.analysiert = analysiert;
    }

    /**
     *
     * @return Gibt den Status zurueck, ob der Schnittpunkt schon Analysiert wurde.
     */
    public boolean getAnalysiert(){
        return this.analysiert;
    }
}
