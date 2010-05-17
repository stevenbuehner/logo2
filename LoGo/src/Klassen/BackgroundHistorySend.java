/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Klassen;

import java.sql.SQLException;
import logo.LoGoApp;

/**
 *
 * @author tommy
 */
public class BackgroundHistorySend implements Runnable{
    private HistoryEintrag historyE;
    private boolean once = false;

    /**
     * Konstruktor der Klasse.
     * @param hist Zu versendender History-Eintrag.
     */
    public BackgroundHistorySend( HistoryEintrag hist ){

        this.historyE = hist;
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {

        HistoryConnector hc = new HistoryConnector();
        HistoryEintrag histEintr = this.historyE;
        if (LoGoApp.debug) {
            System.out.println();
        }

        try {
            hc.open();
            hc.sendeNeuenHistoryEintrag(histEintr);
            hc.close();

        } catch (SQLException ex) {
            System.out.println("Datenbankübertragungsfehler: " + ex.getCause());
        } catch (Exception ex) {
            System.out.println("Datenbankübertragungsfehler: " + ex.getCause());
        }
    }

}
