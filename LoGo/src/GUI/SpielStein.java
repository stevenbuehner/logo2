package GUI;

import Interfaces.Drawable;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SpielStein extends Rectangle2D.Double implements Drawable {

    // Informationen uber den Zustand der SpielSteinAnimation
    protected boolean visible;
    protected int currentSceneIndex;
    protected long thisScenePlayedTime;         // Zeit die die aktuelle Scene schon "verbraten" hat
    protected boolean loop;
    private ArrayList<OneScene> szenen;
    // Die Grafiken die anzuzeigen sind
    protected BufferedImage[] storedImages;
    // Objekt wird demnaechst geloescht
    protected boolean steinWirdGeloescht;
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
    public SpielStein(BufferedImage[] image, double x, double y) {

        // Grafiken zuweisen
        storedImages = image;

        // Position des Rechtecks
        this.x = x;
        this.y = y;

        this.offsetLeft = storedImages[0].getWidth() / 2;
        this.offsetTop = storedImages[0].getHeight() / 2;

        // Standardwerte setzen
        this.width = storedImages[0].getWidth();
        this.height = storedImages[0].getHeight();
        this.visible = true;
        this.steinWirdGeloescht = false;
        this.loop = false;
        this.thisScenePlayedTime = 0;

        this.szenen = new ArrayList<OneScene>();
        this.starteAnimationVerbotenerZugAufheben();
    }

    /**
     *
     * Standardkonstruktor, bei dem nur ein Bild uebergeben wird. Es wird
     * also spaeter nicht animiert
     * @param image Ein Array mit den Bildern die das Objekt verarbeiten soll
     * @param x X-Startposition der Animation (linke obere Ecke als Ausgangspunkt)
     * @param y Y-Startposition der Animation (linke obere Ecke als Ausgangspunkt)
     * @param delay Geschwindigkeit, mit der die Animationsbilder rotieren sollen
     *
     */
    public SpielStein(BufferedImage image, double x, double y) {

        // Grafiken zuweisen
        storedImages = new BufferedImage[1];
        storedImages[0] = image;

        // Position des Rechtecks
        this.x = x;
        this.y = y;

        this.offsetLeft = storedImages[0].getWidth() / 2;
        this.offsetTop = storedImages[0].getHeight() / 2;

        // Standardwerte setzen
        this.width = storedImages[0].getWidth();
        this.height = storedImages[0].getHeight();
        this.starteAnimationVerbotenerZugAufheben();
        this.visible = true;
        this.steinWirdGeloescht = false;
        this.loop = false;
        this.thisScenePlayedTime = 0;

        this.szenen = new ArrayList<OneScene>();
        this.starteAnimationVerbotenerZugAufheben();
    }

    /**
     * Wird vor dem Aufruf von drawObjects aufgerufen und stellt die  Grafiken
     * der Animation so ein, wie es nach der Zeit @param delta zu sein hat.
     * @param timePassed
     */
    public synchronized void doLogic(long timePassed) {

        if (this.szenen.size() > 1) {
            // Nur wenn auch mehr als eine Szene besteht muss das Bild geaendert werden

            this.thisScenePlayedTime += timePassed;
            if (this.thisScenePlayedTime > szenen.get(this.currentSceneIndex).getSceneDuration()) {

                /*
                 * Wenn die Zeit fuer die aktuelle Scene schon verbraucht ist,
                 * berechne die Zeit, die die Scene der naechsten Animation
                 * "geklaut" hat. Ob der darauffolgenden Szene auch schon die Zeit
                 * "gekaut" wurde wird hier bewusst nicht mit abgefragt.
                 */
                this.thisScenePlayedTime = this.thisScenePlayedTime - szenen.get(this.currentSceneIndex).getSceneDuration();
                this.computeAnimation();
            }
        }
    }

    public void drawObjects(Graphics g) {
        if (!visible || szenen == null) {
            return;
        }

        g.drawImage(getImage(),
                (int) x - this.offsetLeft,
                (int) y - this.offsetTop, null);
    }

    /**
     * Setzt die interen Variable currentpic auf das naechste anzuzeigende Bild.
     * Abhaengig von den Variablen loop_from und loop_to, welche selbstbeschreibend sind.
     */
    protected void computeAnimation() {

        // Schon bei der letzten Szene der Animation angekommen??
        if (this.szenen.size() == (this.currentSceneIndex + 1)) {
            if (!this.loop) {
                // Wenn die Animation nicht wiederholt werden soll, bleib auf
                // diesem Index stehen
                return;
            } else {
                // Wiederhole
                this.currentSceneIndex = 0;
                this.thisScenePlayedTime = 0;
            }
        } else {
            // Animationzaehler um eins erhoehen
            this.currentSceneIndex++;
        }
    }

    /**
     * Legt fest, ob die Animation sich wiederholen soll
     */
    public void setLoop(boolean loopAktiviert) {
        this.loop = loopAktiviert;
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

    protected synchronized void addScene(BufferedImage image, long sceneDuration) {
        this.szenen.add(new OneScene(image, sceneDuration));
    }

    /**
     *
     * @return Das aktuelle Image der Animation
     */
    protected synchronized BufferedImage getImage() {
        if (szenen.size() == 0) {
            return null;
        } else {
            return getScene(this.currentSceneIndex).getPic();
        }
    }

    /**
     *
     * @param sceneIndex
     * @return Gibt Szene am sceneIndex zurueck
     */
    protected OneScene getScene(int sceneIndex) {
        return szenen.get(sceneIndex);
    }

    /**
     * Animationseinstellungen auf die erste Szene setzen (init)
     */
    public synchronized void restartAnimation() {
        this.currentSceneIndex = 0;
        this.thisScenePlayedTime = 0;
    }

    protected synchronized void clearScenes() {
        this.restartAnimation();
        if (this.szenen.size() > 0) {
            this.szenen.clear();
        }
    }

    public synchronized void starteAnimationWeissSetzen() {
        this.clearScenes();
        //this.addScene(storedImages[36], 100);
        // this.addScene(storedImages[35], 100);
        this.addScene(storedImages[34], 100);
        //this.addScene(storedImages[33], 100);
        //this.addScene(storedImages[32], 100);
        this.addScene(storedImages[31], 100);
        this.addScene(storedImages[30], 100);
        this.setLoop(false);
    }

    public synchronized void starteAnimationSchwarzSetzen() {
        this.clearScenes();
        // this.addScene(storedImages[0], 100);
        // this.addScene(storedImages[1], 100);
        // this.addScene(storedImages[2], 100);
        // this.addScene(storedImages[3], 100);
        // this.addScene(storedImages[4], 100);
        this.addScene(storedImages[5], 100);
        //this.addScene(storedImages[6], 100);
        //this.addScene(storedImages[7], 100);
        this.addScene(storedImages[8], 100);
        this.addScene(storedImages[9], 100);
        this.setLoop(false);
    }

    public synchronized void starteAnimationWeissEntfernen() {
        this.clearScenes();
        this.addScene(storedImages[29], 100);
        this.addScene(storedImages[28], 100);
        this.addScene(storedImages[27], 100);
        //this.addScene(storedImages[26], 100);
        this.addScene(storedImages[25], 100);
        this.addScene(storedImages[24], 100);
        this.addScene(storedImages[23], 100);
        //this.addScene(storedImages[22], 100);
        this.addScene(storedImages[21], 100); // muss da bleiben!!
        this.setLoop(false);
    }

    public synchronized void starteAnimationSchwarzEntfernen() {
        this.clearScenes();
        //this.addScene(storedImages[8], 100);
        this.addScene(storedImages[9], 100);
        this.addScene(storedImages[10], 100);
        this.addScene(storedImages[11], 100);
        //this.addScene(storedImages[12], 100);
        this.addScene(storedImages[13], 100);
        //this.addScene(storedImages[14], 100);
        this.addScene(storedImages[15], 100);
        //this.addScene(storedImages[16], 100);
        this.addScene(storedImages[17], 100);
        //this.addScene(storedImages[18], 100);
        //this.addScene(storedImages[19], 100);
        //this.addScene(storedImages[20], 100);
        this.addScene(storedImages[21], 100); // muss da bleiben!!
        this.setLoop(false);
    }

    public synchronized void starteAnimationVerbotenerZugWeiss() {
        this.clearScenes();
        this.addScene(storedImages[29], 100);
        this.addScene(storedImages[28], 100);
        this.addScene(storedImages[27], 100);
        //this.addScene(storedImages[26], 100);
        this.addScene(storedImages[25], 100);
        this.addScene(storedImages[24], 100);
        this.addScene(storedImages[23], 100);
        //this.addScene(storedImages[22], 100);
        // this.addScene(storedImages[21], 100); // leeres Bild

        this.addScene(storedImages[37], 100);
        this.setLoop(false);
    }

    public synchronized void starteAnimationVerbotenerZugSchwarz() {
        this.clearScenes();
        //this.addScene(storedImages[8], 100);
        this.addScene(storedImages[9], 100);
        this.addScene(storedImages[10], 100);
        this.addScene(storedImages[11], 100);
        //this.addScene(storedImages[12], 100);
        this.addScene(storedImages[13], 100);
        //this.addScene(storedImages[14], 100);
        this.addScene(storedImages[15], 100);
        //this.addScene(storedImages[16], 100);
        this.addScene(storedImages[17], 100);
        //this.addScene(storedImages[18], 100);
        //this.addScene(storedImages[19], 100);
        //this.addScene(storedImages[20], 100);
        //this.addScene(storedImages[21], 100); // leeres Bild

        this.addScene(storedImages[37], 100);
        this.setLoop(false);
    }

    public synchronized void starteAnimationVerbotenerZugAufheben() {
        this.clearScenes();
        this.addScene(storedImages[21], 100);
        this.setLoop(false);
    }

    public synchronized void setVerbotenerZug() {
        this.clearScenes();
        this.addScene(storedImages[37], 100);
        this.setLoop(false);
    }

    public synchronized void starteAnimationGefangenenSteinSchwarz() {
        this.clearScenes();
        this.addScene(storedImages[39], 100);
        this.setLoop(false);
    }

    public synchronized void starteAnimationGefangenenSteinWeiss() {
        this.clearScenes();
        this.addScene(storedImages[40], 100);
        this.setLoop(false);
    }

    public synchronized void starteAnimationGebietspunktSchwarz() {
        this.clearScenes();
        this.addScene(storedImages[4], 100);
        this.setLoop(false);
    }

    public synchronized void starteAnimationGebietspunktWeiss() {
        this.clearScenes();
        this.addScene(storedImages[35], 100);
        this.setLoop(false);
    }

    public synchronized void starteAnimationGebietSchwarzZuLeer() {
        this.clearScenes();
        this.addScene(storedImages[21], 100);
        this.setLoop(false);
    }

    public synchronized void starteAnimantionGebietWeissZuLeer() {
        this.clearScenes();
        this.addScene(storedImages[21], 100);
        this.setLoop(false);
    }

    public synchronized void setSchwarzerStein() {
        this.clearScenes();
        this.addScene(storedImages[9], 100);
        this.setLoop(false);
    }

    public synchronized void setWeisserStein() {
        this.clearScenes();
        this.addScene(storedImages[30], 100);
        this.setLoop(false);
    }

    public synchronized void setLeeresFeld() {
        this.clearScenes();
        this.addScene(storedImages[21], 100);
        this.setLoop(false);
    }
}
