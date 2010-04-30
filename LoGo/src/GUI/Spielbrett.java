package GUI;

import Klassen.Konstante;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import logo.LoGoApp;

/**
 *
 * @author steven
 * @version 0.1
 * Die Klasse wurde als Vorlage aus einem Forum entnommen:
 * http://www.java-forum.org/awt-swing-swt/95342-spielfeld-gitter-einzelne-zellen-veraendern.html#_
 *
 * Diese Klasse wird aktuell nirgends im Spiel verwedet!!!!
 */
public class Spielbrett extends JComponent {

    protected int brettBreite;
    protected int brettHoehe;
    protected int xOffset;
    protected int yOffset;
    protected int anzahlFelder;
    protected int[][] spielFeldArray;
    // Grafiken
    protected SpielStein[][] feld;
    protected SpielsteinMarkierung markierterStein;
    protected Point letztePositionMarkierterStein;

    // Array-Inhalte alle auf 0 setzen
    public Spielbrett(int breite, int hoehe, int xOffset, int yOffset, int anzahlFelder) {

        // Berechne Brettgroesse und Feldabstaende
        int unpassend = breite % anzahlFelder;
        this.brettBreite = breite - unpassend;
        unpassend = hoehe % anzahlFelder;
        this.brettHoehe = hoehe - unpassend;
        this.xOffset = xOffset + unpassend / 2;
        this.yOffset = yOffset + unpassend / 2;
        this.anzahlFelder = anzahlFelder;
        this.letztePositionMarkierterStein = null;
        this.setSize(30, 30);

        // initialisiere das Spielfeld mit leeren Feldern
        this.spielFeldArray = new int[anzahlFelder][anzahlFelder];
        for (int m = 0; m < this.anzahlFelder; m++) {
            for (int n = 0; n < this.anzahlFelder; n++) {
                spielFeldArray[m][n] = 0;
            }
        }

        //Berechnung zur Initialisierung
        int feldHoehe = brettHoehe / anzahlFelder;
        int feldBreite = brettBreite / anzahlFelder;

        // Spielbrett-Grafiken abhanegig von der Feldbreite und Feldhoehe laden
        this.feld = new SpielStein[this.anzahlFelder][this.anzahlFelder];
        GrafikLib lib = GrafikLib.getInstance();
        // BufferedImage[] bi = lib.getSprite("GUI/resources/Spielsteine_6x5.png", 6, 5);
        this.setBackground(null);

        String spielSteinImageName;
        String markierterSteinImage = "";
        switch(anzahlFelder){
            case 7:
                spielSteinImageName = "GUI/resources/Kugel_7x7.png";
                markierterSteinImage = "GUI/resources/MarkierterStein_7x7.png";
                break;
            case 9:
                spielSteinImageName = "GUI/resources/Kugel_9x9.png";
                markierterSteinImage = "GUI/resources/MarkierterStein3_9x9.png";
                break;
            case 11:
                spielSteinImageName = "GUI/resources/Kugel_11x11.png";
                markierterSteinImage = "GUI/resources/MarkierterStein_11x11.png";
                break;
            case 13:
                spielSteinImageName = "GUI/resources/Kugel_13x13.png";
                markierterSteinImage = "GUI/resources/MarkierterStein_13x13.png";
                break;
            case 15:
                spielSteinImageName = "GUI/resources/Kugel_15x15.png";
                markierterSteinImage = "GUI/resources/MarkierterStein_15x15.png";
                break;
            case 17:
                spielSteinImageName = "GUI/resources/Kugel_17x17.png";
                markierterSteinImage = "GUI/resources/MarkierterStein_17x17.png";
                break;
            case 19:
                spielSteinImageName = "GUI/resources/Kugel_19x19.png";
                markierterSteinImage = "GUI/resources/MarkierterStein_19x19.png";
                break;
            default:
                throw new UnsupportedOperationException("Diese Spiellfeldgroesse wird nicht unterstuetzt.");
        }

        BufferedImage[] kugeln = lib.getSprite(spielSteinImageName, 14, 3);
        BufferedImage[] markStein = lib.getSprite(markierterSteinImage, 12, 1);

        // Initialisierern des Spezial-Objektes zum markieren der Spielsteine
        this.markierterStein = new SpielsteinMarkierung(markStein, 0, 0);
        this.markierterStein.setVisible(false);

        for (int m = 0; m < this.anzahlFelder; m++) {
            for (int n = 0; n < this.anzahlFelder; n++) {
                // Ausgangspunkt für das feld[0][0] ist die linke untere Ecke
                // Ein Spielstein bekommt jeweils die Koordinaten der Mitte, auf der er liegt.
                feld[m][n] = new SpielStein(kugeln, xOffset + feldBreite * m + feldBreite / 2, yOffset + this.brettHoehe - feldHoehe * (n + 0.5));
            }
        }



    }

