package Klassen;

import java.sql.SQLException;
import logo.LoGoApp;

/**
 *
 * @author steven
 */
public class BackgroundStatistikLoader implements Runnable{

    private int anzahlStatistikEintrage;
    private boolean once = false;

    public BackgroundStatistikLoader( int anzahlEintrage ){
        this.anzahlStatistikEintrage = anzahlEintrage;

        Thread t = new Thread(this);
        t.start();
    }

    public void run() {

        HistoryEintrag histEintraege[] = new HistoryEintrag[anzahlStatistikEintrage];
        HistoryConnector cn = new HistoryConnector();

        try {
            cn.open();
            histEintraege = cn.holeDieBestenHistoryEintraege(anzahlStatistikEintrage);
            cn.close();
        } catch (SQLException ex) {
            if (LoGoApp.debug) {
                System.out.println("BackgroundStatistikLoader kann keine History-Einträge laden?!");
            }

        } catch (Exception ex){
            if (LoGoApp.debug) {
                System.out.println(ex.getMessage());
            }
        }
        finally {
            LoGoApp.meinAuswertungsfenster.setHistoryEintraege(histEintraege);
        }
    }
}
