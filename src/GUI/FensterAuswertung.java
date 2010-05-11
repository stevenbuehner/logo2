package GUI;

import Klassen.BackgroundStatistikLoader;
import Klassen.HistoryEintrag;
import Klassen.Konstante;
import Sound.SoundLib;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import logo.LoGoApp;

/**
 * Darstellungsklasse für die Spielauswertung. Inklusiver einer Anzeige für die
 * Bestenliste, welche durch Threads im Hintergrund geladen wird.
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
    private float spielerSchwarzKomi;
    private float spielerWeissKomi;
    private float ergebnisFuerWeiss;
    private int gewinnerBeiAufgOdZeit;
    private static final int AUF_ZEIT_BEENDET = 300;
    private static final int DURCH_AUFGABE_BEENDET = 301;
    private static final int DURCH_AUSZAEHLEN_BEENDET = 302;
    private static final int STATISTIK_XPOS = 91;
    private static final int STATISTIK_YPOS = 500;
    private static final int STATISTIK_ANZAHL = 5;
    private HistoryEintrag histEintraege[];
    private int wieWurdeBeendet;
    private BufferedImage backgroundImage;
    private JPanel contentPanel;
    private ImageIcon LogoIcon;
    private int xKoordSchw = 110;
    private int xKoordWeiss = 580;
    private int zeilenabstand = 30;
    private int yOffset = 250;
    private String datenbankStatustext = "";

    /**
     * Die Auswertung wird erstellt. Dabei wird die Initfunktion aufgerufen.
     */
    public FensterAuswertung() {
        this.init();
    }

    /**
     * Methode zum initialisieren der Variablen
     */
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
        this.gewinnerBeiAufgOdZeit = 0;

        this.histEintraege = new HistoryEintrag[STATISTIK_ANZAHL];

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

    /**
     * Überschriebene Funktion der Super-Klasse. Zeichnet die Hintergrundgrafik
     * und übergibt dann an die Super-Klasse.
     * @param g Graphics-Objekt des Fensters
     */
    @Override
    public void paint(Graphics g) {
        if (this.isVisible()) {
            super.paint(g);
            g.drawImage(backgroundImage, 0, 0, null);

            this.render(g);
        }
    }

    /**
     * Fängt ab, ob mit der Maus auf das Feld geklickt wird.
     * @param e Event
     * @see MouseListener
     */
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
                System.exit(0);
                break;
            default:
                // not used
                break;
        }
    }

    /**
     * Fängt ab, ob die Maus gepresst wurde. Diese Funktion ist leer.
     * @param e Event
     * @see MouseListener
     */
    public void mousePressed(MouseEvent e) {
    }

    /**
     * Fängt ab, ob die Maus losgelassen wurde. Diese Funktion ist leer.
     * @param e Event
     * @see MouseListener
     */
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * Fängt ab, ob die Maus in den Bereich des Fensters eingetreten ist.
     * Diese Funktion ist leer.
     * @param e Event
     * @see MouseListener
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Fängt ab, ob die Maus aus dem Bereich des Fensters ausgetreten ist.
     * Diese Funktion ist leer.
     * @param e Event
     * @see MouseListener
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Die ausgewerteten Informationen auf das Graphics-Objekt geschrieben
     * @param g Graphics-Objekt des Fensters
     */
    private void render(Graphics g) {
        /* Je nachdem, wie Beendet wurde, wird das Ergebnis angezeigt */
        if (this.wieWurdeBeendet == FensterAuswertung.DURCH_AUSZAEHLEN_BEENDET) {
            Font myFont = new Font("TimesRoman", 1, 16);
            g.setFont(myFont);
            g.drawString(this.spielerSchwarzName, this.xKoordSchw, this.yOffset);
            g.drawString("Gebietspunkte: " + this.spielerSchwarzGebietspunkte, this.xKoordSchw, this.yOffset + this.zeilenabstand);
            g.drawString("Gefangen: " + this.spielerSchwarzImSpielGefangen, this.xKoordSchw, this.yOffset + 2 * this.zeilenabstand);
            g.drawString("Gefangen auf Brett: " + this.spielerSchwarzAufBrettGefangen, this.xKoordSchw, this.yOffset + 3 * this.zeilenabstand);

            g.drawString(this.spielerWeissName, this.xKoordWeiss, this.yOffset);
            g.drawString("Gebietspunkte: " + this.spielerWeissGebietspunkte, this.xKoordWeiss, this.yOffset + this.zeilenabstand);
            g.drawString("Gefangen: " + this.spielerWeissImSpielGefangen, this.xKoordWeiss, this.yOffset + 2 * this.zeilenabstand);
            g.drawString("Gefangen auf Brett: " + this.spielerWeissAufBrettGefangen, this.xKoordWeiss, this.yOffset + 3 * this.zeilenabstand);

            NumberFormat numberFormat = new DecimalFormat("0.00");
            numberFormat.setRoundingMode(RoundingMode.DOWN);

            if (this.spielerWeissKomi >= 0 && this.spielerSchwarzKomi == 0) {
                g.drawString("Komi: " + numberFormat.format(this.spielerWeissKomi), this.xKoordWeiss, this.yOffset + 4 * this.zeilenabstand);
            } else if (this.spielerSchwarzKomi >= 0 && this.spielerWeissKomi == 0) {
                g.drawString("Komi: " + numberFormat.format(this.spielerSchwarzKomi), this.xKoordSchw, this.yOffset + 4 * this.zeilenabstand);
            } else {
                g.drawString("Komi: 0", this.xKoordWeiss, this.yOffset + 4 * this.zeilenabstand);
            }

            if (this.ergebnisFuerWeiss > 0.01) {
                g.drawString("Weiss gewinnt mit " + numberFormat.format(this.ergebnisFuerWeiss) + " Punkten.", this.xKoordWeiss, this.yOffset + 5 * this.zeilenabstand);
            } else if (this.ergebnisFuerWeiss < -0.01) {
                g.drawString("Schwarz gewinnt mit " + numberFormat.format((this.ergebnisFuerWeiss) * -1) + " Punkten.", this.xKoordSchw, this.yOffset + 5 * this.zeilenabstand);
            } else {
                g.drawString("Unentschieden", this.xKoordWeiss, this.yOffset + 6 * this.zeilenabstand);
                g.drawString("Unentschieden", this.xKoordSchw, this.yOffset + 6 * this.zeilenabstand);
            }

        } else if (this.wieWurdeBeendet == FensterAuswertung.DURCH_AUFGABE_BEENDET) {
            Font myFont = new Font("TimesRoman", 1, 16);
            g.setFont(myFont);
            g.drawString(this.spielerSchwarzName, this.xKoordSchw, this.yOffset);
            g.drawString(this.spielerWeissName, this.xKoordWeiss, this.yOffset);
            if (this.gewinnerBeiAufgOdZeit == Konstante.SCHNITTPUNKT_SCHWARZ) {
                g.drawString("Schwarz gewinnt durch Aufgabe.", this.xKoordSchw, this.yOffset + this.zeilenabstand);
            } else {
                g.drawString("Weiß gewinnt durch Aufgabe.", this.xKoordWeiss, this.yOffset + this.zeilenabstand);
            }


        } else if (this.wieWurdeBeendet == FensterAuswertung.AUF_ZEIT_BEENDET) {
            Font myFont = new Font("TimesRoman", 1, 16);
            g.setFont(myFont);
            g.drawString(this.spielerSchwarzName, this.xKoordSchw, this.yOffset);
            g.drawString(this.spielerWeissName, this.xKoordWeiss, this.yOffset);
            if (this.gewinnerBeiAufgOdZeit == Konstante.SCHNITTPUNKT_SCHWARZ) {
                g.drawString("Schwarz gewinnt auf Zeit.", this.xKoordSchw, this.yOffset + this.zeilenabstand);
            } else {
                g.drawString("Weiß gewinnt auf Zeit.", this.xKoordWeiss, this.yOffset + this.zeilenabstand);
            }
        }


        /* Ab hier das zeichnen der History-Einträge */

        g.drawString("Spielername", STATISTIK_XPOS, STATISTIK_YPOS);
        g.drawString("Punkte (Schwarz)", STATISTIK_XPOS + 200, STATISTIK_YPOS);
        g.drawString("Spielername", STATISTIK_XPOS + 400, STATISTIK_YPOS);
        g.drawString("Punkte (Weiß)", STATISTIK_XPOS + 600, STATISTIK_YPOS);
        g.drawLine(STATISTIK_XPOS, STATISTIK_YPOS, STATISTIK_XPOS + 830, STATISTIK_YPOS);

        if (histEintraege[0] != null) {
            // zeichne History-Einträge ...
            int x = STATISTIK_XPOS;
            int y = STATISTIK_YPOS;
            g.setColor(Color.BLACK);
            // g.setFont(myFont);

            for (int i = 0; i < STATISTIK_ANZAHL; i++) {
                if (histEintraege[i] != null) {
                    g.drawString(histEintraege[i].getNameSpielerSchwarz(), x, y + zeilenabstand * (i + 1));
                    g.drawString(String.valueOf(histEintraege[i].getPunkteSpielerSchwarz()), x + 200, y + zeilenabstand * (i + 1));
                    g.drawString(histEintraege[i].getNameSpielerWeiss(), x + 400, y + zeilenabstand * (i + 1));
                    g.drawString(String.valueOf(histEintraege[i].getPunkteSpielerWeiss()), x + 600, y + zeilenabstand * (i + 1));
                }
            }
        } else {
            // ansonsten warte auf Datenbank
            g.drawString(this.datenbankStatustext, STATISTIK_XPOS, STATISTIK_YPOS + this.zeilenabstand);
        }

    }

    /**
     * Wird ein Spiel durch auszaehlen beendet, muss das Ergebnis entsprechend
     * aufbereitet werden.
     * @param nameSchwarz Name des schwarzen Spielers
     * @param nameWeiss Name des weissen Spielers
     * @param komiFuerWeiss Komipunkte fuer Weiss
     * @param gebietsPunktSchwarz Anzahl der Gebietspunkte fuer Schwarz
     * @param gebietsPunkteWeiss Anzahl der Gebietspunkte fuer Weiss
     * @param schwarzeGefangenImSpiel Anzahl der im Spiel gefangenen schwarzen Steine
     * @param weisseGefangenImSpiel Anzahl der im Spiel gefangenen weissen Steine
     * @param schwarzeSteineTotAufBrett Anzahl der Toten schwarzen Steine auf dem Brett
     * @param weisseSteineTotAufBrett Anzahl der Toten weissen Steine auf dem Brett
     */
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
        if (komiFuerWeiss >= 0) {
            this.spielerWeissKomi = komiFuerWeiss;
            this.spielerSchwarzKomi = 0;
        } else {
            this.spielerSchwarzKomi = -1 * komiFuerWeiss;
            this.spielerWeissKomi = 0;
        }

        this.ergebnisFuerWeiss = (this.spielerWeissAufBrettGefangen + this.spielerWeissGebietspunkte + this.spielerWeissImSpielGefangen + this.spielerWeissKomi) - (this.spielerSchwarzAufBrettGefangen + this.spielerSchwarzGebietspunkte + this.spielerSchwarzImSpielGefangen + this.spielerSchwarzKomi);
        this.wieWurdeBeendet = FensterAuswertung.DURCH_AUSZAEHLEN_BEENDET;
    }

    /**
     * Wurde das Spiel durch Aufgebe beendet, wird ein entsprechendes Ergebnis
     * erzeugt.
     * @param nameSchwarz Name des Schwarzen Spielers
     * @param nameWeiss Name des Weissen Spielers
     * @param konstanteFuerGewinner Wer hat gewonnen?
     */
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

        this.gewinnerBeiAufgOdZeit = konstanteFuerGewinner;
        this.spielerSchwarzName = nameSchwarz;
        this.spielerWeissName = nameWeiss;
        this.wieWurdeBeendet = FensterAuswertung.DURCH_AUFGABE_BEENDET;
    }

    /**
     * Wurde das Spiel durch Zeitueberschreitung beendet, wird ein entsprechendes Ergebnis
     * erzeugt.
     * @param nameSchwarz Name des Schwarzen Spielers
     * @param nameWeiss Name des Weissen Spielers
     * @param konstanteFuerGewinner Wer hat gewonnen?
     */
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
        this.wieWurdeBeendet = FensterAuswertung.AUF_ZEIT_BEENDET;
        this.spielerSchwarzName = nameSchwarz;
        this.spielerWeissName = nameWeiss;
        this.gewinnerBeiAufgOdZeit = konstanteFuerGewinner;
    }

    /**
     * Überschriebene Funktion der Superklasse.
     * Wenn aufgerufen, wird beim setzen auf sichtbar ein Ton abgespielt und
     * im Hintergrund ein Thread gestartet, der die aktuellen Statistik-Werte
     * aus der Datenbank im Internet abzurufen versucht.
     * @param visible Fenster sichtbar oder nicht (true = sichtbar)
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        SoundLib sLib = SoundLib.getInstance();

        if (visible) {
            sLib.playSound("abschluss");
            this.datenbankStatustext = "";
            // Lade die Statistikwerte im Hintergrund und zeige sie an
            new BackgroundStatistikLoader(STATISTIK_ANZAHL);
        } else {
            sLib.stopSound("abschluss");
        }
    }

    /**
     * Diese Funktion kann vom Thread im Hintergrund aufgerufen werden.
     * Sie übergibt die ermittelten History-Einträge.
     * @param eintraege Die Historyeinträge die Gesetzt werden sollen
     */
    public synchronized void setHistoryEintraege(HistoryEintrag eintraege[]) {
        this.histEintraege = eintraege;
        this.repaint();
    }

    /**
     * Diese Funktion kann vom Thread im Hintergrund aufgerufen werden.
     * Sie übergibt Statusnachrichten, sowie auftretende Fehler bei der
     * Datenbankverbindung.
     * @param statusNachricht Nachicht die Ausgegeben werden soll
     */
    public synchronized void setStatusNachricht(String statusNachricht) {
        this.datenbankStatustext = statusNachricht;
        this.repaint();
    }
}
