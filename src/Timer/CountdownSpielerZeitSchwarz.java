/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Timer;

import interfaces.OberflaecheInterface;
import logo.LoGoApp;

/**
 *
 * @author steven
 * @version 0.1
 */
public class CountdownSpielerZeitSchwarz extends Countdown {

    public CountdownSpielerZeitSchwarz(boolean starteSofort) {
        super(starteSofort);
    }

    public CountdownSpielerZeitSchwarz(boolean starteSofort, long remainingTime) {
        super(starteSofort, remainingTime);
    }

    @Override
    protected void doWhenCountdownFinished() {
        LoGoApp.meineOberflaeche.setAnzeigeSpielerZeitSchwarz(0);
        LoGoApp.meineSteuerung.zeitAbgelaufenSchwarzHauptzeit();
    }

    @Override
    protected void doEverySecondTimerRuns() {
        OberflaecheInterface of = LoGoApp.meineOberflaeche;
        of.setAnzeigeSpielerZeitSchwarz(remainingTime);
    }
}
