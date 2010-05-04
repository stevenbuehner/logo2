package logo;

import GUI.FensterAuswertung;
import GUI.FensterCredits;
import GUI.GrafikLib;
import GUI.FensterEinstellung;
import javax.swing.UnsupportedLookAndFeelException;
import GUI.FensterSpieloberflaeche;
import Klassen.Steuerung;
import Interfaces.OberflaecheInterface;
import Interfaces.SteuerungInterface;
import Klassen.DateiVorlader;
import javax.swing.UIManager;

/**
 * The main class of the application.
 */
public class LoGoApp {

    public static OberflaecheInterface meineOberflaeche;
    public static FensterEinstellung meinEinstellungsfenster;
    public static GrafikLib meineGrafikLib = GrafikLib.getInstance();
    public static SteuerungInterface meineSteuerung;
    public static FensterAuswertung meinAuswertungsfenster;
    public static FensterCredits meineCredits;
    public static boolean debug = false;


    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        UIManager.put("OptionPane.cancelButtonText", "Abbrechen");
        UIManager.put("OptionPane.noButtonText", "Nein");
        UIManager.put("OptionPane.okButtonText", "Einverstanden");
        UIManager.put("OptionPane.yesButtonText", "Ja");

        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
        } catch (ClassNotFoundException e) {
            // handle exception
        } catch (InstantiationException e) {
            // handle exception
        } catch (IllegalAccessException e) {
            // handle exception
        }
        /*
        try {
        // Set cross-platform Java L&F (also called "Metal")
        UIManager.setLookAndFeel(
        UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e) {
        // handle exception
        }
        catch (ClassNotFoundException e) {
        // handle exception
        }
        catch (InstantiationException e) {
        // handle exception
        }
        catch (IllegalAccessException e) {
        // handle exception
        }

         */

        new DateiVorlader();
        
        meineSteuerung = new Steuerung();
        meinAuswertungsfenster = new FensterAuswertung();
        meineCredits = new FensterCredits();

        meineOberflaeche = new FensterSpieloberflaeche("LoGo, by Steven Buehner, Alex Jesche, Rebecca King and Tommy Schladitz");
        meinEinstellungsfenster = new FensterEinstellung("Einstellungen");

        //launch(LoGoApp.class, args);
    }
}
