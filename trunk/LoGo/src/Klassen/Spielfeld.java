/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor
 */
package Klassen;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author steven, tommy
 * @version 0.1
 */
public class Spielfeld {

    // interne Speicherobjekte

    /**
     * Jeder Zug bekommt eine eindeutige fortlaufende Nummer. Das Attribut
     * letzteZugnummer zeigt immer auf die zuletzt verwendete Zugnummer. Diese
     * Nummer kann auch direkt als Array-Zeiger auf den Zug (gespeichert in
     * spielZugCollection) zum zugehoerigen Zug verwendet werden.
     */
    private int letzteZugnummer;

    /**
     * Groesse des Spielfeldes und damit auch Groesse der zweidimensionalen
     * Spielfeldarrays
     */
    private int spielfeldGroesse;
    private int gefangenenAnzahlWeiss;
    private int gefangenenAnzahlSchwarz;
    private List<Spielzug> spielZugCollection;
    private int xPosVerboten;
    private int yPosVerboten;

    private Spieler spielerSchwarz;
    private Spieler spielerWeiss;

    /* Wenn die Spielerzeit aufgebraucht ist gibt es noch die Periodenzeit.
    * Diese ist fuer alle Spieler gleich. In wirklich heisst dieser Wert Byo-yomi.
    */
    private long    periodenZeit;

    
    // Chache Funktionen zum schnelleren Spielen
    private int[][] aktuellesSpielfeldCache;
    private int spielfeldCacheMitZugnummerStand;


    public Spielfeld(){
        this( 9 );
    }

    public Spielfeld(int spielfeldGroesse) {
        this.spielfeldGroesse = spielfeldGroesse;
        this.letzteZugnummer = 0;
        this.setGefangenenAnzahlSchwarz(0);
        this.setGefangenenAnzahlWeiss(0);
        this.spielZugCollection = new ArrayList<Spielzug>();
        this.setXPosVerboten(-1); // noch nichts ist Verboten
        this.setYPosVerboten(-1);

        // Chache Funktionen setzen
        aktuellesSpielfeldCache = new int[this.getSpielfeldGroesse()][this.getSpielfeldGroesse()];
        spielfeldCacheMitZugnummerStand = this.letzteZugnummer;
        /* Feld muss als Leeres Feld initialisiert werden */
        int i,j;
        for(i=0; i<spielfeldGroesse; i++){
            for(j=0; j<spielfeldGroesse; j++){
                aktuellesSpielfeldCache[i][j] = Konstante.SCHNITTPUNKT_LEER;
            }
        }
    }


    /**
     *
     * @return Den schwarzen Spieler mit dem Objekttyp Spieler
     */
    public Spieler getSpielerSchwarz(){
        return this.spielerSchwarz;
    }

    /**
     *
     * @param Setzen des schwarzerSpieler
     */
    public void setSpielerSchwarz( Spieler schwarzerSpieler ){
        this.spielerSchwarz = schwarzerSpieler;
    }


    /**
     *
     * @return Den Weissen Spieler mit dem Objekttyp Spieler
     */
    public Spieler getSpielerWeiss(){
        return this.spielerWeiss;
    }

    /**
     *
     * @param Setzen des WeisserSpieler
     */
    public void setSpielerWeiss( Spieler WeisserSpieler ){
        this.spielerWeiss = WeisserSpieler;
    }

    /**
     *
     * @return Gibt den aktuellen Wert der Periodenzeit zurueck.
     * Wenn die Spielerzeit aufgebraucht ist gibt es noch die Periodenzeit.
     * Diese ist fuer alle Spieler gleich. In wirklich heisst dieser Wert Byo-yomi.
     */
    public long getPeriodenZeit(){
        return this.periodenZeit;
    }

    /**
     *
     * @param Speichern der periodenZeit (fuer beide Spieler)
     * Wenn die Spielerzeit aufgebraucht ist gibt es noch die Periodenzeit.
     * Diese ist fuer alle Spieler gleich. In wirklich heisst dieser Wert Byo-yomi.
     */
    public void setPeriodenZeit( long periodenZeit ){
        this.periodenZeit = periodenZeit;
    }


    /**
     * 
     * @return Die Nummer des zuletzt gespielten Zuges
     */
    public int getLetzteZugnummer() {
        return this.letzteZugnummer;
    }

    /**
     *
     * @return Größe des aktuellen Spielfeldes
     */
    public int getSpielfeldGroesse() {
        return this.spielfeldGroesse;
    }

    /**
     *
     * @return Anzahl der gefangenen schwarzen Stein (Punkte für weiss)
     */
    public int getGefangenenAnzahlWeiss() {
        return this.gefangenenAnzahlWeiss;
    }

    /**
     * @param gefangenenAnzahl Speichern der Anzahl der gefangenen schwarzen Stein (Punkte für weiss)
     */
    public void setGefangenenAnzahlWeiss(int gefangenenAnzahl) {
        this.gefangenenAnzahlWeiss = gefangenenAnzahl;
    }

    /**
     *
     * @return Anzahl der gefangenen weissen Stein (Punkte für schwarz)
     */
    public int getGefangenenanzahlSchwarz() {
        return this.gefangenenAnzahlSchwarz;
    }

