/*
 * LoGoApp.java
 */
package logo;

import GUI.FensterAuswertung;
import GUI.GrafikLib;
import GUI.FensterEinstellung;
import javax.swing.UnsupportedLookAndFeelException;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import GUI.FensterSpieloberflaeche;
import Klassen.Steuerung;
import interfaces.OberflaecheInterface;
import interfaces.SteuerungInterface;
import javax.swing.UIManager;

/**
 * The main class of the application.
 */
public class LoGoApp extends SingleFrameApplication {

    public static OberflaecheInterface meineOberflaeche;
    public static FensterEinstellung meinEinstellungsfenster;
    public static GrafikLib meineGrafikLib = GrafikLib.getInstance();
    public static SteuerungInterface meineSteuerung;
    public static FensterAuswertung meinAuswertungsfenster;
    public static boolean debug = false;

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        //show(new LoGoView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of LoGoApp
     */
    public static LoGoApp getApplication() {
        return Application.getInstance(LoGoApp.class);
    }

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

        meineSteuerung = new Steuerung();
        meineOberflaeche = new FensterSpieloberflaeche("LoGo, by Steven Buehner, Alex Jesche, Rebecca King and Tommy Schladitz");
        meinEinstellungsfenster = new FensterEinstellung("Einstellungen");
        meinAuswertungsfenster = new FensterAuswertung();

        //launch(LoGoApp.class, args);
    }
}
