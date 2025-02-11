package GUI;

import Klassen.Konstante;
import Interfaces.SpielerUhren;
import Interfaces.SpielerZettel;
import Interfaces.OberflaecheInterface;
import Sound.SoundLib;
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
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JOptionPane;
import logo.LoGoApp;

/**
 * Fenster fuer die Spielbrettoberflaeche
 * @author steven
 * @version 0.2
 */
public class FensterSpieloberflaeche extends Frame implements Runnable, KeyListener, OberflaecheInterface, MouseListener, ActionListener {

    // Wenn nicht anders angegeben, verwende diese Masse zum zeichnen des Spielbretts
    private final static int STANDARD_SPIELFELD_HOEHE = 496;
    private final static int STANDARD_SPIELFELD_BREITE = 496;
    private final static int STANDARD_SPIELFELD_XPOS = 497;
    private final static int STANDARD_SPIELFELD_YPOS = 158;
    private final static long STANDARD_ALARM_XX_SEK_VORSCHLUSS = 10*1000;

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
    /**
     * Der Menuebalken
     */
    protected MenuBar dieMenueBar;
    /**
     * Das Spiel-Informationen-Item
     */
    protected MenuItem SpielInfo;
    /**
     * Das Credits-Item, bei klick wird gezeigt, wer am Projekt beteiligt war
     */
    protected MenuItem Credits;
    /**
     * Das Hilfe-Item, bei klick soll sich die pdf ueber das Spiel oeffnen
     */
    protected MenuItem Hilfe;
    /**
     * Neues-Spiel-Item, bei klick kommt das Einstellungsfenster
     */
    protected MenuItem NeuesSpiel;
    /**
     * Spiel-Laden-Item, bei klick kann man ein Spiel laden
     */
    protected MenuItem SpielLaden;
    /**
     * Spiel-Speichern-Item, bei klick kann man das jetzige brett Speichern
     */
    protected MenuItem SpielSpeichern;
    /**
     * Spiel-Beenden-Item, bei klick kann man LoGo beenden
     */
    protected MenuItem SpielBeenden;
    /**
     * Undo-Item, bei klick geht das Spiel einen Zug zurueck
     */
    protected MenuItem Undo;
    /**
     * Redo-Item, bei klick geht das Spiel eine Zug vor
     */
    protected MenuItem Redo;
    /**
     * Passen-Item, bei klick wird gepasst
     */
    protected MenuItem Passen;
    /**
     * Aufgeben-Item, bei klick gibt der momentane Spieler auf
     */
    protected MenuItem Aufgeben;
    /**
     * Aufwertung-Beenden-Item, ist man in der Auswertung, so wird diese beendet
     */
    protected MenuItem AuswertungBeenden;
    /**
     * Pause-Item, bei klick pausiert das Spiel
     */
    protected MenuItem Pause;
    /**
     * Fortsetzen-Item, bei klick wird das Spiel fortgesetzt
     */
    protected MenuItem Fortsetzen;

    /* Double Buffering */
    String mess = "";

    /* Fuer das Spielinfo-Fenster muss der Name der Spieler gespeichert werden */
    String spielerSchwarzName;
    String spielerWeissName;

    /**
     * Konstruktor der Spieloberflaeche
     * @param pFenstername Name des Fensters
     */
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

    /**
     * Funktion zur Initialisierung, hier werden alle Werte voreingestellt.
     */
    public void init() {
        // Namen initialisieren
        this.spielerSchwarzName = "";
        this.spielerWeissName = "";

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
        this.spielerZettelWeiss = new SpielerZettelEinzeln(10, 575, -18.6, "WEISS", Konstante.SCHNITTPUNKT_WEISS);
        this.spielerZettelSchwarz = new SpielerZettelEinzeln(238, 424, 2.5, "SCHWARZ", Konstante.SCHNITTPUNKT_SCHWARZ);

        this.spielOberflaechePausiert = false;

        //this.setResizable(false);
        this.setSize(1024, 768);
        this.setResizable(false);
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
    }

    /**
     * Zum besseren Aussehen, ohne flimmern, muss ein Backbuffer erstellt werden
     */
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

    /**
     * Testen, ob der Backbuffer in ordnung ist.
     */
    protected void checkBackbuffer() {
        if (backbuffer == null) {
            createBackbuffer();
        }
        if (backbuffer.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE) {
            createBackbuffer();
        }
    }

