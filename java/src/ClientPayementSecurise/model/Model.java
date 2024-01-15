package ClientPayementSecurise.model;

import BD.classes.Facture;
import BD.facture.ReponseGETFACTURESsecure;
import BD.facture.ReponsePAYFACTURESsecure;
import BD.facture.RequeteGETFACTURESsecure;
import BD.facture.RequetePAYFACTURESsecure;
import BD.interfaces.Reponse;
import BD.interfaces.Requete;
import BD.login.ReponseLOGINSecure;
import BD.login.RequeteLOGINSecure;
import BD.logout.RequeteLOGOUT;
import BD.request.requestSecure;
import Serveur.ProtocoleVESPAP.Mycrypto;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.ArrayList;

public class Model {
    public static volatile Model instance;
    private User user;
    public ArrayList<Facture> getListeFacture() {
        return listeFacture;
    }

    private ArrayList<Facture> listeFacture;

    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Socket sClient;
    private PublicKey clePubliqueServeur;

    private SecretKey cleSession;

    private Model(boolean isS) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException, ClassNotFoundException {
        connectToServer(isS);
    }

    public static Model getInstance(boolean b) throws SQLException, ClassNotFoundException, IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        if(instance == null){
            synchronized (Model.class){
                if(instance == null){
                    instance = new Model(b);
                }
            }
        }
        return instance;
    }
    public void connectToServer(boolean isSecure) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException, ClassNotFoundException {
        ConfigProperties cg = new ConfigProperties();
        if (isSecure) {
            // Génération d'une clé de session
            KeyGenerator cleGen = null;
            cleSession = null;
            try {
                Security.addProvider(new BouncyCastleProvider());
                cleGen = KeyGenerator.getInstance("DES","BC");
                cleGen.init(new SecureRandom());
                cleSession = cleGen.generateKey();
                System.out.println("Génération d'une clé de session : " + cleSession);

                clePubliqueServeur = RecupereClePubliqueServeur();
                System.out.println("Récupération clé publique du serveur : " + clePubliqueServeur);


            } catch (NoSuchAlgorithmException | NoSuchProviderException | ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
            //Version non-sécurisée
            sClient = new Socket(cg.getServeurIP(), cg.getServeurPortSecurise());
            oos = new ObjectOutputStream(sClient.getOutputStream());
            ois = new ObjectInputStream(sClient.getInputStream());
            byte[] cleSessionCrypte;
            cleSessionCrypte = Mycrypto.CryptAsymRSA(clePubliqueServeur,cleSession.getEncoded());
            System.out.println("Cryptage asymétrique de la clé de session : " + new String(cleSessionCrypte));

            requestSecure req = new requestSecure();
            req.setData1(cleSessionCrypte);
            oos.writeObject(req);
            ois.readObject();
        }
    }

    //login
    public boolean login(String username,String password) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchProviderException {
        //RequeteLOGIN requete = new RequeteLOGIN(username, password);
        RequeteLOGINSecure requete = new RequeteLOGINSecure(username, password);
        Reponse rep = traiteRequete(requete);
        System.out.println("REPPPP : " + rep);
        if(rep instanceof ReponseLOGINSecure){
            if(((ReponseLOGINSecure) rep).isSuccess()){
                user = new User(username,password);
                return true;
            }
            else return false;
        }
        return false;
    }

    public ArrayList<Facture> GetFactures(int idClient) throws Exception{
        RequeteGETFACTURESsecure requete = new RequeteGETFACTURESsecure(idClient,cleSession,RecupereClePriveClient());
        System.out.println("Requete" + requete);
        Reponse reponse = traiteRequete(requete);
        System.out.println("REPPPP : " + reponse);
        if (reponse instanceof ReponseGETFACTURESsecure) {
            byte[] facturesCryptees = ((ReponseGETFACTURESsecure) reponse).getFactureBytesCrypte();
            byte[] facturesDecrypt = Mycrypto.DecryptSymDES(cleSession, facturesCryptees);
            ByteArrayInputStream baos = new ByteArrayInputStream(facturesDecrypt);
            ObjectInputStream ois = new ObjectInputStream(baos);
            listeFacture = (ArrayList<Facture>) ois.readObject() ;
            if(listeFacture.size() > 0) {
                return listeFacture;
            }
            else  throw new Exception("Aucune facture n'a été trouvé pour ce numéro de client!");

        }
        else throw new Exception("Une erreur inconnue est survenue...");
    }
    //logout
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
    //payfactures
    public boolean PayFacture(Facture facture,String visa) throws IOException, ClassNotFoundException {
        try{
            System.out.println("---ENVOI PAY FACTURES---");
            RequetePAYFACTURESsecure requete = new RequetePAYFACTURESsecure(facture.getId(),visa,cleSession);
            Reponse reponse = traiteRequete(requete);
            if(reponse instanceof ReponsePAYFACTURESsecure){
                if(((ReponsePAYFACTURESsecure) reponse).VerifyAuthenticity(cleSession))
                {
                    System.out.println("PASSEEEEEEE");
                    if(((ReponsePAYFACTURESsecure) reponse).isSuccess())
                        return true;
                    else return false;
                }

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
    public static PublicKey RecupereClePubliqueServeur() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(System.getProperty("user.dir") + "/src/cleServeur/clePubliqueServeur.ser"));
        PublicKey cle = (PublicKey) ois.readObject();
        ois.close();
        return cle;
    }

    public static PrivateKey RecupereClePriveClient() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(System.getProperty("user.dir") + "/src/cleServeur/clePriveeClients.ser"));
        PrivateKey cle = (PrivateKey) ois.readObject();
        ois.close();
        return cle;
    }
}