    /**
     * @param gefangenenAnzahl Anzahl der gefangenen weissen Stein (Punkte für schwarz)
     */
    public void setGefangenenAnzahlSchwarz(int gefangenenAnzahl) {
        this.gefangenenAnzahlSchwarz = gefangenenAnzahl;
    }

    /**
     *
     * @return Gibt das aktuelle Spielfeld als Array zurück
     */
    public int[][] getAktuelesSpielFeld() {

        int spielfeld[][];

        if (this.spielfeldCacheMitZugnummerStand != this.letzteZugnummer) {
            spielfeld = new int[this.getSpielfeldGroesse()][this.getSpielfeldGroesse()];

            // Spielfeld initialisieren
            for (int i = this.getSpielfeldGroesse() - 1; i >= 0; i--) {
                for (int j = this.getSpielfeldGroesse() - 1; j >= 0; j--) {
                    spielfeld[j][i] = Konstante.SCHNITTPUNKT_LEER;
                }
            }
            /* Jetzt ist das Brett noch leer. Es muss also mit den richtigen
               Werten gefuellt werden - Fehlt noch*/

            this.aktuellesSpielfeldCache = spielfeld;
            this.spielfeldCacheMitZugnummerStand = this.letzteZugnummer;

            /*
             * Hier muss jetzt noch nach dem initialisieren die Werte aus der Collection
             * abgefragt werden und ins Feld eingetragen werden
             *
             */

        } else {
            spielfeld = this.aktuellesSpielfeldCache;
        }

        return spielfeld;
    }

    /**
     *
     * @param zeitpunkt Zeipunkt zu dem das Spielfeld zurückgegeben werden soll.
     * In erster Linie für REDO und UNDO gedacht, nicht zum häufigen aufrufen,
     * da das Spielfeld hierbei immer neu generiert werden muss
     * @return Spielfeld zum übergebenen Zeipunkt in Form eines Arrays
     */
    public int[][] getSpielfeldZumZeitpunkt(int zeitpunkt) {

        int spielfeld[][];

        if (zeitpunkt == this.spielfeldCacheMitZugnummerStand) {
            spielfeld = this.aktuellesSpielfeldCache;
        } else {
            spielfeld = new int[this.getSpielfeldGroesse()][this.getSpielfeldGroesse()];

            // Spielfeld initialisieren
            for (int i = this.getSpielfeldGroesse() - 1; i >= 0; i--) {
                for (int j = this.getSpielfeldGroesse() - 1; j >= 0; j--) {
                    spielfeld[j][i] = Konstante.SCHNITTPUNKT_LEER;
                }
            }

            /*
             * Hier muss jetzt noch nach dem initialisieren die Werte aus der Collection
             * abgefragt werden und ins Feld eingetragen werden
             */
        }

        return spielfeld;
    }

    /**
     *
     * @param zeitpunkt Zeitpunkt auf den das Spielfeld FEST gesetzt werden soll.
     * Ein Redo ist dann nicht mehr möglich. Das Spiel wird zu diesem Zeitpunkt
     * weitergespielt.
     * @return True, wenn die Funktion erfolgreich ausgeführt wurde, sonst false
     */
    public boolean setSpielfeldZumZeitpunkt(int zeitpunkt) {
        /*
         * Setzen des Spiels auf den Zeitpunkt:
         * - prüfen ob Möglich
         * - überflüssie Spielzüge aus der Collection löschen
         * - Chache erneuern
         * - ChachZähler erneuern
         * - SpielZeitpunkt erneuern
         * - Bei Erfolg true zurückgeben, sonst false
         */

        return true;
    }

    /**
     * 
     * @param xPos X-Position des Spielfelds.
     * Diese kann Werte zwischen 1 und der Feldlänge enthalten
     * @param yPos Y-Position des Spielfelds.
     * Diese kann Werte zwischen 1 und der Feldlänge enthalten
     * @return Je nach Situarion signalisiert der Integer, was passiert ist:
     *  1: Zug wurde erfolgreich durchgefuehrt : (OK)
     *  0: Zug liegt nicht auf Spielfeld: (FEHLER)
     * -1: Zug war verboten : (FEHLER)
     * -2: Schnittpunkt schon belegt : (FEHLER)
     * -3: Selbstmord (verboten) : (FEHLER)
     */
    public int setStein(int xPos, int yPos) {

        /* Die Funktion setzt den Stein fuer abhaengig vom letzten gespielten Zug.
         * Wenn also Spieler schwarz den letzten Zug gesetzt hat, wird der neue
         * Zug für Spieler weiss eingetragen.
         */


         return   this.setStein( xPos, yPos, this.getSpielerAnDerReihe() );


         /* Weitere Aufgaben:
         *  - Prüfen des Zuges, ob er Möglich ist (Doppelzüge beachten)
         *      => Ueber die Funktion setSteinMoeglich( ... )
         *  - Prüfen wer am Zug ist
         *  - Cache-Werte INKREMENTELL erneuern.
         */
        
        //DUmmy
    }

