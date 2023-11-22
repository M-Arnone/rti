package ClientPayement.controler;

import BD.classes.Facture;
import ClientPayement.model.Model;
import ClientPayement.view.ClientPayementGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Controler extends WindowAdapter implements ActionListener {
    private ClientPayementGUI cpg;
    public Controler(ClientPayementGUI c){cpg = c;}

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == cpg.getBtnConnexion()){
            try {
                on_pushBtnLogin();
            } catch (SQLException | IOException | ClassNotFoundException ex) {
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
    }

    private void on_pushBtnLogin() throws SQLException, IOException, ClassNotFoundException {
        String login = cpg.getTextFieldLogin().getText();
        String pwd = cpg.getTextFieldPassword().getText();
        if(login.isEmpty() || pwd.isEmpty())
            JOptionPane.showMessageDialog(null, "Les champs de texte doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
        else{
            Model m = Model.getInstance();
            if(m.login(login,pwd))
                cpg.getBtnConnexion().setEnabled(false);
            else  JOptionPane.showMessageDialog(null, "Erreur de connexion", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void on_pushBtnLogout() throws SQLException, IOException, ClassNotFoundException {
        Model m = Model.getInstance();
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
        Model m = Model.getInstance();
        int id = Integer.parseInt(cpg.getTextFieldClient().getText());
        ArrayList<Facture> listeFacture = m.GetFactures(id);
        cpg.updateFactures(listeFacture);
    }

}


