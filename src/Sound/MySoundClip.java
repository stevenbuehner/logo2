package Sound;

import java.applet.*;

public class MySoundClip extends Thread implements AudioClip {

    AudioClip ac;
    boolean looping = false;
    boolean playing = false;

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
