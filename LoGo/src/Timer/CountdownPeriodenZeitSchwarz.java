/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Timer;

import Interfaces.OberflaecheInterface;
import Logo.LoGoApp;

/**
 *
 * @author steven
 * @version 0.1
 */
public class CountdownPeriodenZeitSchwarz extends Countdown {

    public CountdownPeriodenZeitSchwarz(boolean starteSofort) {
        super(starteSofort);
    }

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
