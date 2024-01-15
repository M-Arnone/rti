package Serveur.ServeurGenerique;

import Serveur.Logger;
import Serveur.ProtocoleVESPAP.VESPAP;
import Serveur.ProtocoleVESPAP.VESPAPS;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.security.KeyStoreException;

public class ServeurVESPAP extends JFrame implements Logger {
    ThreadServeur threadServeur;
    private JButton demarrerServeur;
    private JCheckBox securiseCheckBox;
    private JCheckBox demande;

    public ServeurVESPAP()
    {
        super("Serveur VESPAP");
        initializeComponents();
        setupLayout();
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Pour fermer l'application correctement
        this.setVisible(true);
    }
    private void initializeComponents() {
        demarrerServeur = new JButton("Démarrer Serveur");
        securiseCheckBox = new JCheckBox("Sécurisé");
        demande = new JCheckBox("À la demande");

        demarrerServeur.addActionListener(e -> startServeur(securiseCheckBox.isSelected(),demande.isSelected()));
        securiseCheckBox.addActionListener(e -> {
            if (threadServeur != null && !threadServeur.isAlive()) {
                startServeur(securiseCheckBox.isSelected(),demande.isSelected());
            }
        });
    }
    private void setupLayout() {
        setLayout(new FlowLayout());// ou un autre LayoutManager
        add(demarrerServeur);
        add(securiseCheckBox);
        add(demande);
    }
    private void startServeur(boolean estSecurise,boolean demande) {
        int portNonSecurise = 5678;
        int portSecurise = 9123;
        try
        {
            Protocole protocole = ProtocoleFactory.getProtocole(demande,null);
            if(demande){
                System.out.println("protocole.getNom : " + protocole.getNom());
                threadServeur = new ThreadServeurDemande(portSecurise, protocole, this);
            }
            else {
                int taillePool = 10;
                threadServeur = new ThreadServeurPool(estSecurise ? portSecurise : portNonSecurise, protocole, taillePool, estSecurise, this);
            }
            threadServeur.start();
        }
        catch(NumberFormatException | IOException | KeyStoreException ex)
        {
            throw new RuntimeException(ex);
        }

    }
    @Override
    public void Trace(String message) {
        System.out.println(message);
    }
}
