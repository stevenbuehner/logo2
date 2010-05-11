package GUI;

import Interfaces.Drawable;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Diese Klasse beinhaltet die Grafik(en) und die Animationsvorgänge
 * eines Spielsteines auf dem Spielbrett.
 * @author steven
 * @version 0.4
 */
public class SpielStein extends Rectangle2D.Double implements Drawable {

    // Informationen uber den Zustand der SpielSteinAnimation
    /**
     * Spielstein sichtbar oder nicht
     */
    protected boolean visible;
    /**
     * Aktuelle Grafikindex bei einer Animations mit einem Grafik-Array
     */
    protected int currentSceneIndex;
    /**
     * Zeit, die die aktuelle Szene schon dargestellt wird bei einer Animation
     */
    protected long thisScenePlayedTime;         // Zeit die die aktuelle Scene schon "verbraten" hat
    /**
     * Soll die Animation wiederholt werden?
     */
    protected boolean loop;
    /**
     * Wenn die Animation wiederholt werden soll, ab welchem Index?
     * Bei negativen Werten wird nicht wiederholt.
     */
    protected int loopFromIndex = -2;
    private ArrayList<OneScene> szenen;
    // Die Grafiken die anzuzeigen sind
    /**
     * Array mit den Grafiken. Wenn nur eine Grafik, dann ist es ein Array
     * mit Size = 1
     */
    protected BufferedImage[] storedImages;
    // Objekt wird demnaechst geloescht
    /**
     * Wenn der Stein gelöscht werden soll, dann wird dies hier markiert.
     * Da es unter umständen vorkommen kann, dass ein Stein zwar gelöscht werden
     * soll, aber noch von einer anderen Funktion gebraucht wird.
     */
    protected boolean steinWirdGeloescht;
    /**
     * Offsetposition vom linken Spielfeldrand gemessen am Mittelpunkt der Grafik.
     */
    protected int offsetLeft;
    /**
     * Offsetposition vom oberen Spielfeldrand gemessen am Mittelpunkt der Grafik.
     */
    protected int offsetTop;

