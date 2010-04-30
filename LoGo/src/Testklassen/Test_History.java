/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Testklassen;

import Klassen.HistoryConnector;
import Klassen.HistoryEintrag;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author steven
 */
public class Test_History {


    public static void main(String[] args) throws SQLException {
        HistoryConnector hc = new HistoryConnector();
        try {
            hc.open();
            HistoryEintrag he = hc.holeErstenHistoryEintrag();

            hc.sendeNeuenHistoryEintrag(he);
            
        } catch (SQLException ex) {
            Logger.getLogger(Test_History.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(Test_History.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            hc.close();
        }
      }
}
