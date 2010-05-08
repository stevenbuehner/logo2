package GUI;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Statische Grafikbibliothek welche Instanzen aller angeforderter Grafiken behält.
 * Dadurch ist es möglich häufig verwendete Grafiken schnell aus dem Speicher zu laden
 * ohne dass jedes Mal ein erneuter Festplattenzugriff nötig wäre.
 * Außerdem sind alle Grafiken speziell für die verwendete Grafikkarte optimiert.
 * @author $steven
 * @version 1.0
 */
public class GrafikLib {

    static GrafikLib instance;
    private static GraphicsEnvironment ge;
    private static GraphicsConfiguration gc;
    private static HashMap<URL, BufferedImage> sprites;

    /**
     * Konstruktor welcher die internen Objekte initialisiert. Er kann von Außen
     * nicht aufgerufen werden. Um eine Instanz der Klasse zu bekommen sollte
     * stattdessen die Funktion getInstance() verwendet werden.
     */
    private GrafikLib() {
        // Initialisieren der Lib

        // GraphicsConfiguration fuer VolatileImage
        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gc = ge.getDefaultScreenDevice().getDefaultConfiguration();

        sprites = new HashMap<URL, BufferedImage>();
    }

    /**
     * Erstellt, falls noch nicht vorhanden, ein Objekt der Klasse GrafikLib und
     * gibt dieses zurück. Wenn bereits vorhanden, wird diese direkt übergeben.
     * Eine Instanz dieser Klasse sollte für ein Programm nur EINMAL erstellt
     * werden, um das Konzept optimal auszureizen.
     * @return Instanz der Klasse GrafikLib
     */
    public static GrafikLib getInstance() {
        if (instance == null) {
            instance = new GrafikLib();
        }
        return instance;
    }

    /**
     * Methode um ein Bild aus dem Speicher zu laden und es als Instanz innerhalb
     * der Klasse abzuspeichern. Beim nächsten Aufruf dieser Funktion mit der selben
     * Grafik, wird die Grafik direkt aus dem Speicher, statt von der Festplatte
     * geladen.
     * Diese Funktion ruft intern die Funktion getSprite(URL pfad) auf.
     *
     * @param path Pfad zum gewünschen Image
     * @return Bei Erfolg das Image als BufferedImage, sonst NULL
     */
    public BufferedImage getSprite(String path) {
        URL location = getURLfromRessource(path);
        return getSprite(location);
    }

    /**
     * Methode um ein Bild aus dem Speicher zu laden und es als Instanz innerhalb
     * der Klasse abzuspeichern. Beim nächsten Aufruf dieser Funktion mit der selben
     * Grafik, wird die Grafik direkt aus dem Speicher, statt von der Festplatte
     * geladen.
     *
     * @param path Pfad zum gewünschen Image
     * @return Bei Erfolg das Image als BufferedImage, sonst NULL
     */
    public BufferedImage getSprite(URL pfad) {

        BufferedImage pic = null;

        pic = sprites.get(pfad);

        if (pic != null) {
            return pic;
        }

        try {
            pic = ImageIO.read(pfad); // ueber ImageIO die GIF lesen
        } catch (IOException e1) {
            System.out.println("Fehler beim Image laden: " + e1);
            return null;
        }

        BufferedImage better = gc.createCompatibleImage(pic.getWidth(), pic.getHeight(),
                Transparency.BITMASK);
        Graphics g = better.getGraphics();
        g.drawImage(pic, 0, 0, null);

        sprites.put(pfad, better);

        return better;
    }

    /**
     *
     * Methode um ein mehrere Bilder aus dem Speicher zu laden und diese als
     * Instanz innerhalb der Klasse abzuspeichern. Beim nächsten Aufruf dieser
     * Funktion mit dem selben Grafikpfad, werden die Grafiken direkt aus
     * dem Speicher, anstatt von der Festplatte geladen.
     * Übergeben wird nur EIN Bildpfad. Das übergebene Bild wird dann entsprechend
     * der übergebenen Spalten und Zeilen zerlegt und als ein Array von links nach
     * rechts und von oben nach unten zurückgegen.
     *
     * Diese Methode ruft intern getSprite(URL pfad, int spalten, int zeilen) auf.
     *
     * @param pfad Pfad zur Grafik
     * @param spalten Anzahl der Spalten in die die Grafik zerlegt werden soll
     * @param zeilen Anzahl der Zeilen in die die Grafik zerlegt werden soll
     * @return Array mit den Grafiken. Array-Size = spalten * zeilen
     */
    public BufferedImage[] getSprite(String pfad, int spalten, int zeilen) {
        URL location = getURLfromRessource(pfad);

        return getSprite(location, spalten, zeilen);
    }
    /**
     *
     * Methode um ein mehrere Bilder aus dem Speicher zu laden und diese als
     * Instanz innerhalb der Klasse abzuspeichern. Beim nächsten Aufruf dieser
     * Funktion mit dem selben Grafikpfad, werden die Grafiken direkt aus
     * dem Speicher, anstatt von der Festplatte geladen.
     * Übergeben wird nur EIN Bildpfad. Das übergebene Bild wird dann entsprechend
     * der übergebenen Spalten und Zeilen zerlegt und als ein Array von links nach
     * rechts und von oben nach unten zurückgegen.
     *
     * @param pfad Pfad zur Grafik
     * @param spalten Anzahl der Spalten in die die Grafik zerlegt werden soll
     * @param zeilen Anzahl der Zeilen in die die Grafik zerlegt werden soll
     * @return Array mit den Grafiken. Array-Size = spalten * zeilen
     */
    public BufferedImage[] getSprite(URL pfad, int spalten, int zeilen) {

        BufferedImage source = null;

        source = sprites.get(pfad);

        if (source == null) {
            try {
                source = ImageIO.read(pfad); // ‹ber ImageIO die GIF lesen
            } catch (IOException e1) {
                System.out.println(e1);
                return null;
            }

            sprites.put(pfad, source);

        }

        BufferedImage better = gc.createCompatibleImage(source.getWidth(), source.getHeight(),
                Transparency.BITMASK);
        Graphics g = better.getGraphics();
        g.drawImage(source, 0, 0, null);

        int width = source.getWidth() / spalten;
        int height = source.getHeight() / zeilen;

        BufferedImage[] pics = new BufferedImage[spalten * zeilen];
        int count = 0;

        for (int n = 0; n < zeilen; n++) {
            for (int i = 0; i < spalten; i++) {
                pics[count] = source.getSubimage(i * width, n * height, width, height);
                count++;
            }
        }

        return pics;

    }

    /**
     * Methode um ein Icaon von einem Pfad zurückzugeben. Diese Funktion wurde
     * neu implementiert und ist noch nicht ausreichend getestet worden.
     * @param pfad Pfad zur Grafik
     * @return Bei Erfolg: angefordertes Icon, sonst NULL
     */
    public ImageIcon getIcon(String pfad) {
        return new ImageIcon(pfad);
    }

    /**
     * Wandelt den übergebenen Pfad (String) um in eine URL. Der Pfad kann relativ
     * übergeben werden, doch die URL ist auf der Festplatte eindeutig.
     * @param pfad
     * @return
     */
    public URL getURLfromRessource(String pfad) {
        return getClass().getClassLoader().getResource(pfad);
    }
}
