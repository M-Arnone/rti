package ClientPayement.model;

import BD.classes.Facture;
import BD.facture.ReponseGETFACTURES;
import BD.facture.RequeteGETFACTURES;
import BD.login.ReponseLOGIN;
import BD.login.RequeteLOGIN;
import BD.logout.RequeteLOGOUT;
import ClientPayement.model.ConfigProperties;
import BD.interfaces.*;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class Model {

    public static volatile Model instance;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Socket sClient;
    private User user;
    private ArrayList<Facture> listeFacture;

    public User getUser() {
        return user;
    }

    public void connectToServer() throws IOException {
        ConfigProperties cg = new ConfigProperties();
        sClient = new Socket(cg.getServeurIP(),cg.getServeurPort());
        oos = new ObjectOutputStream(sClient.getOutputStream());
        ois = new ObjectInputStream(sClient.getInputStream());
    }

    public boolean login(String username,String password) throws IOException, ClassNotFoundException {
        RequeteLOGIN requete = new RequeteLOGIN(username, password);
        Reponse rep = traiteRequete(requete);
        if(rep instanceof ReponseLOGIN){
            if(((ReponseLOGIN) rep).isValide()){
                user = new User(username,password);
                return true;
            }
            else return false;
        }
        return false;
    }
    public void logout() throws IOException, ClassNotFoundException {
        try{
            RequeteLOGOUT requete = new RequeteLOGOUT(user.getLogin());
            user.setConnected(false);
            oos.writeObject(requete);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Facture> GetFactures(int idClient) throws Exception {
        RequeteGETFACTURES requete = new RequeteGETFACTURES(idClient);
        Reponse reponse = traiteRequete(requete);
        if (reponse instanceof ReponseGETFACTURES) {
            if (((ReponseGETFACTURES) reponse).getListeFactures().size() > 0) {
                return listeFacture = ((ReponseGETFACTURES) reponse).getListeFactures();
            }
            else throw new Exception("Aucune facture n'a été trouvé pour ce numéro de client!");

        }
        else throw new Exception("Une erreur inconnue est survenue...");
    }

    public Reponse traiteRequete(Requete requete) throws IOException, ClassNotFoundException {
        oos.writeObject(requete);
        return (Reponse) ois.readObject();
    }

    public Model() throws IOException {
        connectToServer();
    }
    public static Model getInstance() throws SQLException, ClassNotFoundException, IOException {
        if(instance == null){
            synchronized (ClientAchat.model.Model.class){
                if(instance == null){
                    instance = new Model();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {

    }
}
