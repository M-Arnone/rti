package ClientPayement.controler;

import BD.classes.Facture;
import ClientPayement.model.Model;
import ClientPayement.view.ClientPayementGUI;
import ClientPayement.view.VisaGUI;
import ClientPayement.view.choixSecure;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Controler extends WindowAdapter implements ActionListener {
    private ClientPayementGUI cpg = new ClientPayementGUI();
    private VisaGUI vg= new VisaGUI();
    private choixSecure cs = new choixSecure();
    public Controler(VisaGUI c) throws SQLException, IOException, ClassNotFoundException {vg = c;}
    public Controler(ClientPayementGUI c) throws SQLException, IOException, ClassNotFoundException {cpg = c;}
    public Controler(choixSecure css) throws SQLException, IOException, ClassNotFoundException {cs = css;}

    private static Model m;

    private boolean isSecure;

    @Override
    public void actionPerformed(ActionEvent e) {
        Controler c;
        if(e.getSource() == cs.getValiderButton())
        {
            cs.setVisible(false);
            isSecure = cs.getSecuriseCheckBox().isSelected();
            System.out.println("isSecure" + isSecure);
            try {
                m = Model.getInstance(isSecure);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            try {
                c = new Controler(cpg);
            } catch (SQLException | IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            cpg.setControler(c);
            cpg.setVisible(true);
        }
        if(e.getSource() == cpg.getBtnConnexion()){
            try {
                on_pushBtnLogin();
            } catch (SQLException | IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchProviderException ex) {
                throw new RuntimeException(ex);
            }
        }
        if(e.getSource() == cpg.getBtnDeconnexion())
        {
            try {
                on_pushBtnLogout();
            } catch (SQLException | IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
        if(e.getSource() == cpg.getBtnVoirFactures())
        {
            try {
                onPush_BtnVoirFactures();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        if(e.getSource() == cpg.getBtnPayerFactures())
        {
            vg.setControler(this);
            vg.setVisible(true);

        }
        if(e.getSource() == vg.getAnnulerButton()){
            cpg.setControler(this);
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

    private void on_pushBtnLogin() throws SQLException, IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchProviderException {
        String login = cpg.getTextFieldLogin().getText();
        String pwd = cpg.getTextFieldPassword().getText();
        if(login.isEmpty() || pwd.isEmpty())
            JOptionPane.showMessageDialog(null, "Les champs de texte doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
        else{
            m = Model.getInstance(isSecure);
            if(m.login(login,pwd)){
                cpg.getBtnConnexion().setEnabled(false);
                cpg.getBtnDeconnexion().setEnabled(true);
                cpg.getBtnVoirFactures().setEnabled(true);
                cpg.getBtnPayerFactures().setEnabled(true);
                JOptionPane.showMessageDialog(null, "Bienvenue, vous êtes connecté.", "Connexion réussie", JOptionPane.INFORMATION_MESSAGE);

            }

            else  JOptionPane.showMessageDialog(null, "Erreur de connexion", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void on_pushBtnLogout() throws SQLException, IOException, ClassNotFoundException {
        m = Model.getInstance(isSecure);
        if(!m.getUser().isConnected())
            JOptionPane.showMessageDialog(null, "Utilisateur non-connecté", "Erreur", JOptionPane.ERROR_MESSAGE);
        else
        {
            m.logout();
            cpg.getBtnConnexion().setEnabled(true);
            DefaultTableModel model = (DefaultTableModel) cpg.getFactureClient().getModel();
            model.setRowCount(0);
            cpg.getTextFieldClient().setText("");
            cpg.getTextFieldLogin().setText("");
            cpg.getTextFieldPassword().setText("");
        }
    }
    private void onPush_BtnVoirFactures() throws Exception {
        m = Model.getInstance(isSecure);
        int id = Integer.parseInt(cpg.getTextFieldClient().getText());
        ArrayList<Facture> listeFacture = m.GetFactures(id);
        cpg.updateFactures(listeFacture);
    }

    private void onPush_ConfirmerVisa() throws Exception {
        String visa = vg.getTextFieldVisa().getText();
        String nom = vg.getTextFieldTitulaire().getText();
        try {
            m = Model.getInstance(isSecure);
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            throw new RuntimeException(ex);
        }
        int indice = cpg.getFactureClient().getSelectedRow();
        Facture f = m.getListeFacture().get(indice);
        if(m.PayFacture(f,nom,visa))
        {
            f.setPaye(true);
            int id = Integer.parseInt(cpg.getTextFieldClient().getText());
            ArrayList<Facture> listeFacture = m.GetFactures(id);
            cpg.updateFactures(listeFacture);
            vg.setVisible(false);
            cpg.setControler(this);
            JOptionPane.showMessageDialog(null, "Le paiement a été effectué", "Paiement réussi", JOptionPane.INFORMATION_MESSAGE);
        }


    }

}