    /**
     * Das Menue, also die Menueleiste, muss erstellt werden. Alle Komponenten
     * werden an der richtigen Stelle eingebunden
     * @param f Frame, in den das Menue eingebunden werden soll
     */
    public void createMenue(Frame f) {

        dieMenueBar = new MenuBar();

        // ------ LoGo-Menue -------
        Menu dasLoGoMenue = new Menu("LoGo");

        // Einstellungen
        SpielInfo = new MenuItem("Spielinformationen");
        SpielInfo.addActionListener(this);
        SpielInfo.setShortcut(new MenuShortcut(KeyEvent.VK_COMMA));
        dasLoGoMenue.add(SpielInfo);

        // Ueber Logo - Credits
        Credits = new MenuItem("Credits");
        Credits.addActionListener(this);
        Credits.setShortcut(new MenuShortcut(KeyEvent.VK_A));
        dasLoGoMenue.add(Credits);

        // Hilfe
        this.Hilfe = new MenuItem("Hilfe");
        this.Hilfe.addActionListener(this);
        this.Hilfe.setShortcut(new MenuShortcut(KeyEvent.VK_H));
        dasLoGoMenue.add(this.Hilfe);


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
        Redo = new MenuItem("Spielzug wiederherstellen");
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

    /**
     * Zeit zwischen 2 Threadaufrufen muss fuer eine fluessigere Animation
     * berechnet werden.
     */
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

    /**
     * die Oberflaeche wird gezeichnet, diese funktion wird in paint aufgerufen
     * @param g
     */
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

    /**
     * Um den Thread zu starten, muss diese Funktion aufgerufen werden. Wenn der
     * Tread noch nicht laeuft, startet er.
     */
    public void start() {
        // Thread anstoßen
        if (!once) {
            once = true;
            Thread t = new Thread(this);
            t.start();
        }
    }

    /**
     * Um den Tread der Oberflaeche zu stoppen.
     */
    public void stop() {
        this.threadLaeuf = false;
    }

    /**
     * Ruft die Logik-Methode im Brett auf, so werden die Steine gleichmaessig
     * gezeichnet
     * @param timePassed Zeit, die seit dem letzten zeichnen vergangen ist.
     */
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
            case KeyEvent.VK_H:
                LoGoApp.meineSteuerung.buttonZeigeHilfeGedrueckt();
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

        if( periodenZeitInMS <= FensterSpieloberflaeche.STANDARD_ALARM_XX_SEK_VORSCHLUSS){
            if( periodenZeitInMS > (STANDARD_ALARM_XX_SEK_VORSCHLUSS-1000)){
                SoundLib.getInstance().playSound("alarm");
            }
        }
    }

