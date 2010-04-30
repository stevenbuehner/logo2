/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Klassen;

import java.sql.Date;

/**
 *
 * @author steven
 */
public class HistoryEintrag {

    private String nameSpielerSchwarz;
    private String nameSpielerWeiss;
    private float punkteSpielerSchwarz;
    private float punkteSpielerWeiss;
    private Date zeitpunkt;

    /**
     * Getter für den Namen von Spieler Schwarz
     * @return
     */
    public String getNameSpielerSchwarz() {
        return this.nameSpielerSchwarz;
    }

    /**
     * Setter für @param name von Spieler Schwarz.
     */
    public void setNameSpielerSchwarz(String name) {
        this.nameSpielerSchwarz = name;
    }

    /**
     * Getter für die Punkte des Spieler Schwarz.
     * @return
     */
    public float getPunkteSpielerSchwarz() {
        return this.punkteSpielerSchwarz;
    }

    /**
     * Setter für die @param punkte des Spielers Schwar.
     */
    public void setPunkteSpielerSchwarz(float punkte) {
        this.punkteSpielerSchwarz = punkte;
    }

    /**
     * Getter für den Namen von Spieler Weiss
     * @return
     */
    public String getNameSpielerWeiss() {
        return this.nameSpielerWeiss;
    }

    /**
     * Setter für @param name von Spieler Weiss.
     */
    public void setNameSpielerWeiss(String name) {
        this.nameSpielerWeiss = name;
    }

    /**
     * Getter für die Punkte des Spieler Weiss.
     * @return
     */
    public float getPunkteSpielerWeiss() {
        return this.punkteSpielerWeiss;
    }

    /**
     * Setter für die @param punkte des Spielers Schwar.
     */
    public void setPunkteSpielerWeiss(float punkte) {
        this.punkteSpielerWeiss = punkte;
    }

    public boolean hatSchwarzGewonnen() {
        if (this.punkteSpielerSchwarz > this.punkteSpielerWeiss){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean hatWeissGewonnen() {
        return !this.hatSchwarzGewonnen();
    }

    public void setDatum( Date datum ){
        this.zeitpunkt = datum;
    }

    public Date getDatum(){
        return this.zeitpunkt;
    }

}

