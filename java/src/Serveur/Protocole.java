package Serveur;

import java.io.IOException;
import java.net.Socket;

public interface Protocole {
    String getNom();
    Reponse TraiteRequete(Requete r, Socket s) throws FinConnexionException;
}