    /**
     *
     * @param xPos X-Position des Spielfelds.
     * Diese kann Werte zwischen 1 und der Feldlänge enthalten
     * @param yPos Y-Position des Spielfelds.
     * Diese kann Werte zwischen 1 und der Feldlänge enthalten
     * @param spielerfarbe Farbe des setzenden Spielers
     * @return Je nach Situarion signalisiert der Integer, was passiert ist:
     *  1: Zug wurde erfolgreich durchgefuehrt : (OK)
     *  0: Zug liegt nicht auf Spielfeld: (FEHLER)
     * -1: Zug war verboten : (FEHLER)
     * -2: Schnittpunkt schon belegt : (FEHLER)
     * -3: Selbstmord (verboten) : (FEHLER)
     */

    public int setStein(int xPos, int yPos, int spielerfarbe) {
        /* Koordinaten umrechnen, da Array bei 0 beginnt */
        int xKoord = xPos - 1;
        int yKoord = yPos - 1;

        /* zugOk dient dazu um zu ermitteln ob der Zug valide ist. Faengt der
         * Zug einen Stein so ist er gueltig, da der Stein danach mindestens
         * eine Freiheit besitzt. Wird eine Nachbarfreiheit gefunden ist der
         * Zug auch valide. Wenn der zug nicht als Ok markiert wird, muss dies
         * allerdings nicht heissen das der Zug ungueltig ist. Es bedeutet
         * lediglich, dass der Zug weiter untersucht werden muss.
         */
        boolean zugOK = false;
        /* Als erstes wird auf Fehler getestet. Dies beinahltet Folgendes:
         * - Zug ist nicht auf dem Spielfeld
         * - Zug ist verboten (wegen Ko)
         * - Stein steht schon an der Position
         */

        /* 1. Testen ob Zug auf dem Spielfeld ist, sonst Fehler */
        if(xKoord < 0 || yKoord < 0 || xKoord >= this.getSpielfeldGroesse() || yKoord >= this.getSpielfeldGroesse()){
            return 0;
        }
        /* 2. Testen auf Ko (Also verbotener Zug)*/
        if(this.aktuellesSpielfeldCache[xKoord][yKoord] == Konstante.SCHNITTPUNKT_VERBOTEN) {
            return -1;
        }
        /* 3. Testen ob schon Stein da steht */
        if(this.aktuellesSpielfeldCache[xKoord][yKoord] == Konstante.SCHNITTPUNKT_SCHWARZ ||
           this.aktuellesSpielfeldCache[xKoord][yKoord] == Konstante.SCHNITTPUNKT_WEISS){
            return -2;
        }

        /* Der Schnittpunkt ist also Leer und es ist erlaubt darauf zu spielen.
         * Da nun die Situation auf dem Feld analysiert werden muss, wird extra
         * dafuer ein Analysefeld erstellt. Dies ist wie das richtige Feld ein
         * 2-Dimensionales Array, jedoch besteht es aus AnalyseSchnittpunkten.
         * Damit hat man die moeglichkeit durch die Eigenschaften der Klasse
         * AnalyseSchnittpunkt eine Breitensuche durchzufuehren.
         */
         
        AnalyseSchnittpunkt anaFeld[][];
        anaFeld = new AnalyseSchnittpunkt[this.getSpielfeldGroesse()][this.getSpielfeldGroesse()];

        /* Nun das Feld initialisieren. Dabei wird eine Kopie des aktuellen
         * Feldes erstellt. */
        for(int i=0; i<this.getSpielfeldGroesse(); i++){
            for(int j=0; j<this.getSpielfeldGroesse(); j++){
                anaFeld[i][j] = new AnalyseSchnittpunkt(i,j,this.aktuellesSpielfeldCache[i][j]);
            }
        }

        /* Auf die Kopie (AnaFeld) wird jetzt der Stein gesetzt. Das wirkt sich
         * nicht auf das momentane Feld aus, da nur auf der Kopie gearbeitet
         * wird .*/
        anaFeld[xKoord][yKoord].setBelegungswert(spielerfarbe);

        /* Nun werden einige Variablen eingefuehrt die das Auswerten erleichtern.
         * --> gefangeneSteine: Anzahl der Gefangenen Steine. Ist die Funktion
         * erfolgreich wird die Anzahl der gefangenen Steine zu den bereits
         * Gefangenen addiert.
         * --> momGefSteine: Um bei jedem Auswertungsschritt die Anzahl der
         * gefangenen Steine zu ermitteln.
         * --> steinIstEinzeln: Fuer die Ko-Regel ist es wichtig, ob der Stein
         * eine Gruppe aus einem Stein darstellt. Also ob er einzeln ist.
         * Wird waerend des Auswertens festgestellt, das ein Nachbarstein die
         * gleiche Farbe hat, wird dieser Wert auf False gesetzt.
         */
        int gefangeneSteine     = 0;
        int momGefSteine        = 0;
        boolean steinIstEinzeln = true;

        /* Jetzt werden die Nachbarsteine betrachtet. Wenn der Stein allerdings
         * am Rand ist, besitzt er bezueglich dieses Randes keinen Nachbarn.
         * Daher wird erst getestet ob sich ein Nachbarschnittpunkt befindet.
         * Ist der Nachbarstein ungleich der Spielfarbe, wird Versucht ein Stein
         * vom Brett zu nehmen. Je nach Rueckgabewert der Funktion
         * versucheSteinZuNehmen wird dann weitergearbeitet. Wenn der Wert 0 ist,
         * wurden keine Steine gefangen, es ist nichts zu tun. Ist der Wert -1
         * so ist der Schnittpunkt leer.
         * Besitzt der Nachbarstein die gleiche Farbe, so ist der Stein nicht
         * einzeln.
         * Es ist zu bemerken, dass wenn ein Stein gefangen wurd, das aktuelle
         * Spielfeld veraendert wird. Das ist allerdings in Ordnung, da wenn
         * ein Stein durch diesen Zug gefangen wird, er automatisch mindestens
         * eine Freiheit haben muss. Daher ist der Zug gueltig. */

        /* 1. Linke Seite */
        if(xKoord!=0){
            if(anaFeld[xKoord-1][yKoord].getBelegungswert()!=spielerfarbe){
                momGefSteine = versucheSteinZuNehmen(xKoord-1, yKoord, anaFeld);
                if(momGefSteine > 0) {
                    gefangeneSteine+=momGefSteine;
                    zugOK = true;
                }
                else if(momGefSteine == 0){/* nichts */ }
                else if(momGefSteine == -1){
                    zugOK = true;
                }
            }
            else {
                steinIstEinzeln = false;
            }
        }
        /* 2. Rechte Seite */
        if(xKoord!=this.getSpielfeldGroesse()-1){
            if(anaFeld[xKoord+1][yKoord].getBelegungswert()!=spielerfarbe){
                momGefSteine = versucheSteinZuNehmen(xKoord+1, yKoord, anaFeld);
                if(momGefSteine > 0) {
                    gefangeneSteine+=momGefSteine;
                    zugOK = true;
                }
                else if(momGefSteine == 0){/* nichts */}
                else if(momGefSteine == -1){
                    zugOK = true;
                }
            }
            else {
                steinIstEinzeln = false;
            }
        }
        /* 3. Untere Seite */
        if(yKoord!=0){
            if(anaFeld[xKoord][yKoord-1].getBelegungswert()!=spielerfarbe){
                momGefSteine = versucheSteinZuNehmen(xKoord, yKoord-1, anaFeld);
                if(momGefSteine > 0) {
                    gefangeneSteine+=momGefSteine;
                    zugOK = true;
                }
                else if(momGefSteine == 0){/* nichts */}
                else if(momGefSteine == -1){
                    zugOK = true;
                }
            }
            else {
                steinIstEinzeln = false;
            }
        }
        /* 4. Obere Seite */
        if(yKoord!=this.getSpielfeldGroesse()-1){
           if(anaFeld[xKoord][yKoord+1].getBelegungswert()!=spielerfarbe){
                momGefSteine = versucheSteinZuNehmen(xKoord, yKoord+1, anaFeld);
                if(momGefSteine > 0) {
                    gefangeneSteine+=momGefSteine;
                    zugOK = true;
                }
                else if(momGefSteine == 0){/* nichts */}
                else if(momGefSteine == -1){
                    zugOK = true;
                }
            }
            else {
                steinIstEinzeln = false;
            }
        }

        /* Ist zugOK immer noch false, hat der Stein weder einen Anderen gefangen,
         * noch besitzt er selbst freiheiten. Daher muss betrachtet werden, ob
         * die Gruppe in der sich der Stein befindet eine Freiheit hat, wenn
         * dies nicht so ist, ist der Zug Selbstmord und somit ungueltig */
        if(zugOK == false){
            if(versucheSteinZuNehmen(xKoord, yKoord, anaFeld, false) == 0){
                /* Dann hat der Zug zwar keine Steine gefangen und der Stein
                 * hat keine Freiheiten, aber die Gruppe hat welche. Deshalb
                 * kann der Stein gesetzt werden */
                this.aktuellesSpielfeldCache[xKoord][yKoord] = spielerfarbe;
                this.spielfeldCacheMitZugnummerStand = this.letzteZugnummer+1;
                this.steinEintragen(xPos, yPos, spielerfarbe);
                this.loescheVerbotenenPunkt();
                this.erhoeheGefangenenZahl(spielerfarbe, gefangeneSteine);
                return 1;
            }
            else {
                /* Die Gruppe zu der der Stein gehoert hat keine Freiheiten.
                 * Es ist also Selbstmord! Es ist zu bemerken, dass dabei die
                 * Brettstellung nicht veraendert wird, da ja nichts gefangen
                 * wurde (also vom richtigen Brett) */
                return -3;
            }
        }

        /* Wenn man bis hier kommt, wurde ein Stein gesetzt der entweder eine
         * Gruppe gefangen hat, oder mindestens eine Freiheit besitzt. Daher
         * kann der Stein einfach gesetzt werden.
         * Zu diesem Zeitpunkt ist der Chache auf jeden Fall veraendert worden
         * Die Zugnummer selbst wird mit steinEintragen eins nach oben gezaehlt.
         * Da zug OK ist, muss der letzte verbotene Punkt geloescht werden!
         */
        aktuellesSpielfeldCache[xKoord][yKoord] = spielerfarbe;
        this.spielfeldCacheMitZugnummerStand = this.letzteZugnummer+1;
        this.loescheVerbotenenPunkt();

        /* Jetzt ist noch Ko abzufangen
         * Wenn nichts gefangen wurde, ist es auch kein Ko.
         * Wenn der Stein nicht einzeln ist, ist es auch kein Ko.
         * Wenn mehr als ein Stein gefangen wurde ist es auch kein Ko.*/
        if(gefangeneSteine == 0 || gefangeneSteine > 1 || steinIstEinzeln == false ){
            this.steinEintragen(xPos, yPos, spielerfarbe);
            this.erhoeheGefangenenZahl(spielerfarbe, gefangeneSteine);
            return 1;
        }

        /* Jetzt ist klar: Es wurde genau ein stein gefangen und der Stein der
         * gesetzt wurde bildet eine Gruppe aus genau einem Stein, naemlich
         * sich selbst. (Ist also einzeln)
         * Nun muss auf Ko geprueft werden. Wenn der Stein genau eine Freiheit
         * besitzt, muss dort das Ko eingezeichnet werden. Daher werden jetzt
         * die Freiheiten des Steins gezaehlt. Sind diese am Ende 1 so ist es Ko
         * Besitzt der Stein nur eine Freiheit, wird sich in freiXPos und freiYPos
         * der Wert dieser Freiheit gemerkt.
         */
         int freiheitDesSteins = 0;
         int freiXPos = -1;
         int freiYPos = -1;

         /* 1. Linke Seite (Wenn sie existiert)*/
         if(xKoord!=0){
             if(this.aktuellesSpielfeldCache[xKoord - 1][yKoord] == Konstante.SCHNITTPUNKT_LEER){
                 freiheitDesSteins++;
                 freiXPos = xKoord - 1;
                 freiYPos = yKoord;
             }
         }
         /* 2. Rechte Seite (Wenn sie existerit)*/
         if(xKoord!=this.getSpielfeldGroesse()-1){
             if(this.aktuellesSpielfeldCache[xKoord + 1][yKoord] == Konstante.SCHNITTPUNKT_LEER){
                 freiheitDesSteins++;
                 freiXPos = xKoord + 1;
                 freiYPos = yKoord;
             }
         }
         /* 3. Untere Seite (Wenn sie existiert)*/
         if(yKoord!=0){
             if(this.aktuellesSpielfeldCache[xKoord][yKoord - 1] == Konstante.SCHNITTPUNKT_LEER){
                 freiheitDesSteins++;
                 freiXPos = xKoord;
                 freiYPos = yKoord - 1;
             }
         }
         /* 4. Obere Seite (Wenn sie existiert)*/
         if(yKoord!=this.getSpielfeldGroesse()-1){
             if(this.aktuellesSpielfeldCache[xKoord][yKoord + 1] == Konstante.SCHNITTPUNKT_LEER){
                 freiheitDesSteins++;
                 freiXPos = xKoord;
                 freiYPos = yKoord + 1;
             }
         }

         /* Wenn die Freiheiten nicht genau 1 sind, ist es kein Ko*/
         if(freiheitDesSteins!=1){
             this.steinEintragen(xPos, yPos, spielerfarbe);
             this.erhoeheGefangenenZahl(spielerfarbe, gefangeneSteine);
             return 1;
         }

         /* Es ist also Ko. Das Muss markiert werden. Der Wert dafuer ist in
          * freiXPos und freiYPos gespeichert. */
         this.setzeVerbotenenPunkt(freiXPos, freiYPos);
         this.aktuellesSpielfeldCache[freiXPos][freiYPos] = Konstante.SCHNITTPUNKT_VERBOTEN;
         this.steinEintragen(xPos, yPos, spielerfarbe);
         this.erhoeheGefangenenZahl(spielerfarbe, gefangeneSteine);
         return 1;
    }

