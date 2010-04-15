/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import interfaces.OberflaecheInterface;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import logo.LoGoApp;


/**
 *
 * @author steven
 * @version 0.2
 */
public class TestOberflaeche extends JFrame implements Runnable, KeyListener, OberflaecheInterface, MouseListener{

    private Spielbrett dasBrett;
    private boolean threadLaeuf;
    private static boolean once = false;

    String mess = "";

    public TestOberflaeche( String pFenstername){
        super( pFenstername );

        init();
        
        this.start();
    }


    public void init(){

        this.dasBrett = new Spielbrett(495, 495, 40, 40, 9, null);
        threadLaeuf = true;

        // Alle Spezialbuttons wie TAB, werden wie normale Keys behandelt
        this.setFocusTraversalKeysEnabled(false);
        this.addKeyListener(this);
        this.addMouseListener(this);

        this.setVisible(true);
        this.setResizable(false);
        this.setBackground(Color.GREEN);
        this.setBounds(50, 50, 800, 600);


        // Programm bei klick auf den roten Knopf beenden
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) { System.exit(0);
        } });
        
    }

    public void run() {

        while(this.threadLaeuf){
            this.repaint();
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                Logger.getLogger(TestOberflaeche.class.getName()).log(Level.SEVERE, null, ex);
            }finally{

            }
        }

    }

    @Override
    public void paint( Graphics g){
        super.paint(g);

        g.setColor(this.getBackground());
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        this.dasBrett.drawObjects(g);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 20 ));
        g.drawString("MESS: "+mess, 30, 50);

    }


    public void start(){
        		// Thread anstoßen
        if ( !once ) {
            once = true;
            Thread t = new Thread( this );
            t.start();
	}
    }

    public void stop(){
        this.threadLaeuf = false;
    }



    public void update( long timePassed ){
        this.dasBrett.doLogic(timePassed);
    }

    public void keyTyped(KeyEvent e) {
        e.consume();
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_ESCAPE){
            this.stop();
        }
        else if(keyCode == KeyEvent.VK_N){
            // neues Spiel
            LoGoApp.meineSteuerung.buttonSpielStarten();
            mess = "Steuerung => Spiel starten";
        }
        else{
            mess = "Pressed: " + KeyEvent.getKeyText(keyCode);
            e.consume(); // Kombinierte Tasten sollen nicht behandlet werden.
        }
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        mess = "Released: " + KeyEvent.getKeyText(keyCode);
        e.consume();
    }

    public void setBrettOberflaeche(int[][] spielfeld, int spielfeldGroesse) {
        // ACHTUNG: Änderun der Spielfeldgroesse wird hier nciht abgefangen!
        this.dasBrett.updateSpielFeld(spielfeld);
    }

    public void setAnzeigePeriodenZeitWeiss(long periodenZeitInMS) {
    }

    public void setAnzeigePeriodenZeitSchwarz(long periodenZeitInMS) {
    }

    public void setAnzeigeSpielerZeitWeiss(long spielerZeitInMS) {
    }

    public void setAnzeigeSpielerZeitSchwarz(long spielerZeitInMS) {
    }

    public void setSpielernameWeiss(String spielername) {
    }

    public void setSpielernameSchwarz(String spielername) {
    }

    public void setGefangeneSteineWeiss(int anzGefangenerSteiner) {
    }

    public void setGefangeneSteineSchwarz(int anzGefangenerSteiner) {
    }

    public void setSchwarzAmZug() {
    }

    public void setWeissAmZug() {
    }

    public void gibFehlermeldungAus(String fehlertext) {
        System.out.println(fehlertext);
    }

    public void mouseClicked(MouseEvent e) {
        Point returnWert = this.dasBrett.berechneTreffer(e.getX(), e.getY());
        if( returnWert != null){
            LoGoApp.meineSteuerung.klickAufFeld(returnWert.x, returnWert.y);
            mess = "klick auf " + returnWert.x + " | " + returnWert.y;
        }else{
            mess = "kein Treffer mit Clicked-Koordinaten: " + e.getX() + " | " + e.getY();
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }


}
