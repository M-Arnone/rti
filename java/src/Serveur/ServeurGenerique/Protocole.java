package Serveur.ServeurGenerique;

import java.net.Socket;

public interface Protocole {
    String getNom();
    Reponse TraiteRequete(Requete r, Socket s) throws FinConnexionException;
}
