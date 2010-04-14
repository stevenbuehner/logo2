package GUI;

import interfaces.Drawable;
import interfaces.Movable;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class SpielStein extends Rectangle2D.Double implements Movable, Drawable {
	long			delay;						// Bewegungsgeschwindigkeit
	protected boolean	visible			= true;

	protected Shape		klickFlaeche            = null;

	protected double	dx;
	protected double	dy;

	protected int		currentpic		= 0;
	protected long		animation		= 0;
	protected int		loop_from		= 0;
	protected int		loop_to			= 0;

        // Zum merken, wo die urspruengliche Startposition war.
        // Beim Aufruf von Reset, kann die Position hierauf zurueckgesetzt werden
	protected double	startPositionX;
	protected double	startPositionY;

        // Für Bewegungen
	protected double	endPositionX;
	protected double	endPositionY;

        // Die Grafiken die anzuzeigen sind
        protected BufferedImage[]	pics;

        // Transformation und Rotationsvariablen
	protected int                   angle;
	protected int			rotationx;
	protected int			rotationy;
	protected AffineTransform       at;



        // Objekt wird demnaechst geloescht
	protected boolean	remove			= false;

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
        public SpielStein(BufferedImage[] i, double x, double y, long delay) {
                this.startPositionX = x;
		this.startPositionY = y;
		this.delay = delay;

                // Grafiken zuweisen
		pics = i;

                // Position des Rechtecks
		this.x = x;
		this.y = y;

		this.width = pics[0].getWidth();
		this.height = pics[0].getHeight();
		loop_from = 0;
		loop_to = pics.length - 1;
		at = new AffineTransform();
		rotationx = (int) (width / 2);
		rotationy = (int) (height / 2);
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
                this.startPositionX = x;
		this.startPositionY = y;
		this.delay = delay;

                // Grafik zuweisen
		pics = new BufferedImage[1];
		pics[0] = i;

                // Position des Rechtecks zuweisen
		this.x = x;
		this.y = y;
                
		this.width = pics[0].getWidth();
		this.height = pics[0].getHeight();
		loop_from = 0;
		loop_to = pics.length - 1;
		at = new AffineTransform();
		rotationx = (int) (width / 2);
		rotationy = (int) (height / 2);
	}


	public void doLogic(long delta) {

		if ( delay == -1 )
			return;

		animation += (delta / 1000000);
		if ( animation > delay ) {
			animation = 0;
			computeAnimation();
		}
	}

	public void move(long delta) {

		if ( dx != 0 ) {
			if ( (dx > 0 && endPositionX > x) || (dx < 0 && endPositionX < x) ) {
				x += dx * (delta / 1e9); // Nur bewegen, wenn es nicht schon an
				// der Endposition ist
			}
			else {
				x = endPositionX;
			}
		}

		if ( dy != 0 ) {
			if ( (dy > 0 && endPositionY > y) || (dy < 0 && endPositionY < y) ) {
				y += dy * (delta / 1e9); // Nur bewegen, wenn es nicht schon an
				// der Endposition ist
			}
			else {
				y = endPositionY;
			}
		}
	}

	public void drawObjects(Graphics g) {
		if ( !visible )
			return;

		Graphics2D g2 = (Graphics2D) g;
		if ( angle != 0 ) {
			at.rotate( Math.toRadians( angle ), x + rotationx, y + rotationy );
			g2.setTransform( at );
			g2.drawImage( pics[currentpic], (int) x, (int) y, null );
			at.rotate( -Math.toRadians( angle ), x + rotationx, y + rotationy );
			g2.setTransform( at );
		}
		else {
			g.drawImage( pics[currentpic], (int) x, (int) y, null );
		}
	}

	/**
	 * Format the intro text.
	 *
	 * @param text
	 *        The intro text.
	 * @return The lines of the formatted intro text as an array.
	 */
	public static Vector<String> wrapText(String text, int width, FontMetrics fm) {

		Vector<String> zerstueckelterText = new Vector<String>();

		int zeigerAnf = 0;
		int zeigerEnd = 1;

		String derString = text.trim(); // Leerzeichen entfernen
		String substring;

		if ( fm.stringWidth( derString ) <= width ) {
			// Der haeufigste Fall ist, dass gar nicht umgebrochen werden muss.
			// Den Fangen wir gleich zu Begin ab:
			zerstueckelterText.add( derString.trim() );
			return zerstueckelterText;
		}

		// Ab hier die normale Zerlegung anhand und Unterteilung immer bei den
		// Leerzeichen
		while (zeigerEnd < derString.length()) {

			substring = derString.substring( zeigerAnf, zeigerEnd );
			while (fm.stringWidth( substring.trim() ) <= width && zeigerEnd < text.length()) {
				zeigerEnd++;
				substring = derString.substring( zeigerAnf, zeigerEnd );
			}

			if ( zeigerEnd <= text.length() && fm.stringWidth( substring.trim() ) <= width ) {
				// Wenn der Text kleiner als Width ist gib ihn gleich
				// zurück.
				zerstueckelterText.add( substring.trim() );
				return zerstueckelterText;
			}
			else {
				// Wenn der Text noch weiter geht, dann muss noch nach
				// Leerzeichen gesucht werden
				int letztesLeerzeichen = substring.lastIndexOf( ' ' );
				if ( letztesLeerzeichen == -1 ) {
					// Kein Leerzeichen, dann wird der Text halt hart gebrochen

				}
				else {
					zeigerEnd = letztesLeerzeichen + zeigerAnf; // Da zeigerEnd
					// in Bezug auf
					// den Substring
					// ist!!!
					substring = derString.substring( zeigerAnf, zeigerEnd );
					zeigerAnf = zeigerEnd;
				}
			}
			zerstueckelterText.add( substring.trim() );

		}
		return zerstueckelterText;
	}

	protected void paintShape(Graphics g) {
		// Nur zum debuggen
		if ( klickFlaeche == null )
			return;

		g.setColor( Color.black );

		if ( klickFlaeche instanceof Polygon ) {
			Polygon zeichenPoly = new Polygon();
			for (int i = 0; i < ((Polygon) this.klickFlaeche).npoints; i++) {
				zeichenPoly.addPoint( (int) (((Polygon) this.klickFlaeche).xpoints[i] + this.x),
						(int) (((Polygon) this.klickFlaeche).ypoints[i] + this.y) );
				g.fillPolygon( zeichenPoly );
			}
		}
		else if ( klickFlaeche instanceof Rectangle2D ) {
			// g.fillRect (  (int) ((Rectangle2D) klickFlaeche).getX(),
			// ((Rectangle2D) klickFlaeche).getY(), ((Rectangle2D)
			// klickFlaeche).getWidth(), ((Rectangle2D)
			// klickFlaeche).getHeight() );
		}
	}

	public boolean feldAngeklickt(int xKlick, int yKlick) {

		if ( this.klickFlaeche == null ) {
			return false;
		}

		// Funktioniert beim Logo nicht
		return this.klickFlaeche.contains( xKlick - this.x, yKlick - this.y );
	}

	protected void computeAnimation() {

		currentpic++;

		if ( currentpic > loop_to ) {
			currentpic = loop_from;
		}
	}

	public void setLoop(int from, int to) {
		loop_from = from;
		loop_to = to;
		currentpic = from;
	}

	public void resetAnimation() {
		this.x = startPositionX;
		this.y = startPositionY;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean sichtbar) {
		this.visible = sichtbar;
	}

	public void setVerticalSpeed(double d) {
		dy = d;
	}

	public void setHorizontalSpeed(double d) {
		dx = d;
	}

	public double getVerticalSpeed() {
		return dy;
	}

	public double getHorizontalSpeed() {
		return dx;
	}

	public void setX(double i) {
		x = i;
	}

	public void setY(double i) {
		y = i;
	}

        public void setAngle(int a) {
		angle = a;
	}

	public int getAngle() {
		return angle;
	}

	public Point getRotation() {
		return (new Point( rotationx, rotationy ));
	}


        public boolean checkOpaqueColorCollisions(SpielStein s) {

		Rectangle2D.Double cut = (Double) this.createIntersection( s );
		if ( (cut.width < 1) || (cut.height < 1) ) {
			return false;
		}

		// Rechtecke in Bezug auf die jeweiligen Images
		Rectangle2D.Double sub_me = getSubRec( this, cut );
		Rectangle2D.Double sub_him = getSubRec( s, cut );

		BufferedImage img_me = pics[currentpic].getSubimage( (int) sub_me.x, (int) sub_me.y,
				(int) sub_me.width, (int) sub_me.height );
		BufferedImage img_him = s.pics[s.currentpic].getSubimage( (int) sub_him.x, (int) sub_him.y,
				(int) sub_him.width, (int) sub_him.height );

		for (int i = 0; i < img_me.getWidth(); i++) {
			for (int n = 0; n < img_him.getHeight(); n++) {

				int rgb1 = img_me.getRGB( i, n );
				int rgb2 = img_him.getRGB( i, n );

				if ( isOpaque( rgb1 ) && isOpaque( rgb2 ) ) {
					return true;
				}
			}
		}
		return false;
	}

	protected Rectangle2D.Double getSubRec(Rectangle2D.Double source, Rectangle2D.Double part) {

		// Rechtecke erzeugen
		Rectangle2D.Double sub = new Rectangle2D.Double();

		// get X - compared to the Rectangle
		if ( source.x > part.x ) {
			sub.x = 0;
		}
		else {
			sub.x = part.x - source.x;
		}

		if ( source.y > part.y ) {
			sub.y = 0;
		}
		else {
			sub.y = part.y - source.y;
		}

		sub.width = part.width;
		sub.height = part.height;

		return sub;
	}

	protected boolean isOpaque(int rgb) {

		int alpha = (rgb >> 24) & 0xff;

		if ( alpha == 0 ) {
			return false;
		}
		return true;
	}

}
