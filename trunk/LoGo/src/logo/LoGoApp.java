/*
 * LoGoApp.java
 */
package logo;

import GUI.GrafikLib;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UnsupportedLookAndFeelException;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import GUI.TestOberflaeche;
import Klassen.Steuerung;
import interfaces.OberflaecheInterface;
import interfaces.SteuerungInterface;
import javax.swing.UIManager;

/**
 * The main class of the application.
 */
public class LoGoApp extends SingleFrameApplication {

    public static OberflaecheInterface meineOberflaeche;
    public static SteuerungInterface meineSteuerung;
    public static GrafikLib meineGrafikLib = GrafikLib.getInstance();

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        show(new LoGoView(this));
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
         try {
	    // Set System L&F
        UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
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
        meineOberflaeche = new TestOberflaeche("LoGo, by Steven Buehner, Alex Jesche, Rebecca Kina and Tommy Schladitz");

        //launch(LoGoApp.class, args);
    }
}
