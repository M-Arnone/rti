package Serveur.ProtocoleVESPAP;

import BD.facture.ReponseGETFACTURESsecure;
import BD.facture.ReponsePAYFACTURESsecure;
import BD.facture.RequeteGETFACTURESsecure;
import BD.facture.RequetePAYFACTURESsecure;
import BD.interfaces.Reponse;
import BD.interfaces.Requete;
import BD.login.ReponseLOGINSecure;
import BD.login.RequeteLOGINSecure;
import BD.request.requestSecure;
import BD.request.requestSecureResponse;
import BD.requetesBD;
import Serveur.Logger;
import Serveur.ServeurGenerique.FinConnexionException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.HashMap;

import Serveur.ServeurGenerique.Protocole;
import org.bouncycastle.jce.provider.*;

public class VESPAPS implements Protocole {
    private HashMap<String, Socket> clientsConnectes;
    private Logger logger;
    private requetesBD rbd;
    SecretKey cleSession;

    @Override
    public String getNom() {
        return "VESPAPS";
    }

    @Override
    public Reponse TraiteRequete(Requete requete, Socket socket) throws IOException, SQLException, FinConnexionException {
        return null;
    }


    public VESPAPS(Logger log) {
        logger = log;
        rbd = new requetesBD(logger);
        clientsConnectes = new HashMap<>();
    }

    public Reponse TraiteRequeteSecure(Requete requete, Socket socket, SecretKey ClientCle) throws FinConnexionException, SQLException, UnrecoverableKeyException, CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidKeyException, ClassNotFoundException, SignatureException {
        System.out.println("EHH PASSAGE HE");
        if (requete instanceof requestSecure)
            return TraiteRequeteSecure((requestSecure) requete, RecupereClePriveeServeur(), ClientCle);
        if (requete instanceof RequeteLOGINSecure)
            return TraiteRequeteLOGINSecure((RequeteLOGINSecure) requete, socket);
        if(requete instanceof RequeteGETFACTURESsecure)
            return TraiteRequeteFactures((RequeteGETFACTURESsecure) requete, RecupereClePriveeServeur(), cleSession);
        if(requete instanceof RequetePAYFACTURESsecure)
            return TraiteRequetePayFactures((RequetePAYFACTURESsecure) requete, RecupereClePriveeServeur(), cleSession);

        return null;
    }

    private synchronized requestSecureResponse TraiteRequeteSecure(requestSecure request, PrivateKey clePriveeServeur, SecretKey ClientCle) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, ClassNotFoundException {
        System.out.println("requestSecure request");
        byte[] cleSessionDecryptee;
        System.out.println("Clé session cryptée reçue = " + new String(request.getData1()));
        Security.addProvider(new BouncyCastleProvider());
        cleSessionDecryptee = Mycrypto.DecryptAsymRSA(clePriveeServeur, request.getData1());
        cleSession = new SecretKeySpec(cleSessionDecryptee, "DES");
        System.out.println("Decryptage asymétrique de la clé de session...");

        return new requestSecureResponse(cleSession);
    }

    public static PrivateKey RecupereClePriveeServeur() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream( System.getProperty("user.dir") + "/src/cleServeur/clePriveeServeur.ser"));
        PrivateKey cle = (PrivateKey) ois.readObject();
        ois.close();
        return cle;
    }
    public static PublicKey RecupereClePubliqueClient() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream( System.getProperty("user.dir") + "/src/cleServeur/clePubliqueClients.ser"));
        PublicKey cle = (PublicKey) ois.readObject();
        ois.close();
        return cle;
    }

    private synchronized ReponseLOGINSecure TraiteRequeteLOGINSecure(RequeteLOGINSecure requete, Socket socket) throws FinConnexionException
    {
        if(rbd.login(requete)){
            String ipPortClient = socket.getInetAddress().getHostAddress() + "/" + socket.getPort();
            //logger.Trace(requete.getLogin() + " correctement loggé de " + ipPortClient);
            clientsConnectes.put(requete.getLogin(),socket);
            return new ReponseLOGINSecure(true);
        }
        return new ReponseLOGINSecure(false);
    }
    private synchronized ReponseGETFACTURESsecure TraiteRequeteFactures(RequeteGETFACTURESsecure request, PrivateKey clePriveeServeur, SecretKey cleSession) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, SignatureException, NoSuchProviderException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        int idClient = request.getIdClient();
        PublicKey cleClient = RecupereClePubliqueClient();
        if(request.VerifySignature(cleClient)){
            System.out.println("Signature validée !");
            return new ReponseGETFACTURESsecure(rbd.getFactures(request), cleSession);
        }else {
            System.out.println("Signature invalide...");
        }
        return null;
    }

    private synchronized ReponsePAYFACTURESsecure TraiteRequetePayFactures(RequetePAYFACTURESsecure request, PrivateKey clePriveeServeur,SecretKey cleSession) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException, IOException, SQLException {
        System.out.println("Pay facture request");

        byte[] decryptedBytes = Mycrypto.DecryptSymDES(cleSession, request.getResponseCrypt());

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decryptedBytes);
        DataInputStream dis = new DataInputStream(byteArrayInputStream);

            String numCarteVisa = dis.readUTF();
            int id = dis.readInt();

            if(numCarteVisa.length() == 16){
                    System.out.println("c'est bon");
                    rbd.payFacture(id);
                    return new ReponsePAYFACTURESsecure(true, cleSession);

                }
            else{
                    System.out.println("pas bon");
                    return new ReponsePAYFACTURESsecure(false, cleSession);
                }
        }
}



