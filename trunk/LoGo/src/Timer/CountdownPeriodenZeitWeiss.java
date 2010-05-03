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
public class CountdownPeriodenZeitWeiss extends Countdown {

    public CountdownPeriodenZeitWeiss(boolean starteSofort) {
        super(starteSofort);
    }

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