    public void setAnzeigePeriodenZeitSchwarz(long periodenZeitInMS) {
        if (this.spielerUhrSchwarz != null) {
            this.spielerUhrSchwarz.restzeitInMS(periodenZeitInMS);
        }

        if( periodenZeitInMS <= FensterSpieloberflaeche.STANDARD_ALARM_XX_SEK_VORSCHLUSS){
            if( periodenZeitInMS > (STANDARD_ALARM_XX_SEK_VORSCHLUSS-1000)){
                SoundLib.getInstance().playSound("alarm");
            }
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
        this.spielerWeissName = spielername;
    }

    public void setSpielernameSchwarz(String spielername) {
        this.spielerZettelSchwarz.setSpielername(spielername);
        this.spielerSchwarzName = spielername;
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

    public void setSpielerMeldungWeiss(String s) {
        this.spielerZettelWeiss.setInfoBox(s);
    }

    public void setSpielerMeldungSchwarz(String s) {
        this.spielerZettelSchwarz.setInfoBox(s);
    }

    public void setUndoErlaubt(boolean undoMoeglich) {
        this.Undo.setEnabled(undoMoeglich);
    }

    public void setRedoErlaubt(boolean redoMoeglich) {
        this.Redo.setEnabled(redoMoeglich);
    }

    public void gibFehlermeldungAus(String fehlertext) {
        if (LoGoApp.debug) {
            System.out.println(fehlertext);
        }
        JOptionPane.showMessageDialog(this, fehlertext);
    }

    /**
     * @param e
     * @see MouseListener
     */
    public void mouseClicked(MouseEvent e) {

        if (this.dasBrett != null) {
            Point returnWert = this.dasBrett.berechneTreffer(e.getX(), e.getY());
            if (returnWert != null) {
                LoGoApp.meineSteuerung.klickAufFeld(returnWert.x, returnWert.y);
                mess = "klick auf " + returnWert.x + " | " + returnWert.y;
            } else {
                mess = "kein Treffer mit Clicked-Koordinaten: " + e.getX() + " | " + e.getY();
            }
        } else {
            // Start-Screen wird angezeigt.
            // Bei einem Klick starte sofort mit Schnellstartmode
            LoGoApp.meineSteuerung.buttonNeuesSchnellstartSpiel();
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
     * Aktion-events abfangen, implementierung des Interfaces
     * @param e Eingegangenes Action-Event
     * @see ActionListener
     */
    public void actionPerformed(ActionEvent e) {
        // Aktions die ueber die Menue-Leiste eingegeben werden
        if (e.getSource() == Credits) {
            this.zeigeCredits();
        } else if (e.getSource() == SpielInfo) {
            this.buttonSpielInfoGedrueckt();
        } else if(e.getSource() == Hilfe){
            this.buttonHilfe();
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
    }

    private void buttonSpielFortsetzenGedrueckt() {
        LoGoApp.meineSteuerung.buttonSpielForsetzen();

        this.spielOberflaechePausiert = false;
        this.Pause.setEnabled(true);
        this.Fortsetzen.setEnabled(false);
        this.spielOberflaechePausiert = false;
    }

    private void buttonSpielSpeichernGedrueckt() {
        LoGoApp.meineSteuerung.buttonSpielSpeichern();
    }

    private void buttonSpielLadenGedrueckt() {
        LoGoApp.meineSteuerung.buttonSpielLaden();
    }

    private void buttonSpielBeendenGedrueckt() {
        LoGoApp.meineSteuerung.buttonSpielBeenden();
    }

    private void buttonSpielInfoGedrueckt() {
        /* Jetzt werden informationen zum Spiel ausgegeben */
        long stundenSchwarz  = LoGoApp.meineSteuerung.getStartHauptzeitSchwarz()/(1000*60*60);
        long minutenSchwarz  = LoGoApp.meineSteuerung.getStartHauptzeitSchwarz()/(1000*60) - stundenSchwarz*60;
        long stundenWeiss    = LoGoApp.meineSteuerung.getStartHauptzeitWeiss()/(1000*60*60);
        long minutenWeiss    = LoGoApp.meineSteuerung.getStartHauptzeitWeiss()/(1000*60) - stundenWeiss * 60;
        long minutenPeriod   = LoGoApp.meineSteuerung.getPeriodenZeit()/(1000*60);
        long sekundenPeriod  = LoGoApp.meineSteuerung.getPeriodenZeit()/(1000) - 60*minutenPeriod;

        String ausgabe = "";
        ausgabe += "Spielername Schwarz: " + this.spielerSchwarzName + "\n";
        ausgabe += "Spielername Weiß: " + this.spielerWeissName + "\n";
        ausgabe += "\n";
        if(LoGoApp.meineSteuerung.getIgnoreTime() == false){
            ausgabe += "Gesamtzeit Schwarz: " + stundenSchwarz + " Stunden, " + minutenSchwarz + " Minuten\n";
            ausgabe += "Gesamtzeit Weiß: " + stundenWeiss + " Stunden, " + minutenWeiss + " Minuten\n";
            ausgabe += "Byo-Yomi: " + minutenPeriod + " Minuten, " + sekundenPeriod + " Sekunden\n";
            ausgabe += "\n";
        }
        NumberFormat numberFormat = new DecimalFormat("0.00");
        numberFormat.setRoundingMode(RoundingMode.DOWN);
        ausgabe += "Komi: " + numberFormat.format(LoGoApp.meineSteuerung.getKomiWeiss()) + "\n";
        JOptionPane.showMessageDialog(this, ausgabe);
    }

    private void buttonCreditsGedrueckt() {
        LoGoApp.meineSteuerung.buttonZeigeHilfeGedrueckt();
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

    public void schwarzInPeriodenZeit(boolean b) {
        if(this.spielerZettelSchwarz != null){
            this.spielerZettelSchwarz.setInPeriodenZeit(b);
        }
    }

    public void weissInPeriodenZeit(boolean b) {
        if(this.spielerZettelWeiss != null){
            this.spielerZettelWeiss.setInPeriodenZeit(b);
        }
    }

    private void buttonHilfe() {
        LoGoApp.meineSteuerung.buttonZeigeHilfeGedrueckt();
    }

    private void zeigeCredits() {
        LoGoApp.meineSteuerung.buttonZeigeCreditsGedrueckt();
    }
}
