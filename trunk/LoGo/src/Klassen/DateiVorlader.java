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
        String baseGrafik = "GUI/resources/";
        String baseSound = "Sound/resources/";

        if (LoGoApp.debug) {
            System.out.println("Beginne mit dem Laden der Grafiken in den Speicher");
        }

        // Kugelgrafiken
        gLib.getSprite(baseGrafik + "Kugel_7x7.png");
        gLib.getSprite(baseGrafik + "Kugel_9x9.png");
        gLib.getSprite(baseGrafik + "Kugel_13x13.png");
        gLib.getSprite(baseGrafik + "Kugel_15x15.png");
        gLib.getSprite(baseGrafik + "Kugel_17x17.png");
        gLib.getSprite(baseGrafik + "Kugel_19x19.png");

        // Markierte Steine
        gLib.getSprite(baseGrafik + "MarkierterStein_7x7.png");
        gLib.getSprite(baseGrafik + "MarkierterStein_9x9.png");
        gLib.getSprite(baseGrafik + "MarkierterStein_13x13.png");
        gLib.getSprite(baseGrafik + "MarkierterStein_15x15.png");
        gLib.getSprite(baseGrafik + "MarkierterStein_17x17.png");
        gLib.getSprite(baseGrafik + "MarkierterStein_19x19.png");

        gLib.getSprite(baseGrafik + "MarkierterStein2_9x9.png");
        gLib.getSprite(baseGrafik + "MarkierterStein3_9x9.png");

        // Uhrzeiger
        gLib.getSprite(baseGrafik + "MinutenZeigerHuebsch2.png");
        gLib.getSprite(baseGrafik + "sekZeiger1.png");
        gLib.getSprite(baseGrafik + "stundenZeigerHuebsch2.png");

        // Screen Grafiken
        gLib.getSprite(baseGrafik + "Auswertungsanzeige.jpg");
        gLib.getSprite(baseGrafik + "GUI_v1.png");
        gLib.getSprite(baseGrafik + "PauseScreen.jpg");
        gLib.getSprite(baseGrafik + "SpielTisch2.jpg");
        gLib.getSprite(baseGrafik + "StartScreen.jpg");

        if (LoGoApp.debug) {
            System.out.println("Grafiken fertig in den Speicher geladen");
        }

        if (LoGoApp.debug) {
            System.out.println("Beginne mit dem Laden der Sounddateien in den Speicher.");
        }

        // Sounddateien laden
        sLib.loadSound( "alarm", baseSound + "alarmMono.wav" );
        sLib.loadSound("abschluss", baseSound + "abschlussMono.wav");

        if (LoGoApp.debug) {
            System.out.println("Sounddateien fertig in den Speicher geladen.");
        }
    }
}
