package Serveur.ServeurGenerique;

import Serveur.Logger;
import Serveur.ProtocoleVESPAP.VESPAP;

import java.io.IOException;

public class ThreadClientPool extends ThreadClient
{
    private FileAttente connexionsEnAttente;
    public ThreadClientPool(VESPAP v, FileAttente file, ThreadGroup tg, Logger l) throws IOException
    {
        super(v,tg,l);
        connexionsEnAttente = file;
    }
    @Override
    public void run()
    {
        logger.Trace("ThreadClientPool - TH Client (Pool) démarre...");
        boolean interrompu = false;
        while(!interrompu)
        {
            try
            {
                logger.Trace("ThreadClientPool - Attente d'une connexion...");
                csocket = connexionsEnAttente.getConnexion();
                logger.Trace("ThreadClientPool - Connexion en prise en charge");
                super.run();
            }
            catch(InterruptedException ex)
            {
                logger.Trace("ThreadClientPool - Demande d'interruption...");
                interrompu = true;
            }
        }
        logger.Trace("ThreadClientPool - TH Client (Pool) se termine.");
    }
}
