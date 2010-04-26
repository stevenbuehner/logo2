/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import Klassen.Spieler;
import Klassen.Spielfeld;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;


/**
 *
 * @author tommy
 * @version 0.1
 */
public class SpieleinstellungFenster extends JFrame implements MouseListener, ActionListener{

    // Felder auf der GUI
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
    private JTextArea spielBrettHinweise;

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

    // Datenhaltung
    private String errorString;
    private boolean fehlerBeiEingabe;
    private Spielfeld dasSpielfeld;
    private Spielbrett dasSpielfeldGUI;


    public SpieleinstellungFenster(){
        this( "Einstellungsfenster");
    }

    public SpieleinstellungFenster(String fenstername) {
        super(fenstername);

        this.init();
        pack();
        this.setSize(500, 700);
        this.setResizable(false);
        this.setLocationRelativeTo(null);       // Fenster zentrieren
        this.setVisible(true);

    }

    private void init(){
        this.startwerteSetzen();
        this.radioButtonsInGroup();
        this.comboBoxenInit();
        this.zumActionListenerAdden();
        this.neuesSpielfeld();
        this.fensterRendern();
        this.komponentenAufContentPane();
    }

    /** Hier werden die Label, Buttons und Textfelder initialisiert. 
     * Beschriftungen und andere textuelle Dinge
     */
    private void startwerteSetzen(){
        this.errorString = "";
        this.fehlerBeiEingabe = false;
        this.dasSpielfeld = new Spielfeld(13);
        //this.dasSpielfeldGUI = new Spielbrett(WIDTH, WIDTH, NORMAL, NORMAL, ICONIFIED, null)
        this.spielerNameWeiss          = new JTextField("Weiss");
        this.spielerZeitMinutenWeiss   = new JTextField("30");
        this.spielerZeitStundenWeiss   = new JTextField("0");
        this.spielerKomiWeiss          = new JTextField("6.5");

        this.spielerNameSchwarz        = new JTextField("Schwarz");
        this.spielerZeitMinutenSchwarz = new JTextField("30");
        this.spielerZeitStundenSchwarz = new JTextField("0");

        this.periodenZeitMinuten       = new JTextField("1");
        this.periodenZeitSekunden      = new JTextField("0");

        this.spielermodus              = new JComboBox();
        this.spielMitZeitSpielen       = new JCheckBox("Zeit setzen", true);
        this.spielMitZeitSpielen.setSelected(true);

        this.spielFeldAuswahl          = new ButtonGroup();
        this.siebenXsieben             = new JRadioButton("7 x 7");
        this.neunXneun                 = new JRadioButton("9 x 9");
        this.elfXelf                   = new JRadioButton("11 x 11");
        this.dreizehnXdreizehn         = new JRadioButton("13 x 13",true);
        this.fuenfzehnXfuenfzehn       = new JRadioButton("15 x 15");
        this.siebzehnXsiebzehn         = new JRadioButton("17 x 17");
        this.neunzehnXneunzehn         = new JRadioButton("19 x 19");

        this.spielVorgabeSteine        = new JComboBox();
        this.spielBrettHinweise        = new JTextArea("", 15, 5);
        this.spielStarten              = new JButton("Start");
        this.hilfeButton               = new JButton("Hilfe");

        /* Zusaetzliche Labels */
        this.labelSpielmodus = new JLabel("Spielmodus",JLabel.LEFT);
        this.labelPeriodenzeit = new JLabel("Byo-Yomi",JLabel.LEFT);
        this.labelMinutenP = new JLabel("Min",JLabel.LEFT);
        this.labelSekundenP = new JLabel("Sek",JLabel.LEFT);
        this.labelNameSchwarz = new JLabel("Spieler Schwarz",JLabel.LEFT);
        this.labelZeitSchwarz = new JLabel("Zeit Schwarz",JLabel.LEFT);
        this.labelMinutenS = new JLabel("Min",JLabel.LEFT);
        this.labelStundenS = new JLabel("Std",JLabel.LEFT);
        this.labelNameWeiss = new JLabel("Spieler Weiß",JLabel.LEFT);
        this.labelZeitWeiss = new JLabel("Zeit Weiß",JLabel.LEFT);
        this.labelMinutenW = new JLabel("Min",JLabel.LEFT);
        this.labelStundenW = new JLabel("Std",JLabel.LEFT);
        this.labelKomi = new JLabel("Komi:",JLabel.LEFT);
        this.labelGroessenWahl = new JLabel("Brettgröße",JLabel.LEFT);
        this.labelVorgabe = new JLabel("Vorgabe",JLabel.LEFT);
    }

