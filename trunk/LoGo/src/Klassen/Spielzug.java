/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Klassen;

/**
 * Speichert alle wesentlichen Daten zu einem Spielzug
 * @author steven
 * @version 0.1
 */
public class Spielzug {

    private int xPosition;
    private int yPosition;
    private int farbe;

    /**
     * Konstruktur der Klasse
     * @param xPos X-Position des Spielzuges
     * @param yPos Y-Position des Spielzuges
     * @param farbe Spielerfarbe des Steins der gelegt wurde. zur Auswahl stehen
     * NUR die Konstanten Konstante.SCHNITTPUNKT_SCHWARZ und
     * Konstante.SCHNITTPUNKT_WEISS. Wurde ein anderer Wert uebergeben wird
     * automatisch der Wert Konstante.SCHNITTPUNKT_LEER gesetzt.
     */
    public Spielzug(int xPos, int yPos, int farbe) {
        this.setXPosition(xPos);
        this.setYPosition(yPos);
        this.setFarbe(farbe);
    }

    /**
     *
     * @return X-Position des Spielerzuges
     */
    public int getXPosition() {
        return xPosition;
    }

    /**
     *
     * @param val X-Position des Spielerzuges
     */
    public void setXPosition(int val) {
        this.xPosition = val;
    }

    /**
     *
     * @return Y-Position des Spielzuges
     */
    public int getYPosition() {
        return yPosition;
    }

    /**
     *
     * @param val Y-Position des Spielzuges
     */
    public void setYPosition(int val) {
        this.yPosition = val;
    }

    /**
     * @return Spielerfarbe des Steins der gelegt wurde. zur Auswahl stehen
     * NUR die Konstanten Konstante.SCHNITTPUNKT_SCHWARZ und
     * Konstante.SCHNITTPUNKT_WEISS. Wurde ein anderer Wert uebergeben wird
     * automatisch der Wert Konstante.SCHNITTPUNKT_LEER gesetzt.
    */
    public int getFarbe() {
        return this.farbe;
    }
    
    /**
     * @param farbe Spielerfarbe des Steins der gelegt wurde. zur Auswahl stehen
     * NUR die Konstanten Konstante.SCHNITTPUNKT_SCHWARZ und
     * Konstante.SCHNITTPUNKT_WEISS. Wurde ein anderer Wert uebergeben wird
     * automatisch der Wert Konstante.SCHNITTPUNKT_LEER gesetzt.
    */
    public void setFarbe(int farbe) {
        if (farbe == Konstante.SCHNITTPUNKT_SCHWARZ ||
                farbe == Konstante.SCHNITTPUNKT_WEISS) {
            this.farbe = farbe;
        } else {
            this.farbe = Konstante.SCHNITTPUNKT_LEER;
        }
    }
}
