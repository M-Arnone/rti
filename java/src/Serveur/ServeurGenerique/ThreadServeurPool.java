package Serveur.ServeurGenerique;

import Serveur.Logger;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ThreadServeurPool extends ThreadServeur
{

    private FileAttente connexionsEnAttente;
    private ThreadGroup pool;
    private int taillePool;

    public ThreadServeurPool(int p, Protocole protocole, int taillePool, Logger logger) throws IOException
    {
        super(p, protocole, logger);
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
                new ThreadClientPool(protocole,connexionsEnAttente,pool,logger).start();
        }
        catch (IOException ex)
        {
            logger.Trace("ThreadServeurPool - Erreur I/O lors de la création du pool de threads");
            return;
        }
        //Attente des connexions
        while(!this.isInterrupted())
        {
            Socket csocket;
            try
            {
                csocket = sSocket.accept();
                logger.Trace("ThreadServeurPool - Connexion acceptée, mise en file d'attente.");
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
        logger.Trace("ThreadServeurPool - TH Serveur (Pool) interrompu.");
        pool.interrupt();
    }
}
