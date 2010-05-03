/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Klassen.Spielfeld;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import logo.LoGoApp;

/**
 *
 * @author steven
 * @version 0.2
 */
public class FensterAuswertung extends JFrame implements MouseListener {

    BufferedImage backgroundImage;
    JPanel contentPanel;
    Spielfeld dasSpielfeld;
    ImageIcon LogoIcon;

    public FensterAuswertung() {
        this.init();
    }

    private void init() {

        GrafikLib lib = GrafikLib.getInstance();
        this.backgroundImage = lib.getSprite("GUI/resources/Auswertungsanzeige.jpg");
        this.contentPanel = new BackgroundImagePanel(backgroundImage);
        this.dasSpielfeld = null;
        //this.LogoIcon = lib.getIcon("GUI/resources/logo.icns");

        this.addMouseListener(this);

        this.setUndecorated(true);
        this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        this.setContentPane(contentPanel);
        this.setSize(1024, 768);
        this.setLocationRelativeTo(null);
        // this.setVisible(true);
    }

    public void setAnzeige(Spielfeld dasFeld) {
        this.dasSpielfeld = dasFeld;
        this.berechneInformationen();
    }

    public void setAnzeige(Spielfeld dasFeld, boolean fensterSichtbar) {
        this.dasSpielfeld = dasFeld;
        this.setVisible(fensterSichtbar);
        this.berechneInformationen();
    }

    @Override
    public void paint(Graphics g) {
        if (this.isVisible()) {
            super.paint(g);
            g.drawImage(backgroundImage, 0, 0, null);

            this.render(g);
        }
    }

    public void mouseClicked(MouseEvent e) {
        Object[] options = {"Abbrechen",
            "Neues Spiel starten",
            "Programm beenden"};

        int antwort = JOptionPane.showOptionDialog(this,
                "Wie möchten Sie nun fortfahren?",
                "Zwischenfrage",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        switch (antwort) {
            case JOptionPane.YES_OPTION:
                // Abbrechen gedrückt
                // mache nichts
                break;
            case JOptionPane.NO_OPTION:
                // Neues Spiel starten angeklickt
                LoGoApp.meineSteuerung.buttonNeuesSpiel();
                break;
            case JOptionPane.CANCEL_OPTION:
                // Spiel beenden angeklickt
                LoGoApp.meineSteuerung.buttonSpielBeenden();
                break;
            default:
                // not used
                break;
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    /**
     * Die ausgewerteten Informationen auf das Fenster @param g schreiben
     */
    private void render(Graphics g) {
        if (this.dasSpielfeld == null
                || this.dasSpielfeld.getSpielerSchwarz() == null
                || this.dasSpielfeld.getSpielerWeiss() == null) {
            // Es muss mindestens das Feld und die Spieler existieren,
            // sonst wird gar nicht erst etwas gerendert! :-)
            return;
        }

        // Zeichne die Informationen aus dem Spielfeld auf die Oberflaeche
    }

    /**
     * Beim übergeben eines neuen Spielfeldes wird diese Methode intern aufgerufen
     * sie macht alle nötigen Berechnungen die sich nicht direkt aus dem übergebenen
     * Spielfeld herauslesen lassen. Damit wird die performance erhöht und die Werte
     * müssen nicht bei jedem Aufruf von render bzw. paint neu berechnet werden.
     */
    private void berechneInformationen() {
    }
}
