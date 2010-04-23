/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import Klassen.Spielfeld;
import java.awt.FlowLayout;
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
        this.panelAllgemein = new JPanel(new FlowLayout());
        this.panelSchwarz   = new JPanel(new FlowLayout());
        this.panelWeiss     = new JPanel(new FlowLayout());
        this.panelSpielfeld = new JPanel(new FlowLayout());

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

    }

    public void mouseClicked(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
