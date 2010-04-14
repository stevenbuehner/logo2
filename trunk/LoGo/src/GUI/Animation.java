/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import java.awt.Image;
import java.util.ArrayList;

/**
 *
 * @author steven
 */
public class Animation {

    private ArrayList<OneScene> scenes;
    private int sceneIndex;
    private long movieTime;
    private long totalTime;

    public Animation(){
        this.scenes = new ArrayList();
        this.totalTime = 0;
        start();
    }


    public synchronized void addScene(Image i, long t){
        this.totalTime += t;
        this.scenes.add(new OneScene(i, movieTime));
    }

    /**
     * Animationseinstellungen auf die erste Szene setzen (init)
     */
    public synchronized void start(){
        this.movieTime = 0;
        this.sceneIndex = 0;
    }
    
    public synchronized void update(long timePassed){
        if(this.scenes.size()>1){
            // Nur wenn auch mehr als eine Szene besteht muss das Bild geaendert werden
            this.movieTime += timePassed;
            if(this.movieTime >= this.totalTime){
                this.movieTime = 0;     // Warum nicht: this.movieTime = this.movieTime%this.totalTime
                this.sceneIndex = 0;
            }
            while( this.movieTime > getScene(this.sceneIndex).getEndTime()){
                this.sceneIndex++;
            }
        }
    }
    
    /**
     * 
     * @return Das aktuelle Image der Animation
     */
    public synchronized Image getImage(){
        if(scenes.size()==0){
            return null;
        }else{
            return getScene(this.sceneIndex).getPic();
        }
    }

    /**
     *
     * @param sceneIndex
     * @return Gibt Szene am sceneIndex zurueck
     */
    private OneScene getScene(int sceneIndex) {
        return (OneScene)scenes.get(sceneIndex);
    }

}
