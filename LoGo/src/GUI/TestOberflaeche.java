package GUI;

import interfaces.OberflaecheInterface;
import interfaces.SpielerUhren;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;
import logo.LoGoApp;

/**
 *
 * @author steven
 * @version 0.2
 */
public class TestOberflaeche extends JFrame implements Runnable, KeyListener, OberflaecheInterface, MouseListener, ActionListener {

    // Wenn nicht anders angegeben, verwende diese Masse zum zeichnen des Spielbretts
    private final static int STANDARD_SPIELFELD_HOEHE = 496;
    private final static int STANDARD_SPIELFELD_BREITE = 496;
    private final static int STANDARD_SPIELFELD_XPOS = 496;
    private final static int STANDARD_SPIELFELD_YPOS = 158;

    private BufferedImage backgroundImage;
    private boolean threadLaeuf;
    private static boolean once = false;
    private boolean spielOberflaechePausiert = false;
    // GUI-Teile
    private Spielbrett dasBrett;
    private SpielerUhren spielerUhrSchwarz;
    private SpielerUhren spielerUhrWeiss;
    protected JMenuBar dieMenueBar;
    protected JMenuItem Einstellungen;
    protected JMenuItem UeberLoGo;
    protected JMenuItem SpielLaden;
    protected JMenuItem SpielSpeichern;
    protected JMenuItem SpielBeenden;
    protected JMenuItem Undo;
    protected JMenuItem Redo;
    protected JMenuItem Pause;
    protected JMenuItem Fortsetzen;

    /* Double Buffering */
    String mess = "";

    public TestOberflaeche(String pFenstername) {
        super(pFenstername);

        init();

        this.start();
    }

