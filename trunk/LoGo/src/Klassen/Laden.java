/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Klassen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Diese Klasse uebernimmt den Ladevorgang
 * @author beccy
 */
public class Laden {

    //lokale Variablen
    private String _geladenerString;
    private Spieler spieler_weiss;
    private Spieler spieler_schwarz;
    public Spielfeld _spielfeld;

    //Funktion für den Identifier des Switch-Case Konstrukts
    public int getID(String id)
    {
        int _id = 0;

        //Spielfeldgroesse
        if(id.equals("SZ"))
        {
          _id = 1;
        }
        //Vorgabesteine
        else if(id.equals("HA"))
        {
          _id = 2;
        }
        //Komipunkte
        else if(id.equals("KM"))
        {
          _id = 3;
        }
        //Spielername Weiss
        else if(id.equals("PW"))
        {
          _id = 4;
        }
        //Spielername Schwarz
        else if(id.equals("PB"))
        {
          _id = 5;
        }
        //Zeitanzeige Weisser Spieler
        else if(id.equals("TPW"))
        {
            _id = 6;
        }
        // Zeitanzeige Schwarzer Spieler
        else if (id.equals("TPB"))
        {
            _id = 7;
        }
        //Periodenzeit
        else if(id.equals("OT"))
        {
            _id = 8;
        }
        //Spielzuege Schwarz
        else if(id.equals("S"))
        {
          _id = 9;
        }
        //Spielzuege Weiss
        else if(id.equals("W"))
        {
          _id = 10;
        }
        else
        {
          _id = -1;
        }
        return _id;
    }

    public Laden(){

    }
    
    public Spielfeld getSpielfeld(){
        return this._spielfeld;
    }



    public void LadeSpiel( ){

        try{
            //Hier muss der String des OpenFileDialog verwendet werden
            BufferedReader in = new BufferedReader(new FileReader ("C:/Go-Spiel.txt"));
            String zeile = null;
            while((zeile = in.readLine()) != null);
            _geladenerString = zeile;
        }catch(IOException e){
            ;
        }

    //Positionsvariablen von den Klammern
    int _klammer_auf;
    int _klammer_zu;
    
    String identifier = null;

    //Herausschneidender Standard_SGF_Konfiguration
    _klammer_auf = _geladenerString.indexOf("(;");
    _geladenerString.substring(_klammer_auf);

    //Ein Identifier besteht maximal aus zwei Ziffern
    char c1;
    char c2;
    
    String s_parameter  = null;

    //Deklaration von Parametervariablen als Integerzahl, Float und Long
    int i_parameter = 0;
    float f_parameter = 0;
    long l_parameter = 0;

    //Parametervariablen für die Spielzüge als String
    String s_parameterX;
    String s_parameterY;

    //Parametervariablen für die Spielzüge als Integerzahl
    int i_parameterX = 0;
    int i_parameterY = 0;

    //Durchfuehren der Schleife bis der String keinen Inhalt mehr besitzt
    while(_geladenerString!=null)
    {
        _klammer_auf = _geladenerString.indexOf("[");


       c1 = _geladenerString.charAt(0);
       identifier = String.valueOf(c1);

       //Wenn der Identifier aus mehr als einem Buchstaben besteht
       if(_klammer_auf > 1)
       {
        c2 = _geladenerString.charAt(1);
        identifier += String.valueOf(c2);
       }

       _klammer_zu = _geladenerString.indexOf("]");

       //Parameterzuweisung
       s_parameter = _geladenerString.substring(_klammer_auf, _klammer_zu);
       
    /*
     * Je nach Identifier wird der Parameter als Integerzahl, Float, Long oder
     * String interpretiert und verwendet
     */
    switch(this.getID(identifier)){
        case 1:
            /* Spielfeldgröße
             * Umwandeln von String zu Intergerzahl für die Spielfeldgroesse
             */
            i_parameter = Integer.parseInt(s_parameter);

            //Erzeuge Spielfeld und weise der lokalen Variablen das neue Spielfeld zu
            this._spielfeld = new Spielfeld(i_parameter);

            //Weise dem Spielfeld die beiden Spieler zu
            _spielfeld.setSpielerSchwarz(spieler_schwarz);
            _spielfeld.setSpielerWeiss(spieler_weiss);
            break;
        case 2: 
            //Umwandeln von String zu Integerzahl für Vorgabesteine
            i_parameter = Integer.parseInt(s_parameter);
            this._spielfeld.initialisiereFeldMitVorgabenFuerSchwarz(i_parameter);
            break;
        case 3:
            //Umwandeln von String zu Float für Komipunkte
            f_parameter = Float.parseFloat(s_parameter);
            this.spieler_weiss.setKomiPunkte(f_parameter);
            break;
        case 4:
            //Name Spieler - Weiss
            spieler_weiss.setSpielerName(s_parameter);
            break;
        case 5:
            //Name Spieler - Schwarz
            spieler_schwarz.setSpielerName(s_parameter);
            break;
        case 6:
            //Umwandeln von String zu Long für Spielzeit
            l_parameter = Long.parseLong(s_parameter);
            this.spieler_weiss.setVerbleibendeSpielzeitInMS(l_parameter);
            break;
        case 7:
            //Umwandeln von String zu Long für verbleibende Zeit
            l_parameter = Long.parseLong(s_parameter);
            this.spieler_schwarz.setVerbleibendeSpielzeitInMS(l_parameter);
            break;
        case 8:
            //Umwandeln von String zu Long für Periodenzeit
            l_parameter = Long.parseLong(s_parameter);
            this._spielfeld.setPeriodenZeit(l_parameter);
            break;
        case 9:
            /*
             * Koordinaten wenn als Identifier 'B' erscheint
             * Trenne X-Buchstabe von Y-Buchstabe
             */
            s_parameterX = s_parameter.substring(0,1);
            s_parameterY = s_parameter;

            //Umwandlung und weise Koordinaten lokalen Variablen zu
            i_parameterX = Integer.parseInt(s_parameterX);
            i_parameterY = Integer.parseInt(s_parameterY);

            //Ziehe das 'a' = 97 wieder ab um die Koordinate als Zahl zu bekommen
            i_parameterX -= 97;
            i_parameterY -= 97;

            //Mache Zug mit Farbe = 1 für Spieler schwarz
            this._spielfeld.macheZug(i_parameterX, i_parameterY, 1);
            break;
        case 10:
            /*
             * Koordinaten wenn als Identifier 'W' erscheint
             * Siehe case 9
             *
             */
            s_parameterX = s_parameter.substring(0,1);
            s_parameterY = s_parameter;
            i_parameterX = Integer.parseInt(s_parameterX);
            i_parameterY = Integer.parseInt(s_parameterY);
            i_parameterX -= 97;
            i_parameterY -= 97;

            //Mache Zug mit Farbe = 2 für Spieler weiss
            this._spielfeld.macheZug(i_parameterX, i_parameterY, 2);
            break;

        default:
            break;
        }
        //Schneidet aus dem Originalstring die schließende Klammer heraus
        _geladenerString.substring(_klammer_zu);
        }
    }
}
