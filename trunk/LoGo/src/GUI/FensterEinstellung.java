package GUI;

import Klassen.Konstante;
import Klassen.Laden;
import Klassen.Spieler;
import Klassen.Spielfeld;
import java.awt.FocusTraversalPolicy;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import logo.LoGoApp;

/**
 *
 * @author tommy
 * @version 0.1
 */
public class FensterEinstellung extends JFrame implements MouseListener, ActionListener {

    /* Felder auf der GUI
     * Zur darstellung der Daten
     */
    private JTextField spielerNameWeiss;
    private JTextField spielerZeitStundenWeiss;
    private JTextField spielerZeitMinutenWeiss;
    private JTextField spielerKomiWeiss;
    private JTextField spielerNameSchwarz;
    private JTextField spielerZeitStundenSchwarz;
    private JTextField spielerZeitMinutenSchwarz;
    private JComboBox spielermodus;
    private JCheckBox spielMitZeitSpielen;
    private JTextField periodenZeitMinuten;
    private JTextField periodenZeitSekunden;
    private ButtonGroup spielFeldAuswahl;
    private JRadioButton siebenXsieben;
    private JRadioButton neunXneun;
    private JRadioButton elfXelf;
    private JRadioButton dreizehnXdreizehn;
    private JRadioButton fuenfzehnXfuenfzehn;
    private JRadioButton siebzehnXsiebzehn;
    private JRadioButton neunzehnXneunzehn;
    private JComboBox spielVorgabeSteine;
    private JLabel spielBrettHinweise;
    private JButton spielStarten;
    private JButton hilfeButton;
    private JLabel labelSpielmodus;
    private JLabel labelPeriodenzeit;
    private JLabel labelMinutenP;
    private JLabel labelSekundenP;
    private JLabel labelNameSchwarz;
    private JLabel labelZeitSchwarz;
    private JLabel labelMinutenS;
    private JLabel labelStundenS;
    private JLabel labelNameWeiss;
    private JLabel labelZeitWeiss;
    private JLabel labelMinutenW;
    private JLabel labelStundenW;
    private JLabel labelKomi;
    private JLabel labelGroessenWahl;
    private JLabel labelVorgabe;
    /* Die Benutzereingaben muessen verwaltet werden */
    private String errorString;
    private boolean fehlerBeiEingabe;
    private Spielfeld dasSpielfeld;
    private EinstellungSpielbrett dasSpielfeldGUI;
    private int brettbreite;
    private int brettXOffset;
    private int brettYOffset;
    private int bretthoehe;
    private int paneloffset;
    private int seitenoffset;
    private String momSpielModus;

    /* Zum zeichnen des Feldes */
    private int frameMinhoehe = 270;
    private int frameMaxhoehe = 768;

    public FensterEinstellung() {
        this("Einstellungsfenster");
    }