    /*
     * Wollen wir die Funktion loeschen? Ich waere dafuer, da wir sie wahrscheinlich nicht
     * brauchen. gez tommy
     */
    public boolean setSteinMoeglich( int xPos, int yPos, int spielerfarbe ) {
        /*
         * Prüft ab ob an diesem Punkt ein Stein gesetzt werden darf.
         * Funktion wird verwendet, wenn zum Beispiel über einen Schnittpunkt
         * mit der Maus gefahren wird
         *
         * Evt. kann auch hierfür eine Cache-Collection aufgebaut werden,
         * die mit dem setzen eines neuen Steines dann allerdings wieder gelöscht
         * werden muss.
         */


        return true;
    }

    private int getSpielerAnDerReihe(){

        int letzterSpieler = getSpielerVonLetztemZug();
        
        if (letzterSpieler == Konstante.SCHNITTPUNKT_SCHWARZ){
            return Konstante.SCHNITTPUNKT_WEISS;
        }
        else if (letzterSpieler == Konstante.SCHNITTPUNKT_WEISS){
            return Konstante.SCHNITTPUNKT_SCHWARZ;
        }
        else{
            // FEHLER
            return Konstante.FEHLER;
        }
        

    }

    private int getSpielerVonLetztemZug(){
        /* Ich bin mir nicht ganz sicher ob die Funktion funktioniert
             und ob sie auch noch funktioniert, wenn mal Elemente in der Collection
             gelöscht wurden.
         */
        if (this.spielZugCollection.size() == 0){
            // Es wurde noch kein Zug eingetragen. Dann beginnt immer Spieler SCHWARZ.
            // Das heisst dass theoretisch Weiss davor dran gewesen waere.
            return Konstante.SCHNITTPUNKT_WEISS;
        }
        else{
            return this.spielZugCollection.get(this.spielZugCollection.size()-1).getFarbe();
        }
    }


