package Serveur.ServeurGenerique;

import BD.interfaces.Reponse;
import BD.interfaces.Requete;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.SQLException;

public interface Protocole {
    String getNom();
    Reponse TraiteRequete(Requete requete, Socket socket) throws IOException, SQLException, FinConnexionException;
    Reponse TraiteRequeteSecure(Requete requete, Socket socket, SecretKey ClientCle) throws FinConnexionException, SQLException, UnrecoverableKeyException, CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidKeyException, ClassNotFoundException, SignatureException;
}
