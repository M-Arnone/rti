package BD;

import BD.classes.Facture;
import BD.facture.RequeteGETFACTURES;
import Serveur.Logger;
import BD.login.RequeteLOGIN;

import java.sql.*;
import java.util.ArrayList;

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
    public ArrayList<Facture> getFactures(RequeteGETFACTURES requete){
        int idClient = requete.getIdClient();
        try
        {
            String requeteSql = "SELECT id,idclient, date, montant, paye FROM factures WHERE idClient = " + idClient + ";";
            ResultSet rs = cbd.getTuple(requeteSql);
            ArrayList<Facture> listeFactures = new ArrayList<>();
            while(rs.next()) {
                int id = rs.getInt("id");
                int idc = rs.getInt("idclient");
                Date date = rs.getDate("date");
                float montant = rs.getFloat("montant");
                boolean paye = rs.getBoolean("paye");

                Facture facture = new Facture(id, idc,date, montant, paye);
                listeFactures.add(facture);
            }
            return listeFactures;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
