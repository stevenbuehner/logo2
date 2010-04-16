package GUI;

import interfaces.Drawable;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class SpielStein extends Rectangle2D.Double implements Drawable {

    long delay;						// Bewegungsgeschwindigkeit
    protected boolean visible;
    protected int currentpic;
    protected long animation;
    protected int loop_from;
    protected int loop_to;
    // Die Grafiken die anzuzeigen sind
    protected BufferedImage[] pics;
    // Objekt wird demnaechst geloescht
    protected boolean remove;

    protected int offsetLeft;
    protected int offsetTop;

    /**
     *
     * Standardkonstruktor, bei dem gleich mehrere einzelne Bilder als Array
     * (= spaetere Animation) uebergeben werden.
     * @param i Ein Array mit den Bildern die das Objekt verarbeiten soll
     * @param x X-Startposition der Animation (linke obere Ecke als Ausgangspunkt)
     * @param y Y-Startposition der Animation (linke obere Ecke als Ausgangspunkt)
     * @param delay Geschwindigkeit, mit der die Animationsbilder rotieren sollen
     *
     */
    public SpielStein(BufferedImage[] image, double x, double y, long delay) {
        this.delay = delay;

        // Grafiken zuweisen
        pics = image;

        // Position des Rechtecks
        this.x = x;
        this.y = y;

        this.offsetLeft = pics[0].getWidth()/2;
        this.offsetTop = pics[0].getHeight()/2;

        // Standardwerte setzen
        this.width = pics[0].getWidth();
        this.height = pics[0].getHeight();
        this.visible = true;
        this.remove = false;
        this.starteAnimationVerbotenerZugAufheben();
    }

    /**
     *
     * Standardkonstruktor, bei dem nur ein Bild uebergeben wird. Es wird
     * also spaeter nicht animiert
     * @param i Ein Array mit den Bildern die das Objekt verarbeiten soll
     * @param x X-Startposition der Animation (linke obere Ecke als Ausgangspunkt)
     * @param y Y-Startposition der Animation (linke obere Ecke als Ausgangspunkt)
     * @param delay Geschwindigkeit, mit der die Animationsbilder rotieren sollen
     *
     */
    public SpielStein(BufferedImage i, double x, double y, long delay) {
        this.delay = delay;

        // Grafiken zuweisen
        pics = new BufferedImage[1];
        pics[0] = i;

        // Position des Rechtecks
        this.x = x;
        this.y = y;

        this.offsetLeft = pics[0].getWidth()/2;
        this.offsetTop = pics[0].getHeight()/2;

        // Standardwerte setzen
        this.width = pics[0].getWidth();
        this.height = pics[0].getHeight();
        this.starteAnimationVerbotenerZugAufheben();
        this.visible = true;
        this.remove = false;
    }

    /**
     * Wird vor dem Aufruf von drawObjects aufgerufen und stellt die  Grafiken
     * der Animation so ein, wie es nach der Zeit @param delta zu sein hat.
     * @param delta
     */
    public synchronized void doLogic(long delta) {

        if (delay == -1) {
            return;
        }

        animation += (delta / 1000000);
        if (animation > delay) {
            animation = 0;
            computeAnimation();
        }
    }

    public void drawObjects(Graphics g) {
        if (!visible) {
            return;
        }

        g.drawImage(pics[currentpic], (int) x-this.offsetLeft, (int) y-this.offsetTop, null);
    }

    /**
     * Setzt die interen Variable currentpic auf das naechste anzuzeigende Bild.
     * Abhaengig von den Variablen loop_from und loop_to, welche selbstbeschreibend sind.
     */
    protected void computeAnimation() {

        if (this.loop_from < this.loop_to) {
            this.currentpic++;

            if (this.currentpic > this.loop_to) {
                this.currentpic = this.loop_to;
            }

        } else {
            this.currentpic--;

            if (this.currentpic < this.loop_to) {
                this.currentpic = this.loop_to;
            }
        }
    }

    /**
     * Setzt dem Bereich in dem animiert werden soll zwischen
     * @param from und @param to
     */
    public void setLoop(int from, int to) {
        loop_from = from;
        loop_to = to;
        currentpic = from;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean sichtbar) {
        this.visible = sichtbar;
    }

    public void setX(double i) {
        x = i;
    }

    public void setY(double i) {
        y = i;
    }

    public synchronized void starteAnimationWeissSetzen() {
        this.setLoop(36, 30);
    }

    public synchronized void starteAnimationSchwarzSetzen() {
        this.setLoop(0, 9);
    }

    public synchronized void starteAnimationWeissEntfernen() {
        this.setLoop(29, 21);
    }

    public synchronized void starteAnimationSchwarzEntfernen() {
        this.setLoop(8, 21);
    }

    public synchronized void starteAnimationVerbotenerZug() {
        this.setLoop(37, 37);
    }

    public synchronized void starteAnimationVerbotenerZugAufheben() {
        this.setLoop(21, 21);
    }
}
