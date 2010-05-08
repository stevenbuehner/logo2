package GUI;

import Klassen.Konstante;

/**
 * Abgeleitete Klasse von Spielbrett, das speziell zur Darstellung des
 * Spielbrettes innerhalb des Einstellungsfensters gedacht ist. Sie nimmt
 * benötigte Änderungen innerhalb dieser vor und überschreibt die Methode
 * updateSpielFeld(int[][] neuesSpielFeld).
 * Dies ist nötig, da (anders wie im laufenden Spiel) Kugeln sowohl gesetzt,
 * als auch entfernt werden können müssen.
 * 
 * @author tommy, Steven
 * @version 0.2
 */
public class EinstellungSpielbrett extends Spielbrett {

    /**
     * Konstruktor der die Parameter alle direkt weiter an den Konstrutor der
     * Vaterklasse Spielbrett leitet.
     * @param breite Breite des zu zeichnenden Spielfeldes in Pixeln
     * @param hoehe Höhe des zu zeichnenden Spielfeldes in Pixeln
     * @param xOffset Abstand vom linken Rand des Fensters in Pixeln
     * @param yOffset Abstand vom oberen Rand des Fensters in Pixeln
     * @param anzahlFelder Anzahl der darzustellenden Felder in X- und Y-Richtung
     * @see Spielbrett
     */
    public EinstellungSpielbrett(int breite, int hoehe, int xOffset, int yOffset, int anzahlFelder) {
        super(breite, hoehe, xOffset, yOffset, anzahlFelder);
    }

    /**
     * Bei Änderungen des Spielfeldes wird hier ein
     * Integer-Array mit den neuen Spieldaten übergeben. Die erlaubten Werte
     * sind in der Klasse Konstante definiert. Möglich sind alle Konstanden,
     * die mit SCHNITTPUNKT_ beginnen. 
     * Die Klasse sorgt dafür, dass ÄNDERUNGEN animiert, bzw. in der GUI umgesetzt werden.
     *
     * @param neuesSpielFeld Integer-Array mit dem neuen Spielfeld
     * @see Konstante
     *
     */
    @Override
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
                            if (spielFeldArray[i][j] == Konstante.SCHNITTPUNKT_SCHWARZ) {
                                this.feld[i][j].starteAnimationVerbotenerZugSchwarz();
                            } else if (spielFeldArray[i][j] == Konstante.SCHNITTPUNKT_WEISS) {
                                this.feld[i][j].starteAnimationVerbotenerZugWeiss();
                            } else {
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
