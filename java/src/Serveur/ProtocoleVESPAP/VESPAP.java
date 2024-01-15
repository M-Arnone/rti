package Serveur.ProtocoleVESPAP;

import BD.classes.Facture;
import BD.facture.ReponseGETFACTURES;
import BD.facture.ReponsePAYFACTURES;
import BD.facture.RequeteGETFACTURES;
import BD.facture.RequetePAYFACTURES;
import BD.interfaces.*;
import BD.login.ReponseLOGIN;
import BD.login.RequeteLOGIN;
import BD.logout.RequeteLOGOUT;
import BD.requetesBD;
import Serveur.*;
import Serveur.ServeurGenerique.FinConnexionException;
import Serveur.ServeurGenerique.Protocole;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class VESPAP implements Protocole {

    private HashMap<String,Socket> clientsConnectes;
    private Logger logger;
    private requetesBD rbd;

    public VESPAP(Logger log)
    {
        logger = log;
        rbd = new requetesBD(logger);
        clientsConnectes = new HashMap<>();
    }
    @Override
    public String getNom()
    {
        return "VESPAP";
    }

    public synchronized Reponse TraiteRequete(Requete requete, Socket socket) throws SQLException, FinConnexionException {
        if (requete instanceof RequeteLOGIN)
            return TraiteRequeteLOGIN((RequeteLOGIN) requete, socket);
        if (requete instanceof RequeteLOGOUT)
            TraiteRequeteLOGOUT((RequeteLOGOUT) requete);
        if(requete instanceof RequeteGETFACTURES)
            return TraiteRequeteGETFACTURES((RequeteGETFACTURES) requete);
        if(requete instanceof RequetePAYFACTURES)
            return TraiteRequetePAYFACTURES((RequetePAYFACTURES) requete);
        return null;
    }

    @Override
    public Reponse TraiteRequeteSecure(Requete requete, Socket socket, SecretKey ClientCle) throws FinConnexionException, SQLException, UnrecoverableKeyException, CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        return null;
    }

    private synchronized ReponseLOGIN TraiteRequeteLOGIN(RequeteLOGIN requete, Socket socket) throws FinConnexionException
    {
        logger.Trace("VESPAP - RequeteLOGIN reçue de " + requete.getLogin());
        if(!clientsConnectes.containsKey(requete.getLogin()))
        {
            if(rbd.login(requete)){
                String ipPortClient = socket.getInetAddress().getHostAddress() + "/" + socket.getPort();
                logger.Trace(requete.getLogin() + " correctement loggé de " + ipPortClient);
                clientsConnectes.put(requete.getLogin(),socket);
                return new ReponseLOGIN(true);
            }
            return new ReponseLOGIN(false,"Le nom d'utilisateur ou le mot passe entré est incorrect!");
        }
        else{
            logger.Trace(requete.getLogin() + " --> erreur de login");
            throw new FinConnexionException(new ReponseLOGIN(false));
        }
    }
    private synchronized ReponseGETFACTURES TraiteRequeteGETFACTURES(RequeteGETFACTURES requete)
    {
        ArrayList<Facture> listeFactures = rbd.getFactures(requete);
        return new ReponseGETFACTURES(listeFactures);
    }
    private synchronized ReponsePAYFACTURES TraiteRequetePAYFACTURES(RequetePAYFACTURES requete) throws SQLException {
            if(isVisaOk(requete.getVisa())) {
                if(rbd.payFacture(requete))
                    return new ReponsePAYFACTURES(true);
                else return new ReponsePAYFACTURES(false,"Le paiement n'a pas pu être effectué...");
            }
            return new ReponsePAYFACTURES(false,"Le numéro de carte VISA est invalide!");
    }

    private synchronized void TraiteRequeteLOGOUT(RequeteLOGOUT requete) throws FinConnexionException
    {
        logger.Trace("RequeteLOGOUT reçue de " + requete.getLogin());
        clientsConnectes.remove(requete.getLogin());
        logger.Trace(requete.getLogin() + " correctement déloggé");
        throw new FinConnexionException(null);
    }

    private boolean isVisaOk(String visa) {
    if(visa.length() == 16) {
        for(char c : visa.toCharArray()) {
            if(!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
    else return false;

    }

}