    /**
     *
     * Standardkonstruktor, bei dem gleich mehrere einzelne Bilder als Array
     * (= spaetere Animation) uebergeben werden.
     * @param image
     * @param x X-Startposition der Animation (linke obere Ecke als Ausgangspunkt)
     * @param y Y-Startposition der Animation (linke obere Ecke als Ausgangspunkt)
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

            this.thisScenePlayedTime += timePassed/1000000;
            if (this.thisScenePlayedTime > szenen.get(this.currentSceneIndex).getSceneDuration()) {

                /*
                 * Wenn die Zeit fuer die aktuelle Scene schon verbraucht ist,
                 * berechne die Zeit, die die Scene der naechsten Animation
                 * "geklaut" hat. Ob der darauffolgenden Szene auch schon die Zeit
                 * "gekaut" wurde wird hier bewusst nicht mit abgefragt.
                 */
                this.thisScenePlayedTime = 0;
                this.computeAnimation();
            }
        }
    }

    /**
     * Zeichnet die Grfik auf das Spielbrett
     * @param g Graphicfenster
     */
    @Override
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
                this.currentSceneIndex = this.loopFromIndex;
            }
        } else {
            // Animationzaehler um eins erhoehen
            this.currentSceneIndex++;
        }
    }

    /**
     * Legt fest, ob die Animation sich wiederholen soll
     * @param loopAktiviert Ob die Animation wiederholt werden soll
     */
    public void setLoop(boolean loopAktiviert) {
        this.loop = loopAktiviert;
    }

    /**
     * Legt fest, ob die Animation sich wiederholen soll und wenn ja ab welchem Szenenindex.
     * @param loopAktiviert Ob die Animation wiederholt werden soll
     * @param loopFromIndex Wenn loop = true, dann ab welchem Index.
     */
    public void setLoop(boolean loopAktiviert, int loopFromIndex) {
        this.loop = loopAktiviert;
        this.loopFromIndex = loopFromIndex;
    }

    /**
     *
     * @return Ob das Objekt gerade sichtbar ist
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     *
     * @param sichtbar Ob das Objekt gerade sichtbar sein soll (true = sichtbar)
     */
    public void setVisible(boolean sichtbar) {
        this.visible = sichtbar;
    }

    /**
     *
     * @param i Setzen der X-Position. Abstand vom Zentrum der Spielgrafik zum linken Fensterrand
     */
    public void setX(double i) {
        x = i;
    }

    /**
     *
     * @param i Setzen der Y-Position. Abstand vom Zentrum der Spielgrafik zum oberen Fensterrand
     */
    public void setY(double i) {
        y = i;
    }

    /**
     *
     * @param image Grafik, die mit der Länge des zweiten Parameters angezeigt werden soll
     * @param sceneDuration Anzeigedauer der Grafik
     */
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
     * @param sceneIndex Gewünschter Szenenindex
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

    /**
     * Alle vorhandenen Szenen löschen und damit verbundene Parameter zurücksetzen
     */
    protected synchronized void clearScenes() {
        this.restartAnimation();
        if (this.szenen.size() > 0) {
            this.szenen.clear();
        }
        this.setLoop(false);
        this.loopFromIndex = 0; // negativer Wert
    }

    /**
     * Die Animation eines gelegten weissen Spielsteines starten.
     */
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

    /**
     * Die Animation eines gelegten schwarzen Spielsteines starten.
     */
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

    /**
     * Die Animation eines zu entfernenden weissen Spielsteines starten.
     */
    public synchronized void starteAnimationWeissEntfernen() {
        this.clearScenes();
        this.addScene(storedImages[29], 100);
        this.addScene(storedImages[28], 100);
        this.addScene(storedImages[27], 100);
        //this.addScene(storedImages[26], 100);
        this.addScene(storedImages[25], 100);
        this.addScene(storedImages[24], 200);
        this.addScene(storedImages[23], 300);
        //this.addScene(storedImages[22], 100);
        this.addScene(storedImages[21], 300); // muss da bleiben!!
        this.setLoop(false);
    }

    /**
     * Die Animation eines zu entfernenden schwarzen Spielsteines starten.
     */
    public synchronized void starteAnimationSchwarzEntfernen() {
        this.clearScenes();
        //this.addScene(storedImages[8], 100);
        this.addScene(storedImages[9], 100);
        this.addScene(storedImages[10], 100);
        this.addScene(storedImages[11], 100);
        //this.addScene(storedImages[12], 100);
        this.addScene(storedImages[13], 100);
        //this.addScene(storedImages[14], 100);
        this.addScene(storedImages[15], 200);
        //this.addScene(storedImages[16], 100);
        this.addScene(storedImages[17], 300);
        //this.addScene(storedImages[18], 100);
        //this.addScene(storedImages[19], 100);
        //this.addScene(storedImages[20], 100);
        this.addScene(storedImages[21], 300); // muss da bleiben!!
        this.setLoop(false);
    }

    /**
     * Die Animation, dass dieses Feld jetzt ein verbotener Zug für Weiss
     * ist, soll gestartet werden.
     */
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

    /**
     * Die Animation, dass dieses Feld jetzt ein verbotener Zug für Schwarz
     * ist, soll gestartet werden.
     */
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

    /**
     * Ein verbotenes Feld soll jetzt wieder normal dargestellt werden. Dies
     * soll animiert werden.
     */
    public synchronized void starteAnimationVerbotenerZugAufheben() {
        this.clearScenes();
        this.addScene(storedImages[21], 100);
        this.setLoop(false);
    }

    /**
     * Ein Feld soll als verboten markiert sein, ohne Animation.
     */
    public synchronized void setVerbotenerZug() {
        this.clearScenes();
        this.addScene(storedImages[37], 100);
        this.setLoop(false);
    }

    /**
     * Die Animation, dass ein Spielstein von Schwarz durch Weiss gefangen wurde, starten
     */
    public synchronized void starteAnimationGefangenenSteinSchwarz() {
        this.clearScenes();
        this.addScene(storedImages[39], 100);
        this.setLoop(false);
    }

    /**
     * Die Animation, dass ein Spielstein von Weiss durch Schwarz gefangen wurde, starten
     */
    public synchronized void starteAnimationGefangenenSteinWeiss() {
        this.clearScenes();
        this.addScene(storedImages[40], 100);
        this.setLoop(false);
    }

    /**
     * Die Animation, dass ein Gebietspunkt Schwarz zugerechnet wird.
     */
    public synchronized void starteAnimationGebietspunktSchwarz() {
        this.clearScenes();
        this.addScene(storedImages[41], 100);
        this.setLoop(false);
    }

    /**
     * Die Animation, dass ein Gebietspunkt Weiss zugerechnet wird.
     */
    public synchronized void starteAnimationGebietspunktWeiss() {
        this.clearScenes();
        this.addScene(storedImages[42], 100);
        this.setLoop(false);
    }

    /**
     * Die Animation, dass ein Gebietspunkt Schwarz zugerechnet wurde und jetzt
     * wieder niemandem zugerechnet wird.
     */
    public synchronized void starteAnimationGebietSchwarzZuLeer() {
        this.clearScenes();
        this.addScene(storedImages[21], 100);
        this.setLoop(false);
    }

    /**
     * Die Animation, dass ein Gebietspunkt Weiss zugerechnet wurde und jetzt
     * wieder niemandem zugerechnet wird.     */
    public synchronized void starteAnimantionGebietWeissZuLeer() {
        this.clearScenes();
        this.addScene(storedImages[21], 100);
        this.setLoop(false);
    }

    /**
     * Ein schwarzer Stein soll ohne Animation angezeigt werden.
     */
    public synchronized void setSchwarzerStein() {
        this.clearScenes();
        this.addScene(storedImages[9], 100);
        this.setLoop(false);
    }

    /**
     * Ein weisser Stein soll ohne Animation angezeigt werden.
     */
    public synchronized void setWeisserStein() {
        this.clearScenes();
        this.addScene(storedImages[30], 100);
        this.setLoop(false);
    }

    /**
     * Ein leeres Feld soll ohne Animation angezeigt werden.
     */
    public synchronized void setLeeresFeld() {
        this.clearScenes();
        this.addScene(storedImages[21], 100);
        this.setLoop(false);
    }
}
