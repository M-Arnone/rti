package Serveur.ServeurGenerique;

import Serveur.Logger;
import Serveur.ProtocoleVESPAP.VESPAP;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.*;
import java.security.cert.CertificateException;

public class ThreadServeurPool extends ThreadServeur
{

    private FileAttente connexionsEnAttente;
    private ThreadGroup pool;
    private int taillePool;

    public ThreadServeurPool(int p, Protocole v, int taillePool, boolean iss, Logger logger) throws IOException, KeyStoreException {
        super(p, v, logger,iss);
        connexionsEnAttente = new FileAttente();
        pool = new ThreadGroup("POOL");
        this.taillePool = taillePool;
    }
    @Override
    public void run()
    {
        logger.Trace("ThreadServeurPool - Démarrage du TH Serveur (Pool)...");
        //Création du pool de threads
        try
        {
            for(int i=0;i<taillePool;i++)
                new ThreadClientPool(new VESPAP(logger),connexionsEnAttente,pool,logger).start();
        }
        catch (IOException ex)
        {
            logger.Trace("ThreadServeurPool - Erreur I/O lors de la création du pool de threads");
            return;
        }
        //Attente des connexions
        while(!this.isInterrupted())
        {
            if(!isSecure)
            {
                //Version non-sécurisé
                Socket csocket;
                try
                {
                    csocket = sSocket.accept();
                    logger.Trace("ThreadServeurPool - Connexion acceptée (non-sécurisée), mise en file d'attente.");
                    connexionsEnAttente.addConnexion(csocket);
                }
                catch (SocketTimeoutException ex)
                {
                    logger.Trace("ThreadServeurPool - Thread interrompu");
                }
                catch (IOException ex)
                {
                    logger.Trace("Erreur I/O");
                }
            }
            else{
                //Version sécurisée
                SSLSocket sslSocket = null;
                try {
                    sslSocket = (SSLSocket)SslSSocket.accept();
                    logger.Trace("ThreadServeurPool - Connexion acceptée (sécurisée), mise en file d'attente.");
                    connexionsEnAttente.addConnexion(sslSocket);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }





        }
        logger.Trace("ThreadServeurPool - TH Serveur (Pool) interrompu.");
        pool.interrupt();
    }
}
