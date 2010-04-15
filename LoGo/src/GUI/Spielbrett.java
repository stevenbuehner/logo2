package GUI;

import Klassen.Konstante;
import interfaces.Drawable;
import interfaces.Movable;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 *
 * @author steven
 * @version 0.1
 * Die Klasse wurde als Vorlage aus einem Forum entnommen:
 * http://www.java-forum.org/awt-swing-swt/95342-spielfeld-gitter-einzelne-zellen-veraendern.html#_
 *
 * Diese Klasse wird aktuell nirgends im Spiel verwedet!!!!
 */
public class Spielbrett extends Canvas implements Drawable {

    private int brettBreite;
    private int brettHoehe;
    private int xOffset;
    private int yOffset;
    private int anzahlFelder;
    private int[][] spielFeldArray;

    // Grafiken
    private Image backgoundImage;
    private SpielStein[][] feld;
        
    //int state = 0;
    private int wert = 20;

    private int[][] array;

    // Array-Inhalte alle auf 0 setzen
    public Spielbrett(int breite, int hoehe,int xOffset,int yOffset, int anzahlFelder, Image backgroundImage) {
        this.brettBreite = breite;
        this.brettHoehe = hoehe;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.anzahlFelder = anzahlFelder;
        this.backgoundImage = backgroundImage;

        // initialisiere das Spielfeld mit leeren Feldern
        this.spielFeldArray = new int[anzahlFelder][anzahlFelder];
        for (int m = 0; m < this.anzahlFelder; m++) {
            for (int n = 0; n < this.anzahlFelder; n++) {
                spielFeldArray[m][n] = 0;
            }
        }

        //Berechnung zur Initialisierung
        double feldHoehe = brettHoehe/anzahlFelder;
        double feldBreite = brettBreite/anzahlFelder;

        // Spielbrett-Grafiken laden
        this.feld = new SpielStein[this.anzahlFelder][this.anzahlFelder];
        GrafikLib lib = GrafikLib.getInstance();
        BufferedImage[] bi = lib.getSprite("GUI/resources/Spielsteine_6x5.png", 6, 5);
        for (int m = 0; m < this.anzahlFelder; m++) {
            for (int n = 0; n < this.anzahlFelder; n++) {
                // Ausgangspunkt für das feld[0][0] ist die linke untere Ecke
                feld[m][n] = new SpielStein(bi, xOffset+feldBreite*m, yOffset + this.brettHoehe - feldHoehe*(n+1), 20);
            }
        }



    }

    /**
     * Versuch nur die benötigten / veränderten Felder neu zu zeiche ... mal
     * schaun ob sich das umsetzen lässt
     * @param g
     * @param neuesFeld
     */
    private void zeichneBenoetigteFelderNeu(Graphics g, int neuesFeld[][]) {

        // ACHTUNG! FUNKTION NOCH NICHT IM EINSATZ!

        //Checke ob sich was veraendert hat wen ja, kopiere gleich
        for(int i=0; i < this.anzahlFelder; i++ ){
            for(int j=0; j < this.anzahlFelder; j++ ){
                if(this.spielFeldArray[i][j] != neuesFeld[i][j]){

                }
            }
        }
    }

    /**
     * @see interfaces.Drawable
     * @param g
     */
    public void drawObjects(Graphics g) {
        if(backgoundImage != null)
            g.drawImage(backgoundImage, xOffset, yOffset, this);

        //Berechnung fuer das Feld
        int feldHoehe = brettHoehe/anzahlFelder;
        int feldBreite = brettBreite/anzahlFelder;
        int halbeFeldHoehe = feldHoehe/2;
        int halbeFeldBreite = feldBreite/2;
        int linienBreite = this.brettBreite-feldBreite;     // eine Feldbreite kuerzer
        int linienHoehe  = this.brettHoehe-feldHoehe;
        
        g.setColor(Color.BLACK);


        // Horizontale Linien zeichnen
        for (int i = 0; i < this.anzahlFelder; i++) {
            g.drawLine(this.xOffset + halbeFeldBreite, this.yOffset+i*feldHoehe + halbeFeldHoehe, xOffset+linienBreite +halbeFeldBreite, yOffset + i*feldHoehe + halbeFeldHoehe);
        }

        // Vertikale Linien zeichnen
        for (int i = 0; i < this.anzahlFelder; i++) {
            g.drawLine(xOffset + i*feldBreite + halbeFeldBreite, yOffset+ halbeFeldHoehe, xOffset + i*feldBreite + halbeFeldBreite, yOffset+linienHoehe+ halbeFeldHoehe);
        }


        // Jedes Feld soll sich selbst zeichnen
        for(int i=0; i < this.anzahlFelder; i++){
            for(int j=0; j < this.anzahlFelder; j++){
                feld[i][j].drawObjects(g);
            }
        }
    }

