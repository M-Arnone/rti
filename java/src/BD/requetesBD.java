package BD;

import Serveur.Logger;
import BD.login.RequeteLOGIN;

import java.sql.*;

public class requetesBD {
    private connexionBD cbd;
    private Logger logger;
    public requetesBD(Logger l)
    {
        try
        {
            cbd = new connexionBD("127.0.0.1");
        }
        catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        this.logger = logger;
    }

    public boolean login(RequeteLOGIN requete)
    {
        String login = requete.getLogin();
        String password = requete.getPassword();
        try {
            String requeteSql = "SELECT password FROM employes WHERE login = ?;";
            PreparedStatement ps = cbd.con.prepareStatement(requeteSql);
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String realPassword = rs.getString("password");
                rs.close();
                ps.close();
                return password.equals(realPassword);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            logger.Trace("Erreur DatabaseConnection: " + e.getMessage());
            throw new RuntimeException("Une erreur est survenue lors de l'envoi de la requete!");
        }
        return false;
    }

    public void close(){
        try {
            cbd.close();
        }
        catch(SQLException e) {
            logger.Trace("Erreur requete BD : " + e.getMessage());
        }
    }

}
