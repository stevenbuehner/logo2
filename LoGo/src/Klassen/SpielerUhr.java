/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Klassen;

import interfaces.SpielerUhren;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author tommy
 */
public class SpielerUhr implements SpielerUhren{
    private int xMittelPos;
    private int yMittelPos;
    private int radius;
    private long anzeigeZeit;
    private boolean istAktiv;

    /* Verschiedene Bilder fuer die Zeiger */
    BufferedImage BISekundenZeiger;
    BufferedImage BIMinutenZeiger;
    BufferedImage BIStundenZeiger;

    int sekZeigerXrotation;
    int sekZeigerYrotation;
    int minZeigerXrotation;
    int minZeigerYrotation;
    int stuZeigerXrotation;
    int stuZeigerYrotation;

    public SpielerUhr(int xPos, int yPos, int radius, long anfangsZeit) throws Exception{
        this.xMittelPos = xPos;
        this.yMittelPos = yPos;
        this.radius = radius;
        this.anzeigeZeit = anfangsZeit;
        this.istAktiv = false;

        /* Bilder laden und skalieren */
        this.BISekundenZeiger = ImageIO.read(new File("../GUI/resources/ZeigerGold.png"));
        /*this.BISekundenZeiger = (BufferedImage) this.BISekundenZeiger.getScaledInstance(
                (radius * this.BISekundenZeiger.getWidth()) /this.BISekundenZeiger.getHeight(),
                radius,
                yPos);*/
        this.BIMinutenZeiger  = ImageIO.read(new File("../GUI/resources/ZeigerGold.png"));
        /*this.BIMinutenZeiger = (BufferedImage) this.BIMinutenZeiger.getScaledInstance(
                (radius * this.BIMinutenZeiger.getWidth()) /this.BIMinutenZeiger.getHeight(),
                radius,
                yPos);*/
        this.BIStundenZeiger  = ImageIO.read(new File("../GUI/resources/ZeigerGold.png"));
        /*this.BIStundenZeiger = (BufferedImage) this.BIStundenZeiger.getScaledInstance(
                (radius * this.BIStundenZeiger.getWidth()) /this.BIStundenZeiger.getHeight(),
                radius,
                yPos);*/

        /* Rotationsachsen festlegen 27, 360?*/
        this.sekZeigerXrotation = 27;
        this.sekZeigerYrotation = 360;
        this.minZeigerXrotation = 27;
        this.minZeigerYrotation = 360;
        this.stuZeigerXrotation = 27;
        this.stuZeigerYrotation = 360;
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
     * unterteilt.
     * @return Winkel des Stundenzeigers in Grad
     */
    private int getStundenPosInGrad(){
        long verbleibendeStunden = (this.anzeigeZeit / 1000) / 3600;
        int rueckgabeWinkel = 30 * (int) verbleibendeStunden;
        return rueckgabeWinkel;
    }

    /**
     * Hier soll der Zeiger gezeichnet werden. Dafuer muss er eine Graphik bekommen,
     * wo man drauf zeichnen kann.
     * @param g Graphic zum Malen
     */
    public void zeichneZeiger(Graphics g){

    }

}
