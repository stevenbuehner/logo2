package Klassen;

import logo.LoGoApp;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author steven
 */
public class HistoryConnector {

    final private String host = "141.31.8.27";
    final private String user = "logo";
    final private String pass = "checker08";
    final private String dbName = "logo";
    protected Connection con;
    protected java.sql.Statement stmt;

    public HistoryConnector() {
        //  Proxy pro = new Proxy(Proxy.Type.HTTP, null)
    }

    public void open() throws Exception {

        try {
            LoGoApp.meinAuswertungsfenster.setStatusNachricht("Verbindung zur Datenbank wird aufgebaut ...");

            // Treiber laden und Connection erzeugen
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + host + "/" + dbName, user, pass);
//            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1/logo", "root", "");

            // Statementobjekte erzeugen
            stmt = con.createStatement();

            // Metadaten ausgeben
            if (LoGoApp.debug) {
                DatabaseMetaData dmd = con.getMetaData();

                System.out.println("");
                System.out.println("Connection URL: " + dmd.getURL());
                System.out.println("Driver Name: " + dmd.getDriverName());
                System.out.println("Driver Version: " + dmd.getDriverVersion());
                System.out.println("");
            }

        } catch (SQLException e) {
            while (e != null) {

                System.out.println(e.toString());
                System.out.println("SQL-State: " + e.getSQLState());
                System.out.println("ErrorCode: " + e.getErrorCode());

                LoGoApp.meinAuswertungsfenster.setStatusNachricht("Verbindung zur Datenbank konnte nicht aufgebaut werden ...");
                e = e.getNextException();
            }

        } catch (Exception e) {
            LoGoApp.meinAuswertungsfenster.setStatusNachricht("Verbindung zur Datenbank konnte nicht aufgebaut werden ...");
            System.err.println(e.toString());
        }
    }

    public void close() throws SQLException {
        stmt.close();
        con.close();
        LoGoApp.meinAuswertungsfenster.setStatusNachricht("Verbindung zur Datenbank wurde geschlossen ...");

    }

    public HistoryEintrag holeErstenHistoryEintrag() throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM history;");
        if (!rs.next()) {
            throw new SQLException("SELECT *: no result");
        }
        do {
            HistoryEintrag letzteNachricht = new HistoryEintrag();

            letzteNachricht.setNameSpielerSchwarz(rs.getString("nameSchwarz"));
            letzteNachricht.setNameSpielerWeiss(rs.getString("nameWeiss"));
            letzteNachricht.setPunkteSpielerSchwarz(rs.getFloat("punkteSchwarz"));
            letzteNachricht.setPunkteSpielerWeiss(rs.getFloat("punkteWeiss"));
            letzteNachricht.setDatum(rs.getDate("datum"));

            return letzteNachricht;

        } while (rs.next());
    }

    public HistoryEintrag[] holeDieBestenHistoryEintraege(int anzahl) throws SQLException {

        if (anzahl < 0) {
            anzahl = anzahl * (-1);
        }
        HistoryEintrag[] rueckgabeHistoryEintraege = new HistoryEintrag[anzahl];
        int zaehler = 0;

        ResultSet rs = stmt.executeQuery("Select nameSchwarz, nameWeiss, punkteSchwarz, punkteWeiss, datum, ABS(punkteSchwarz-punkteWeiss) AS punkteDifferenz "
                + " FROM history "
                + " ORDER BY ABS(punkteSchwarz-punkteWeiss) "
                + " LIMIT 0, " + String.valueOf(anzahl) + ";");


        if (!rs.next()) {
            throw new SQLException("SELECT *: no result");
        }
        do {
            rueckgabeHistoryEintraege[zaehler] = new HistoryEintrag();
            rueckgabeHistoryEintraege[zaehler].setNameSpielerSchwarz(rs.getString("nameSchwarz"));
            rueckgabeHistoryEintraege[zaehler].setNameSpielerWeiss(rs.getString("nameWeiss"));
            rueckgabeHistoryEintraege[zaehler].setPunkteSpielerSchwarz(rs.getFloat("punkteSchwarz"));
            rueckgabeHistoryEintraege[zaehler].setPunkteSpielerWeiss(rs.getFloat("punkteWeiss"));
            rueckgabeHistoryEintraege[zaehler].setDatum(rs.getDate("datum"));
            zaehler++;

        } while (rs.next() && zaehler < anzahl);

        return rueckgabeHistoryEintraege;
    }

    public HistoryEintrag getMessage(int pIdMessage) throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM message WHERE ID_message = " + pIdMessage + ";");
        if (!rs.next()) {
            throw new SQLException("SELECT *: no result");
        }
        HistoryEintrag letzteNachricht = new HistoryEintrag();

        letzteNachricht.setNameSpielerSchwarz(rs.getString("nameSchwarz"));
        letzteNachricht.setNameSpielerWeiss(rs.getString("nameWeiss"));
        letzteNachricht.setPunkteSpielerSchwarz(rs.getFloat("punkteSchwarz	"));
        letzteNachricht.setPunkteSpielerWeiss(rs.getFloat("punkteWeiss"));
        letzteNachricht.setDatum(rs.getDate("datum"));

        return letzteNachricht;
    }

    public void sendeNeuenHistoryEintrag(HistoryEintrag histEintr) throws SQLException {
        String sqlQuerry = "INSERT INTO history (nameSchwarz, nameWeiss, punkteSchwarz, punkteWeiss, datum) VALUES ("
                + "'" + histEintr.getNameSpielerSchwarz() + "',"
                + "'" + histEintr.getNameSpielerWeiss() + "',"
                + "'" + histEintr.getPunkteSpielerSchwarz() + "',"
                + "'" + histEintr.getPunkteSpielerWeiss() + "',"
                + "'" + histEintr.getDatum() + "');";

        int rs = stmt.executeUpdate(sqlQuerry);

        System.out.println("Abgeschickter-SQL-Befehl: " + sqlQuerry);
        System.out.println("Rückgabewert des SQL-Befehls war: " + String.valueOf(rs));
    }
}
