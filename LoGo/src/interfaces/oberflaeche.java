/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package interfaces;

/**
 *
 * @author steven
 * @version 0.1
 */
public interface oberflaeche {
    public void setBrettOberflaeche( int spielfeld[][], int spielfeldGroesse );
    public void updateGUITimerWeiss( long periodenZeitInMS, long spielerZeitInMS );
    public void updateGUITimerSchwarz( long periodenZeitInMS, long spielerZeitInMS );
    public void setSpielernameWeiss( String spielername );
    public void setSpielernameSchwarz( String spielername );
    public void setGefangeneSteineWeiss( int anzGefangenerSteiner );
    public void setGefangeneSteineSchwarz( int anzGefangenerSteiner );
    public void setSchwarzAmZug();
    public void setWeissAmZug();

}