    /**
     * Die schon definierten Komponenten werden nun auf die ContentPane
     * gelegt
     */
    private void komponentenAufContentPane(){
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
        this.getContentPane().add( this.siebzehnXsiebzehn);
        this.getContentPane().add( this.neunzehnXneunzehn);
        this.getContentPane().add(this.spielVorgabeSteine);
        this.getContentPane().add(this.spielBrettHinweise);
        this.getContentPane().add(this.spielStarten);
        this.getContentPane().add(this.hilfeButton);
        this.getContentPane().add(this.spielVorgabeSteine);
        this.getContentPane().add(this.spielBrettHinweise);
       // this.getContentPane().add(this.dasSpielfeld); // muss noch kommen <-----------
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
    }

    /** 
     * Die Komponenten muessen einen genau definierten Ort bekommen
     */
    private void fensterRendern(){
        int horAbs = 20; // horizontaler Abstand
        int verAbs = 10; // vertikaler Abstand
        int textFH = 20; // Textfeldhoehe
        int labH   = 30; // Labelhoehe
        int cBoxH  = 30; // Checkboxhoehe
        int labelTFA = 5; // Abstand zwischen Label und Links daneben Textfeld (label-textfeld-abstand)
        int labelTFV = 0; // Vertikaler Abstand von Label und Textfeld

        this.labelSpielmodus.setBounds(10, 10, 100, 20);
        this.labelPeriodenzeit.setBounds(this.labelSpielmodus.getX()+this.labelSpielmodus.getWidth()+2* horAbs + 120,
                                    this.labelSpielmodus.getY() ,
                                    100,
                                    this.labelSpielmodus.getHeight());
        this.spielermodus .setBounds(this.labelSpielmodus.getX(),
                                    this.labelSpielmodus.getY() + this.labelSpielmodus.getHeight() + labelTFV ,
                                    125, textFH );
        this.spielMitZeitSpielen .setBounds(this.spielermodus.getX() + this.spielermodus.getWidth() + horAbs,
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
        this.periodenZeitSekunden.setBounds(this.labelMinutenP.getX()+this.labelMinutenP.getWidth()+horAbs,
                                    this.periodenZeitMinuten.getY(),
                                    50,
                                    textFH);
        this.labelSekundenP.setBounds(this.periodenZeitSekunden.getX()+this.periodenZeitSekunden.getWidth()+labelTFA,
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
        this.spielerNameSchwarz .setBounds(this.labelNameSchwarz.getX(),
                                    this.labelNameSchwarz.getY() + this.labelNameSchwarz.getHeight()+ labelTFV,
                                    100,
                                    textFH);
        this.spielerZeitStundenSchwarz.setBounds(this.labelZeitSchwarz.getX(),
                                    this.labelZeitSchwarz.getY()+this.labelZeitSchwarz.getHeight()+labelTFV,
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
                                    this.labelNameWeiss.getY() + this.labelNameWeiss.getHeight()+ labelTFV,
                                    100,
                                    textFH);
        this.spielerZeitStundenWeiss.setBounds(this.labelZeitWeiss.getX(),
                                    this.labelZeitWeiss.getY()+this.labelZeitWeiss.getHeight()+labelTFV,
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
                                    this.spielerNameWeiss.getY() + this.spielerNameWeiss.getHeight()+verAbs,
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
                                    100 ,
                                    radBuH );
        this.siebenXsieben.setBounds(this.labelGroessenWahl.getX(),
                                    this.labelGroessenWahl.getY()+this.labelGroessenWahl.getHeight()+verAbs,
                                    100 ,
                                    radBuH);
        this.neunXneun.setBounds(this.labelGroessenWahl.getX() + this.labelGroessenWahl.getWidth() + horAbs,
                                    this.labelGroessenWahl.getY(),
                                    100 ,
                                    radBuH);
        this.elfXelf.setBounds(this.neunXneun.getX(),
                                    this.neunXneun.getY() + this.neunXneun.getHeight() + verAbs,
                                    100 ,
                                    radBuH);
        this.dreizehnXdreizehn.setBounds(this.neunXneun.getX() + this.neunXneun.getWidth() + horAbs ,
                                    this.labelGroessenWahl.getY(),
                                    100 ,
                                    radBuH);
        this.fuenfzehnXfuenfzehn.setBounds(this.dreizehnXdreizehn.getX(),
                                    this.dreizehnXdreizehn.getY() + this.dreizehnXdreizehn.getHeight() + verAbs,
                                    100 ,
                                    radBuH);
        this.siebzehnXsiebzehn.setBounds(this.dreizehnXdreizehn.getX() + this.dreizehnXdreizehn.getWidth() + horAbs,
                                    this.labelGroessenWahl.getY(),
                                    100 ,
                                    radBuH);
        this.neunzehnXneunzehn.setBounds(this.siebzehnXsiebzehn.getX(),
                                    this.siebzehnXsiebzehn.getY() + this.siebzehnXsiebzehn.getHeight() + verAbs,
                                    100 ,
                                    radBuH);

        int buttonH = 30; // hoehe eines Buttons
        this.spielStarten.setBounds(this.siebenXsieben.getX(),
                                    this.siebenXsieben.getY() + this.siebenXsieben.getHeight() + verAbs,
                                    75,
                                    buttonH);
        this.hilfeButton.setBounds( this.spielStarten.getX() + this.spielStarten.getWidth() + 50,
                                    this.siebenXsieben.getY() + this.siebenXsieben.getHeight() + verAbs,
                                    75,
                                    buttonH);

        this.labelVorgabe.setBounds(300,
                                    this.neunzehnXneunzehn.getY() + this.neunzehnXneunzehn.getWidth() + verAbs,
                                    100,
                                    labH);
        this.spielVorgabeSteine.setBounds(this.labelVorgabe.getX(),
                                    this.labelVorgabe.getY() + this.labelVorgabe.getHeight() + labelTFA ,
                                    100 ,
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
    private void radioButtonsInGroup(){
        
        this.spielFeldAuswahl.add(this.siebenXsieben);
        this.spielFeldAuswahl.add(this.neunXneun);
        this.spielFeldAuswahl.add(this.elfXelf);
        this.spielFeldAuswahl.add(this.dreizehnXdreizehn);
        this.spielFeldAuswahl.add(this.fuenfzehnXfuenfzehn);
        this.spielFeldAuswahl.add(this.siebzehnXsiebzehn);
        this.spielFeldAuswahl.add(this.neunzehnXneunzehn);

    }

    private void comboBoxenInit(){
        this.spielermodus.addItem("Schnellstart");
        this.spielermodus.addItem("DefiniertesFeld");

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
     * Alle Textfelder und Buttons muessen zum ActionListener geaddet werden
     */
    private void zumActionListenerAdden(){
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

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void actionPerformed(ActionEvent e) {

        //System.out.println(e.getSource());
        if(e.getSource() == this.spielMitZeitSpielen){
            this.toggleSpielMitZeitSpielen();
        }

        else if(e.getSource() == this.spielStarten){
            this.versucheZuStarten();
        }

        else if(e.getSource() == this.hilfeButton){
            this.zeigeHilfeAn();
        }

        else if(e.getSource() == this.spielermodus){

        }

        else if(e.getSource() == this.siebenXsieben ||
                e.getSource() == this.neunXneun ||
                e.getSource() == this.elfXelf ||
                e.getSource() == this.dreizehnXdreizehn ||
                e.getSource() == this.fuenfzehnXfuenfzehn ||
                e.getSource() == this.siebzehnXsiebzehn ||
                e.getSource() == this.neunzehnXneunzehn){
                this.neuesSpielfeld();
        }

    }

    /**
     * Toggelt die Spiel-Einstellungs-Felder die mit der SPielzeit zu tun haben
     * zwischen Enabled und Disabled hin und her.
     */
    private void toggleSpielMitZeitSpielen() {
        JCheckBox box = (JCheckBox)this.spielMitZeitSpielen;
        if( !box.isSelected() ){
            this.periodenZeitMinuten.setEnabled(false);
            this.periodenZeitSekunden.setEnabled(false);
            this.spielerZeitMinutenSchwarz.setEnabled(false);
            this.spielerZeitMinutenWeiss.setEnabled(false);
            this.spielerZeitStundenSchwarz.setEnabled(false);
            this.spielerZeitStundenWeiss.setEnabled(false);
        }
        else{
            this.periodenZeitMinuten.setEnabled(true);
            this.periodenZeitSekunden.setEnabled(true);
            this.spielerZeitMinutenSchwarz.setEnabled(true);
            this.spielerZeitMinutenWeiss.setEnabled(true);
            this.spielerZeitStundenSchwarz.setEnabled(true);
            this.spielerZeitStundenWeiss.setEnabled(true);
        }
    }

    /**
     * Wird aufgerufen wenn das Feld angeklickt wurde und übergibt die Pixelkoordinaten
     * abhängig von der linken oberen Ecke des Spielfeldes.
     * @param xPos
     * @param yPos
     */
    private void klickAufFeld( int xPos, int yPos){

    }

    private void neuesSpielfeld() {
        /* parameter herausfinden */
        int feldgroesse = this.getSelectedFeldgroesse();
        /* Ist die Feldgroesse gleich geblieben, muss nichts gemacht werden */
        if(feldgroesse == this.dasSpielfeld.getSpielfeldGroesse()){
            return;
        }

        this.dasSpielfeld = new Spielfeld(feldgroesse);
        this.initFeld();
        /* Es muss noch eine neue Oberflaeche angelegt werden */
    }

    /**
     * Aus den RadioButtons muss ermittelt werden, welcher gerade ausgewaehlt ist
     * @return Spielfeldgroesse, je nach Button-Stellung;
     */
    private int getSelectedFeldgroesse(){
        int feldgroesse;
        if(this.siebenXsieben.isSelected() == true){ feldgroesse = 7;}
        else if(this.neunXneun.isSelected() == true){feldgroesse = 9;}
        else if(this.elfXelf.isSelected() == true){feldgroesse = 11;}
        else if(this.dreizehnXdreizehn.isSelected() == true){feldgroesse = 13;}
        else if(this.fuenfzehnXfuenfzehn.isSelected() == true){feldgroesse = 15;}
        else if(this.siebzehnXsiebzehn.isSelected() == true){feldgroesse = 17;}
        else {feldgroesse = 19;}
        return feldgroesse;
    }

    /**
     * Das Eingabefeld des Spielernamen von Weiss muss geparst werden und
     * Falscheingaben werden abgefangen
     * @return Spielername Weiss
     */
    private String getNameWeiss(){
        String rueckgabe = "";
        String textFeldString = this.spielerNameWeiss.getText();
        if(textFeldString.matches(".*\\[.*") ||
           textFeldString.matches(".*\\;.*") ||
           textFeldString.matches(".*\\,.*") ||
           textFeldString.matches(".*\\].*")){
            rueckgabe = "Weiss";
            sendMessageToUser("Weißer Name falsch. Enthält eines der Zeichen: \'[\' \']\' \';\' \',\'");
        }
        else if(textFeldString.length() == 0){
            rueckgabe = "Weiss";
            sendMessageToUser("Name des weißen Spielers nicht angegeben.");
        }
        else {
            rueckgabe = textFeldString;
        }
        return rueckgabe;
    }

    /**
     * Das Eingabefeld des Spielernamen von Schwarzen muss geparst werden und 
     * Falscheingaben werden abgefangen
     * @return Spielername Schwarz
     */
    private String getNameSchwarz(){
        String rueckgabe = "";
        String textFeldString = this.spielerNameSchwarz.getText();
        if(textFeldString.matches(".*\\[.*") ||
           textFeldString.matches(".*\\;.*") ||
           textFeldString.matches(".*\\,.*") ||
           textFeldString.matches(".*\\].*")){
            rueckgabe = "Schwarz";
            sendMessageToUser("Schwarzer Name falsch. Enthält eines der Zeichen: \'[\' \']\' \';\' \',\'");
        }
        else if(textFeldString.length() == 0){
            rueckgabe = "Schwarz";
            sendMessageToUser("Name des schwarzen Spielers nicht angegeben.");
        }
        else {
            rueckgabe = textFeldString;
        }
        return rueckgabe;
    }

    /**
     * Parse das Sekundenfeld in der Periodenzeit
     * @return Zeit in ms (fuer Verarbeitung)
     */
    private long getSekundenPeriode(){
        long rueckgabe = 0;
        String textFeldVal = this.periodenZeitSekunden.getText();
        try{
            rueckgabe = 1000 * Integer.parseInt(textFeldVal);
        }catch(NumberFormatException nfe){
           sendMessageToUser("Byo-Yomi: Sekundenzeit Falsch");
           rueckgabe = 0;
        }
        if(rueckgabe < 0){
            sendMessageToUser("Byo-Yomi: Minuten negativ");
            rueckgabe = 0;
        }
        return rueckgabe;
    }

    /**
     * Parse das Minutenfeld in der Periodenzeit
     * @return Zeit in ms (fuer Veratbeitung)
     */
    private long getMinutenPeriode(){
        long rueckgabe = 0;
        String textFeldVal = this.periodenZeitMinuten.getText();
        try{
            rueckgabe = 60 * 1000 * Integer.parseInt(textFeldVal);
        }catch(NumberFormatException nfe){
           sendMessageToUser("Byo-Yomi: Minutenzeit Falsch");
           rueckgabe = 0;
        }
        if(rueckgabe < 0){
            sendMessageToUser("Byo-Yomi: Minuten negativ");
            rueckgabe = 0;
        }

        return rueckgabe;
    }

    /**
     * Parse das Minutenfeld des schwarzen Spielers
     * @return Zeit in ms (fuer Verarbeitung)
     */
    private long getMinutenSchwarz(){
        long rueckgabe = 0;
        String textFeldVal = this.spielerZeitMinutenSchwarz.getText();
        try{
            rueckgabe = 60 * 1000 * Integer.parseInt(textFeldVal);
        }catch(NumberFormatException nfe){
           sendMessageToUser("Zeit Schwarz: Minutenzeit Falsch");
           rueckgabe = 0;
        }
        if(rueckgabe < 0){
            sendMessageToUser("Byo-Yomi: Minuten negativ");
            rueckgabe = 0;
        }
        return rueckgabe;
    }

    /**
     * Parse das Minutenfeld des weissen Spielers
     * @return Zeit in ms (fuer Verarbeitung)
     */
    private long getMinutenWeiss(){
        long rueckgabe = 0;
        String textFeldVal = this.spielerZeitMinutenWeiss.getText();
        try{
            rueckgabe = 60 * 1000 * Integer.parseInt(textFeldVal);
        }catch(NumberFormatException nfe){
           sendMessageToUser("Zeit Weiss: Minutenzeit Falsch");
           rueckgabe = 0;
        }
        if(rueckgabe < 0){
            sendMessageToUser("Byo-Yomi: Minuten negativ");
            rueckgabe = 0;
        }
        return rueckgabe;
    }

    /**
     * Parse das Stundenfeld des schwarzen Spielers
     * @return Zeit in ms (fuer Verarbeitung)
     */
    private long getStundenSchwarz(){
        long rueckgabe = 0;
        String textFeldVal = this.spielerZeitStundenSchwarz.getText();
        try{
            rueckgabe = 60 * 60 * 1000 * Integer.parseInt(textFeldVal);
        }catch(NumberFormatException nfe){
           sendMessageToUser("Zeit Schwarz: Stundenzeit Falsch");
           rueckgabe = 0;
        }
        if(rueckgabe < 0){
            sendMessageToUser("Byo-Yomi: Minuten negativ");
            rueckgabe = 0;
        }
        return rueckgabe;
    }

    /**
     * Parse das Stundenfeld des weissen Spielers
     * @return Zeit in ms (fuer Verarbeitung)
     */
    private long getStundenWeiss(){
        long rueckgabe = 0;
        String textFeldVal = this.spielerZeitStundenWeiss.getText();
        try{
            rueckgabe = 60 * 60 * 1000 * Integer.parseInt(textFeldVal);
        }catch(NumberFormatException nfe){
           sendMessageToUser("Zeit Weiss: Stundenzeit Falsch");
           rueckgabe = 0;
        }
        if(rueckgabe < 0){
            sendMessageToUser("Byo-Yomi: Minuten negativ");
            rueckgabe = 0;
        }
        return rueckgabe;
    }

    private float getKomi(){
        float rueckgabe = 0;
        String textFeldVal = this.spielerKomiWeiss.getText();
        try{
            rueckgabe = Float.valueOf(textFeldVal).floatValue();
        }catch(NumberFormatException nfe){
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
    private void sendMessageToUser(String s){
        this.fehlerBeiEingabe = true;
        this.errorString+=s+"\n";
    }

    private void versucheZuStarten() {
        this.fehlerBeiEingabe = false;
        Spieler spielerSchwarz = new Spieler(this.getNameSchwarz(),this.getStundenSchwarz()+this.getMinutenSchwarz(), 0, 0);
        Spieler spielerWeiss   = new Spieler(this.getNameWeiss(), this.getStundenWeiss()+this.getMinutenWeiss(),0, this.getKomi());
        this.dasSpielfeld.setPeriodenZeit(this.getMinutenPeriode() + this.getSekundenPeriode());
        this.dasSpielfeld.setSpielerSchwarz(spielerSchwarz);
        this.dasSpielfeld.setSpielerWeiss(spielerWeiss);
        if(this.spielMitZeitSpielen.isSelected()==true){
            this.dasSpielfeld.setIgnoreTime(false);
        }
        else{
            this.dasSpielfeld.setIgnoreTime(true);
        }
        if(this.fehlerBeiEingabe == true){

            /* Fehlerausgabe */
            this.outputMessageToUser();

            /* Werte zurueckschreiben und verbessern */
            this.syncWerteInFeldern();

            /* Da Werte nun Korrekt, keine Fehlerausgabe */
            this.errorString = "";
            this.fehlerBeiEingabe = false;

            return;
        }
        else{
            System.out.println("Alles Ok");
            /* Jetzt spiel Starten */
            return;
        }


    }

    private void zeigeHilfeAn() {
    }

    private void syncWerteInFeldern() {
        this.spielerKomiWeiss.setText(String.valueOf(this.getKomi()));
        this.spielerNameSchwarz.setText(this.getNameSchwarz());
        this.spielerNameWeiss.setText(this.getNameWeiss());
        this.spielerZeitMinutenSchwarz.setText(String.valueOf(this.getMinutenSchwarz()/60000));
        this.spielerZeitStundenSchwarz.setText(String.valueOf(this.getStundenSchwarz()/36000000));
        this.spielerZeitMinutenWeiss.setText(String.valueOf(this.getMinutenWeiss()/60000));
        this.spielerZeitStundenWeiss.setText(String.valueOf(this.getStundenWeiss()/36000000));
        this.periodenZeitMinuten.setText(String.valueOf(this.getMinutenPeriode()/60000));
        this.periodenZeitSekunden.setText(String.valueOf(this.getSekundenPeriode()/1000));
    }

    private void outputMessageToUser() {
        System.out.print(this.errorString);
        this.errorString = "";
    }

    private void initFeld() {
        this.dasSpielfeld.initialisiereFeldMitVorgabenFuerSchwarz(this.getVorgabeWert());
    }

    private int getVorgabeWert() {
        return Integer.valueOf(this.spielVorgabeSteine.getSelectedItem().toString());
    }
}