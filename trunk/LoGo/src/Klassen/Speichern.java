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
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFileChooser;
import logo.LoGoApp;

/**
 * Diese Klasse uebernimmt den Speichervorgang
 * Es wird im SGF-Format abgespeichert: Startbelegung, Spielernamen, Spielzuege,
 * Standardkonfigurationen von SGF, Vorgabesteine, Komipunkte, Spielfeldgroesse
 * @author beccy
 * @version 0.2
 */
public class Speichern {

    //Lokale Variablen
    private String spielzuege; 
    private Spielfeld spielfeld;
    private int spielfeldArray[][];
    private Spieler spieler_weiss;
    private Spieler spieler_schwarz;
    private long spieler_weiss_spielzeit;
    private long spieler_schwarz_spielzeit;
    private int spielfeldgroesse;
    private float komipunkte;
    private int vorgabesteine;
    private long periodenzeit;
    private Date aktuellesDatum;
    private SimpleDateFormat aktuellesDatum_format;
    private Date aktuelleZeit;
     private SimpleDateFormat aktuelleZeit_format;

    /**
     * Die Klasse bekommt mit dem Konstruktor das @param zuSpeicherndesSpielfeld
     * übergeben. ACHTUNG! Es sollten hierbei keine Änderungen an dem Objekt
     * Spielfeld vorgenommen werden, da diese sich auch direkt auf das laufende Spiel
     * übertragen würden.
     */
    Speichern(Spielfeld zuSpeicherndesSpielfeld) {
        //Instanz von Spielfeld wird der Speicherklasse im Konstruktor übergeben
        this.spielfeld = zuSpeicherndesSpielfeld;
    }

    public void SpeicherSpiel( ){

        //Get-Funktionen für die lokalen Variablen
        spieler_schwarz = spielfeld.getSpielerSchwarz();
        spieler_weiss = spielfeld.getSpielerWeiss();
        spielfeldgroesse = spielfeld.getSpielfeldGroesse();
        komipunkte = spieler_weiss.getKomiPunkte();
        vorgabesteine = spielfeld.getVorgabeZahl();
        periodenzeit = spielfeld.getPeriodenZeit();
        spieler_schwarz_spielzeit = spielfeld.getSpielerSchwarz().getVerbleibendeSpielzeitInMS();
        spieler_weiss_spielzeit = spielfeld.getSpielerWeiss().getVerbleibendeSpielzeitInMS();
        
        //Datumsformat festlegen
        aktuellesDatum = new Date();
        aktuellesDatum_format = new SimpleDateFormat("dd-MM-yyyy");

        //Zeitformat festlegen
        aktuelleZeit = new Date();
        aktuelleZeit_format = new SimpleDateFormat("HH:mm:ss");
              
        //Startbelegung
        spielfeldArray = spielfeld.getSpielfeldZumZeitpunkt(0);

        //spielzuege leeren
        spielzuege = "";

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

        //Spielzeiten der Spieler
        spielzuege += "TW["+spieler_weiss_spielzeit / 1000+"]";
        spielzuege += "TB["+spieler_schwarz_spielzeit / 1000+"]";

        //Standardkonfigurationen
        spielzuege += "GN[LoGo]";

        //Datum
        spielzuege += "DT["+aktuellesDatum_format.format(aktuellesDatum)+"]";

        //Zeit
        spielzuege += "TM["+aktuelleZeit_format.format(aktuelleZeit)+"]";

        //Periodenzeit
        spielzuege += "OT["+periodenzeit / 1000+"]";

        //Farb-Identifier: 1 für Schwarz | 2 für Weiss
        int farbe = 0;

        //X-Koordinate
        int x = 0; 
        
        //Y-Koordinate
        int y = 0; 

        //Für jeden Spielzug in der Collection
        for(int i=0;i<(spielfeld.getspielZugCollection().size());i++)
        {
            farbe = spielfeld.getspielZugCollection().get(i).getFarbe();
            x = spielfeld.getspielZugCollection().get(i).getXPosition();
            y = spielfeld.getspielZugCollection().get(i).getYPosition();

            //Umrechnung der Koordinaten in Buchstaben: Anfang mit 'a' = 97
            x += 97;
            y += 97;

            char _buchstabeX = (char)x;
            char _buchstabeY = (char)y;

            //Schwarzer Stein
            if (farbe == 1)
            {
                spielzuege += "S["+_buchstabeX+_buchstabeY+"]";
            }
            //Weisser Stein
            else if (farbe == 2)
            {
                spielzuege += "W["+_buchstabeX+_buchstabeY+"]";
            } else
            {
                break;
            }
        }

        //Fenster zum Speichervorgang
        JFileChooser chooser = new JFileChooser();
        chooser.showSaveDialog((FensterSpieloberflaeche)LoGoApp.meineOberflaeche);

        //Directory und Dateiname definieren
        String selFile = chooser.getCurrentDirectory() + "\\" + chooser.getSelectedFile().getName();

        //Endung überprüfen
        if(selFile.toLowerCase().contains(".sfg"))
        {
            // File Name enthält die Endung ".sfg"
        }
        else
        {
            selFile += ".sfg";
        }

        //Zusammengesetzten String in die Datei abspeichern
        PrintWriter pw = null;
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(selFile.toString()));
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
