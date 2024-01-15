package ClientPayementSecurise.controler;

import BD.classes.Facture;
import ClientPayementSecurise.model.Model;
import ClientPayementSecurise.view.ClientPayementSecuriseGUI;
import ClientPayementSecurise.view.VisaGUI;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Controler extends WindowAdapter implements ActionListener {
    private static Model m;
    private ClientPayementSecuriseGUI cpsg = new ClientPayementSecuriseGUI();
    private VisaGUI vg= new VisaGUI();
    public Controler(ClientPayementSecuriseGUI c) throws SQLException, IOException, ClassNotFoundException {cpsg = c;}
    public Controler(VisaGUI v){vg = v;}
    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == cpsg.getBtnConnexion())
        {
            System.out.println("Ouais");
            try {
                m = Model.getInstance(true);
                System.out.println("nom : " + cpsg.getTextFieldLogin().getText());
                if(m.login(cpsg.getTextFieldLogin().getText(),cpsg.getTextFieldPassword().getText()))
                {
                    System.out.println("--- Connecté ---");
                    cpsg.getBtnConnexion().setEnabled(false);
                    cpsg.getBtnDeconnexion().setEnabled(true);
                    cpsg.getBtnVoirFactures().setEnabled(true);
                    cpsg.getBtnPayerFactures().setEnabled(true);
                    JOptionPane.showMessageDialog(null, "Bienvenue, vous êtes connecté.", "Connexion réussie", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | NoSuchProviderException |
                     SQLException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException |
                     InvalidKeyException ex) {
                throw new RuntimeException(ex);
            }
        }

        if(e.getSource() == cpsg.getBtnVoirFactures()){
            System.out.println("FACTUUUUUURES");
            try {
                m = Model.getInstance(true);
            } catch (SQLException | ClassNotFoundException | IOException | NoSuchPaddingException |
                     IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException |
                     NoSuchProviderException | InvalidKeyException ex) {
                throw new RuntimeException(ex);
            }
            //int id = Integer.parseInt(cpsg.getTextFieldClient().getText());
            int id = 3;
            ArrayList<Facture> listeFacture = null;
            try {
                listeFacture = m.GetFactures(id);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            cpsg.updateFactures(listeFacture);
        }
        if(e.getSource() == cpsg.getBtnPayerFactures())
        {
            vg.setControler(this);
            vg.setVisible(true);

        }
        if(e.getSource() == vg.getAnnulerButton()){
            cpsg.setControler(this);
            vg.setVisible(false);
        }
        if(e.getSource() == vg.getConfirmerButton())
        {
            try {
                onPush_ConfirmerVisa();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

    }
    private void onPush_ConfirmerVisa() throws Exception {
        String visa = vg.getTextFieldVisa().getText();
        String nom = vg.getTextFieldTitulaire().getText();
        try {
            m = Model.getInstance(true);
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            throw new RuntimeException(ex);
        }
        int indice = cpsg.getFactureClient().getSelectedRow();
        Facture f = m.getListeFacture().get(indice);
        if(m.PayFacture(f,visa))
        {
            f.setPaye(true);
            int id = Integer.parseInt(cpsg.getTextFieldClient().getText());
            ArrayList<Facture> listeFacture = m.GetFactures(id);
            cpsg.updateFactures(listeFacture);
            vg.setVisible(false);
            cpsg.setControler(this);
            JOptionPane.showMessageDialog(null, "Le paiement a été effectué", "Paiement réussi", JOptionPane.INFORMATION_MESSAGE);
        }


    }
}
