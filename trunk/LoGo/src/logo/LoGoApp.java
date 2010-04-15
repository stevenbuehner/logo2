/*
 * LoGoApp.java
 */

package logo;

import GUI.GrafikLib;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import GUI.Oberflaeche;
import GUI.TestOberflaeche;
import Klassen.Steuerung;


/**
 * The main class of the application.
 */
public class LoGoApp extends SingleFrameApplication {

    public static TestOberflaeche   meineOberflaeche;
    public static Steuerung     meineSteuerung;
    public static GrafikLib     meineGrafikLib = GrafikLib.getInstance();
    

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        show(new LoGoView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
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

        meineSteuerung      = new Steuerung();
        meineOberflaeche    = new TestOberflaeche( "LoGo, by Steven Buehner and Tommy Schladitz :D");
        meineOberflaeche.run();

        //launch(LoGoApp.class, args);
    }
}
