package GUI;

import interfaces.SpielerUhren;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

/**
 *
 * @author tommy
 * @version 0.2
 */
public class SpielerUhr extends JComponent implements SpielerUhren{
    /* Wichtige Variablen zum Zeichnen. Geben an, um welchen
     * Punkt der Zeiger gedreht wird, Offsetwinkel da Uhr schief liegen kann */
    private int xMittelPos;
    private int yMittelPos;
    private double OwinkelInRad;

    /* Zeit, die angezeigt werden soll */
    private long anzeigeZeit;

    private boolean istAktiv;

    /* Wert, bei der die Uhr anfangen soll zu signalisieren, dass
     * die Zeit kritisch ist */
    private long kritischeZeitInMS;


    /* Verschiedene Bilder fuer die Zeiger */
    BufferedImage sekundenZeigerBImage;
    BufferedImage minutenZeigerBImage;
    BufferedImage stundenZeigerBImage;

    /* Zum drehen der Bilder */
  //  AffineTransform at;

    int sekZeigerXrotation;
    int sekZeigerYrotation;


    public SpielerUhr(int xPos, int yPos, long anfangsZeit, double offsetWinkel) {
        this.xMittelPos = xPos;
        this.yMittelPos = yPos;
        this.anzeigeZeit = anfangsZeit;
        this.istAktiv = false;
        this.OwinkelInRad =  Math.toRadians(offsetWinkel);
        this.kritischeZeitInMS = 30000;

        GrafikLib lib = GrafikLib.getInstance();

        /* Bilder laden und nicht skalieren (sind schon skaliert) */
       // this.sekundenZeigerBImage =  lib.getSprite("GUI/resources/ZeigerBearb4.png");
        this.sekundenZeigerBImage =  lib.getSprite("GUI/resources/sekZeiger1.png");
        this.minutenZeigerBImage = lib.getSprite("GUI/resources/ZeigerBearb4.png");
        this.stundenZeigerBImage = lib.getSprite("GUI/resources/StZeigerEinfach1.png");




    }

    /**
     * Um die Uhr zu veraendern, muss man die Zeit verstellen.
     * @param zeit Neue Zeit in millisekunden.
     */
    public void restzeitInMS(long zeit) {
        this.anzeigeZeit = zeit;
    }

    /**
     * Es soll gekennzeichnet werden, ob die Uhr gerade aktiv ist.
     * @param istAktiv Flag ob aktiv.
     */
    public void uhrAktiv(boolean istAktiv) {
        this.istAktiv = istAktiv;
    }

    /**
     * Gibt den Winkel des Sekundenzeigers wieder, in 60 Intervalle unterteilt.
     * @return Winkel Sekundenzeiger
     */
    private int getSekundenPosInGrad(){
        /* Da die Zeit in Milisekunden gespeichert wird, muss sie umgerechnet
         * werden
         */
        long verbleibendeSekunden = this.anzeigeZeit / 1000;
        verbleibendeSekunden = verbleibendeSekunden % 60;

        int rueckgabeWinkel = 6 * (int) verbleibendeSekunden;
        return rueckgabeWinkel;
    }

    /**
     * Gibt den Winkel des Minutenzeigers wieder, in 60 Intervalle unterteilt.
     * @return Winkel Minutenzeiger
     */
    private int getMinutenPosInGrad(){
        /* Umrechnen der Zeit */
        long verbleibendeMinuten = (this.anzeigeZeit / 1000) / 60;
        verbleibendeMinuten = verbleibendeMinuten % 60;

        int rueckgabeWinkel = 6 * (int) verbleibendeMinuten;
        return rueckgabeWinkel;
    }

    /**
     * Gibt den Winkel des Stundenzeigers wieder. Dieser ist nicht in Intervalle
     * unterteilt, und daher ein double.
     * @return Winkel des Stundenzeigers in Grad
     */
    private double getStundenPosInGrad(){
        double verbleibendeStunden = (double)this.anzeigeZeit / 1000/3600;
        double rueckgabeWinkel = 30 * verbleibendeStunden;
        return rueckgabeWinkel;
    }

    @Override
    public void paint(Graphics g){
        this.paintComponents(g);
    }

    @Override
    public void paintComponents(Graphics g){
        this.zeichneZeiger(g);
    }

    public void zeichneZeiger(Graphics g) {
 Graphics2D g2 = (Graphics2D) g;
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      /* Sekundenzeiger zeichnen */
      AffineTransform at = AffineTransform.getRotateInstance(
                -Math.toRadians(this.getSekundenPosInGrad())+this.OwinkelInRad,
                xMittelPos,
                yMittelPos);
       g2.setTransform(at);
       BufferedImage rotatedImage =  this.sekundenZeigerBImage;
       g2.drawImage(rotatedImage, xMittelPos-(this.sekundenZeigerBImage.getWidth()/2), yMittelPos-(this.sekundenZeigerBImage.getHeight()/2),null);

       /* Minutenzeiger zeichnen */
       at = AffineTransform.getRotateInstance(
                -Math.toRadians(this.getMinutenPosInGrad())+this.OwinkelInRad,
                xMittelPos,
                yMittelPos);
       g2.setTransform(at);
       rotatedImage =  this.minutenZeigerBImage;
       g2.drawImage(rotatedImage, xMittelPos-(this.minutenZeigerBImage.getWidth()/2), yMittelPos-(this.minutenZeigerBImage.getHeight()/2),null);

       /* Stundenzeiger zeichnen */
       at = AffineTransform.getRotateInstance(
                -Math.toRadians(this.getStundenPosInGrad())+this.OwinkelInRad - Math.PI,
                xMittelPos,
                yMittelPos);
       g2.setTransform(at);
       rotatedImage =  this.stundenZeigerBImage;
       g2.drawImage(rotatedImage, xMittelPos-(this.stundenZeigerBImage.getWidth()/2), yMittelPos-(this.stundenZeigerBImage.getHeight()/2),null);

       /* Transformation wieder loeschen */
       at = AffineTransform.getRotateInstance(
                0,
                0,
                0);
       g2.setTransform(at);
       g = (Graphics) g2;    }

}
