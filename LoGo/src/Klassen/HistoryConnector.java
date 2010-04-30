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

    protected Connection con;
    protected java.sql.Statement stmt;

    public HistoryConnector() {
        //  Proxy pro = new Proxy(Proxy.Type.HTTP, null)
    }

    public void open() throws Exception {

        try {

            // Treiber laden und Connection erzeugen
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1/logo", "root", "");

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

                e = e.getNextException();
            }

            System.exit(1);
        } catch (Exception e) {
            System.err.println(e.toString());
            System.exit(1);
        }
    }

    public void close() throws SQLException {
        stmt.close();
        con.close();
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
