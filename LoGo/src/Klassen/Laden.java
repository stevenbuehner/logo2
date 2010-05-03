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

    //Funktion für den Identifier des Switch Case Konstrukts
    public int getID(String id)
    {
        int _id=0;

        //Spielfeldgroesse
        if(id == "SZ")
        {
          _id = 1;
        }
        //Vorgabesteine
        else if(id == "HA")
        {
          _id = 2;
        }
        //Komipunkte
        else if(id == "KM")
        {
          _id = 3;
        }
        //Spielername Weiss
        else if(id == "PW")
        {
          _id = 4;
        }
        //Spielername Schwarz
        else if(id == "PB")
        {
          _id = 5;
        }
        //Zeitanzeige Weisser Spieler
        else if(id == "TPW")
        {
            _id = 6;
        }
        // Zeitanzeige Schwarzer Spieler
        else if (id == "TPB")
        {
            _id = 7;
        }
        //Periodenzeit
        else if(id == "OT")
        {
            _id = 8;
        }
        //Spielzuege Schwarz
        else if(id == "S")
        {
          _id = 9;
        }
        //Spielzuege Weiss
        else if(id == "W")
        {
          _id = 10;
        }
        else
        {
          _id = -1;
        }
        return _id;
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

    //Herausschneiden von (;
    _klammer_auf = _geladenerString.indexOf("(;");
    _geladenerString.substring(_klammer_auf);

    //Ein Identifier besteht maximal aus 2 Ziffern
    char c1;
    char c2;
    
    String s_parameter  = null;
    //Parametervariablen deklaration
    int i_parameter = 0;
    float f_parameter = 0;
    long l_parameter = 0;

    //String Parametervariablen für Spielzüge
    String s_parameterX;
    String s_parameterY;
    //Int Parametervariablen für Spielzüge
    int i_parameterX = 0;
    int i_parameterY = 0;

    
    while(_geladenerString!=null)//Solange im String was drin steht
    {
        _klammer_auf = _geladenerString.indexOf("[");


       c1 = _geladenerString.charAt(0);
       identifier = String.valueOf(c1);
       if(_klammer_auf > 1) //Wenn der Identifier aus mehr als einem Buchstaben besteht
       {
        c2 = _geladenerString.charAt(1);
        identifier += String.valueOf(c2);
       }

       _klammer_zu = _geladenerString.indexOf("]");

       //Parameterzuweisung
       s_parameter = _geladenerString.substring(_klammer_auf, _klammer_zu);
       
    //Je nach Identifier wird der Parameter als Int Float Long oder String interpretiert
    // und verwendet
    switch(this.getID(identifier)){
        case 1://Spielfeldgröße
            //String zu Int Umwandlung für die Spielfeldgröße
            i_parameter = Integer.parseInt(s_parameter);
            //Erzeuge Spielfeld und weise der lokalen Variable das neue Spielfeld zu
            this._spielfeld = new Spielfeld(i_parameter);
            //Weise dem Spielfeld die beiden Spieler zu
            _spielfeld.setSpielerSchwarz(spieler_schwarz);
            _spielfeld.setSpielerWeiss(spieler_weiss);
            break;
        case 2: //String zu Int Umwandlung für Vorgabesteine
            i_parameter = Integer.parseInt(s_parameter);
            this._spielfeld.initialisiereFeldMitVorgabenFuerSchwarz(i_parameter);
            break;
        case 3:
            //String Float Umwandlung für Komipunkte
            f_parameter = Float.parseFloat(s_parameter);
            this.spieler_weiss.setKomiPunkte(f_parameter);
            break;
        case 4:
            //Name Spieler - weiss
            spieler_weiss.setSpielerName(s_parameter);
            break;
        case 5:
            //Name Spieler - schwarz
            spieler_schwarz.setSpielerName(s_parameter);
            break;
        case 6:
            //String long Umwandlung für Spielzeit
            l_parameter = Long.parseLong(s_parameter);
            this.spieler_weiss.setVerbleibendeSpielzeitInMS(l_parameter);
            break;
        case 7:
            //String long Umwandlung für verbleibende Zeit
            l_parameter = Long.parseLong(s_parameter);
            this.spieler_schwarz.setVerbleibendeSpielzeitInMS(l_parameter);
            break;
        case 8:
            //String long Umwandlung für Periodenzeit
            l_parameter = Long.parseLong(s_parameter);
            this._spielfeld.setPeriodenZeit(l_parameter);
            break;
        case 9:
            //Koordinaten wenn als Identifier 'B' erscheint
            //Trenne X-Buchstabe von Y-Buchstabe
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
            //Koordinaten wenn als Identifier 'W' erscheint
            //Siehe case 9
            s_parameterX = s_parameter.substring(0,1);
            s_parameterY = s_parameter;
            i_parameterX = Integer.parseInt(s_parameterX);
            i_parameterY = Integer.parseInt(s_parameterY);
            i_parameterX -= 97;
            i_parameterY -= 97;
            this._spielfeld.macheZug(i_parameterX, i_parameterY, 2);
            break;

        default:
            break;
        }
        //schneide aus Originalstring die schließende Klammer heraus
        _geladenerString.substring(_klammer_zu);
        }
    }
}
