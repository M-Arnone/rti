package Serveur.ServeurGenerique;

import BD.interfaces.Reponse;
import BD.interfaces.Requete;
import Serveur.Logger;
import Serveur.ProtocoleVESPAP.VESPAPS;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.SQLException;

public abstract class ThreadClient extends Thread {
    protected Protocole protocole;
    protected Socket csocket;
    protected Logger logger;
    private int numero;
    protected static int numCourant = 1;
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
                System.out.println("ois : " + ois);
                System.out.println("oos : " + oos);
                while(true)
                {
                    try{
                        Requete requete = (Requete) ois.readObject();
                        Reponse reponse;
                        if (protocole instanceof VESPAPS) {
                            // Pour les connexions sécurisées
                            reponse = protocole.TraiteRequeteSecure(requete, csocket, null);
                        } else {
                            // Pour les connexions non-sécurisées
                            reponse = protocole.TraiteRequete(requete, csocket);
                        }
                        oos.writeObject(reponse);
                    }
                    catch (IOException e){
                        System.out.println("[" + getName() + "]IOException : " + e.getMessage());
                    } catch (SignatureException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
            catch (SQLException | UnrecoverableKeyException | FinConnexionException | NoSuchPaddingException |
                   IllegalBlockSizeException | CertificateException | KeyStoreException | NoSuchAlgorithmException |
                   BadPaddingException | NoSuchProviderException | InvalidKeyException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        catch (IOException ex) { logger.Trace("Erreur I/O"); } finally {
            try{csocket.close();}
            catch (IOException ex){logger.Trace("Erreur fermeture socket");}
        }
    }
}