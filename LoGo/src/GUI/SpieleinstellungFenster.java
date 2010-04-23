/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import Klassen.Spielfeld;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
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

    private JTextField spielVorgabeSteine;
    private JTextArea spielBrettHinweise;

    private JButton spielStarten;
    
    // Panes
    private JPanel panelAllgemein;
    private JPanel panelSchwarz;
    private JPanel panelWeiss;
    private JPanel panelSpielfeld;
    private JPanel spielgroessenWahl;

    // Datenhaltung
    private Spielfeld dasSpielfeld;


    public SpieleinstellungFenster(){
        this( "Einstellungsfenster");
    }

    public SpieleinstellungFenster(String fenstername) {
        super(fenstername);

        this.init();

        this.setSize(500, 500);
        this.setResizable(false);
        this.setLocationRelativeTo(null);       // Fenster zentrieren

        this.setVisible(true);


    }

    private void init(){
        /* Panels initialisieren */
        this.panelAllgemein = new JPanel(new GridLayout(4,2));
        this.panelSchwarz   = new JPanel(new GridLayout(3,2));
        this.panelWeiss     = new JPanel(new GridLayout(3,3));
        this.panelSpielfeld = new JPanel(new GridLayout(3,2));
        this.spielgroessenWahl = new JPanel(new GridLayout(7, 1));

        /* Textfields Initialisieren */
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
        this.spielMitZeitSpielen       = new JCheckBox("Mit Zeit spielen", true);

        this.spielFeldAuswahl          = new ButtonGroup();
        this.siebenXsieben             = new JRadioButton("7 x 7",false);
        this.neunXneun                 = new JRadioButton("9 x 9",false);
        this.elfXelf                   = new JRadioButton("11 x 11",false);
        this.dreizehnXdreizehn         = new JRadioButton("13 x 13",true);
        this.fuenfzehnXfuenfzehn       = new JRadioButton("15 x 15",false);
        this.siebzehnXsiebzehn         = new JRadioButton("17 x 17",false);
        this.neunzehnXneunzehn         = new JRadioButton("19 x 19",false);

        this.spielVorgabeSteine        = new JTextField("0");
        this.spielBrettHinweise        = new JTextArea("", 15, 5);
        this.spielStarten              = new JButton("Start");
        this.dasSpielfeld              = new Spielfeld(13);

        /* Jetzt wurden alle werte zumindest initialisiert,
         * Nun die panel zuteilen */
        this.panelAllgemein.add(this.spielermodus);
        this.panelAllgemein.add(this.spielMitZeitSpielen);
        this.panelAllgemein.add(this.periodenZeitMinuten);
        this.panelAllgemein.add(this.periodenZeitSekunden);

        this.panelSchwarz.add(this.spielerNameSchwarz);
        this.panelSchwarz.add(this.spielerZeitStundenSchwarz);
        this.panelSchwarz.add(this.spielerZeitMinutenSchwarz);

        this.panelWeiss.add(this.spielerNameWeiss);
        this.panelWeiss.add(this.spielerZeitStundenWeiss);
        this.panelWeiss.add(this.spielerZeitMinutenWeiss);
        this.panelWeiss.add(this.spielerKomiWeiss);
        

        this.spielgroessenWahl.add(this.siebenXsieben);
        this.spielgroessenWahl.add(this.neunXneun);
        this.spielgroessenWahl.add(this.elfXelf);
        this.spielgroessenWahl.add(this.dreizehnXdreizehn);
        this.spielgroessenWahl.add(this.fuenfzehnXfuenfzehn);
        this.spielgroessenWahl.add(this.siebzehnXsiebzehn);
        this.spielgroessenWahl.add(this.neunzehnXneunzehn);

        this.panelSpielfeld.add(this.spielgroessenWahl);

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
    
        if(e.getSource() == this.spielMitZeitSpielen){
            this.toggleSpielMitZeitSpielen();
        }

    }


    private void spielmodusChanged(){

    }

    /**
     * Toggelt die Spiel-Einstellungs-Felder die mit der SPielzeit zu tun haben
     * zwischen Enabled und Disabled hin und her.
     */
    private void toggleSpielMitZeitSpielen() {
        JCheckBox box = (JCheckBox)this.spielMitZeitSpielen;
        if( box.isSelected() ){
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
     * Wird aufgerufen, wenn der User etwas an der periodenZeit geändert hat.
     * Wird aufgerufen egal ob im Minuten oder Sekundenfeld etwas geänder wurde.
     * Die Funktion validiert die Usereingaben dieser Felder und ändert sie
     * gegebenenfalls ab.
     */
    private void periodenZeitChanged(){
        
    }

    /**
     * Wird aufgerufen wenn der Spielername von Schwarz geändert wurde.
     * Die Funktion validiert die Usereingaben und ändert sie
     * gegebenenfalls ab.
     */
    private void spielerNameSchwarzChanged(){
        
    }

    /**
     * Wird aufgerufen wenn entweder die Stunden- oder die Minutenangaben des
     * schwarzen Spielers geändert wurden. Die Funktion validiert die
     * Usereingaben dieser Felder und korrigiert sie gegebenenfalls.
     */
    private void spielerZeitSchwarzChanged(){
        
    }

    /**
     * Wird aufgerufen wenn der Spielername von Weiss geändert wurde.
     * Die Funktion validiert die Usereingaben und ändert sie
     * gegebenenfalls ab.
     */
    private void spielerNameWeissChanged(){
        
    }

    /**
     * Wird aufgerufen wenn entweder die Stunden- oder die Minutenangaben des
     * weissen Spielers geändert wurden. Die Funktion validiert die
     * Usereingaben dieser Felder und korrigiert sie gegebenenfalls.
     */
    private void spielerZeitWeissChanged(){
        
    }

    /**
     * Diese Funktion wird aufgerufen, wenn die Eingaben im Komi-Textfeld
     * durch den User verändert wurden. Es wird überprüft, ob diese Einstellung
     * im zulässigen Bereich > 0 ist und gegebenfalls werden die Werte angepasst.
     */
    private void spielerKomiWeissChanged(){
        
    }

    /**
     * Diese Funktion wird aufgerufen, wenn die Spielfeldgroesse verändert wurde.
     * Sie passt die Einstellungen für das Spielfeld an.
     */
    private void spielfeldGroesseChanged(){
        
    }

    /**
     * Wird aufgerufen wenn das Textfeld mit den Vorgabesteinen geändert wird.
     * Die Funktion validiert gleichzeitig den eingegebenen Wert.
     */
    private void vorgabeAnzahlChanged(){
        
    }

    /**
     * Wird aufgerufen wenn das Feld angeklickt wurde und übergibt die Pixelkoordinaten
     * abhängig von der linken oberen Ecke des Spielfeldes.
     * @param xPos
     * @param yPos
     */
    private void klickAufFeld( int xPos, int yPos){
        
    }







}
