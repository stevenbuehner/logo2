/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import Klassen.Konstante;
import Klassen.Steuerung;
import interfaces.oberflaecheInterface;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import javax.swing.JFrame;
import logo.LoGoApp;

/**
 *
 * @author steven
 * @version 0.1
 */
public class Oberflaeche extends Canvas implements oberflaecheInterface, MouseListener, KeyListener {

    private Steuerung                   meineSteuerung;

    private VolatileImage		backbuffer;
    private GraphicsEnvironment         ge;
    private GraphicsConfiguration	gc;
    private BufferStrategy		strategy;

    
    protected Frame			frame;
    protected boolean                   once		= false;


    private BufferedImage		backgroundImage;						// Hintergrundbild


    private int                         spielfeldGroesse    = 9;
    private int                         spielfeldBreite     = 480;
    private int                         spielfeldHoehe      = 480;
    private int                         feldBreite          = spielfeldBreite/spielfeldGroesse;
    private int                         feldHoehe           = spielfeldHoehe/spielfeldGroesse;

    private int                         spielSteine[][];


    public Oberflaeche() {
        this( "LoGo by DHBW", 678, 549, LoGoApp.meineSteuerung );
    }

    public Oberflaeche (String fenstername, int width, int height, Steuerung pSteuerung) {
		meineSteuerung = pSteuerung;


		this.setPreferredSize( new Dimension( width, height ) );

		frame = new JFrame( fenstername ); // geaendert wegen while
		// frame.setLocationRelativeTo( null );
		frame.setLocation( 200, 100 );
		frame.addKeyListener( this );
		this.addMouseListener( this );
		frame.add( this );
		frame.pack();
		frame.setResizable( false );
		frame.setIgnoreRepaint( true );

		// frame.setMenuBar( createMenue() );

		frame.setVisible( true );

		createBufferStrategy( 2 );
		strategy = getBufferStrategy();
		createBackbuffer();

		doInitializations();
	}

    protected void doInitializations() {
     //   this.backgroundImage = new I
    }

    protected void createBackbuffer() {
        if ( backbuffer != null ) {
		backbuffer.flush();
		backbuffer = null;
	}
        
        // GraphicsConfiguration für VolatileImage
	ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
	backbuffer = gc.createCompatibleVolatileImage( getWidth(), getHeight() );
    }

    @Override
    public void paint(Graphics g)
    {
        int x       = spielfeldGroesse;
        int y       = spielfeldGroesse;
        int wert    = feldBreite;

        int xOffset = feldBreite/2;
        int yOffset = feldHoehe/2;

        /*
        //Spielsteine setzen Test
        spielSteine = new int[this.spielfeldGroesse][this.spielfeldGroesse];
        spielSteine[1][2] = Konstante.SCHNITTPUNKT_SCHWARZ;
        spielSteine[1][3] = Konstante.SCHNITTPUNKT_SCHWARZ;
        spielSteine[1][4] = Konstante.SCHNITTPUNKT_SCHWARZ;
        spielSteine[1][5] = Konstante.SCHNITTPUNKT_SCHWARZ;

        spielSteine[2][2] = Konstante.SCHNITTPUNKT_WEISS;
        spielSteine[3][3] = Konstante.SCHNITTPUNKT_WEISS;
        spielSteine[4][4] = Konstante.SCHNITTPUNKT_WEISS;
        spielSteine[5][5] = Konstante.SCHNITTPUNKT_WEISS;
        */

        // horizontales Gitter zeichnen
        for(int i=0; i<x; i++)
        {
            g.drawLine(xOffset , yOffset + feldHoehe*i, xOffset + feldBreite*(spielfeldGroesse-1) , yOffset + feldHoehe*i);
        }

        // Zeichne die vertikalen Linien
        for(int i=0; i<y; i++)
        {
            g.drawLine(xOffset + feldBreite*i, yOffset, xOffset + feldBreite*i , yOffset + feldHoehe*(spielfeldGroesse-1));
        }
         

        
      // Zellen ausfüllen, bei denen das Array den Inhalt "1" hat
        if( this.spielSteine != null){
            for(int k = 0; k < x; k++)
            {
                for(int l = 0; l < y; l++)
                {

                    switch (this.spielSteine[k][l]){

                        case Konstante.SCHNITTPUNKT_LEER:
                            break;
                        case Konstante.SCHNITTPUNKT_SCHWARZ:
                            g.setColor(Color.BLACK);
                            g.fillOval(feldBreite*k, feldHoehe*l, feldBreite, feldHoehe);
                            //g.fillRect(0+k*wert,0+l*wert,wert,wert);
                            break;
                        case Konstante.SCHNITTPUNKT_WEISS:
                            g.setColor(Color.BLACK);
                            g.drawOval(feldBreite*k, feldHoehe*l, feldBreite, feldHoehe);
                            break;
                        case Konstante.SCHNITTPUNKT_VERBOTEN:
                            g.setColor(Color.BLACK);
                            g.drawRect(feldBreite*k-feldBreite/2, feldHoehe*l-feldHoehe/2, feldBreite/2, feldHoehe/2);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }



    public void setBrettOberflaeche(int[][] spielfeld, int spielfeldGroesse) {

        this.spielSteine        = spielfeld;
        this.spielfeldGroesse   = spielfeldGroesse;
        this.feldBreite         = this.spielfeldBreite/spielfeldGroesse;
        this.feldHoehe          = this.spielfeldHoehe/spielfeldGroesse;
        
        // Feld neu zeichnen
        this.repaint();
        
        throw new UnsupportedOperationException("Not filly supported yet.");
    }

    public void updateGUITimerWeiss(long periodenZeitInMS, long spielerZeitInMS) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateGUITimerSchwarz(long periodenZeitInMS, long spielerZeitInMS) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setSpielernameWeiss(String spielername) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setSpielernameSchwarz(String spielername) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setGefangeneSteineWeiss(int anzGefangenerSteiner) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setGefangeneSteineSchwarz(int anzGefangenerSteiner) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setSchwarzAmZug() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setWeissAmZug() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseClicked(MouseEvent e) {

        int xKlick = e.getX();
        int yKlick = e.getY();

        if( xKlick > 0 && xKlick < this.spielfeldBreite  && yKlick > 0 && yKlick < this.spielfeldHoehe){
            // Klick ist im Wertebereich

            //Berechne den angeklickten Schnittpunkt
            /*Einfache Version vorerst ... (+1, weil wir unser SPielfeld im Berech
                1 bis spielfeldGroesse deffiniert haben ...)
            */
            int xPos = xKlick/this.feldBreite + 1;
            int yPos = yKlick/this.feldHoehe + 1;

            LoGoApp.meineSteuerung.klickAufFeld(xPos, yPos);
        }


        throw new UnsupportedOperationException("Not fully supported yet.");
    }

    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void keyPressed(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
