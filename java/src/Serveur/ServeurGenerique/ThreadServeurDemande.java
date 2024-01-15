package Serveur.ServeurGenerique;

import Serveur.Logger;
import Serveur.ProtocoleVESPAP.VESPAPS;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.KeyStoreException;

public class ThreadServeurDemande extends ThreadServeur {

    private ThreadGroup pool;
    private static int taillePool = 0 ;

    protected int port;

    protected Protocole protocole;
    public ThreadServeurDemande(int port, Protocole protocole, Logger logger) throws IOException, KeyStoreException {
        super(port, protocole, logger,false);
        this.protocole = protocole;
        pool = new ThreadGroup("DEMANDE");
        this.taillePool ++;
    }
    @Override
    public void run()
    {
        logger.Trace("Démarrage du TH Serveur (Demande)...");
        while(!this.isInterrupted())
        {
            Socket csocket;
        try
        {
            csocket = sSocket.accept();
            logger.Trace("Connexion acceptée, création TH Client");
            Thread th = new ThreadClientDemande(protocole,csocket,logger);
            th.start();
        }
        catch (SocketTimeoutException ex) {
            // Pour vérifier si le thread a été interrompu
            logger.Trace("Thread interrompu - Erreur I/O");
        }
        catch (IOException ex)
        {
            logger.Trace("IOException --- Erreur I/O");
        }
        }
        logger.Trace("TH Serveur (Demande) interrompu.");
        try {
            sSocket.close();
        }
        catch (IOException ex) { logger.Trace("Erreur I/O"); }
    } }