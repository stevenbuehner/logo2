/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import interfaces.SpielerZettel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import javax.swing.JComponent;

/**
 *
 * @author Alex
 */
public class SpielerZettelEinzeln extends JComponent implements SpielerZettel {

    /* Wichtige Variablen zum Zeichnen. Geben an, um welchen
     * Punkt der Zettel gedreht wird und wo die Zettel liegen*/
    private int xMittelPos;
    private int yMittelPos;
    private double OwinkelInRad;

    /* Variable für den Inhalt*/
    private String anzeigeInhalt;

    /* weitere Variablen*/
    private String anzeigeText;

    /* übergebene Variablen*/
    private String spielername;
    private int Anzahl;
    private String fehlermeldung;
    private String startText;



    public SpielerZettelEinzeln(int xPos, int yPos, double offsetWinkel, String startText) {
        this.xMittelPos = xPos;
        this.yMittelPos = yPos;
        this.OwinkelInRad = Math.toRadians(offsetWinkel);
        this.startText = startText;
    }

    public void StringBau(String spielername, int Anzahl, String fehlermeldung) {
        this.spielername = spielername;
        this.Anzahl = Anzahl;
        this.fehlermeldung = fehlermeldung;
        anzeigeText = startText;
        anzeigeText += "Spieler:  " + spielername + "\n";
        anzeigeText += "Anzahl Gefangene:  " + Anzahl + "\n";
        anzeigeText += fehlermeldung;
    }

    public void setSpielername(String spielername) {
        this.StringBau(spielername, this.Anzahl, this.fehlermeldung);
    }

    public void setGefangenenAnzahl(int anzahl) {
        this.StringBau(this.spielername, Anzahl, this.fehlermeldung);
    }

    public void setFehlermeldung(String fehlermeldung) {
        this.StringBau(this.spielername, this.Anzahl, fehlermeldung);
    }

    @Override
    public void paint(Graphics g){
        this.paintComponents(g);
    }

    @Override
    public void paintComponents(Graphics g){
        this.zeichneDich(g);
    }

    public void zeichneDich(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        AffineTransform at = AffineTransform.getRotateInstance(
                -Math.toRadians(OwinkelInRad));
       g2.setTransform(at);
 //      g2.drawString(String anzeigeText, int xPos, int yPos);
        
    }

}
