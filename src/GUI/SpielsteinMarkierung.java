package GUI;

import java.awt.image.BufferedImage;

/**
 *
 * @author steven
 * @version 0.2
 */
public class SpielsteinMarkierung extends SpielStein {

    SpielsteinMarkierung(BufferedImage[] image) {
        super(image, 0, 0);

        //this.addScene(storedImages[48], 1000);
        this.addScene(storedImages[55], 300); // leerlauf
        this.addScene(storedImages[49], 100);
        this.addScene(storedImages[50], 100);
        this.addScene(storedImages[51], 100);
        this.addScene(storedImages[52], 100);
        this.addScene(storedImages[53], 100);
        this.addScene(storedImages[54], 8000);

        this.setLoop(true, 1);
    }

    public SpielsteinMarkierung(BufferedImage image) {
        super(image, 0, 0);

        this.addScene(this.storedImages[0], 0);
        this.setLoop(false);
    }

    @Override
    public synchronized void starteAnimationWeissSetzen() {
    }

    @Override
    public synchronized void starteAnimationSchwarzSetzen() {
    }

    @Override
    public synchronized void starteAnimationWeissEntfernen() {
    }

    @Override
    public synchronized void starteAnimationSchwarzEntfernen() {
    }

    @Override
    public synchronized void starteAnimationVerbotenerZugWeiss() {
    }

    @Override
    public synchronized void starteAnimationVerbotenerZugSchwarz() {
    }

    @Override
    public synchronized void starteAnimationVerbotenerZugAufheben() {
    }

    @Override
    public synchronized void setVerbotenerZug() {
    }
}