    /**
     * Versuch nur die benötigten / veränderten Felder neu zu zeiche ... mal
     * schaun ob sich das umsetzen lässt
     * @param g
     * @param neuesFeld
     */
    /* private void zeichneBenoetigteFelderNeu(Graphics g, int neuesFeld[][]) {

        // ACHTUNG! FUNKTION NOCH NICHT IM EINSATZ!

        //Checke ob sich was veraendert hat wen ja, kopiere gleich
        for (int i = 0; i < this.anzahlFelder; i++) {
            for (int j = 0; j < this.anzahlFelder; j++) {
                if (this.spielFeldArray[i][j] != neuesFeld[i][j]) {
                }
            }
        }
    }
     
     */

    @Override
    public void paint(Graphics g){
        this.paintComponents(g);
    }

    @Override
    public void paintComponents(Graphics g){
        
        g = (Graphics2D)g;

        //Berechnung fuer das Feld
        int feldHoehe = brettHoehe / anzahlFelder;
        int feldBreite = brettBreite / anzahlFelder;
        int halbeFeldHoehe = feldHoehe / 2;
        int halbeFeldBreite = feldBreite / 2;
        int linienBreite = this.brettBreite - feldBreite;     // eine Feldbreite kuerzer
        int linienHoehe = this.brettHoehe - feldHoehe;

        if(LoGoApp.debug){
            g.setColor(Color.gray);
            g.fillRect(xOffset, yOffset, brettBreite, brettHoehe);
            g.setColor(Color.red);
            g.fillOval(874-5, 271-5, 10, 10);
        }

        g.setColor(Color.BLACK);

        // Horizontale Linien zeichnen
        for (int i = 0; i < this.anzahlFelder; i++) {
            g.drawLine(this.xOffset + halbeFeldBreite, this.yOffset + i * feldHoehe + halbeFeldHoehe, xOffset + linienBreite + halbeFeldBreite, yOffset + i * feldHoehe + halbeFeldHoehe);
        }

        // Vertikale Linien zeichnen
        for (int i = 0; i < this.anzahlFelder; i++) {
            g.drawLine(xOffset + i * feldBreite + halbeFeldBreite, yOffset + halbeFeldHoehe, xOffset + i * feldBreite + halbeFeldBreite, yOffset + linienHoehe + halbeFeldHoehe);
        }

        // Jedes Feld soll sich selbst zeichnen
        for (int i = 0; i < this.anzahlFelder; i++) {
            for (int j = 0; j < this.anzahlFelder; j++) {
                feld[i][j].drawObjects(g);
            }
        }

        // Den letzten Zug mit einer Markierung belegen
        this.markierterStein.drawObjects(g);
    }

    /**
     * Nur Logic (=Animationen) berechnen
     * @param delta
     */
    public synchronized void doLogic(long delta) {
        // Logik auf alle Felder anwenden
        for (int i = 0; i < this.anzahlFelder; i++) {
            for (int j = 0; j < this.anzahlFelder; j++) {
                this.feld[i][j].doLogic(delta);
            }
        }

        // Animationen im markierten Stein
        this.markierterStein.doLogic(delta);
    }

