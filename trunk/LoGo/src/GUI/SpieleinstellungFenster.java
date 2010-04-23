/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

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

    }

    private void init(){
        /* */
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
