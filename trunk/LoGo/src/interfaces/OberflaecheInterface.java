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
public interface OberflaecheInterface {
    /**
     *
     * @param spielfeld ist ein zweidimensionales Integer-Array mit der Groesse
     * @param spielfeldGroesse. Das Array kann die Werte SCHNITTPUNKT_LEER,
     * SCHNITTPUNKT_SCHWARZ, SCHNITTPUNKT_WEISS und SCHNITTPUNKT_VERBOTEN enthalten,
     * welche in der Klasse Konstante spezifiziert sind.
     */
    public void setBrettOberflaeche( int spielfeld[][], int spielfeldGroesse );
    public void setAnzeigePeriodenZeitWeiss( long periodenZeitInMS );
    public void setAnzeigePeriodenZeitSchwarz( long periodenZeitInMS );
    public void setAnzeigeSpielerZeitWeiss( long spielerZeitInMS );
    public void setAnzeigeSpielerZeitSchwarz( long spielerZeitInMS );
    public void setSpielernameWeiss( String spielername );
    public void setSpielernameSchwarz( String spielername );
    public void setGefangeneSteineWeiss( int anzGefangenerSteiner );
    public void setGefangeneSteineSchwarz( int anzGefangenerSteiner );
    public void setSchwarzAmZug();
    public void setWeissAmZug();
    public void gibFehlermeldungAus( String fehlertext );

}