    /**
     * @param neuesSpielFeld[][] Wenn sich das Spielfeld veraendert hat
     */
    public void updateSpielFeld(int[][] neuesSpielFeld) {

        // Logik auf alle Felder anwenden
        for (int i = 0; i < this.anzahlFelder; i++) {
            for (int j = 0; j < this.anzahlFelder; j++) {
                if (this.spielFeldArray[i][j] != neuesSpielFeld[i][j]) {

                    switch (neuesSpielFeld[i][j]) {
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
                            if (this.spielFeldArray[i][j] == Konstante.SCHNITTPUNKT_SCHWARZ) {
                                // Schwarzer Stein wurde entfernt
                                // Starte ausblende Animation fuer Schwarz
                                this.feld[i][j].starteAnimationSchwarzEntfernen();
                                this.repaint();
                            } else if (this.spielFeldArray[i][j] == Konstante.SCHNITTPUNKT_WEISS) {
                                // Weisser Stein wurde entfernt
                                // Starte ausblende Animation fuer Weiss
                                this.feld[i][j].starteAnimationWeissEntfernen();
                                this.repaint();
                            } else if (this.spielFeldArray[i][j] == Konstante.SCHNITTPUNKT_VERBOTEN) {
                                this.feld[i][j].starteAnimationVerbotenerZugAufheben();
                            } else if (this.spielFeldArray[i][j] == Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ){
                                this.feld[i][j].starteAnimationGebietSchwarzZuLeer();
                            } else if (this.spielFeldArray[i][j] == Konstante.SCHNITTPUNKT_GEBIET_WEISS){
                                this.feld[i][j].starteAnimantionGebietWeissZuLeer();
                            }
                            break;
                        case Konstante.SCHNITTPUNKT_VERBOTEN:
                            // Zeichne das Feld als verbotenen Schnittpunkt
                            if(spielFeldArray[i][j] == Konstante.SCHNITTPUNKT_SCHWARZ){
                                this.feld[i][j].starteAnimationVerbotenerZugSchwarz();
                            }
                            else if( spielFeldArray[i][j] == Konstante.SCHNITTPUNKT_WEISS){
                                this.feld[i][j].starteAnimationVerbotenerZugWeiss();
                            }
                            else{
                                this.feld[i][j].setVerbotenerZug();
                            }
                            this.repaint();
                            break;
                        case Konstante.SCHNITTPUNKT_GEBIET_SCHWARZ:
                            this.feld[i][j].starteAnimationGebietspunktSchwarz();
                            break;
                        case Konstante.SCHNITTPUNKT_GEBIET_WEISS:
                            this.feld[i][j].starteAnimationGebietspunktWeiss();
                            break;
                        case Konstante.SCHNITTPUNKT_SCHWARZ_GEFANGEN:
                            this.feld[i][j].starteAnimationGefangenenSteinSchwarz();
                            break;
                        case Konstante.SCHNITTPUNKT_WEISS_GEFANGEN:
                            this.feld[i][j].starteAnimationGefangenenSteinWeiss();
                            break;
                        default:
                        // ungueltiger Wert
                    }
                    this.spielFeldArray[i][j] = neuesSpielFeld[i][j];
                }
            }
        }
    }

    public void setMarkierterStein(Point steinPos){
        
            if(steinPos == null){
            this.markierterStein.visible = false;
        }
        else{
            if(this.letztePositionMarkierterStein == null
                    || this.letztePositionMarkierterStein.x != steinPos.x
                    || this.letztePositionMarkierterStein.y != steinPos.y){
                // Wenn die Markierung auf einen neuen Stein gelegt wird,
                // dann soll das Erscheinen der Markierung erneut animiert werden
                this.markierterStein.restartAnimation();
            }

            int feldHoehe = brettHoehe / anzahlFelder;
            int feldBreite = brettBreite / anzahlFelder;

            this.markierterStein.setX(xOffset + feldBreite * (steinPos.x-1) + feldBreite / 2);
            this.markierterStein.setY(yOffset + this.brettHoehe - feldHoehe * (steinPos.y - 0.5) );
            this.markierterStein.setVisible(true);
        }
    }

    /**
     *
     * @see interfaces.Movable
     * @param delta
     */
    public void move(long delta) {
        // Bewegungen ausführen bei allen Feldern
        for (int i = 0; i < this.anzahlFelder; i++) {
            for (int j = 0; j < this.anzahlFelder; j++) {
                feld[i][j].doLogic(delta);
            }
        }
    }

    public Point berechneTreffer(int xPos, int yPos) {

        int xPosRelativ = xPos - this.xOffset;
        int yPosRelativ = yPos - this.yOffset;

        int xPunkt = xPosRelativ / (this.brettBreite / this.anzahlFelder) + 1;
        int yPunkt = (this.brettHoehe - yPosRelativ) / (this.brettHoehe / this.anzahlFelder) + 1;

        if (xPunkt > 0 && xPunkt <= this.anzahlFelder && yPunkt > 0 && yPunkt <= this.anzahlFelder) {
            return new Point(xPunkt, yPunkt);
        } else {
            return null;
        }
    }

    public int getAnzahlFelder(){
        return this.anzahlFelder;
    }

    /*
    public void setX( int x){
        this.xOffset = x;
    }

    @Override
    public int getX (){
        return this.xOffset;
    }

    public void setY( int y){
        this.yOffset = y;
    }

    @Override
    public int getY ( ){
        return this.yOffset;
    }
    */
}
