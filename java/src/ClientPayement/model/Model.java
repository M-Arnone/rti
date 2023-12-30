package ClientPayement.model;

import BD.classes.Facture;
import BD.facture.ReponseGETFACTURES;
import BD.facture.ReponsePAYFACTURES;
import BD.facture.RequeteGETFACTURES;
import BD.facture.RequetePAYFACTURES;
import BD.login.ReponseLOGIN;
import BD.login.RequeteLOGIN;
import BD.logout.RequeteLOGOUT;
import ClientPayement.model.ConfigProperties;
import BD.interfaces.*;

import javax.net.ssl.*;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Model {

    public static volatile Model instance;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Socket sClient;

    private SSLSocket sslSocket;
    private User user;

    public ArrayList<Facture> getListeFacture() {
        return listeFacture;
    }

    private ArrayList<Facture> listeFacture;

    public User getUser() {
        return user;
    }

    public void connectToServer(boolean isSecure) throws IOException {
        ConfigProperties cg = new ConfigProperties();
        if(!isSecure)
        {
            //Version non-sécurisée
            sClient = new Socket(cg.getServeurIP(),cg.getServeurPort());
            oos = new ObjectOutputStream(sClient.getOutputStream());
            ois = new ObjectInputStream(sClient.getInputStream());
        }
        else{
            //Version sécurisée
            try
            {
                // 1. Keystore
                KeyStore clientKs = KeyStore.getInstance("JKS");
                String keystorePath = "/Users/matteo/Desktop/demoCA/client_keystore.jks";
                char[] keystorePassword = "matteocli".toCharArray();
                try (FileInputStream fis = new FileInputStream(keystorePath)) {
                    clientKs.load(fis, keystorePassword);
                }

                // 2. Contexte
                SSLContext sslContext = SSLContext.getInstance("TLS");
                KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
                char[] PASSWD_KEY = "matteocli".toCharArray();
                kmf.init(clientKs, PASSWD_KEY);
                TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
                tmf.init(clientKs);
                sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

                // 3. Factory
                SSLSocketFactory SslSFac= sslContext.getSocketFactory();
                // 4. Socket
                System.out.println("Portic" + cg.getServeurPortSecurise());
                sslSocket = (SSLSocket) SslSFac.createSocket(cg.getServeurIP(), cg.getServeurPortSecurise());

                oos = new ObjectOutputStream(sslSocket.getOutputStream());
                ois = new ObjectInputStream(sslSocket.getInputStream());
            } catch (UnrecoverableKeyException | CertificateException | KeyStoreException | NoSuchAlgorithmException |
                     KeyManagementException e) {
                throw new RuntimeException(e);
            }

        }


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
            if (!((ReponseGETFACTURES) reponse).getListeFactures().isEmpty()) {
                return listeFacture = ((ReponseGETFACTURES) reponse).getListeFactures();
            }
            else throw new Exception("Aucune facture n'a été trouvé pour ce numéro de client!");

        }
        else throw new Exception("Une erreur inconnue est survenue...");
    }
    public boolean PayFacture(Facture facture,String nom, String visa) throws IOException, ClassNotFoundException {
        try{
            System.out.println("---ENVOI PAY FACTURES---");
            RequetePAYFACTURES requete = new RequetePAYFACTURES(facture.getId(),nom,visa);
            Reponse reponse = traiteRequete(requete);
            if(reponse instanceof ReponsePAYFACTURES){
                if(((ReponsePAYFACTURES) reponse).isValide())
                    return true;
                else return false;
            }
            return false;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Reponse traiteRequete(Requete requete) throws IOException, ClassNotFoundException {
        oos.writeObject(requete);
        return (Reponse) ois.readObject();
    }

    private Model(boolean isS) throws IOException {
        connectToServer(isS);
    }
    public static Model getInstance(boolean b) throws SQLException, ClassNotFoundException, IOException {
        if(instance == null){
            synchronized (Model.class){
                if(instance == null){
                    instance = new Model(b);
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {

    }
}
