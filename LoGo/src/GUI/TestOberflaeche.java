/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import interfaces.OberflaecheInterface;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import logo.LoGoApp;


/**
 *
 * @author steven
 * @version 0.2
 */
public class TestOberflaeche extends CoreWindow implements KeyListener, OberflaecheInterface, MouseListener{

    private Spielbrett testbrett;

    String mess = "";

    public void init(){
        super.init();

        this.testbrett = new Spielbrett(495, 495, 50, 20, 9, null);

        Window w = s.getFullScreenWindow();
        // Alle Spezialbuttons wie TAB, werden wie normale Keys behandelt
        w.setFocusTraversalKeysEnabled(false);
        w.addKeyListener(this);
        
        
    }

   /*
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

        try{
            testbrett.
        }
    }
            */


    @Override
    public synchronized void draw(Graphics2D g) {
        Window w = s.getFullScreenWindow();
        g.setColor(w.getBackground());
        g.fillRect(0, 0, s.getWidth(), s.getHeight());
        g.setColor(w.getForeground());
        g.drawString(mess, 30, 30);

        this.testbrett.drawObjects(g);

    }

    @Override
    public void update( long timePassed ){
        this.testbrett.doLogic(timePassed);
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
        this.testbrett.updateSpielFeld(spielfeld);
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
    }

    public void mouseClicked(MouseEvent e) {
        Point returnWert = this.testbrett.berechneTreffer(e.getX(), e.getY());
        if( returnWert != null){
            LoGoApp.meineSteuerung.klickAufFeld(returnWert.x, returnWert.y);
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
