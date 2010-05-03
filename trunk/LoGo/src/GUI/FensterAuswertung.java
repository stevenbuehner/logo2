/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Klassen.Konstante;
import Klassen.Spielfeld;
import java.awt.Font;
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

    /* Infos zur Auswertung */
    private String spielerSchwarzName;
    private String spielerWeissName = "";
    private int spielerSchwarzGebietspunkte;
    private int spielerWeissGebietspunkte;
    private int spielerSchwarzImSpielGefangen;
    private int spielerWeissImSpielGefangen;
    private int spielerSchwarzAufBrettGefangen;
    private int spielerWeissAufBrettGefangen;
    private int spielerSchwarzKomi;
    private float spielerWeissKomi;
    private float ergebnisFuerWeiss;

    private static final int AUF_ZEIT_BEENDET = 300;
    private static final int DURCH_AUFGABE_BEENDET = 301;
    private static final int DURCH_AUSZAEHLEN_BEENDET = 302;

    private int wieWurdeBeendet;

    private BufferedImage backgroundImage;
    private JPanel contentPanel;
    private ImageIcon LogoIcon;

    public FensterAuswertung() {
        this.init();
    }

    private void init() {

        this.spielerSchwarzName = "";
        this.spielerWeissName = "";
        this.spielerSchwarzGebietspunkte = 0;
        this.spielerWeissGebietspunkte = 0;
        this.spielerSchwarzImSpielGefangen = 0;
        this.spielerWeissImSpielGefangen = 0;
        this.spielerSchwarzAufBrettGefangen = 0;
        this.spielerWeissAufBrettGefangen = 0;
        this.spielerSchwarzKomi = 0;
        this.spielerWeissKomi = 0;
        this.wieWurdeBeendet = 0;
        this.ergebnisFuerWeiss = 0;

        GrafikLib lib = GrafikLib.getInstance();
        this.backgroundImage = lib.getSprite("GUI/resources/Auswertungsanzeige.jpg");
        this.contentPanel = new BackgroundImagePanel(backgroundImage);
        //this.LogoIcon = lib.getIcon("GUI/resources/logo.icns");

        this.addMouseListener(this);

        this.setUndecorated(true);
        this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        this.setContentPane(contentPanel);
        this.setSize(1024, 768);
        this.setLocationRelativeTo(null);
        // this.setVisible(true);
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
        /* Je nachdem, wie Beendet wurde, wird das Ergebnis angezeigt */
        if(this.wieWurdeBeendet == FensterAuswertung.AUF_ZEIT_BEENDET){
            Font myFont = new Font("TimesRoman", 1, 16);


        }
        else if(this.wieWurdeBeendet == FensterAuswertung.DURCH_AUFGABE_BEENDET){
            Font myFont = new Font("TimesRoman", 1, 16);

        }
        else if(this.wieWurdeBeendet == FensterAuswertung.DURCH_AUSZAEHLEN_BEENDET){
            Font myFont = new Font("TimesRoman", 1, 16);

        }
        

        // Zeichne die Informationen aus dem Spielfeld auf die Oberflaeche
    }


    public void ergebnisAuszaehlenZeigen(String nameSchwarz, String nameWeiss, float komiFuerWeiss, int gebietsPunktSchwarz, int gebietsPunkteWeiss,
            int schwarzeGefangenImSpiel, int weisseGefangenImSpiel, int schwarzeSteineTotAufBrett, int weisseSteineTotAufBrett) {
        if (LoGoApp.debug) {
            System.out.println("Name Schwarz: " + nameSchwarz);
            System.out.println("Name Weiss: " + nameWeiss);
            System.out.println("Gebietspunkte Schwarz: " + gebietsPunktSchwarz);
            System.out.println("Gebietspunkte Weiss: " + gebietsPunkteWeiss);
            System.out.println("Gefangene Schwarze im Spiel: " + schwarzeGefangenImSpiel);
            System.out.println("Gefangene Weisse im Spiel: " + weisseGefangenImSpiel);
            System.out.println("Tote Schwarze auf Brett: " + schwarzeSteineTotAufBrett);
            System.out.println("Tote Weisse auf Brett: " + weisseSteineTotAufBrett);
            System.out.println("Komi fuer Weiss: " + komiFuerWeiss);
        }
        this.spielerSchwarzName = nameSchwarz;
        this.spielerWeissName = nameWeiss;
        this.spielerSchwarzGebietspunkte = gebietsPunktSchwarz;
        this.spielerWeissGebietspunkte = gebietsPunkteWeiss;
        this.spielerWeissImSpielGefangen = schwarzeGefangenImSpiel;
        this.spielerSchwarzImSpielGefangen = weisseGefangenImSpiel;
        this.spielerSchwarzAufBrettGefangen = weisseSteineTotAufBrett;
        this.spielerWeissAufBrettGefangen = schwarzeSteineTotAufBrett;
        this.spielerSchwarzKomi = 0;
        this.spielerWeissKomi = komiFuerWeiss;
        this.ergebnisFuerWeiss = (this.spielerWeissAufBrettGefangen + this.spielerWeissGebietspunkte + this.spielerWeissImSpielGefangen + this.spielerWeissKomi)
                                 - (this.spielerSchwarzAufBrettGefangen + this.spielerSchwarzGebietspunkte + this.spielerSchwarzImSpielGefangen + this.spielerSchwarzKomi);
    }

    public void ergebnisAufgebenZeigen(String nameSchwarz, String nameWeiss, int konstanteFuerGewinner) {
        /* Erstmal zum Debugen */
        if (LoGoApp.debug) {
            System.out.println("Name Schwarz: " + nameSchwarz);
            System.out.println("Name Weiss: " + nameWeiss);
        }

        String gewinner;
        if (konstanteFuerGewinner == Konstante.SCHNITTPUNKT_SCHWARZ) {
            gewinner = "Schwarz";
        } else {
            gewinner = "Weiss";
        }

        if (LoGoApp.debug) {
            System.out.println(gewinner + " gewinnt durch Aufgabe");
        }
    }

    public void ergebnisAufZeitVerlorenZeigen(String nameSchwarz, String nameWeiss, int konstanteFuerGewinner) {
        /* Erstmal zum Debugen */
        if (LoGoApp.debug) {
            System.out.println("Name Schwarz: " + nameSchwarz);
            System.out.println("Name Weiss: " + nameWeiss);
        }

        String gewinner;
        if (konstanteFuerGewinner == Konstante.SCHNITTPUNKT_SCHWARZ) {
            gewinner = "Schwarz";
        } else {
            gewinner = "Weiss";
        }

        if (LoGoApp.debug) {
            System.out.println(gewinner + " gewinnt durch Zeit");
        }
    }
}
