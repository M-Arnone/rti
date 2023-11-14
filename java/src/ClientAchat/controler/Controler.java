package ClientAchat.controler;

import ClientAchat.model.Model;
import ClientAchat.view.ClientAchatGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.sql.SQLException;

public class Controler implements ActionListener, WindowListener {
    Model m = Model.getInstance();
    private ClientAchatGUI _cag = new ClientAchatGUI("Acceuil");
    public Controler(ClientAchatGUI cag) throws SQLException, IOException, ClassNotFoundException {_cag = cag;}
    public Controler() throws SQLException, IOException, ClassNotFoundException {}
    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            Controler c = new Controler();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        if(e.getSource() == _cag.getLoginButton()){
            try {
                String nom = _cag.getTextFieldNom().getText();
                String pwd = _cag.getTextFieldMotDePasse().getText();
                if(nom.isEmpty() || pwd.isEmpty())
                    JOptionPane.showMessageDialog(null, "Les champs de texte doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
                else{
                    boolean newClient = false;
                    if(_cag.getNouveauClientCheckBox().isSelected())
                        newClient = true;
                    m.on_pushLogin(nom,pwd,newClient);
                }

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if(e.getSource() == _cag.getLogoutButton()){
            try {
                m.on_pushLogout();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
