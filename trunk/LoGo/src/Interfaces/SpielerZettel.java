package Interfaces;

import java.awt.Graphics;

/**
 *
 * @author steven
 * @version 0.1
 *
 * Ein SpielerZettel stellt alle für einen Spieler relavanten Informationen
 * dar und dient dazu, einen Überblick über die wichtigsten Änderungen und
 * Eigenschaftesn über sein Spiel zu bekommen.
 *
 * Klasse bekommt im Konstruktor xPos, yPos linkeEcke oben und die Winkelabweichung
 * senkrecht zum Lot. Im Uhrzeigersinn positive Werte. Negative Werte sind gegen
 * den Uhrzeigersinn.
 */
public interface SpielerZettel {

    /**
     * Setzen des @param spielername ns. Der @param spielername enthält dabei
     * den Namen, den der Spieler sich als Spielernamen gegeben hat.
     */
    public void setSpielername(String spielername);

    /**
     * Übergibt dem Zettel die aktuelle @param anzahl vom Gegner gefangener Steine.
     *
     */
    public void setGefangenenAnzahl(int anzahl);

    /**
     * @param fehlermeldung übergibt eine Information oder Fehlermeldung die speziell für diesen Spieler
     * gedacht ist. Allgemeine Fehlermeldungen können direkt über die Oberfläche
     * ausgegeben werden. @see OberflaecheInterface
     */
    public void setInfoBox(String fehlermeldung);

    /**
     * Die Inhalte des Spielerzettels sollen mit diesem Aufruf auf das übergebene
     * Graphics-Objekt @param g gezeichnet werden.
     * Die zu verwendende Positionen und Winkel werden dem Zeichenobjekt bereits
     * im Konstruktor mitgeteilt.
     */
    public void zeichneDich(Graphics g);

    /**
     * Methode um dem Zettel mittzuteilen, dass ein Spieler seine Spielerzeit
     * aufgebraucht hat und nun mit der Periodenzeit fortfährt.
     * Wenn @param periodenZeitAktiv = true, dann befindet sich der Spieler
     * "Periodenzeit-Modus".
     */
    public void setInPeriodenZeit(boolean periodenZeitAktiv);


}
