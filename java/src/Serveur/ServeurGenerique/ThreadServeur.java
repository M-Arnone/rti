package Serveur.ServeurGenerique;

import Serveur.Logger;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.security.*;
import java.security.cert.CertificateException;

public abstract class ThreadServeur  extends Thread{
    protected int port;
    protected Protocole protocole;
    protected Logger logger;
    protected boolean isSecure;
    protected ServerSocket sSocket;
    protected SSLServerSocket SslSSocket;

    public ThreadServeur(int port, Protocole protocole, Logger logger,boolean isS) throws IOException, IOException, KeyStoreException {
        super("TH Serveur (port=" + port + ",protocole=" + protocole.getNom() + ")");
        this.port = port;
        this.protocole = protocole;
        this.logger = logger;
        this.isSecure = isS;
        if(!isSecure)
            sSocket = new ServerSocket(port);
        else{
            try{
                // 1. Keystore
                KeyStore ServerKs = KeyStore.getInstance("JKS");
                String FICHIER_KEYSTORE = "/Users/matteo/Desktop/mykeystore.jks";
                char[] PASSWD_KEYSTORE = "beaugosseser".toCharArray();
                FileInputStream ServerFK = new FileInputStream(FICHIER_KEYSTORE);
                ServerKs.load(ServerFK, PASSWD_KEYSTORE);
                // 2. Contexte
                SSLContext SslC = SSLContext.getInstance("TLS");
                KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
                char[] PASSWD_KEY = "beaugosseser".toCharArray();
                kmf.init(ServerKs, PASSWD_KEY);
                TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
                tmf.init(ServerKs);
                SslC.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
                // 3. Factory
                SSLServerSocketFactory SslSFac= SslC.getServerSocketFactory();
                // 4. Socket
                SslSSocket = (SSLServerSocket) SslSFac.createServerSocket(port);
            }
            catch (UnrecoverableKeyException | CertificateException | NoSuchAlgorithmException |
                     KeyManagementException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