    /**
     * Nur Logic (=Animationen) berechnen
     * @param delta
     */
    public void doLogic( long delta ){
         // Logik auf alle Felder anwenden
        for(int i=0; i < this.anzahlFelder; i++){
            for(int j=0; j < this.anzahlFelder; j++){
                this.feld[i][j].doLogic(delta);
            }
        }
    }

    /**
     * @param neuesSpielFeld[][] Wenn sich das Spielfeld veraendert hat
     */
    public void updateSpielFeld(int[][] neuesSpielFeld) {

        // Logik auf alle Felder anwenden
        for(int i=0; i < this.anzahlFelder; i++){
            for(int j=0; j < this.anzahlFelder; j++){
                if(this.spielFeldArray[i][j] != neuesSpielFeld[i][j] ){

                    switch(neuesSpielFeld[i][j]){
                        case Konstante.SCHNITTPUNKT_SCHWARZ:
                            // Ein schwarzer Stein wurde neu draufgesetzt
                            // Starte einblende Animation fuer Schwarz
                            this.feld[i][j].starteAnimationSchwarzSetzen();
                            this.repaint();
                            break;
                        case Konstante.SCHNITTPUNKT_WEISS:
                            // Ein weisser Stein wurde neu draufgesetzt
                            // Starte einblende Animation fuer Weiss
                            this.feld[i][j].starteAnimationWeissSetzen();
                            this.repaint();
                            break;
                        case Konstante.SCHNITTPUNKT_LEER:
                            // Der vorherige Stein wurde entfernt
                            // der Stein kann nicht Verboten gewesen sein, da dies weiter oben bereits abgefangen wurde
                            if( this.spielFeldArray[i][j] == Konstante.SCHNITTPUNKT_SCHWARZ){
                                // Schwarzer Stein wurde entfernt
                                // Starte ausblende Animation fuer Schwarz
                                this.feld[i][j].starteAnimationSchwarzEntfernen();
                                this.repaint();
                            } else if(this.spielFeldArray[i][j] == Konstante.SCHNITTPUNKT_WEISS){
                                // Weisser Stein wurde entfernt
                                // Starte ausblende Animation fuer Weiss
                                this.feld[i][j].starteAnimationWeissEntfernen();
                                this.repaint();
                            } else if (this.spielFeldArray[i][j] == Konstante.SCHNITTPUNKT_VERBOTEN){
                                this.feld[i][j].starteAnimationVerbotenerZugAufheben();
                            }
                            break;
                        case Konstante.SCHNITTPUNKT_VERBOTEN:
                            // Zeichne das Feld als verbotenen Schnittpunkt
                            this.feld[i][j].starteAnimationVerbotenerZug();
                            this.repaint();
                            break;
                        default:
                            // ungueltiger Wert
                    }
                    this.spielFeldArray[i][j]=neuesSpielFeld[i][j];
                }
            }
        }
    }

    /**
     *
     * @see interfaces.Movable
     * @param delta
     */
    public void move(long delta) {
        // Bewegungen ausführen bei allen Feldern
        for(int i=0; i < this.anzahlFelder; i++){
            for(int j=0; j < this.anzahlFelder; j++){
                feld[i][j].doLogic(delta);
            }
        }
    }

    public Point berechneTreffer( int xPos, int yPos ){

        int xPosRelativ = xPos-this.xOffset;
        int yPosRelativ = yPos-this.yOffset;

        int xPunkt = xPosRelativ/(this.brettBreite/this.anzahlFelder)+1;
        int yPunkt = (this.brettHoehe-yPosRelativ)/(this.brettHoehe/this.anzahlFelder)+1;

        if (xPunkt > 0 && xPunkt <= this.anzahlFelder && yPunkt > 0 && yPunkt <= this.anzahlFelder){
            return new Point(xPunkt, yPunkt);
        }
        else{
            return null;
        }
    }

}
