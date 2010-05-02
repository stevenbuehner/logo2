package GUI;

import Klassen.Konstante;
import Interfaces.SpielerUhren;
import Interfaces.SpielerZettel;
import Interfaces.OberflaecheInterface;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import javax.swing.JOptionPane;
import Logo.LoGoApp;

/**
 *
 * @author steven
 * @version 0.2
 */
public class FensterSpieloberflaeche extends Frame implements Runnable, KeyListener, OberflaecheInterface, MouseListener, ActionListener {

    // Wenn nicht anders angegeben, verwende diese Masse zum zeichnen des Spielbretts
    private final static int STANDARD_SPIELFELD_HOEHE = 496;
    private final static int STANDARD_SPIELFELD_BREITE = 496;
    private final static int STANDARD_SPIELFELD_XPOS = 497;
    private final static int STANDARD_SPIELFELD_YPOS = 158;
    // Damit dass Spiel fluessig laueft
    private long delta = 0;
    private long last = 0;
    private long fps = 0;
    private BufferedImage backgroundImage;
    private BufferedImage pauseImage;
    private BufferedImage startImage;
    private boolean spielOberflaechePausiert = false;
    private boolean threadLaeuf;
    private static boolean once = false;
    // Backbuffer und Anpassungen an die Performance der Grafikkarte
    private VolatileImage backbuffer;
    private GraphicsEnvironment ge;
    private GraphicsConfiguration gc;
    private BufferStrategy strategy;
    // GUI-Teile
    private Spielbrett dasBrett;
    private SpielerUhren spielerUhrSchwarz;
    private SpielerUhren spielerUhrWeiss;
    private SpielerZettel spielerZettelSchwarz;
    private SpielerZettel spielerZettelWeiss;
    protected MenuBar dieMenueBar;
    protected MenuItem Einstellungen;
    protected MenuItem UeberLoGo;
    protected MenuItem NeuesSpiel;
    protected MenuItem SpielLaden;
    protected MenuItem SpielSpeichern;
    protected MenuItem SpielBeenden;
    protected MenuItem Undo;
    protected MenuItem Redo;
    protected MenuItem Passen;
    protected MenuItem Aufgeben;
    protected MenuItem AuswertungBeenden;
    protected MenuItem Pause;
    protected MenuItem Fortsetzen;

    /* Double Buffering */
    String mess = "";

    public FensterSpieloberflaeche(String pFenstername) {
        super(pFenstername);

        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
        this.setIgnoreRepaint(true);

        init();

        this.createBufferStrategy(2);
        strategy = getBufferStrategy();
        createBackbuffer();


        // Thread anstoßen
        this.start();
    }

