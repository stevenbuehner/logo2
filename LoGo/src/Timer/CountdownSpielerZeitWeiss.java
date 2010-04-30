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
public class CountdownSpielerZeitWeiss extends Countdown {

    public CountdownSpielerZeitWeiss(boolean starteSofort) {
        super(starteSofort);
    }

    public CountdownSpielerZeitWeiss(boolean starteSofort, long remainingTime) {
        super(starteSofort, remainingTime);
    }

    @Override
    protected void doWhenCountdownFinished() {
        LoGoApp.meineOberflaeche.setAnzeigeSpielerZeitWeiss(0);
        LoGoApp.meineSteuerung.zeitAbgelaufenWeissHauptzeit();
    }

    @Override
    protected void doEverySecondTimerRuns() {
        OberflaecheInterface of = LoGoApp.meineOberflaeche;
        of.setAnzeigeSpielerZeitWeiss(remainingTime);
    }
}
