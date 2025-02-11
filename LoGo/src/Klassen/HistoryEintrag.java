/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Klassen;

import java.sql.Date;

/**
 * Objekte dieser Klasse enthalten alle relevanten Informationen für einen
 * Historyeintrag in der Datenbank.
 * @author steven
 */
public class HistoryEintrag {

    private String nameSpielerSchwarz;
    private String nameSpielerWeiss;
    private float punkteSpielerSchwarz;
    private float punkteSpielerWeiss;
    private Date zeitpunkt;

    /**
     * Standardkonstruktor, der alle Objekte initialisiert.
     */
    public HistoryEintrag(){
        this.nameSpielerSchwarz = "";
        this.nameSpielerWeiss = "";
        this.punkteSpielerSchwarz = 0;
        this.punkteSpielerWeiss = 0;
        this.zeitpunkt = null;
    }

    /**
     * Getter für den Namen von Spieler Schwarz
     * @return Name des schwarzen Spielers
     */
    public String getNameSpielerSchwarz() {
        return this.nameSpielerSchwarz;
    }

    /**
     * Setter für @param name von Spieler Schwarz.
     * @param name Name des spielers
     */
    public void setNameSpielerSchwarz(String name) {
        this.nameSpielerSchwarz = name;
    }

    /**
     * Getter für die Punkte des Spieler Schwarz.
     * @return Anzahl der Punkte des schwarzen Spielers
     */
    public float getPunkteSpielerSchwarz() {
        return this.punkteSpielerSchwarz;
    }

    /**
     * Setter für die @param punkte des Spielers Schwar.
     * @param punkte Anzahl der Punkte
     */
    public void setPunkteSpielerSchwarz(float punkte) {
        this.punkteSpielerSchwarz = punkte;
    }

    /**
     * Getter für den Namen von Spieler Weiss
     * @return Name des weißen Spielers
     */
    public String getNameSpielerWeiss() {
        return this.nameSpielerWeiss;
    }

    /**
     * Setter für @param name von Spieler Weiss.
     * @param name
     */
    public void setNameSpielerWeiss(String name) {
        this.nameSpielerWeiss = name;
    }

    /**
     * Getter für die Punkte des Spieler Weiss.
     * @return Anzahl der Punkte des weißen Spielers
     */
    public float getPunkteSpielerWeiss() {
        return this.punkteSpielerWeiss;
    }

    /**
     * Setter für die @param punkte des Spielers Schwar.
     * @param punkte  Anzahl der Punkte
     */
    public void setPunkteSpielerWeiss(float punkte) {
        this.punkteSpielerWeiss = punkte;
    }

    /**
     * Eintrag, ob schwarz gewonnen hat.
     * @return Hat schwarz gewonnen
     */
    public boolean hatSchwarzGewonnen() {
        if (this.punkteSpielerSchwarz > this.punkteSpielerWeiss){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Eintrag, ob weiss gewonnen hat
     * @return Hat weiss gewonnen
     */
    public boolean hatWeissGewonnen() {
        return !this.hatSchwarzGewonnen();
    }

    /**
     * Datum des Eintrages Setzen
     * @param datum
     */
    public void setDatum( Date datum ){
        this.zeitpunkt = datum;
    }

    /**
     * Getter, Datum des Eintrages
     * @return Datum
     */
    public Date getDatum(){
        return this.zeitpunkt;
    }

}

