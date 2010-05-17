package GUI;

import java.awt.image.BufferedImage;
import javax.swing.JFrame;



/**
 * Klasse für das Credits-Fenster
 * @author steven
 */
public class FensterCredits extends JFrame{

    private BackgroundImagePanel backgroundPanel;

    /**
     * Standardkonstruktor mit welchem ein Objekt vom Typ FensterCredits erstellt
     * werden kann. Sie ruft die Methoden super() und init() auf. Im Anschluss
     * wird das Fenster NICHT sichtbar gemacht, sondern auf visible=false gesetzt.
     * Um es dann anzuzeigen muss die Methode setVisible(true) aus der Vaterklasse
     * aufgerufen werden.
     * @see JFrame
     */
    public FensterCredits() {
        super("Credits");

        this.init();

        this.setVisible(false);
    }

    /**
     * Methode um die initialisierungsschritte aus dem Konstruktor auszulagen.
     * Sie lädt die Grafiken, setzt die Fenstergröße und Fensterposition.
     * Desweiteren wird die bei Exit-Klick HIDE_ON_CLOSE definiert.
     */
    private void init(){

        GrafikLib lib = GrafikLib.getInstance();
        BufferedImage bg = lib.getSprite("GUI/resources/Credits.jpg");
        this.backgroundPanel = new BackgroundImagePanel(bg);
        this.setContentPane(backgroundPanel);

        this.setSize(556, 392);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }
    
}
