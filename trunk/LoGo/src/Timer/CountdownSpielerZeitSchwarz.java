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
public class CountdownSpielerZeitSchwarz extends Countdown {

    /**
     * Konstruktor der Klasse
     * @param starteSofort Soll der Countdown sofort laufen?
     */
    public CountdownSpielerZeitSchwarz(boolean starteSofort) {
        super(starteSofort);
    }

    /**
     * Countdown einstellen
     * @param starteSofort Soll Countdown sofort laufen?
     * @param remainingTime Einzustellende Zeit.
     */
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
