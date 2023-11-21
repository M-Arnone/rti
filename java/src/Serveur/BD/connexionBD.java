package Serveur.BD;
import java.sql.*;
import java.util.Hashtable;
public class connexionBD {
    Connection con = null;
    public connexionBD(String ip) throws ClassNotFoundException, SQLException{
        try {
            Class leDriver = Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Obtention du driver OK...");
            con = DriverManager.getConnection("jdbc:mysql://"+ip+"/PourStudent", "root", "");
            System.out.println("Connexion à la BD PourStudent OK...");

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public synchronized ResultSet getTuple(String sql) throws SQLException {
        Statement statement = con.createStatement();
        return statement.executeQuery(sql);
    }
    public synchronized int executeUpdate(String sql) throws SQLException {
        Statement statement = con.createStatement();
        return statement.executeUpdate(sql);
    }
    public synchronized void close() throws SQLException {
        if(con != null && !con.isClosed()) {
            try {
                con.close();
                System.out.println("Connexion fermée.");
            } catch (SQLException e) {
                System.out.println("Exception de fermeture");
            }
        }
    }

    public static void main(String[] args) throws SQLException {

    }
}
