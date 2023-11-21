package Serveur.ServeurGenerique;

import BD.interfaces.Reponse;
import BD.interfaces.Requete;

import java.net.Socket;

public interface Protocole {
    String getNom();
    Reponse TraiteRequete(Requete r, Socket s) throws FinConnexionException;
}
