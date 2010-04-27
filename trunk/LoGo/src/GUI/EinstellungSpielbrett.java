/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import Klassen.Konstante;

/**
 *
 * @author tommy
 */
public class EinstellungSpielbrett extends Spielbrett{
    public EinstellungSpielbrett(int breite, int hoehe, int xOffset, int yOffset, int anzahlFelder){
        super(breite, hoehe, xOffset, yOffset, anzahlFelder);
    }
    
    public void updateSpielFeld(int[][] neuesSpielFeld) {

        // Logik auf alle Felder anwenden
        for (int i = 0; i < this.anzahlFelder; i++) {
            for (int j = 0; j < this.anzahlFelder; j++) {
                if (this.spielFeldArray[i][j] != neuesSpielFeld[i][j]) {

                    switch (neuesSpielFeld[i][j]) {
                        case Konstante.SCHNITTPUNKT_SCHWARZ:
                            // Ein schwarzer Stein wurde neu draufgesetzt
                            // Starte einblende Animation fuer Schwarz
                            this.feld[i][j].setSchwarzerStein();
                            this.repaint();
                            break;
                        case Konstante.SCHNITTPUNKT_WEISS:
                            // Ein weisser Stein wurde neu draufgesetzt
                            // Starte einblende Animation fuer Weiss
                            this.feld[i][j].setWeisserStein();
                            this.repaint();
                            break;
                        case Konstante.SCHNITTPUNKT_LEER:
                            // Der vorherige Stein wurde entfernt
                            // der Stein kann nicht Verboten gewesen sein, da dies weiter oben bereits abgefangen wurde
                            if (this.spielFeldArray[i][j] == Konstante.SCHNITTPUNKT_SCHWARZ) {
                                // Schwarzer Stein wurde entfernt
                                // Starte ausblende Animation fuer Schwarz
                                this.feld[i][j].setLeeresFeld();
                                this.repaint();
                            } else if (this.spielFeldArray[i][j] == Konstante.SCHNITTPUNKT_WEISS) {
                                // Weisser Stein wurde entfernt
                                // Starte ausblende Animation fuer Weiss
                                this.feld[i][j].setLeeresFeld();
                                this.repaint();
                            } else if (this.spielFeldArray[i][j] == Konstante.SCHNITTPUNKT_VERBOTEN) {
                                this.feld[i][j].setLeeresFeld();
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
                        default:
                        // ungueltiger Wert
                    }
                    this.spielFeldArray[i][j] = neuesSpielFeld[i][j];
                }
            }
        }
    }

}
