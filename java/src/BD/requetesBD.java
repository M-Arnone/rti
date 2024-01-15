package BD;

import BD.classes.Facture;
import BD.facture.RequeteGETFACTURES;
import BD.facture.RequeteGETFACTURESsecure;
import BD.facture.RequetePAYFACTURES;
import BD.facture.RequetePAYFACTURESsecure;
import BD.login.RequeteLOGINSecure;
import Serveur.Logger;
import BD.login.RequeteLOGIN;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
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
    public boolean login(RequeteLOGINSecure requete)
    {
        String login = requete.getLogin();

        try {
            String requeteSql = "SELECT password FROM employes WHERE login = ?;";
            PreparedStatement ps = cbd.con.prepareStatement(requeteSql);
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String realPassword = rs.getString("password");
                rs.close();
                ps.close();
                return requete.VerifyPassword(realPassword);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            logger.Trace("Erreur DatabaseConnection: " + e.getMessage());
            throw new RuntimeException("Une erreur est survenue lors de l'envoi de la requete!");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
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
                String montant = rs.getString("montant");
                boolean paye = rs.getBoolean("paye");

                Facture facture = new Facture(id, idc,date, montant, paye);
                listeFactures.add(facture);
            }
            return listeFactures;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public ArrayList<Facture> getFactures(RequeteGETFACTURESsecure requete){
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
                String montant = rs.getString("montant");
                boolean paye = rs.getBoolean("paye");

                Facture facture = new Facture(id, idc,date, montant, paye);
                listeFactures.add(facture);
            }
            return listeFactures;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean payFacture(RequetePAYFACTURES requete) throws SQLException {
        int idFacture = requete.getIdFacture();
        String requeteSql = "UPDATE factures SET paye = true WHERE id = "+idFacture +";";
        int nbLignesAffectees = cbd.executeUpdate(requeteSql);
        if(nbLignesAffectees == 1) return true;
        else return false;

    }
    public boolean payFacture(int id) throws SQLException {
        int idFacture = id;
        String requeteSql = "UPDATE factures SET paye = true WHERE id = "+idFacture +";";
        int nbLignesAffectees = cbd.executeUpdate(requeteSql);
        if(nbLignesAffectees == 1) return true;
        else return false;

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
