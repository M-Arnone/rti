package Serveur.ProtocoleVESPAP;

import BD.classes.Facture;
import BD.facture.ReponseGETFACTURES;
import BD.facture.RequeteGETFACTURES;
import BD.interfaces.*;
import BD.login.ReponseLOGIN;
import BD.login.RequeteLOGIN;
import BD.logout.RequeteLOGOUT;
import BD.requetesBD;
import Serveur.*;
import Serveur.ServeurGenerique.FinConnexionException;
import Serveur.ServeurGenerique.Protocole;

import java.net.Socket;
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
    @Override
    public synchronized Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException
    {
        if (requete instanceof RequeteLOGIN)
            return TraiteRequeteLOGIN((RequeteLOGIN) requete, socket);
        if (requete instanceof RequeteLOGOUT)
            TraiteRequeteLOGOUT((RequeteLOGOUT) requete);
        if(requete instanceof RequeteGETFACTURES)
            return TraiteRequeteGETFACTURES((RequeteGETFACTURES) requete);
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

    private synchronized void TraiteRequeteLOGOUT(RequeteLOGOUT requete) throws FinConnexionException
    {
        logger.Trace("RequeteLOGOUT reçue de " + requete.getLogin());
        clientsConnectes.remove(requete.getLogin());
        logger.Trace(requete.getLogin() + " correctement déloggé");
        throw new FinConnexionException(null);
    }
    private synchronized ReponseGETFACTURES TraiteRequeteGETFACTURES(RequeteGETFACTURES requete)
    {
        ArrayList<Facture> listeFactures = rbd.getFactures(requete);
        return new ReponseGETFACTURES(listeFactures);
    }


}
