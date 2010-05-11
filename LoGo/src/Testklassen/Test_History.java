package Testklassen;

import Klassen.HistoryConnector;
import Klassen.HistoryEintrag;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasse zum Testen der Datenbank-Zugriffsfunktionen über das Internet
 * @author steven
 */
public class Test_History {


    /**
     * Main Programm zum starten der Tests
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        HistoryConnector hc = new HistoryConnector();
        HistoryEintrag eintraege[] = null;

        try {
            hc.open();
            eintraege = hc.holeDieBestenHistoryEintraege(3);

            //hc.sendeNeuenHistoryEintrag(he);
            
        } catch (SQLException ex) {
            Logger.getLogger(Test_History.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(Test_History.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{

            for( int i = 0; i < eintraege.length; i++){
                System.out.println(eintraege[i].getNameSpielerSchwarz() + ", " +
                        eintraege[i].getNameSpielerWeiss() + ", " +
                        eintraege[i].getPunkteSpielerSchwarz() + ", " +
                        eintraege[i].getPunkteSpielerWeiss() + ", " +
                        eintraege[i].getDatum() );
            }

            hc.close();
      }
   }
}
