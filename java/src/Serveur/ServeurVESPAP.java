package Serveur;

import Serveur.ProtocoleVESPAP.VESPAP;
import Serveur.ServeurGenerique.Protocole;
import Serveur.ServeurGenerique.ThreadServeur;
import Serveur.ServeurGenerique.ThreadServeurPool;

import java.io.IOException;

public class ServeurVESPAP implements Logger{
    ThreadServeur threadServeur;
    public ServeurVESPAP()
    {
        threadServeur = null;
        try
        {
            Protocole protocole = new VESPAP(this);
            int port = 1234;
            int taillePool = 10;
            threadServeur = new ThreadServeurPool(port,protocole,taillePool,this);
            threadServeur.start();
        }
        catch(NumberFormatException | IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }
    @Override
    public void Trace(String message) {
        System.out.println(message);
    }
}
