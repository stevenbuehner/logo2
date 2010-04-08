package Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 *
 * @author steven
 * @version 1.0
 * Uebernommen aus dem Spiel >Wer wird Biblionaer< von Steven Buehner
 */
public abstract class Countdown {

    protected Timer countdownTimer;
    protected long remainingTime;

    public Countdown(boolean starteSofort) {
        this(starteSofort, 30); // Starte mit Standarzeit, 30 Sekunden
    }

    public Countdown(boolean starteSofort, long zeitInMillesekunden) {
        this.remainingTime = zeitInMillesekunden;

        countdownTimer = new Timer(1000, new CountdownTimerListener());
        countdownTimer.setRepeats(true); // Standard

        if (starteSofort) {
            countdownTimer.start();
        }
    }

    public long getRemainingTime() {
        return this.remainingTime;
    }

    public void setRemainingTime(long remainingTimeInMillesekunden) {
        this.remainingTime = remainingTimeInMillesekunden;
    }

    /**
     * Startet den Countdown
     */
    public void starteCountdown() {
        if (!countdownTimer.isRunning()) {
            countdownTimer.start();
        }
    }

    /**
     * Stoppt den Countdown
     */
    public void stoppeCountdown() {
        if (countdownTimer.isRunning()) {
            countdownTimer.stop();
        }
    }

    /**
     * Wenn der Countdown läuft und bei 0 angelang ist, wird diese Methode
     * aufgerufen.
     */
    protected abstract void doWhenCountdownFinished();

    /**
     * Diese Methode wird nur aufgerufen, wenn der Countdown läuft und noch
     * nicht bei 0 angelant ist. Diese Methode wird beim erreichen der 0 nicht
     * mehr aufgerufen. Siehe stattdessen doWhenCountdownFinished().
     */
    protected abstract void doEverySecondTimerRuns();

    class CountdownTimerListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            // 1 Sekunde davon abziehen
            remainingTime -= 1000;
            
            if (remainingTime > 0) {
                doEverySecondTimerRuns();

                /*
                 * Diese Abfrage macht die Funktion zwar exakter,
                 * koennte den Benutzer aber irritieren ...
                 * und wird darum auskommentiert

                if( remainingTime < 1000 ){
                countdownTimer.setDelay( remainingTime );
                }
                 */

                // label.setText(String.valueOf(remainingTime));
            } else {
                doWhenCountdownFinished();
                // label.setText("Time's up!");
                countdownTimer.stop();
            }
        }
    }
}
