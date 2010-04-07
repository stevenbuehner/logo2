
package GUI;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;



/**
 *
 * @author steven
 * @version 0.1
 * Die Klasse wurde als Vorlage aus einem Forum entnommen:
 * http://www.java-forum.org/awt-swing-swt/95342-spielfeld-gitter-einzelne-zellen-veraendern.html#_
 *
 * Diese Klasse wird aktuell nirgends im Spiel verwedet!!!!
 */
public class Spielbrett extends Canvas
{
    //int state = 0;
    private int     i;
    private int     wert    = 20;
    private int     x       = 20;
    private int     y       = 20;
    private int[][] array;

    // Array-Inhalte alle auf 0 setzen
    public Spielbrett()
    {
        array = new int[x][y];
        for(int m = 0; m < x; m++)
        {
            for(int n = 0; n < x; n++)
            {
                array[m][n] = 0;
            }
        }
    }

    @Override
    public void paint(Graphics g)
    {
        // Gitter zeichnen
        for(i=0;i<=x;i++)
        {
            g.drawLine(wert*i, wert*x, wert*i , 0);
        }

        for(i=0;i<=y;i++)
        {
            g.drawLine(0 , wert*i, wert*y, wert*i);
        }

        // Zellen ausfüllen, bei denen das Array den Inhalt "1" hat
        for(int k = 0; k < x; k++)
        {
            for(int l = 0; l < y; l++)
            {
                if(array[k][l] == 1)
                {
                    g.setColor(Color.BLACK);
                    g.fillRect(0+k*wert,0+l*wert,wert,wert);
                }
            }
        }
    }

}