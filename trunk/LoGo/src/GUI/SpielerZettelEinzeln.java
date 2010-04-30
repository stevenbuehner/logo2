package GUI;

import Interfaces.SpielerZettel;
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
    private int ZeilenAbstand;

    /* die einzelnen Textvariablen*/
    private String anzeigeText_spielername;
    private String anzeigeText_gefangene;
    private String anzeigeText_fehlermeldung;

    /* übergebene Variablen*/
    private String spielername;
    private int Anzahl;
    private String fehlermeldung;

    public SpielerZettelEinzeln(int xPos, int yPos, double offsetWinkel, String startText) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.OwinkelInRad = Math.toRadians(offsetWinkel);
        this.anzeigeText_spielername = startText;
    }

    public void StringBau(String spielername, int Anzahl, String fehlermeldung) {
        this.spielername = spielername;
        this.Anzahl = Anzahl;
        this.fehlermeldung = fehlermeldung;
        anzeigeText_spielername = "Spieler:  " + spielername;
        anzeigeText_gefangene = "Anzahl Gefangene:  " + Anzahl;
        anzeigeText_fehlermeldung = fehlermeldung;
        ZeilenAbstand = 50;
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
    public void paint(Graphics g) {
        this.paintComponents(g);
    }

    @Override
    public void paintComponents(Graphics g) {
        this.zeichneDich(g);
    }

    public void zeichneDich(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        AffineTransform at = AffineTransform.getRotateInstance(
                this.OwinkelInRad, xPos, yPos);
        g2.setTransform(at);
        if (this.anzeigeText_spielername == null) {
            this.anzeigeText_spielername = "";
        }
        if (this.anzeigeText_gefangene == null) {
            this.anzeigeText_gefangene = "";
        }
        if (this.anzeigeText_fehlermeldung == null) {
            this.anzeigeText_fehlermeldung = "";
        }
        g2.drawString(this.anzeigeText_spielername, xPos, yPos);
        xPos = xPos + ZeilenAbstand;
        yPos = yPos + ZeilenAbstand;
        g2.drawString(this.anzeigeText_gefangene, xPos, yPos);
        xPos = xPos + ZeilenAbstand;
        yPos = yPos + ZeilenAbstand;
        g2.drawString(this.anzeigeText_fehlermeldung, xPos, yPos);

        at = AffineTransform.getRotateInstance(
                0,
                0,
                0);
        g2.setTransform(at);
        g = (Graphics) g2;
    }
}
