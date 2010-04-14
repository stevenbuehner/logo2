/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Window;
import javax.swing.JFrame;


/**
 *
 * @author steven
 */
public abstract class CoreWindow {

    private JFrame test;

    private static DisplayMode modes[] = {
        new DisplayMode(800, 600, 32, 0),
        new DisplayMode(800, 600, 24, 0),
        new DisplayMode(800, 600, 16, 0),
        new DisplayMode(640, 480, 32, 0),
        new DisplayMode(640, 480, 24, 0),
        new DisplayMode(640, 480, 16, 0)
    };

    private boolean running;
    protected ScreenManager s;



  public void stop(){
      running = false;
  }

  public void run(){
      try{
          init();
          gameLoop();
      }finally{
          s.restoreScreen();
      }
  }

  /**
   * CoreWindow initialisieren
   */
  protected void init(){
      this.s = new ScreenManager();
      DisplayMode dm = s.findFirstCompatibleMode(modes);
      s.setFullScreen(dm);

      Window w = s.getFullScreenWindow();
      w.setFont(new Font( "Arial", Font.PLAIN, 20));
      w.setBackground(Color.GREEN);
      w.setForeground(Color.WHITE);
      running = true;
  }

  public void gameLoop(){
      long startTime = System.currentTimeMillis();
      long cumTime = startTime;

      while(running){
          long timePassed = System.currentTimeMillis() - cumTime;
          cumTime += timePassed;

          update(timePassed);

          Graphics2D g = s.getGraphics();
          draw(g);
          g.dispose();
          s.update();

          try{
              Thread.sleep(20);
          }catch(Exception ex){}
      }
  }

  public void update (long timePased){

  }
  
  public abstract void draw(Graphics2D g);

}
