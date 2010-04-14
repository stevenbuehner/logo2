/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import javax.swing.JFrame;

/**
 *
 * @author steven
 * @version 0.1
 * Diese Klasse handelt den Bildschirm. Mit der Klasse laesst sich unser
 * Bildschirm verwalten und auf Vollbild umschalten
 */
public class Screen {

    private GraphicsDevice vc;      // Video-Card

    public Screen() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.vc = env.getDefaultScreenDevice();
    }

    /**
     * Screen in Fullscreen bringen
     * @param dm DisplayMode enhtaelt die Aufloesung, die Bildtiefe und die Anzeigegeschwindigkeit
     * @param window das Fenster
     */
    public void setFullScreen(DisplayMode dm, JFrame window) {
        window.setUndecorated(true);    // Fenster ohne Umrandung
        window.setResizable(false);     // Fenster nicht in der Größe verändern
        vc.setFullScreenWindow(window); // Fenster Window im FullScreen anzeigen

        if (dm != null && vc.isDisplayChangeSupported()) {
            try {
                vc.setDisplayMode(dm);
            } catch (Exception e) {
            }
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
     * Fullscreen-Modus beenden
     */
    public void restoreScreen() {
        Window w = vc.getFullScreenWindow();
        if (w != null) {
            w.dispose();            // Garbage.Collection beschleunigen
        }
        vc.setFullScreenWindow(null); // Der VideoKarte das Fenster nehmen

    }
}