    public void init() {

        // Menue-Bar erstellen
        createMenue(this);

        // Schwere und leichte Komponenten
        // JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        // ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);

        /* Buffern */
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setUndecorated(true);
        this.setSize(1024, 768);
        // this.backgroundImage = GrafikLib.getInstance().getSprite("GUI/resources/SpielTisch.jpg");
        this.backgroundImage = GrafikLib.getInstance().getSprite("GUI/resources/SpielTisch2.jpg");
        this.spielerUhrSchwarz = new SpielerUhr(316, 215, 0, 4.5);
        this.spielerUhrWeiss = new SpielerUhr(111, 124, 0, 1);

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
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                //System.exit(0);
                LoGoApp.meineSteuerung.buttonSpielBeenden();
        } });
    }

    public void createMenue(JFrame f) {

        dieMenueBar = new JMenuBar();

        // ------ LoGo-Menue -------
        JMenu dasLoGoMenue = new JMenu("LoGo");

        // Einstellungen
        Einstellungen = new JMenuItem("Einstellungen");
        Einstellungen.addActionListener(this);
        setMenuAccelerator(Einstellungen, ',');
        dasLoGoMenue.add(Einstellungen);

        // Ueber
        UeberLoGo = new JMenuItem("Über LoGo");
        UeberLoGo.addActionListener(this);
        setMenuAccelerator(UeberLoGo, 'a');
        dasLoGoMenue.add(UeberLoGo);

        // ------ Spiel-Menue -------
        JMenu dasSpielMenue = new JMenu("Spiel");

        // Spiel Laden
        SpielLaden = new JMenuItem("Spiel laden");
        SpielLaden.addActionListener(this);
        setMenuAccelerator(SpielLaden, 'l');
        dasSpielMenue.add(SpielLaden);

        // Spiel Speichern
        SpielSpeichern = new JMenuItem("Spiel speichern");
        SpielSpeichern.addActionListener(this);
        setMenuAccelerator(SpielSpeichern, 's');
        dasSpielMenue.add(SpielSpeichern);

        // Spiel Speichern
        SpielBeenden = new JMenuItem("Spiel beenden");
        SpielBeenden.addActionListener(this);
        setMenuAccelerator(SpielBeenden, 'q');
        dasSpielMenue.add(SpielBeenden);

        // Trenner
        dasSpielMenue.addSeparator();

        // Spielzug Undo
        Undo = new JMenuItem("Spielzug rückgängig");
        Undo.addActionListener(this);
        Undo.setEnabled(false);
        dasSpielMenue.add(Undo);

        // Spielzug Redo
        Redo = new JMenuItem("Spielzug wieder herstellen");
        Redo.addActionListener(this);
        Redo.setEnabled(false);
        dasSpielMenue.add(Redo);

        // Trenner
        dasSpielMenue.addSeparator();

        // Spielzug Undo
        Pause = new JMenuItem("Spiel pausieren");
        Pause.addActionListener(this);
        setMenuAccelerator(Pause, 'p');
        Pause.setEnabled(false);
        dasSpielMenue.add(Pause);

        // Spielzug Redo
        Fortsetzen = new JMenuItem("Spiel fortsetzen");
        Fortsetzen.addActionListener(this);
        setMenuAccelerator(Fortsetzen, 'p');
        Fortsetzen.setEnabled(false);
        dasSpielMenue.add(Fortsetzen);



        dieMenueBar.add(dasLoGoMenue);
        dieMenueBar.add(dasSpielMenue);
        f.setJMenuBar(dieMenueBar);
    }

    protected void setMenuAccelerator(JMenuItem pMenuItem, char pMnemonic) {
        // Bei Windows und Linux mit STR, bei Apple mit Apfel
        KeyStroke ks = KeyStroke.getKeyStroke(pMnemonic, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        pMenuItem.setAccelerator(ks);
    }

    public void run() {
        long startTime = System.nanoTime();
        long cumTime = startTime;

        while (this.threadLaeuf) {
            long timePassed = System.nanoTime() - cumTime;
            cumTime += timePassed;

            this.doLogic(timePassed);
            this.drawStuff();
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
            }
        }
    }

    private void drawStuff() {
        BufferStrategy bf = this.getBufferStrategy();
        Graphics g = null;

        try {
            g = bf.getDrawGraphics();
            super.paint(g);
            // g.setColor(this.getBackground());
            // g.fillRect(0, dieMenueBar.getHeight() + this.getInsets().top, this.getWidth(), this.getHeight());

            if( this.backgroundImage != null ){
                g.drawImage(this.backgroundImage, 0, 0, null);
            }


            if (this.dasBrett != null) {
                this.dasBrett.drawObjects(g);
            }

            if (this.spielerUhrSchwarz != null){
                this.spielerUhrSchwarz.zeichneZeiger(g);
            }

            if( this.spielerUhrWeiss != null ){
                this.spielerUhrWeiss.zeichneZeiger(g);
            }

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("MESS: " + mess, 55, 560);
        } finally {
            // Am Ende Disposen ist am besten ;)
            g.dispose();
        }

        // Shows the contents of the backbuffer on the screen.
        bf.show();

        //Tell the System to do the Drawing now, otherwise it can take a few extra ms until
        //Drawing is done which looks very jerky
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void update(Graphics g) {
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

    public void setBrettOberflaeche(int[][] spielfeld, int spielfeldGroesse, Point markierterStein ) {
        // Spielfeld updaten wenn es von der gleichen groesse ist, ansonsten
        // ein neues Spielfeld erstellen
        if (this.dasBrett != null && this.dasBrett.getAnzahlFelder() == spielfeldGroesse) {
            this.dasBrett.updateSpielFeld(spielfeld);
        } else {
            this.dasBrett = new Spielbrett(STANDARD_SPIELFELD_BREITE,
                    STANDARD_SPIELFELD_HOEHE,
                    STANDARD_SPIELFELD_XPOS,
                    STANDARD_SPIELFELD_YPOS,
                    spielfeldGroesse,
                    null);
            this.dasBrett.updateSpielFeld(spielfeld);
            this.dasBrett.setMarkierterStein(markierterStein);
            this.Pause.setEnabled(true);
            // Undo und Redo muessen erlaubt sein, fuer den Fall das ein Spiel geladen wurde
            // this.Undo.setEnabled(false);
            // this.Redo.setEnabled(false);
        }

    }

    public void setAnzeigePeriodenZeitWeiss(long periodenZeitInMS) {
        if(this.spielerUhrWeiss != null ){
            this.spielerUhrWeiss.restzeitInMS(periodenZeitInMS);
        }
    }

    public void setAnzeigePeriodenZeitSchwarz(long periodenZeitInMS) {
        if(this.spielerUhrSchwarz != null ){
            this.spielerUhrSchwarz.restzeitInMS(periodenZeitInMS);
        }
    }

    public void setAnzeigeSpielerZeitWeiss(long spielerZeitInMS) {
        if(this.spielerUhrWeiss != null ){
            this.spielerUhrWeiss.restzeitInMS(spielerZeitInMS);
        }
    }

    public void setAnzeigeSpielerZeitSchwarz(long spielerZeitInMS) {
        if(this.spielerUhrSchwarz != null ){
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
        } else if( e.getSource() == SpielBeenden ){
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

        // Hier dann die Glass-Pane zeichnen / aktivieren
    }

    private void buttonSpielFortsetzenGedrueckt() {
        LoGoApp.meineSteuerung.buttonSpielForsetzen();
        
        this.spielOberflaechePausiert = false;
        this.Pause.setEnabled(true);
        this.Fortsetzen.setEnabled(false);

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

    private void buttonSpielBeendenGedrueckt(){
        LoGoApp.meineSteuerung.buttonSpielBeenden();
    }

    private void buttonEinstellungenGedrueckt() {
    }

    private void buttonUeberLogoGedrueckt() {
    }

    private void buttonUndoGedrueckt() {
    }

    private void buttonRedoGedrueckt() {
    }

}
