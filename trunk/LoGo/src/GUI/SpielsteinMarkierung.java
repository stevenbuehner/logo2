package GUI;

import java.awt.image.BufferedImage;

/**
 *
 * @author steven
 */
public class SpielsteinMarkierung extends SpielStein {

    SpielsteinMarkierung(BufferedImage[] image, double x, double y){
        super(image, x, y);

        this.addScene(this.storedImages[33], 0);
        this.loop = false;

    }

    public SpielsteinMarkierung (BufferedImage i, double x, double y) {
        super(i, x, y);

        this.addScene(this.storedImages[33], 0);
        this.loop = false;
    }


    @Override
    public synchronized void starteAnimationWeissSetzen() { }

    @Override
    public synchronized void starteAnimationSchwarzSetzen() {    }

    @Override
    public synchronized void starteAnimationWeissEntfernen() { }

    @Override
    public synchronized void starteAnimationSchwarzEntfernen() {}

    @Override
    public synchronized void starteAnimationVerbotenerZug() { }

    @Override
    public synchronized void starteAnimationVerbotenerZugAufheben() { }
    

}
