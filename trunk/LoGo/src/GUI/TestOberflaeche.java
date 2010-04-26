package GUI;

import interfaces.OberflaecheInterface;
import interfaces.SpielerUhren;
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
import java.awt.Toolkit;
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
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import logo.LoGoApp;

/**
 *
 * @author steven
 * @version 0.2
 */
public class TestOberflaeche extends Frame implements Runnable, KeyListener, OberflaecheInterface, MouseListener, ActionListener {

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
    protected MenuBar dieMenueBar;
    protected MenuItem Einstellungen;
    protected MenuItem UeberLoGo;
    protected MenuItem SpielLaden;
    protected MenuItem SpielSpeichern;
    protected MenuItem SpielBeenden;
    protected MenuItem Undo;
    protected MenuItem Redo;
    protected MenuItem Pause;
    protected MenuItem Fortsetzen;
    protected BackgroundImagePanel backgroundPanel;

    /* Double Buffering */
    String mess = "";

    public TestOberflaeche(String pFenstername) {
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

        // Menue-Bar erstellen
        createMenue(this);

        last = System.nanoTime(); // Ohne Initialisierung stimmt die Berechnung
        // von delta nicht!!!
        berechneDelta(); // delta wird unten bei den Images benötigt


        backgroundPanel = new BackgroundImagePanel(
                GrafikLib.getInstance().getSprite("GUI/resources/SpielTisch2.jpg"));


        // Schwere und leichte Komponenten
        //  JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        // ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);

        /* Buffern */
        //  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setUndecorated(true);
        this.setSize(1024, 768);
        // this.backgroundImage = GrafikLib.getInstance().getSprite("GUI/resources/SpielTisch.jpg");
        this.backgroundImage = GrafikLib.getInstance().getSprite("GUI/resources/SpielTisch2.jpg");


        this.spielerUhrSchwarz = new SpielerUhr(316, 215, 0, 4.5);
        this.spielerUhrWeiss = new SpielerUhr(112, 144, 0, 1);
        this.spielOberflaechePausiert = false;

        setLocationRelativeTo(null); // Fenster zentrieren
        //this.setResizable(false);
        this.setVisible(true);
        this.setBackground(Color.ORANGE);
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

        // Spiel Speichern
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

        // Spielzug Undo
        Pause = new MenuItem("Spiel pausieren");
        Pause.addActionListener(this);
        Pause.setShortcut(new MenuShortcut(KeyEvent.VK_P));
        Pause.setEnabled(false);
        dasSpielMenue.add(Pause);

        // Spielzug Redo
        Fortsetzen = new MenuItem("Spiel fortsetzen");
        Fortsetzen.addActionListener(this);
        Fortsetzen.setShortcut(new MenuShortcut(KeyEvent.VK_P));
        Fortsetzen.setEnabled(false);
        dasSpielMenue.add(Fortsetzen);



        dieMenueBar.add(dasLoGoMenue);
        dieMenueBar.add(dasSpielMenue);
        this.setMenuBar(dieMenueBar);

    }

    protected void setMenuAccelerator(JMenuItem pMenuItem, char pMnemonic) {
        // Bei Windows und Linux mit STR, bei Apple mit Apfel
        KeyStroke ks = KeyStroke.getKeyStroke(pMnemonic, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        pMenuItem.setAccelerator(ks);
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

        if (this.dasBrett != null) {
            dasBrett.paintComponents(g);
        }

        if (spielerUhrSchwarz != null) {
            this.spielerUhrSchwarz.zeichneZeiger(g);
        }

        if (this.spielerUhrWeiss != null) {
            this.spielerUhrWeiss.zeichneZeiger(g);
        }

        if(this.spielOberflaechePausiert){
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            if(this.pauseImage != null){
                g.drawImage(pauseImage, (this.getWidth()-pauseImage.getWidth())/2,
                        (this.getHeight()-pauseImage.getHeight())/2, this);
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
                // neues Spiel
                LoGoApp.meineSteuerung.buttonSpielStarten();
                mess = "Steuerung => Spiel starten";
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
                e.consume(); // Kombinierte Tasten sollen nicht behandlet werden.
        }
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        mess = "Released: " + KeyEvent.getKeyText(keyCode);
        e.consume();
    }

    public void setBrettOberflaeche(int[][] spielfeld, int spielfeldGroesse, Point markierterStein) {
        // Spielfeld updaten wenn es von der gleichen groesse ist, ansonsten
        // ein neues Spielfeld erstellen
        if (this.dasBrett != null && this.dasBrett.getAnzahlFelder() == spielfeldGroesse) {
            this.dasBrett.updateSpielFeld(spielfeld);
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
    }

    public void setSpielernameSchwarz(String spielername) {
    }

    public void setGefangeneSteineWeiss(int anzGefangenerSteiner) {
    }

    public void setGefangeneSteineSchwarz(int anzGefangenerSteiner) {
    }

    public void setSchwarzAmZug() {
    }

    public void setWeissAmZug() {
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
        } else if (e.getSource() == SpielLaden) {
            this.buttonSpielLadenGedrueckt();
        } else if (e.getSource() == SpielSpeichern) {
            this.buttonSpielSpeichernGedrueckt();
        } else if (e.getSource() == SpielBeenden) {
            this.buttonSpielBeendenGedrueckt();
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

    private void buttonNeuesSpielGedrueckt() {
        this.dasBrett = null;
        this.Pause.setEnabled(false);
        this.Fortsetzen.setEnabled(false);
        LoGoApp.meineSteuerung.buttonSpielStarten();
    }

    private void buttonSpielSpeichernGedrueckt() {
        LoGoApp.meineSteuerung.buttonSpielSpeichern();
    }

    private void buttonSpielLadenGedrueckt() {
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
        ;
    }

    private void buttonRedoGedrueckt() {
        LoGoApp.meineSteuerung.buttonRedo();
        ;
    }
}
