package GUI;

import Interfaces.SpielerZettel;
import Klassen.Konstante;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
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
    private int zeilenAbstandOben = 30;
    private int zeilenAbstandUnten = 20;
    private int schriftGroesseOben = 16;
    private int schriftGroesseUnten = 14;
    private int maxBuchstProZeile = 25;

    /* die einzelnen Textvariablen*/
    private String anzeigeText_spielername;
    private String anzeigeText_gefangene;
    private String spielerFarbe;
    private List<String> infoTextTeile;


    /* übergebene Variablen*/
    private String spielername;
    private int anzahlGefangene;

    /* Wenn der Spieler in Periodenzeit ist, wird dies Angezeigt */
    private boolean spielerInPeriodenZeit;

    public SpielerZettelEinzeln(int xPos, int yPos, double offsetWinkel, String spielerName, int spielerFarbe) {
        if(spielerFarbe == Konstante.SCHNITTPUNKT_SCHWARZ){
            this.spielerFarbe = "(Schwarz)";
        }else{
            this.spielerFarbe = "(Weiß)";
        }
        this.xPos = xPos;
        this.yPos = yPos;
        this.OwinkelInRad = Math.toRadians(offsetWinkel);
        this.anzeigeText_spielername = spielerName;
        this.infoTextTeile = new ArrayList<String>();
        this.anzahlGefangene = 0;
        this.spielerInPeriodenZeit = false;

    }

    public void ObererZettelTeil(String spielername, int anzahlGefangene) {
        this.spielername = spielername;
        this.anzahlGefangene = anzahlGefangene;

        anzeigeText_spielername =this.spielername + " " + this.spielerFarbe;
        anzeigeText_gefangene = this.anzahlGefangene + " Steine gefangen";
    }


    public void setSpielername(String spielername) {
        this.ObererZettelTeil(spielername, this.anzahlGefangene);
    }

    public void setGefangenenAnzahl(int anzahl) {
        this.ObererZettelTeil(this.spielername, anzahl);
    }

    public void setInfoBox(String infoIn) {
        this.infoTextTeile = new ArrayList<String>();
        if(infoIn.length() <= this.maxBuchstProZeile){
            this.infoTextTeile.add(infoIn);
            return;
        }
        String uebrigerText = infoIn;
        String textAusschnitt;
        int abgearbeitetBis = 0;
        while(uebrigerText.length() > maxBuchstProZeile){
            textAusschnitt = uebrigerText.substring(0, this.maxBuchstProZeile);
            if(textAusschnitt.lastIndexOf(' ') > 0){
                this.infoTextTeile.add(textAusschnitt.substring(0,textAusschnitt.lastIndexOf(' ')));
                uebrigerText = uebrigerText.substring(textAusschnitt.lastIndexOf(' ')+1);
            }
            else {
                this.infoTextTeile.add(textAusschnitt);
                uebrigerText = uebrigerText.substring(this.maxBuchstProZeile);
            }
        }
        /* Noch den Rest anhaengen */
        this.infoTextTeile.add(uebrigerText);

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
        /* Oberen Teil Zeichnen */
        Font myFontOben = new Font("TimesRoman", 1, this.schriftGroesseOben);
        g2.setFont(myFontOben);

        if (this.anzeigeText_spielername == null) {
            this.anzeigeText_spielername = "";
        }
        if (this.anzeigeText_gefangene == null) {
            this.anzeigeText_gefangene = "";
        }

        g2.drawString(this.anzeigeText_spielername, xPos, yPos);
        g2.drawString(this.anzeigeText_gefangene, xPos, yPos + this.zeilenAbstandOben);
        
        /* Nun der untere Teil */
        int offsetTextY;
        int startY = yPos + 2 * this.zeilenAbstandOben;
        Font myFontUnten = new Font("TimesRoman", 1, this.schriftGroesseUnten);
        g2.setFont(myFontUnten);
        if(this.getInPeriodenZeit() == true){
            g2.drawString("Byo-Yomi aktiv", xPos, startY);
            offsetTextY = this.zeilenAbstandUnten;
        }
        else {
            offsetTextY = 0;
        }
        if(this.infoTextTeile.size()>0){
            for(int i=0; i<this.infoTextTeile.size(); i++){
                g2.drawString(this.infoTextTeile.get(i), xPos, startY + i*this.zeilenAbstandUnten + offsetTextY);
            }
        }


        at = AffineTransform.getRotateInstance(
                0,
                0,
                0);
        g2.setTransform(at);
        g = (Graphics) g2;
    }

    public void setInPeriodenZeit(boolean b) {
        this.spielerInPeriodenZeit = b;
    }

    public boolean getInPeriodenZeit() {
        return this.spielerInPeriodenZeit;
    }
}
