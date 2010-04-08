/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Timer;

import logo.LoGoApp;

/**
 *
 * @author steven
 */
public class CountdownSpielerZeitSchwarz extends Countdown {

    public CountdownSpielerZeitSchwarz( boolean starteSofort ){
        super( starteSofort );
    }

    public CountdownSpielerZeitSchwarz( boolean starteSofort, long remainingTime ){
        super(true, remainingTime);
    }
    
    @Override
    protected void doWhenCountdownFinished() {
        LoGoApp.meineOberflaeche.setAnzeigeSpielerZeitSchwarz( 0 );
        LoGoApp.meineSteuerung.zeitAbgelaufenSchwarzHauptzeit();
    }

    @Override
    protected void doEverySecondTimerRuns() {
        LoGoApp.meineOberflaeche.setAnzeigeSpielerZeitSchwarz( remainingTime );
    }

}
