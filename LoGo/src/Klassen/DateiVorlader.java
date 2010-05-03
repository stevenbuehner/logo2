package Klassen;

import GUI.GrafikLib;
import Sound.SoundLib;
import logo.LoGoApp;

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
public class DateiVorlader {

    public DateiVorlader() {
        this.ladeGrafiken();
    }

    private void ladeGrafiken() {

        GrafikLib gLib = GrafikLib.getInstance();
        SoundLib sLib = SoundLib.getInstance();
        String base = "GUI/resources/";

        if (LoGoApp.debug) {
            System.out.println("Beginne mit dem Laden der Grafiken in den Speicher");
        }

        // Kugelgrafiken
        gLib.getSprite(base + "Kugel_7x7.png");
        gLib.getSprite(base + "Kugel_9x9.png");
        gLib.getSprite(base + "Kugel_13x13.png");
        gLib.getSprite(base + "Kugel_15x15.png");
        gLib.getSprite(base + "Kugel_17x17.png");
        gLib.getSprite(base + "Kugel_19x19.png");

        // Markierte Steine
        gLib.getSprite(base + "MarkierterStein_7x7.png");
        gLib.getSprite(base + "MarkierterStein_9x9.png");
        gLib.getSprite(base + "MarkierterStein_13x13.png");
        gLib.getSprite(base + "MarkierterStein_15x15.png");
        gLib.getSprite(base + "MarkierterStein_17x17.png");
        gLib.getSprite(base + "MarkierterStein_19x19.png");

        gLib.getSprite(base + "MarkierterStein2_9x9.png");
        gLib.getSprite(base + "MarkierterStein3_9x9.png");

        // Uhrzeiger
        gLib.getSprite(base + "MinutenZeigerHuebsch2.png");
        gLib.getSprite(base + "sekZeiger1.png");
        gLib.getSprite(base + "stundenZeigerHuebsch2.png");

        // Screen Grafiken
        gLib.getSprite(base + "Auswertungsanzeige.jpg");
        gLib.getSprite(base + "GUI_v1.png");
        gLib.getSprite(base + "PauseScreen.jpg");
        gLib.getSprite(base + "SpielTisch2.jpg");
        gLib.getSprite(base + "StartScreen.jpg");

        if (LoGoApp.debug) {
            System.out.println("Grafiken fertig in den Speicher geladen");
        }

        if (LoGoApp.debug) {
            System.out.println("Beginne mit dem Laden der Sounddateien in den Speicher.");
        }

        // Sounddateien laden
        sLib.loadSound("abschluss", "sound/boom.wav");
        sLib.loadSound("alarm", "sound/rocket_start.wav");

        if (LoGoApp.debug) {
            System.out.println("Sounddateien fertig in den Speicher geladen.");
        }
    }
}
