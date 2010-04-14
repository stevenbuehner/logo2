/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JFrame;

/**
 *
 * @author steven
 */
public class Images extends JFrame{
    public static void main (String[] args){

        DisplayMode dm = new DisplayMode(800, 600, 16, DisplayMode.REFRESH_RATE_UNKNOWN);
        Images of = new Images();
        of.run(dm);
    }

    public void run(DisplayMode dm){
        setBackground(Color.PINK);
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.PLAIN, 24));

        ScreenManager s = new ScreenManager();
        try {
            s.setFullScreen(dm, this);
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
            }
        } finally{
            s.restoreScreen();
        }
    }

    public void paint(Graphics g){
        if(g instanceof Graphics2D){
            // Text etwas schoener darstellen
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        g.drawString("This is gona be awesome", 200, 200);
    }
}
