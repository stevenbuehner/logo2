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
    private int xPos;
    private int yPos;
    private double OwinkelInRad;

    /* weitere Variablen*/
    private String anzeigeText;

    /* übergebene Variablen*/
    private String spielername;
    private int Anzahl;
    private String fehlermeldung;



    public SpielerZettelEinzeln(int xPos, int yPos, double offsetWinkel, String startText) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.OwinkelInRad = Math.toRadians(offsetWinkel);
        this.anzeigeText = startText;
    }

    public void StringBau(String spielername, int Anzahl, String fehlermeldung) {
        this.spielername = spielername;
        this.Anzahl = Anzahl;
        this.fehlermeldung = fehlermeldung;
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
                this.OwinkelInRad, xPos, yPos);
       g2.setTransform(at);
       if(this.anzeigeText == null){
           this.anzeigeText="";
       }
       g2.drawString(this.anzeigeText, xPos, yPos);

       at = AffineTransform.getRotateInstance(
                0,
                0,
                0);
       g2.setTransform(at);
       g = (Graphics) g2;
    }

}
