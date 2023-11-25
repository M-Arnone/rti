package Serveur.ServeurGenerique;

import BD.interfaces.Reponse;
import BD.interfaces.Requete;
import Serveur.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

public abstract class ThreadClient extends Thread {
    protected Protocole protocole;
    protected Socket csocket;
    protected Logger logger;
    private int numero;
    private static int numCourant = 1;
    public ThreadClient(Protocole p, Socket cs,Logger l) throws IOException
    {
        super("TH Client" + numCourant + "(protocole=" + p.getNom()+")");
        this.protocole = p;
        this.csocket = cs;
        this.logger = l;
        this.numero = numCourant++;
    }

    public ThreadClient(Protocole p, ThreadGroup tg, Logger l) throws IOException
    {
        super(tg,"TH Client" + numCourant + "(protocole=" + p.getNom()+")");
        this.protocole = p;
        this.csocket = null;
        this.logger = l;
        this.numero = numCourant++;
    }
    @Override
    public void run()
    {
        try
        {
            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;
            try
            {
                ois = new ObjectInputStream(csocket.getInputStream());
                oos = new ObjectOutputStream(csocket.getOutputStream());
                while(true)
                {
                    Requete requete = (Requete) ois.readObject();
                    Reponse reponse = protocole.TraiteRequete(requete,csocket);
                    oos.writeObject(reponse);
                }
            }
            catch(FinConnexionException ex)
            {
                logger.Trace("Thread Client - Fin connexion demandée par le protocle");
                if(oos != null && ex.getReponse() != null)
                        oos.writeObject(ex.getReponse());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        catch (IOException ex) { logger.Trace("Erreur I/O"); }
        catch (ClassNotFoundException ex) { logger.Trace("Erreur requete invalide");}
        finally {
            try{csocket.close();}
            catch (IOException ex){logger.Trace("Erreur fermeture socket");}
        }
    }
}
