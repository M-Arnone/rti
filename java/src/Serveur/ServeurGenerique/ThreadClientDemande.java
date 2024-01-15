package Serveur.ServeurGenerique;

import Serveur.Logger;
import Serveur.ProtocoleVESPAP.VESPAPS;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ThreadClientDemande extends ThreadClient {
    SecretKey ClientKey;
    private int numero;

    public ThreadClientDemande(Protocole p, Socket csocket, Logger logger) throws IOException {
        super(p, csocket, logger);
        numero = numCourant++;
    }

    @Override
    public void run() {
        boolean interrompu = false;
        logger.Trace("TH Client (Demande) démarre...");
        while (!interrompu) {
            logger.Trace("ThreadClientDemande - Attente d'une connexion...");
            logger.Trace("ThreadClientDemande - Connexion en prise en charge");
            super.run();
            logger.Trace("TH Client (Demande) se termine.");
        }
    }
}