    public void init() {

        GrafikLib lib = GrafikLib.getInstance();

        // Menue-Bar erstellen
        createMenue(this);

        last = System.nanoTime(); // Ohne Initialisierung stimmt die Berechnung
        // von delta nicht!!!
        berechneDelta(); // delta wird unten bei den Images benötigt

        // später benötigte Bilder in den Cache laden
        this.pauseImage = lib.getSprite("GUI/resources/PauseScreen.jpg");
        this.startImage = lib.getSprite("GUI/resources/StartScreen.jpg");
        this.backgroundImage = lib.getSprite("GUI/resources/SpielTisch2.jpg");

        this.spielerUhrSchwarz = new SpielerUhr(316, 215, 0, 4.5);
        this.spielerUhrWeiss = new SpielerUhr(114, 144, 0, 1);
        this.spielerZettelWeiss = new SpielerZettelEinzeln(5, 570, -18.6, "WEISS", Konstante.SCHNITTPUNKT_WEISS);
        this.spielerZettelSchwarz = new SpielerZettelEinzeln(238, 424, 2.5, "SCHWARZ", Konstante.SCHNITTPUNKT_SCHWARZ);

        this.spielOberflaechePausiert = false;

        //this.setResizable(false);
        this.setSize(1024, 768);
        setLocationRelativeTo(null); // Fenster zentrieren
        this.setVisible(true);
        this.createBufferStrategy(2);

        threadLaeuf = true;

        // Alle Spezialbuttons wie TAB, werden wie normale Keys behandelt
        this.setFocusTraversalKeysEnabled(false);
        this.addKeyListener(this);
        this.addMouseListener(this);

        // Programm bei klick auf den roten Knopf nicht beenden sondern Event weiter verarbeiten
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                //System.exit(0);
                LoGoApp.meineSteuerung.buttonSpielBeenden();
            }
        });

        /*
        int returnWert = JOptionPane.showConfirmDialog(this, "Bist Du gerade an der DHBW?");
        if(returnWert == JOptionPane.OK_OPTION || returnWert == JOptionPane.CANCEL_OPTION){
        System.exit(0);
        }
         */
    }

    protected void createBackbuffer() {
        if (backbuffer != null) {
            backbuffer.flush();
            backbuffer = null;
        }
        // GraphicsConfiguration für VolatileImage
        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
        backbuffer = gc.createCompatibleVolatileImage(getWidth(), getHeight());

    }

    protected void checkBackbuffer() {
        if (backbuffer == null) {
            createBackbuffer();
        }
        if (backbuffer.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE) {
            createBackbuffer();
        }
    }

    public void createMenue(Frame f) {

        dieMenueBar = new MenuBar();

        // ------ LoGo-Menue -------
        Menu dasLoGoMenue = new Menu("LoGo");

        // Einstellungen
        Einstellungen = new MenuItem("Einstellungen");
        Einstellungen.addActionListener(this);
        Einstellungen.setShortcut(new MenuShortcut(KeyEvent.VK_COMMA));
        dasLoGoMenue.add(Einstellungen);

        // Ueber
        UeberLoGo = new MenuItem("Über LoGo");
        UeberLoGo.addActionListener(this);
        UeberLoGo.setShortcut(new MenuShortcut(KeyEvent.VK_A));
        dasLoGoMenue.add(UeberLoGo);

        // ------ Spiel-Menue -------
        Menu dasSpielMenue = new Menu("Spiel");

        // Neues Spiel 
        NeuesSpiel = new MenuItem("Neues Spiel");
        NeuesSpiel.addActionListener(this);
        NeuesSpiel.setShortcut(new MenuShortcut(KeyEvent.VK_N));
        dasSpielMenue.add(NeuesSpiel);

        // Spiel Laden
        SpielLaden = new MenuItem("Spiel laden");
        SpielLaden.addActionListener(this);
        SpielLaden.setShortcut(new MenuShortcut(KeyEvent.VK_L));
        dasSpielMenue.add(SpielLaden);

        // Spiel Speichern
        SpielSpeichern = new MenuItem("Spiel speichern");
        SpielSpeichern.addActionListener(this);
        SpielSpeichern.setShortcut(new MenuShortcut(KeyEvent.VK_S));
        dasSpielMenue.add(SpielSpeichern);

        // Spiel Beenden
        SpielBeenden = new MenuItem("Spiel beenden");
        SpielBeenden.addActionListener(this);
        SpielBeenden.setShortcut(new MenuShortcut(KeyEvent.VK_Q));
        dasSpielMenue.add(SpielBeenden);

        // Trenner
        dasSpielMenue.addSeparator();

        // Spielzug Undo
        Undo = new MenuItem("Spielzug rückgängig");
        Undo.addActionListener(this);
        Undo.setEnabled(false);
        Undo.setShortcut(new MenuShortcut(KeyEvent.VK_LEFT));
        dasSpielMenue.add(Undo);

        // Spielzug Redo
        Redo = new MenuItem("Spielzug wieder herstellen");
        Redo.addActionListener(this);
        Redo.setEnabled(false);
        Redo.setShortcut(new MenuShortcut(KeyEvent.VK_RIGHT));
        dasSpielMenue.add(Redo);

        // Trenner
        dasSpielMenue.addSeparator();

        // Spielzug Passen
        Passen = new MenuItem("Passen");
        Passen.addActionListener(this);
        Passen.setEnabled(false);
        dasSpielMenue.add(Passen);

        // Spiel aufgeben
        Aufgeben = new MenuItem("Aufgeben");
        Aufgeben.addActionListener(this);
        Aufgeben.setEnabled(false);
        dasSpielMenue.add(Aufgeben);

        // Spiel AuswertungBeenden
        AuswertungBeenden = new MenuItem("Auswertung beenden");
        AuswertungBeenden.addActionListener(this);
        AuswertungBeenden.setEnabled(false);
        dasSpielMenue.add(AuswertungBeenden);

        // Trenner
        dasSpielMenue.addSeparator();

        // Spielzug Pause
        Pause = new MenuItem("Spiel pausieren");
        Pause.addActionListener(this);
        Pause.setShortcut(new MenuShortcut(KeyEvent.VK_P));
        Pause.setEnabled(false);
        dasSpielMenue.add(Pause);

        // Spielzug Fortsetzen
        Fortsetzen = new MenuItem("Spiel fortsetzen");
        Fortsetzen.addActionListener(this);
        Fortsetzen.setShortcut(new MenuShortcut(KeyEvent.VK_P));
        Fortsetzen.setEnabled(false);
        dasSpielMenue.add(Fortsetzen);



        dieMenueBar.add(dasLoGoMenue);
        dieMenueBar.add(dasSpielMenue);
        this.setMenuBar(dieMenueBar);

    }

    protected void berechneDelta() {

        delta = System.nanoTime() - last;
        last = System.nanoTime();

        if (delta != 0) {
            fps = ((long) 1e9) / delta;
        }
    }

    public void run() {
        long startTime = System.nanoTime();
        long cumTime = startTime;

        while (this.threadLaeuf) {
            long timePassed = System.nanoTime() - cumTime;
            cumTime += timePassed;

            this.doLogic(timePassed);
            this.draw();
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
        }
    }

    private void draw() {
        checkBackbuffer(); // Pr¸f-Methode f¸r VolatileImage

        Graphics g = backbuffer.getGraphics(); // GraphicsObject vom
        // VolatileImage holen
        g.clearRect(0, 0, getWidth(), getHeight());
        this.render(g); // alle Zeichenoperationen: Map, Player, etc.
        g.dispose(); // Graphics-Objekt verwerfen

        Graphics g2 = strategy.getDrawGraphics(); // Zeichenobjekt der
        // BufferStrategy holen
        g2.drawImage(backbuffer, 0, 0, this); // VolatileImage in den Buffer
        // zeichnen
        g2.dispose(); // GraphicsObject verwerfen

        strategy.show(); // Bufferanzeigen.
    }

    public void render(Graphics g) {

        g.drawImage(backgroundImage, 0, 0, this);

        if (spielerUhrSchwarz != null) {
            this.spielerUhrSchwarz.zeichneZeiger(g);
        }

        if (this.spielerUhrWeiss != null) {
            this.spielerUhrWeiss.zeichneZeiger(g);
        }

        if (this.spielerZettelSchwarz != null) {
            this.spielerZettelSchwarz.zeichneDich(g);
        }

        if (this.spielerZettelWeiss != null) {
            this.spielerZettelWeiss.zeichneDich(g);
        }

        if (this.dasBrett != null) {
            dasBrett.paintComponents(g);
        } else if (!this.spielOberflaechePausiert) {
            // Startscreen zeichnen, wenn nicht gerade Pause ansteht :-)
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            if (this.startImage != null) {
                g.drawImage(startImage, (this.getWidth() - startImage.getWidth()) / 2,
                        (this.getHeight() - startImage.getHeight()) / 2, this);
            }
        }

        if (this.spielOberflaechePausiert) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            if (this.pauseImage != null) {
                g.drawImage(pauseImage, (this.getWidth() - pauseImage.getWidth()) / 2,
                        (this.getHeight() - pauseImage.getHeight()) / 2, this);
            }
        }
    }

    public void start() {
        // Thread anstoßen
        if (!once) {
            once = true;
            Thread t = new Thread(this);
            t.start();
        }
    }

    public void stop() {
        this.threadLaeuf = false;
    }

    public synchronized void doLogic(long timePassed) {
        if (this.dasBrett != null) {
            this.dasBrett.doLogic(timePassed);
        }
    }

    public void keyTyped(KeyEvent e) {
        e.consume();
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_ESCAPE:
                // Wenn spiel pausiert, dann beende Pause, sonst starte Pause
                if (this.spielOberflaechePausiert) {
                    this.buttonSpielFortsetzenGedrueckt();
                } else {
                    this.buttonSpielPausierenGedrueckt();
                }
                break;
            case KeyEvent.VK_N:
                // neues Spiel. Momentanes wird pausiert um ein neues zu laden
                this.buttonNeuesSpiel();
                mess = "Steuerung => neues Spiel";
                break;
            case KeyEvent.VK_A: // A wie Aussetzen, sollte noch geaendert werden <----------------------
                LoGoApp.meineSteuerung.buttonPassen();
                mess = "Gepasst";
                break;
            case KeyEvent.VK_LEFT:
                LoGoApp.meineSteuerung.buttonUndo();
                mess = "Zug Rückgängig machen";
                break;
            case KeyEvent.VK_RIGHT:
                LoGoApp.meineSteuerung.buttonRedo();
                mess = "Zug wieder herstellen";
                break;
            case KeyEvent.VK_P:
                // Pausieren (beenden) wurde gedrückt
                if (this.spielOberflaechePausiert) {
                    this.buttonSpielFortsetzenGedrueckt();
                } else {
                    this.buttonSpielPausierenGedrueckt();
                }
                break;
            default:
                mess = "Pressed: " + KeyEvent.getKeyText(keyCode);
            // e.consume(); // Kombinierte Tasten sollen nicht behandlet werden.
        }
        e.consume(); // Kombinierte Tasten sollen nicht behandlet werden.
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        mess = "Released: " + KeyEvent.getKeyText(keyCode);
        e.consume();
    }

    public void setBrettOberflaeche(int[][] spielfeld, int spielfeldGroesse, Point markierterStein) {
        // Spielfeld updaten wenn es von der gleichen groesse ist, ansonsten
        // ein neues Spielfeld erstellen
        if (this.dasBrett != null && this.dasBrett.getAnzahlFelder() == spielfeldGroesse && spielfeld != null) {
            this.dasBrett.updateSpielFeld(spielfeld);
            this.dasBrett.setMarkierterStein(markierterStein);
        } else {
            this.dasBrett = null;
            this.dasBrett = new Spielbrett(STANDARD_SPIELFELD_BREITE,
                    STANDARD_SPIELFELD_HOEHE,
                    STANDARD_SPIELFELD_XPOS,
                    STANDARD_SPIELFELD_YPOS,
                    spielfeldGroesse);
            this.dasBrett.updateSpielFeld(spielfeld);
            this.dasBrett.setMarkierterStein(markierterStein);
            this.Pause.setEnabled(true);
            // Undo und Redo muessen erlaubt sein, fuer den Fall das ein Spiel geladen wurde
            // this.Undo.setEnabled(false);
            // this.Redo.setEnabled(false);
        }

    }

    public void setAnzeigePeriodenZeitWeiss(long periodenZeitInMS) {
        if (this.spielerUhrWeiss != null) {
            this.spielerUhrWeiss.restzeitInMS(periodenZeitInMS);
        }
    }

    public void setAnzeigePeriodenZeitSchwarz(long periodenZeitInMS) {
        if (this.spielerUhrSchwarz != null) {
            this.spielerUhrSchwarz.restzeitInMS(periodenZeitInMS);
        }
    }

    public void setAnzeigeSpielerZeitWeiss(long spielerZeitInMS) {
        if (this.spielerUhrWeiss != null) {
            this.spielerUhrWeiss.restzeitInMS(spielerZeitInMS);
        }
    }

    public void setAnzeigeSpielerZeitSchwarz(long spielerZeitInMS) {
        if (this.spielerUhrSchwarz != null) {
            this.spielerUhrSchwarz.restzeitInMS(spielerZeitInMS);
        }
    }

    public void setSpielernameWeiss(String spielername) {
        this.spielerZettelWeiss.setSpielername(spielername);
    }

    public void setSpielernameSchwarz(String spielername) {
        this.spielerZettelSchwarz.setSpielername(spielername);
    }

    public void setGefangeneSteineWeiss(int anzGefangenerSteiner) {
        this.spielerZettelWeiss.setGefangenenAnzahl(anzGefangenerSteiner);
    }

    public void setGefangeneSteineSchwarz(int anzGefangenerSteiner) {
        this.spielerZettelSchwarz.setGefangenenAnzahl(anzGefangenerSteiner);
    }

    public void setSchwarzAmZug() {
    }

    public void setWeissAmZug() {
    }

    public void setSpielerMeldungWeiss(String s){
        this.spielerZettelWeiss.setFehlermeldung(s);
    }

    public void setSpielerMeldungSchwarz(String s){
        this.spielerZettelSchwarz.setFehlermeldung(s);
    }

    public void setUndoErlaubt(boolean undoMoeglich) {
        this.Undo.setEnabled(undoMoeglich);
    }

    public void setRedoErlaubt(boolean redoMoeglich) {
        this.Redo.setEnabled(redoMoeglich);
    }

    public void gibFehlermeldungAus(String fehlertext) {
        System.out.println(fehlertext);
        JOptionPane.showMessageDialog(this, fehlertext);
    }

    public void mouseClicked(MouseEvent e) {
        if (this.dasBrett != null) {
            Point returnWert = this.dasBrett.berechneTreffer(e.getX(), e.getY());
            if (returnWert != null) {
                LoGoApp.meineSteuerung.klickAufFeld(returnWert.x, returnWert.y);
                mess = "klick auf " + returnWert.x + " | " + returnWert.y;
            } else {
                mess = "kein Treffer mit Clicked-Koordinaten: " + e.getX() + " | " + e.getY();
            }
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
     *
     * @param e
     * @see ActionListener
     */
    public void actionPerformed(ActionEvent e) {
        // Aktions die ueber die Menue-Leiste eingegeben werden
        if (e.getSource() == UeberLoGo) {
            this.buttonUeberLogoGedrueckt();
        } else if (e.getSource() == Einstellungen) {
            this.buttonEinstellungenGedrueckt();
        } else if (e.getSource() == NeuesSpiel) {
            this.buttonNeuesSpiel();
        } else if (e.getSource() == SpielLaden) {
            this.buttonSpielLadenGedrueckt();
        } else if (e.getSource() == SpielSpeichern) {
            this.buttonSpielSpeichernGedrueckt();
        } else if (e.getSource() == SpielBeenden) {
            this.buttonSpielBeendenGedrueckt();
        } else if (e.getSource() == Passen) {
            this.buttonPassen();
        } else if (e.getSource() == Aufgeben) {
            this.buttonAufgeben();
        } else if (e.getSource() == AuswertungBeenden) {
            this.buttonAuswertungBeendet();
        } else if (e.getSource() == Undo) {
            this.buttonUndoGedrueckt();
        } else if (e.getSource() == Redo) {
            this.buttonRedoGedrueckt();
        } else if (e.getSource() == Pause) {
            this.buttonSpielPausierenGedrueckt();
        } else if (e.getSource() == Fortsetzen) {
            this.buttonSpielFortsetzenGedrueckt();
        }
    }

    private void buttonNeuesSpiel() {
        // Die Entscheidung was geschieht obliegt der Steuerung
        LoGoApp.meineSteuerung.buttonNeuesSpiel();
    }

    private void buttonSpielPausierenGedrueckt() {
        // Infos siehe hier: http://forum.fachinformatiker.de/java/13120-kennt-jemand-layeredpane-glasspane-animationen.html
        LoGoApp.meineSteuerung.buttonPause();

        this.spielOberflaechePausiert = true;
        this.Pause.setEnabled(false);
        this.Fortsetzen.setEnabled(true);
        this.spielOberflaechePausiert = true;

        // Hier dann die Glass-Pane zeichnen / aktivieren
    }

    private void buttonSpielFortsetzenGedrueckt() {
        LoGoApp.meineSteuerung.buttonSpielForsetzen();

        this.spielOberflaechePausiert = false;
        this.Pause.setEnabled(true);
        this.Fortsetzen.setEnabled(false);
        this.spielOberflaechePausiert = false;


        // Hier dann die Glass-Pane wegnehmen / deaktivieren
    }

    /*  private void buttonNeuesSpielGedrueckt() {
    this.dasBrett = null;
    this.Pause.setEnabled(false);
    this.Fortsetzen.setEnabled(false);
    LoGoApp.meineSteuerung.buttonSpielStarten();
    }*/
    private void buttonSpielSpeichernGedrueckt() {
        LoGoApp.meineSteuerung.buttonSpielSpeichern();
    }

    private void buttonSpielLadenGedrueckt() {
        LoGoApp.meineSteuerung.buttonSpielLaden();
    }

    private void buttonSpielBeendenGedrueckt() {
        LoGoApp.meineSteuerung.buttonSpielBeenden();
    }

    private void buttonEinstellungenGedrueckt() {
    }

    private void buttonUeberLogoGedrueckt() {
    }

    private void buttonUndoGedrueckt() {
        LoGoApp.meineSteuerung.buttonUndo();
    }

    private void buttonRedoGedrueckt() {
        LoGoApp.meineSteuerung.buttonRedo();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
    }

    public void setPauseScreen(boolean setPause) {
        this.spielOberflaechePausiert = setPause;
    }

    private void buttonPassen() {
        LoGoApp.meineSteuerung.buttonPassen();
    }

    private void buttonAufgeben() {
        LoGoApp.meineSteuerung.buttonAufgeben();
    }

    private void buttonAuswertungBeendet() {
        LoGoApp.meineSteuerung.buttonAuswertungBeendet();
    }

    public void ergebnisAuszaehlenZeigen(String nameSchwarz, String nameWeiss, float komiFuerWeiss, int gebietsPunktSchwarz, int gebietsPunkteWeiss,
            int schwarzeGefangenImSpiel, int weisseGefangenImSpiel, int schwarzeSteineTotAufBrett, int weisseSteineTotAufBrett) {
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

    public void ergebnisAufgebenZeigen(String nameSchwarz, String nameWeiss, int konstanteFuerGewinner) {
        /* Erstmal zum Debugen */
        System.out.println("Name Schwarz: " + nameSchwarz);
        System.out.println("Name Weiss: " + nameWeiss);
        String gewinner;
        if (konstanteFuerGewinner == Konstante.SCHNITTPUNKT_SCHWARZ) {
            gewinner = "Schwarz";
        } else {
            gewinner = "Weiss";
        }
        System.out.println(gewinner + " gewinnt durch Aufgabe");
    }

    public void ergebnisAufZeitVerlorenZeigen(String nameSchwarz, String nameWeiss, int konstanteFuerGewinner) {
        /* Erstmal zum Debugen */
        System.out.println("Name Schwarz: " + nameSchwarz);
        System.out.println("Name Weiss: " + nameWeiss);
        String gewinner;
        if (konstanteFuerGewinner == Konstante.SCHNITTPUNKT_SCHWARZ) {
            gewinner = "Schwarz";
        } else {
            gewinner = "Weiss";
        }
        System.out.println(gewinner + " gewinnt durch Zeit");
    }

    public void visibleNeuesSpiel(boolean visible) {
        this.AuswertungBeenden.setEnabled(visible);
    }

    public void visibleSpielLaden(boolean visible) {
        this.SpielLaden.setEnabled(visible);
    }

    public void visibleSpielSpeichern(boolean visible) {
        this.SpielSpeichern.setEnabled(visible);
    }

    public void visiblePause(boolean visible) {
        this.Pause.setEnabled(visible);
    }

    public void visibleFortsetzen(boolean visible) {
        this.Fortsetzen.setEnabled(visible);
    }

    public void visiblePassen(boolean visible) {
        this.Passen.setEnabled(visible);
    }

    public void visibleAufgeben(boolean visible) {
        this.Aufgeben.setEnabled(visible);
    }

    public void visibleAuswertungBeenden(boolean visible) {
        this.AuswertungBeenden.setEnabled(visible);
    }
}
