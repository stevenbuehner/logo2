package GUI;

import java.awt.image.BufferedImage;

/**
 *
 * @author steven
 */
public class OneScene {

    private BufferedImage pic;
    private long sceneDuration;

    /**
     * Defaultkonstruktor
     * @param pic
     * @param sceneDuration
     */
    public OneScene(BufferedImage pic, long sceneDuration) {
        this.pic = pic;
        this.sceneDuration = sceneDuration;
    }

    /**
     *
     * @return Gibt das Image dieser Szene zurueck
     */
    public BufferedImage getPic(){
        return this.pic;
    }

    /**
     *
     * @return Gibt die anzuzeigende Zeit fuer diese Szene zurueck
     */
    public long getSceneDuration(){
        return this.sceneDuration;
    }
}