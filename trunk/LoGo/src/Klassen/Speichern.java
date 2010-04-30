/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Klassen;

import GUI.FensterSpieloberflaeche;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import logo.LoGoApp;

/**
 *
 * @author beccy
 * @version 0.2
 */
public class Speichern {

    private String spielzuege;
    private Spielfeld spielfeld;
    private int spielfeldArray[][];
    private Spieler spieler_weiss;
    private Spieler spieler_schwarz;
    private int spielfeldgroesse;
    private float komipunkte;
    private int vorgabesteine;

    /**
     * Die Klasse bekommt mit dem Konstruktor das @param zuSpeicherndesSpielfeld
     * übergeben. ACHTUNG! Es sollten hierbei keine Änderungen an dem Objekt
     * Spielfeld vorgenommen werden, da diese sich auch direkt auf das laufende Spiel
     * übertragen würden.
     */
    Speichern(Spielfeld zuSpeicherndesSpielfeld) {
        this.spielfeld = zuSpeicherndesSpielfeld;
    }

    public void SpeicherSpiel() {
        spieler_schwarz = spielfeld.getSpielerSchwarz();
        spieler_weiss = spielfeld.getSpielerWeiss();
        spielfeldgroesse = spielfeld.getSpielfeldGroesse();
        komipunkte = spieler_weiss.getKomiPunkte();
        vorgabesteine = spielfeld.getVorgabeZahl();

        //Startbelegung
        spielfeldArray = spielfeld.getSpielfeldZumZeitpunkt(0);

        //Standardkonfigurationen
        spielzuege += "(;GM[1]FF[4]RU[Japanese]";

        //Spielfeldgroesse
        spielzuege += "SZ[" + spielfeldgroesse + "]";

        //Vorgabesteine
        spielzuege += "HA[" + vorgabesteine + "]";

        //Komipunkte
        spielzuege += "KM[" + komipunkte + "]";

        //Spielernamen
        spielzuege += "PW[" + spieler_weiss.getSpielerName() + "]";
        spielzuege += "PB[" + spieler_schwarz.getSpielerName() + "]";

        //Standardkonfigurationen
        spielzuege += "GN[LoGo]DT[Default]TM[Default]";

        int farbe = 0;
        int x = 0;
        int y = 0;

        for (int i = 0; i < (spielfeld.getspielZugCollection().size()); i++) {

            farbe = spielfeld.getspielZugCollection().get(i).getFarbe();
            x = spielfeld.getspielZugCollection().get(i).getXPosition();
            y = spielfeld.getspielZugCollection().get(i).getYPosition();

            if (farbe == 1) {//Schwarzer Stein
                spielzuege += "S[" + x + y + "]";
            } else if (farbe == 2) {//Weisser Stein
                spielzuege += "W[" + x + y + "]";
            } else {
                break;
            }
        }


        /*
         * Hi Beccy, bin da grad druebergestolpert und dachte das war doch so etwas,
         * wie Du gesucht hast ... ?!? ... Wenn nicht einfach wieder loeschen ...
         * LG, Steven
         */
        JFileChooser chooser = new JFileChooser();
        chooser.showSaveDialog( (FensterSpieloberflaeche)LoGoApp.meineOberflaeche);
        String selFile = chooser.getSelectedFile().getName() + ".cfg";


        PrintWriter pw = null;
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(selFile));
            pw = new PrintWriter(bw);

            pw.println(spielzuege);
        } catch (IOException e) {
            System.out.println("Konnte Datei nicht erstellen");
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }
}
