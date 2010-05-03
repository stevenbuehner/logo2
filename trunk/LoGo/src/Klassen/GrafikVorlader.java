package Klassen;

import GUI.GrafikLib;

/**
 *
 * @author steven
 * @version 0.2
 * Einzige Aufgabe dieser Klasse ist es, die später benötigten Grafiken
 * in den Speicher vorzuladen.
 *
 * Hier für wird die GrafikLib verwendet, welche Verweise auf die Grafiken im
 * Speicher behält
 */
public class GrafikVorlader {

    public GrafikVorlader() {
        this.ladeGrafiken();
    }

    private void ladeGrafiken() {

        GrafikLib lib = GrafikLib.getInstance();
        String base = "GUI/resources/";

        // Kugelgrafiken
        lib.getSprite(base + "Kugel_7x7.png");
        lib.getSprite(base + "Kugel_9x9.png");
        lib.getSprite(base + "Kugel_13x13.png");
        lib.getSprite(base + "Kugel_15x15.png");
        lib.getSprite(base + "Kugel_17x17.png");
        lib.getSprite(base + "Kugel_19x19.png");

        // Markierte Steine
        lib.getSprite(base + "MarkierterStein_7x7.png");
        lib.getSprite(base + "MarkierterStein_9x9.png");
        lib.getSprite(base + "MarkierterStein_13x13.png");
        lib.getSprite(base + "MarkierterStein_15x15.png");
        lib.getSprite(base + "MarkierterStein_17x17.png");
        lib.getSprite(base + "MarkierterStein_19x19.png");

        lib.getSprite(base + "MarkierterStein2_9x9.png");
        lib.getSprite(base + "MarkierterStein3_9x9.png");

        // Minutenzeiger
        lib.getSprite(base + "MinutenZeigerHuebsch1.png");
        lib.getSprite(base + "MinutenZeigerHuebsch2.png");


        // Screen Grafiken
        lib.getSprite(base + "Auswertungsanzeige.jpg");
        lib.getSprite(base + "GUI_v1.png");
        lib.getSprite(base + "PauseScreen.jpg");
        lib.getSprite(base + "SpielTisch2.jpg");
        lib.getSprite(base + "StartScreen.jpg");
        
    }
}
