package GUI;

import java.awt.image.BufferedImage;

/**
 *
 * @author steven
 */
public class SpielsteinMarkierung extends SpielStein {

    SpielsteinMarkierung(BufferedImage[] image, double x, double y){
        super(image, x, y);

        this.addScene(this.storedImages[0], 0);
        this.addScene(this.storedImages[1], 0);
        this.addScene(this.storedImages[2], 0);
        this.addScene(this.storedImages[3], 0);
        this.addScene(this.storedImages[4], 0);
        this.addScene(this.storedImages[5], 0);
        this.addScene(this.storedImages[6], 0);
        this.addScene(this.storedImages[7], 0);

        this.loop = true;

    }

    public SpielsteinMarkierung (BufferedImage image, double x, double y) {
        super(image, x, y);

        this.addScene(this.storedImages[0], 0);
        this.loop = true;
    }

    @Override
    protected void computeAnimation() {

        if (this.currentSceneIndex < 7){
            this.currentSceneIndex++;
        }
        else if (this.currentSceneIndex == 7 && this.loop){
            currentSceneIndex--;
        }
        else{
            // this.currentSceneIndex = this.currentSceneIndex;
        }
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
    public synchronized void starteAnimationVerbotenerZugWeiss() {}

    @Override
    public synchronized void starteAnimationVerbotenerZugSchwarz() {}

    @Override
    public synchronized void starteAnimationVerbotenerZugAufheben() {}

    @Override
    public synchronized void setVerbotenerZug() {}
    

}
