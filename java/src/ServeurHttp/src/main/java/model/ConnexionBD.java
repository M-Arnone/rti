package model;

import java.sql.*;
import java.util.Hashtable;

public class ConnexionBD {

    private static Connection connection;
    private static final String MYSQL = "MySql";
    private static final String SERVER = "127.0.0.1";
    private static final String DB_NAME = "PourStudent";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Hashtable<String, String> drivers;

    static {
        drivers = new Hashtable<>();
        drivers.put(MYSQL, "com.mysql.cj.jdbc.Driver");
    }

    // Constructeur privé pour empêcher l'instanciation directe
    private ConnexionBD() throws SQLException, ClassNotFoundException {
        initializeConnection();
    }

    // Méthode getInstance pour obtenir l'instance unique de la connexion
    public static synchronized ConnexionBD getInstance() throws ClassNotFoundException, SQLException {
        ConnexionBD con = null;
        if (connection == null || connection.isClosed()) {
            con = new ConnexionBD();
        }
        return con;
    }

    private static void initializeConnection() throws ClassNotFoundException, SQLException {
        Class.forName(drivers.get(MYSQL));

        String url = "jdbc:mysql://" + SERVER + "/" + DB_NAME;
        connection = DriverManager.getConnection(url, USER, PASSWORD);
    }

    //faire un select par exemple app dans le get
    public synchronized ResultSet executeQuery(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }

    //faire un update app apres une modif dans le post
    public synchronized int executeUpdate(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(sql);
    }

    //ferme la connexion a la base de donnée
    public synchronized void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
