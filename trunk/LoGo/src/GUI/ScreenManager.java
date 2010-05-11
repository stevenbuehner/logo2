/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

/**
 *
 * @author steven
 * @version 0.1
 * Diese Klasse handelt den Bildschirm. Mit der Klasse laesst sich unser
 * Bildschirm verwalten und auf Vollbild umschalten
 */
public class ScreenManager {

    private GraphicsDevice vc;      // Video-Card

    /**
     * Standardkonstrkutor der die benötigten Variablen initialisiert
     */
    public ScreenManager() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.vc = env.getDefaultScreenDevice();
    }

    /**
     *
     * @return Alle kompatiblen DisplayModes
     */
    public DisplayMode[] getCompatibleDisplayModes() {
        return vc.getDisplayModes();
    }

    /**
     *
     * @param modes Liste von DisplayModes
     * @return Gib den ersten kompatiblem DisplayModus zurueck,
     * bzw. null, wenn keiner gefunden wurde.
     */
    public DisplayMode findFirstCompatibleMode(DisplayMode modes[]) {
        DisplayMode goodModes[] = vc.getDisplayModes();
        for (int x = 0; x < modes.length; x++) {
            for (int y = 0; y < goodModes.length; y++) {
                if (displayModesMatch(modes[x], goodModes[y])) {
                    return modes[x];
                }
            }
        }
        return null;
    }

    /**
     *
     * @return Gibt den DisplayMode zurueck, mit dem gerade gearbeitet wird
     */
    public DisplayMode getCurrentDisplayMode() {
        return vc.getDisplayMode();
    }

    /**
     * Vergleicht die beiden Display Modes
     * @param mode1
     * @param mode2
     * @return und gibt true zurueck, wenn alle vier Parameter von DisplayModes uebereinsteimmen
     * @see ScreenManager.displayModeMatch
     */
    private boolean displayModesMatch(DisplayMode m1, DisplayMode m2) {
        // Hoehe und Breite vergleichen des DisplayModes (Aufloesung)
        if (m1.getWidth() != m2.getWidth() || m1.getHeight() != m2.getHeight()) {
            return false;
        }

        // Bildtiefe vergleichen
        if (m1.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI
                && m2.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI
                && m1.getBitDepth() != m2.getBitDepth()) {
            return false;
        }

        // Vergleiche die Bildwiedergaberate (Refreshrate)
        if (m1.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN
                && m2.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN
                && m1.getRefreshRate() != m2.getRefreshRate()) {
            return false;
        }

        // Wenn alle tests nicht ansschlagen, sind die modes gleich
        return true;
    }

    /**
     *
     * @param dm Setzt den zu verwendenen DisplayMode
     */
    public void setFullScreen(DisplayMode dm) {
        JFrame window = new JFrame();
        this.setFullScreen(dm, window);
    }

    /**
     * ScreenManager in Fullscreen bringen
     * @param dm DisplayMode enhtaelt die Aufloesung, die Bildtiefe und die Anzeigegeschwindigkeit
     * @param window Das Fenster
     */
    public void setFullScreen(DisplayMode dm, JFrame window) {
        window.setUndecorated(true);    // Fenster ohne Umrandung
        window.setIgnoreRepaint(true);  // Warum auch immer!?
        window.setResizable(false);     // Fenster nicht in der Größe verändern
        vc.setFullScreenWindow(window); // Fenster Window im FullScreen anzeigen

        if (dm != null && vc.isDisplayChangeSupported()) {
            try {
                vc.setDisplayMode(dm);
            } catch (Exception e) {
            }
        }
        window.createBufferStrategy(2);
    }

    /**
     *
     * @return Gibt ein Objekt vom Typ Graphics zurück
     */
    public Graphics2D getGraphics() {
        Window w = vc.getFullScreenWindow();
        if (w != null) {
            BufferStrategy s = w.getBufferStrategy();
            return (Graphics2D) s.getDrawGraphics();
        } else {
            return null;
        }
    }

    /**
     *
     * @return Getter, der das aktuelle Window zurück gibt, das gerade auf
     * Fullscreen angezeigt wird
     */
    public Window getFullScreenWindow() {
        return vc.getFullScreenWindow();
    }

    /**
     * Prüft ob Content vorhanden ist und wenn ja, zeigt es ihn zBsp an.
     */
    public void update() {
        Window w = vc.getFullScreenWindow();
        if (w != null) {
            BufferStrategy s = w.getBufferStrategy();
            if (!s.contentsLost()) {
                // Wenn Content vorhanden ist
                s.show();
            }
        }
    }

    /**
     *
     * @return Gibt die Breite des Fensters zurueck
     */
    public int getWidth() {
        Window w = vc.getFullScreenWindow();
        if (w != null) {
            return w.getWidth();
        } else {
            return 0;
        }
    }

    /**
     *
     * @return Gibt die Fensterhoehe zurueck
     */
    public int getHeight() {
        Window w = vc.getFullScreenWindow();
        if (w != null) {
            return w.getHeight();
        } else {
            return 0;
        }
    }

    /**
     * Fullscreen-Modus beenden
     */
    public void restoreScreen() {
        Window w = vc.getFullScreenWindow();
        if (w != null) {
            w.dispose();            // Garbage.Collection beschleunigen
        }
        vc.setFullScreenWindow(null); // Der VideoKarte das Fenster nehmen
    }

    /**
     *
     * @param width
     * @param height
     * @param transparency
     * @return Gibt ein BufferedImage zurueck, das auf die Grafik-Konfiguration
     * des Anzeigegerätes abgestimmt ist mit
     */
    public BufferedImage createCompatibleImage(int width, int height, int transparency) {
        Window win = vc.getFullScreenWindow();
        if (win != null) {
            // Grafik-Konfiguration des Windows
            GraphicsConfiguration gc = win.getGraphicsConfiguration();
            return gc.createCompatibleImage(width, height, transparency);
        }
        return null;
    }
}
