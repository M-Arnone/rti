package ClientPayement.model;

import BD.login.ReponseLOGIN;
import BD.login.RequeteLOGIN;
import ClientPayement.model.ConfigProperties;
import BD.interfaces.*;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class Model {

    public static volatile Model instance;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Socket sClient;
    private User user;
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
            System.out.println("PASSAGE");
            if(((ReponseLOGIN) rep).isValide()){
                user = new User(username,password);
                return true;
            }
            else return false;
        }
        return false;
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
