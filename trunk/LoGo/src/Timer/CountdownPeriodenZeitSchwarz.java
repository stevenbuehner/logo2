/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Timer;

import Interfaces.OberflaecheInterface;
import logo.LoGoApp;

/**
 *
 * @author steven
 * @version 0.1
 */
public class CountdownPeriodenZeitSchwarz extends Countdown {

    /**
     * Konstruktor der Klasse
     * @param starteSofort Soll der Cowntdown sofort starten?
     */
    public CountdownPeriodenZeitSchwarz(boolean starteSofort) {
        super(starteSofort);
    }

    /**
     * Countdown einstellen.
     * @param starteSofort Soll Countdown sofort Starten?
     * @param remainingTime Einzustellende Zeit in Millisekunden
     */
    public CountdownPeriodenZeitSchwarz(boolean starteSofort, long remainingTime) {
        super(starteSofort, remainingTime);
    }

    @Override
    protected void doWhenCountdownFinished() {
        LoGoApp.meineOberflaeche.setAnzeigePeriodenZeitSchwarz(0);
        LoGoApp.meineSteuerung.zeitAbgelaufenSchwarzPeriodenzeit();
    }

    @Override
    protected void doEverySecondTimerRuns() {
        OberflaecheInterface of = LoGoApp.meineOberflaeche;
        of.setAnzeigePeriodenZeitSchwarz(remainingTime);
    }
}
