/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Image;

/**
 *
 * @author steven
 */
public class OneScene {

    private Image pic;
    private long endTime;

    /**
     * Defaultkonstruktor
     * @param pic
     * @param endTime
     */
    public OneScene(Image pic, long endTime) {
        this.pic = pic;
        this.endTime = endTime;
    }

    /**
     *
     * @return Gibt das Image dieser Szene zurueck
     */
    public Image getPic(){
        return this.pic;
    }

    /**
     * 
     * @return Gibt die anzuzeigende Zeit fuer diese Szene zurueck
     */
    public long getEndTime(){
        return this.endTime;
    }
}