    public FensterEinstellung(String fenstername) {
        super(fenstername);
        this.init();
        pack();
        this.setSize(650, this.frameMinhoehe);
        this.setResizable(false);
        this.setLocationRelativeTo(null);       // Fenster zentrieren
        this.setVisible(true);

        // Programm bei klick auf den roten Knopf nicht beenden sondern Event weiter verarbeiten
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                /*
                 * Wenn die Spieloberflaeche sichtbar ist, soll die Steuerung für
                 * das Beenden verantwortlich gemacht werden. Ansonsten darf das
                 * Einstellungsfenster direkt das Programm beenden
                 */
                if( ((FensterSpieloberflaeche)LoGoApp.meineOberflaeche).isVisible() ){
                    LoGoApp.meineSteuerung.buttonSpielBeenden();
                }else{
                    System.exit(0);
                }
            }
        });
    }

    /**
     * Initialisieren der Werte. Hier werden die Initialfunktionen in der richtigen
     * Reihenfolge aufgerufen, bevor der Benutzer agieren kann.
     */
    private void init() {
        this.addMouseListener(this);
        this.startwerteSetzen();
        this.radioButtonsInGroup();
        this.comboBoxenInit();
        this.zumActionListenerAdden();
        this.neuesSpielfeld();
        this.fensterRendern();
        this.tabReihenfolge();
        this.komponentenAufContentPane();
    }

    /**
     * Hier werden die Label, Buttons und Textfelder initialisiert.
     * Beschriftungen und andere textuelle Dinge
     */
    private void startwerteSetzen() {
        this.errorString = "";
        this.momSpielModus = "Schnellstart";
        this.fehlerBeiEingabe = false;
        this.dasSpielfeld = new Spielfeld(13);

        /* Initialisieren der Oberflaeche */
        this.spielerNameWeiss = new JTextField("Weiss");
        this.spielerNameWeiss.setToolTipText("Name des weißen Spielers eingeben");
        this.spielerZeitMinutenWeiss = new JTextField("30");
        this.spielerZeitMinutenWeiss.setToolTipText("Hauptzeit: Minuten des weißen Spielers");
        this.spielerZeitStundenWeiss = new JTextField("0");
        this.spielerZeitStundenWeiss.setToolTipText("Hauptzeit: Stunden des weißen Spielers");
        this.spielerKomiWeiss = new JTextField("6.5");
        this.spielerKomiWeiss.setToolTipText("Ausgleichspunkte (Komi) für Weiß");

        this.spielerNameSchwarz = new JTextField("Schwarz");
        this.spielerNameSchwarz.setToolTipText("Name des schwarzen Spielers eingeben");
        this.spielerZeitMinutenSchwarz = new JTextField("30");
        this.spielerZeitMinutenSchwarz.setToolTipText("Hauptzeit: Minuten des schwarzen Spielers");
        this.spielerZeitStundenSchwarz = new JTextField("0");
        this.spielerZeitStundenSchwarz.setToolTipText("Hauptzeit: Stunden des schwarzen Spielers");

        this.periodenZeitMinuten = new JTextField("1");
        this.periodenZeitMinuten.setToolTipText("Byo-Yomi: Minuten beider Spieler");
        this.periodenZeitSekunden = new JTextField("0");
        this.periodenZeitSekunden.setToolTipText("Byo-Yomi: Sekunden beider Spieler");

        this.spielermodus = new JComboBox();
        this.spielermodus.setToolTipText("Wählen Sie einen Spielmodus, oder laden Sie ein bestehendes Spiel");
        this.spielMitZeitSpielen = new JCheckBox("Zeit setzen", true);
        this.spielMitZeitSpielen.setToolTipText("Mit oder ohne Zeitbegrenzung spielen");
        this.spielMitZeitSpielen.setSelected(true);

        this.spielFeldAuswahl = new ButtonGroup();
        this.siebenXsieben = new JRadioButton("7 x 7");
        this.siebenXsieben.setToolTipText("Feldgröße 7x7");
        this.neunXneun = new JRadioButton("9 x 9");
        this.neunXneun.setToolTipText("Feldgröße 9x9");
        this.elfXelf = new JRadioButton("11 x 11");
        this.elfXelf.setToolTipText("Feldgröße 11x11");
        this.dreizehnXdreizehn = new JRadioButton("13 x 13", true);
        this.dreizehnXdreizehn.setToolTipText("Feldgröße 13x13");
        this.fuenfzehnXfuenfzehn = new JRadioButton("15 x 15");
        this.fuenfzehnXfuenfzehn.setToolTipText("Feldgröße 15x15");
        this.siebzehnXsiebzehn = new JRadioButton("17 x 17");
        this.siebzehnXsiebzehn.setToolTipText("Feldgröße 17x17");
        this.neunzehnXneunzehn = new JRadioButton("19 x 19");
        this.neunzehnXneunzehn.setToolTipText("Feldgröße 19x19");

        this.spielVorgabeSteine = new JComboBox();
        this.spielVorgabeSteine.setToolTipText("Wählen Sie die Zahl der Vorgabesteine");
        this.spielBrettHinweise = new JLabel("Schnellstart");
        this.spielBrettHinweise.setToolTipText("Hinweise zur Bedienung");
        this.spielStarten = new JButton("Start");
        this.spielStarten.setToolTipText("Das Spiel starten");
        this.hilfeButton = new JButton("Hilfe");
        this.hilfeButton.setToolTipText("Hilf zum Spiel");

        /* Zusaetzliche Labels */
        this.labelSpielmodus = new JLabel("Spielmodus", JLabel.LEFT);
        this.labelPeriodenzeit = new JLabel("Byo-Yomi", JLabel.LEFT);
        this.labelMinutenP = new JLabel("Min", JLabel.LEFT);
        this.labelSekundenP = new JLabel("Sek", JLabel.LEFT);
        this.labelNameSchwarz = new JLabel("Spieler Schwarz", JLabel.LEFT);
        this.labelZeitSchwarz = new JLabel("Zeit Schwarz", JLabel.LEFT);
        this.labelMinutenS = new JLabel("Min", JLabel.LEFT);
        this.labelStundenS = new JLabel("Std", JLabel.LEFT);
        this.labelNameWeiss = new JLabel("Spieler Weiß", JLabel.LEFT);
        this.labelZeitWeiss = new JLabel("Zeit Weiß", JLabel.LEFT);
        this.labelMinutenW = new JLabel("Min", JLabel.LEFT);
        this.labelStundenW = new JLabel("Std", JLabel.LEFT);
        this.labelKomi = new JLabel("Komi:", JLabel.LEFT);
        this.labelGroessenWahl = new JLabel("Brettgröße", JLabel.LEFT);
        this.labelVorgabe = new JLabel("Vorgabe", JLabel.LEFT);

        /* Zum Spielbrett */
        this.brettbreite = 496;
        this.bretthoehe = 496;
        this.brettXOffset = 5;
        this.brettYOffset = 240;
        this.dasSpielfeldGUI = new EinstellungSpielbrett(this.brettbreite, this.bretthoehe, this.brettXOffset, this.brettYOffset, 13);
    }

    /**
     * Die schon definierten Komponenten werden nun auf die ContentPane
     * gelegt
     */
    private void komponentenAufContentPane() {
        this.getContentPane().add(this.spielerNameWeiss);
        this.getContentPane().add(this.spielerZeitMinutenWeiss);
        this.getContentPane().add(this.spielerZeitStundenWeiss);
        this.getContentPane().add(this.spielerKomiWeiss);
        this.getContentPane().add(this.spielerNameSchwarz);
        this.getContentPane().add(this.spielerZeitMinutenSchwarz);
        this.getContentPane().add(this.spielerZeitStundenSchwarz);
        this.getContentPane().add(this.periodenZeitMinuten);
        this.getContentPane().add(this.periodenZeitSekunden);
        this.getContentPane().add(this.spielermodus);
        this.getContentPane().add(this.spielMitZeitSpielen);
        this.getContentPane().add(this.siebenXsieben);
        this.getContentPane().add(this.neunXneun);
        this.getContentPane().add(this.elfXelf);
        this.getContentPane().add(this.dreizehnXdreizehn);
        this.getContentPane().add(this.fuenfzehnXfuenfzehn);
        this.getContentPane().add(this.siebzehnXsiebzehn);
        this.getContentPane().add(this.neunzehnXneunzehn);
        this.getContentPane().add(this.spielVorgabeSteine);
        this.getContentPane().add(this.spielBrettHinweise);
        this.getContentPane().add(this.spielStarten);
        this.getContentPane().add(this.hilfeButton);
        this.getContentPane().add(this.spielVorgabeSteine);
        this.getContentPane().add(this.spielBrettHinweise);
        this.getContentPane().add(labelSpielmodus);
        this.getContentPane().add(labelPeriodenzeit);
        this.getContentPane().add(labelMinutenP);
        this.getContentPane().add(labelSekundenP);
        this.getContentPane().add(labelNameSchwarz);
        this.getContentPane().add(labelZeitSchwarz);
        this.getContentPane().add(labelMinutenS);
        this.getContentPane().add(labelStundenS);
        this.getContentPane().add(labelNameWeiss);
        this.getContentPane().add(labelZeitWeiss);
        this.getContentPane().add(labelMinutenW);
        this.getContentPane().add(labelStundenW);
        this.getContentPane().add(labelKomi);
        this.getContentPane().add(labelGroessenWahl);
        this.getContentPane().add(labelVorgabe);
        this.getContentPane().add(new JLabel("")); // dirty, aber so klappts
        this.getContentPane().add(this.dasSpielfeldGUI); // muss noch kommen <-----------
    }

    private void tabReihenfolge() {
        // Tabreihenfolgen wird festgelegt


        FocusTraversalPolicy policy = TabTravel.getFocusTraversal(new JComponent[] {
            this.spielerNameSchwarz,
            this.spielerNameWeiss ,
            this.periodenZeitMinuten,
            this.periodenZeitSekunden,
            this.spielerZeitStundenSchwarz,
            this.spielerZeitMinutenSchwarz,
            this.spielerZeitStundenWeiss,
            this.spielerZeitMinutenWeiss,
            this.spielerKomiWeiss,
            this.siebenXsieben,
            this.neunXneun,
            this.elfXelf,
            this.dreizehnXdreizehn,
            this.fuenfzehnXfuenfzehn,
            this.siebzehnXsiebzehn,
            this.neunzehnXneunzehn,
            this.spielStarten,
            this.hilfeButton,
            this.spielermodus});

        this.setFocusTraversalPolicy(policy);
        this.setFocusCycleRoot(true);
    }

    /** 
     * Die Komponenten muessen einen genau definierten Ort bekommen
     */
    private void fensterRendern() {
        int horAbs = 20; // horizontaler Abstand
        int verAbs = 5; // vertikaler Abstand
        int textFH = 20; // Textfeldhoehe
        int labH = 30; // Labelhoehe
        int cBoxH = 30; // Checkboxhoehe
        int labelTFA = 5; // Abstand zwischen Label und Links daneben Textfeld (label-textfeld-abstand)
        int labelTFV = 0; // Vertikaler Abstand von Label und Textfeld

        this.labelSpielmodus.setBounds(10, 10, 100, 20);
        this.labelPeriodenzeit.setBounds(this.labelSpielmodus.getX() + this.labelSpielmodus.getWidth() + 2 * horAbs + 120,
                this.labelSpielmodus.getY(),
                100,
                this.labelSpielmodus.getHeight());
        this.spielermodus.setBounds(this.labelSpielmodus.getX(),
                this.labelSpielmodus.getY() + this.labelSpielmodus.getHeight() + labelTFV,
                125, textFH);
        this.spielMitZeitSpielen.setBounds(this.spielermodus.getX() + this.spielermodus.getWidth() + horAbs,
                this.spielermodus.getY(),
                100,
                cBoxH);
        this.periodenZeitMinuten.setBounds(this.labelPeriodenzeit.getX(),
                this.spielMitZeitSpielen.getY(),
                50,
                textFH);
        labelMinutenP.setBounds(this.periodenZeitMinuten.getX() + this.periodenZeitMinuten.getWidth() + labelTFA,
                this.periodenZeitMinuten.getY(),
                50,
                labH);
        this.periodenZeitSekunden.setBounds(this.labelMinutenP.getX() + this.labelMinutenP.getWidth() + horAbs,
                this.periodenZeitMinuten.getY(),
                50,
                textFH);
        this.labelSekundenP.setBounds(this.periodenZeitSekunden.getX() + this.periodenZeitSekunden.getWidth() + labelTFA,
                this.periodenZeitSekunden.getY(),
                50,
                labH);
        this.labelNameSchwarz.setBounds(this.spielermodus.getX(),
                this.spielermodus.getY() + this.spielermodus.getHeight() + verAbs,
                150,
                labH);
        this.labelZeitSchwarz.setBounds(this.periodenZeitMinuten.getX(),
                this.periodenZeitMinuten.getY() + this.periodenZeitMinuten.getHeight() + verAbs,
                100,
                labH);
        this.spielerNameSchwarz.setBounds(this.labelNameSchwarz.getX(),
                this.labelNameSchwarz.getY() + this.labelNameSchwarz.getHeight() + labelTFV,
                100,
                textFH);
        this.spielerZeitStundenSchwarz.setBounds(this.labelZeitSchwarz.getX(),
                this.labelZeitSchwarz.getY() + this.labelZeitSchwarz.getHeight() + labelTFV,
                50,
                textFH);
        this.labelStundenS.setBounds(this.spielerZeitStundenSchwarz.getX() + this.spielerZeitStundenSchwarz.getWidth() + labelTFA,
                this.spielerZeitStundenSchwarz.getY(),
                50,
                labH);
        this.spielerZeitMinutenSchwarz.setBounds(this.labelStundenS.getX() + this.labelStundenS.getWidth() + horAbs,
                this.labelStundenS.getY(),
                50,
                textFH);
        this.labelMinutenS.setBounds(this.spielerZeitMinutenSchwarz.getX() + this.spielerZeitMinutenSchwarz.getWidth() + labelTFA,
                this.spielerZeitMinutenSchwarz.getY(),
                50,
                labH);

        this.labelNameWeiss.setBounds(this.spielerNameSchwarz.getX(),
                this.spielerNameSchwarz.getY() + this.spielerNameSchwarz.getHeight() + verAbs,
                100,
                labH);
        this.labelZeitWeiss.setBounds(this.spielerZeitStundenSchwarz.getX(),
                this.spielerZeitStundenSchwarz.getY() + this.spielerZeitStundenSchwarz.getHeight() + verAbs,
                100,
                labH);
        this.spielerNameWeiss.setBounds(this.labelNameWeiss.getX(),
                this.labelNameWeiss.getY() + this.labelNameWeiss.getHeight() + labelTFV,
                100,
                textFH);
        this.spielerZeitStundenWeiss.setBounds(this.labelZeitWeiss.getX(),
                this.labelZeitWeiss.getY() + this.labelZeitWeiss.getHeight() + labelTFV,
                50,
                textFH);
        this.labelStundenW.setBounds(this.spielerZeitStundenWeiss.getX() + this.spielerZeitStundenWeiss.getWidth() + labelTFA,
                this.spielerZeitStundenWeiss.getY(),
                50,
                labH);
        this.spielerZeitMinutenWeiss.setBounds(this.labelStundenW.getX() + this.labelStundenW.getWidth() + horAbs,
                this.labelStundenW.getY(),
                50,
                textFH);
        this.labelMinutenW.setBounds(this.spielerZeitMinutenWeiss.getX() + this.spielerZeitMinutenWeiss.getWidth() + labelTFA,
                this.spielerZeitMinutenWeiss.getY(),
                50,
                labH);
        this.labelKomi.setBounds(this.spielerNameWeiss.getX(),
                this.spielerNameWeiss.getY() + this.spielerNameWeiss.getHeight() + verAbs,
                100,
                labH);
        this.spielerKomiWeiss.setBounds(this.spielerZeitStundenWeiss.getX(),
                this.spielerZeitStundenWeiss.getY() + this.spielerZeitStundenWeiss.getHeight() + verAbs,
                50,
                textFH);

        int vAbsRB = 10; // vertikaler Abstand von Radiobuttons
        int radBuH = 15; // hoehe eines Radiobuttons
        this.labelGroessenWahl.setBounds(this.labelKomi.getX(),
                this.labelKomi.getY() + this.labelKomi.getHeight() + verAbs,
                100,
                radBuH);
        this.siebenXsieben.setBounds(this.labelGroessenWahl.getX() + this.labelGroessenWahl.getWidth() + horAbs,
                this.labelGroessenWahl.getY(),
                100,
                radBuH);
        this.neunXneun.setBounds(this.siebenXsieben.getX() + this.siebenXsieben.getWidth() + horAbs,
                this.siebenXsieben.getY(),
                100,
                radBuH);
        this.elfXelf.setBounds(this.neunXneun.getX() + this.neunXneun.getWidth() + horAbs,
                this.neunXneun.getY(),
                100,
                radBuH);
        this.dreizehnXdreizehn.setBounds(this.labelGroessenWahl.getX(),
                this.labelGroessenWahl.getY() + this.labelGroessenWahl.getHeight() + verAbs,
                100,
                radBuH);
        this.fuenfzehnXfuenfzehn.setBounds(this.siebenXsieben.getX(),
                this.siebenXsieben.getY() + this.siebenXsieben.getHeight() + verAbs,
                100,
                radBuH);
        this.siebzehnXsiebzehn.setBounds(this.neunXneun.getX(),
                this.neunXneun.getY() + this.neunXneun.getHeight() + verAbs,
                100,
                radBuH);
        this.neunzehnXneunzehn.setBounds(this.elfXelf.getX(),
                this.elfXelf.getY() + this.elfXelf.getHeight() + verAbs,
                100,
                radBuH);

        int buttonH = 30; // hoehe eines Buttons
        this.spielStarten.setBounds(this.labelMinutenS.getX() + this.labelMinutenS.getWidth() + horAbs,
                this.labelMinutenS.getY(),
                75,
                buttonH);
        this.hilfeButton.setBounds(this.spielStarten.getX(),
                this.spielStarten.getY() + this.spielStarten.getHeight() + verAbs,
                75,
                buttonH);

        this.labelVorgabe.setBounds(this.brettXOffset + this.brettbreite + horAbs,
                this.brettYOffset + verAbs,
                100,
                labH);
        this.spielVorgabeSteine.setBounds(this.labelVorgabe.getX(),
                this.labelVorgabe.getY() + this.labelVorgabe.getHeight() + labelTFA,
                100,
                20);
        this.spielBrettHinweise.setBounds(this.spielVorgabeSteine.getX(),
                this.spielVorgabeSteine.getY() + this.spielVorgabeSteine.getHeight() + verAbs,
                this.spielVorgabeSteine.getWidth(),
                100);
    }

    /**
     * Die definierten RadioButtons fuer die Wahl der Feldgroesse muessen in
     * einer Gruppe zusammengefasst werden
     */
    private void radioButtonsInGroup() {

        this.spielFeldAuswahl.add(this.siebenXsieben);
        this.spielFeldAuswahl.add(this.neunXneun);
        this.spielFeldAuswahl.add(this.elfXelf);
        this.spielFeldAuswahl.add(this.dreizehnXdreizehn);
        this.spielFeldAuswahl.add(this.fuenfzehnXfuenfzehn);
        this.spielFeldAuswahl.add(this.siebzehnXsiebzehn);
        this.spielFeldAuswahl.add(this.neunzehnXneunzehn);

    }

    /**
     * Die beiden Combo-Boxes fuer Spielmodus und Vorgabeauswahl werden initialisiert
     * Wird hier ein Wert geaendert, muss er auch im restlichen Programm
     * veraendert werden
     */
    private void comboBoxenInit() {

        this.spielermodus.addItem("Schnellstart");
        this.spielermodus.addItem("Vorgabe");
        this.spielermodus.addItem("Startformation");
        this.spielermodus.addItem("Spiel Laden");

        this.spielVorgabeSteine.addItem("0");
        this.spielVorgabeSteine.addItem("2");
        this.spielVorgabeSteine.addItem("3");
        this.spielVorgabeSteine.addItem("4");
        this.spielVorgabeSteine.addItem("5");
        this.spielVorgabeSteine.addItem("6");
        this.spielVorgabeSteine.addItem("7");
        this.spielVorgabeSteine.addItem("8");
        this.spielVorgabeSteine.addItem("9");
    }

    /**
     * Alle Textfelder und Buttons muessen zum ActionListener hinzugefuegt
     * werden.
     */
    private void zumActionListenerAdden() {
        this.spielermodus.addActionListener(this);
        this.spielMitZeitSpielen.addActionListener(this);
        this.siebenXsieben.addActionListener(this);
        this.neunXneun.addActionListener(this);
        this.elfXelf.addActionListener(this);
        this.dreizehnXdreizehn.addActionListener(this);
        this.fuenfzehnXfuenfzehn.addActionListener(this);
        this.siebzehnXsiebzehn.addActionListener(this);
        this.neunzehnXneunzehn.addActionListener(this);
        this.spielVorgabeSteine.addActionListener(this);
        this.spielStarten.addActionListener(this);
        this.hilfeButton.addActionListener(this);
    }

    /**
     * Das klicken mit der Maus ist als event nur wichtig, wenn man ein
     * benutzerdefiniertes Feld erstellen wollen
     * @param e Event von der Maus (mit Koordinaten)
     */
    public void mouseClicked(MouseEvent e) {
        switch (this.getSelectedFeldgroesse()) {
            case 7:
                this.paneloffset = 47;
                this.seitenoffset = 17;
                break;
            case 9:
                this.paneloffset = 29;
                this.seitenoffset = 17;
                break;
            case 11:
                this.paneloffset = 20;
                this.seitenoffset = 17;
                break;
            case 13:
                this.paneloffset = 13;
                this.seitenoffset = 16;
                break;
            case 15:
                this.paneloffset = 8;
                this.seitenoffset = 16;
                break;
            case 17:
                this.paneloffset = 4;
                this.seitenoffset = 16;
                break;
            case 19:
                this.paneloffset = 1;
                this.seitenoffset = 16;
                break;
        }
        if (this.momSpielModus.equals("Startformation")
                && e.getX() - 15 >= this.brettXOffset && e.getX() - 15 <= this.brettXOffset + this.brettbreite
                && e.getY() - 24 >= this.brettYOffset && e.getY() - 24 <= this.brettYOffset + this.bretthoehe) {

            int farbe = 0;
            if (e.getButton() == MouseEvent.BUTTON1) {
                farbe = Konstante.SCHNITTPUNKT_SCHWARZ;
            } else {
                farbe = Konstante.SCHNITTPUNKT_WEISS;
            }
            int xKoord = 1;
            int yKoord = 1;
            xKoord = (int) ((((double) e.getX() - this.seitenoffset + (double) this.brettXOffset) / (double) this.brettbreite) * (double) this.getSelectedFeldgroesse()) + 1;
            yKoord = this.getSelectedFeldgroesse() - (int) ((((double) e.getY() + this.paneloffset - (double) this.brettYOffset) / (double) this.bretthoehe) * (double) this.getSelectedFeldgroesse()) + 1;
            if (xKoord < 1) {
                xKoord = 1;
            }
            if (xKoord > this.getSelectedFeldgroesse()) {
                xKoord = this.getSelectedFeldgroesse();
            }
            if (yKoord < 1) {
                yKoord = 1;
            }
            if (yKoord > this.getSelectedFeldgroesse()) {
                yKoord = this.getSelectedFeldgroesse();
            }
            this.dasSpielfeld.legeSteinAufInitBrett(xKoord, yKoord, farbe);
            this.dasSpielfeldGUI.updateSpielFeld(this.dasSpielfeld.getSpielfeldZumZeitpunkt(0));
            this.validate();
            repaint();
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
     * Bei einem Aktion event, muss je nach gewaehlter Komponente das Feld
     * veraendert, oder andere Werte angepasst werden
     * @param e Action-Event
     */
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == this.spielMitZeitSpielen) {
            this.toggleSpielMitZeitSpielen();
        } else if (e.getSource() == this.spielStarten) {
            this.versucheZuStarten();
        } else if (e.getSource() == this.hilfeButton) {
            this.zeigeHilfeAn();
        } else if (e.getSource() == this.spielermodus) {
            this.updateSpielmodus(this.spielermodus.getSelectedItem().toString());
        } else if (e.getSource() == this.spielVorgabeSteine) {
            this.updateBrettVorgabe();
        } else if (e.getSource() == this.siebenXsieben
                || e.getSource() == this.neunXneun
                || e.getSource() == this.elfXelf
                || e.getSource() == this.dreizehnXdreizehn
                || e.getSource() == this.fuenfzehnXfuenfzehn
                || e.getSource() == this.siebzehnXsiebzehn
                || e.getSource() == this.neunzehnXneunzehn) {
            this.neuesSpielfeld();
        }

    }

    /**
     * Toggelt die Spiel-Einstellungs-Felder die mit der SPielzeit zu tun haben
     * zwischen Enabled und Disabled hin und her.
     */
    private void toggleSpielMitZeitSpielen() {
        JCheckBox box = (JCheckBox) this.spielMitZeitSpielen;
        if (!box.isSelected()) {
            this.periodenZeitMinuten.setEnabled(false);
            this.periodenZeitSekunden.setEnabled(false);
            this.spielerZeitMinutenSchwarz.setEnabled(false);
            this.spielerZeitMinutenWeiss.setEnabled(false);
            this.spielerZeitStundenSchwarz.setEnabled(false);
            this.spielerZeitStundenWeiss.setEnabled(false);
        } else {
            this.periodenZeitMinuten.setEnabled(true);
            this.periodenZeitSekunden.setEnabled(true);
            this.spielerZeitMinutenSchwarz.setEnabled(true);
            this.spielerZeitMinutenWeiss.setEnabled(true);
            this.spielerZeitStundenSchwarz.setEnabled(true);
            this.spielerZeitStundenWeiss.setEnabled(true);
        }
    }

    /**
     * Wenn ein neues Feld angelegt wird, benoetigt man eine neue Datenstruktur
     * und eine neue Oberflaeche. Dafuer muss die alte Oberflaeche entfernt werden
     * und neue danach eingebunden werden
     */
    private void neuesSpielfeld() {
        /* parameter herausfinden */
        int feldgroesse = this.getSelectedFeldgroesse();
        /* Ist die Feldgroesse gleich geblieben, muss nichts gemacht werden */
        if (feldgroesse == this.dasSpielfeld.getSpielfeldGroesse()) {
            return;
        }

        this.dasSpielfeld = new Spielfeld(feldgroesse);
        /*   if(this.momSpielModus.equals("Vorgabe")){*/
        this.dasSpielfeld.initialisiereFeldMitVorgabenFuerSchwarz(this.getVorgabeWert());
        //   }
        /* Es muss noch eine neue Oberflaeche angelegt werden */

        this.getContentPane().remove(this.dasSpielfeldGUI);
        this.dasSpielfeldGUI = new EinstellungSpielbrett(this.brettbreite,
                this.bretthoehe,
                this.brettXOffset,
                this.brettYOffset,
                this.getSelectedFeldgroesse());
        this.dasSpielfeldGUI.updateSpielFeld(this.dasSpielfeld.getSpielfeldZumZeitpunkt(0));
        this.getContentPane().add(this.dasSpielfeldGUI);
        this.validate();
        this.repaint();
    }

    /**
     * Aus den RadioButtons muss ermittelt werden, welcher gerade ausgewaehlt ist
     * @return Spielfeldgroesse, je nach Button-Stellung;
     */
    private int getSelectedFeldgroesse() {
        int feldgroesse;
        if (this.siebenXsieben.isSelected() == true) {
            feldgroesse = 7;
        } else if (this.neunXneun.isSelected() == true) {
            feldgroesse = 9;
        } else if (this.elfXelf.isSelected() == true) {
            feldgroesse = 11;
        } else if (this.dreizehnXdreizehn.isSelected() == true) {
            feldgroesse = 13;
        } else if (this.fuenfzehnXfuenfzehn.isSelected() == true) {
            feldgroesse = 15;
        } else if (this.siebzehnXsiebzehn.isSelected() == true) {
            feldgroesse = 17;
        } else {
            feldgroesse = 19;
        }
        return feldgroesse;
    }

    /**
     * Das Eingabefeld des Spielernamen von Weiss muss geparst werden und
     * Falscheingaben werden abgefangen
     * @return Spielername Weiss
     */
    private String getNameWeiss() {
        String rueckgabe = "";
        String textFeldString = this.spielerNameWeiss.getText();
        if (!textFeldString.matches("[A-Za-z0-9]*")) {
            rueckgabe = "Weiss";
            sendMessageToUser("Weißer Name falsch. Darf nur aus Buchstaben und Zahlen bestehen.");
        } else if (textFeldString.length() == 0) {
            rueckgabe = "Weiss";
            sendMessageToUser("Name des weißen Spielers nicht angegeben.");
        } else {
            rueckgabe = textFeldString;
        }
        return rueckgabe;
    }

    /**
     * Das Eingabefeld des Spielernamen von Schwarzen muss geparst werden und 
     * Falscheingaben werden abgefangen
     * @return Spielername Schwarz
     */
    private String getNameSchwarz() {
        String rueckgabe = "";
        String textFeldString = this.spielerNameSchwarz.getText();
        if (!textFeldString.matches("[A-Za-z0-9]*")) {
            rueckgabe = "Schwarz";
            sendMessageToUser("Schwarzer Name falsch. Darf nur aus Buchstaben und Zahlen bestehen.");
        } else if (textFeldString.length() == 0) {
            rueckgabe = "Schwarz";
            sendMessageToUser("Name des schwarzen Spielers nicht angegeben.");
        } else {
            rueckgabe = textFeldString;
        }
        return rueckgabe;
    }

    /**
     * Parse das Sekundenfeld in der Periodenzeit
     * @return Zeit in ms (fuer Verarbeitung)
     */
    private long getSekundenPeriode() {
        long rueckgabe = 0;
        String textFeldVal = this.periodenZeitSekunden.getText();
        try {
            rueckgabe = 1000 * Integer.parseInt(textFeldVal);
        } catch (NumberFormatException nfe) {
            sendMessageToUser("Byo-Yomi: Sekundenzeit Falsch");
            rueckgabe = 0;
        }
        if (rueckgabe < 0) {
            sendMessageToUser("Byo-Yomi: Minuten negativ");
            rueckgabe = 0;
        }
        return rueckgabe;
    }

    /**
     * Parse das Minutenfeld in der Periodenzeit
     * @return Zeit in ms (fuer Veratbeitung)
     */
    private long getMinutenPeriode() {
        long rueckgabe = 0;
        String textFeldVal = this.periodenZeitMinuten.getText();
        try {
            rueckgabe = 60 * 1000 * Integer.parseInt(textFeldVal);
        } catch (NumberFormatException nfe) {
            sendMessageToUser("Byo-Yomi: Minutenzeit Falsch");
            rueckgabe = 0;
        }
        if (rueckgabe < 0) {
            sendMessageToUser("Byo-Yomi: Minuten negativ");
            rueckgabe = 0;
        }

        return rueckgabe;
    }

    /**
     * Parse das Minutenfeld des schwarzen Spielers
     * @return Zeit in ms (fuer Verarbeitung)
     */
    private long getMinutenSchwarz() {
        long rueckgabe = 0;
        String textFeldVal = this.spielerZeitMinutenSchwarz.getText();
        try {
            rueckgabe = 60 * 1000 * Integer.parseInt(textFeldVal);
        } catch (NumberFormatException nfe) {
            sendMessageToUser("Zeit Schwarz: Minutenzeit Falsch");
            rueckgabe = 0;
        }
        if (rueckgabe < 0) {
            sendMessageToUser("Byo-Yomi: Minuten negativ");
            rueckgabe = 0;
        }
        return rueckgabe;
    }

    /**
     * Parse das Minutenfeld des weissen Spielers
     * @return Zeit in ms (fuer Verarbeitung)
     */
    private long getMinutenWeiss() {
        long rueckgabe = 0;
        String textFeldVal = this.spielerZeitMinutenWeiss.getText();
        try {
            rueckgabe = 60 * 1000 * Integer.parseInt(textFeldVal);
        } catch (NumberFormatException nfe) {
            sendMessageToUser("Zeit Weiss: Minutenzeit Falsch");
            rueckgabe = 0;
        }
        if (rueckgabe < 0) {
            sendMessageToUser("Byo-Yomi: Minuten negativ");
            rueckgabe = 0;
        }
        return rueckgabe;
    }

    /**
     * Parse das Stundenfeld des schwarzen Spielers
     * @return Zeit in ms (fuer Verarbeitung)
     */
    private long getStundenSchwarz() {
        long rueckgabe = 0;
        String textFeldVal = this.spielerZeitStundenSchwarz.getText();
        try {
            rueckgabe = 60 * 60 * 1000 * Integer.parseInt(textFeldVal);
        } catch (NumberFormatException nfe) {
            sendMessageToUser("Zeit Schwarz: Stundenzeit Falsch");
            rueckgabe = 0;
        }
        if (rueckgabe < 0) {
            sendMessageToUser("Byo-Yomi: Minuten negativ");
            rueckgabe = 0;
        }
        return rueckgabe;
    }

    /**
     * Parse das Stundenfeld des weissen Spielers
     * @return Zeit in ms (fuer Verarbeitung)
     */
    private long getStundenWeiss() {
        long rueckgabe = 0;
        String textFeldVal = this.spielerZeitStundenWeiss.getText();
        try {
            rueckgabe = 60 * 60 * 1000 * Integer.parseInt(textFeldVal);
        } catch (NumberFormatException nfe) {
            sendMessageToUser("Zeit Weiss: Stundenzeit Falsch");
            rueckgabe = 0;
        }
        if (rueckgabe < 0) {
            sendMessageToUser("Byo-Yomi: Minuten negativ");
            rueckgabe = 0;
        }
        return rueckgabe;
    }

    /**
     * Das Komi, das im dazugehoerigen Textfeld eingegeben wird, muss erkannt
     * werden.
     * @return Erkanntes Komi
     */
    private float getKomi() {
        float rueckgabe = 0;
        String textFeldVal = this.spielerKomiWeiss.getText();
        try {
            rueckgabe = Float.valueOf(textFeldVal).floatValue();
        } catch (NumberFormatException nfe) {
            sendMessageToUser("Komi falsch angegeben");
            rueckgabe = 6.5f;
        }
        return rueckgabe;
    }

    /**
     * Will eine Funktion dem Benutzer etwas mittelen, kann diese Funktion genutzt
     * werden.
     * @param s Zu uebermittelnde Nachicht
     */
    private void sendMessageToUser(String s) {
        this.fehlerBeiEingabe = true;
        this.errorString += s + "\n";
    }

    /**
     * Wurde auf den Start-Button geklickt so wird versucht das Spiel zu starten.
     * Sind die Eingaben korrekt, wird gestartet. Ansonsten wird nicht gestartet
     * und dem Benutzer eine Fehlerausgabe geliefert.
     */
    private boolean versucheZuStarten() {
        this.fehlerBeiEingabe = false;
        long zeitSchwarz = this.getStundenSchwarz() + this.getMinutenSchwarz();
        long zeitWeiss = this.getStundenWeiss() + this.getMinutenWeiss();
        long periodenzeit = this.getMinutenPeriode() + this.getSekundenPeriode();
        Spieler sSchwarz = new Spieler(this.getNameSchwarz(), zeitSchwarz, 0, 0);
        Spieler sWeiss = new Spieler(this.getNameWeiss(), zeitWeiss, 0, this.getKomi());
        this.dasSpielfeld.setSpielerSchwarz(sSchwarz);
        this.dasSpielfeld.setSpielerWeiss(sWeiss);
        this.dasSpielfeld.setPeriodenZeit(periodenzeit);
        if (this.spielMitZeitSpielen.isSelected() == true) {
            this.dasSpielfeld.setIgnoreTime(false);
        } else {
            this.dasSpielfeld.setIgnoreTime(true);
        }
        if (this.fehlerBeiEingabe == true) {

            /* Fehlerausgabe */
            this.outputMessageToUser();

            /* Werte zurueckschreiben und verbessern */
            this.syncWerteInFeldern();

            /* Da Werte nun Korrekt, keine Fehlerausgabe */
            this.errorString = "";
            this.fehlerBeiEingabe = false;

            return false;
        } else {
            String validierungsAntwort = this.dasSpielfeld.spielfeldValidiert();
            if (validierungsAntwort == null) {
                LoGoApp.meineSteuerung.initMitDatenModell(this.dasSpielfeld, this.getNameSchwarz(), this.getNameWeiss(), zeitSchwarz, zeitWeiss, periodenzeit, this.getKomi());
                LoGoApp.meineSteuerung.buttonSpielStarten();
                /* Jetzt spiel Starten */
                this.setVisible(false);
                return true;
            } else {
                if (LoGoApp.debug) {
                    System.out.println("Feld nicht valide");
                }
                this.sendMessageToUser("Ihre Eingaben sind nicht zulässig!\n" + validierungsAntwort);
                return false;
            }
        }
    }

    private void zeigeHilfeAn() {
        LoGoApp.meineSteuerung.buttonZeigeHilfeGedrueckt();
    }

    /**
     * Alle Werte in den Textfeldern werden Valide gemacht
     */
    private void syncWerteInFeldern() {
        this.spielerKomiWeiss.setText(String.valueOf(this.getKomi()));
        this.spielerNameSchwarz.setText(this.getNameSchwarz());
        this.spielerNameWeiss.setText(this.getNameWeiss());
        this.spielerZeitMinutenSchwarz.setText(String.valueOf(this.getMinutenSchwarz() / 60000));
        this.spielerZeitStundenSchwarz.setText(String.valueOf(this.getStundenSchwarz() / 36000000));
        this.spielerZeitMinutenWeiss.setText(String.valueOf(this.getMinutenWeiss() / 60000));
        this.spielerZeitStundenWeiss.setText(String.valueOf(this.getStundenWeiss() / 36000000));
        this.periodenZeitMinuten.setText(String.valueOf(this.getMinutenPeriode() / 60000));
        this.periodenZeitSekunden.setText(String.valueOf(this.getSekundenPeriode() / 1000));
    }

    /**
     * Alle Fehlermeldungen von den Funktionen, werden ausgegeben. Danach wird
     * der Fehlerstring geleert
     */
    private void outputMessageToUser() {
        JOptionPane.showMessageDialog(this, this.errorString);
        this.errorString = "";
    }

    /**
     * Die eingestellte Vorgabe muss aus der Combo-Box gelesen werden
     * @return
     */
    private int getVorgabeWert() {
        return Integer.valueOf(this.spielVorgabeSteine.getSelectedItem().toString());
    }

    private void updateSpielmodus(String modus) {
        if (modus.equals(this.momSpielModus)) {
            return; // nichts aendert sich
        }
        this.momSpielModus = modus;
        /* Da Spielermodus anders ist, muss Feld geleert werden. Da sich die Feldgroesse
         * nicht geaendert hat, muss man nur das datenmodell auf leer setzen*/
        this.dasSpielfeld = new Spielfeld(this.getSelectedFeldgroesse());

        if (modus.equals("Schnellstart")) {
            /* Das Feld ist jetzt leer, bereit fuer den Schnellstart */
            this.animiereFrameStart();
            /* Bei einem Schnellstart spielt man ohne Vorgaben */
            this.spielVorgabeSteine.setSelectedItem("0");
            this.dasSpielfeldGUI.updateSpielFeld(this.dasSpielfeld.getSpielfeldZumZeitpunkt(0));
            this.repaint();

        } else if (modus.equals("Vorgabe")) {
            this.dasSpielfeld.initialisiereFeldMitVorgabenFuerSchwarz(this.getVorgabeWert());
            this.dasSpielfeldGUI.updateSpielFeld(this.dasSpielfeld.getSpielfeldZumZeitpunkt(0));
            this.spielVorgabeSteine.setEnabled(true);
            this.spielBrettHinweise.setText("<HTML><BODY>Mit Vorgabe spielen. Oben die Zahl w&auml;hlen.</BODY></HTML>");
            this.repaint();
            this.animiereFrameEnde();

        } else if (modus.equals("Startformation")) {
            this.spielVorgabeSteine.setEnabled(false);
            this.spielBrettHinweise.setName("Mit eigenem Feld spielen");
            this.spielBrettHinweise.setText("<HTML><BODY>Linksklick für Schwarz Rechtsklick für Weiß Gleiche Farben heben sich auf.</BODY></HTML>");
            this.animiereFrameEnde();
            this.spielVorgabeSteine.setSelectedItem(0);
            this.dasSpielfeldGUI.updateSpielFeld(this.dasSpielfeld.getSpielfeldZumZeitpunkt(0));
            this.repaint();

        } else if (modus.equals("Spiel Laden")) {
            // Es soll ein neues Spiel geladen werden
            Laden ladenObjekt = new Laden();

            try{
                ladenObjekt.LadeSpiel();
            }catch(Exception ex){
            this.sendMessageToUser("Es ist ein Fehler beim Laden aufgetreten.");
            this.outputMessageToUser();
            this.spielermodus.setSelectedItem("Schnellstart");
            this.animiereFrameStart();
            return;
            }


            Spielfeld neuesSpielfeld = ladenObjekt.getSpielfeld();
            String fehlermeldung = "";

            if (neuesSpielfeld != null) {
                fehlermeldung = neuesSpielfeld.spielfeldValidiert();
            }

            if (fehlermeldung == null) {
                this.dasSpielfeld = neuesSpielfeld;
                if(this.versucheZuStarten()){
                    LoGoApp.meineSteuerung.buttonPause();
                }else {
                    this.sendMessageToUser("Spiel konnte leider nicht gestartet werden");
                    this.outputMessageToUser();
                }
            } else {
                //this.sendMessageToUser("Konnte die Datei leider nicht öffnen.");
                this.updateSpielmodus("Schnellstart");
                //this.outputMessageToUser();
            }
            this.spielermodus.setSelectedItem("Schnellstart");
            this.animiereFrameStart();
        }
    }

    private void animiereFrameStart() {
        this.setSize(this.getWidth(), this.frameMinhoehe);
        this.validate();
        this.repaint();
    }

    private void animiereFrameEnde() {
        this.setSize(this.getWidth(), this.frameMaxhoehe);
        this.validate();
        this.setLocationRelativeTo(null);
        this.repaint();
    }

    private void updateBrettVorgabe() {
        if (this.momSpielModus.equals("Vorgabe")) {
            this.dasSpielfeld = new Spielfeld(this.getSelectedFeldgroesse());
            this.dasSpielfeld.initialisiereFeldMitVorgabenFuerSchwarz(this.getVorgabeWert());
            this.dasSpielfeldGUI.updateSpielFeld(this.dasSpielfeld.getSpielfeldZumZeitpunkt(0));
        }
    }

    public void macheFensterSichtbar(boolean b){
        if(b==true){
            this.dasSpielfeld = new Spielfeld(getSelectedFeldgroesse());
            this.dasSpielfeldGUI.updateSpielFeld(this.dasSpielfeld.getAktuellesSpielFeld());
            this.setVisible(true);
        }else{
            this.setVisible(false);
        }

    }
}
