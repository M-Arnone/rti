package Serveur;

import java.io.IOException;
import java.net.ServerSocket;

public abstract class ThreadServeur extends Thread
{
    protected int port;
    protected Protocole protocole;
    protected Logger logger;
    protected ServerSocket ssocket;
    public ThreadServeur(int p, Protocole pro, Logger l) throws IOException
    {
        super("TH Serveur (port=" + p + ",protocole=" + pro.getNom() + ")");
        this.port = p;
        this.protocole = pro;
        this.logger = l;
        ssocket = new ServerSocket(p);
    }
}