    /**
     * Diese Funktion soll Steine vom Spielfeld nehmen, wenn dies moeglich ist.
     * Dabei benoetigt sie eine Koordinate mit der sie beginnen kann, damit klar
     * ist, welche Gruppe gemeint ist. Von dieser Koordinate aus versucht sie dann
     * Steine vom Brett zu entfernen.
     * @param xPos Ist die X-Koordinate des Steines, der zur Gruppe gehoert,
     * die vom Brett zu nehmen versucht wird. (Wert 0 bis feldgroesse-1)
     * @param yPos Ist die Y-Koordinate des Steins. (Wert 0 bis feldgroesse-1)
     * @param Feld Ist ein 2-Dimensionales Array vom Typ AnalyseSchnittpunkt. Es
     * dient als Brettstellung, die zu betrachten ist. Dieses Feld wird veraendert
     * @param nehmen Gibt an ob die Steine nur markiert werden, oder auch wirklich
     * vom Brett genommen werden sollen. Sollen die Steine nicht vom Brett genommen
     * werden, so ist der Rueckgabewert 1, wenn die Steine genommen werden koennten,
     * und 0 wenn nicht.
     * @return Der Rueckgabewert kennzeichnet bei Erfolg (also wenn die Gruppe
     * vom Brett genommen werden konnte) wieviele Steine entfernt worden. Bei
     * Misserfolg gibt es 2 Moeglichkeiten: 1. Die Gruppe konnte nicht entfernt
     * werden, da sie noch Freiheiten besitzt (Rueckgabewert 0). 2. Die Gruppe
     * konnte nicht entfernt werden, weil an (xPos, yPos) gar kein Stein liegt
     * (Rueckgabewert -1)
     */
    private int versucheSteinZuNehmen(int xPos, int yPos, AnalyseSchnittpunkt feld[][], boolean nehmen){
        /* Zuerst testen ob an (x,y) ueberhaupt ein Stein ist */
        if(feld[xPos][yPos].getBelegungswert()==Konstante.SCHNITTPUNKT_LEER || feld[xPos][yPos].getBelegungswert()==Konstante.SCHNITTPUNKT_VERBOTEN){
            return -1;
        }

        /* Jetzt beginnt die eigentliche Prozedur, da man weiss, dass es sich entweder
         * um einen schwarzen oder einen weissen Stein handelt. Zuerst wird getestet ob die
         * Steine schon betrachtet wurden. Ist dies der Fall wird abgebrochen und
         * gesagt, das keine Steine gefangen worden (Da diese wenn sie schon
         * markiert sind auch schon vom Brett genommen worden, oder nicht)*/
        if(feld[xPos][yPos].getSteinStatus()==Konstante.STEIN_GEFANGEN ||
           feld[xPos][yPos].getSteinStatus()==Konstante.STEIN_LEBENDIG){
            return 0;
        }

        /* Es muss also was veraendert werden, da der Status der Steine noch
         * ungewiss ist. Am ende der Funktion werden entweder alle Steine der
         * Gruppe als lebendig markiert, oder alle als tot markiert und vom Brett
         * (richtigen spielfeld) genommen.
         */
        boolean fangbar = true;
        int farbe = feld[xPos][yPos].getBelegungswert();

        /* Es wird eine Liste angelegt in die die durchsuchten Steine gelegt
         * werden. Dabei wird eine Gruppe von Steinen mit Hilfe von Breitensuche
         * durchsucht.
         */
        AnalyseSchnittpunkt [] listeSteine;             // Das sollte man mit ner
        listeSteine = new AnalyseSchnittpunkt[362];     // Collection auch loesen koennen
        int momElement = 0;
        int endElement = 1;

        /* Als erstes wird der Stein, an dem begonnen wird zu suchen in die
         * Liste eingenommen
         */
        feld[xPos][yPos].setMarkiert(true);
        listeSteine[0] = feld[xPos][yPos];
        /* Jetzt solange nach Steinen suchen und diese in Liste aufnehmen,
         * bis keine Steine mehr da sind */
        do{
            /* Jetzt, wenn Nachbarsteine existieren und diese noch nicht
             * markiert sind, dann werden sie in die Liste aufgenommen.
             * Ist der Nachbar leer oder verboten, so steht dort kein Stein und
             * die Gruppe hat mindestens eine Freiheit. Sie ist somit nicht
             * fangbar.
             * Wenn der Nachbar ein Stein der gleichen Farbe ist, so muss er
             * in die Liste aufgenommen werden. Allerdings nur, wenn er noch
             * nicht markiert ist. */

            /* Wenn Stein linken Nachbarn hat*/
             if(listeSteine[momElement].getXPos()!=0){
                 if(feld[listeSteine[momElement].getXPos()-1][listeSteine[momElement].getYPos()].getBelegungswert() == Konstante.SCHNITTPUNKT_LEER ||
                    feld[listeSteine[momElement].getXPos()-1][listeSteine[momElement].getYPos()].getBelegungswert() == Konstante.SCHNITTPUNKT_VERBOTEN){
                     fangbar=false; 
                 }
                 else if(feld[listeSteine[momElement].getXPos()-1][listeSteine[momElement].getYPos()].getBelegungswert() == farbe){
                     if(feld[listeSteine[momElement].getXPos()-1][listeSteine[momElement].getYPos()].getMarkiert() == false){
                         feld[listeSteine[momElement].getXPos()-1][listeSteine[momElement].getYPos()].setMarkiert(true);
                         listeSteine[endElement] = feld[listeSteine[momElement].getXPos()-1][listeSteine[momElement].getYPos()];
                         ++endElement;
                     }
                 }
             }

             /* Wenn Stein rechten Nachbarn hat */
             if(listeSteine[momElement].getXPos()!=this.getSpielfeldGroesse()-1){
                 if(feld[listeSteine[momElement].getXPos()+1][listeSteine[momElement].getYPos()].getBelegungswert() == Konstante.SCHNITTPUNKT_LEER ||
                    feld[listeSteine[momElement].getXPos()+1][listeSteine[momElement].getYPos()].getBelegungswert() == Konstante.SCHNITTPUNKT_VERBOTEN){
                     fangbar=false; 
                 }
                 else if(feld[listeSteine[momElement].getXPos()+1][listeSteine[momElement].getYPos()].getBelegungswert() == farbe){
                     if(feld[listeSteine[momElement].getXPos()+1][listeSteine[momElement].getYPos()].getMarkiert() == false){
                         feld[listeSteine[momElement].getXPos()+1][listeSteine[momElement].getYPos()].setMarkiert(true);
                         listeSteine[endElement] = feld[listeSteine[momElement].getXPos()+1][listeSteine[momElement].getYPos()];
                         ++endElement;
                     }
                 }
             }

             /* Wenn Stein oberen Nachbarn hat (Oben und unten werden vielleicht
              * umdefiniert, ist aber egal fuer den Algorithmus!)*/
             if(listeSteine[momElement].getYPos()!=this.getSpielfeldGroesse()-1){
                 if(feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()+1].getBelegungswert() == Konstante.SCHNITTPUNKT_LEER ||
                    feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()+1].getBelegungswert() == Konstante.SCHNITTPUNKT_VERBOTEN){
                     fangbar=false; 
                 }
                 else if(feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()+1].getBelegungswert() == farbe){
                     if(feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()+1].getMarkiert() == false){
                         feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()+1].setMarkiert(true);
                         listeSteine[endElement] = feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()+1];
                         ++endElement;
                     }
                 }
             }

             /* Wenn Stein unteren Nachbarn hat */
             if(listeSteine[momElement].getYPos()!=0){
                 if(feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()-1].getBelegungswert() == Konstante.SCHNITTPUNKT_LEER ||
                    feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()-1].getBelegungswert() == Konstante.SCHNITTPUNKT_VERBOTEN){
                     fangbar=false;
                 }
                 else if(feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()-1].getBelegungswert() == farbe){
                     if(feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()-1].getMarkiert() == false){
                         feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()-1].setMarkiert(true);
                         listeSteine[endElement] = feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()-1];
                         ++endElement;
                     }
                 }
             }

             /* Jetzt wurden alle Nachbarn aufgenommen. Der naechste Stein wird
              * also untersucht. Doch zuerst muss der jetzige sauber
              * abgeschlossen werden */
             feld[listeSteine[momElement].getXPos()][listeSteine[momElement].getYPos()].setAnalysiert(true);
             ++momElement;
        }while(momElement<endElement);

        /* Alle Steine der Gruppe sind jetzt in der Liste. Jetzt wird weiter
         * gemacht, je nachdem die Steine gefangen werden konnten */
        if(fangbar==false){
            /* Steine als lebendig markieren 
             * da momElement gerade auf leeres Feld zeigt! dekrementieren */
            if(nehmen==true){
                for(int i = momElement - 1; i>=0; --i) {
                    feld[listeSteine[i].getXPos()][listeSteine[i].getYPos()].setSteinStatus(Konstante.STEIN_LEBENDIG);
                }
            }
        return 0; // wurde ja kein Stein gefangen
        }
        else {
            /* Wenn Steine gefangen werder koennen, kommt es auf den
             * Aufrufparameter der Funktion an. Je nachdem, ob Steine vom
             * Richtigen brett genommen werden sollen. */
            if(nehmen==true){
                for(int i = momElement -1; i>=0; --i) {
                    feld[listeSteine[i].getXPos()][listeSteine[i].getYPos()].setSteinStatus(Konstante.STEIN_GEFANGEN);
                    aktuellesSpielfeldCache[listeSteine[i].getXPos()][listeSteine[i].getYPos()] = Konstante.SCHNITTPUNKT_LEER;
                }
            return momElement; // momElement ist gerade die Gefangenenzahl ;)
            }
            else {
                return 1;
            }
        }
    }

    /**
     * Vereinfachte variante der Funktion versucheSteinZuNehmen. Dabei muss nicht
     * uebergeben werden, ob man die Steine nehmen soll. Dieser Wert wird true
     * gesetzt
     * @param XPos X-Position des Steins
     * @param YPos Y-Position des Steins
     * @param feld Spielfeld
     * @return
     */
    private int versucheSteinZuNehmen(int XPos, int YPos, AnalyseSchnittpunkt feld[][]){
        return versucheSteinZuNehmen(XPos, YPos, feld, true);
    }

    /**
     * Bei Ko muss ein Spielpunkt verboten werden. Zum schnelleren Zugriff wird
     * sich dieser Punkt gemerkt. Hiermit wird die X-Koordinate dieses Punktes
     * gesetzt
     * @param xPosVerboten X-Koordinate (Von 0 bis Feldgroesse-1)
     */
    private void setXPosVerboten(int xPosVerboten){
        this.xPosVerboten = xPosVerboten;
    }

    /**
     * Bei Ko muss ein Spielpunkt verboten werden. Zum schnelleren Zugriff wird
     * sich dieser Punkt gemerkt. Hiermit wird die Y-Koordinate dieses Punktes
     * gesetzt
     * @param yPosVerboten Y-Koordinate (Von 0 bis Feldgroesse-1)
     */
    private void setYPosVerboten(int yPosVerboten){
        this.yPosVerboten = yPosVerboten;
    }

    /**
     * @return X-Koordinate des Verbotenen Zugs (Von 0 bis Spielfeldgroesse-1)
     */
    private int getXPosVerboten(){
        return this.xPosVerboten;
    }

    /**
     * @return Y-Koordinate des Verbotenen Zugs (Von 0 bis Spielfeldgroesse-1)
     */
    private int getYPosVerboten(){
        return this.yPosVerboten;
    }

    /**
     * Auf dem Go-Brett ist ein Punkt immer nur fuer einen Zug verboten. Daher
     * muss der Punkt wieder freigegeben werden. Somit kann man beim naechsten
     * Zug wieder auf diesen Spielpunkt spielen.
     * Der verbotene Punkt wird natuerlich nur geloescht wenn er auch wirklich
     * existiert (x- und y-koordinaten sind groesser gleich 0)
     */
    private void loescheVerbotenenPunkt(){
        if(this.getXPosVerboten()>=0 && this.getYPosVerboten()>=0){
            if(this.aktuellesSpielfeldCache[this.getXPosVerboten()][this.getYPosVerboten()] == Konstante.SCHNITTPUNKT_VERBOTEN){
                this.aktuellesSpielfeldCache[this.getXPosVerboten()][this.getYPosVerboten()] = Konstante.SCHNITTPUNKT_LEER;
                this.setXPosVerboten(-1);
                this.setYPosVerboten(-1);
            }
        }
    }

    /**
     * Setzt den Wert fuer den verbotenen Punkt neu. Der verbotene Punkt entsteht
     * durch eine Ko-Situation.
     * @param xArray X-Koordinate (0 bis Feldgroesse-1)
     * @param yArray Y-Koordinate (0 bis Feldgroesse-1)
     */
    private void setzeVerbotenenPunkt(int xArray, int yArray){
        this.setXPosVerboten(xArray);
        this.setYPosVerboten(yArray);
    }

    /**
     * Nachdem ein Zug ausgefuehrt wurde, muss dieser in die Liste der bisherigen
     * Zuege eingetragen werden. Somit kann aus der Liste der bisherigen Zuege
     * einfach die Brettsituation wieder hergestellt werden.
     * @param xPos X-Koordinate (1-Feldgroesse)
     * @param yPos Y-Koordinate (1-Feldgroesse)
     * @param farbe Farbe des Spielers, der Zug ausgefuehrt hat
     */
    private void steinEintragen( int xPos, int yPos, int farbe ){
        this.spielZugCollection.add(new Spielzug(xPos, yPos, farbe));
        this.letzteZugnummer++;
    }

    /**
     * Nachdem Steine von einer bestimmten Farbe gefangen wurden, muss die
     * Anzahl der gefangenen Steine dieser Farbe erhoeht werden. Wenn zum Beispiel
     * 8 schwarze Steine gefangen wurden, muss die Anzahl der gefangenen schwarzen
     * Steine um 8 erhoeht werden
     * @param farbe Farbe der gefangenen Steine
     * @param zahl Anzahl der Steine die gefangen wurden
     */
    private void erhoeheGefangenenZahl(int farbe, int zahl){
        if(farbe == Konstante.SCHNITTPUNKT_SCHWARZ){
            this.gefangenenAnzahlSchwarz+=zahl;
        }
        if(farbe == Konstante.SCHNITTPUNKT_WEISS){
            this.gefangenenAnzahlWeiss+=zahl;
        }
    }
    
}
