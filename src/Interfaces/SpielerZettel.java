package Interfaces;

import java.awt.Graphics;

/**
 * Ein SpielerZettel stellt alle für einen Spieler relavanten Informationen
 * dar und dient dazu, einen Überblick über die wichtigsten Änderungen und
 * Eigenschaftesn über sein Spiel zu bekommen.
 *
 * Klasse bekommt im Konstruktor xPos, yPos linkeEcke oben und die Winkelabweichung
 * senkrecht zum Lot. Im Uhrzeigersinn positive Werte. Negative Werte sind gegen
 * den Uhrzeigersinn.
 *
 * @author steven
 * @version 0.1
 */
public interface SpielerZettel {

    /**
     * Setzen des Spielernamens. Der Spielername enthält dabei
     * den Namen, den der Spieler sich als Spielernamen gegeben hat.
     * @param spielername Name des Spielers
     */
    public void setSpielername(String spielername);

    /**
     * Übergibt dem Zettel die aktuelle Anzahl vom Gegner gefangener Steine.
     * @param anzahl Anzahl der gefangener Steine
     */
    public void setGefangenenAnzahl(int anzahl);

    /**
     * @param fehlermeldung übergibt eine Information oder Fehlermeldung die speziell für diesen Spieler
     * gedacht ist. Allgemeine Fehlermeldungen können direkt über die Oberfläche
     * ausgegeben werden.
     * @see OberflaecheInterface
     */
    public void setInfoBox(String fehlermeldung);

    /**
     * Die Inhalte des Spielerzettels sollen mit diesem Aufruf auf das übergebene
     * Graphics-Objekt gezeichnet werden.
     * Die zu verwendende Positionen und Winkel werden dem Zeichenobjekt bereits
     * im Konstruktor mitgeteilt.
     * @param g Graphics Objekt
     */
    public void zeichneDich(Graphics g);

    /**
     * Methode um dem Zettel mittzuteilen, dass ein Spieler seine Spielerzeit
     * aufgebraucht hat und nun mit der Periodenzeit fortfährt.
     * Wenn periodenZeitAktiv = true, dann befindet sich der Spieler
     * "Periodenzeit-Modus".
     * @param periodenZeitAktiv
     */
    public void setInPeriodenZeit(boolean periodenZeitAktiv);
}
