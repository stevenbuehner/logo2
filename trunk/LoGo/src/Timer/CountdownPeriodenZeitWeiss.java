/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Timer;

import Interfaces.OberflaecheInterface;
import logo.LoGoApp;

/**
 * Klasse fuer die Periodenzeit des Schwarzen Spielers
 * @author steven
 * @version 0.1
 */
public class CountdownPeriodenZeitWeiss extends Countdown {

    /**
     * Konstruktor der Klasse.
     * @param starteSofort Soll sofort gestartet werden?
     */
    public CountdownPeriodenZeitWeiss(boolean starteSofort) {
        super(starteSofort);
    }

    /**
     * Cowntdown einstellen.
     * @param starteSofort Soll der Cowntdown sofort starten?
     * @param remainingTime Einzustellende Zeit
     */
    public CountdownPeriodenZeitWeiss(boolean starteSofort, long remainingTime) {
        super(starteSofort, remainingTime);
    }

    @Override
    protected void doWhenCountdownFinished() {
        LoGoApp.meineOberflaeche.setAnzeigePeriodenZeitWeiss(0);
        LoGoApp.meineSteuerung.zeitAbgelaufenWeissPeriodenzeit();
    }

    @Override
    protected void doEverySecondTimerRuns() {
        OberflaecheInterface of = LoGoApp.meineOberflaeche;
        of.setAnzeigePeriodenZeitWeiss(remainingTime);
    }
}
