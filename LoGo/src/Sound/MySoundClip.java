package Sound;

import java.applet.*;

/**
 *
 * @author steven
 */
public class MySoundClip extends Thread implements AudioClip {

    AudioClip ac;
    boolean looping = false;
    boolean playing = false;

    /**
     * einstellen des Audio-clipps
     * @param a audio-clip
     */
    public MySoundClip(AudioClip a) {
        ac = a;
    }

    public void loop() {
        looping = true;
        start();
    }

    public void play() {
        playing = true;
        start();
    }

    /**
     * Den Audio-Clip stoppen
     */
    public void stopClip() {
        ac.stop();
    }

    @Override
    public void run() {

        if (looping) {
            ac.loop();
        }

        if (playing) {
            ac.play();
        }
    }
}
