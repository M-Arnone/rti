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
    private ClientAchatGUI _cag = new ClientAchatGUI("Acceuil");
    public Controler(ClientAchatGUI cag){_cag = cag;}
    public Controler(){}
    @Override
    public void actionPerformed(ActionEvent e) {
        Controler c = new Controler();
        if(e.getSource() == _cag.getLoginButton()){
            try {
                String nom = _cag.getTextFieldNom().getText();
                String pwd = _cag.getTextFieldMotDePasse().getText();
                if(nom.isEmpty() || pwd.isEmpty())
                    JOptionPane.showMessageDialog(null, "Les champs de texte doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
                else{
                    Model m = Model.getInstance();
                    boolean newClient = false;
                    if(_cag.getNouveauClientCheckBox().isSelected())
                        newClient = true;
                    m.setLogin(nom,pwd,newClient);
                }

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
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
