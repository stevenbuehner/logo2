/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import Klassen.Steuerung;
import interfaces.oberflaecheInterface;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
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



    public void setBrettOberflaeche(int[][] spielfeld, int spielfeldGroesse) {
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
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
