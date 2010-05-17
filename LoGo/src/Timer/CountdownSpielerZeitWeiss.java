/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Timer;

import Interfaces.OberflaecheInterface;
import logo.LoGoApp;

/**
 * Countdown fuer die Hauptzeit des weissen Spielers
 * @author steven
 * @version 0.1
 */
public class CountdownSpielerZeitWeiss extends Countdown {

    /**
     * Konstruktor der Klasse
     * @param starteSofort Soll Countdown sofort starten?
     */
    public CountdownSpielerZeitWeiss(boolean starteSofort) {
        super(starteSofort);
    }

    /**
     * Einstellen des Countdowns
     * @param starteSofort Soll der Countdown sofort starten?
     * @param remainingTime Einzustellende Zeit in Millisekunden.
     */
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
