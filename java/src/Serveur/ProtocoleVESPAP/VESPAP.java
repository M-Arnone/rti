package Serveur.ProtocoleVESPAP;

import Serveur.*;
import Serveur.ServeurGenerique.FinConnexionException;
import Serveur.ServeurGenerique.Protocole;
import Serveur.ServeurGenerique.Reponse;
import Serveur.ServeurGenerique.Requete;

import java.net.Socket;
import java.util.HashMap;

public class VESPAP implements Protocole {
    private HashMap<String,String> passwords;
    private HashMap<String,Socket> clientsConnectes;
    private Logger logger;

    public VESPAP(Logger log)
    {
        passwords = new HashMap<>();
        passwords.put("wagner","abcd");
        passwords.put("charlet","1234");
        passwords.put("w","1");
        logger = log;
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
        return null;
    }
    private synchronized ReponseLOGIN TraiteRequeteLOGIN(RequeteLOGIN requete, Socket socket) throws FinConnexionException
    {
        logger.Trace("VESPAP - RequeteLOGIN reçue de " + requete.getLogin());
        String password = passwords.get(requete.getLogin());
        if (password != null)
            if (password.equals(requete.getPassword()))
            {
                String ipPortClient = socket.getInetAddress().getHostAddress() + "/" + socket.getPort();
                logger.Trace(requete.getLogin() + " correctement loggé de " + ipPortClient);
                clientsConnectes.put(requete.getLogin(),socket);
                return new ReponseLOGIN(true);
            }
        logger.Trace(requete.getLogin() + " --> erreur de login");
        throw new FinConnexionException(new ReponseLOGIN(false));
    }

    private synchronized void TraiteRequeteLOGOUT(RequeteLOGOUT requete) throws FinConnexionException
    {
        logger.Trace("RequeteLOGOUT reçue de " + requete.getLogin());
        clientsConnectes.remove(requete.getLogin());
        logger.Trace(requete.getLogin() + " correctement déloggé");
        throw new FinConnexionException(null);
    }

}
